package com.dhht.action.task;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.dhht.common.SpringBeanUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.weixin.PollData;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.device.DeviceService;
import com.dhht.service.weixin.PollDataService;

public class MatchTimeTask extends TimerTask{
	private static final String FUNC_MATCHTIME = "06";
	DeviceService deviceService = (DeviceService)SpringBeanUtil.getBean("deviceService");
	PollDataService pollDataService = (PollDataService)SpringBeanUtil.getBean("pollDataService");
	@Override
	public void run() {
		try {
			Long count = deviceService.queryAllCount();
			if(count!=null&&count>0){
				int totlePage = (int) ((count/1000)+1); 
				for(int i=1;i<=totlePage;i++){
					List<Device> list = deviceService.queryDeviceList(i, 1000);
					if(list!=null&&list.size()>0){
						for (Device device : list) {
							try {
								if(!pollDataService.queryDateByMacAndFun(device.getDeviceMac(), "06")){ //
									//不存在就发送时间同步
									PollData pollData = new PollData();
									pollData.setId(UUIDUtil.generate());
									pollData.setDeviceMac(device.getDeviceMac());
									pollData.setFunc(FUNC_MATCHTIME);
									pollData.setData(SendDataUtil.BinaryToHexString(SendDataUtil.matchTime()));
									pollData.setPollTime(new Date().getTime());
									pollData.setSendCount(0);
									pollData.setState(0);
									pollData.setOnline(1);
									pollDataService.save(pollData);
								}else{
									pollDataService.getFirstData(device.getDeviceMac(), "06");
								}
							} catch (Exception e) {
								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
	}

}