package com.dhht.action.system;

import javax.annotation.Resource;

import org.apache.http.client.utils.Punycode;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.system.SysRole;
import com.dhht.service.system.SysRoleService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/role")
public class RoleAction extends BaseAction implements ModelDriven<SysRole>{

	
	private static final Logger logger = Logger.getLogger(RoleAction.class);
	
	@Resource
	private SysRoleService sysRoleService;
	
	//前段传过来的的url字符(权限)
	private String resourcesIds;
	
	private SysRole sysRole = new SysRole();
	@Override
	public SysRole getModel() {
		return sysRole;
	}
	
	@Action("list")
	public String list(){
		return SUCCESS;
	}

	@Action("add")
	public String add(){
		return SUCCESS;
		
	}
	
	@Action("delete")
	public String delete(){
		return SUCCESS;
	}
	
	@Action("update")
	public String update(){
		return SUCCESS;
	}
	
	@Action("getList")
	public void getList(){
		RoleData data = new RoleData();
		data.setData(sysRoleService.getRoleList());
		data.setCount(sysRoleService.getRoleList().size());
		writeJson(data);
	}
    //新增角色
	@Action("saveAdd")
	public void saveAdd(){
		try {
			writeJson(sysRoleService.saveAdd(sysRole,resourcesIds));
		} catch (Exception e) {
			logger.error("新增角色失败", e);
			writeJson(new AccessResult(false,"新增角色失败"));
		}
	}
	//删除角色
	@Action("deleteRole")
	public void deleteRole(){
		try {
			writeJson(sysRoleService.deleteRole(sysRole));
		} catch (Exception e) {
			logger.error("删除角色失败", e);
			writeJson(new AccessResult(false,"删除角色失败"));
		}
	}
	
	//修改角色
	@Action("updataRole")
	public void updataRole() {
		try {
			writeJson(sysRoleService.updataRole(sysRole,resourcesIds));
		} catch (Exception e) {
			logger.error("修改角色失败", e);
			writeJson(new AccessResult(false,"修改角色失败"));
		}
	}
	
	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public String getResourcesIds() {
		return resourcesIds;
	}

	public void setResourcesIds(String resourcesIds) {
		this.resourcesIds = resourcesIds;
	}
}
