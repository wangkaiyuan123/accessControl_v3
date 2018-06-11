package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

//import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStream;
import com.weixin.pool.Image;
import com.weixin.pool.ImageMessage;
import com.weixin.pool.News;
import com.weixin.pool.NewsMessage;
import com.weixin.pool.TextMessage;
/**
 * xml转为map集合
 * @author Administrator
 *
 */
public class MessageUtil {
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_SCANCODE = "scancode_push";
	public static final String MESSAGE_WAITMSG = "scancode_waitmsg";
	
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String, String>();
		SAXReader reader =new SAXReader();
		InputStream ins = request.getInputStream();
		org.dom4j.Document doc = reader.read(ins);		
		Element root = doc.getRootElement();		
		List<Element> list = root.elements();
		for (Element element : list) {
			map.put(element.getName(), element.getText());
			if(element.elements()!=null){
				List<Element> list1 = element.elements();
				for (Element element1 : list1) {
					map.put(element1.getName(), element1.getText());
				}
			}
		}
		ins.close();
		return map;
	}
	
	/**
	 * 将文本消息对象转换为XML
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * 组装文本消息
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToXml(text);
	}
	
	/**
	 * 组装主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注,请按关键字进行操作:\n");
		sb.append("回复1、菜单介绍\n");
		sb.append("回复2、公众号介绍\n");
		sb.append("回复3、查看个人账号\n\n");
		sb.append("回复?调出此提示。");
		return sb.toString();
	}
	
	/**
	 * 菜单1：菜单介绍
	 * @return
	 */
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("扫码开门：用户在获得开门权限后可扫描二维码开门\n");
		sb.append("访客预约：访客提前预约，审核通过后即可在预约时间开门\n");
		sb.append("我的门卡：可以查看个人门卡信息，需提前申请\n");
		sb.append("开门记录：可查看个人所有开门记录\n");
		return sb.toString();
	}
	
	/**
	 * 菜单2：公众号介绍
	 * @return
	 */
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("本公众号主要用于小区门禁开启，访客预约，开门记录查询等公共服务。");
		return sb.toString();
	}
	
	/**
	 * 菜单3：查看个人账号
	 * @return
	 */
	public static String thirdMenu(Integer account){
		StringBuffer sb = new StringBuffer();
		sb.append("您的账号是");
		sb.append(account);
		sb.append("，此账号用于区分微信用户，请牢记!");
		return sb.toString();
	}
	
//	/**
//	 * 菜单4：查看申请进度
//	 * @return
//	 */
//	public static String fourMenu(){
//		StringBuffer sb = new StringBuffer();
//		sb.append("您的申请正在审核中，请耐心等待!");
//		return sb.toString();
//	}
	
	/**
	 * 运维入口
	 * @return
	 */
	public static String yunweiMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"http://g6mud8.natappfree.cc/weixinpage/yunweiPage\">点击进入运维登录页面</a>");
		return sb.toString();
	}
	
	/**
	 * 将图文消息对象转换为XML
	 * @param textMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * 组装图文消息
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		News news1 = new News();
		news1.setTitle("此处标题");
		news1.setDescription("此处为新闻描述");
		news1.setPicUrl("此处为图片地址url");
		news1.setUrl("此处为跳转页面url");
		newsList.add(news1);
		
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	/**
	 * 组装图片消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initImageMessage(String toUserName,String fromUserName){
		String message = null;
		Image image = new Image();
		image.setMediaId("DnHGdOZllps-qg8ghgGWaMSonJCoEdie49SmFVFF2-RK-Oji7Tudyc4KN8NWm8RQ");
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message = imageMessageToXml(imageMessage);
		return message;
	}
	
	/**
	 * 图片消息转换成xml
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
}
