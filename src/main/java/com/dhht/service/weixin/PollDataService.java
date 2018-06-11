package com.dhht.service.weixin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.weixin.OpenDoorFlag;
import com.dhht.entity.weixin.PollData;
import com.dhht.mina.job.SessionBean;
import com.dhht.mina.server.SendDataUtil;

@Service
public class PollDataService {
	private static String FUNC_HEARTBEAT= "01";//心跳--功能码
	private static String FUNC_OPENDOOR= "02";//开门指令--功能码
	private static String FUNC_REPLAYQCODE= "03";//发送二维码--功能码
	private static String FUNC_CHANGEAUTH= "04";//授权模式变更--功能码
	private static String FUNC_NEWS= "05";//广告--功能码
	private static String FUNC_MATCHTIME= "06";//时间同步--功能码
	private static String FUNC_UPLOADRECORD= "07";//回复收到上传记录--功能码
	private static String FUNC_ADDCARD= "08";//新增卡号--功能码
	private static String FUNC_DELETECARD= "09";//删除卡号--功能码
	private static String FUNC_CLEARCARD= "0A";//清空卡号--功能码
	private static String FUNC_REPLAYFAIL= "0F";//回复效验失败--功能码
	
	@Autowired
	private SessionBean sessionBean;
	@Resource
	private BaseDao<PollData> pollDataDao;
	@Resource
	private BaseDao<Device> deviceDao;
	@Resource
	private BaseDao<OpenDoorFlag> openDoorFlagDao;
	
    /**
     * 根据设备id获取设备mac
     */
    public String getMacById(String id){
    	Device device = deviceDao.get(Device.class, id);
    	if(device!=null){
    		return device.getDeviceMac();
    	}
    	return null;
    }
    
    /**
     * 根据mac获取IoSession
     */
    public IoSession getSessionByMac(String mac){
    	return sessionBean.getSession(mac);
    }
    
    public void putDataByMac(String mac,String openId){
    	sessionBean.putData(mac, openId);
    }
    public String getDataByMac(String mac){
    	return sessionBean.getData(mac);
    }
    public void delDataByMac(String mac){
    	sessionBean.delData(mac);
    }
    
    /**
     * 从sessionMap中删除IoSession
     */
    public void deleteIoSession(String mac){
    	sessionBean.delSession(mac);
    }
	
	/**
	 * 获取一定长度的离线数据包
	 * @param size
	 * @return
	 */
	public List<PollData> getPollDataList(int size){
		String hql = " from PollData where state in(0,2) and sendCount<=4 and online=1 order by pollTime asc";
		return pollDataDao.find(hql, 1, size);
	}
	
	/**
	 * 获取一定长度的离线数据包
	 * @param size 长度
	 * firstWord 主键首字母
	 * @return
	 */
	public List<PollData> getPollDataList(String lastWord,int size){
		long now = System.currentTimeMillis();
		String hql = " from PollData where state in(0,2) and sendCount<=4 and online=1 and SUBSTRING(deviceMac , 12 , 1)='"+lastWord+"' and pollTime<="+now+" order by pollTime asc";
		return pollDataDao.find(hql, 1, size);
	}
	
	/**
	 * 新增一条数据
	 */
	public void save(PollData pollData){
		pollDataDao.save(pollData);
	}
	
	/**
	 * 更新一条数据
	 */
	public void update(PollData pollData){
		pollDataDao.update(pollData);
	}
	
	/**
	 * 更新设备在线与否
	 * 0离线   1在线
	 */
	public void updateOnlineBySql(String mac, int online){
		String sql = " update poll_data set online="+online+" where deviceMac='"+mac+"' and state in(0,2)";
		pollDataDao.updateBySql(sql);
	}
	
