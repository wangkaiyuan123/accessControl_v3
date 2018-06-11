package com.weixin.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.weixin.util.AuthUtil;

/**
 * 授权页面的组装，登录入口
 * @author Administrator
 *
 */
@WebServlet("/wxLogin")
public class LoginServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//第一步：用户同意授权，获取code
		String backUrl = "https://mytest123.tunnel.2bdata.com/WxAuth/callBack";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AuthUtil.APPID+
				"&redirect_uri=" +URLEncoder.encode(backUrl)+
				"&response_type=code" +
				"&scope=snsapi_userinfo" +
				"&state=STATE#wechat_redirect";
		
		System.out.println(url);
		resp.sendRedirect(url);
		//super.doGet(req, resp);
	}
}
