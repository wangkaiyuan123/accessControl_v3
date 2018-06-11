package com.dhht.service.device;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.community.Community;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.doorguard.DeviceCard;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.User;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.Unit;
import com.dhht.entity.unit.UnitDevice;
import com.dhht.mina.job.SessionBean;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.person.PeopleService;
import com.dhht.service.record.OperateRecordService;
import com.dhht.service.region.RegionService;

@Service
public class DeviceService {
	@Autowired     //自动装配
	private SessionBean sessionBean;
	@Resource
	private BaseDao<Region> regionDao;
	@Resource
	private BaseDao<Device> deviceDao;
	@Resource
	private RegionService regionService;
	@Resource
	private BaseDao<UnitDevice> unitDeviceDao;
	@Resource
	private BaseDao<PersonUnit> personUnitDao;
	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private BaseDao<DeviceCard> deviceCardDao;
	@Resource
	private BaseDao<Unit> unitDao;
	@Resource
	private BaseDao<Community> communityDao;
	@Resource
	private OperateRecordService operateRecordService;
	/**
	 * 新增卡号(实际加的是关联表)
	 * @param mac
	 * @param cardNo
	 * @return
	 */
	public AccessResult addCard(String mac,String cardNo){
		DeviceCard deviceCard = new DeviceCard();
		deviceCard.setId(UUIDUtil.generate());
		deviceCard.setMac(mac);
		deviceCard.setCardNo(cardNo);
		deviceCardDao.save(deviceCard);
		return new AccessResult(true,"");
	}
	
	/**
	 * 删除设备关联卡号
	 * @param mac
	 * @param cardNo
	 * @return
	 */
	public AccessResult deleteCard(String mac,String cardNo){
		//String hql = DaoUtil.getFindPrefix(DeviceCard.class)+" where mac ='"+mac+"' and cardNo = '"+cardNo+"'";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mac", mac);
		param.put("cardNo", cardNo);
		String hql = DaoUtil.getFindPrefix(DeviceCard.class)+" where mac =:mac and cardNo =:cardNo";
		DeviceCard deviceCard = deviceCardDao.get(hql,param);
		deviceCardDao.delete(deviceCard);
		return new AccessResult(true,"");
	}
	
	/**
	 * 清除设备所有关联卡号
	 * @return
	 */
	public AccessResult clearAllCard(String mac){
		String sql = "delete from device_card where mac ='"+mac+"'";
		deviceCardDao.deleteBySql(sql);
		return new AccessResult(true,"");
	}
	
	/**
	 * 获取设备列表
	 */
    public List<Device> queryDeviceList(int page,int rows){
    	String hql = " from Device where state=1 order by id";
    	return deviceDao.find(hql,page,rows);
    }
    
    /**
     * 获取设备表总数
     */
    public Long queryAllCount(){
    	return (Long)deviceDao.count("select count(id) from Device where state=1");
    }
	
	/**
	 * 获取设备列表
	 * @param address
	 * @param page
	 * @param rows
	 * @return
	 */
    public List<Device>  getDeviceList(String address,int page,int rows,User user){
    	List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
    	String hql = DaoUtil.getFindPrefix(Device.class)+" where unitId in ("+regionsIdStr+") and isdelete = 0";
    	//条件查询用
    	if(!StringUtils.isBlank(address)){
    		hql +=" and deviceAddress like '%"+address+"%'";
    	}
    	hql+=" order by deviceAddress";
    	List<Device> devices = deviceDao.find(hql,null,page,rows);
    	//判断在线状态
    	IoSession session = null;
    	for(Device d:devices){   //这里就是判断设备在线与否的地方
    		session = sessionBean.getSession(d.getDeviceMac());
    		if(session!=null&&session.isClosing()==false){
    			d.setOnline("在线");
			}else{
				d.setOnline("离线");
			}
    	}
    	return devices;
    }
    
