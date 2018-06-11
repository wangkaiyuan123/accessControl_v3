package com.dhht.mina.job;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.mina.core.session.IoSession;

import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.weixin.PollDataService;
/**
 * key=mac地址
 * value=IoSession
 * 如果服务器要向终端发消息，则以mac找到session，然后write()
 */
public class SessionBean {

	private final ConcurrentHashMap<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();
	private final ConcurrentHashMap<String, String> dataMap = new ConcurrentHashMap<String, String>();
	
	@Resource
	private PollDataService pollDataService;
	/**
	 * 管理session
	 * @param systemId
	 * @param session
	 */
	public void putSession(String systemId, IoSession session){
		if (!sessionMap.containsKey(systemId)){
			sessionMap.put(systemId, session);
			pollDataService.newMatchTime(systemId);   //发送时间同步
		}else if(sessionMap.get(systemId) != session){
			sessionMap.put(systemId, session);
			pollDataService.newMatchTime(systemId);   //发送时间同步
		}
	}
	
	public void putData(String mac, String value){
		if (!dataMap.containsKey(mac)){
			dataMap.put(mac, value);
		}else if(dataMap.get(mac) != value){
			dataMap.put(mac, value);
		}
	}
	public void delData(String mac){
		if (dataMap.containsKey(mac)){
			dataMap.remove(mac);
		}
	}
	
	/**
	 * 删除session
	 * @param systemId
	 */
	public void delSession(String systemId){
		if (sessionMap.containsKey(systemId)){
			sessionMap.remove(systemId);
		}
	}
	/**
	 * 根据设备编码获取对应的session
	 * @param systemId
	 * @return
	 */
	public IoSession getSession(String systemId){
		return sessionMap.get(systemId);
	}
	
	public String getData(String mac){
		return dataMap.get(mac);
	}
	
	public ConcurrentHashMap<String, IoSession> getSessionMap() {
		return sessionMap;
	}
	
	public ConcurrentHashMap<String, String> getDataMap() {
		return dataMap;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "你好你好 ";
	}
}
