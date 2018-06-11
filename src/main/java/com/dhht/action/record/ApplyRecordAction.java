package com.dhht.action.record;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.hibernate.exception.LockAcquisitionException;

import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.record.UserApplyRecord;
import com.dhht.service.person.PeopleService;
import com.dhht.service.record.ApplyRecordService;
import com.dhht.service.record.OperateRecordService;
import com.opensymphony.xwork2.ModelDriven;


@SuppressWarnings("serial")
@Namespace("/applyrecord")
public class ApplyRecordAction extends BaseAction implements ModelDriven<UserApplyRecord>{
	
	private static final Logger logger = Logger.getLogger(ApplyRecordAction.class);

	@Resource
    private ApplyRecordService applyRecordService;
	@Resource
	private PeopleService peopleService;
	@Resource
	private OperateRecordService operateRecordService;
	private UserApplyRecord userApplyRecord = new UserApplyRecord();
	private int page;
	private int rows;
	private String regionId;  //组织机构Id
	
	private Integer check;
	@Override
	public UserApplyRecord getModel() {
		return userApplyRecord;
	}
	
	@Action("list")
	public String list(){
		return SUCCESS;
	}
	
	@Action("check")
	public String check(){
		return SUCCESS;
	}
	
	//获取记录数据
	@Action("getApplyRecord")
	public void getApplyRecord(){
		try {
			ApplyRecordData data = new ApplyRecordData();
			data.setData(applyRecordService.getList(userApplyRecord,page,rows,regionId));
			data.setCount(applyRecordService.getCount(userApplyRecord, regionId));
			writeJson(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//审核按钮接口
	@Action("getRecordCheck")
	public void  getRecordCheck(){
	    try {
			writeJson(applyRecordService.test(getLoginUser(),check,userApplyRecord.getId(),userApplyRecord.getState()));
		}
	    catch(LockAcquisitionException e){
			writeJson(new AccessResult(true,""));
		}
	    catch (Exception e) {
			logger.error("审核异常", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 9, false, json);
			writeJson(new AccessResult(false,"审核异常"));
			e.printStackTrace();
		}
   } 
	
	@Action("getTrees")
	public void getTrees(){
		writeJson(peopleService.getTrees(getLoginUser()));
	} 
	
	public ApplyRecordService getApplyRecordService() {
		return applyRecordService;
	}

	public void setApplyRecordService(ApplyRecordService applyRecordService) {
		this.applyRecordService = applyRecordService;
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

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Integer getCheck() {
		return check;
	}

	public void setCheck(Integer check) {
		this.check = check;
	}
}
