package com.dhht.action.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhht.common.SpringBeanUtil;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.weixin.WxuserService;
import com.weixin.pool.AccessToken;
import com.weixin.util.WeixinUtil;

/**
 * 定时任务:补全微信用户的信息
 * @author Administrator
 *
 */
public class GetNicknameTask extends TimerTask{
	
	private static final String ACHIEVE_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";
	WxuserService wexinUserService = (WxuserService)SpringBeanUtil.getBean("wxuserService");
	
	/**
	 * 补全微信用户的信息
	 * @param token
	 * @return 
	 */
	public void getWeiXinUserNickname(){
			String openId = null;
			Long count = wexinUserService.completionAllCount();
			if(count!=null&&count>0){
				int totlePage = (int)(count/50)+1;
				for(int i=1;i<=totlePage;i++){
					List<WeixinUser> list = wexinUserService.completionWeixinUsers(i, 50);
					if( list!=null && list.size() > 0 ){
						for(WeixinUser wx : list) {
							openId = wx.getOpenid();
							String url = ACHIEVE_USER_URL.replace("ACCESS_TOKEN", SendDataUtil.access_token);
							Map<String,String> map = new HashMap<String, String>();
							map.put("openid", openId);
							map.put("lang", "zh_CN");
							List<Map> user_list = new ArrayList<Map>();
							user_list.add(map);
							Map<String,Object> map1 = new HashMap<String, Object>();
							map1.put("user_list",user_list);
							String openIdList = JSONObject.toJSONString(map1);
							String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
//							System.out.println(jsonObject);
							if(!StringUtils.isBlank(jsonObject)){
								if(jsonObject.indexOf("errcode")>=0){
									if (jsonObject.indexOf("42001") >= 0
											|| jsonObject.indexOf("40001") >= 0
											|| jsonObject.indexOf("40014") >= 0
											|| jsonObject.indexOf("41001") >= 0
											|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
										AccessToken token = WeixinUtil.getAccessToken();
										if(token.getErrcode()>0){			
//											System.out.println("获取access_token时errcode:"+token.getErrcode());													
											return;
										}
										wexinUserService.updateToken(token.getToken());	
										return;
									}
								}
								Map<String,Object> map_back = new HashMap<String, Object>();
								map_back = (Map<String,Object>)JSON.parse(jsonObject);
								JSONArray jsonobj = (JSONArray)map_back.get("user_info_list");
								if(jsonobj!=null){
									List<WeixinUser> listU = JSON.parseArray(jsonobj.toJSONString(), WeixinUser.class); 
									for (WeixinUser weixinUser : listU) {
//									System.out.println(weixinUser.getNickname());
										wx.setCity(weixinUser.getCity());
										wx.setNickname(weixinUser.getNickname());
										wexinUserService.update(wx);
									}
								}
//								System.out.println(listU.size());
							}
						 }
					 }
				}
			}
		}
	
	@Override
	public void run() {
		try {
			getWeiXinUserNickname();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
