package com.dhht.action.regions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.region.Region;
import com.dhht.service.MainService;
import com.dhht.service.region.RegionService;
import com.dhht.service.system.UserService;
import com.opensymphony.xwork2.ModelDriven;
@Namespace("/regions")
public class RegionAction extends BaseAction implements ModelDriven<Region>{

	private static final Logger logger = Logger.getLogger(RegionAction.class);
	private static final long serialVersionUID = 1L;
	//账号
	private String guserName;
	private String puserName;
	//联系方式
	private String guserPhone;
	private String puserPhone;
	//用户名
	private String guserRealName;
	private String puserRealName;
	
	//新增运维人员账号
	private String operatorName;
	//新增运维人员用户名
	private String operatorRealName;
	//新增运维人员电话
	private String operatorPhone;
	//运维人员Id
	private String operatorId;
	
	@Resource
	private RegionService regionService;
	@Resource
	private MainService mainService;
	@Resource
	private UserService userService;
	
	//分页
	private int page;
	
	private int rows;
	@Override
	public Region getModel() {
		return region;
	}
	private Region region = new Region();
	
	//机构列表
	@Action("list")
	public String list() {
		return SUCCESS;
	}
	
	//新增机构页面
	@Action("add")
	public String add(){
		return SUCCESS;
	}
	
	//新增运维人员页面
	@Action("addOperator")
	public String addOperator(){
		return SUCCESS;
	}
	
	@Action("update")
	public String update(){
		return SUCCESS;
	}
	
	//运维人员列表
	@Action("operatorList")
	public String operatorList(){
		return SUCCESS;
	}
	
	@Action("delete")
	public String delete(){
		return SUCCESS;
	}
	
	
	@Action("deleteOperator")
	public String deleteOperator(){
		return SUCCESS;
	}
	
	@Action("updateOperator")
	public String updateOperator(){
		return SUCCESS;
	}
	
	@Action("reset")
	public String reset(){
		return SUCCESS;
	}
	/**
	 * 获取登录用户的下级机构(获取下级机构)
	 * @return
	 */
	
	@Action("getLoginSubRegion")
	public void getLoginSubRegion(){
		//writeJson(getLoginUser().getUserRegions());
		writeJson(regionService.getRegionsList(getLoginUser().getRegionId()));
		//writeJson(regionService.getRegionsTrees(getLoginUser().getRegionId()));
	}
	
	/**
	 *  注意在新增机构的同时，创建一个下级机构管理员及巡警账号
	 */
	
	@Action("saveRegion")
	public void save(){
		try {
			Map<String, String> parameter = new HashMap<String, String>();
			parameter.put("guserName", guserName);
			parameter.put("puserName", puserName);
			parameter.put("guserPhone", guserPhone);
			parameter.put("puserPhone", puserPhone);
			parameter.put("guserRealName", guserRealName);
			parameter.put("puserRealName", puserRealName);
			writeJson(regionService.save(region,getLoginUser(),parameter));
		} catch (Exception e) {
			logger.error("新增机构失败", e);
			writeJson(new AccessResult(false,"新增机构失败"));
			
		}
	}
	
	@Action("deleteRegion")
	public void deleteRegion(){
		try {
			writeJson(regionService.delete(region));
		} catch (Exception e) {
			logger.error("删除机构失败", e);
			writeJson(new AccessResult(false,"删除机构失败"));
		}
	}
	
	@Action("updataRegion")
	public void updataRegion(){
		try {
			writeJson(regionService.updata(region));
		} catch (Exception e) {
			logger.error("修改机构失败", e);
			writeJson(new AccessResult(false,"修改机构失败"));
		}
	}
	
	//运维人员列表
    @Action("getOperatorList")
    public void getOperatorList(){
    	Operator operator = new Operator();
    	operator.setData(userService.getOperators(operatorName, operatorPhone,page,rows));
    	operator.setCount(userService.getCounts(operatorName, operatorPhone));
    	writeJson(operator);
    }
    
    //新增运维人员接口
    @Action("saveOperator")
	public void saveOperator(){
		try {
			Map<String, String> parameter = new  HashMap<String, String>();
			parameter.put("operatorName", operatorName);
			parameter.put("operatorRealName", operatorRealName);
			parameter.put("operatorPhone", operatorPhone);
			writeJson(userService.savaOperator(parameter,getLoginUser()));
		} catch (Exception e) {
			logger.error("新增运维人员失败", e);
			writeJson(new AccessResult(false,"新增运维人员失败"));
		}
	}
    //删除运维人员
    @Action("deleOperator")
    public void deleOperator(){
    	try {
			writeJson(userService.deleteOperatorById(operatorId));
		} catch (Exception e) {
			logger.error("删除运维人员失败", e);
			writeJson(new AccessResult(false,"删除运维人员失败"));
		}
    }
    
    @Action("updataOptor")
    public void updataOperator(){
    	Map<String, String> parameter = new  HashMap<String, String>();
    	parameter.put("operatorId", operatorId);
		parameter.put("operatorName", operatorName);
		parameter.put("operatorRealName", operatorRealName);
		parameter.put("operatorPhone", operatorPhone);
		writeJson(userService.updataOperator(parameter));
    }
    
    @Action("resetPwd")
    public void resetPwd(){
    	try {
			writeJson(userService.resetPwd(operatorId));
		} catch (Exception e) {
			logger.error("重置密码失败", e);
			writeJson(new AccessResult(false,"重置密码失败"));
		}
    }
    
    @Action("getRegionTrees")
    public void  getRegionTrees(){
    	writeJson(regionService.getRegionTrees(getLoginUser()));
    }
    
	public MainService getMainService(){
		return mainService;
	}

	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public String getGuserName() {
		return guserName;
	}

	public void setGuserName(String guserName) {
		this.guserName = guserName;
	}

	public String getPuserName() {
		return puserName;
	}

	public void setPuserName(String puserName) {
		this.puserName = puserName;
	}

	public String getGuserPhone() {
		return guserPhone;
	}

	public void setGuserPhone(String guserPhone) {
		this.guserPhone = guserPhone;
	}

	public String getPuserPhone() {
		return puserPhone;
	}

	public void setPuserPhone(String puserPhone) {
		this.puserPhone = puserPhone;
	}

	public String getGuserRealName() {
		return guserRealName;
	}

	public void setGuserRealName(String guserRealName) {
		this.guserRealName = guserRealName;
	}

	public String getPuserRealName() {
		return puserRealName;
	}

	public void setPuserRealName(String puserRealName) {
		this.puserRealName = puserRealName;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorRealName() {
		return operatorRealName;
	}

	public void setOperatorRealName(String operatorRealName) {
		this.operatorRealName = operatorRealName;
	}

	public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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
}
