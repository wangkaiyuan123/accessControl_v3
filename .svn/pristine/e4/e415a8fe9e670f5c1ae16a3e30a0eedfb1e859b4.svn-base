package com.dhht.action.record;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.entity.record.OperateRecord;
import com.dhht.service.record.OperateRecordService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/operaterecord")
public class OperateRecordAction extends BaseAction implements ModelDriven<OperateRecord>{
    
    @Resource
    private OperateRecordService operateRecordService;
	private int page;
	private int rows;
	private OperateRecord operateRecord = new OperateRecord();
	@Override
	public OperateRecord getModel() {
		// TODO Auto-generated method stub
		return operateRecord;
	}

	@Action("list")
	public String list(){
		return SUCCESS;
	}
	
	@Action("getList")
	public void getList(){
		OperateRecordDate data = new OperateRecordDate();
		data.setDate(operateRecordService.getRecords(page,rows,getLoginUser()));
		data.setCount(operateRecordService.getCount());
		writeJson(data);
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
