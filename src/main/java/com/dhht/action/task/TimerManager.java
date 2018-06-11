package com.dhht.action.task;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.dhht.mina.server.CommunicateUtil;

public class TimerManager {
	// 时间间隔
	private static final long TIME = 21600000;//6小时
	private static final long MATCH_TIME = 86400000;//24小时
	private static final long NICK_TIME = 2000;
	private static final String[] LAST_WORD = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
	
	public TimerManager() {
		Calendar calendar = Calendar.getInstance();
		//处理：发送通讯数据
		calendar.add(Calendar.SECOND,10); //延迟10秒
		Date communicateDate = calendar.getTime();
//		for(final String lastWord:LAST_WORD){
//			new Timer().schedule(new TimerTask() {
//				@Override
//				public void run() {
//					CommunicateUtil.sendPollData(lastWord);  //每一条线程去执行的任务
//				}
//			}, communicateDate, 3000);   //timer.schedule(task, delay, period)  3个参数：要执行的任务，
//		}
		
		//定时任务：时间同步
		calendar.add(Calendar.SECOND,5);
		Timer matchTimeTimer = new Timer();
		Date matchTimeData = calendar.getTime();
		MatchTimeTask matchTimeTask = new MatchTimeTask();
//     	matchTimeTimer.schedule(matchTimeTask, matchTimeData, MATCH_TIME);
		
		//定时任务：发送广告
		calendar.add(Calendar.SECOND,5);
		Timer newsTimer = new Timer();
		Date newsData = calendar.getTime();
		SendNewsTask sendNewsTask = new SendNewsTask();
//		newsTimer.schedule(sendNewsTask, newsData, MATCH_TIME);
		
		//微信定时任务;补全用户信息
//		calendar.add(Calendar.SECOND, 10);
//		Date weixinDate2 = calendar.getTime();
//		Timer wexintimer2 = new Timer();
//		GetNicknameTask weixindgetnicknameTask = new GetNicknameTask();
//		wexintimer2.schedule(weixindgetnicknameTask, weixinDate2, NICK_TIME);
		
		//微信定时任务;更新用户信息
//		calendar.add(Calendar.SECOND,10);
//		Date weixinDate = calendar.getTime();
//		Timer wexintimer = new Timer();
//		UpdateWxUserTask weixindtask = new UpdateWxUserTask();
//		wexintimer.schedule(weixindtask, weixinDate, TIME);
			
		//定时任务：删除已成功处理的发送包
		calendar.add(Calendar.SECOND,5);
		Timer clearPollDataTimer = new Timer();
		Date clearPollDataData = calendar.getTime();
		ClearPollDataTask clearPollDataTask = new ClearPollDataTask();
//		clearPollDataTimer.schedule(clearPollDataTask, clearPollDataData, TIME);
		
	}
}