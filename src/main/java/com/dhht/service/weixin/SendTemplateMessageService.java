package com.dhht.service.weixin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.dao.BaseDao;
import com.dhht.entity.system.Person;
import com.dhht.entity.template.Data_first;
import com.dhht.entity.template.Data_keyword;
import com.dhht.entity.template.Data_remark;
import com.dhht.entity.template.NewTemplate;
import com.dhht.entity.template.newData;
import com.dhht.entity.weixin.WeixinUser;
import com.weixin.pool.AccessToken;
import com.weixin.util.WeixinUtil;

@Service
public class SendTemplateMessageService {

	private static final String INDUSTRY_INFORMATION_URL = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN"; //get方式   获取设置的行业信息微信接口URL           
	private static final String TEMPLATE_ID_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN"; //post方式  获得模板ID微信接口URL
	private static final String TEMPLATE_LIST_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";//get方式    获取模板列表微信接口URL
	private static final String DELETE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";//post方式     删除模板微信接口URL
	private static final String TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN"; //post方式    发送模版消息微信接口URL

	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private BaseDao<WeixinUser> weixinUserDao;
	@Resource
	private WxuserService wxuserService;
	
	NewTemplate temp = new NewTemplate();
	newData data = new newData();
	Data_first first = new Data_first();
	Data_keyword keyword1 = new Data_keyword();
	Data_keyword keyword2 = new Data_keyword();
	Data_keyword keyword3 = new Data_keyword();
	Data_keyword keyword4 = new Data_keyword();
	Data_keyword keyword5 = new Data_keyword();
	Data_keyword keyword6 = new Data_keyword();
	Data_remark remark = new Data_remark();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //时间格式
	
	/**
	 * 发送模版消息——帐号审核通过
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage1(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		Date d = new Date();
		String nowtime = df.format(d);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String APPLAY_SUCCESS = wxuserService.getWxConfig("APPLAY_SUCCESS");
		if(StringUtils.isBlank(APPLAY_SUCCESS)||"null".equals(APPLAY_SUCCESS)){
			return false;
		}
		first.setValue("您好，你的审核请求已通过，欢迎使用。");
		first.setColor("#32CD32");
		keyword1.setValue(p.getName());
		keyword1.setColor("#32CD32");
		keyword2.setValue(p.getTelephone());
		keyword2.setColor("#32CD32");
		keyword3.setValue(nowtime);
		keyword3.setColor("#32CD32");
		remark.setValue("感谢您的使用");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setKeyword3(keyword3);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(APPLAY_SUCCESS);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true;
	}
	
	/**
	 * 发送模版消息——帐号审核未通过
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage2(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		Date d = new Date();
		String nowtime = df.format(d);
		String hql = " from WeixinUser where openid = '" + openId + "'";
		WeixinUser u = weixinUserDao.get(hql);
		String AUDIT_FAILURE = wxuserService.getWxConfig("AUDIT_FAILURE");
		if(StringUtils.isBlank(AUDIT_FAILURE)||"null".equals(AUDIT_FAILURE)){
			return false;
		}
		first.setValue("抱歉审核失败");
		first.setColor("#32CD32");
		keyword1.setValue(u.getNickname());
		keyword1.setColor("#32CD32");
		keyword2.setValue("抱歉，您的帐号审核失败，请您重新申请。");
		keyword2.setColor("#32CD32");
		keyword3.setValue(nowtime);
		keyword3.setColor("#32CD32");
		remark.setValue("您的帐号审核未通过，请您重新提交。");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setKeyword3(keyword3);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(AUDIT_FAILURE);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true;
	}
	
	/**
	 * 发送模版消息——申请审核提醒
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage3(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String AUDIT_NOTICE = wxuserService.getWxConfig("AUDIT_NOTICE");
		if(StringUtils.isBlank(AUDIT_NOTICE)||"null".equals(AUDIT_NOTICE)){
			return false;
		}
		first.setValue("你有新的审核通知，请尽快处理");
		first.setColor("#32CD32");
		keyword1.setValue("采购计划");//需要与业务结合，待改
		keyword1.setColor("#32CD32");
		keyword2.setValue(p.getName());//需要与业务结合，待改
		keyword2.setColor("#32CD32");
		remark.setValue("感谢你的使用");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(AUDIT_NOTICE);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true;
	}
	
	/**
	 * 发送模版消息——客户预约通知
	 * @param token
	 * @param openId 客户的id
	 * @param time 预约时间
	 * @param ytype 预约类型
	 */
	public boolean sendMessage4(String token,String openId,String time,String ytype){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String CUSTOMER_BOOKING_NOTICE = wxuserService.getWxConfig("CUSTOMER_BOOKING_NOTICE");
		if(StringUtils.isBlank(CUSTOMER_BOOKING_NOTICE)||"null".equals(CUSTOMER_BOOKING_NOTICE)){
			return false;
		}
		first.setValue("有新的客户预约，请及时确认");
		first.setColor("#32CD32");
		keyword1.setValue(p.getName());
		keyword1.setColor("#32CD32");
		keyword2.setValue(p.getTelephone());
		keyword2.setColor("#32CD32");
		keyword3.setValue(time);
		keyword3.setColor("#32CD32");
		keyword4.setValue(ytype);
		keyword4.setColor("#32CD32");
		remark.setValue("点击确认预约时间，或修改预约时间。");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setKeyword3(keyword3);
		data.setKeyword4(keyword4);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(CUSTOMER_BOOKING_NOTICE);
		temp.setUrl("www.baidu.com");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true; 
	}
	
