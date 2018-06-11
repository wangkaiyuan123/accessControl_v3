package com.dhht.action.record;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import com.dhht.action.BaseAction;
import com.dhht.entity.visitors.VisitorRecord;
import com.dhht.service.record.VisitorOpenService;

import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/visitor")
public class VisitorOpenRecordAction extends BaseAction implements ModelDriven<VisitorRecord>{

	VisitorRecord visitorRecord = new VisitorRecord();
	private int page;
	private int rows;
	@Resource
	private VisitorOpenService visitorOpenService;
	
	@Override
	public VisitorRecord getModel() {
		// TODO Auto-generated method stub
		return visitorRecord;
	}

	@Action("list")
	public String list(){
		return SUCCESS;
	}
	
	@Action("getList")
	public void getList(){
		VisitorOpenDoorDate date = new VisitorOpenDoorDate();
		date.setDate(visitorOpenService.getVisitorRecord(page,rows,getLoginUser()));
		date.setCount(visitorOpenService.getCount(getLoginUser()));
		writeJson(date);
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
