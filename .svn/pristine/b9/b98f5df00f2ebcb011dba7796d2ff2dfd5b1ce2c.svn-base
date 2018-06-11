package com.dhht.service.system;

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
import com.dhht.entity.system.SysResource;
import com.dhht.entity.system.SysRole;
import com.dhht.entity.system.SysRoleResource;
import com.dhht.entity.system.User;

@Service
public class ResourceService {
    @Resource
	private BaseDao<SysResource> resourceDao;
    @Resource
    private BaseDao<SysRole> sysRoleDao;
	@Resource
	private BaseDao<SysRoleResource> sysRoleResourceDao;
    /**
     * 查找依赖项资源
     * @param parentId
     * @param isRequired
     * @return  List<SysResource>
     */
	public List<SysResource> findByPidAndRequired(String parentId,int isRequired){
		if(parentId==null){
			parentId = "";
		}
		//String hql = DaoUtil.getFindPrefix(SysResource.class)+"where parentId ='"+parentId+"' and isRequired = "+isRequired+"order by sort";
		String hql = DaoUtil.getFindPrefix(SysResource.class)+"where parentId =:parentId and isRequired =:isRequired order by sort";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId", parentId);
		param.put("isRequired", isRequired);
		return resourceDao.find(hql, param);
	}

	/**
	 * 获取当下所有资源
	 * @return
	 */
	public List<SysResource> getAllResources(){
		//获取一级菜单
        String hql1 = DaoUtil.getFindPrefix(SysResource.class)+" where parentId = ''";  
        List<SysResource> rList = resourceDao.find(hql1);
        Set<String> strSet = new HashSet<String>();
        for(SysResource sr:rList){
        	strSet.add(sr.getId());
        }
        //重组下显示项
		String hql2 = DaoUtil.getFindPrefix(SysResource.class)+" order by sort";
		List<SysResource> resources = resourceDao.find(hql2);
		for(SysResource sr:resources){
			if(StringUtils.isBlank(sr.getParentId())){
				sr.setParentName("");
				continue;
			}
			SysResource pResource = resourceDao.get(SysResource.class, sr.getParentId());
			sr.setParentName(pResource.getResourceName());
		}
		
		//资源存储
		Map<String, SysResource> map = new HashMap<String, SysResource>();
		for(SysResource s:resources){
			map.put(s.getId(), s);
		}
		//循环归类
		for(SysResource sysResource:resources){
			if(!StringUtils.isBlank(sysResource.getParentId())){
				SysResource pResource = map.get(sysResource.getParentId());  //获取上级机构
				if(null!=pResource){
					if(pResource.getChildren()==null){
						List<SysResource> children = new ArrayList<SysResource>();
						pResource.setChildren(children);
					}
					pResource.getChildren().add(sysResource);
				}
			}
		}
		//过滤掉多余的资源
		List<SysResource> resourcesList = new ArrayList<SysResource>();
        for(SysResource sysResource:resources){
        	if(strSet.contains(sysResource.getId())){
        		resourcesList.add(sysResource);
        	}
        }
		return resourcesList;
		//return resources;
	}
	
	/**
	 * 新增资源
	 * @param sysResource
	 * @return  AccessResult
	 */
	public AccessResult saveRecource(SysResource sysResource,User user){
		SysResource s = new SysResource();
		s.setId(UUIDUtil.generate());
		s.setIsMenu(sysResource.getIsMenu());
		s.setIconCls(sysResource.getIconCls());  //获取图标
		s.setIsRequired(sysResource.getIsRequired());
		s.setParentId(sysResource.getParentId());   //添加上级资源的Id存到parentId
		s.setResourceName(sysResource.getResourceName());
		s.setResourceUrl(sysResource.getResourceUrl());
		s.setSort(sysResource.getSort());
		resourceDao.save(s);
		
		//新增资源的同时,角色资源关联表也得添加进去
		SysRoleResource sysRoleResource = new SysRoleResource();
		sysRoleResource.setId(UUIDUtil.generate());
		sysRoleResource.setResourceId(s.getId());
		sysRoleResource.setRoleId(getByRoleLevel(user.getRoleLevel()).getId());
		sysRoleResourceDao.save(sysRoleResource);
		return new AccessResult(true,"新增资源成功");
	}
	
	
	/**
	 * 根据角色等级查找角色对象
	 * @param level
	 * @return
	 */
	public SysRole getByRoleLevel(Integer level){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("level", level);
		String hql = DaoUtil.getFindPrefix(SysRole.class)+" where roleLevel =:level";
		return sysRoleDao.get(hql,param);
	}
	
	/**
	 * 删除资源
	 * @return  AccessResult
	 */
	public AccessResult deleteRecource(SysResource sysResource){
		SysResource s  = resourceDao.get(SysResource.class, sysResource.getId());
		String hql = DaoUtil.getFindPrefix(SysRoleResource.class)+" where resourceId = '"+s.getId()+"'";
		SysRoleResource sysRoleResource = sysRoleResourceDao.get(hql);
		//同时删除资源所关联的角色资源关联表
		sysRoleResourceDao.delete(sysRoleResource);
	    resourceDao.delete(s);
		return new AccessResult(true,"删除资源成功");
	}
	
	/**
	 * 修改资源
	 * @return
	 */
	public AccessResult updateRecourse(SysResource sysResource){
		SysResource s  = resourceDao.get(SysResource.class, sysResource.getId());
		s.setIconCls(sysResource.getIconCls());
		s.setIsMenu(sysResource.getIsMenu());
		s.setIsRequired(sysResource.getIsRequired());
		s.setParentId(sysResource.getParentId());
		s.setResourceName(sysResource.getResourceName());
		s.setResourceUrl(sysResource.getResourceUrl());
		s.setSort(sysResource.getSort());
		resourceDao.update(s);
		return new AccessResult(true,"修改资源成功");
		
	}
	
	public BaseDao<SysResource> getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(BaseDao<SysResource> resourceDao) {
		this.resourceDao = resourceDao;
	}

	public BaseDao<SysRole> getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(BaseDao<SysRole> sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}

	public BaseDao<SysRoleResource> getSysRoleResourceDao() {
		return sysRoleResourceDao;
	}

	public void setSysRoleResourceDao(BaseDao<SysRoleResource> sysRoleResourceDao) {
		this.sysRoleResourceDao = sysRoleResourceDao;
	}
	
}
