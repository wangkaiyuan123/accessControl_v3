package com.dhht.service.record;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.community.Community;
import com.dhht.entity.record.UserApplyRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.OperatePicture;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.User;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.Unit;
import com.dhht.entity.unit.UnitDevice;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.region.RegionService;
import com.dhht.service.weixin.WxuserService;

@Service
public class ApplyRecordService {
	@Resource
	private RegionService regionService;
	@Resource
	private WxuserService wxuserService;
	@Resource
	private  BaseDao<UserApplyRecord> userApplyRecordDao;
	@Resource
	private BaseDao<Person> personDao;
    @Resource
    private BaseDao<WeixinUser> weixinUserdDao;
    @Resource
    private BaseDao<PersonUnit> personUnitDao;
    @Resource
    private BaseDao<OperatePicture> operatePictureDao;
    @Resource
    private BaseDao<Unit> unitDao;
    @Resource
    private BaseDao<Community> communityDao;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private BaseDao<UnitDevice> unitDeviceDao;
	/**
	 * 获取申请记录数据
	 * @param userApplyRecord
	 * @param page
	 * @param rows
	 * @param regionId
	 * @return
	 */
	public List<UserApplyRecord> getList(UserApplyRecord userApplyRecord,int page,int rows,String regionId){
		List<Region> regions =  regionService.getRegionsTrees(regionId);   //包涵单元  
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
		List<UserApplyRecord> records = findByRegions(userApplyRecord,page,rows,regionsIdStr);
		for(UserApplyRecord record:records){
			if(record.getState()==0){
				record.setCheckPerson("");  //未审核就不显示
			}
//			//获取审核图片Id
//			Person person = personDao.get(Person.class,record.getPersonId());
//			if(person!=null&&person.getIdentifyPicId()!=null){
//				OperatePicture operatePicture = operatePictureDao.get(OperatePicture.class, person.getIdentifyPicId());
//				if(operatePicture!=null){
//					//record.setImgUrl(operatePicture.getPicturePath());
//					record.setImgUrl(""+operatePicture.getPicturePath().substring(operatePicture.getPicturePath().indexOf("/")+1, operatePicture.getPicturePath().length()));
//					//System.out.println(record.getImgUrl());
//				}else{
//					record.setImgUrl("");
//				}
//			}
			if(record.getImgUrl()!=null){
				record.setImgUrl(record.getImgUrl().substring(record.getImgUrl().indexOf("/")+1,record.getImgUrl().length()));
				record.setUnitName(unitDao.get(Unit.class, record.getUnitId()).getUnitName());
			}
		}
		return records;
	}

	private List<UserApplyRecord> findByRegions(UserApplyRecord userApplyRecord,int page,int rows,String regionsIdStr){
		String hql = DaoUtil.getFindPrefix(UserApplyRecord.class)+" where unitId IN ("+regionsIdStr+")";
		if(userApplyRecord.getState()==null){  //查询所有
			hql+=" order by applyDate desc";
			List<UserApplyRecord> records = userApplyRecordDao.find(hql, page, rows);
			return records;
		}
		//这里预留的是根据审核状态去条件查询
		hql+=" and state ="+userApplyRecord.getState();
		List<UserApplyRecord> records = userApplyRecordDao.find(hql, page, rows);
		return records;
	}
    
  /**
   * 获取总记录数
   * @return
   */
  public long getCount(UserApplyRecord userApplyRecord,String regionId){
	  List<Region> regions =  regionService.getRegionsTrees(regionId);   //包涵单元  
		Set<String> regionsIdSet = new HashSet<String>();
		for (Region region : regions) {
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet); // 机构Id的集合包括小区
		String hql = DaoUtil.getFindPrefix(UserApplyRecord.class)+ " where unitId IN (" + regionsIdStr + ")";
		if (userApplyRecord.getState() == null) { // 查询所有
			List<UserApplyRecord> records = userApplyRecordDao.find(hql);
			return (long)records.size();
		}
		// 这里预留的是根据审核状态去条件查询
		hql += " and state =" + userApplyRecord.getState();
		List<UserApplyRecord> records = userApplyRecordDao.find(hql);
		return (long)records.size();
  }
	
	
	
