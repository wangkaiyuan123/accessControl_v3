package com.dhht.action.system;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.system.User;
import com.dhht.service.record.OperateRecordService;
import com.dhht.service.system.UserPasswordService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/password")
public class PasswordAction extends BaseAction implements ModelDriven<User>{
	
	private static final Logger logger = Logger.getLogger(PasswordAction.class);
	
	@Resource
	private UserPasswordService userPasswordService;
	@Resource
	private OperateRecordService operateRecordService;
	//旧密码
	private String oldPwd;
	//新密码
	private String newPwd;
    private User user = new User();
	@Override
	public User getModel() {
		return user;
	}
	
	
	@Action(value = "changePwd",results={@Result(name = "changePwd",location = "/changePwd.jsp")})
	public String changePwd(){
		return "changePwd";
	}
	
	
	@Action("savePwd")
	public void savePwd(){
		try {
			writeJson(userPasswordService.changePwd(getLoginUser(), oldPwd, newPwd));
		} catch (Exception e) {
			logger.error("修改密码失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 15, false, json);
			writeJson(new AccessResult(false,"密码修改失败，请重新登录后进行修改!"));
		}
	}
	
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}


	public UserPasswordService getUserPasswordService() {
		return userPasswordService;
	}


	public void setUserPasswordService(UserPasswordService userPasswordService) {
		this.userPasswordService = userPasswordService;
	}
   
   
  
      
	
}
