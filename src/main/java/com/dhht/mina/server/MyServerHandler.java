package com.dhht.mina.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhht.common.CRC7;
import com.dhht.common.DaoUtil;
import com.dhht.common.MapUtil;
import com.dhht.common.SpringBeanUtil;
import com.dhht.mina.job.SessionBean;
import com.dhht.service.weixin.PollDataService;
import com.dhht.service.weixin.WxuserService;

/**
 * mina消息处理业务层
 */
public class MyServerHandler extends IoHandlerAdapter {
	public static Logger logger = Logger.getLogger(MyServerHandler.class);
	
	@Autowired
	private SessionBean sessionBean;
	
	private WxuserService wxuserService = (WxuserService)SpringBeanUtil.getBean("wxuserService");
	private PollDataService pollDataService = (PollDataService)SpringBeanUtil.getBean("pollDataService");
	 
	//异常状态
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("exceptionCaught"+session.toString()+"cause:"+cause);
	}
	
    //服务器接收
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		byte[] msg = (byte[])message;
		String msgStr = SendDataUtil.BinaryToHexString(msg);
//		System.out.println("服务器接收："+msgStr);
//		System.out.println("服务器地址："+session.getLocalAddress());
//		System.out.println("终端地址    ："+session.getRemoteAddress());
		if(!StringUtils.isBlank(msgStr)){
			//获取mac
			final String machineMac = msgStr.substring(0,12);
//			System.out.println("mac地址："+machineMac);
			sessionBean.putSession(machineMac.toUpperCase(), session);
			//判断报头是否合法GB2312
			if("55AA".equals(msgStr.substring(12,16))){
				String func = msgStr.substring(16,18);
//				System.out.println("功能码："+func);
				String length = msgStr.substring(18,20);
//				System.out.println("十六进制长度："+length);
				String realdata="";
				String data = "";
				if(!"00".equals(length)){
					int len = DaoUtil.parseInt(SendDataUtil.hexStringToByte(length)[0]+"", 0);
//					System.out.println("十进制长度："+len);
					data = msgStr.substring(20,20+len*2);
//					System.out.println("十六进制数据："+data);
					if(len>0){
						realdata = SendDataUtil.HexStringToString(data);
//						System.out.println("解析后的数据："+realdata);
					}
				}else{
//					System.out.println("十进制长度："+0);
				}
				//校验：功能码+长度+数据
				if(msg[msg.length-1]==CRC7.CheckCode_CRC7((SendDataUtil.hexStringToByte(func+length+data)))){
//					System.out.println("效验通过++++++++++++++++++++++");
				}else{
					System.out.println("效验失败"+msgStr+"----------------------");
					SendDataUtil.sendData(session, SendDataUtil.replayFail(), "0F");
				}
				if("11".equals(func)){
					//终端发来心跳包
					System.out.println("收到心跳"+machineMac+"----------------------");
					SendDataUtil.sendData(session,SendDataUtil.heartbeat(),"01");//服务器回复心跳
					pollDataService.updateOnlineBySql(machineMac, 1);   //收到心跳，将关于设备数据的状态
					
				}else if("12".equals(func)){   //发送开门指令
					//pollDataService.delDataByMac(machineMac);
					//终端回复开门成功与否
					if("00".equals(realdata)){//成功
						wxuserService.processOpenSuccess(session, machineMac);
					}else{//失败
						
					}
				}else if("13".equals(func)){
					//终端请求二维码
					//http://weixin.qq.com/r/Yi8LE7nE9OSLrf1w93pw
					//http://mj.donghuahongtai.com/weixinpage/ope?random=123456MAC=123456123456
					//http://mj.donghuahongtai.com/w/o?M=123456123456T=123456781234567812345678
					//01.12--改成：http://mj.donghuahongtai.com/w/o?M=123456123456
					//String random = MapUtil.getRandom(6);
					String sigInfo = SendDataUtil.SIGNINFO;
					sigInfo = sigInfo + "?M="+ machineMac;
//					System.out.println(sigInfo);
					// 更新数据库中的随机数
					//wxuserService.updateDynamicRandom(random, machineMac);
					try {
						SendDataUtil.sendData(session, SendDataUtil.replayQcode(sigInfo.getBytes("GB2312")),"03");// 回复二维码
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if("14".equals(func)){
					//终端回复授权变更成功
					Long lastTaskTime = (Long)session.getAttribute("lastTaskTime");
					String lastTask = String.valueOf(session.getAttribute("lastTask"));
					if(lastTaskTime!=null&&lastTaskTime>0&&!StringUtils.isBlank(lastTask)&&!"null".equals(lastTask)&&"04".equals(lastTask)){
						System.out.println("==============================>收到授权变更反馈");
						session.setAttribute("lastTaskResult", "1");//0未处理  1成功  2失败
					}
				}else if("15".equals(func)){
					//终端回复接收广告成功
					Long lastTaskTime = (Long)session.getAttribute("lastTaskTime");
					String lastTask = String.valueOf(session.getAttribute("lastTask"));
					if(lastTaskTime!=null&&lastTaskTime>0&&!StringUtils.isBlank(lastTask)&&!"null".equals(lastTask)&&"05".equals(lastTask)){
						System.out.println("==============================>收到接收广告反馈");
						session.setAttribute("lastTaskResult", "1");//0未处理  1成功  2失败
					}
				}else if("16".equals(func)){
					//终端回复同步时间成功
					Long lastTaskTime = (Long)session.getAttribute("lastTaskTime");
					String lastTask = String.valueOf(session.getAttribute("lastTask"));
					if(lastTaskTime!=null&&lastTaskTime>0&&!StringUtils.isBlank(lastTask)&&!"null".equals(lastTask)&&"06".equals(lastTask)){
						System.out.println("==============================>收到同步时间反馈");
						session.setAttribute("lastTaskResult", "1");//0未处理  1成功  2失败
					}
				}else if("17".equals(func)){
					//上传记录,服务器接收记录:8字节时间+3字节卡号
					//01.12改：加1字节标志：0为刷卡开门，1为密码开门。
					String real = SendDataUtil.HexStringToString(data.substring(0, 16));
					String flag = data.substring(22, 24);
					if("00".equals(flag)){//刷卡开门
				/*
						String first = Integer.parseInt(data.substring(16, 18), 16)+"";
						String completion1 ="";
						if(first.length()<3){
							for(int i=1;i<=3-first.length();i++){
								completion1 += "0";
							}
							first = completion1+first;
						}
						real+=first;
						String second = Integer.parseInt(data.substring(18, 22), 16)+"";
						String completion2 ="";
						if(second.length()<5){
							for(int i=1;i<=5-second.length();i++){
								completion2 += "0";
							}
							second = completion2+second;
						}
						real+=second;
						real+=flag;
				*/
						String cardNo = data.substring(16, 22);  //门卡号
						String dateStr = real+cardNo+flag;
						System.out.println("服务器接收-刷卡-开门记录:"+dateStr);
						if(dateStr.length()==24){
							//服务器回复收到上传记录
							SendDataUtil.sendData(session, SendDataUtil.replayGetRecord(),"07");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String realDate = ""+real+cardNo+flag;
							String dateString = realDate.substring(0, 4)+"-"+realDate.substring(4, 6)+"-"
							+realDate.substring(6,8)+" "+realDate.substring(8, 10)+":"+realDate.subSequence(10, 12)+":"+realDate.subSequence(12, 14);
							//String cardNo = real.substring(16, 24);
							Date date = sdf.parse(dateString);
							//新增openDoorRecord
							wxuserService.uploadOpenDoorRecord(cardNo,machineMac,date);
						}
					}else if("01".equals(flag)){//密码开门
						
						String secret = data.substring(16, 22);//动态密码
						real+=secret;//时间+动态密码
						System.out.println("服务器接收-密码-开门记录:"+real);
						if(real.length()==22){
							SendDataUtil.sendData(session, SendDataUtil.replayGetRecord(),"07");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dateString = real.substring(0, 4)+"-"+real.substring(4, 6)+"-"
							+real.substring(6,8)+" "+real.substring(8, 10)+":"+real.subSequence(10, 12)+":"+real.subSequence(12, 14);
							Date date = sdf.parse(dateString);
							wxuserService.uploadSecretOpenDoorRecord(secret, machineMac, date);  //离线上传记录(一般离线记录/访客离线记录)
						}
					}
					
				}else if("18".equals(func)){
					//终端回复新增卡号成功
					Long lastTaskTime = (Long)session.getAttribute("lastTaskTime");
					String lastTask = String.valueOf(session.getAttribute("lastTask"));
					if(lastTaskTime!=null&&lastTaskTime>0&&!StringUtils.isBlank(lastTask)&&!"null".equals(lastTask)&&"08".equals(lastTask)){
						System.out.println("==============================>收到新增卡号反馈");
						session.setAttribute("lastTaskResult", "1");//0未处理  1成功  2失败
					}
				}else if("19".equals(func)){
					//终端回复删除卡号成功
					Long lastTaskTime = (Long)session.getAttribute("lastTaskTime");
					String lastTask = String.valueOf(session.getAttribute("lastTask"));
					if(lastTaskTime!=null&&lastTaskTime>0&&!StringUtils.isBlank(lastTask)&&!"null".equals(lastTask)&&"09".equals(lastTask)){
						System.out.println("==============================>收到删除卡号反馈");
						session.setAttribute("lastTaskResult", "1");//0未处理  1成功  2失败
					}
				}else if("1A".equals(func)){
					//终端回复清空卡号成功
					Long lastTaskTime = (Long)session.getAttribute("lastTaskTime");
					String lastTask = String.valueOf(session.getAttribute("lastTask"));
					if(lastTaskTime!=null&&lastTaskTime>0&&!StringUtils.isBlank(lastTask)&&!"null".equals(lastTask)&&"0A".equals(lastTask)){
						System.out.println("==============================>收到清空卡号反馈");
						session.setAttribute("lastTaskResult", "1");//0未处理  1成功  2失败
					}
				}else if("1B".equals(func)){
					//终端回复重启
					System.out.println("==============================>收到重启反馈");
				}else if("1F".equals(func)){
					//告知服务器效验失败
					System.out.println("告知服务器效验失败");
					wxuserService.processMatchFail(session, machineMac);
				}
			}
		}
	}
    
	//向客服端发送消息后会调用此方法
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("服务器发送消息成功..."+session.toString());
		super.messageSent(session, message);
	}

	//关闭与客户端的连接时会调用此方法
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("服务器与客户端断开连接..."+session.toString());
		System.out.println("断开连接");
		super.sessionClosed(session);
	}

	//服务器与客户端创建连接
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("服务器与客户端创建连接..."+session.toString());
		System.out.println("服务器与客户端创建连接");
		super.sessionCreated(session);
	}

	//服务器与客户端连接打开
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("服务器与客户端连接打开..."+session.toString());
		System.out.println("服务器与客户端连接打开");
		super.sessionOpened(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.info("服务器进入空闲状态...");
		System.out.println("进入空闲");
		session.close(true);
		super.sessionIdle(session, status);
	}
	
}