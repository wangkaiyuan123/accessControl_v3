package com.dhht.action.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.dhht.common.SpringBeanUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.entity.doorguard.Advertising;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.weixin.PollData;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.device.AdvertisingService;
import com.dhht.service.device.DeviceService;
import com.dhht.service.weixin.PollDataService;
/**
 * 定时发送广告任务
 * @author Administrator
 *
 */
public class SendNewsTask extends TimerTask{
	private static final long TIME = 86400000;//24小时
	private static final String FUNC_SENDNEWS = "05";
	AdvertisingService advertisingService = (AdvertisingService)SpringBeanUtil.getBean("advertisingService");
	PollDataService pollDataService = (PollDataService)SpringBeanUtil.getBean("pollDataService");
	DeviceService deviceService = (DeviceService)SpringBeanUtil.getBean("deviceService");
	@Override
	public void run() {
		try {
			List<Advertising> list = advertisingService.getAllList();
			if(list!=null&&list.size()>0){
				long now = Calendar.getInstance().getTimeInMillis();
				long interval = TIME/list.size();
				for(int i=0;i<list.size();i++){
					long plan = now+(interval*i);
					list.get(i).setPlanTime(plan);
				}
				Long count = deviceService.queryAllCount();
				if(count!=null&&count>0){
					int totlePage = (int) ((count/1000)+1); 
					for(int i=1;i<=totlePage;i++){
						List<Device> devicelist = deviceService.queryDeviceList(i, 1000);
						if(devicelist!=null&&devicelist.size()>0){
							for (Advertising advertising : list) {
								for (Device device : devicelist) {
									try {
										//查之前先查下，再存，只存最新一条，有的话就把之前的都给删掉
										if(!pollDataService.queryDateByMacAndFun(device.getDeviceMac(), "05")){
											//不存在未处理或发送失败的数据,jiu
											PollData pollData = new PollData();
											pollData.setId(UUIDUtil.generate());
											pollData.setDeviceMac(device.getDeviceMac());
											pollData.setFunc(FUNC_SENDNEWS);
											pollData.setData(SendDataUtil.BinaryToHexString(SendDataUtil.sendNews(advertising.getContent())));
											pollData.setPollTime(advertising.getPlanTime());
											pollData.setSendCount(0);
											pollData.setState(0);
											pollData.setOnline(1);
											pollDataService.save(pollData);
										}else{
											pollDataService.getFirstData(device.getDeviceMac(), "05");
										}
									} catch (Exception e) {
										
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
	}

}