    /**
     * 获取设备总数量
     * @return
     */
    public long getCount(String address,User user){
    	List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
    	String hql = "select count(id) from Device where unitId in ("+regionsIdStr+") and isdelete = 0"; //未删除的
    	if(!StringUtils.isBlank(address)){
    		hql +=" and deviceAddress like '%"+address+"%'";
    	}
    	//Device是类名,不是纯的sql
    	Long deviceNum = (Long)deviceDao.count(hql); 
    	Integer count =  deviceDao.countSQLQuery("select count(id) from device  where unitId in ("+regionsIdStr+") and isdelete = 0");
    	if(count!=null&&count>0){
    		return count;
    	}
//    	if(deviceNum!=null&&deviceNum>0){
//    		return deviceNum;
//    	}
    	return 0;
    }
    
    /**新增设备
     * @return AccessResult
     */
    public AccessResult saveDevice(Device device,User user){
    	Map<String, Object> param = new HashMap<String, Object>();
    	//String h = DaoUtil.getFindPrefix(Device.class)+" where deviceMac = '"+device.getDeviceMac()+"' and isdelete = 0";
    	String h = DaoUtil.getFindPrefix(Device.class)+" where deviceMac =:deviceMac and isdelete = 0";
    	param.put("deviceMac", device.getDeviceMac());
    	Device d1 = deviceDao.get(h,param);
    	if(d1!=null){
    		return new AccessResult(false,"该设备已存在");
    	}
    	//如果新增的设备之前被删掉了,改下状态
    	if(getDeviceByMac(device.getDeviceMac().trim().toUpperCase())!=null){
    		
    		Device de = getDeviceByMac(device.getDeviceMac().trim().toUpperCase());
    		de.setTcpPort(device.getTcpPort());    //TCP端口
        	de.setDevicePort(device.getDevicePort());  //设备端口 
        	//获取门禁地址
        	String address = getLoginUserAddress(user,device.getUnitId());
        	de.setDeviceAddress(address);
        	de.setPassword(device.getPassword());
        	de.setState(1);  //默认为启动
        	de.setIsReadCard(1);
        	de.setIsdelete(0);    //将状态重新改为未删除
        	de.setAuthotity(0);  //默认权限为非授权模式
        	//绑定单元Id
        	de.setUnitId(device.getUnitId());
        	de.setAddPerson(user.getRealName());
        	deviceDao.update(de);   //更新下状态就可以啦
        	
        	//新增单元设备关联表
        	UnitDevice ud = new UnitDevice();
    		ud.setId(UUIDUtil.generate());
    		ud.setDeviceId(de.getId());
    		ud.setUnitId(device.getUnitId());
    		unitDeviceDao.save(ud);
    		
    		SendDataUtil.sendChangeAuth(0, de.getId());  //新增设备更改为非授权模式
    	}else{
    		//没有就新增
    		Device d = new Device();
    		d.setId(UUIDUtil.generate());
    		d.setTcpPort(device.getTcpPort());    //TCP端口
    		d.setDevicePort(device.getDevicePort());  //设备端口 
    		d.setDeviceMac(device.getDeviceMac().trim().toUpperCase());
    		//获取门禁地址
    		String address = getLoginUserAddress(user,device.getUnitId());
    		d.setDeviceAddress(address);
    		d.setPassword(device.getPassword());
    		d.setState(1);  //默认为启动
    		d.setIsReadCard(1);
    		d.setIsdelete(0);  //未删除的
    		d.setAuthotity(0);  //默认权限为非授权模式
    		
    		//绑定单元Id
    		d.setUnitId(device.getUnitId());
    		d.setAddPerson(user.getRealName());
    		deviceDao.save(d);
    		//新增单元设备关联表
    		UnitDevice ud = new UnitDevice();
    		ud.setId(UUIDUtil.generate());
    		ud.setDeviceId(d.getId());
    		ud.setUnitId(device.getUnitId());
    		unitDeviceDao.save(ud);
    		//同时发送非授权模式给设备
    		SendDataUtil.sendChangeAuth(0, d.getId());   //新增设备更改为非授权模式
    	}
    	Unit unit = unitDao.get(Unit.class, device.getUnitId());
    	Community community = communityDao.get(Community.class, user.getRegionId());
    	String content = community.getCommunityName()+unit.getUnitName()+"新增设备"+device.getDeviceMac();
    	operateRecordService.saveLog(user, 13, true, content);
    	return new AccessResult(true,"新增设备成功");
    }
    
    
    
    
    
