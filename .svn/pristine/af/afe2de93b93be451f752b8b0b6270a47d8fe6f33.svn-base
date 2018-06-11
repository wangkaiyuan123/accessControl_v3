package com.dhht.service.community;

import java.util.ArrayList;
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
import com.dhht.entity.region.Region;
import com.dhht.entity.system.User;
import com.dhht.entity.unit.Unit;
import com.dhht.service.region.RegionService;
import com.dhht.service.system.UserService;
@Service
public class CommunityService {
	@Resource
	private RegionService regionService;
	@Resource
	private BaseDao<Region> regionDao;
	@Resource
	private BaseDao<Community> communityDao;
    @Resource
    private BaseDao<User> userDao;
    @Resource
    private UserService userService;
    @Resource
    private BaseDao<Unit> unitDao;

	//获取小区列表
	public List<Community> getCommunityList(String address,String regionId,int page,int rows){
		List<Region> regions =  regionService.getRegionsList(regionId);
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);
		List<Community> cList = findByRegionIds(regionsIdStr,page,rows,address);
		return cList;
	}
	
	
	public List<Community> findByRegionIds(String regionsIdStr,int page,int rows,String address){
		String hql = DaoUtil.getFindPrefix(Community.class)+" where regionId in ("+regionsIdStr+")";
		if(!StringUtils.isBlank(address)){
			hql+=" and address like '%"+address+"%'";
		}
		return  communityDao.find(hql, null, page, rows);
	}
    
	public List<Community> getCounts(String address,String regionId){
		List<Region> regions =  regionService.getRegionsList(regionId);
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);
		String hql = DaoUtil.getFindPrefix(Community.class)+" where regionId in ("+regionsIdStr+")";
		if(!StringUtils.isBlank(address)){
			hql+=" and address like '%"+address+"%'";
		}
		//List<Community> communities =  communityDao.find(hql);
		return communityDao.find(hql);
	}
	
	/**
	 * 保存新增小区
	 * @return
	 */
	public AccessResult save(Community community,String address,String pid,String cid,String did,String regionId,User user,String glyName,String glyPhone){
		Community c = new Community();
		c.setId(UUIDUtil.generate());
		c.setAddress(address);
		c.setCommunityName(community.getCommunityName());
		c.setProvinceId(pid);
		c.setCityId(cid);
		c.setDistrictId(did);
		c.setRegionId(regionId);
		communityDao.save(c);
		//新增小区时,机构列表也要随之添加
		Region region = new Region();
		region.setId(c.getId());
		region.setLevel(5);
		region.setParentId(regionId);
		region.setRegionName(community.getCommunityName());
		region.setIsGurad(0); //
		regionDao.save(region);
		//新增小区时默认添加大门,可理解为一特殊的单元
		Unit u = new Unit();
		u.setId(UUIDUtil.generate());
		u.setCommunityId(c.getId());
		u.setUnitName(c.getCommunityName()+"大门");
		u.setIsGurad(1); //大门标志
		unitDao.save(u);
        //同时添加到region表中		
		Region regionDoor = new Region();
		regionDoor.setId(u.getId());
		regionDoor.setLevel(6);
		regionDoor.setParentId(c.getId());
		regionDoor.setRegionName(c.getCommunityName()+"大门");
		regionDoor.setIsGurad(1);  //大门标志
		regionDao.save(regionDoor);
		//新增小区的同时,为小区添加个默认的管理员
		userService.addCommunityGly(community,region,user,glyName,glyPhone);
		
		return new AccessResult(true,"新增小区成功");
	}
	/**
	 * 修改小区
	 * @param community
	 * @param address
	 * @param regionId
	 * @return
	 */
	public AccessResult update(Community community,String address,String regionId){
		Community c = communityDao.get(Community.class, community.getId());
		c.setAddress(address);
		c.setCommunityName(community.getCommunityName());
		c.setRegionId(regionId);
		
		communityDao.update(c);
		return new AccessResult(true,"修改小区成功");
	}
	
	/**
	 * 下拉框选择地区
	 * @param level
	 * @param parentId
	 * @return
	 */
	public List<Region> getByLevel(int level,String parentId){
		if(StringUtils.isBlank(parentId)){
			parentId = "";
		}
//		String hql = DaoUtil.getFindPrefix(Region.class)+" where level ="+level;
//		hql+=" and parentId ='"+parentId+"'";
		String hql = DaoUtil.getFindPrefix(Region.class)+" where level =:level and parentId =:parentId";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("level", level);
		param.put("parentId", parentId);
		return regionDao.find(hql,param);
	}
	
	
	public AccessResult deleteById(String cid){
		Community community = communityDao.get(Community.class, cid);
		communityDao.delete(community);
		//删除小区的同时，删掉对应的机构及管理员,角色关系表
		Region region = regionDao.get(Region.class, cid);  
		regionDao.delete(region);
		userService.deleteUserByRegionId(cid);
		return new AccessResult(true,"删除小区成功");
	}
	
	/**
	 * 获取机构树
	 * @return
	 */
	public List<Region> getRegionTrees(User user){
		List<Region> userRegions = new ArrayList<Region>();
		List<Region> regions = regionService.getRegionsList(user.getRegionId());
		//机构储存
		Map<String, Region> map = new HashMap<String, Region>();
		for(Region region :regions){
			map.put(region.getId(), region);
		}
		//循环归类
		for(Region region :regions){
			if(StringUtils.isNotBlank(region.getParentId())){
				Region parentRegion = map.get(region.getParentId());
				if(null != parentRegion){
					if(null == parentRegion.getChildren()){
						List<Region> children = new ArrayList<Region>();
						parentRegion.setChildren(children);
					}
					parentRegion.getChildren().add(region);
				}
			}
			if(user.getLevel()==0){
				if(region.getLevel()==1)
					userRegions.add(region);
			}else{
				if(user.getLevel()==region.getLevel())
					userRegions.add(region);
			}
		}
    	return userRegions;
	}
	
	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public BaseDao<Community> getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(BaseDao<Community> communityDao) {
		this.communityDao = communityDao;
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


	public BaseDao<Unit> getUnitDao() {
		return unitDao;
	}

	public void setUnitDao(BaseDao<Unit> unitDao) {
		this.unitDao = unitDao;
	}
	
}
