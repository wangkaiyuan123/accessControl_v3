package com.dhht.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import com.dhht.entity.system.User;
import com.dhht.common.WebUtil;

/**
 * struts2 拦截器，用于拦截用户请求的action权限验证。
 * 
 */
public class PermissionInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 0L;
    private static Logger logger = Logger.getLogger(PermissionInterceptor.class);

    /**
     * 权限拦截器
     */
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        String path = getServletPath();
        //System.out.println(path);
        User loginUser = WebUtil.getLoginUser();
        //过滤掉登录拦截
        if(path.equals("/checkLogin")){
        	return this.doInvoke(actionInvocation);  //放行
        }
        if(loginUser == null) {
            //logger.error("未登录：ip"+WebUtil.getRemoteIp()+" 目标资源:" + path);
        	//System.out.println("未登录：ip"+WebUtil.getRemoteIp()+" 目标资源:" + path);
            return "index";
        }

         //校验权限
        if(!PermissionUtils.hasPerssion(path)) {
            logger.error("无权限,userName: " + loginUser.getUserName() + ", path: " + path + " ip:" + WebUtil.getRemoteIp());
            return "noPermission";
        }
        return this.doInvoke(actionInvocation);
    }

    /**
     * 获取url需要校验的部分
     * 
     * @return
     */
    private String getServletPath() {
        // String url = WebUtil.getRequest().getRequestURL().toString();
        // System.out.println(url);
        // String context = WebUtil.getRequest().getContextPath();
        // System.out.println(context);
        // System.out.println();
        // return url.substring(url.indexOf(context) + context.length() + 1, url.length());
        // System.out.println(WebUtil.getRequest().getServletPath());
        return WebUtil.getRequest().getServletPath();
    }

    /**
     * 继续拦截器链
     * 
     * @param actionInvocation
     * @return
     * @throws Exception
     */
    private String doInvoke(ActionInvocation actionInvocation) throws Exception {
        return actionInvocation.invoke();
    }

}
