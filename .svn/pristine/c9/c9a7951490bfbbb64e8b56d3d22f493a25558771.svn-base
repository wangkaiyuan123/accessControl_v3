package com.dhht.service.person;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.Dictionary;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.community.Community;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.doorguard.DeviceCard;
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.entity.record.UserApplyRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.PersonType;
import com.dhht.entity.system.SysRoleUser;
import com.dhht.entity.system.User;
import com.dhht.entity.template.newData;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.Unit;
import com.dhht.entity.unit.UnitDevice;
import com.dhht.entity.weixin.SMSCode;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.device.DeviceService;
import com.dhht.service.record.OperateRecordService;
import com.dhht.service.region.RegionService;
import com.dhht.service.system.UserService;
import com.dhht.service.unit.UnitService;
import com.dhht.service.weixin.WxuserService;
import com.sun.xml.internal.bind.v2.model.core.ID;
@Service
public class PeopleService  {
	
	@Resource
	private BaseDao<PersonType> personTypeDao;
	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private BaseDao<Unit> unitDao;
	@Resource
	private BaseDao<Community> communityDao;
    @Resource
    private RegionService regionService;
    @Resource
    private UnitService unitService;
    @Resource
    private BaseDao<WeixinUser> wDao;
    @Resource
    private BaseDao<PersonUnit> personUnitDao;
    @Resource
    private WxuserService wxuserService;
    @Resource
    private BaseDao<Region> regionDao;
    @Resource
    private BaseDao<User> userDao;
    @Resource
    private UserService userService;
    @Resource
    private BaseDao<SysRoleUser> sysRoleUserDao;
    @Resource
    private BaseDao<UnitDevice> unitDeviceDao;
    @Resource
    private BaseDao<DoorCard> doorCardDao;
    @Resource
    private BaseDao<Device> deviceDao;
    @Resource
    private BaseDao<DeviceCard> deviceCardDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private BaseDao<SMSCode> sMSCodeDao;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private BaseDao<UserApplyRecord> userApplyRecordDao;
	/**
	 * 获取用户列表(身份证/电话查询)
	 * @return
	 */
	public List<Person> getPersonList(Person person,int page,int rows,String regionId,User user){
		
		List<Region> regions =  regionService.getDevicesByRegions(regionId);   //包涵单元及小区
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
		List<Person> pList = findPersonByRegion(person,page,rows,regionsIdStr,regionId,user);
		return pList;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getCount(Person person,String regionId){
		List<Region> regions =  regionService.getRegionsTrees(regionId);   //包涵单元
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
		String str = DaoUtil.getFindPrefix(PersonUnit.class)+" where unitId in ("+regionsIdStr+")";
		
		List<PersonUnit> puList = personUnitDao.find(str);  //人员单元关联数据集合
		Set<String>  personStrs = new HashSet<String>();
		for(PersonUnit pu:puList){
			personStrs.add(pu.getPersonId());
		}
		String personIdStr = DaoUtil.generateInStr(personStrs);   //存的当前小区所有人的集合
		
		String hql = DaoUtil.getFindPrefix(Person.class)+" where id in ("+personIdStr+")";
		if(!StringUtils.isBlank(person.getIdentifyId())){
			hql+=" and identifyId = '"+person.getIdentifyId()+"'";
		}
		if(!StringUtils.isBlank(person.getTelephone())){
			hql+=" and telephone = '"+person.getTelephone()+"'";
		}
		List<Person> pList = personDao.find(hql);
		return (long)pList.size();
	}
	
	/**
	 * 
	 * @param person
	 * @param page
	 * @param rows
	 * @param regionsIdStr   机构的集合
	 * @return
	 */
	private List<Person> findPersonByRegion(Person person,int page,int rows,String regionsIdStr,String regionId,User user) {
		String str = DaoUtil.getFindPrefix(PersonUnit.class)+" where unitId in ("+regionsIdStr+")";
		List<PersonUnit> puList = personUnitDao.find(str);  //人员单元关联数据集合
		Set<String>  personStrs = new HashSet<String>();
		for(PersonUnit pu:puList){
			personStrs.add(pu.getPersonId()); //过滤掉相同的人
		}
		String personIdStr = DaoUtil.generateInStr(personStrs);   //存的当前小区所有人的集合
		
		//申请后未审核通过，即使Person表中有此人，但关联表中未添加也是找不出来的
		String hql = DaoUtil.getFindPrefix(Person.class)+" where id in ("+personIdStr+")";   
		if(!StringUtils.isBlank(person.getIdentifyId())){
			hql+=" and identifyId = '"+person.getIdentifyId()+"'";
		}
		if(!StringUtils.isBlank(person.getTelephone())){
			hql+=" and telephone = '"+person.getTelephone()+"'";
		}
		List<Person> pList = personDao.find(hql, null, page, rows);
		//本小区单元集合
		List<Region> regions = null;
		if(StringUtils.isBlank(user.getRegionId())){ //超级管理员或运维人员
			regions =  regionService.getDevicesByRegions(regionId);
		}else{
			regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元
		}
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStrs = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
		for(Person p:pList){
			//过滤掉非本小区的人员单元关联
			String h = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId='"+p.getId()+"' and unitId in ("+regionsIdStrs+")";  //可能一个人与多个单元关联
			List<PersonUnit> personUnits = personUnitDao.find(h);
			//存放单元集合
			List<String> unitIds =new ArrayList<String>();
			String unitName ="";
			for(PersonUnit pu :personUnits){
				String string = DaoUtil.getFindPrefix(Unit.class)+" where id ='"+pu.getUnitId()+"' and isGurad = 1";
				Unit dUnit = unitDao.get(string);
				
				//过滤掉大门,只留单元及非本小区的单元
				if(dUnit!=null){
					continue;
				}
				Unit unit = unitDao.get(Unit.class, pu.getUnitId());
				if(unit!=null){
					Community community = communityDao.get(Community.class, unit.getCommunityId());
					if(community!=null){
						unitName+=community.getCommunityName()+unit.getUnitName()+"，";
					}
					unitIds.add(pu.getUnitId());  //只有单元，没有大门
				}
			}
			if(!StringUtils.isBlank(unitName)){
				p.setUnitName(unitName.substring(0, unitName.length()-1));
			}
			p.setUnitIds(unitIds);
			if(!StringUtils.isBlank(p.getOpenid())&&wxuserService.getWXUserByOpenId(p.getOpenid())!=null){
				p.setNeckName(wxuserService.getWXUserByOpenId(p.getOpenid()).getNickname());  //前端展示字段
			}else{
				p.setNeckName("");
			}
			p.setPersonType(personTypeDao.get(PersonType.class, p.getPersonTypeId()).getTypeName());
			p.setRegionId(regionId);
            			
		}
		return pList;
	}

	
	
   /**
    * 组织架构tree
    * @param user
    * @return
    */
	public List<Region> getTrees(User user){
		List<Region> userRegions = new ArrayList<Region>();
		List<Region> regions= regionService.getRegionsTrees(user.getRegionId());
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
	 * 保存用户
	 * @param person
	 * @return
	 */
	public AccessResult savePerson(Person person,String unitId,String code,User user){
		Map<String, Object> param = new HashMap<String, Object>();
		//验证验证码是否正确
		AccessResult result = checkCode(code, person.getTelephone());
		if(!result.isSuccess()){
			return result;
		}
		//先查看新增的卡号有没有
		String hqlString = DaoUtil.getFindPrefix(DoorCard.class)+" where cardNo = '"+person.getCardId()+"'";
		//String hqlString = DaoUtil.getFindPrefix(DoorCard.class)+" where cardNo =:cardNo";
		DoorCard doorCard = doorCardDao.get(hqlString);
		//如果之前的卡号存在就不新增
		if(doorCard!=null){
			return new AccessResult(false,"新增人员卡号不能相同");
		}
		if(!StringUtils.isBlank(person.getCardId())&&person.getCardId().length()!=10){
			return new AccessResult(false,"卡号长度必须为10位");
		}
	    if(queryPersonBeforeSave(person.getIdentifyId())){
	    	person.setId(UUIDUtil.generate());
	    	person.setCardId(person.getCardId().substring(2, 8));
	    	person.setState(1);  //状态正常
	    	if("".equals(person.getOpenid())){
	    		person.setOpenid(null);
	    	}
	    	personDao.save(person);
	    	//选择绑定微信用户
	    	if(!StringUtils.isBlank(person.getOpenid())){
	    		WeixinUser wxUser = wxuserService.getWXUserByOpenId(person.getOpenid());
	    		wxUser.setInnerPerson(1); //改为内部人员
	    		wDao.update(wxUser);
	    	}
	    	
	    	//保存数据的同时,存入人员单元关联表,
	    	PersonUnit personUnit = new PersonUnit();
	    	personUnit.setId(UUIDUtil.generate());
	    	personUnit.setPersonId(person.getId());
	    	personUnit.setUnitId(unitId);
	    	personUnitDao.save(personUnit);
	    	//同时加入人员小区大门单元关联表
	    	PersonUnit personDoorUnit = new PersonUnit();
	    	personDoorUnit.setId(UUIDUtil.generate());
	    	personDoorUnit.setPersonId(person.getId());
	    	Unit unit = unitDao.get(Unit.class, unitId);   //这人关联的单元
	    	String h = DaoUtil.getFindPrefix(Unit.class)+" where communityId = '"+unit.getCommunityId()+"' and isGurad = 1";
	    	//String h = DaoUtil.getFindPrefix(Unit.class)+" where communityId =:communityId and isGurad = 1";
	    	//本小区大门单元
	    	Unit doorUnit = unitDao.get(h);
	    	personDoorUnit.setUnitId(doorUnit.getId());
	    	personUnitDao.save(personDoorUnit);
	    	
	    	//同时将绑定的卡加上去
	    	if(!StringUtils.isBlank(person.getCardId())){
	    		DoorCard card = new DoorCard();
	    		card.setId(UUIDUtil.generate());
	    		card.setCardNo(person.getCardId());
	    		card.setCardNumber("");
	    		card.setComment("");
	    		card.setRecentTime(new Date());
	    		card.setUserId(person.getId());
	    		doorCardDao.save(card);
	    	}
	    	
	    	//往新增人员时关联的单元(默认小区大门和新增的单元)所关联的设备上发送卡号
	    	//String strHql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId = '"+person.getId()+"'";
	    	String strHql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId =:personId";
	    	param.put("personId", person.getId());
	    	List<PersonUnit> puList = personUnitDao.find(strHql,param);  //这里至少应该是由两个的
	    	for(PersonUnit pUnit:puList){
	    		String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+pUnit.getUnitId()+"'";
	    		List<UnitDevice> udList = unitDeviceDao.find(hql);
	    		for(UnitDevice ud:udList){
	    			//新增卡的时候也要把卡号发过去，
	    			if(!StringUtils.isBlank(person.getCardId())){
	    				SendDataUtil.sendAddCardNew(person.getCardId(),ud.getDeviceId());
	    				//设备门卡关联表
	    				DeviceCard deviceCard = new DeviceCard();
	    				deviceCard.setId(UUIDUtil.generate());
	    				deviceCard.setCardNo(person.getCardId());
	    				Device device = deviceDao.get(Device.class, ud.getDeviceId());
	    				deviceCard.setMac(device.getDeviceMac());
	    				deviceCardDao.save(deviceCard);
	    			}
	    		}
	    	}
	    	String json = "新增人员： "+person.getName();
	    	operateRecordService.saveLog(user, 4, true,json);
	    	return new AccessResult(true,"新增人员成功");
	    }
	    String json = "新增人员： "+person.getName();
	    operateRecordService.saveLog(user, 4, false,json);
		return new AccessResult(false,"新增人员身份证号重复");
	}
	/**
	 * 删除人员
	 * @return AccessResult对象
	 */
	public AccessResult deletePerson(User user,Person person,String regionId,String unitArrays){
		List<String> unitIdList  = DaoUtil.parseJsonStrToList(unitArrays);  //存的仅仅只是本小区的集合
		String  h = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId ='"+person.getId()+"'";
		Person p =  personDao.get(Person.class,person.getId());
		//只关联了一个单元,紧紧只针对一个小区而言
		if(unitIdList.size()==1){
			//人员单元关联表给删掉
			List<PersonUnit> pus = personUnitDao.find(h);
			//意味着在其他小区还绑定了小区单元
			if(pus.size()>2){
				String  hql = DaoUtil.getFindPrefix(Region.class)+" where id ='"+unitIdList.get(0).split("\"")[1]+"'";
				String communityId = regionDao.get(hql).getParentId();
				String sql = "SELECT * FROM person_unit WHERE unitId IN (SELECT id FROM unit WHERE communityId ='"+communityId+"') and personId = '"+
						person.getId()+"'" ;
				List<PersonUnit> personUnits = personUnitDao.findBySql(sql, PersonUnit.class);
				for(PersonUnit pu:personUnits){
					personUnitDao.delete(pu);
				}
			}
			String content = "";
			Unit unit = unitDao.get(Unit.class, regionId);
			Community community = communityDao.get(Community.class, unit.getCommunityId());
			content = "将人员"+p.getName()+"从"+community.getCommunityName()+unit.getUnitName()+"删除";
			operateRecordService.saveLog(user, 6, true,content);
			
			//同时删除门卡设备关联表，及发送删除卡号指令,前提是这个人的有门卡
			if(!StringUtils.isBlank(p.getCardId())){  //有门卡
				String macStrs  = DaoUtil.generateInStr(deviceService.getDeviceList(user));  //本小区的mac
				String hql = DaoUtil.getFindPrefix(DeviceCard.class)+" where cardNo ='"+p.getCardId()+"' and mac IN ("+macStrs+")";
				//可能这张卡关联多个设备
				List<DeviceCard> deviceCards = deviceCardDao.find(hql); //存进去最好别给删掉，方便到时候1000删除,找用用的卡(后期)
				if(deviceCards!=null&&deviceCards.size()>0){
					for(DeviceCard deviceCard:deviceCards){
						SendDataUtil.sendDeleteCardNew(p.getCardId(),deviceService.getDeviceByMac(deviceCard.getMac()).getId()); //（门卡Id，设备Id
						//更改门卡设备关联表
						deviceCardDao.delete(deviceCard);
						
					}
				}
				//同时删掉卡号
				String strHql = DaoUtil.getFindPrefix(DoorCard.class)+" where cardNo = '"+p.getCardId()+"'";
				if(pus.size()==2){
					DoorCard card = doorCardDao.get(strHql);
					if(card!=null){
						//删掉卡
						doorCardDao.delete(card);
					}
				}
			}
			if(pus.size()<=2){
				for(PersonUnit pu:pus){
					personUnitDao.delete(pu);
				}
				personDao.delete(p);
				
				//同时将微信用户的状态置为原始状态
				if(!StringUtils.isBlank(p.getOpenid())){
					WeixinUser wxUser = wxuserService.getWXUserByOpenId(p.getOpenid());
					wxUser.setInnerPerson(0); //改为非内部人员
					wDao.update(wxUser);
				}
			}
			
			
			return new AccessResult(true,"删除人员成功");
		}
		//关联多个单元,只删除其中某个关联单元，
		String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId ='"+person.getId()+"' and unitId = '"+regionId+"'";
		PersonUnit personUnit = personUnitDao.get(hql);
//		if(personUnit==null){
//			//说明其上级机构操作的
//			List<PersonUnit> puList = personUnitDao.find(h);
//			for(PersonUnit pu:puList){
//				personUnitDao.delete(pu);
//			}
//		    //还要将其绑定的卡号都从设备上删掉,发送卡号删除指令，其实不是删除，只是改变下设备里面的门卡状态
//			for(PersonUnit pu:puList){
//				String str = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+pu.getUnitId()+"'";
//				List<UnitDevice> udList = unitDeviceDao.find(str);
//				for(UnitDevice unitDevice:udList){
//					if(!StringUtils.isBlank(p.getCardId())){
//						SendDataUtil.sendDeleteCard(p.getCardId(), unitDevice.getDeviceId());
//					}
//				}
//			}
//			//再删除绑定的卡
//			if(!StringUtils.isBlank(p.getCardId())){
//				String str = DaoUtil.getFindPrefix(DoorCard.class)+" where cardNo = '"+p.getCardId()+"'";
//				DoorCard card = doorCardDao.get(str);
//				if(card!=null){
//					doorCardDao.delete(card);
//				}
//			}
//			//直接删掉这个人
//			personDao.delete(p);
//			return new AccessResult(true,"删除人员成功");
//		}
		//如果仅仅只是将这个人从本单元删掉，人先不删，先将人关联单元的设备上的卡给删掉
		if(personUnit!=null){
			String str = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+personUnit.getUnitId()+"'";
			List<UnitDevice> udList = unitDeviceDao.find(str);
			for(UnitDevice unitDevice:udList){
				if(!StringUtils.isBlank(p.getCardId())){
					SendDataUtil.sendDeleteCardNew(p.getCardId(), unitDevice.getDeviceId());
				}
			}
			personUnitDao.delete(personUnit);
		}
		
		Unit unit = unitDao.get(Unit.class, regionId);
		Community community = communityDao.get(Community.class, unit.getCommunityId());
		String content = "将人员"+p.getName()+"从"+community.getCommunityName()+unit.getUnitName()+"删除";
		operateRecordService.saveLog(user, 6, true,content);
		return new AccessResult(true,"删除人员成功");
	}
	
	/**
	 * 修改人员
	 * @return
	 */
	public AccessResult updatePerson(Person person,String code,User user){
		//未修改电话情况下，不用验证
		if(StringUtils.isNotBlank(code)){
			//验证码
			AccessResult result = checkCode(code, person.getTelephone());
			if(!result.isSuccess()){
				return result;
			}
		}
		
        //查出之前的数据
		boolean flag = false;
		Person p = personDao.get(Person.class, person.getId()); 
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("personId", person.getId());
		String h = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId =:personId";
		List<PersonUnit> puLists = personUnitDao.find(h,param1);
		 
		//先删除门卡,其实不是删除，只是改变下设备里面的门卡状态
		for(PersonUnit pu:puLists){
			String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+pu.getUnitId()+"'";
			List<UnitDevice> udList = unitDeviceDao.find(hql);
			if(udList!=null&&udList.size()>0){
				for(UnitDevice unitDevice:udList){
					//如果卡号有修改,将之前的卡从设备删掉
					if(person.getCardId().length()==10||person.getCardId().length()==6){
						if(!StringUtils.isBlank(p.getCardId())&&!p.getCardId().equals(person.getCardId().substring(2, 8))){ //卡号有所改变
							SendDataUtil.sendDeleteCardNew(p.getCardId(), unitDevice.getDeviceId());
							//更改门卡设备关联表
							Device d = deviceDao.get(Device.class,unitDevice.getDeviceId());
							String str = DaoUtil.getFindPrefix(DeviceCard.class)+" where cardNo ='"+p.getCardId()+"' and mac = '"+d.getDeviceMac()+"'";
							DeviceCard dc = deviceCardDao.get(str);
							if(dc!=null){
								deviceCardDao.delete(dc);
							}
						}
					}
				}
			}  
		}
		if(!StringUtils.isBlank(p.getCardId())){  //之前有卡,现在换卡号
			//先删除之前的门卡，再添加新的门卡
			String hql = DaoUtil.getFindPrefix(DoorCard.class)+" where cardNo = '"+p.getCardId()+"'";
			DoorCard doorCard = doorCardDao.get(hql);
			//如果卡号不相同,就删除
			if(!person.getCardId().equals(p.getCardId())){   //!p.getCardId().equals(person.getCardId())
				flag = true;
				if(doorCard!=null){
					//删掉之前的卡
					doorCardDao.delete(doorCard);
					//再添加新的门卡
					DoorCard newCard = new DoorCard();
					newCard.setId(UUIDUtil.generate());
					newCard.setCardNumber("");
					newCard.setComment("");
					newCard.setRecentTime(new Date());
					newCard.setUserId(person.getId());
					if(person.getCardId().length()==10){
						newCard.setCardNo(person.getCardId().substring(2, 8));
					}
					if(StringUtils.isNotBlank(person.getCardId())&&!p.getCardId().equals(person.getCardId().substring(2, 8))){
						doorCardDao.save(newCard);
					}
				}
			}
		}else{  //之前没卡，现在换卡号
			DoorCard newCard = new DoorCard();
			newCard.setId(UUIDUtil.generate());
			newCard.setCardNumber("");
			newCard.setComment("");
			newCard.setRecentTime(new Date());
			newCard.setUserId(person.getId());
			if(person.getCardId().length()==10){
				newCard.setCardNo(person.getCardId().substring(2, 8));
			}
			if(StringUtils.isNotBlank(person.getCardId())){
				doorCardDao.save(newCard);
			}
		}
			
		//操作记录
		StringBuilder sbf = new StringBuilder();
		if(!person.getName().equals(p.getName())){
			sbf.append(p.getName()+"的名字改为："+person.getName()+";");
		}
		if(person.getSex()!=p.getSex()){
			if(person.getSex()==1){
				sbf.append(p.getName()+"的性别改为：男 ;");
			}else {
				sbf.append(p.getName()+"的性别改为：女 ;");
			}
		}
		if(person.getAge()!=p.getAge()){
			sbf.append(p.getName()+"年龄改为："+person.getAge()+";");
		}
		if(!person.getCardId().equals(p.getCardId())){
			sbf.append(p.getName()+"的卡号 "+p.getCardId()+"改为："+person.getCardId()+";");
		}
		if(!person.getIdentifyId().equals(p.getIdentifyId())){
			sbf.append(p.getName()+"的身份证改为："+person.getIdentifyId()+";");
		}
		if(!person.getTelephone().equals(p.getTelephone())){
			sbf.append(p.getName()+"的电话改为：" + person.getTelephone()+";");
		}
		if(!person.getPersonTypeId().equals(p.getPersonTypeId())){
			PersonType pt = personTypeDao.get(PersonType.class, person.getPersonTypeId());
			sbf.append(p.getName()+"的类型改为："+pt.getTypeName()+";");
		}
		if(StringUtils.isBlank(p.getOpenid())){  //绑定微信用户
			if(StringUtils.isNotBlank(person.getOpenid())){
				WeixinUser wx =  wxuserService.getWXUserByOpenId(person.getOpenid());
				wx.setInnerPerson(1);
				wDao.update(wx); //设置为内部人员
				sbf.append(p.getName()+"绑定微信为："+wx.getNickname());
			}
		}
		if(StringUtils.isNotBlank(p.getOpenid())&&StringUtils.isBlank(person.getOpenid())){
			sbf.append(p.getName()+"取消微信绑定用户");
		}
		if(StringUtils.isNotBlank(p.getOpenid())&&StringUtils.isNotBlank(person.getOpenid())){
			WeixinUser wx =  wxuserService.getWXUserByOpenId(person.getOpenid());
			sbf.append(p.getName()+"修改绑定微信用户为："+wx.getNickname());
		}
		if(StringUtils.isNotBlank(sbf.toString())){
			operateRecordService.saveLog(user, 5, true, sbf.toString());
		}
		//修改之后
		p.setName(person.getName());
		p.setAge(person.getAge());
		p.setSex(person.getSex());
		if(person.getCardId().length()==10){
			p.setCardId(person.getCardId().substring(2, 8));
		}
		p.setIdentifyId(person.getIdentifyId());
		p.setOpenid(person.getOpenid());
		p.setPersonTypeId(person.getPersonTypeId());
		p.setTelephone(person.getTelephone());
		personDao.update(p);
		
		//编辑的时候得先发送删除卡号指令，再发送卡号
		for(PersonUnit pu:puLists){
			String str = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+pu.getUnitId()+"'";
			List<UnitDevice> udList = unitDeviceDao.find(str);
			for(UnitDevice unitDevice:udList){
				//如果是修改了卡号，则将修改之后的卡号给发送到设备上去
				if(!StringUtils.isBlank(p.getCardId())){
					SendDataUtil.sendAddCardNew(person.getCardId().substring(2, 8), unitDevice.getDeviceId());
					//设备门卡关联表
    				DeviceCard deviceCard = new DeviceCard();
    				deviceCard.setId(UUIDUtil.generate());
    				deviceCard.setCardNo(person.getCardId().substring(2, 8));  
    				Device device = deviceDao.get(Device.class, unitDevice.getDeviceId());
    				deviceCard.setMac(device.getDeviceMac());
    				deviceCardDao.save(deviceCard);
				}
			}
		}
//				//同时修改人员单元关联表  (有可能关联多个单元)
//				String  hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId ='"+person.getId()+"'";
//				List<PersonUnit> puList = personUnitDao.find(hql);
//				//先删除
//				for(PersonUnit pu:puList){
//					PersonUnit personUnit = personUnitDao.get(PersonUnit.class,pu.getId());
//					personUnitDao.delete(personUnit);
//				}
//				//再添加
//				PersonUnit personUnit = new PersonUnit();
//				personUnit.setId(UUIDUtil.generate());
//				personUnit.setPersonId(person.getId());
//				personUnit.setUnitId(unitId);
//				personUnitDao.save(personUnit);
		
//		String json = 
//		operateRecordService.saveLog(user, 5, true,json);
		return new AccessResult(true,"修改人员成功");
	}
	
	/**
	 * 获取用户类型
	 * @return
	 */
	public List<PersonType> getPersonTypes(){
		String hql = DaoUtil.getFindPrefix(PersonType.class);
		return personTypeDao.find(hql);
	}
 
	/**
	 * 根据登录的用户获取单元列表
	 * @return
	 */
	public List<Unit>  getUnits(User user){
		List<Region> regions = regionService.getRegions(user.getRegionId());
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);
		List<Unit> units = findByIds(regionsIdStr);
		return units;
	}
	
	private List<Unit> findByIds(String regionsIdStr) {
		String hql = DaoUtil.getFindPrefix(Unit.class)+" where communityId in ("+regionsIdStr+") and isGurad = 0";
		List<Unit> units = unitDao.find(hql);
		for(Unit unit :units){
			String unitNameString = communityDao.get(Community.class, unit.getCommunityId()).getCommunityName()+unit.getUnitName();
			unit.setCommunityName(unitNameString);
		}
		return units;
	}

	/**
	 * 获取微信人员关注列表
	 * @return
	 */
	public List<WeixinUser> getWeiXinUsers(int page,int rows,String account){
		String hql = DaoUtil.getFindPrefix(WeixinUser.class);//+" order by addtime DESC";
		if(!StringUtils.isBlank(account)){
			hql+=" where nickname LIKE '%"+account+"%' or account = '"+account+"'";
		}
		hql+=" order by addtime DESC";
		return wDao.find(hql, null, page, rows);
	}
	
	/**
	 * 获取非内部人员微信列表（未申请/审核未通过的）
	 * @param page
	 * @param rows
	 * @param account
	 * @return
	 */
	public List<WeixinUser> getNoWeiXinUser(int page,int rows,String account){
		String hql = DaoUtil.getFindPrefix(WeixinUser.class)+" where 1=1 ";
		if(!StringUtils.isBlank(account)){
			hql+=" and nickname LIKE '%"+account+"%' or account = '"+account+"'";
		}
		hql+=" and innerPerson IN (0,2) order by addtime DESC";
		return wDao.find(hql, null, page, rows);
	}
	
	/**
	 * 非内部
	 * @param account
	 * @return
	 */
	public long getNoWxUserCount(String account){
		String hql = DaoUtil.getFindPrefix(WeixinUser.class)+" where 1=1 ";
		if(!StringUtils.isBlank(account)){
			hql+=" and nickname LIKE '%"+account+"%' or account = '"+account+"'";
		}
		hql+=" and innerPerson = 0 order by addtime DESC";
		return (long)wDao.find(hql).size();
	}
	
	
	/**
	 * 获取总记录数
	 * @param account
	 * @return
	 */
	public long getWxUserCount(String account){
		String hql = DaoUtil.getFindPrefix(WeixinUser.class);//+" order by addtime DESC";
		if(!StringUtils.isBlank(account)){
			hql+=" where nickname LIKE '%"+account+"%' or account = '"+account+"' order by addtime DESC";
		}
		return (long)wDao.find(hql).size();
	}
	
	//新增人员时判断是否存在(身份证)
	public boolean queryPersonBeforeSave(String identifyId){
		String hql = DaoUtil.getFindPrefix(Person.class)+" where identifyId = '"+identifyId+"'";
		Person person =  personDao.get(hql);
		if(person==null){
			return true;
		}
		return false;
	}
	
	//验证码验证
	public AccessResult checkCode(String codeNum,String phone){
		
		Long nowtime = new Date().getTime();
		String hql = DaoUtil.getFindPrefix(SMSCode.class)+" where phone = '"+phone+"'";
		SMSCode code = sMSCodeDao.get(hql);
		if(code==null){
    		return new AccessResult(false, "请点击发送验证码");
    	}
    	if(!code.getSmscode().equals(codeNum)){
    		return new AccessResult(false, "验证码错误");
    	}
    	if(nowtime-code.getLastTime()>60000){
    		return new AccessResult(false, "验证码超时，请重新发送");
    	}
		return new AccessResult(true,"效验通过"); 
	}
	
	//保存绑定单元(添加到人员单元关联表)
	public AccessResult saveAddUnits(Person person ,String unitId,User user){
		Person p = personDao.get(Person.class, person.getId());
		PersonUnit personUnit = new  PersonUnit();
		personUnit.setId(UUIDUtil.generate());
		personUnit.setPersonId(person.getId());
		personUnit.setUnitId(unitId);
		//防止同一个人重复绑定相同的单元
		if(queryAdd(person.getId(),unitId)){
			personUnitDao.save(personUnit);
			//同时把该人员的卡号发送到新绑定单元所关联的门禁设备中去
			String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where unitId = '"+unitId+"'";
			List<UnitDevice> unitDevices = unitDeviceDao.find(hql);
			for(UnitDevice ud :unitDevices){
				if(StringUtils.isNotBlank(p.getCardId())&&p.getCardId().length()==6){
					SendDataUtil.sendAddCardNew(p.getCardId(),ud.getDeviceId());
				}
			}
 			Unit unit = unitDao.get(Unit.class, unitId);
			Community community = communityDao.get(Community.class, unit.getCommunityId());
			String content = "将"+p.getName()+"重新绑定到"+community.getCommunityName()+unit.getUnitName();
			operateRecordService.saveLog(user, 7, true, content);
			return new AccessResult(true,"绑定单元成功");
		}
		//
		return new AccessResult(false,"已绑定改单元,请选择其他单元进行绑定");
	}
	
	//防止重复绑定
	public boolean queryAdd(String personId,String unitId){
		String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where personId = '"+personId+"' and unitId = '"+unitId+"'";
		PersonUnit personUnit = personUnitDao.get(hql);
		if(personUnit==null){
			return true;
		}
		return false;
	}
	
	//物业人员升级为管理员(只能升级为本小区的管理员)
	public AccessResult updateGly(Person person,User user){
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("personId", person.getId());
		String sql = "SELECT t1.* FROM region t1 INNER JOIN (SELECT * FROM person_unit WHERE personId =:personId GROUP BY unitId) t2 " +
				" WHERE t1.id = t2.unitId GROUP BY t1.parentId";
		
		PersonType personType = personTypeDao.get(PersonType.class, person.getPersonTypeId());
		//如果该人员为物业人员
		if(personType.getTypeName().equals(Dictionary.PERSON_1)){
			List<Region> regions = regionDao.findBySql(sql, param1, Region.class); 
			for(Region region:regions){
				if(region.getParentId().equals(user.getRegionId())){  //与登录人所在的小区Id相同
					//添加个管理员账号
					User u = new User();
					u.setId(UUIDUtil.generate());
					u.setIsChangePWD(0);
					u.setIsDeleted(0);
					u.setIsLocked(0);
					u.setIsPolic(0);
					u.setLevel(5);
					u.setLoginErrorTimes(0);
					u.setParentId(user.getId());
					u.setPassword("123456");
					Region r = regionDao.get(Region.class,region.getParentId());
					u.setRealName(r.getRegionName()+"物业人员");
					u.setRegionId(region.getParentId());
					u.setRoleLevel(9);
					u.setTelephone(person.getTelephone());
					//账号就为物业人员电话号码
					u.setUserName(person.getTelephone());
					Map<String, Object> param2 = new HashMap<String, Object>();
					param2.put("userName", u.getUserName());
					param2.put("telephone", u.getTelephone());
					String str = DaoUtil.getFindPrefix(User.class)+" where userName =:userName and telephone =:telephone";
					User user2 =  userDao.get(str,param2);
					if(user2==null){
						userDao.save(u);
					}
					
					//同时保存到用户角色关联表中
					SysRoleUser sysRoleUser = new SysRoleUser();
					sysRoleUser.setId(UUIDUtil.generate());
					sysRoleUser.setUserId(u.getId());
					sysRoleUser.setRoleId(userService.getRoleByLevel(9).getId());
					String string = DaoUtil.getFindPrefix(SysRoleUser.class)+" where userId ='"+u.getId()+"' and roleId ='"+sysRoleUser.getRoleId()+"'";
					SysRoleUser sRoleUser = sysRoleUserDao.get(string);
					if(sRoleUser==null){
						sysRoleUserDao.save(sysRoleUser);
					}
					String content = "将物业人员"+person.getName()+"升级为"+""+"管理员";
					operateRecordService.saveLog(user, 8, true, content);
					return new AccessResult(true,"升级成功");
				}
			}
		}
		return new AccessResult(true,"物业人员才能升级为管理员");
	}
	
	/**
	 * 获取未绑定卡的人员
	 * @return
	 */
	public List<Person> getOnbandPersons(String personName,String identifyId,int page,int rows){
		String hql = DaoUtil.getFindPrefix(Person.class);
		if(!StringUtils.isBlank(personName)){
			hql +=" and person like '%"+personName+"%'";
		}
		if(!StringUtils.isBlank(identifyId)){
			hql+=" and identifyId='"+identifyId+"'";
		}
		return personDao.find(hql,null,page,rows);
	}
	
	public Long getOnbandPersonsCount(String personName,String identifyId){
		String hql = DaoUtil.getFindPrefix(Person.class);
		if(!StringUtils.isBlank(personName)){
			hql +=" and person like '%"+personName+"%'";
		}
		if(!StringUtils.isBlank(identifyId)){
			hql+=" and identifyId='"+identifyId+"'";
		}
		return (long)personDao.find(hql).size();
	}
	
	/**
	 * 获取当前小区下的单元列表(非大门)
	 * @param user
	 * @return List<Region>
	 */
	public List<Unit> getCommunityUnits(User user,Person person){
		if(!StringUtils.isBlank(user.getRegionId())){
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("communityId", user.getRegionId());
			String hql = DaoUtil.getFindPrefix(Unit.class)+" where communityId =:communityId and isGurad = 0";
			return unitDao.find(hql,param1);
		}
		//person中有regionId这个属性
		Unit unit = unitDao.get(Unit.class, person.getRegionId());
		if(unit!=null){
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put("communityId", unit.getCommunityId());
			String h = DaoUtil.getFindPrefix(Unit.class)+" where communityId =:communityId and isGurad = 0";
			return unitDao.find(h,param2);
		}
	    return null;
	}
	
	/**
	 * 获取该人员审核通过时的照片
	 * @param personId
	 * @return
	 */
	public String getPicUrl(String personId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("personId", personId);
		String hql = DaoUtil.getFindPrefix(UserApplyRecord.class)+" where personId =:personId and state = 1";
		List<UserApplyRecord> records = userApplyRecordDao.find(hql,param);
		if(records!=null&&records.size()>0){
			String url = records.get(0).getImgUrl();
			if(StringUtils.isNotBlank("url")){
				return url.substring(url.indexOf("/")+1,url.length());
			}
		}
		return "";
	}
	
	public AccessResult queryResult(String telephone){
		String hql = DaoUtil.getFindPrefix(User.class)+" where userName = '"+telephone+"'";
		User u = userDao.get(hql);
		if(u!=null){  //存在
			return new AccessResult(true,"");
		}
		return new AccessResult(false,"");
	}
	
	public BaseDao<PersonType> getPersonTypeDao() {
		return personTypeDao;
	}

	public void setPersonTypeDao(BaseDao<PersonType> personTypeDao) {
		this.personTypeDao = personTypeDao;
	}

	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public BaseDao<Unit> getUnitDao() {
		return unitDao;
	}

	public void setUnitDao(BaseDao<Unit> unitDao) {
		this.unitDao = unitDao;
	}

	public BaseDao<Community> getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(BaseDao<Community> communityDao) {
		this.communityDao = communityDao;
	}

	public BaseDao<WeixinUser> getwDao() {
		return wDao;
	}

	public void setwDao(BaseDao<WeixinUser> wDao) {
		this.wDao = wDao;
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

	public UnitService getUnitService() {
		return unitService;
	}

	public void setUnitService(UnitService unitService) {
		this.unitService = unitService;
	}

	public WxuserService getWxuserService() {
		return wxuserService;
	}

	public void setWxuserService(WxuserService wxuserService) {
		this.wxuserService = wxuserService;
	}

	public BaseDao<Region> getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(BaseDao<Region> regionDao) {
		this.regionDao = regionDao;
	}

	public BaseDao<User> getUserDao() {
		return userDao;
	}

	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BaseDao<SysRoleUser> getSysRoleUserDao() {
		return sysRoleUserDao;
	}

	public void setSysRoleUserDao(BaseDao<SysRoleUser> sysRoleUserDao) {
		this.sysRoleUserDao = sysRoleUserDao;
	}

	public BaseDao<UnitDevice> getUnitDeviceDao() {
		return unitDeviceDao;
	}

	public void setUnitDeviceDao(BaseDao<UnitDevice> unitDeviceDao) {
		this.unitDeviceDao = unitDeviceDao;
	}

	public BaseDao<DoorCard> getDoorCardDao() {
		return doorCardDao;
	}

	public void setDoorCardDao(BaseDao<DoorCard> doorCardDao) {
		this.doorCardDao = doorCardDao;
	}
	
	public List<Person> getList(){
		String hql = DaoUtil.getFindPrefix(Person.class);
		return personDao.find(hql);
	}
	
	public void saveOrUpdate(Person person){
		personDao.saveOrUpdate(person);
	}
}
