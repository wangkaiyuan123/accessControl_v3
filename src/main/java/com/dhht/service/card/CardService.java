package com.dhht.service.card;

import java.util.ArrayList;
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
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.User;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.UnitDevice;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.region.RegionService;

@Service
public class CardService {
	@Resource
	private RegionService regionService;
	@Resource
	private BaseDao<DoorCard> doorCardDao;
	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private BaseDao<PersonUnit> personUnitDao;
	@Resource
	private BaseDao<UnitDevice> unitDeviceDao;
	/**
	 * 组织架构tree
	 * 
	 * @param user
	 * @return
	 */
	public List<Region> getTrees(User user) {
		List<Region> userRegions = new ArrayList<Region>();
		List<Region> regions = regionService.getRegionsTrees(user.getRegionId());
		// 机构储存
		Map<String, Region> map = new HashMap<String, Region>();
		for (Region region : regions) {
			map.put(region.getId(), region);
		}
		// 循环归类
		for (Region region : regions) {
			if (StringUtils.isNotBlank(region.getParentId())) {
				Region parentRegion = map.get(region.getParentId());
				if (null != parentRegion) {
					if (null == parentRegion.getChildren()) {
						List<Region> children = new ArrayList<Region>();
						parentRegion.setChildren(children);
					}
					parentRegion.getChildren().add(region);
				}
			}
			if (user.getLevel() == 0) {
				if (region.getLevel() == 1)
					userRegions.add(region);
			} else {
				if (user.getLevel() == region.getLevel())
					userRegions.add(region);
			}
		}
		return userRegions;
	}

	/**
	 * 门卡列表
	 * @return
	 */
    public List<DoorCard> getCardList(DoorCard card,int page,int rows,User user){
    	List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元及大门
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
    	String sql = "SELECT d.* FROM doorcard d,person_unit pu WHERE d.userId = pu.personId AND pu.unitId IN ("+regionsIdStr+")";
    	if(!StringUtils.isBlank(card.getCardNo())){
    		sql += " AND cardNo ='"+card.getCardNo()+"'";
    	}
    	sql+="GROUP BY cardNo";
		List<DoorCard> cards = doorCardDao.findBySql(sql, DoorCard.class, page, rows);
		for(DoorCard c:cards){
			if(StringUtils.isBlank(c.getUserId())){
				c.setName("");
				continue;
			}
			Person person = personDao.get(Person.class, c.getUserId());
			if(person!=null){
				c.setName(person.getName());
				continue;
			}
			c.setName("");
		}
		return cards;
	}
	
    public List<DoorCard> getList(){
		String hql = DaoUtil.getFindPrefix(DoorCard.class);
		return doorCardDao.find(hql);
	}
    
    public void saveOrUpdate(DoorCard card){
    	doorCardDao.saveOrUpdate(card);
    }
    
    public long getCount(DoorCard card,User user){
    	List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元及大门
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
    	String sql = "select count(*) from (SELECT d.* FROM doorcard d,person_unit pu WHERE d.userId = pu.personId AND pu.unitId IN ("+regionsIdStr+")";
    	if(!StringUtils.isBlank(card.getCardNo())){
    		sql += " AND cardNo ='"+card.getCardNo()+"'";
    	}
    	sql+="GROUP BY cardNo) a";
    	Integer conut = doorCardDao.countSQLQuery(sql);
    	if(conut!=null&&conut>0){
    		return (long)conut;
    	}
    	return 0;
    }
    
