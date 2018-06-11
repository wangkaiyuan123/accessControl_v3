package com.dhht.action.weixin;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.common.TeaEncodeUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.common.WebUtil;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.system.Person;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.weixin.DynamicRandom;
import com.dhht.entity.weixin.OfflineRecordFlag;
import com.dhht.entity.weixin.OpenDoorFlag;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.weixin.PersonService;
import com.dhht.service.weixin.PollDataService;
import com.dhht.service.weixin.WxuserService;
@SuppressWarnings("serial")
@Namespace("/w")
@InterceptorRef("wxInterceptorStack")
public class WeixinOpenDoorAction extends BaseAction{
	private static final Logger logger = Logger.getLogger(WeixinOpenDoorAction.class);
	@Resource
	private WxuserService wxuserService;
	@Resource
	private PersonService personService;
	@Resource
	private PollDataService pollDataService;
	
	private String searchUuid;
	@Action("o")
	public String ope() {
		try {
			final String openId = (String) getSession().getAttribute("wxopenid");
			System.out.println("openid = "+openId);
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxlogin"; //引导获取openID
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				//跳转关注
				return "goSubscribe";  //外面扫一扫提醒第一次关注微信公众号
			}
			//验证innerPerson
			if(wxuser.getInnerPerson()==0){//非内部人员,需跳转询问申请或进行访客访问
				String paramMsg =  WebUtil.getRequest().getParameter("M");
				if(!StringUtils.isBlank(paramMsg)){
					String deviceMac = paramMsg.substring(0,12);
					String time = paramMsg.substring(14, 30);
					getRequest().setAttribute("mac", deviceMac);
					getRequest().setAttribute("time", time);
					return "goRegister";   //转发，会打参数带过去
				}else{
					String pageMessage = "二维码错误,请重新扫码";
					getRequest().setAttribute("pageMessage", pageMessage);
					getRequest().setAttribute("mac", "");
					getRequest().setAttribute("time", "");
					return SUCCESS; 
				}
			}
			if(wxuser.getInnerPerson()==2){//审核中,暂时无法操作
				return "waitApproval";
			}
			if(wxuser.getInnerPerson()==1){//内部人员,将人员id存入session
				Person person = personService.getPersonByOpenid(openId);
				if(person!=null){
					getSession().setAttribute("personId", person.getId());
					String pageMessage = "";
					HttpServletRequest req = WebUtil.getRequest();
					//***********处理开门逻辑************
					//01.12--改成：http://mj.donghuahongtai.com/w/o?M=123456123456
					//01.12--改成：http://mj.donghuahongtai.com/w/o?M=123456123456T=2018011215560005
					String param = req.getParameter("M");
					//String random = "";
					String time = "";
					System.out.println(param);
					if(!StringUtils.isBlank(param)){
						if(param.length()==30){
							//random = param.substring(0, 6);
							final String deviceMac = param.substring(0,12);
							time = param.substring(14, 30);  //
							//验证人员权限
							if(person.getState()==2){
								pageMessage = "无权限，请等待申请的审核结果";
								getRequest().setAttribute("pageMessage", pageMessage);
								getRequest().setAttribute("mac", deviceMac);
								getRequest().setAttribute("time", time);
								return SUCCESS;
							}
							Device device = wxuserService.getDeviceByMAC(deviceMac);
							if(device==null){
								pageMessage = "设备未找到,请重新扫码";
								getRequest().setAttribute("pageMessage", pageMessage);
								getRequest().setAttribute("mac", deviceMac);
								getRequest().setAttribute("time", time);
								return SUCCESS;
							}
							final String deviceAddress = device.getDeviceAddress();
							if(StringUtils.isBlank(device.getUnitId())){
								pageMessage = "效验失败,门禁设备尚未关联单元";
								getRequest().setAttribute("pageMessage", pageMessage);
								getRequest().setAttribute("mac", deviceMac);
								getRequest().setAttribute("time", time);
								return SUCCESS;
							}
							PersonUnit pu = wxuserService.getPersonUnitByIds(device.getUnitId(), person.getId());
							if(pu==null){
								String myCard = "<a href=\"http://mj.donghuahongtai.com/weixinpage/myCard\">我的门卡</a>";
								//pageMessage = "无权限，您尚未申请关联此单元请到\"我的门卡\"中进行绑定单元操作";
								pageMessage = "无权限，您尚未申请关联此单元请到\""+myCard+"\"中进行绑定单元操作";
								getRequest().setAttribute("pageMessage", pageMessage);
								getRequest().setAttribute("mac", deviceMac);
								getRequest().setAttribute("time", time);
								return SUCCESS;
							}
							//调用算法，获取动态密码
							final String secret = TeaEncodeUtil.teaEncode(SendDataUtil.stringMacToByte(deviceMac), SendDataUtil.stringToHexByte(time));
							if(StringUtils.isBlank(secret)||(!StringUtils.isBlank(secret)&&secret.length()!=6)){
								System.out.println("算法出错*****************************************");
								return "算法出错，请稍后重试";
							}
							//*****************发送开门指令*******************
							/*0208修改：**************************/
							final String uuid = UUIDUtil.generate();
							wxuserService.insertOpenDoorFlag(uuid);   //先存起来
							/*0208修改：**************************/
							try {
								IoSession session = pollDataService.getSessionByMac(deviceMac);
								if(session!=null&&!session.isClosing()){
									SendDataUtil.sendData(session,SendDataUtil.openDoor(secret),"02");
									//pollDataService.putDataByMac(deviceMac, openId);
									System.out.println("开门指令密码："+secret);
									session.removeAttribute("uuid");
									session.removeAttribute("lastopendoorPerson");
									session.removeAttribute("lastopendoorTime");
									session.removeAttribute("secret");
									
									session.setAttribute("uuid", uuid);
									session.setAttribute("lastopendoorPerson", person.getId());
									session.setAttribute("lastopendoorTime", new Date().getTime());
									session.setAttribute("secret", secret);
								}else{
									//一般离线开门记录
									wxuserService.updateDynamicRandom(secret, deviceMac,person.getId());
									OfflineRecordFlag flag =  new OfflineRecordFlag();
									flag.setId(UUIDUtil.generate());
									flag.setMac(deviceMac);
									flag.setPersonId(person.getId());
									flag.setSecret(secret);
									flag.setTime(time);
									wxuserService.saveOfflineRecord(flag);
									pageMessage = "网络异常，请使用该密码开门:"+secret;
									getRequest().setAttribute("pageMessage", pageMessage);
									getRequest().setAttribute("mac", deviceMac);
									getRequest().setAttribute("time", time);
									return SUCCESS;
								}
							} catch (Exception e) {
								logger.error("mina异常", e);
								wxuserService.updateDynamicRandom(secret, deviceMac,person.getId());
								OfflineRecordFlag flag =  new OfflineRecordFlag();
								flag.setId(UUIDUtil.generate());
								flag.setMac(deviceMac);
								flag.setPersonId(person.getId());
								flag.setSecret(secret);
								flag.setTime(time);
								wxuserService.saveOfflineRecord(flag);
								pageMessage = "服务忙，请使用该密码开门:"+secret;
								getRequest().setAttribute("pageMessage", pageMessage);
								getRequest().setAttribute("mac", deviceMac);
								getRequest().setAttribute("time", time);
								return SUCCESS;
							}
							System.out.println("***************发送开门指令******************");
							pageMessage = "效验通过，请稍等...";
							getRequest().setAttribute("pageMessage", pageMessage);
							getRequest().setAttribute("mac", deviceMac);
							getRequest().setAttribute("time", time);   //二维码上的时间
							getRequest().setAttribute("uuid", uuid);
							final Timer timer = new Timer();
							TimerTask task = new TimerTask() {
								@Override
								public void run() {
									try {
										/*String flag = pollDataService.getDataByMac(deviceMac);
										if(!StringUtils.isBlank(flag)&&openId.equals(flag)){
											wxuserService.sendWeixinMessageForOpenDoor(2, openId, deviceAddress,secret);
										}*/
										OpenDoorFlag openDoorFlag = wxuserService.getOpenDoorFlagById(uuid);
										if(openDoorFlag!=null&&openDoorFlag.getFlag()==0){  //未处理设置为2 开门失败
											wxuserService.updateOpenDoorFlag(uuid, 2);
										}
									} catch (Exception e) {
										logger.error("", e);
									}
									timer.cancel();
								}
							};
							timer.schedule(task, 3000);  //10秒后执行该线程
							return SUCCESS;
							/*DynamicRandom dynamicRandom = wxuserService.getDynamicRandomByMAC(deviceMac);
							if(dynamicRandom==null){
								pageMessage = "设备未找到,请重新扫码";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							if(random.equals(dynamicRandom.getRandom1())||random.equals(dynamicRandom.getRandom2())){
							if((new Date().getTime()-dynamicRandom.getLastTime())>40000){
								pageMessage = "二维码已失效,请重新扫码";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							//匹配动态密码正确，再看此用户有没有权限
							//1根据openid查询人员，再查出人员关联的设备，有则发送开门指令
							//2查看有没有预约记录，有则发送开门指令
							if(person.getState()==2){
								pageMessage = "无权限，请等待审核结果";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							Device device = wxuserService.getDeviceByMAC(deviceMac);
							if(device==null){
								pageMessage = "设备未找到,请重新扫码";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							if(StringUtils.isBlank(device.getUnitId())){
								pageMessage = "效验失败,门禁设备尚未关联单元";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							PersonUnit pu = wxuserService.getPersonUnitByIds(device.getUnitId(), person.getId());
							if(pu==null){
								pageMessage = "无权限，您尚未申请关联此单元请到\"我的门卡\"中进行绑定单元操作";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							//*****************发送开门指令*******************
							try {
								IoSession session = pollDataService.getSessionByMac(deviceMac);
								if(session!=null&&!session.isClosing()){
									SendDataUtil.sendData(session,SendDataUtil.openDoor(),"02");
									session.removeAttribute("lastopendoorPerson");
									session.removeAttribute("lastopendoorTime");
									session.setAttribute("lastopendoorPerson", person.getId());
									session.setAttribute("lastopendoorTime", new Date().getTime());
								}else{
									pageMessage = "网络异常，请稍后重试";
									getRequest().setAttribute("pageMessage", pageMessage);
									return SUCCESS;
								}
							} catch (Exception e) {
								logger.error("mina异常", e);
								pageMessage = "服务忙，请稍后重试";
								getRequest().setAttribute("pageMessage", pageMessage);
								return SUCCESS;
							}
							System.out.println("***************发送开门指令******************");
							pageMessage = "效验通过，等待开门";
							getRequest().setAttribute("pageMessage", pageMessage);
							return SUCCESS;
						}else{
							pageMessage = "二维码已失效,请重新扫码";
						}*/
						}else{
							pageMessage = "二维码错误,请重新扫码";
							getRequest().setAttribute("pageMessage", pageMessage);
							getRequest().setAttribute("mac", "");
							getRequest().setAttribute("time", "");
							return SUCCESS;
						}
					}else{
						pageMessage = "二维码错误,请重新扫码";
						getRequest().setAttribute("pageMessage", pageMessage);
						getRequest().setAttribute("mac", "");
						getRequest().setAttribute("time", "");
						return SUCCESS;
					}
				}else{
					wxuser.setInnerPerson(0);
					wxuserService.update(wxuser);
					return "goRegister";
				}
			}
		} catch (Exception e) {
			logger.error("微信扫码开门异常", e);
			getRequest().setAttribute("pageMessage", "异常了，请重新操作!");
		}
		getRequest().setAttribute("mac", "");
		getRequest().setAttribute("time", "");
		return SUCCESS;
	}
	
	@Action("searchResult")
	public void searchResult() {
		try {
			OpenDoorFlag openDoorFlag = wxuserService.getOpenDoorFlagById(searchUuid); //flag = 0 初始值未处理
			if(openDoorFlag!=null){
				if(openDoorFlag.getFlag()==0){   //
					writeJson(new AccessResult(false,""));
				}else if(openDoorFlag.getFlag()==1){
					writeJson(new AccessResult(true,"开门成功，谢谢使用!"));
				}else if(openDoorFlag.getFlag()==2){
					writeJson(new AccessResult(true,"开门失败，请重新扫码!"));
				}
			}else{
				writeJson(new AccessResult(false,""));
			}
		} catch (Exception e) {
			
		}
	}

	
	
	public String getSearchUuid() {
		return searchUuid;
	}

	public void setSearchUuid(String searchUuid) {
		this.searchUuid = searchUuid;
	}
	
	
}
