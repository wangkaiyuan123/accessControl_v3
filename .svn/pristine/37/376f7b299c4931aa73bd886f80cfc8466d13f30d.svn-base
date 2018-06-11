package com.dhht.action.task;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.dhht.common.SpringBeanUtil;
import com.dhht.service.weixin.PollDataService;
/**
 * 定时删除成功处理的发送包
 * @author Administrator
 *
 */
public class ClearPollDataTask extends TimerTask{
	private static final Logger logger = Logger.getLogger(ClearPollDataTask.class);
	PollDataService pollDataService = (PollDataService)SpringBeanUtil.getBean("pollDataService");
	@Override
	public void run() {
		pollDataService.deleteAllSuccessData();
		pollDataService.deleteOldOpenDoorFlag();   //定时开门成功记录表
		logger.error("执行定时任务：删除已成功处理的发送包*************************");
	}

}
