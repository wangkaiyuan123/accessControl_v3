package com.dhht.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.dhht.common.AccessResult;
import com.dhht.entity.system.User;
import com.dhht.service.LoginService;
import com.dhht.service.record.OperateRecordService;
/**
 * 登录控制器
 * @author Administrator
 *
 */
@Namespace("/")
@InterceptorRef("interceptorStack")
public class LoginAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoginAction.class);
	
	@Resource
	private LoginService loginService;
	@Resource
	private OperateRecordService operateRecordService;
	//用户名
	private String userName;
	//密码
	private String password;
	
	/**
	 * 跳转到登录界面
	 */
	@Action(value = "login",results={@Result(name = "login",location = "/index.jsp")})
	public String login() {
	   return "login";
	}

	/**
	 * 验证
	 */
//	@Action(value =  "checkLogin" ,results={@Result(name = "login",location = "/index.jsp")})
//	public String checkLogin() {
//		try {
//			AccessResult result = loginService.login(userName, password);
//			if(result.isSuccess()){   //登录成功
//				getRequest().setAttribute("message", "登录成功");
//				return "main";
//			}else{
//				getRequest().setAttribute("message", result.getResultMsg());
//				return "login";
//			}
//		} catch (Exception e) {
//			logger.error("用户登录失败",e);
//			getRequest().setAttribute("message", "用户名或密码错误，请重新登录！");
//		}
//		return "login";
//	}
//	
	@Action("checkLogin")
	public void  checkLogin(){
		AccessResult result = loginService.login(userName, password);
		if(result.isSuccess()){
			result.setResultMsg("登录成功");
			User user = (User) loginService.checkUser(userName, password).get("userInfo");
			String content = user.getRealName()+"登录平台成功";
			operateRecordService.saveLog(user, 16, true, content);
		}
		writeJson(result);
	}
	
	/**
	 * 
	 * @return
	 */
	@Action(value = "logout" ,results={@Result(name = "/",location = "logout.jsp")})
	public String logout() {
		String content = getLoginUser().getRealName()+"退出登录";
		operateRecordService.saveLog(getLoginUser(), 14, true, content);
		loginService.logout();
		return "/";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	
	 
}
