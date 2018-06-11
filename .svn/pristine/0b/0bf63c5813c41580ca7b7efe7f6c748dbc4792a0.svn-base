package com.dhht.action.gzh;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.weixin.WeixinDictionary;
import com.dhht.service.gzh.GzhService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/gzh")
public class GzhAction extends BaseAction implements ModelDriven<WeixinDictionary>{
	
	public static final Logger logger = Logger.getLogger(GzhAction.class);
	
	private WeixinDictionary weixinDictionary = new WeixinDictionary();
	@Resource
	private GzhService gzhService;

	@Override
	public WeixinDictionary getModel() {
		return weixinDictionary;
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
	
	@Action("getList")
	public void getList(){
		MessageData data = new MessageData();
		data.setData(gzhService.getList());
		data.setCount(gzhService.getList().size());
		writeJson(data);
	}

	@Action("saveAdd")
	public void saveAdd(){
		try {
			writeJson(gzhService.saveAdd(weixinDictionary));
		} catch (Exception e) {
			logger.error("新增失败", e);
			writeJson(new AccessResult(false,"新增失败"));
		}
	}
	
	@Action("deleteMsg")
	public void deleteMsg(){
		try {
			writeJson(gzhService.deleteMsg(weixinDictionary));
		} catch (Exception e) {
			logger.error("删除失败", e);
			writeJson(new AccessResult(false,"删除失败"));
		}
	}
	
	@Action("updateMsg")
	public void updateMsg(){
		try {
			writeJson(gzhService.updateMsg(weixinDictionary));
		} catch (Exception e) {
			logger.error("修改失败", e);
			writeJson(new AccessResult(false,"修改失败"));
		}
	}
	
	
	public GzhService getGzhService() {
		return gzhService;
	}

	public void setGzhService(GzhService gzhService) {
		this.gzhService = gzhService;
	}
}
