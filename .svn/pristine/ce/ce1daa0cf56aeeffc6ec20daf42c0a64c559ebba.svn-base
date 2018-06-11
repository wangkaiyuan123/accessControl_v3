package com.dhht.action.record;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.service.record.ExportExcelService;
import com.dhht.service.record.OpenDoorService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/opendoor")
public class OpenDoorAction extends BaseAction implements ModelDriven<OpenDoorRecord>{
	
	public static final Logger logger = Logger.getLogger(OpenDoorAction.class); 
	
	@Resource
	private OpenDoorService openDoorService;
	@Resource
	private ExportExcelService exportExcelService;
	
	private OpenDoorRecord openDoorRecord = new OpenDoorRecord();
	
	private int page;
	private int rows;
	

	@Override
	public OpenDoorRecord getModel() {
		// TODO Auto-generated method stub
		return openDoorRecord;
	}
	
	@Action("list")
	public String list(){
		return SUCCESS;
	}
	
	@Action("exportPage")
	public String exportPage(){
		return SUCCESS;
	}
	
	@Action("getList")
	public void getList(){
		OpenDoorDate date = new OpenDoorDate();
		date.setDate(openDoorService.getList(page,rows,getLoginUser()));
		date.setCount(openDoorService.getCounts(getLoginUser()));
		writeJson(date);
	}

	//记录导出
	@Action("export")
	public void export(){
		String dateTime  =  getRequest().getParameter("dateTime");  //yyyy-MM
		try {
			exportExcelService.exportExcelC(getLoginUser(),getRequest(), getResponse(),dateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//记录下载
	@Action("download")
	public void download(){
		try {
			exportExcelService.download("",getResponse(),getLoginUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public OpenDoorService getOpenDoorService() {
		return openDoorService;
	}

	public void setOpenDoorService(OpenDoorService openDoorService) {
		this.openDoorService = openDoorService;
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
