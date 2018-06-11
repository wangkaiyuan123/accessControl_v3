package com.dhht.action.gzh;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.weixin.WeixinDictionary;
import com.dhht.service.gzh.MessageService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/message")
public class MessageAction extends BaseAction implements ModelDriven<WeixinDictionary>{
	
	public static final Logger logger = Logger.getLogger(MessageAction.class);
	
	private WeixinDictionary weixinDictionary = new WeixinDictionary();

	@Resource
	private MessageService messageService;
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
		data.setData(messageService.getList());
		data.setCount(messageService.getList().size());
		writeJson(data);
	}
	
	@Action("saveAdd")
	public void saveAdd(){
		try {
			writeJson(messageService.saveAdd(weixinDictionary));
		} catch (Exception e) {
			logger.error("新增失败", e);
			writeJson(new AccessResult(false,"新增失败"));
		}
	}

	@Action("deleteMessage")
	public void deleteMessage(){
		try {
			writeJson(messageService.deleteMessage(weixinDictionary));
		} catch (Exception e) {
			logger.error("删除失败", e);
			writeJson(new AccessResult(false,"删除失败"));
		}
	}
	
	@Action("updateMessage")
	public void updateMessage(){
		try {
			writeJson(messageService.updateMessage(weixinDictionary));
		} catch (Exception e) {
			logger.error("修改失败", e);
			writeJson(new AccessResult(false,"修改失败"));
		}
	}
	
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	
}
