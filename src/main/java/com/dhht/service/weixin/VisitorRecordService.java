package com.dhht.service.weixin;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.dhht.dao.BaseDao;
import com.dhht.entity.system.Person;
import com.dhht.entity.visitors.VisitorOfflineFlag;
import com.dhht.entity.visitors.VisitorRecord;

@Service
public class VisitorRecordService {
	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private BaseDao<VisitorRecord> visitorRecordDao;
	@Resource
	private BaseDao<VisitorOfflineFlag> visitorOfflineFlagDao;
	
	/**
	 * 新增一条访客预约记录
	 */
	public void insertVisitorRecord(VisitorRecord record){
		visitorRecordDao.save(record);
	}
	
	/**
	 * 根据主键查询记录
	 */
	public VisitorRecord getRecord(String id){
		return visitorRecordDao.get(VisitorRecord.class, id);
	}
	
	/**
	 * 跟新一条记录
	 */
	public void updateRecord(VisitorRecord o){
		visitorRecordDao.update(o);
	}
	
	public void addOfflineFlag(VisitorOfflineFlag vf){
		visitorOfflineFlagDao.save(vf);
	}
	
	/**
	 * mac查询
	 * @param mac
	 * @return
	 */
	public VisitorOfflineFlag getListByMacAndSecret(String mac,String secret){
		return visitorOfflineFlagDao.get("from VisitorOfflineFlag where mac = '"+mac+"' and secreat = '"+secret+"'");
	}
	
}
