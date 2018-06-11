package com.dhht.action.community;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.community.Community;
import com.dhht.entity.region.Region;
import com.dhht.service.community.CommunityService;
import com.dhht.service.region.RegionService;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 小区管理模块
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Namespace("/community")
public class CommunityAction extends BaseAction implements ModelDriven<Community>{

	private static final Logger logger = Logger.getLogger(CommunityAction.class);
	//机构等级
	private int level;
	private String parentId;
	private int page;
	private int rows;
	
	//查询所需参数
    private String communityAddress;
	private Community community = new Community();
	@Override
	public Community getModel() {
		return community;
	}
	@Resource
	private CommunityService communityService;
	@Resource
	private RegionService regionService;
	
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
	public String updata(){
		return SUCCESS;
	}
	
	//获取小区列表
	@Action("getCommunityList")
	public void getCommunityList(){
//		CommunityData data = new CommunityData();
//		data.setCount(communityService.getCounts(getLoginUser(),communityAddress,regionId).size());
//		data.setData(communityService.getCommunityList(getLoginUser(),communityAddress,regionId,page,rows));
//		writeJson(data);
		String regionId = getRequest().getParameter("regionId");
		CommunityData data = new CommunityData();
		data.setCount(communityService.getCounts(communityAddress,regionId).size());
		data.setData(communityService.getCommunityList(communityAddress,regionId,page,rows));
		writeJson(data);
	}
	
	@Action("saveCommunity")
	public void saveCommunity(){
		String pName = getRequest().getParameter("pName");
		String cName = getRequest().getParameter("cName");
		String dName = getRequest().getParameter("dName");
		String sName = getRequest().getParameter("sName");
		String glyName = getRequest().getParameter("glyName");
		String glyPhone = getRequest().getParameter("glyPhone");
		Region p =  regionService.getById(pName);
		Region c =  regionService.getById(cName);
		Region d =  regionService.getById(dName);
		Region s =  regionService.getById(sName);
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(p.getRegionName()).append(c.getRegionName()).append(d.getRegionName()).append(s.getRegionName()).append(community.getCommunityName());
			writeJson(communityService.save(community,sb.toString(),pName,cName,dName,sName,getLoginUser(),glyName,glyPhone));
		} catch (Exception e) {
			logger.error("新增小区失败", e);
			writeJson(new AccessResult(false,"新增小区失败"));
		}
	}
	//删除接口
	@Action("deleteCommunity")
	public void deleteCommunity(){
		try {
			writeJson(communityService.deleteById(community.getId()));
		} catch (Exception e) {
			logger.error("删除小区失败", e);
			writeJson(new AccessResult(false,"删除小区失败"));
		}
	}
	
	@Action("updateCommunity")
	public void updateCommunity(){
		String pName = getRequest().getParameter("pName");
		String cName = getRequest().getParameter("cName");
		String dName = getRequest().getParameter("dName");
		String sName = getRequest().getParameter("sName");
		Region p =  regionService.getById(pName);
		Region c =  regionService.getById(cName);
		Region d =  regionService.getById(dName);
		Region s =  regionService.getById(sName);
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(p.getRegionName()).append(c.getRegionName()).append(d.getRegionName()).append(s.getRegionName()).append(community.getCommunityName());
			writeJson(communityService.update(community,sb.toString(),sName));
		} catch (Exception e) {
			logger.error("修改小区失败", e);
			writeJson(new AccessResult(false,"修改小区失败"));
		}
	}
	
	//地址下拉框的接口
	@Action("getRegion")
	public void getProvince(){
		writeJson(communityService.getByLevel(level,parentId));
	}
	
	@Action("getRegionTrees")
	public void getTrees(){
		writeJson(communityService.getRegionTrees(getLoginUser()));
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}


	public String getCommunityAddress() {
		return communityAddress;
	}

	public void setCommunityAddress(String communityAddress) {
		this.communityAddress = communityAddress;
	}
}