    /**
     * 删除设备
     * @param device
     * @return AccessResult
     */
    public AccessResult deleteDevice(Device device,User user){
    	
    	Device d = deviceDao.get(Device.class, device.getId());
    	//删除设备同时，清空卡号
    	SendDataUtil.sendClearCard(d.getId());
    	//同时删除设备关联的单元
    	//String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where  deviceId ='"+device.getId()+"'";
    	Map<String, Object> param = new HashMap<String, Object>();
    	String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where  deviceId =:deviceId";
    	param.put("deviceId", device.getId());
    	UnitDevice ud = unitDeviceDao.get(hql,param);
    	if(ud!=null){
    		unitDeviceDao.delete(ud);
    	}
    	//这里是软删除
    	d.setIsdelete(1);
    	deviceDao.update(d);
    	Unit unit = unitDao.get(Unit.class, d.getUnitId());
    	Community community = communityDao.get(Community.class, unit.getCommunityId());
    	String content = "将设备"+d.getDeviceMac()+"从"+community.getCommunityName()+unit.getUnitName()+"删除";
    	operateRecordService.saveLog(user, 11, true, content);
    	return new AccessResult(true,"删除设备成功");
    }
    
    
    public AccessResult deleteDeviceBatch(String deviceIds,User user){
    	String[]  strs = deviceIds.split(",");
    	for(String str:strs){
    		Map<String, Object> param = new HashMap<String, Object>();
    		param.put("str", str);
    		String sql = "SELECT * FROM device WHERE id =:str";
    		Device d = deviceDao.getBySql(sql, Device.class, param);
        	//删除设备同时，清空卡号
        	SendDataUtil.sendClearCard(d.getId());
        	//同时删除设备关联的单元
        	String hql = DaoUtil.getFindPrefix(UnitDevice.class)+" where  deviceId =:str";
        	UnitDevice ud = unitDeviceDao.get(hql,param);
        	if(ud!=null){
        		unitDeviceDao.delete(ud);
        	}
        	//这里是软删除
        	d.setIsdelete(1);
        	//添加到操作日志
        	Unit unit = unitDao.get(Unit.class, d.getUnitId());
        	Community community = communityDao.get(Community.class, unit.getCommunityId());
        	String content = "将设备"+d.getDeviceMac()+"从"+community.getCommunityName()+unit.getUnitName()+"删除";
        	operateRecordService.saveLog(user, 11, true, content);
        	deviceDao.update(d);
    	}
    	return new AccessResult(true,"删除设备成功");
    }
    
    /**
     * 变更设备模式
     * @param style
     * @param device
     *   0:非授权模式  所有卡都能开   1:授权模式
     * @return  AccessResult
     */
    public AccessResult change(int style,Device device,User user){    
    	Device d = deviceDao.get(Device.class, device.getId());
    	//同时与设备通信，修改收授权模式
    	//同时与设备通信，修改收授权模式,同时将该设备单元关联的人存到该设备中
    	SendDataUtil.sendChangeAuth(style, d.getId());   //存到pollData中
    	String model = "";
    	if(style==0){
    		model = "非授权模式";
    	}else{
    		model = "授权模式";
    	}
    	Unit unit = unitDao.get(Unit.class, d.getUnitId());
    	Community community = communityDao.get(Community.class, unit.getCommunityId());
    	String content = "将"+community.getCommunityName()+unit.getUnitName()+d.getDeviceMac()+"改为"+model;
    	operateRecordService.saveLog(user, 10, true, content);
    	//非授权模式不发卡号，授权模式才发(最好是先加人，在添设备)
    	if(style==1){
    		String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where unitId = '"+d.getUnitId()+"'";  //设备所在的单元
    		//String hql = DaoUtil.getFindPrefix(PersonUnit.class)+" where unitId =:unitId";
        	List<PersonUnit> pUnits = personUnitDao.find(hql); 
        	//如果有就将单元关联的人发送到该设备上去
        	if(pUnits!=null&&pUnits.size()>0){
        		for(PersonUnit pu:pUnits){
        			Person person = personDao.get(Person.class, pu.getPersonId());
        			if(person!=null){
        				if(!StringUtils.isBlank(person.getCardId())&&person.getCardId().length()==6){
        					SendDataUtil.sendAddCardNew(person.getCardId(),d.getId());
        					//新增到门卡设备关联表
        					DeviceCard deviceCard = new DeviceCard();
        					deviceCard.setId(UUIDUtil.generate());
        					deviceCard.setCardNo(person.getCardId());
        					Device device2 = deviceDao.get(Device.class, d.getId());
        					if(device2!=null){
        						deviceCard.setMac(device2.getDeviceMac());
        					}
        				}
        			}
        		}
        	}
    	}
    	//同时将该设备单元关联的人存到该设备中
//    	String hql = DaoUtil.getFindPrefix(Device.class)+" where unitId = '"+device.getUnitId()+"' and id != '"+device.getId()+"'";
//    	List<Device> devices = deviceDao.find(hql);
//    	//找出同单元下的其他设备
//    	if(devices.size()>0){
//    		
//    	}
    	return new AccessResult(true,"模式变更成功");
    }
    
