package com.dhht.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cglib.transform.impl.AddDelegateTransformer;
import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.SysResource;
import com.dhht.entity.system.SysRoleResource;
import com.dhht.entity.system.User;

@Service
public class MainService {
	@Resource
	private BaseDao<SysResource> resourceDao;
	@Resource
	private BaseDao<Region> regionDao;
	@Resource
	private BaseDao<SysRoleResource> sysRoleResourceDao;
    
	/**
	 * 获取顶层菜单资源
	 * @param resourceIdSet
	 * @return
	 */
	public List<SysResource> getMenu(Set<String> resourceIdSet,User user) {
	    String resourceIdStrs =  DaoUtil.generateInStr(resourceIdSet);
	    //获取该用户的一级菜单
	    List<SysResource> rootResources = findByPid("", resourceIdStrs);
	    List<SysResource> menuResources = new ArrayList<SysResource>();
	    //获取二级菜单
	    for(SysResource resource:rootResources){   
	    	List<SysResource> subResources = findByPid(resource.getId(),resourceIdStrs);
	    	
	    	resource.setChildren(subResources);
	    	menuResources.add(resource);
	    }
		return menuResources;
	}
	
	
	
	/**
	 * 
	 * @param parentId   上级资源Id
	 * @param resourceIds    资源Id集合
	 * @return
	 */
	public List<SysResource> findByPid(String parentId,String resourceIds) {
		String hql = DaoUtil.getFindPrefix(SysResource.class)+" where isMenu = 1 and parentId ='"+parentId+
				"' and id IN("+resourceIds+") order by sort";
		return resourceDao.find(hql);
	}

	
	/**
	 * 获取下级机构
	 * @param regionIdsSet
	 * @param user
	 * @return
	 */
	public List<Region> getRegions(Set<String> regionIdSet,User user){
		String regionIdStrs =  DaoUtil.generateInStr(regionIdSet);
		String Pid = user.getRegionId();
		if(Pid==null){
			Pid = "";
		}
	    //获取该用户机构列表
		List<Region> rootRegions = findRegionByPid(Pid,regionIdStrs);
		List<Region> regions = new ArrayList<Region>();
		for(Region r :rootRegions){
			List<Region> subRegion1 = findRegionByPid(r.getId(),regionIdStrs); //获取下级机构
			r.setChildren(subRegion1);
			
		}
		return null;
	}
	
	
	public List<SysResource> getResources(){
		String hql = DaoUtil.getFindPrefix(SysResource.class)+" order by sort";
		 return resourceDao.find(hql);
	}
	
	 
	public List<Region> findRegionByPid(String Pid,String regionIdStrs){
		String hql = DaoUtil.getFindPrefix(Region.class)+" where parentId='"+Pid+"' and id IN("+regionIdStrs+") order by LEVEL";
		return regionDao.find(hql);
	}
	/**
	 * 保存条记录
	 * @param s
	 */
	public void saveData(SysRoleResource s){
		sysRoleResourceDao.save(s);
	}
	
	
	public List<Region> getData(Set<String> keySet) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public BaseDao<SysResource> getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(BaseDao<SysResource> resourceDao) {
		this.resourceDao = resourceDao;
	}

	public BaseDao<SysRoleResource> getSysRoleResourceDao() {
		return sysRoleResourceDao;
	}

	public void setSysRoleResourceDao(BaseDao<SysRoleResource> sysRoleResourceDao) {
		this.sysRoleResourceDao = sysRoleResourceDao;
	}

	public BaseDao<Region> getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(BaseDao<Region> regionDao) {
		this.regionDao = regionDao;
	}



}
