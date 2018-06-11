package com.dhht.action.person;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.dao.DataIntegrityViolationException;

import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.system.Person;
import com.dhht.service.person.PeopleService;
import com.dhht.service.record.OperateRecordService;
import com.dhht.service.weixin.PersonService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.org.apache.bcel.internal.generic.NEW;

import freemarker.core.ReturnInstruction.Return;

@Namespace("/person")
public class PersonAction extends BaseAction implements ModelDriven<Person>{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PersonAction.class);
	private Person person = new Person();
	
	private int page;
	private int rows;
	//单元
	private String unitId;
	//验证码
	private String code;
	private String unitArrays;
	
	//查询微信用户的标识或微信昵称
	private String account;
	@Resource
	private PeopleService peopleService;
	@Resource
    private OperateRecordService operateRecordService;
	@Override
	public Person getModel() {
		return person;
	}

	@Action("list")
	public String list(){
		return SUCCESS;
	}
	
	@Action("WeiXinList")
	public String WeiXinList(){
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
	
	@Action("addUnits")
	public String addUnits(){
		return SUCCESS;
	}
	
	//微信用户列表页面
	@Action("weixinUser")
	public String weixinUser(){
		return SUCCESS;
	}
	
	//是否将物业人员升级为小区管理员
	@Action("updateForGly")
	public String updateInnerPerson(){
		return SUCCESS;
	}
	
	
	
	//获取人员列表
	@Action("getPersonList")
	public void getPersonList(){
		String regionId = getRequest().getParameter("regionId");
		PersonData data = new PersonData();
		data.setData(peopleService.getPersonList(person,page,rows,regionId,getLoginUser()));
		data.setCount(peopleService.getCount(person, regionId));
		writeJson(data);
	}
    
	//获取微信用户关注列表
	@Action("getWeiXinUser")
	public void getWeiXinUser(){
		WeiXinData data = new WeiXinData();
		data.setData(peopleService.getWeiXinUsers(page,rows,account));
		data.setCount(peopleService.getWxUserCount(account));
		writeJson(data);
	}
	
	
	//获取非内部人员信息列表
	@Action("getNoWeiXinUser")
	public void getNoWeiXinUser(){
		WeiXinData data = new WeiXinData();
		data.setData(peopleService.getNoWeiXinUser(page, rows, account));
		data.setCount(peopleService.getNoWxUserCount(account));
		writeJson(data);
	}
	
	//新增人员
	@Action("savePersonData")
	public void savePersonData(){
		try {
			writeJson(peopleService.savePerson(person,unitId,code,getLoginUser()));
		} catch (Exception e) {
			logger.error("新增人员失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 4, false,json);
			writeJson(new AccessResult(false,"新增人员失败"));
		}
	}
	
	@Action("deletePerson")
	public void deletePerson(){
		try {
			String regionId = getRequest().getParameter("regionId");
			writeJson(peopleService.deletePerson(getLoginUser(),person,regionId,unitArrays.substring(1, unitArrays.length()-1)));
		} catch (Exception e) {
			logger.error("删除人员失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 6, false,json);
			writeJson(new AccessResult(false,"删除人员失败"));
		}
	}
	
	//用户类型下拉菜单接口
	@Action("getPersonType")
	public void getPersonType(){
		writeJson(peopleService.getPersonTypes());
	}
	//楼单元下拉框
	@Action("getUnits")
	public void getUnits(){
		writeJson(peopleService.getUnits(getLoginUser()));
	}
	
	//获取组织架构
	@Action("getTrees")
	public void getTrees(){
		writeJson(peopleService.getTrees(getLoginUser()));
	}
	
	//编辑人员按钮
	@Action("updatePerson")
	public void updatePerson(){
		try {
			writeJson(peopleService.updatePerson(person,code,getLoginUser()));
		}catch (Exception e) {
			logger.error("修改人员失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 5, false,json);
			writeJson(new AccessResult(false,"修改人员失败"));
		}
	}
	
	//绑定单元
	@Action("saveAddUnits")
	public void saveAddUnits(){
		try {
			writeJson(peopleService.saveAddUnits(person, unitId,getLoginUser()));
		} catch (Exception e) {
			logger.error("绑定单元失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 7, false,json);
			writeJson(new AccessResult(false,"绑定单元失败"));
		}
	}
	
	//获取当前小区所有单元
	@Action("getCommunityUnits")
	public void getCommunityUnits(){
		writeJson(peopleService.getCommunityUnits(getLoginUser(),person));
	}
		
	
	//将物业人员升级为管理员
	@Action("updateGly")
	public void updateGly(){
		try {
			writeJson(peopleService.updateGly(person,getLoginUser()));
		} catch (Exception e) {
			logger.error("升级失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 8, false,json);
			writeJson(new AccessResult(false,"升级失败"));
		}
	}
	
	//获取人员照片
	@Action("getPicUrl")
	public void getPicUrl(){
		PicUrlData data = new PicUrlData();
		data.setUrl(peopleService.getPicUrl(person.getId()));
		writeJson(data);
	}
	
	@Action("queryAdmin")
	public void queryAdmin(){
		String tele = getRequest().getParameter("tele");
		System.out.println(tele);
		writeJson(peopleService.queryResult(tele));
	}
	
	public PeopleService getPeopleService() {
		return peopleService;
	}

	public void setPeopleService(PeopleService peopleService) {
		this.peopleService = peopleService;
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

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUnitArrays() {
		return unitArrays;
	}

	public void setUnitArrays(String unitArrays) {
		this.unitArrays = unitArrays;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
