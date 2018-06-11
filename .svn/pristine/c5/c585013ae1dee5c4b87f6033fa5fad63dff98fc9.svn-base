package com.dhht.action.system;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.entity.system.User;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 系统用户 控制器
 * @author Administrator
 *
 */
@Namespace("/system/user")
public class UserAction extends BaseAction implements ModelDriven<User>{
	 private static final long serialVersionUID = 1L;
	 private User user = new User();
	 @Override
	 public User getModel() {
		 // TODO Auto-generated method stub
		 return user;
	 }
	/**
	 * 跳转到系统人员管理界面
	 */
	 @Action("userManage")
	public String userManage() {
		return SUCCESS;
	}
	
	 @Action("operatorList")
	public String operatorList(){
		 return SUCCESS;
	 }


}
