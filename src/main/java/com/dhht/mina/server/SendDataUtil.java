package com.dhht.mina.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhht.common.CRC7;
import com.dhht.common.SpringBeanUtil;
import com.dhht.common.TeaEncodeUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.entity.weixin.PollData;
import com.dhht.service.device.DeviceService;
import com.dhht.service.weixin.PollDataService;
import com.dhht.service.weixin.WxuserService;

/**
 * 服务器向终端通讯
 * @author Administrator
 *
 */
public class SendDataUtil {
	private static byte[] HEAD = new byte[]{(byte)0xAA,(byte)0x55};//报头
	private static byte FUNC_HEARTBEAT= (byte)0x01;//心跳--功能码
	private static byte FUNC_OPENDOOR= (byte)0x02;//开门指令--功能码
	private static byte FUNC_REPLAYQCODE= (byte)0x03;//回复二维码--功能码
	private static byte FUNC_CHANGEAUTH= (byte)0x04;//授权模式变更--功能码
	private static byte FUNC_NEWS= (byte)0x05;//广告--功能码
	private static byte FUNC_MATCHTIME= (byte)0x06;//时间同步--功能码
	private static byte FUNC_UPLOADRECORD= (byte)0x07;//回复收到上传记录--功能码
	private static byte FUNC_ADDCARD= (byte)0x08;//新增卡号--功能码
	private static byte FUNC_DELETECARD= (byte)0x09;//删除卡号--功能码
	private static byte FUNC_CLEARCARD= (byte)0x0A;//清空卡号--功能码
	private static byte FUNC_RESTART= (byte)0x0B;//设备重启--功能码
	private static byte FUNC_REPLAYFAIL= (byte)0x0F;//回复效验失败--功能码
	public static String SIGNINFO;//公众号信息
	public static String access_token;//微信调用凭证
	public static String TIME;//token的创建时间

	private static final Logger logger = Logger.getLogger(SendDataUtil.class);
	public static DeviceService deviceService = (DeviceService)SpringBeanUtil.getBean("deviceService");
	public static PollDataService pollDataService = (PollDataService)SpringBeanUtil.getBean("pollDataService");

	
	static{
		WxuserService wxuserService = (WxuserService)SpringBeanUtil.getBean("wxuserService");
		if(StringUtils.isBlank(SIGNINFO)||"null".equals(SIGNINFO)){
			SIGNINFO = wxuserService.getWxConfig("SIGNINFO");
		}
		if(StringUtils.isBlank(access_token)||"null".equals(access_token)){
			access_token = wxuserService.getWxConfig("TOKEN");
		}
	}
	
