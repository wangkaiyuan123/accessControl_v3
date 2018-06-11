package com.dhht.action;


import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;


import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.system.SysResource;
import com.dhht.entity.system.SysRoleResource;
import com.dhht.entity.system.User;
import com.dhht.service.MainService;

@Namespace("/")
public class MainAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	@Resource
	private MainService mainService;
	
	/**
	 * 主页面
	 * @return   主页面
	 */
	@Action(value = "main" , results={@Result(name = "changePwd",location = "/changePwd.jsp")})
	public String main(){
		//获取当前登录的用户
		User loginUser = getLoginUser();
		HttpServletRequest request = getRequest();
		//request.setAttribute("loginUser", loginUser);  //session其实已经存了loginUser
		request.getSession().setAttribute("loginUser", loginUser);
		
		//初始登录成功，进入修改密码界面
		
//		if(loginUser.getIsChangePWD()==0){
//			return "changePwd";
//		}
		return SUCCESS;
		
	}
	
	/**
	 * 获取菜单数据
	 */
	@Action("getMenuData")
	public void getMenuData(){
		writeJson(mainService.getMenu(getLoginUser().getUserResources().keySet(), getLoginUser()));
	}
	
	
	public MainService getMainService() {
		return mainService;
	}

	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}


}