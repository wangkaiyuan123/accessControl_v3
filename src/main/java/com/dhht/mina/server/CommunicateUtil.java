package com.dhht.mina.server;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.dhht.common.DaoUtil;
import com.dhht.common.SpringBeanUtil;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.weixin.PollData;
import com.dhht.service.device.DeviceService;
import com.dhht.service.weixin.PollDataService;

public class CommunicateUtil {
	private static final String FUNC_CHANGEAUTH = "04";
	private static final String FUNC_MATCHTIME = "06";
	private static final String FUNC_ADDCARD = "08";
	private static final String FUNC_DELETECARD = "09";
	private static final String FUNC_CLEARCARD = "0A";
	private static final Logger logger = Logger.getLogger(CommunicateUtil.class);
	public static DeviceService deviceService = (DeviceService)SpringBeanUtil.getBean("deviceService");
	public static PollDataService pollDataService = (PollDataService)SpringBeanUtil.getBean("pollDataService");
	
	/**
	 * 多线程处理poll_data
	 * 即：向设备发送数据包
	 * 平台发送：修改授权模式04、新增卡号08、删除卡号09、清空卡号0A
	 * firstWord:主键id首字母
	 */
	public static void sendPollData(String lastWord){
//		System.out.println("执行处理发送数据包任务"+firstWord+"**********************");
		List<PollData> list = pollDataService.getPollDataList(lastWord,100);
		if(list!=null&&list.size()>0){
			IoSession session = null;
			for (PollData pollData : list) {
				try {
					session = pollDataService.getSessionByMac(pollData.getDeviceMac());
					if(session!=null&&!session.isClosing()){
						try {
							session.removeAttribute("lastTask");
							session.removeAttribute("lastTaskResult");
							session.removeAttribute("lastTaskTime");
							session.setAttribute("lastTask", pollData.getFunc());
							session.setAttribute("lastTaskTime", new Date().getTime());
							if(FUNC_MATCHTIME.equals(pollData.getFunc())){
								SendDataUtil.sendData(session, SendDataUtil.matchTime(),pollData.getFunc());
							}else{
								SendDataUtil.sendData(session, SendDataUtil.hexStringToByte(pollData.getData()),pollData.getFunc());
							}
							Thread.currentThread().sleep(600);
							int result = DaoUtil.parseInt((String)session.getAttribute("lastTaskResult"), 0);
							if(result==1){
								pollData.setSendCount(pollData.getSendCount()+1);
								pollData.setState(1);
								pollDataService.update(pollData);
								if(FUNC_CHANGEAUTH.equals(pollData.getFunc())){//修改授权模式
									if(pollData.getData().length()>10){
										if("00".equals(pollData.getData().subSequence(8, 10))){
											Device device = deviceService.getDeviceByMac(pollData.getDeviceMac());
											device.setAuthotity(0);
											deviceService.updateDevice(device);
										}else if("01".equals(pollData.getData().subSequence(8, 10))){
											Device device = deviceService.getDeviceByMac(pollData.getDeviceMac());
											device.setAuthotity(1);
											deviceService.updateDevice(device);
										}
									}
								}else if(FUNC_ADDCARD.equals(pollData.getFunc())){//新增卡号返回成功
									
								}else if(FUNC_DELETECARD.equals(pollData.getFunc())){//删除卡号返回成功
									
								}else if(FUNC_CLEARCARD.equals(pollData.getFunc())){//清空卡号返回成功
									
								}
							}else{
								pollData.setSendCount(pollData.getSendCount()+1);
								pollData.setState(2);
								if(pollData.getSendCount()>=2){
									//连续2次失败，强制断开连接
									session.close(true);
								}
								if(pollData.getSendCount()==5){
									//同一条数据处理5次失败，则放弃发送，并确定业务失败
									if(FUNC_ADDCARD.equals(pollData.getFunc())){//新增卡号失败
										
									}else if(FUNC_DELETECARD.equals(pollData.getFunc())){//删除卡号失败
										
									}else if(FUNC_CLEARCARD.equals(pollData.getFunc())){//清空卡号失败
										
									}
								}
								pollDataService.update(pollData);
							}
						} catch (Exception e) {
							logger.error("发送数据异常", e);
							pollData.setSendCount(pollData.getSendCount()+1);
							pollData.setState(2);
							pollData.setResultDec("发送数据时异常："+e.toString());
							pollDataService.update(pollData);
						}
					}else{
						//修改所有数据状态为离线
						logger.error("/////////////////////////////////////////修改离线"+pollData.getDeviceMac());
						pollDataService.updateOnlineBySql(pollData.getDeviceMac(), 0);
					}
				} catch (Exception e) {
					
				}
			}
		}
	}
}
