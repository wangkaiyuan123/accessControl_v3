package com.dhht.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.system.SysRole;
import com.dhht.entity.system.SysRoleResource;

@Service
public class SysRoleService {
	
	@Resource
	private BaseDao<SysRole> sysRoleDao;
	@Resource
	private BaseDao<SysRoleResource> sysRoleResourceDao;
    /**
     * 获取系统角色列表
     * @return
     */
	public List<SysRole> getRoleList(){
		String hql = DaoUtil.getFindPrefix(SysRole.class)+" order by roleLevel";
		List<SysRole> sysRoles = sysRoleDao.find(hql);
		for(SysRole sysRole:sysRoles){
			List<String> resourceIds = new ArrayList<String>();
			String h = DaoUtil.getFindPrefix(SysRoleResource.class)+" where roleId = '"+sysRole.getId()+"'";
			List<SysRoleResource> sysRoleResources = sysRoleResourceDao.find(h);
			for(SysRoleResource sysRoleResource:sysRoleResources){
				resourceIds.add(sysRoleResource.getResourceId());
			}
			sysRole.setResourceIds(resourceIds);
		}
		return sysRoles;
	}
    
	/**
	 * 新增角色，并分配权限资源
	 * @param sysRole
	 * @param urls
	 * @return  AccessResult
	 */
	public AccessResult saveAdd(SysRole sysRole,String resourcesIds){
		if(StringUtils.isBlank(resourcesIds)){
			return new AccessResult(false,"请选择对应的权限");
		}
		//保存角色表
		SysRole s = new SysRole();
		s.setId(UUIDUtil.generate());
		s.setRoleCode(sysRole.getRoleCode());
		s.setRoleName(sysRole.getRoleName());
		s.setRoleLevel(sysRole.getRoleLevel());
		sysRoleDao.save(s);
		
		//保存到角色资源关联表
		List<String> rStrs = DaoUtil.parseJsonStrToList(resourcesIds);
		for(String resourcesId:rStrs){
			SysRoleResource sysRoleResource = new SysRoleResource();
			sysRoleResource.setId(UUIDUtil.generate());
			sysRoleResource.setResourceId(resourcesId); //资源Id
			sysRoleResource.setRoleId(s.getId());
			sysRoleResourceDao.save(sysRoleResource);
		}
		return new AccessResult(true,"新增角色成功");
	}
	
	/**
	 * 删除角色,同时删除角色对应的资源
	 * @return AccessResult
	 */
	public AccessResult deleteRole(SysRole sysRole){
		SysRole s = sysRoleDao.get(SysRole.class, sysRole.getId());
		//删除角色对应的资源信息
		String hql = DaoUtil.getFindPrefix(SysRoleResource.class)+" where roleId = '"+sysRole.getId()+"'";
		List<SysRoleResource> sysRoleResources = sysRoleResourceDao.find(hql);
		for(SysRoleResource sysRoleResource:sysRoleResources){
			sysRoleResourceDao.delete(sysRoleResource);
		}
		//删除角色
		sysRoleDao.delete(s);
		return new AccessResult(true,"删除角色成功");
	}
	
	/**
	 * 编辑角色信息
	 * @return
	 */
	public AccessResult updataRole(SysRole sysRole,String resourcesIds){
		SysRole s = sysRoleDao.get(SysRole.class, sysRole.getId());
		s.setRoleCode(sysRole.getRoleCode());
		s.setRoleName(sysRole.getRoleName());
		s.setRoleLevel(sysRole.getRoleLevel());
		//同时修改角色对应的资源
		String hql = DaoUtil.getFindPrefix(SysRoleResource.class)+" where roleId = '"+sysRole.getId()+"'";
		List<SysRoleResource> sysRoleResources = sysRoleResourceDao.find(hql);
		//先删除
		for(SysRoleResource sysRoleResource:sysRoleResources){
			sysRoleResourceDao.delete(sysRoleResource);
		}
		//再保存
		List<String> rStrs = DaoUtil.parseJsonStrToList(resourcesIds);
		for(String resourcesId:rStrs){
			SysRoleResource sysRoleResource = new SysRoleResource();
			sysRoleResource.setId(UUIDUtil.generate());
			sysRoleResource.setResourceId(resourcesId); //资源Id
			sysRoleResource.setRoleId(s.getId());
			sysRoleResourceDao.save(sysRoleResource);
		}
		sysRoleDao.update(s);
		return new AccessResult(true,"修改角色成功");
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
