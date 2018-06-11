package com.weixin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSONObject;
import com.weixin.util.AuthUtil;
/**
 * 微信回调入口
 * @author Administrator
 *
 */
@WebServlet("/callBack")
public class CallBackServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//第二步：通过code换取网页授权access_token
		System.out.println(1);
		String code = req.getParameter("code");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +AuthUtil.APPID+
				"&secret=" +AuthUtil.APPSECRET+
				"&code=" +code+
				"&grant_type=authorization_code ";
		JSONObject jsonObject = AuthUtil.doGetJson(url);
		System.out.println(2);
		String openid = jsonObject.getString("openid");
		String token = jsonObject.getString("access_token");
		String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" +token+
				"&openid=" +openid+
				"&lang=zh_CN";
		//拉取用户信息(需第一步的scope为 snsapi_userinfo)
		JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
		System.out.println(3);
		System.out.println(userInfo);
		//super.doGet(req, resp);
	}
}
