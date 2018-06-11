package com.dhht.action.unit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.unit.Unit;
import com.dhht.service.record.OperateRecordService;
import com.dhht.service.unit.UnitService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/unit")
public class UnitAction extends BaseAction implements ModelDriven<Unit>{
	
	private static final Logger logger = Logger.getLogger(UnitAction.class);
	private Unit unit = new Unit();
	//分页
	private int page;
	private int rows;
	//小区名
	private String address;
	private String regionId;
    @Resource
	private UnitService unitService;
    @Resource
    private OperateRecordService operateRecordService;
	@Override
	public Unit getModel() {
		return unit;
	}

	@Action("list")
	public String getUintList(){
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
	
	//获取楼单元列表
	@Action("getUnitList")
	public void getUnitList(){
		UnitData data = new UnitData();
		data.setCount(unitService.getCounts(address,regionId).size());
		data.setData(unitService.getUnits(regionId, address, page, rows));
		writeJson(data);
	}
   
	//新增单元
//	@Action("saveUnit")
//	public void saveUnit(){
//		try {
//			String rid = getRequest().getParameter("rid"); //小区ID
//			writeJson(unitService.save(unit,rid));
//		} catch (Exception e) {
//			logger.error("新增单元失败", e);
//			writeJson(new AccessResult(false,"新增单元失败"));
//		}
//	}
	
	@Action("saveUnit")
	public void saveUnit(){
		try {
			writeJson(unitService.save(unit,getLoginUser().getRegionId(),getLoginUser()));
		} catch (Exception e) {
			logger.error("新增单元失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 1, false,json);
			writeJson(new AccessResult(false,"新增单元失败"));
		}
	}
	
	//删除单元
	@Action("deleteUnit")
	public void deleteUnit(){
		try {
			writeJson(unitService.deleteById(unit.getId(),getLoginUser()));
		} catch (Exception e) {
			logger.error("删除单元失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 2, false,json);
			writeJson(new AccessResult(false,"删除单元失败"));
		}
	}
	
	
	@Action("getTrees")
	public void getTrees(){
		writeJson(unitService.getTrees(getLoginUser()));
	}
	
	
	
	public UnitService getUnitService() {
		return unitService;
	}

	public void setUnitService(UnitService unitService) {
		this.unitService = unitService;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
}
