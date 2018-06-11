package com.dhht.action.device;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.doorguard.Advertising;
import com.dhht.service.device.AdvertisingService;
import com.opensymphony.xwork2.ModelDriven;


@SuppressWarnings("serial")
@Namespace("/advertising")
public class AdvertisingAction extends BaseAction implements ModelDriven<Advertising>{
	
	public static final Logger logger = Logger.getLogger(AdvertisingAction.class);
	@Resource
	private AdvertisingService advertisingService;
	private int page;
	private int rows;

	private Advertising advertising = new Advertising();
	@Override
	public Advertising getModel() {
		return advertising;
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
	
	@Action("getList")
	public void getList(){
		AdvertisingData data = new AdvertisingData();
		data.setData(advertisingService.getList(page,rows));
		data.setCount(advertisingService.getList(page,rows).size());
		writeJson(data);
	}
 
	@Action("saveAdd")
	public void saveAdd(){
		try {
			writeJson(advertisingService.saveAdd(advertising));
		} catch (Exception e) {
			logger.error("新增广告失败", e);
			writeJson(new AccessResult(false,"新增广告失败"));
		}
	}
	
	@Action("deleteAdvertising")
	public void deleteAdvertising(){
		try {
			writeJson(advertisingService.delete(advertising));
		} catch (Exception e) {
			logger.error("删除广告失败", e);
			writeJson(new AccessResult(false,"删除广告失败"));
		}
	}
	
	public AdvertisingService getAdvertisingService() {
		return advertisingService;
	}

	public void setAdvertisingService(AdvertisingService advertisingService) {
		this.advertisingService = advertisingService;
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