    /**
     * 
     * @return String
     */
    public String getLoginUserAddress(User user,String unitId){
    	
    	
    	if(!StringUtils.isBlank(unitId)){
    		//单元
    		Region unit = regionDao.get(Region.class,unitId);
    		return getAddress(user.getLevel(),user)+unit.getRegionName();
    	}
    	return  getAddress(user.getLevel(),user);
    }
    
    /**
     * 根据不同的level获取地址
     * @param level
     * @param user
     * @return
     */
    public String getAddress(int level,User user){
    	if(level==5){
    		//小区
        	Region community = regionDao.get(Region.class, user.getRegionId());
        	//街道
        	Region street = regionDao.get(Region.class, community.getParentId());
        	//区
        	Region district = regionDao.get(Region.class, street.getParentId());
        	//市
        	Region city = regionDao.get(Region.class, district.getParentId());
        	//省
        	Region province = regionDao.get(Region.class, city.getParentId());
        	return  province.getRegionName()+city.getRegionName()+district.getRegionName()+street.getRegionName()+community.getRegionName();
    	}
    	if(level==4){
    		//街道
        	Region street = regionDao.get(Region.class, user.getRegionId());
        	//区
        	Region district = regionDao.get(Region.class, street.getParentId());
        	//市
        	Region city = regionDao.get(Region.class, district.getParentId());
        	//省
        	Region province = regionDao.get(Region.class, city.getParentId());
        	return  province.getRegionName()+city.getRegionName()+district.getRegionName()+street.getRegionName();
    	}
    	if(level==3){
    		//区
        	Region district = regionDao.get(Region.class, user.getRegionId());
        	//市
        	Region city = regionDao.get(Region.class, district.getParentId());
        	//省
        	Region province = regionDao.get(Region.class, city.getParentId());
        	return  province.getRegionName()+city.getRegionName()+district.getRegionName();
    	}
    	if(level==2){
    		//市
    		Region city = regionDao.get(Region.class, user.getRegionId());
        	//省
        	Region province = regionDao.get(Region.class, city.getParentId());
        	return province.getRegionName()+city.getRegionName();
    	}
    	if(level==1){
    		//省
        	Region province = regionDao.get(Region.class, user.getRegionId());
        	return province.getRegionName();
    	}
    	return "";
    }
    
    
    
    /**
     * 根据设备id获取设备mac
     * @return
     */
    public String getMacById(String id){
    	Device device = deviceDao.get(Device.class, id);
    	if(device!=null){
    		return device.getDeviceMac();
    	}
    	return null;
    }
    
    /**
     * 清空所有卡号
     * @return  AccessResult
     */
    public AccessResult clearCard(Device device,User user){
    	//清空设备门卡关联数据
    	Device d = deviceDao.get(Device.class, device.getId());
    	clearAllCard(d.getDeviceMac());
    	SendDataUtil.sendClearCard(device.getId());
    	Unit unit = unitDao.get(Unit.class, d.getUnitId());
    	Community community = communityDao.get(Community.class, unit.getCommunityId());
    	String content = "将"+community.getCommunityName()+unit.getUnitName()+d.getDeviceMac()+"设备所有卡号清空";
    	operateRecordService.saveLog(user, 12, true, content);
    	return new AccessResult(true,"清空所有卡号成功");
    }
    
