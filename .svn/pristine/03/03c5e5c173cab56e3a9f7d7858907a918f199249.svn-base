package com.dhht.service.weixin;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.dao.BaseDao;
import com.dhht.entity.weixin.SMSCode;

@Service
public class SMSCodeService {
	@Resource
	private BaseDao<SMSCode> sMSCodeDao;
	
	/**
	 * 根据电话号码获取对象
	 */
	public SMSCode getSMSCodeByPhone(String phone){
		return sMSCodeDao.get(" from SMSCode where phone='"+phone+"'");
	}
	
	/**
	 * 保存对象
	 */
	public void save(SMSCode sMSCode){
		sMSCodeDao.save(sMSCode);
	}
	
	/**
	 * 更新对象
	 */
	public void update(SMSCode sMSCode){
		sMSCodeDao.update(sMSCode);
	}
}
