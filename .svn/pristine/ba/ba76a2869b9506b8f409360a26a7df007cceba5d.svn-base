package com.dhht.action.gzh;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.weixin.KeyWord;
import com.dhht.service.gzh.KeyWordService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/keyword")
public class KeyWordAction extends BaseAction implements ModelDriven<KeyWord>{
	
	public static final Logger logger = Logger.getLogger(KeyWordAction.class);
	
	private KeyWord keyWord = new KeyWord();

	@Resource
	private KeyWordService keyWordService;
	@Override
	public KeyWord getModel() {
		return keyWord;
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
		KeyWordData data = new KeyWordData();
		data.setData(keyWordService.getList());
		data.setCount(keyWordService.getList().size());
		writeJson(data);
	}

	@Action("saveAdd")
	public void saveAdd(){
		try {
			writeJson(keyWordService.saveAdd(keyWord));
		} catch (Exception e) {
			logger.error("新增失败", e);
			writeJson(new AccessResult(false,"新增失败"));
		}
	}
	
	@Action("deleteKeyword")
	public void deleteKeyword(){
		try {
			writeJson(keyWordService.deleteKeyword(keyWord));
		} catch (Exception e) {
			logger.error("删除失败", e);
			writeJson(new AccessResult(false,"删除失败"));
		}
	}
	
	public KeyWordService getKeyWordService() {
		return keyWordService;
	}

	public void setKeyWordService(KeyWordService keyWordService) {
		this.keyWordService = keyWordService;
	}
}
