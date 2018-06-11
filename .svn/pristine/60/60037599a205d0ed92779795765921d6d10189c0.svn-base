package com.dhht.service.weixin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.dao.BaseDao;
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.entity.record.UserApplyRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.User;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.Unit;
import com.dhht.entity.weixin.WeixinUser;

@Service
public class PersonService {
	@Resource
	private BaseDao<Person> personDao;
	
	@Resource
	private BaseDao<DoorCard> doorCardDao;
	
	@Resource
	private BaseDao<OpenDoorRecord> openDoorRecordDao;
	
	@Resource
	private BaseDao<PersonUnit> personUnitDao;
	
	@Resource
	private BaseDao<Unit> unitDao;
	
	@Resource
	private BaseDao<Region> regionDao;
	
	@Resource
	private BaseDao<UserApplyRecord> userApplyRecordDao;
	
	@Resource
	private BaseDao<User> userDao;
	
	/**
	 * 根据opendId获取人员
	 * @param openId
	 * @return
	 */
	public Person getPersonByOpenid(String openId){
		String hql = " from Person where openid='"+openId+"'";
		return personDao.get(hql);
	}
	
	/**
	 * 根据cardId获取我的门卡信息
	 * @param cardId
	 * @return
	 */
	public DoorCard getDoorCardByCardId(String cardId){
		String hql = " from DoorCard where id = '" + cardId + "'";
		return doorCardDao.get(hql);
	}
	
	/**
	 * 根据personId获取我的门卡的开门记录
	 * @param personId 用户id
	 * @param page 页码
	 * @return
	 */
	public List<OpenDoorRecord> getOpenDoorRecordByPersonId(String personId,int page){
		String hql = " from OpenDoorRecord where personId = '" + personId + "' order by openDate desc";
		List<OpenDoorRecord> list = openDoorRecordDao.find(hql,page, 5);
		return list;
	}
	
	/**
	 * 根据personId获取单元id
	 * @param personId
	 * @return
	 */
	public List<PersonUnit> getUnitIdByPersonId(String personId){
//		String hql = " from PersonUnit pu left join Unit un on pu.unitId=un.id  where pu.personId = '" + personId + "' order by un.communityId,un.isGurad desc";
//		List<PersonUnit> list = new ArrayList<PersonUnit>();
//		list = personUnitDao.find(hql);
		//SQL
		String sql = "select * from person_unit pu left join unit un on pu.unitId=un.id  where pu.personId = '" + personId + "' order by un.communityId,un.isGurad desc";
		List<PersonUnit> listsqList = personUnitDao.findBySql(sql, PersonUnit.class);
		
		return listsqList;
	}
	
	/**
	 * 根据unitId获取该单元的其他属性，返回单元对象
	 * @param unitId
	 * @return
	 */
	public Unit getUnitById(String unitId){
		String hql = " from Unit where id = '" + unitId + "' order by communityId,isGurad desc ";
		return unitDao.get(hql);
	}

	/**
	 * 根据小区id(communityId)来获取小区名
	 * @param communityId
	 * @return
	 */
	public Region getCommunityNameById(String communityId){
		String hql = " from Region where id = '" + communityId + "' and level = 5 ";
		return regionDao.get(hql);
	}
	
	/**
	 * 根据unitId和personId去用户申请表中查询申请状态，为1通过，为0审核中，为2审核失败
	 * @param unitId
	 * @param personId
	 * @return
	 */
	public UserApplyRecord getUnitStateByUnitId(String unitId,String personId){
		String hql = " from UserApplyRecord where unitId = '" + unitId + "' and personId = '" + personId + "'";
		return userApplyRecordDao.get(hql);
	}
	
	/**
	 * 根据openId去运维人员中查询该对象
	 */
	public User getYunWeiByOpenId(String openId){
		String hql = " from User where openid = '" + openId + "'";
		return userDao.get(hql);
	}
	
}