	//组装同步时间数据
	public void newMatchTime(String mac){
		PollData pollData = new PollData();
		pollData.setId(UUIDUtil.generate());
		pollData.setDeviceMac(mac);
		pollData.setFunc(FUNC_MATCHTIME);
		pollData.setData(SendDataUtil.BinaryToHexString(SendDataUtil.matchTime()));
		pollData.setPollTime(new Date().getTime());
		pollData.setSendCount(0);
		pollData.setState(0);
		pollData.setOnline(1);
		pollDataDao.save(pollData);
	}
	
	/**
	 * 定时任务：删除所有成功处理的发送包
	 * state=1
	 */
	public void deleteAllSuccessData(){
		String sql = " delete from poll_data where state=1";
		pollDataDao.deleteBySql(sql);
	}
	
	public void deleteOldOpenDoorFlag(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,-100);
		Date now = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = " delete from openDoorFlag where addtime<='"+df.format(now)+"'";
		openDoorFlagDao.deleteBySql(sql);
	}
	
	/**
	 * 查询OpenDoorFlag
	 */
	public OpenDoorFlag getOpenDoorFlagById(String uuid){
		return openDoorFlagDao.get(OpenDoorFlag.class, uuid);
	}
	
	public boolean processOpenDoorFlag(String uuid,String openid,String secret){
		OpenDoorFlag openDoorFlag = openDoorFlagDao.get(OpenDoorFlag.class, uuid);
		if(openDoorFlag!=null){
			if(openDoorFlag.getFlag()==0){
				openDoorFlag.setFlag(2);
				openDoorFlagDao.update(openDoorFlag);
				return false;
			}else if(openDoorFlag.getFlag()==1){
				return true;
			}else if(openDoorFlag.getFlag()==2){
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 分发数据，一次发一条
	 */
	public void sendData1(PollData pollData){
		IoSession session = sessionBean.getSession(pollData.getDeviceMac());
		String func = pollData.getFunc();
		String data = pollData.getData();
		if(session!=null&&session.isClosing()==false){
			if(FUNC_HEARTBEAT.equals(func)){
				session.write(SendDataUtil.heartbeat());
			}else if(FUNC_OPENDOOR.equals(func)){
				session.write(SendDataUtil.openDoor());
			}else if(FUNC_REPLAYQCODE.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_CHANGEAUTH.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_NEWS.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_MATCHTIME.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_UPLOADRECORD.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_ADDCARD.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_DELETECARD.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_CLEARCARD.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}else if(FUNC_REPLAYFAIL.equals(func)){
				session.write(SendDataUtil.hexStringToByte(data));
			}
			try {
				Thread.currentThread().sleep(500);
				String backdata = sessionBean.getData(pollData.getDeviceMac());
				if(StringUtils.isBlank(backdata)){
					//成功
				}else{
					if("1F".equals(backdata)){
						
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pollData.setSendCount(pollData.getSendCount()+1);
//			pollData.setState(1);
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean queryDateByMacAndFun(String mac,String func){
		String hql = DaoUtil.getFindPrefix(PollData.class)+" where deviceMac ='"+mac+"' and func ='"+func+"' and state in(0,2)";
		 if(pollDataDao.count(hql)>0){
			 return true;
		 }
		 return false;
	}
	
	/**
	 * 保留最新的一条的未处理或未发送成功数据的,并删除之后的数据
	 * @param mac
	 * @param func
	 */
	public void getFirstData(String mac,String func){
		String hql = DaoUtil.getFindPrefix(PollData.class)+" where deviceMac ='"+mac+"' and func ='"+func+"' and state in(0,2) order by pollTime desc";
		List<PollData> pollDatas = pollDataDao.find(hql);
		//如果是时间同步,还要更新最新记录的时间
		if("06".equals(pollDatas.get(0).getFunc())){
			pollDatas.get(0).setData(SendDataUtil.BinaryToHexString(SendDataUtil.matchTime())); //更新最新的时间
			pollDataDao.saveOrUpdate(pollDatas.get(0));
		}
		for(int i=1 ;i<pollDatas.size();i++){
			pollDataDao.delete(pollDatas.get(i));
		}
	}
}
