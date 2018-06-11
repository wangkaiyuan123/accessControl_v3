package com.dhht.interceptor;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dhht.common.WebUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 微信登录拦截器
 * @author Lisi
 *
 */
public class WeChatInterceptor extends AbstractInterceptor {
	public static final String WX_TARGET_PATH = "wxTargetPath";
	public static final String WX_TARGET_PARAMS = "wxTargetParams";
	/**
	 * 拦截器
	 * session为空则进行拦截
	 */
	private static final long serialVersionUID = 1L;
	//会传MAC过来
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String openId = (String) WebUtil.getSession().getAttribute("wxopenid");   //30分钟之内并且还在微信中就session中的属性就一直有效
//		System.out.println(WebUtil.getRequest().getServletPath()); //请求路径
//		System.out.println(WebUtil.getRequest().getMethod());  //get请求
		if(StringUtils.isBlank(openId)||"null".equals(openId)){
			String path = WebUtil.getRequest().getServletPath();
			WebUtil.getSession().setAttribute(WX_TARGET_PATH, path);
			Map<String, String[]> map = WebUtil.getRequest().getParameterMap();  //获取请求链接中的参数
			String parmStr="";
			Set<String> keys = map.keySet();  // keySet()获取所有的键值，key
			for (String key : keys){
				String[] parms = map.get(key); //参数值可能相同
				for (String parm : parms) {
					parmStr += key + "=" + parm + "&";
				}
	 		}
			WebUtil.getSession().setAttribute(WX_TARGET_PARAMS, parmStr);
			return "wxlogin";
		}
		return this.doInvoke(actionInvocation);
	}
	  /**
     * 继续拦截器链
     * @param actionInvocation
     * @throws Exception
     */
	private String doInvoke(ActionInvocation actionInvocation) throws Exception {
        return actionInvocation.invoke();
    }

}