	/**
	 * 发送模版消息——预约成功通知
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage5(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		String APPOINTMENT_SUCCESSFUL = wxuserService.getWxConfig("APPOINTMENT_SUCCESSFUL");
		if(StringUtils.isBlank(APPOINTMENT_SUCCESSFUL)||"null".equals(APPOINTMENT_SUCCESSFUL)){
			return false;
		}
		first.setValue("访客预约开门成功提醒");
		first.setColor("#32CD32");
		keyword1.setValue("预约开门");
		keyword1.setColor("#32CD32");
		keyword2.setValue("微信扫码");
		Date d = new Date();
		String nowtime = df.format(d);
		keyword2.setColor("#32CD32");
		keyword3.setValue(nowtime);
		keyword3.setColor("#32CD32");
//		keyword4.setValue("/");
//		keyword4.setColor("#32CD32");
		remark.setValue("感谢您的使用");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setKeyword3(keyword3);
//		data.setKeyword4(keyword4);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(APPOINTMENT_SUCCESSFUL);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true;
	}
	
	/**
	 * 发送模版消息——预约取消通知
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage6(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		
		String CANCELLATION_OF_APPOINTMENT = wxuserService.getWxConfig("CANCELLATION_OF_APPOINTMENT");
		if(StringUtils.isBlank(CANCELLATION_OF_APPOINTMENT)||"null".equals(CANCELLATION_OF_APPOINTMENT)){
			return false;
		}
		first.setValue("访客预约开门失败提醒");
		first.setColor("#32CD32");
		keyword1.setValue("预约开门");
		keyword1.setColor("#32CD32");
		keyword2.setValue("已拒绝");
		keyword2.setColor("#32CD32");
		remark.setValue("您预约的开门已被对方拒绝，请重新预约");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(CANCELLATION_OF_APPOINTMENT);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true;
	}
	
	/**
	 * 发送模版消息——审核成功通知
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage7(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		Date d = new Date();
		String nowtime = df.format(d);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String AUDIT_SUCCESS = wxuserService.getWxConfig("AUDIT_SUCCESS");
		if(StringUtils.isBlank(AUDIT_SUCCESS)||"null".equals(AUDIT_SUCCESS)){
			return false;
		}
		first.setValue("您发布的兼职已通过审核，快去看看吧");
		first.setColor("#32CD32");
		keyword1.setValue(p.getName());
		keyword1.setColor("#32CD32");
		keyword2.setValue("二维码推广");
		keyword2.setColor("#32CD32");
		keyword3.setValue(nowtime);
		keyword3.setColor("#32CD32");
		remark.setValue("感谢您的使用");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setKeyword3(keyword3);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(AUDIT_SUCCESS);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true; 
	}
	
	/**
	 * 发送模版消息——审核通过提醒
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage8(String token,String openId){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		Date d = new Date();
		String nowtime = df.format(d);
		String AUDIT_PASSED = wxuserService.getWxConfig("AUDIT_PASSED");
		if(StringUtils.isBlank(AUDIT_PASSED)||"null".equals(AUDIT_PASSED)){
			return false;
		}
		first.setValue("你好，你提交的资料已通过审核。");
		first.setColor("#32CD32");
		keyword1.setValue("通过审核");
		keyword1.setColor("#32CD32");
		keyword2.setValue(nowtime);
		keyword2.setColor("#32CD32");
		remark.setValue("你的维泽无线已经可以正常使用了。");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(AUDIT_PASSED);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true;  
	}
	
	/**
	 * 发送模版消息——绑定单元审核失败
	 * @param token
	 * @param openId
	 */
	public boolean sendMessage9(String token,String openId,String reason){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		Date d = new Date();
		String nowtime = df.format(d);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String AUDIT_FAILURE_UNIT = wxuserService.getWxConfig("AUDIT_FAILURE_UNIT");
		if(StringUtils.isBlank(AUDIT_FAILURE_UNIT)||"null".equals(AUDIT_FAILURE_UNIT)){
			return false;
		}
		first.setValue("您好，您有一条被驳回房源待处理。");
		first.setColor("#32CD32");
		keyword1.setValue(p.getUnitName());
		keyword1.setColor("#32CD32");
		keyword2.setValue(reason);
		keyword2.setColor("#32CD32");
		remark.setValue("请在24小时内登陆电脑端完成修改，谢谢配合");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(AUDIT_FAILURE_UNIT);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		System.out.println(jsonObject);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
            System.out.println(result);           	
        }  
        return true; 
	}
	
	/**
	 * 开门成功或失败发送模板消息:1成功  2失败
	 */
	public boolean sendMessageOpneDoor(String token,String openId,int type,String macName,String secret){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		Date d = new Date();
		String nowtime = df.format(d);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String OPENDOORRESULT = "";
		if(type==1){
			OPENDOORRESULT = wxuserService.getWxConfig("OPENDOORSUCCESS");
		}else if(type==2){
			OPENDOORRESULT = wxuserService.getWxConfig("OPENDOORFAIL");
		}
		if(StringUtils.isBlank(OPENDOORRESULT)||"null".equals(OPENDOORRESULT)){
			return false;
		}
		if(type==1){
			first.setValue("开门成功,谢谢使用!");
		}else{
			first.setValue("请使用该密码开门:"+secret);
		}
		first.setColor("#FF0000");
		keyword1.setValue(nowtime);
		keyword1.setColor("#32CD32");
		keyword2.setValue(p.getName());
		keyword2.setColor("#32CD32");
		if(type==1){
			keyword3.setValue(macName);
			keyword3.setColor("#32CD32");
		}
		remark.setValue("使用微信扫一扫，轻松开门！");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		if(type==1){
			data.setKeyword3(keyword3);
		}
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(OPENDOORRESULT);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
        }  
        return true; 
	}
	
	/**
	 * 预约结果通知
	 */
	public boolean sendMessageVisitor(String token,String openId,String personName,String contant,int type){
		String url = TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", token);
		String hql = " from Person where openid = '" + openId + "'";
		Person p = personDao.get(hql);
		String VISITOR_RESULT = wxuserService.getWxConfig("VISITOR_RESULT");
		if(StringUtils.isBlank(VISITOR_RESULT)||"null".equals(VISITOR_RESULT)){
			return false;
		}
		first.setValue(contant);
		first.setColor("#FF0000");
		keyword1.setValue(personName);
		keyword1.setColor("#32CD32");
		keyword2.setValue("预约开门");
		keyword2.setColor("#32CD32");
		if(type==1){
			keyword3.setValue("成功");
		}else {
			keyword3.setValue("失败");
		}
		keyword3.setColor("#FF0000");
		remark.setValue("使用微信扫一扫，轻松开门！");
		remark.setColor("#32CD32");
		
		data.setFirst(first);
		data.setKeyword1(keyword1);
		data.setKeyword2(keyword2);
		data.setKeyword3(keyword3);
		data.setRemark(remark);
		
		temp.setTouser(openId);
		temp.setTemplate_id(VISITOR_RESULT);
		temp.setUrl("");
		temp.setColor("#32CD32");
		temp.setData(data);
		
		String openIdList = JSONObject.toJSONString(temp);
		String jsonObject = WeixinUtil.doPostStrs(url, openIdList);
		int result = 0;
        if (!StringUtils.isBlank(jsonObject)) {  
        	if(jsonObject.indexOf("errcode")>=0){
    			if (jsonObject.indexOf("42001") >= 0
    					|| jsonObject.indexOf("40001") >= 0
    					|| jsonObject.indexOf("40014") >= 0
    					|| jsonObject.indexOf("41001") >= 0
    					|| jsonObject.indexOf("-1") >= 0) {// token超时或失效
    				AccessToken accesstoken = WeixinUtil.getAccessToken();
    				if(accesstoken.getErrcode()>0){			
    					System.out.println("获取access_token时errcode:"+accesstoken.getErrcode());					
    					return false;
    				}
    				wxuserService.updateToken(accesstoken.getToken());	
    				return false;
    			}
    		}
            result = jsonObject.indexOf("errcode");  
        }  
        return true; 
	}
	
	
}