	/**
	 * 发送数据：针对单个会话，写出操作必须保证同步，且有一定的时间间隔，   //发送数据
	 * 否则会连包，即前后两次发送被客户端一次读到，进而无法识别
	 */
	public static void sendData(IoSession session, byte[] data, String func){
		try {
			if(session!=null&&!session.isClosing()){
				while(true){
					synchronized (session) {  //回话同步
						if(session.isWriteSuspended()){
							continue;
						}else{
							session.write(data);
							session.removeAttribute("lastSend");
							session.setAttribute("lastSend", func);
							session.suspendWrite();
							session.wait(500);//实际测试有人模块300可以满足，本地电脑1可以满足
							session.resumeWrite();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("write数据异常", e);
		}
	}
	
	/**
	 * 回复心跳包
	 */
	public static byte[] heartbeat(){
		byte[] sendByte = new byte[5];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_HEARTBEAT;
		sendByte[3] = (byte)0x00;
		byte[] crcdata = new byte[2];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		sendByte[4] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**
	 * 发送开门指令
	 */
	public static byte[] openDoor(){
		byte[] sendByte = new byte[5];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_OPENDOOR;
		sendByte[3] = (byte)0x00;
		byte[] crcdata = new byte[2];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		sendByte[4] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**
	 * 发送开门指令:01.12改：加入算法密码
	 */
	public static byte[] openDoor(String secret){
		byte[] byte_secret = hexStringToByte(secret);
		byte[] sendByte = new byte[8];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_OPENDOOR;
		sendByte[3] = (byte)0x03;
		System.arraycopy(byte_secret, 0, sendByte, 4, 3);
		byte[] crcdata = new byte[5];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		System.arraycopy(byte_secret, 0, crcdata, 2, 3);
		sendByte[7] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**
	 * 授权模式变更
	 * 0为非授权、1为授权
	 */
	public static byte[] changeAuth(int code){
		byte[] sendByte = new byte[6];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_CHANGEAUTH;
		sendByte[3] = (byte)0x01;
		if(code==0){
			sendByte[4] = (byte)0x00;
		}
		if(code==1){
			sendByte[4] = (byte)0x01;
		}
		byte[] crcdata = new byte[3];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		crcdata[2] = sendByte[4];
		sendByte[5] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**
	 * 发送广告
	 * 127字节文本，没文字部分补充空格0x20。
	 * news.getBytes().length<=127
	 */
	public static byte[] sendNews(String news){
		byte[] sendByte = new byte[132];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_NEWS;
		sendByte[3] = (byte)0x7F;
		byte[] news_byte;
		byte[] realnews = new byte[127];
		try {
			news_byte = news.getBytes("GB2312");
			if(news_byte.length<127){
				System.arraycopy(news_byte, 0, realnews, 0, news_byte.length);
				for(int i=news_byte.length;i<realnews.length;i++){
					realnews[i] = (byte)0x20;
				}
			}else if(news_byte.length>127){
				System.arraycopy(news_byte, 0, realnews, 0, realnews.length);
			}
			System.arraycopy(realnews, 0, sendByte, 4, realnews.length);
			byte[] crcdata = new byte[129];
			crcdata[0] = sendByte[2];
			crcdata[1] = sendByte[3];
			System.arraycopy(realnews, 0, crcdata, 2, realnews.length);
			sendByte[131] = CRC7.CheckCode_CRC7(crcdata);
		} catch (Exception e) {
			e.printStackTrace();
			byte[] crcdata = new byte[129];
			crcdata[0] = sendByte[2];
			crcdata[1] = sendByte[3];
			for(int i=4;i<=130;i++){
				sendByte[i] = (byte)0x20; //异常就用空格表示
			}
			System.arraycopy(sendByte, 4, crcdata, 2, 127);
			sendByte[131] = CRC7.CheckCode_CRC7(crcdata);
		}
//		System.out.println(BinaryToHexString(sendByte));
		return sendByte;
	}
	
	/**
	 * 同步时间:
	 * 8字节时间，例：0x14 0x11 0x09 0x16 0x0D 0x39 0x05 0x05 
	 * 前两个字节表示年，剩下依次为月、日、时、分、秒、星期几
	 */
	public static byte[] matchTime(){
		byte[] sendByte = new byte[13];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_MATCHTIME;  //功能码
		sendByte[3] = (byte)0x08;
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_WEEK);
		day = day-1;
		if(day==0){
			day = 7;
		}
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss"); 
		String nowstr = formatter.format(date);
		byte[] timeByte = stringToHexByte(nowstr);  //将10进制的数字字符串，转成16进制的数组   “20171116”-->"14110B10" [14,11,0B,10]
		System.arraycopy(timeByte, 0, sendByte, 4, timeByte.length); //接上数组
		String hexString = Integer.toHexString(day);  //10进制转16进制字符串
		if(hexString.length()==1){
			hexString = "0"+hexString;
		}
		sendByte[11] = hexStringToByte(hexString)[0]; //将16进制字符串转byte数组
		byte[] crcdata = new byte[10];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		System.arraycopy(sendByte, 4, crcdata, 2, 8);  //[2]--[9]
		sendByte[12] = CRC7.CheckCode_CRC7(crcdata);  //校验位
		return sendByte;  
	}
	
	/**
	 * 新增卡号
	 * cardNo必须是纯数字，不能有字母，长度8
	 * 123 45678
	 */
	public static byte[] addCard(String cardNo){
  		String hexStr = "";
  		String first = "";
  		first = Integer.toHexString(Integer.parseInt(cardNo.substring(0, 3)));
  		String completion1 ="";
  		if(first.length()<2){
			for(int i=1;i<=2-first.length();i++){
				completion1 += "0";
			}
			first = completion1+first;
		}
  		String second = "";
  		second = Integer.toHexString(Integer.parseInt(cardNo.substring(3, 8)));
  		String completion2 ="";
  		if(second.length()<4){
			for(int i=1;i<=4-second.length();i++){
				completion2 += "0";
			}
			second = completion2+second;
		}
  		hexStr = first+second;
		byte[] cardByte = hexStringToByte(hexStr);
		byte[] sendByte = new byte[8];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_ADDCARD;
		
		sendByte[3] = (byte)0x03;
		System.arraycopy(cardByte, 0, sendByte, 4, cardByte.length);
		byte[] crcdata = new byte[5];
		crcdata[0] = sendByte[2]; //功能码
		crcdata[1] = sendByte[3]; //数据长度
		System.arraycopy(cardByte, 0, crcdata, 2, cardByte.length);
		sendByte[7] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	//16进制的卡号
	public static byte[] addCardNew(String cardNo){
		byte[] sendByte = new byte[8];
		byte[] cardByte = hexStringToByte(cardNo);   //02800C129C   800C12
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_ADDCARD;
		sendByte[3] = (byte)0x03;
		System.arraycopy(cardByte, 0, sendByte, 4, cardByte.length);
		byte[] crcdata = new byte[5];
		crcdata[0] = sendByte[2]; //功能码
		crcdata[1] = sendByte[3]; //数据长度
		System.arraycopy(cardByte, 0, crcdata, 2, cardByte.length);
		sendByte[7] = CRC7.CheckCode_CRC7(crcdata); //校验
		return sendByte;
	}
	/**
	 * 删除卡号
	 * cardNo必须是纯数字，不能有字母，长度6
	 */
	public static byte[] deleteCard(String cardNo){
  		String hexStr = "";
  		String first = "";
  		first = Integer.toHexString(Integer.parseInt(cardNo.substring(0, 3)));
  		String completion1 ="";
  		if(first.length()<2){
			for(int i=1;i<=2-first.length();i++){
				completion1 += "0";
			}
			first = completion1+first;
		}
  		String second = "";
  		second = Integer.toHexString(Integer.parseInt(cardNo.substring(3, 8)));
  		String completion2 ="";
  		if(second.length()<4){
			for(int i=1;i<=4-second.length();i++){
				completion2 += "0";
			}
			second = completion2+second;
		}
  		hexStr = first+second;
		byte[] cardByte = hexStringToByte(hexStr);
		byte[] sendByte = new byte[8];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_DELETECARD;
		sendByte[3] = (byte)0x03;
		System.arraycopy(cardByte, 0, sendByte, 4, cardByte.length);
		byte[] crcdata = new byte[5];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		System.arraycopy(cardByte, 0, crcdata, 2, cardByte.length);
		sendByte[7] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**删除卡号*/
	public static byte[] deleteCardNew(String cardNo){
		byte[] sendByte = new byte[8];
		byte[] cardByte = hexStringToByte(cardNo);   //02800C129C   800C12
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_DELETECARD;
		sendByte[3] = (byte)0x03;
		System.arraycopy(cardByte, 0, sendByte, 4, cardByte.length);
		byte[] crcdata = new byte[5];
		crcdata[0] = sendByte[2]; //功能码
		crcdata[1] = sendByte[3]; //数据长度
		System.arraycopy(cardByte, 0, crcdata, 2, cardByte.length);
		sendByte[7] = CRC7.CheckCode_CRC7(crcdata); //校验
		return sendByte;
	}
	
	/**
	 * 清空卡号
	 */
	public static byte[] clearCard(){
		byte[] sendByte = new byte[5];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_CLEARCARD;
		sendByte[3] = (byte)0x00;
		byte[] crcdata = new byte[2];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		sendByte[4] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**
	 * 回复收到开门记录
	 */
	public static byte[] replayGetRecord(){
		byte[] sendByte = new byte[5];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_UPLOADRECORD;
		sendByte[3] = (byte)0x00;
		byte[] crcdata = new byte[2];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		sendByte[4] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	/**
	 * 回复二维码
	 */
	public static byte[] replayQcode(byte[] data){
		byte[] sendByte = new byte[5+data.length];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_REPLAYQCODE;
		int len = data.length;   //73
//		System.out.println("十进制的二维码长度：" + len);
		String hexLen = Integer.toHexString(len);  //49         十六进制的整形
		if(hexLen.length()<2){
			hexLen = "0"+hexLen.toUpperCase();
		}
		sendByte[3] = hexStringToByte(hexLen)[0];   //十六进制的字符串转化为byte数组          十六进制 数据长度
		System.arraycopy(data, 0, sendByte, 4, len);

		byte[] crcdata = new byte[2+len];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		System.arraycopy(data, 0, crcdata, 2, len);
		sendByte[4+len] = CRC7.CheckCode_CRC7(crcdata);
//		System.out.println("十六进制的二维码数据：" +BinaryToHexString(sendByte));
		return sendByte;
	}
	
	/**
	 * 回复效验失败
	 */
	public static byte[] replayFail(){
		byte[] sendByte = new byte[5];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_REPLAYFAIL;
		sendByte[3] = (byte)0x00;
		byte[] crcdata = new byte[2];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		sendByte[4] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	
	//TODO**********************************以下为平台点击时调用**********************************
	/**
	 * 平台发送修改授权模式:0为非授权、1为授权
	 */
	public static void sendChangeAuth(int type, String deviceId){
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_CHANGEAUTH;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(changeAuth(type)));
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	/**
	 * 平台发送同步时间
	 */
	public static void sendMachTime(String deviceId){
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_MATCHTIME;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(matchTime()));
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	/**
	 * 平台发送：新增卡号(之前需要将卡号转16进制发送给终端)
	 */
	public static void sendAddCard(String cardNo, String deviceId){
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_ADDCARD;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(addCard(cardNo)));
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	public static void sendAddCardNew(String cardNo, String deviceId){
		System.out.println("发送卡号···");
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_ADDCARD;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(addCardNew(cardNo)));   //02800C129C   800C12
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	/**
	 * 平台发送：删除卡号
	 */
	public static void sendDeleteCard(String cardNo, String deviceId){
		System.out.println("删除卡号");
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_DELETECARD;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(deleteCard(cardNo)));
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	/** 
	 * 平台发送：删除卡号   未转16进制
	 */
	public static void sendDeleteCardNew(String cardNo, String deviceId){
		System.out.println("删除卡号");
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_DELETECARD;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(deleteCardNew(cardNo)));
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	/**
	 * 平台发送：清空卡号
	 */
	public static void sendClearCard(String deviceId){
		String mac = deviceService.getMacById(deviceId);
		if(!StringUtils.isBlank(mac)){
			PollData pollData = new PollData();
			pollData.setId(UUIDUtil.generate());
			pollData.setDeviceMac(mac);
			byte[] by = new byte[1];
			by[0] = FUNC_CLEARCARD;
			pollData.setFunc(BinaryToHexString(by));
			pollData.setData(BinaryToHexString(clearCard()));
			pollData.setPollTime(new Date().getTime());
			pollData.setSendCount(0);
			pollData.setState(0);
			pollData.setOnline(1);
			pollDataService.save(pollData);
		}
	}
	
	/**
	 * 设备重启
	 */
	public static byte[] restart(){
		byte[] sendByte = new byte[5];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_RESTART;
		sendByte[3] = (byte)0x00;
		byte[] crcdata = new byte[2];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		sendByte[4] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
	
	//TODO**********************************以下为通用方法**********************************
	//将十六进制字符串转成byte数组，例：“0A”-->(byte)0x0A  两位两位一取
    public static byte[] hexStringToByte(String hex) {
    	hex = hex.toUpperCase();
        int len = (hex.length() / 2);   
        byte[] result = new byte[len];   
        char[] achar = hex.toCharArray();   
        for (int i = 0; i < len; i++) {   
         int pos = i * 2;   
         result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));   
        }   
        return result;   
    }  
    private static byte toByte(char c) {   
        byte b = (byte) "0123456789ABCDEF".indexOf(c);   
        return b;   
    }
    
    
	//将16进制的字符串，转成10进制的字符串,例：“0A”-->"10",2位取一次
	public static String HexStringToString(String hexString) { 
		hexString = hexString.toUpperCase();
		String str = "";
		for(int i = 1;i<=(hexString.length()/2);i++){
			String stri = hexString.substring((i-1)*2,2*i);
			int inti = Integer.parseInt(stri,16);
			if(inti>=0&&inti<=9){
				str+="0"+inti;
			}else{
				str+=inti;
			}
		}
		return str;
	}
	
	//将10进制的数字字符串，转成16进制的数组,例：“20171116”-->"14110B10",2位取一次
	public static byte[] stringToHexByte(String string) {
		String str = "";
		for(int i = 1;i<=(string.length()/2);i++){
			String stri = string.substring((i-1)*2,2*i);
			String hexLen = Integer.toHexString(Integer.parseInt(stri));
			if(hexLen.length()==1){
				hexLen = "0"+hexLen;
			}
			str+=hexLen;
		}
		return hexStringToByte(str.toUpperCase());
	}
//	public static void main(String[] args) {
//		String deviceMac ="D8B 04C DEC6C1";
//		String secret = TeaEncodeUtil.teaEncode(SendDataUtil.hexStringToByte(deviceMac), SendDataUtil.stringToHexByte(time));
//	}
	
	
    //将字节数组转换为16进制字符串  例：{(byte)0x0A,byte0x0B}-->"0A0B"
    public static String BinaryToHexString(byte[] bytes) {  
        String hexStr = "0123456789ABCDEF";  
        String result = "";  
        String hex = "";  
        for (byte b : bytes) {  
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));  
            hex += String.valueOf(hexStr.charAt(b & 0x0F));  
            result += hex;  
        }  
        return result;  
    } 
    
    
	public static void StringToAscii() {// 字符串转换为ASCII码
		String s = "abc";// 字符串
		char[] chars = s.toCharArray(); // 把字符中转换为字符数组
		System.out.println("STRING TO ASCII\n----------------------");
		for (int i = 0; i < chars.length; i++) {
			System.out.println(chars[i] + " " + (int) chars[i]);
		}
	}
    
	private static String cnToUnicode(String cn) {
	    char[] chars = cn.toCharArray();
	    StringBuffer returnStr = new StringBuffer();
	    String temp;
	    for (int i = 0; i < chars.length; i++) {
	    	temp = Integer.toString(chars[i], 16);
	    	if(temp.length() < 4) {
	    		returnStr.append("00");
	    	}
	    	returnStr.append(temp);
	    }
	    return returnStr.toString();
	}
	
	/**
	 * 如：D8B 04C DEC 6C1  -->
     *          0x00, 0x44, 0x38, 0x42, 
                0x00, 0x30, 0x34, 0x43, 
                0x00, 0x44, 0x45, 0x43, 
                0x00, 0x36, 0x43, 0x31
	 * @param mac
	 * @return
	 */
	public static byte[] stringMacToByte(String mac){
		byte[] ch = new byte[mac.length()];
		for(int i=0;i<mac.length();i++){
			ch[i] = (byte)mac.charAt(i);
		}
		Object[] subAry = splitAry(ch, 3);//分割后的子块数组
		int[] arr = new int[16];  //目标数组
		for(int i=0;i<4;i++){
			System.arraycopy((int[])subAry[i],0,arr,1+4*i,3);
		}
		//十六进制的字节数组
		byte[] toHex = new byte[16];
		for(int i=0;i<arr.length;i++){
			toHex[i] =Byte.parseByte(Integer.toHexString(arr[i]));
		}
		byte[] b2 = new byte[16];
		for(int i=0;i<toHex.length;i++){
			if(toHex[i]==0){
				b2[i]=hexStringToByte("00")[0];
			}else{
				b2[i]=hexStringToByte(""+toHex[i])[0];
			}
		}
		return b2;
	}
	
	//拆分数组
    public static Object[] splitAry(byte[] ary, int subSize) {
        int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;

        List<List<Byte>> subAryList = new ArrayList<List<Byte>>();

        for (int i = 0; i < count; i++) {
         int index = i * subSize;
         List<Byte> list = new ArrayList<Byte>();
         int j = 0;
             while (j < subSize && index < ary.length) {
                 list.add(ary[index++]);
                 j++;
             }
         subAryList.add(list);
        }
         
        Object[] subAry = new Object[subAryList.size()];
         
        for(int i = 0; i < subAryList.size(); i++){
             List<Byte> subList = subAryList.get(i);
             int[] subAryItem = new int[subList.size()];
             for(int j = 0; j < subList.size(); j++){
                 subAryItem[j] = subList.get(j).intValue();
             }
             subAry[i] = subAryItem;
        }
         
        return subAry;
        }

}
