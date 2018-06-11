package com.dhht.action.system;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.system.SysResource;
import com.dhht.service.region.RegionService;
import com.dhht.service.system.ResourceService;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 系统资源
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Namespace("/sysResource")
public class ResourceAction  extends BaseAction implements ModelDriven<SysResource>{
	
	public static final Logger logger = Logger.getLogger(ResourceAction.class);
	
    @Resource
    private ResourceService resourceService;
	
	private SysResource sysResource = new SysResource();
	@Override
	public SysResource getModel(){
		return sysResource;
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
	
	@Action("showIconCls")
	public String showIconCls(){
		return SUCCESS;
	}
	
	//获取资源列表
	@Action("getAllResources")
	public void getAllResources(){
		try {
			writeJson(resourceService.getAllResources());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//保存
	@Action("saveRecource")
	public void saveRecource(){
		try {
			writeJson(resourceService.saveRecource(sysResource,getLoginUser()));
		} catch (Exception e) {
			logger.error("新增资源失败", e);
			writeJson(new AccessResult(false,"新增资源失败"));
		}
	}
	
	//删除
	@Action("deleteRecource")
	public void deleteRecource(){
		try {
			writeJson(resourceService.deleteRecource(sysResource));
		} catch (Exception e) {
			logger.error("删除资源失败", e);
			writeJson(new AccessResult(false,"删除资源失败"));
		}
	}
	
	//修改
	@Action("updateRecourse")
	public void updateRecourse(){
		try {
			writeJson(resourceService.updateRecourse(sysResource));
		} catch (Exception e) {
			logger.error("修改资源失败", e);
			writeJson(new AccessResult(false,"修改资源失败"));
		}
	}

	
	
	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
}