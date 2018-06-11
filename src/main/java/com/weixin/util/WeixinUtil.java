package com.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.SpringBeanUtil;
import com.dhht.service.weixin.WxuserService;
import com.weixin.menu.Button;
import com.weixin.menu.ClickButton;
import com.weixin.menu.Menu;
import com.weixin.menu.ViewButton;
import com.weixin.pool.AccessToken;


public class WeixinUtil {
	private static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String USER_INFO="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url){
//		DefaultHttpClient httpClient = new DefaultHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.parseObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * post请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
//		DefaultHttpClient httpClient = new DefaultHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;		
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);		
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.parseObject(result);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static String doPostStrs(String url,String outStr){
//		DefaultHttpClient httpClient = new DefaultHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		String result = null;		
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);		
			result = EntityUtils.toString(response.getEntity(),"UTF-8");
//			jsonObject = JSONObject.parseObject(result);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取access_token方法
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", AuthUtil.APPID).replace("APPSECRET", AuthUtil.APPSECRET);
		System.out.println(url);
		//发送get请求获取access_token
		JSONObject jsonObject = doGetStr(url);	
		if(jsonObject!=null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getIntValue("expires_in"));
			System.out.println(jsonObject.toJSONString());
		}
		return token;
	}
	
	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw new IOException("文件不存在!");
		}
		String url = UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		//设置请求头消息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//设置边界
		String BOUNDARY = "---------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuffer sb = new StringBuffer();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");
		//获取输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		//把文件以流文件的方式 推入到url中
		DataInputStream ins = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes=ins.read(bufferOut))!=-1){
			out.write(bufferOut, 0, bytes);
		}
		ins.close();
		//结尾部分
		byte[] foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");//定义最后数据分割线
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		
		//定义BufferedReader输入流来读取URL的响应
		try {
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line=reader.readLine())!=null){
				buffer.append(line);
			}
			if(result==null){
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		//System.out.println(jsonObject);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "media_id";
		}
		String mediaId = jsonObject.getString("media_id");
		return mediaId;
	}
	
	/**
	 * 组装菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton button11 = new ClickButton();
		button11.setName("扫码开门");
		button11.setType("scancode_waitmsg");
		button11.setKey("openDoor");
		
		ViewButton button21 = new ViewButton();
		button21.setName("访客预约");
		button21.setType("view");
		button21.setUrl("http://pmtrv2.natappfree.cc/weixinpage/visitorAppointment");
		
		ViewButton button22 = new ViewButton();
		button22.setName("我的门卡");
		button22.setType("view");
		button22.setUrl("http://pmtrv2.natappfree.cc/weixinpage/myCard");
		
		ViewButton button23 = new ViewButton();
		button23.setName("开门记录");
		button23.setType("view");
		button23.setUrl("http://pmtrv2.natappfree.cc/weixinpage/openDoorRecord");
		
//		ViewButton button24 = new ViewButton();
//		button24.setName("申请绑定");
//		button24.setType("view");
//		button24.setUrl("http://1831y56b62.imwork.net/weixin/goRegister");
//		
//		ViewButton button25 = new ViewButton();
//		button25.setName("申请页面");
//		button25.setType("view");
//		button25.setUrl("http://1831y56b62.imwork.net/weixin/userRegister");
		
		Button button = new Button();
		button.setName("智能门卡");
		button.setSub_button(new Button[]{button21,button22,button23});
		
		menu.setButton(new Button[]{button11,button});
		return menu;
	}
	
	/**
	 * 创建菜单
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject!=null){
			result = jsonObject.getIntValue("errcode");
		}
		return result;
	}
	
	public static String getTOKEN(){
		String token = null;
		WxuserService wxuserService = (WxuserService)SpringBeanUtil.getBean("wxuserService");
		token = wxuserService.getWxConfig("TOKEN");
		return token;
	}
}