	/**
	 * 保存绑定人员
	 * @param personId
	 * @return AccessResult
	 */
	public AccessResult saveBindPerson(String personId,DoorCard doorCard){
		DoorCard card = doorCardDao.get(DoorCard.class, doorCard.getId());
		card.setUserId(personId);
		//同时更新人员卡号信息
		Person person = personDao.get(Person.class, personId);
		person.setCardId(card.getCardNo());
		personDao.update(person);
		doorCardDao.update(card);
		//同时得与设备通信,将卡的信息写入到关联的设备中
		//1,人员关联的所有设备
		//2.遍历发送卡号SendDataUtil.sendAddCard(card.getCardNo(), deviceId);
		//String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId = '"+card.getUserId()+"'";
		String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId =:personId";
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("personId", card.getUserId());
		List<PersonUnit> pu = personUnitDao.find(hql, param1);
		//人员所关联的单元
		for(PersonUnit personUnit:pu){
			//String h = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+personUnit.getUnitId()+"'";
			String h = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId =:unitId";
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put("unitId", personUnit.getUnitId());
			List<UnitDevice> unitDevices = unitDeviceDao.find(h);
			//单元关联的设备
			for(UnitDevice unitDevice:unitDevices){
				//绑定卡时,将卡关联的所有设备找到，并将卡信息发送到所有设备去
				SendDataUtil.sendAddCardNew(card.getCardNo().substring(2, 8), unitDevice.getDeviceId());
			}
		}
		return new AccessResult(true,"绑定人员成功！");
	}
	
	/**
	 * 保存新增卡号
	 * @param card
	 * @return
	 */
	public AccessResult saveAdd(DoorCard card){
		DoorCard doorCard = new DoorCard();
		doorCard.setId(UUIDUtil.generate());
		doorCard.setCardNo(card.getCardNo()); //卡号
		doorCard.setCardNumber(card.getCardNumber()); //卡串码
		doorCard.setRecentTime(new Date());  //新增时间
		doorCardDao.save(doorCard);
		return new AccessResult(true,"新增门卡成功");
	}
	
	/**
	 * 删除卡号
	 * @param card
	 * @return AccessResult
	 */
	public AccessResult deleteCard(DoorCard doorCard){
		DoorCard card = doorCardDao.get(DoorCard.class, doorCard.getId());
		if(StringUtils.isBlank(card.getUserId())){
			return new AccessResult(true,"删除成功");
		}
		//1.人员关联的所有设备
		//2.遍历发送卡号sendDeleteCard(card.getCardNo(), deviceId);
		//String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId = '"+card.getUserId()+"'";
		String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId =:personId";
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("personId", card.getUserId());
		List<PersonUnit> pu = personUnitDao.find(hql,param1);
		//人员所关联的单元
		for(PersonUnit personUnit:pu){
			//String h = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+personUnit.getUnitId()+"'";
			String h = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId =:unitId";
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put("unitId", personUnit.getUnitId());
			List<UnitDevice> unitDevices = unitDeviceDao.find(h);
			//单元关联的设备
			for(UnitDevice unitDevice:unitDevices){
				//绑定卡时,将卡关联的所有设备找到，并将卡信息发送到所有设备去
				SendDataUtil.sendDeleteCardNew(card.getCardNo(), unitDevice.getDeviceId());
			}
		}
		
		doorCardDao.delete(card);
		return new AccessResult(true,"删除成功");
	}
	
	public RegionService getRegionService() {
		return regionService;
	}


	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public BaseDao<DoorCard> getDoorCardDao() {
		return doorCardDao;
	}

	public void setDoorCardDao(BaseDao<DoorCard> doorCardDao) {
		this.doorCardDao = doorCardDao;
	}

	public BaseDao<Person> getPersonDao() {
		return personDao;
	}

	public void setPersonDao(BaseDao<Person> personDao) {
		this.personDao = personDao;
	}

	public BaseDao<PersonUnit> getPersonUnitDao() {
		return personUnitDao;
	}

	public void setPersonUnitDao(BaseDao<PersonUnit> personUnitDao) {
		this.personUnitDao = personUnitDao;
	}

	public BaseDao<UnitDevice> getUnitDeviceDao() {
		return unitDeviceDao;
	}

	public void setUnitDeviceDao(BaseDao<UnitDevice> unitDeviceDao) {
		this.unitDeviceDao = unitDeviceDao;
	}
	
	
}
