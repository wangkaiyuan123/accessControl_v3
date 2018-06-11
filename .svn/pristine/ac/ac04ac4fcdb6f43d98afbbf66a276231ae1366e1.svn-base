package com.dhht.action.weixin;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.common.WebUtil;
import com.dhht.entity.weixin.SMSCode;
import com.dhht.entity.weixin.WeixinDictionary;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.interceptor.WeChatInterceptor;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.weixin.PollDataService;
import com.dhht.service.weixin.SMSCodeService;
import com.dhht.service.weixin.WxuserService;
import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;
import com.weixin.pool.AccessToken;
import com.weixin.util.AuthUtil;
import com.weixin.util.CheckUtil;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil;

import freemarker.cache.StrongCacheStorage;

@SuppressWarnings("serial")
@Namespace("/weixin")
@InterceptorRef("defaultStack")  //不会进去到默认拦截器栈，就不进行拦截
public class WeixinAuthAction extends BaseAction{
	private static final Logger logger = Logger.getLogger(WeixinAuthAction.class);
	@Resource
	private WxuserService wxuserService;
	@Resource
	private SMSCodeService sMSCodeService;
	@Resource
	private PollDataService pollDataService;
	
	private String targetPath;
	private String personLocationIds = "123";
    private Integer aaa = 456;
	/**
	 * 用户消息和开发者需要的事件推送，将会被转发到该URL中
	 */
	@Action("check")            //所有关于微信端的接口都会进入到这里来   类似于这种https://api.weixin.qq.com/...
	public void check(){
		try {
			HttpServletRequest req = WebUtil.getRequest();
			//System.out.println(req.getCharacterEncoding());
			String method = req.getMethod();
			System.out.println(method);
			if("GET".equals(method)){
				String signature = req.getParameter("signature");
				String timestamp = req.getParameter("timestamp");
				String nonce = req.getParameter("nonce");
				String echostr = req.getParameter("echostr");
				if (StringUtils.isBlank(signature)
						|| StringUtils.isBlank(timestamp)
						|| StringUtils.isBlank(nonce)
						|| StringUtils.isBlank(echostr)) {
					return;
				}
				if(CheckUtil.checkSignature(signature, timestamp, nonce)){
					WebUtil.writeString(echostr);
				}
			}else{
				//POST请求
				String message = null;
				Map<String,String> map = MessageUtil.xmlToMap(req);
				for (String key : map.keySet()) {
					System.out.print(key+":");
					System.out.println(map.get(key));
				}
				String fromUserName = map.get("FromUserName");
				String toUserName = map.get("ToUserName");
				String msgType = map.get("MsgType");
				if(MessageUtil.MESSAGE_EVENT.equals(msgType)){//接收到事件类型的消息
					String eventType = map.get("Event");//获取事件类型
					if(MessageUtil.MESSAGE_WAITMSG.equals(eventType)){//如果是扫码带参事件
						String scanResult = map.get("ScanResult");//获取参数
						String eventKey = map.get("EventKey");//事件key值,用来区分相同类型事件
						message = wxuserService.processScancode(scanResult, eventKey, fromUserName);  //交互
					}else if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){//关注事件
							message = wxuserService.processSubscribe(fromUserName);//新增微信用户
					}else if(MessageUtil.MESSAGE_UNSUBSCRIBE.equals(eventType)){//取消关注事件
					}
				}else if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
					String content = map.get("Content");
					//用户发送消息时:根据消息进行回复
					//普通String类型的message
					message = wxuserService.getReplyContent(content,fromUserName);//(FromUserName	发送方帐号（一个OpenID）)
					//System.out.println(message);
				}
				if(!StringUtils.isBlank(message)){
					message = MessageUtil.initText(toUserName, fromUserName, message);  //组装文本消息
					//xml格式的String类型
					//System.out.println(message);
					WebUtil.writeString(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微信页面拦截器 
	 * 如果拦截不到用户的openid，则进此方法
	 * 获取微信昵称
	 */
	@Action("login")
	public String login() {
		try {
			String params = (String) getSession().getAttribute(WeChatInterceptor.WX_TARGET_PARAMS);
	    	targetPath = (String) getSession().getAttribute(WeChatInterceptor.WX_TARGET_PATH) + "?" + params;
	    	System.out.println("PATH =  "+targetPath);
	    	getSession().removeAttribute(WeChatInterceptor.WX_TARGET_PARAMS);
	    	getSession().removeAttribute(WeChatInterceptor.WX_TARGET_PATH);
			//第一步：login.jsp中引导用户默认授权，获取code值
			HttpServletRequest req = WebUtil.getRequest();
			String code = req.getParameter("code");
			//第二步：通过code值获取openid,access_token
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +AuthUtil.APPID+
					"&secret=" +AuthUtil.APPSECRET+
					"&code=" +code+
					"&grant_type=authorization_code";
			JSONObject jsonObject = AuthUtil.doGetJson(url);  //跟微信服务器交互
			String openid = jsonObject.getString("openid");
			String token = jsonObject.getString("access_token"); //网页授权的token
			//拉取用户信息(需scope为 snsapi_userinfo)
			String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
			String newInfoUrl = infoUrl.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
			JSONObject userInfo = AuthUtil.doGetJson(newInfoUrl);
			System.out.println(JSON.toJSONString(userInfo));
			String headimgurl = userInfo.getString("headimgurl");
			//第三步：将openid存入session
			WebUtil.getSession().setAttribute("wxopenid", openid);  //不管是手机端到服务器端，还是服务器端到手机端，只要是同一个http请求过去的，都是同一个session
			WebUtil.getSession().setAttribute("headimgurl", headimgurl);
			WeixinUser wu = wxuserService.getWXUserByOpenId(openid);
		} catch (Exception e) {
			e.printStackTrace();
			return "wxnoPermission";
		}
		return "wxLoginResult"; //会将targetPath传到struts.xml文件中       /w/o?M=D8B04CDEC6B4&  获取二维码中的参数
	}
	
	/**
	 * 发送短信验证码
	 */
	@Action("getCode")
	public void getCode() {
		try {
			HttpServletRequest req = WebUtil.getRequest();
			String phone = req.getParameter("phone");
			if(StringUtils.isBlank(phone)){
				writeJson(new AccessResult(false,""));
			}else{
				ArrayList<String> params = new ArrayList<String>();
				//生成6位随机数
				String code = "";
				for(int i=1;i<=6;i++){
					int d=(int)(Math.random()*9+1);
					code+=d;
				}
				System.out.println(code);
				params.add(code);
				SMSCode smscode= sMSCodeService.getSMSCodeByPhone(phone); 
				if(smscode==null){
					smscode = new SMSCode();
					smscode.setId(UUIDUtil.generate());
					smscode.setLastTime(new Date().getTime());
					smscode.setPhone(phone);
					smscode.setSmscode(code);
					sMSCodeService.save(smscode);
				}else{
					smscode.setLastTime(new Date().getTime());
					smscode.setSmscode(code);
					sMSCodeService.update(smscode);  
				}
				sendPhoneMessage(phone, params);
				writeJson(new AccessResult(true,code));
			}
		} catch (Exception e) {
			writeJson(new AccessResult(false,""));
		}
	}
	
	/**
	 * 同步发送手机验证码
	 */
	public void sendPhoneMessage(String phone,ArrayList<String> params){
		try {
			int appid = 1400047268;
			String appkey = "5e0e87a6bc2f28ddc221b7de8386ffe1";
			String nationCode = "86";// 国家码  123456为您申请绑定的验证码，请于2分钟内填写。如非本人操作，请忽略本短信。
			int tmplId = 63278;
			SmsSingleSender singleSender;// 初始化单发
			SmsSingleSenderResult singleSenderResult = new SmsSingleSenderResult();
			// 初始化单发
			singleSender = new SmsSingleSender(appid, appkey);
			//同步发送
			singleSenderResult = singleSender.sendWithParam(nationCode, phone, tmplId,params, "", "", "");
			System.out.println(singleSenderResult);
		} catch (Exception e) {
			
		}
		
	}
	
//	/**
//	 * 根据所在地区 获取：街道 小区  单元
//	 */
//	@Action("getRegionByLocation1")
//	public void getRegionByLocation1() {
//		try {
//			System.out.println(111);
//			writeJson(wxuserService.getRegionByLocation1(personLocationIds));
//		} catch (Exception e) {
//			logger.error("获取街道列表异常",e);
//			writeJson(new AccessResult(false,"获取街道列表异常!"));
//		}
//	}
	
	/**1
	 * 根据所在地区 获取：街道 
	 */
	@Action("getStreetByLocation")
	public void getStreetByLocation() {
		try {
			writeJson(wxuserService.getStreetByLocation(personLocationIds));
		} catch (Exception e) {
			logger.error("获取街道列表异常",e);
			writeJson(new AccessResult(false,"获取街道列表异常!"));
		}
	}
	
	/**2
	 * 根据街道获取 小区 
	 */
	@Action("getVillageByStreet")
	public void getVillageByStreet() {
		try {
			writeJson(wxuserService.getVillageByStreet(personLocationIds));
		} catch (Exception e) {
			logger.error("获取小区列表异常",e);
			writeJson(new AccessResult(false,"获取小区列表异常!"));
		}
	}
	
	/**3
	 * 根据小区获取 单元
	 */
	@Action("getUnitByVillage")
	public void getUnitByVillage() {
		try {
			writeJson(wxuserService.getUnitByVillage(personLocationIds));
		} catch (Exception e) {
			logger.error("获取单元列表异常",e);
			writeJson(new AccessResult(false,"获取单元列表异常!"));
		}
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getPersonLocationIds() {
		return personLocationIds;
	}

	public void setPersonLocationIds(String personLocationIds) {
		this.personLocationIds = personLocationIds;
	}
	
}