    /**
     * 批量清空卡号
     * @param deviceIds
     * @return
     */
    public AccessResult clearCardBatch(String deviceIds,User user){
        String[]  strs = deviceIds.split(",");
        for(String str:strs){
        	Map<String, Object> param = new HashMap<String, Object>();
        	String sql = "SELECT * FROM device WHERE id =:str";
        	param.put("str", str);
        	Device device = deviceDao.getBySql(sql, Device.class,param);
        	clearAllCard(device.getDeviceMac());
        	SendDataUtil.sendClearCard(device.getId());
        	Unit unit = unitDao.get(Unit.class, device.getUnitId());
        	Community community = communityDao.get(Community.class, unit.getCommunityId());
        	String content = "将"+community.getCommunityName()+unit.getUnitName()+device.getDeviceMac()+"设备所有卡号清空";
        	operateRecordService.saveLog(user, 12, true, content);
        }
    	return new AccessResult(true,"请空所有卡号成功！");
    }
    
    public List<Unit> getUnit(User user){
    	Map<String, Object> param = new HashMap<String, Object>();
    	//String hql = DaoUtil.getFindPrefix(Unit.class)+" where communityId = '"+user.getRegionId()+"'";
    	String hql = DaoUtil.getFindPrefix(Unit.class)+" where communityId =:communityId";
    	param.put("communityId", user.getRegionId());
    	return unitDao.find(hql,param);
    }
    
    
    /**
     *获取对象 
     */
    public Device getDeviceByMac(String mac){
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("mac", mac);
    	String hql = DaoUtil.getFindPrefix(Device.class)+" where deviceMac=:mac";
    	return deviceDao.get(hql,param);
    }
    
    /**
     *更新对象 
     */
    public void updateDevice(Device device){
    	deviceDao.update(device);
    }
    
    /**
     * 设备重启
     * @param deviceIds
     * @param user
     * @return
     */
    public AccessResult reStart(String deviceIds,User user){
    	if(StringUtils.isBlank(deviceIds)){
    		return new AccessResult(false,"请选择设备");
    	}
    	String[]  strs = deviceIds.split(",");
    	for(String deviceId:strs){
    		Device device = deviceDao.get(" from Device where id="+deviceId);
        	if(device!=null){
        		String mac = device.getDeviceMac();
            	IoSession session = null;
            	session = sessionBean.getSession(mac);
            	if(session!=null&&session.isClosing()==false){
            		//在线发送指令
            		SendDataUtil.sendData(session, SendDataUtil.restart(), "0B");
            		Unit unit = unitDao.get(Unit.class, device.getUnitId());
                	Community community = communityDao.get(Community.class, unit.getCommunityId());
                	String content = "将"+community.getCommunityName()+unit.getUnitName()+device.getDeviceMac()+"设备重启"; 
            		operateRecordService.saveLog(user, 17, true, content);
        		}
        	}
    	}
    	return new AccessResult(true,"操作成功");
    }
    
    //获取小区所有设备Mac
    public Set<String> getDeviceList(User user) {
    	List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元
		Set<String>  regionsIdSet = new HashSet<String>();
		Set<String>  devicesMacSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
    	String hql = DaoUtil.getFindPrefix(Device.class)+" where unitId in ("+regionsIdStr+") and isdelete = 0";
    	List<Device> devices = deviceDao.find(hql);
    	for(Device device:devices){
    		devicesMacSet.add(device.getDeviceMac());
    	}
    	return devicesMacSet;
	}
    
    public List<DeviceCard> getList(){
    	String hql = DaoUtil.getFindPrefix(DeviceCard.class);
    	return deviceCardDao.find(hql);
    }
    
    public void saveOrUpdate(DeviceCard deviceCard){
    	deviceCardDao.saveOrUpdate(deviceCard);
    }
}