	/**
	 * 
	 * @return
	 */
	public AccessResult test(User user,Integer check,String id,Integer stata){
		if (stata == 1 || stata == 2) {
			return new AccessResult(false, "记录已审核,不能重复审核");
		}
		if(check==1){
			//记录表
			UserApplyRecord userApplyRecord =  userApplyRecordDao.get(UserApplyRecord.class, id);
			
			userApplyRecord.setState(1);
			userApplyRecord.setCheckPerson(user.getRealName());
			userApplyRecord.setCheckDate(new Date());
			userApplyRecordDao.update(userApplyRecord);
			//人员表
			Person person = personDao.get(Person.class, userApplyRecord.getPersonId());
			person.setState(1);
			personDao.update(person);  //将状态置为正常
			//微信表
			WeixinUser weixinUser = wxuserService.getWXUserByOpenId(person.getOpenid());
			if(weixinUser!=null){
				weixinUser.setInnerPerson(1);
				weixinUserdDao.update(weixinUser);
			}
			//审核通过加上大门,及申请的单元
			PersonUnit pu = new PersonUnit();
			pu.setId(UUIDUtil.generate());
			pu.setPersonId(person.getId());
			pu.setUnitId(userApplyRecord.getUnitId());
			//之前有的就不添加
			if(!query(pu)){
				personUnitDao.save(pu);
				//如果该人员已经绑定了门卡，将门卡发送到其绑定的单元及大门中
				if(StringUtils.isNotBlank(person.getCardId())){
					Map<String,Object> param1 = new HashMap<String, Object>();
					param1.put("unitId", userApplyRecord.getUnitId());
					String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId =:unitId ";
					List<UnitDevice> udList = unitDeviceDao.find(hql,param1);
					for(UnitDevice ud:udList){
						SendDataUtil.sendAddCardNew(person.getCardId(),ud.getDeviceId());
					}
				}
			}
			PersonUnit personUnit = new PersonUnit();
			personUnit.setId(UUIDUtil.generate());
			personUnit.setPersonId(person.getId());
			//大门单元ID
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put("communityId", user.getRegionId());
			String hql = DaoUtil.getFindPrefix(Unit.class)+" where communityId =:communityId and isGurad = 1"; //当前用户所在小区大门
			Unit doorUnit = unitDao.get(hql,param2);
			personUnit.setUnitId(doorUnit.getId());
			
			if(!query(personUnit)){
				personUnitDao.save(personUnit);
				if(StringUtils.isNotBlank(person.getCardId())){
					Map<String, Object> param3 = new HashMap<String, Object>();
					param3.put("unitId", doorUnit.getId());
					String h = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId =:unitId";
					List<UnitDevice> udList = unitDeviceDao.find(h);
					for(UnitDevice ud:udList){
						SendDataUtil.sendAddCardNew(person.getCardId(),ud.getDeviceId());
					}
				}
			}
			
			
			//微信短信发送
			//AccessResult  (true,"发送微信消息成功")  
			AccessResult accessResult = new AccessResult(false,"发送微信消息失败");
			try {
				accessResult = wxuserService.sendWeixinMessage(userApplyRecord.getId(),1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Unit unit = unitDao.get(Unit.class, userApplyRecord.getUnitId());
			Community community = communityDao.get(Community.class, unit.getCommunityId());
			String content = userApplyRecord.getName()+"申请审核"+community.getCommunityName()+unit.getUnitName()+"通过";
			operateRecordService.saveLog(user, 9, true, content);
			return new AccessResult(true,"操作成功,"+accessResult.getResultMsg());
		}
		//申请不通过
		if(check==0){
			//记录表
			UserApplyRecord userApplyRecord =  userApplyRecordDao.get(UserApplyRecord.class, id);
			userApplyRecord.setState(2);  //不通过
			userApplyRecord.setCheckPerson(user.getRealName());
			userApplyRecord.setCheckDate(new Date());
			userApplyRecordDao.update(userApplyRecord);
			//删除人员表数据 ,微信表改变状态
			//审核不通过就不会添加关联，之前申请的不受影响
			Person person = personDao.get(Person.class, userApplyRecord.getPersonId());
			String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId ='"+person.getId()+"'";
			List<PersonUnit> pu = personUnitDao.find(hql);
			WeixinUser weixinUser = wxuserService.getWXUserByOpenId(person.getOpenid());
			//再来删除这个人,但有可能是第一次申请未通过，或者之前申请通过，再申请未通过,分两种情况讨论
		    if(pu.size()==0){
		    	//第一次申请未通过
		    	weixinUser.setInnerPerson(0);
				weixinUserdDao.update(weixinUser);
		    	personDao.delete(person);
		    	
		    }
			//personDao.delete(person);
			//微信短信发送
			AccessResult accessResult = new AccessResult(false,"发送微信消息失败");
			try {
				accessResult = wxuserService.sendWeixinMessage(userApplyRecord.getId(), 2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    //未通过
			Unit unit = unitDao.get(Unit.class, userApplyRecord.getUnitId());
			Community community = communityDao.get(Community.class, unit.getCommunityId());
			String content = userApplyRecord.getName()+"申请审核"+community.getCommunityName()+unit.getUnitName()+"不通过";
			operateRecordService.saveLog(user, 9, true, content);
			return new AccessResult(true,"操作成功,"+accessResult.getResultMsg());
		}
		return new AccessResult(false, "操作失败");
	}
	
	/**
	 * 防止保存重复数据
	 * @param pu
	 * @return
	 */
	public boolean query(PersonUnit pu){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("personId", pu.getPersonId());
		param.put("unitId", pu.getUnitId());
		String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId =:personId  and unitId =:unitId";
		PersonUnit personUnit = personUnitDao.get(hql,param);
		//存在
		if(personUnit!=null){
			return true;
		}
		return false;
	}
	
	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public BaseDao<UserApplyRecord> getUserApplyRecordDao() {
		return userApplyRecordDao;
	}

	public void setUserApplyRecordDao(BaseDao<UserApplyRecord> userApplyRecordDao) {
		this.userApplyRecordDao = userApplyRecordDao;
	}

	public BaseDao<Person> getPersonDao() {
		return personDao;
	}

	public void setPersonDao(BaseDao<Person> personDao) {
		this.personDao = personDao;
	}

	public WxuserService getWxuserService() {
		return wxuserService;
	}

	public void setWxuserService(WxuserService wxuserService) {
		this.wxuserService = wxuserService;
	}

	public BaseDao<WeixinUser> getWeixinUserdDao() {
		return weixinUserdDao;
	}

	public void setWeixinUserdDao(BaseDao<WeixinUser> weixinUserdDao) {
		this.weixinUserdDao = weixinUserdDao;
	}

	public BaseDao<PersonUnit> getPersonUnitDao() {
		return personUnitDao;
	}

	public void setPersonUnitDao(BaseDao<PersonUnit> personUnitDao) {
		this.personUnitDao = personUnitDao;
	}
}
