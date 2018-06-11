package com.dhht.service.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.community.Community;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.User;
import com.dhht.entity.unit.Unit;
import com.dhht.service.community.CommunityService;
import com.dhht.service.record.OperateRecordService;
import com.dhht.service.region.RegionService;

@Service
public class UnitService {
	@Resource
	private CommunityService communityService;
	@Resource
	private RegionService regionService;
	@Resource
	private BaseDao<Unit> unitDao;
	@Resource
	private BaseDao<Community> communityDao;
	@Resource
	private BaseDao<Region> regionDao;
	@Resource
	private OperateRecordService operateRecordService;
    /**
     * 获取单元列表
     * @return
     */
	public List<Unit> getUnits(String regionId,String address,int page,int rows){ 
		List<Unit> unitList = new ArrayList<Unit>();
		List<Region> regions =  regionService.getRegions(regionId);   //包涵小区
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);
		List<Unit> uList = findByRegionIds(regionsIdStr,page,rows);
		for(Unit u :uList){
			u.setCommunityName(communityDao.get(Community.class, u.getCommunityId()).getCommunityName()) ;
		}
		//条件查询
//		if(!StringUtils.isBlank(address)){
//			String hql = DaoUtil.getFindPrefix(Community.class)+" where communityName like '%"+address+"%'";
//			List<Community> clList = communityDao.find(hql);
//			for(Unit unit:uList){
//				for(Community community:clList){
//					if(unit.getCommunityId().equals(community.getId())){
//						unit.setCommunityName(community.getCommunityName());
//						unitList.add(unit);
//					}
//				}
//			}
//			return unitList;
//		}
		return uList;
	}
	
	
	public List<Unit> findByRegionIds(String regionsIdStr, int page, int rows) {
		String hql = DaoUtil.getFindPrefix(Unit.class)+" where communityId in ("+regionsIdStr+") order by communityId";
		return unitDao.find(hql, null, page, rows);
	}

	/**
	 * 单元tree 
	 * @param user
	 * @return
	 */
	public List<Region> getTrees(User user){
		List<Region> userRegions = new ArrayList<Region>();
		List<Region> regions= regionService.getUnitRegions(user.getRegionId());
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
	 * 获取数据总条数
	 * @param address
	 * @param regionId
	 * @return
	 */
	public List<Unit> getCounts(String address,String regionId){
		List<Unit> uList = new ArrayList<Unit>();
		List<Region> regions =  regionService.getRegions(regionId);
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);  //小区Id在regionsIdStr中
		String h = DaoUtil.getFindPrefix(Unit.class)+" where communityId in ("+regionsIdStr+")";
		
		List<Unit> units = unitDao.find(h);  //所有小区单元
		if(!StringUtils.isBlank(address)){
			String hql = DaoUtil.getFindPrefix(Community.class)+" where communityName like '%"+address+"%'";
			List<Community> clList = communityDao.find(hql);
			for(Unit unit:units){
				for(Community community:clList){
					if(unit.getCommunityId().equals(community.getId())){
						uList.add(unit);
					}
				}
			}
			return uList;
		}
		return units;
	}
	
	/**
	 * 保存单元
	 * @param unit
	 * @param rid 所在小区Id
	 * @return
	 */
	public AccessResult save(Unit unit ,String rid,User user){   
		Unit u = new Unit();
		u.setId(UUIDUtil.generate());
		u.setUnitName(unit.getUnitName());
		u.setCommunityId(rid);
		u.setIsGurad(0); //这里添加的不是大门单元
		unitDao.save(u);
		//新增小区，在组织架构表中也新增下
		Region region = new Region();
		region.setId(u.getId());
		region.setLevel(6);
		region.setParentId(rid);
		region.setRegionName(u.getUnitName());
		region.setIsGurad(0);  //这里添加的是非大门单元
		regionDao.save(region);
		//保存操作记录
		String json = "新增单元:"+u.getUnitName();
		operateRecordService.saveLog(user,1,true,json);
		return new AccessResult(true,"新增单元成功");
	}
	
	/**
	 * 删除单元
	 * @param unitId
	 * @return
	 */
	public AccessResult deleteById(String unitId,User user){
		Unit unit =  unitDao.get(Unit.class, unitId);
		unitDao.delete(unit);
		//同时删除组织架构中的表
		Region region = regionDao.get(Region.class, unitId);
		regionDao.delete(region);
		String json = "删除单元：  "+unit.getUnitName();
		operateRecordService.saveLog(user, 2, true,json);
		return new AccessResult(true,"删除单元成功");
	}
	
	public CommunityService getCommunityService() {
		return communityService;
	}

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
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

	public BaseDao<Region> getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(BaseDao<Region> regionDao) {
		this.regionDao = regionDao;
	}
}
