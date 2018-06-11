package com.weixin.test;


import java.net.URLEncoder;
import com.alibaba.fastjson.JSONObject;
import com.weixin.pool.AccessToken;
import com.weixin.util.AuthUtil;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil;

public class WeixinTest {
	
	private static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String APPID ="wxe7ebfe0a39fec1fe";
	private static final String APPSECRET ="fb182c7b462f393d0ed4e118a3e31f7d";
	/**
	 * 获取access_token方法
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET", APPSECRET);
		System.out.println(url);
		//发送get请求获取access_token
		JSONObject jsonObject = WeixinUtil.doGetStr(url);	
		if(jsonObject!=null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getIntValue("expires_in"));
			System.out.println(jsonObject.toJSONString());
		}
		return token;
	}
	
	public static void main(String[] args){
		try {
			AccessToken  token = new AccessToken();
			token = getAccessToken();
			//AccessToken token = WeixinUtil.getAccessToken();
			System.out.println("票据:"+token.getToken());
			//lQz5h5CDOwESQ3eKeZ31Jeq0ygCvDPLL1-x0nv9A4T_5KETth_nj7Ru2bwXKiqomzFhCB93L_5QLxr25zl4u3tT0rahRtQW071ZfbYYiDPdmLJm4fPp9uSkRUazgt_kGXLXfAGAESE
			System.out.println("有效时间:"+token.getExpiresIn());
			//String path = "D:/timg.jpg";
			//String mediaId = WeixinUtil.upload(path, token.getToken(), MessageUtil.MESSAGE_IMAGE);			
			//DnHGdOZllps-qg8ghgGWaMSonJCoEdie49SmFVFF2-RK-Oji7Tudyc4KN8NWm8RQ
			
			//创建菜单
			String menu = JSONObject.toJSONString(WeixinUtil.initMenu());
			int result = WeixinUtil.createMenu(token.getToken(), menu);
			if(result==0){
				System.out.println("创建菜单成功!");
			}else{
				System.out.println("错误码:"+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		String backUrl = "https://1831y56b62.imwork.net/weixin/login";
//		System.out.println(URLEncoder.encode(backUrl));
//	}
}