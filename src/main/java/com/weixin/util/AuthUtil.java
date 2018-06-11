package com.weixin.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.SpringBeanUtil;
import com.dhht.common.UploadFileUtils;
import com.dhht.service.weixin.WxuserService;

/**
 * 连接工具类
 * @author Administrator
 *
 */
public class AuthUtil {
	public static String APPID;  //第三方用户唯一凭证
	public static String APPSECRET; //第三方用户唯一凭证密钥，即appsecret
	
	static{
		WxuserService wxuserService = (WxuserService)SpringBeanUtil.getBean("wxuserService");
		if(StringUtils.isBlank(APPID)||"null".equals(APPID)){
			APPID = wxuserService.getWxConfig("APPID");
		}
		if(StringUtils.isBlank(APPSECRET)||"null".equals(APPSECRET)){
			APPSECRET = wxuserService.getWxConfig("APPSECRET");
		}
	}
	
	public static JSONObject doGetJson(String url) throws ClientProtocolException, IOException{
		JSONObject jsonobject = null;
//		DefaultHttpClient client = new DefaultHttpClient();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if(entity!=null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonobject = JSONObject.parseObject(result);
		}
		httpGet.releaseConnection();
		return jsonobject;
	}
	
}
