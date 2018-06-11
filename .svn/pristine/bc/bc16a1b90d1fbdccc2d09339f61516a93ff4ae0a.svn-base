package com.dhht.action.weixin;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.common.QrcodeUtil;
import com.dhht.common.TeaEncodeUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.common.UploadFileUtils;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.entity.record.UserApplyRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.User;
import com.dhht.entity.template.newData;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.Unit;
import com.dhht.entity.visitors.VisitorOfflineFlag;
import com.dhht.entity.visitors.VisitorRecord;
import com.dhht.entity.weixin.OpenDoorFlag;
import com.dhht.entity.weixin.WeixinRegister;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.mina.server.SendDataUtil;
import com.dhht.service.weixin.PersonService;
import com.dhht.service.weixin.PollDataService;
import com.dhht.service.weixin.VisitorRecordService;
import com.dhht.service.weixin.WxuserService;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/weixinpage")
@InterceptorRef("wxInterceptorStack")
@SuppressWarnings("serial")
public class WeixinPageAction extends BaseAction implements ModelDriven<WeixinRegister>{
	private static final Logger logger = Logger.getLogger(WeixinPageAction.class);
	
	WeixinRegister weixinRegister = new WeixinRegister();

	@Override
	public WeixinRegister getModel() {
		return weixinRegister;
	}
	@Resource
	private WxuserService wxuserService;
	@Resource
	private PersonService personService;
	@Resource
	private PollDataService pollDataService;
	@Resource
	private VisitorRecordService visitorRecordService;
	
	private File cardFile;
	
	private String approvalToken;
	
	private String approvalFlag;
	
	/**
	 * 0109新增"我是访客"入口
	 */
	@Action("visitor")    //返回success则跳转到对应的  weixinpage/visitor.jsp  返回其他的 对应跳到  weixinpage/XXX.jsp
	public String visitor(){
		try {
			String mac = getRequest().getParameter("mac");
			String time = getRequest().getParameter("time");
			System.out.println("mac================>"+mac);
			System.out.println("time================>"+time);
			String openId = (String) getSession().getAttribute("wxopenid");  //把微信端就当作浏览器
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxnoPermission";
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				wxuser = new WeixinUser();
				wxuser.setId(UUIDUtil.generate());
				wxuser.setOpenid(openId);
				wxuser.setAddtime(new Date());
				wxuser.setInnerPerson(0);
				wxuser.setCity("");
				wxuser.setNickname("");
				wxuser.setSex(0);
				wxuserService.save(wxuser);
			}
			if(wxuser.getInnerPerson()==0){ //我是访客
				//访问的是哪台设备
				Device device = wxuserService.getDeviceByMAC(mac);
				VisitorRecord record = new VisitorRecord();
				record.setId(UUIDUtil.generate());
				record.setVisitorOpenId(openId);   
				record.setVisitorPersonId("--");
				record.setVisitorName("--");
				record.setMac(mac);
				String secret = TeaEncodeUtil.teaEncode(SendDataUtil.stringMacToByte(mac), SendDataUtil.stringToHexByte(time)); //加密生成密码
				if(StringUtils.isBlank(secret)){  //防止其抛异常
					secret = "";
				}
				record.setSecret(secret);
				record.setUnitId("");
				if(device!=null){
					record.setUnitId(device.getUnitId()); //设备所关联的的单元
				}
				record.setAppointLongTime(new Date().getTime());  //预约时间
				record.setFlag(0); 
				record.setOpenDate(null);
				visitorRecordService.insertVisitorRecord(record);  //点击我是访客，插入一条记录
				String url = "http://mj.donghuahongtai.com/weixinpage/approvalVisitor?token="+record.getId();  //token就是Id
				String uid = UUIDUtil.generate();
				String path = "dhht/qrcodes/"+uid+".png";
				QrcodeUtil.createQrcode(url, uid);
				getRequest().setAttribute("path", path);
				return SUCCESS;
			}
			if(wxuser.getInnerPerson()==2){//审核中,暂时无法操作
				return "waitApproval";
			}
			if(wxuser.getInnerPerson()==1){
				Person person = personService.getPersonByOpenid(openId);
				if(person!=null){
					Device device = wxuserService.getDeviceByMAC(mac);
					if(device==null){
						getRequest().setAttribute("errorMsg", "设备未找到,请重新扫码");
						return "approvalError";
					}
					if(StringUtils.isBlank(device.getUnitId())){
						getRequest().setAttribute("errorMsg", "门禁设备尚未关联单元");
						return "approvalError";
					}
					VisitorRecord record = new VisitorRecord();
					record.setId(UUIDUtil.generate());
					record.setVisitorOpenId(openId);   
					record.setVisitorPersonId(person.getId());
					record.setVisitorName(person.getName());
					record.setMac(mac);
					String secret = TeaEncodeUtil.teaEncode(SendDataUtil.stringMacToByte(mac), SendDataUtil.stringToHexByte(time)); //加密生成密码
					if(StringUtils.isBlank(secret)){  //防止其抛异常
						secret = "";
					}
					record.setSecret(secret);
					record.setUnitId(device.getUnitId()); //设备所关联的的单元
					record.setAppointLongTime(new Date().getTime());  //预约时间
					record.setFlag(0);  // 0未审批    1同意    2拒绝
					record.setOpenDate(null);
					visitorRecordService.insertVisitorRecord(record);
					getRequest().setAttribute("mac", mac);
					getRequest().setAttribute("time", time);
					getRequest().setAttribute("token", record.getId());  // token就是预约访客记录的Id
					String url = "http://mj.donghuahongtai.com/weixinpage/approvalVisitor?token="+record.getId();
					String uid = UUIDUtil.generate();
					String path = "dhht/qrcodes/"+uid+".png";
					QrcodeUtil.createQrcode(url, uid);
					getRequest().setAttribute("path", path);
				}else{
					wxuser.setInnerPerson(0);
					wxuserService.update(wxuser);
					return "goRegister";
				}
			}
		} catch (Exception e) {
			logger.error("跳转我是访客页面异常", e);
			return "wxException";
		}
		return SUCCESS;
	}
	
	/**
	 * 0109新增审批访客入口(被访者识别图中二维码入口)
	 */
	@Action("approvalVisitor")
	public String approvalVisitor(){
		try {
			String token = getRequest().getParameter("token");  //预约访问记录id
			System.out.println("token================>"+token);
			String openId = (String) getSession().getAttribute("wxopenid");  //这里获取的被访者的openId
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxnoPermission";
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				wxuser = new WeixinUser();
				wxuser.setId(UUIDUtil.generate());
				wxuser.setOpenid(openId);
				wxuser.setAddtime(new Date());
				wxuser.setInnerPerson(0);
				wxuser.setCity("");
				wxuser.setNickname("");
				wxuser.setSex(0);
				wxuserService.save(wxuser);
			}
			String errorMsg = "";
			VisitorRecord record = visitorRecordService.getRecord(token); //访客那条记录
			if(record==null){
				errorMsg = "二维码错误，请稍后重试";
				getRequest().setAttribute("errorMsg", errorMsg);
				return "approvalError";
			}
			if(wxuser.getInnerPerson()==0){//非内部人员,需跳转询问申请
				wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友尚无权限开门", 2);
				return "goRegister";
			}
			if(wxuser.getInnerPerson()==2){//审核中,暂时无法操作
				wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友尚无权限开门", 2);
				return "waitApproval";
			}
			if(wxuser.getInnerPerson()==1){
				Person person = personService.getPersonByOpenid(openId);
				if(person!=null){
					// TODO: handle exception    approvalError
					if(record!=null){
						if(record.getFlag()!=0){
							errorMsg = "此申请已审批，请重新申请";
							getRequest().setAttribute("errorMsg", errorMsg);
							return "approvalError";
						}
						PersonUnit pu = wxuserService.getPersonUnitByIds(record.getUnitId(), person.getId());
						if(pu==null){
							errorMsg = "您尚未此门禁的权限,无法审批";
							getRequest().setAttribute("errorMsg", errorMsg);
							wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友尚无权限开门", 2);
							return "approvalError";
						}
					}
				}else{
					wxuser.setInnerPerson(0);
					wxuserService.update(wxuser);
					return "goRegister";
				}
			}
			getRequest().setAttribute("token", token);  //访客记录这的id
		} catch (Exception e) {
			logger.error("跳转审批访客页面异常", e);
			return "wxException";
		}
		return SUCCESS;
	}
	
	//*******************加一个审批的接口，参数1：token，参数2flag，//flag=1同意，2不同意 approval
	@Action("doApproval")
	public void doApproval() {
		try {
			final String openId = (String) getSession().getAttribute("wxopenid");
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				writeJson(new AccessResult(false,"无权限，请在微信端操作"));
				return;
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				wxuser = new WeixinUser();
				wxuser.setId(UUIDUtil.generate());
				wxuser.setOpenid(openId);
				wxuser.setAddtime(new Date());
				wxuser.setInnerPerson(0);
				wxuser.setCity("");
				wxuser.setNickname("");
				wxuser.setSex(0);
				wxuserService.save(wxuser);
			}
			VisitorRecord record = visitorRecordService.getRecord(approvalToken);  //set get进来的
			if(record==null){
				writeJson(new AccessResult(false,"异常了，请稍后重试"));
				return;
			}
			final String visitorOpenId = record.getVisitorOpenId();
			final String visitorName = record.getVisitorName();
			final String visitorRecordId = record.getId();
			if(wxuser.getInnerPerson()==0){//非内部人员,需跳转询问申请
				writeJson(new AccessResult(false,"无权限"));
				wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友尚无权限开门", 2);
				return;
			}
			if(wxuser.getInnerPerson()==2){//审核中,暂时无法操作
				writeJson(new AccessResult(false,"无权限"));
				wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友尚无权限开门", 2);
				return;
			}
			if(wxuser.getInnerPerson()==1){
				Person person = personService.getPersonByOpenid(openId);
				if(person!=null){
					if(record!=null){
						if(record.getFlag()!=0){
							writeJson(new AccessResult(false,"此申请已被审批，无法再次操作"));
							return;
						}
						if((new Date().getTime()/1000)-(record.getAppointLongTime()/1000)>60){
							writeJson(new AccessResult(false,"申请已超时，请访客重新预约"));
							wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "预约已超时，请重新预约", 2);
							record.setFlag(2);
							visitorRecordService.updateRecord(record);
							return;
						}
						final String secret = record.getSecret();
						PersonUnit pu = wxuserService.getPersonUnitByIds(record.getUnitId(), person.getId());
						if(pu==null){
							writeJson(new AccessResult(false,"无权限"));
							wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友尚无权限开门", 2);
							return;
						}
						//开门指令*********************  同意开门
						/*0212修改：**************************/
						final String uuid = UUIDUtil.generate();
						wxuserService.insertOpenDoorFlag(uuid);
						/*0212修改：**************************/
						try {
							IoSession session = pollDataService.getSessionByMac(record.getMac());
							if(session!=null&&!session.isClosing()){
								if("2".equals(approvalFlag)){
									record.setApproverPersonId(person.getId());
									record.setApproverName(person.getName());
									record.setFlag(4); //在线拒绝授权
									record.setOpenDate(new Date());
									visitorRecordService.updateRecord(record);
									writeJson(new AccessResult(true,"已拒绝"));
									wxuserService.sendWeixinMessageForVisitor(record.getVisitorOpenId(), record.getVisitorName(), "您的好友已拒绝开门", 2);
									wxuserService.sendWeixinMessageForVisitor(openId, visitorName, "已拒绝开门", 2);
									return;
								}
								//同意发送开门指令
								SendDataUtil.sendData(session,SendDataUtil.openDoor(secret),"02");  //发送开门指令
								//pollDataService.putDataByMac(record.getMac(), openId);
								session.removeAttribute("uuid");
								session.removeAttribute("lastopendoorPerson");
								session.removeAttribute("lastopendoorTime");
								session.removeAttribute("secret");
								
								session.setAttribute("uuid", uuid);
								session.setAttribute("lastopendoorPerson", person.getId());
								session.setAttribute("lastopendoorTime", new Date().getTime());
								session.setAttribute("secret", secret);
								writeJson(new AccessResult(true,"已同意"));
								record.setApproverPersonId(person.getId());
								record.setApproverName(person.getName());
								record.setFlag(1);
								record.setOpenDate(new Date());
								visitorRecordService.updateRecord(record);
								final String deviceMac = record.getMac();
								//final String visitorOpenId = record.getVisitorOpenId();
								final Timer timer = new Timer();
								TimerTask task = new TimerTask() {
									@Override
									public void run() {
										try {
											/*String flag = pollDataService.getDataByMac(deviceMac);
											if(!StringUtils.isBlank(flag)&&openId.equals(flag)){
												wxuserService.sendWeixinMessageForOpenDoor(2, openId, "",secret);
												wxuserService.sendWeixinMessageForOpenDoor(2, visitorOpenId, "",secret);
											}*/
											System.out.println("###定时任务");
											OpenDoorFlag openDoorFlag = wxuserService.getOpenDoorFlagById(uuid);
											VisitorRecord vrecord = visitorRecordService.getRecord(visitorRecordId);            
											if(openDoorFlag!=null){  //
												if(openDoorFlag.getFlag()==0){
													openDoorFlag.setFlag(2); //没收到开门
													vrecord.setFlag(2); //开门失败
													visitorRecordService.updateRecord(vrecord);
													wxuserService.updateOpenDoorFlag(openDoorFlag);
													wxuserService.sendWeixinMessageForVisitor(visitorOpenId, visitorName, "开门失败，请重新预约", 2);
													wxuserService.sendWeixinMessageForVisitor(openId, visitorName, "开门失败，请访客重新预约", 2);
												}else if(openDoorFlag.getFlag()==1){
													wxuserService.sendWeixinMessageForVisitor(visitorOpenId, visitorName, "开门成功，谢谢使用", 1);
													wxuserService.sendWeixinMessageForVisitor(openId, visitorName, "开门成功，谢谢使用", 1);
												}else if(openDoorFlag.getFlag()==2){
													wxuserService.sendWeixinMessageForVisitor(visitorOpenId, visitorName, "开门失败，请重新预约", 2);
													wxuserService.sendWeixinMessageForVisitor(openId, visitorName, "开门失败，请访客重新预约", 2);
												}
											}
										} catch (Exception e) {
											System.out.println("====================================>预约审核异常");
										}
										timer.cancel();
									}
								};
								timer.schedule(task, 3000);
								
							}else{
								/**
								 * 被访者如果点击不同意，访客还是通过密码开门
								 * 还有就是访客记录的问题
								 * 设备离线情况下,访客开门往访客那边推送模板消息,访客开门记录展示,
								 *   离线 1:同意授权(等到了离线开门记录上传后，再将其改为开门成功)
								 *   离线 2:拒绝授权 (同样也要生成记录)
								 */
								VisitorOfflineFlag fOfflineFlag = new VisitorOfflineFlag();
								fOfflineFlag.setId(UUIDUtil.generate());
								fOfflineFlag.setMac(record.getMac());
								fOfflineFlag.setVisitorRecordId(record.getId());
								fOfflineFlag.setSecreat(record.getSecret());  //这个是关键
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								fOfflineFlag.setTime(sdf.format(new Date()));             
								visitorRecordService.addOfflineFlag(fOfflineFlag);  //离线我就插入一条标记
								if("1".equals(approvalFlag)){
									record.setFlag(3);
								}else if("2".equals(approvalFlag)){
									record.setFlag(4);
								}
								record.setApproverPersonId(person.getId());
								record.setApproverName(person.getName());
								record.setOpenDate(new Date());
								wxuserService.updateDynamicRandom(secret, record.getMac(),person.getId());
								if(record.getFlag()==3){  //授权同意
									writeJson(new AccessResult(false,"设备离线,请您的访客使用该密码开门:"+secret));
									wxuserService.sendWeixinMessageForVisitor(visitorOpenId, visitorName, "设备离线,请使用该密码开门:"+secret, 1);
								}else if(record.getFlag()==4){  //授权拒绝
									writeJson(new AccessResult(false,"不同意"));
									wxuserService.sendWeixinMessageForVisitor(visitorOpenId, visitorName, "被访者未同意", 2);
								}
								visitorRecordService.updateRecord(record);
								return;
							}
						} catch (Exception e) {
							logger.error("mina异常", e);
							wxuserService.updateDynamicRandom(secret, record.getMac(),person.getId());
							record.setFlag(Integer.parseInt(approvalFlag));
							visitorRecordService.updateRecord(record);
							writeJson(new AccessResult(false,"服务忙，请您的访客使用该密码开门:"+secret));
							wxuserService.sendWeixinMessageForVisitor(visitorOpenId, visitorName, "设备离线,请使用该密码开门:"+secret, 1);
							return;
						}
					}
				}else{
					wxuser.setInnerPerson(0);
					wxuserService.update(wxuser);
					writeJson(new AccessResult(false,"无权限"));
					return;
				}
			}
		} catch (Exception e) {
			logger.error("审批访客预约异常", e);
			writeJson(new AccessResult(false,"审批异常"));
		}
	}
	
	/**
	 * 微信端跳转到：访客预约页面
	 */
	@Action("visitorAppointment")
	public String visitorAppointment() {
		try {
			String openId = (String) getSession().getAttribute("wxopenid");
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxnoPermission";
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				wxuser = new WeixinUser();
				wxuser.setId(UUIDUtil.generate());
				wxuser.setOpenid(openId);
				wxuser.setAddtime(new Date());
				wxuser.setInnerPerson(0);
				wxuser.setCity("");
				wxuser.setNickname("");
				wxuser.setSex(0);
				wxuserService.save(wxuser);
			}
		} catch (Exception e) {
			logger.error("跳转访客预约页面异常", e);
		}
		return SUCCESS;
	}
	
	/**
	 * 微信端跳转到：我的门卡页面
	 */
	@Action("myCard")
	public String myCard() {
		try {
			String openId = (String) getSession().getAttribute("wxopenid");
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxnoPermission";
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				wxuser = new WeixinUser();
				wxuser.setId(UUIDUtil.generate());
				wxuser.setOpenid(openId);
				wxuser.setAddtime(new Date());
				wxuser.setInnerPerson(0);
				wxuser.setCity("");
				wxuser.setNickname("");
				wxuser.setSex(0);
				wxuserService.save(wxuser);
			}
			if(wxuser.getInnerPerson()==0){//非内部人员,需跳转询问申请
				return "goRegister";
			}
			if(wxuser.getInnerPerson()==2){//审核中,暂时无法操作
				return "waitApproval";
			}
			if(wxuser.getInnerPerson()==1){//内部人员,将人员id存入session
				Person person = personService.getPersonByOpenid(openId);
				if(person!=null){
					getSession().setAttribute("personId", person.getId());
					DoorCard doorCard = new DoorCard();
					String cardId = StringUtils.isBlank(person.getCardId())?"":person.getCardId();
					String personName = person.getName();
					doorCard = personService.getDoorCardByCardId(cardId);
					
					List<PersonUnit> listPersonUnitId = new ArrayList<PersonUnit>();
					List<Unit> list = new ArrayList<Unit>();
					listPersonUnitId = personService.getUnitIdByPersonId(person.getId());
					if(listPersonUnitId != null && listPersonUnitId.size() > 0){
						for(PersonUnit p:listPersonUnitId){
							Unit unit = personService.getUnitById(p.getUnitId());
							if(unit!=null){
								Region region = personService.getCommunityNameById(unit.getCommunityId());
								if(region!=null){
									unit.setCommunityName(region.getRegionName());
								}else{
									unit.setCommunityName("");
								}
								list.add(unit);
							}
						}
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Map<String, Object> map = new HashMap<String, Object>();
					if(doorCard != null){	
						String recentTime = df.format(doorCard.getRecentTime());
						map.put("cardNo", doorCard.getCardNo());
						map.put("cardNumber", doorCard.getCardNumber());
						map.put("personName", personName);
						map.put("recentTime", recentTime);
						map.put("list", JSON.toJSON(list));
//						map.put("comment", doorCard.getComment());	
						
					}else{
						map.put("cardNo", "暂无");
						map.put("cardNumber", "暂无");
						map.put("personName", personName);
						map.put("recentTime", "暂无");
						map.put("list", JSON.toJSON(list));
					}
					getRequest().setAttribute("map", map);
				}else{
					wxuser.setInnerPerson(0);
					wxuserService.update(wxuser);
					return "goRegister";
				}
			}
		} catch (Exception e) {
			logger.error("微信跳转我的门卡页面异常", e);
			return "wxException";
		}
		return SUCCESS;
	}
	
	/**
	 * 微信跳转绑定单元页面
	 */
	@Action("toNewUnit")
	public String toNewUnit() {
		try {
			//带入地区选项
			getRequest().setAttribute("regionList", JSONObject.toJSONString(wxuserService.getRegionList()));
		} catch (Exception e) {
			logger.error("微信跳转绑定单元页面异常", e);
			return "wxException";
		}
		return SUCCESS;
	}
	
	/**
	 * 新增绑定单元表提交
	 */
	@Action("addNewUnit")
	public void addNewUnit(){
		try {
			String personId = (String) getSession().getAttribute("personId");
			writeJson(wxuserService.addNewUnit(weixinRegister,personId));
		} catch (Exception e) {
			logger.error("单元绑定异常!",e);
			writeJson(new AccessResult(false,"提交数据异常"));
		}
	}
	
	/**
	 * 微信端跳转到：开门记录页面
	 */
	@Action("openDoorRecord")
	public String openDoorRecord() {
		try {
			String openId = (String) getSession().getAttribute("wxopenid");
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxnoPermission";
			}
			WeixinUser wxuser = wxuserService.getWXUserByOpenId(openId);
			if(wxuser==null){
				wxuser = new WeixinUser();
				wxuser.setId(UUIDUtil.generate());
				wxuser.setOpenid(openId);
				wxuser.setAddtime(new Date());
				wxuser.setInnerPerson(0);
				wxuser.setCity("");
				wxuser.setNickname("");
				wxuser.setSex(0);
				wxuserService.save(wxuser);
			}
			if(wxuser.getInnerPerson()==0){//非内部人员,需跳转询问申请
				return "goRegister";
			}
			if(wxuser.getInnerPerson()==2){//审核中,暂时无法操作
				return "waitApproval";
			}
			if(wxuser.getInnerPerson()==1){//内部人员,将人员id存入session
				Person person = personService.getPersonByOpenid(openId);
				if(person!=null){
					getSession().setAttribute("personId", person.getId());
					String openDateStr = null;
					String openTypeStr = null;
					List<OpenDoorRecord> list = personService.getOpenDoorRecordByPersonId(person.getId(),1);
					List<OpenDoorRecord> listStr = new ArrayList<OpenDoorRecord>();
					if(list!=null&&list.size()>0){
						for (OpenDoorRecord openDoorRecord : list) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							openDateStr = df.format(openDoorRecord.getOpenDate());
							if(openDoorRecord.getOpenType() == 1){
								openTypeStr = "刷卡开门";
							}else if(openDoorRecord.getOpenType() == 2){
								openTypeStr = "扫码开门";
							}else{
								openTypeStr = "密码开门";
							}
							openDoorRecord.setOpenDateStr(openDateStr);
							openDoorRecord.setOpenTypeStr(openTypeStr);
							listStr.add(openDoorRecord);
						}
					}
					getRequest().setAttribute("listStr", JSON.toJSON(listStr));
				}else{
					wxuser.setInnerPerson(0);
					wxuserService.update(wxuser);
					return "goRegister";
				}
			}
		} catch (Exception e) {
			logger.error("微信跳转开门记录页面异常", e);
			return "wxException";
		}
		return SUCCESS;
	}
	
	/**
	 * 开门记录查看更多 ajax
	 * */
	@Action("openDoorRecordPage")
	public void openDoorRecordPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("success", false);
			String personId = (String) getSession().getAttribute("personId");
			if(StringUtils.isBlank(personId)||"null".equals(personId)){
				map.put("info", "请求超时，请重新登录!");
				writeJson(map);  //传入前台json格式
			}
			String openDateStr = null;
			String openTypeStr = null;
			List<OpenDoorRecord> list = personService.getOpenDoorRecordByPersonId(personId,weixinRegister.getPageNumber());
			List<OpenDoorRecord> listStr = new ArrayList<OpenDoorRecord>();
			if(list!=null&&list.size()>0){
				for (OpenDoorRecord openDoorRecord : list) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					openDateStr = df.format(openDoorRecord.getOpenDate());
					if(openDoorRecord.getOpenType() == 1){
						openTypeStr = "刷卡开门";
					}else if(openDoorRecord.getOpenType() == 2){
						openTypeStr = "扫码开门";
					}else{
						openTypeStr = "密码开门";
					}
					openDoorRecord.setOpenDateStr(openDateStr);
					openDoorRecord.setOpenTypeStr(openTypeStr);
					listStr.add(openDoorRecord);
				}
				map.put("success", true);
				map.put("listStr", listStr);
				writeJson(map);
			}else{
				map.put("info", "没有更多了!");
				writeJson(map);
			}
		} catch (Exception e) {
			logger.error("加载开门记录异常", e);
			map.put("info", "加载异常，请重新操作");
			writeJson(map);
		}
	}
	
	/**
	 * 微信端跳转到：申请页面
	 */
	@Action("userRegister")
	public String userRegister() {
		try {
			//带入地区，用户类型选项
			getRequest().setAttribute("personTypeList", JSONObject.toJSONString(wxuserService.getPersonType()));
			getRequest().setAttribute("regionList", JSONObject.toJSONString(wxuserService.getRegionList()));
		} catch (Exception e) {
			logger.error("微信跳转申请页面异常", e);
			return "wxException";
		}
		return SUCCESS;
	}
	
	/**
	 * 表单提交前效验手机号，身份证的唯一性
	 * 同时效验短信验证码是否正确
	 */
	@Action("checkPhoneAndIDCard")
	public void checkPhoneAndIDCard() {
		try {
			writeJson(wxuserService.checkPhoneAndIDCard(weixinRegister));
		} catch (Exception e) {
			logger.error("效验手机和身份证异常",e);
			writeJson(new AccessResult(false,"效验手机和身份证异常!"));
		}
	}
	
	/**
	 * 申请表单提交
	 */
	@Action("checkRegister")
	public String checkRegister() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			String filePath = UploadFileUtils.getDirectory("userRegister")+"/"+formatter.format(new Date());
			String uuid = UUIDUtil.generate();
			String fileName = uuid+".jpg";
			filePath = filePath+"/"+fileName;
			File target = new File(filePath);
			if(target.exists()){
				target.delete();
			}
			FileUtils.copyFile(cardFile, target);  //target创建后为空的文件
			return wxuserService.checkRegister(weixinRegister,target,filePath,uuid);
		} catch (Exception e) {
			logger.error("微信用户注册异常",e);
			//writeJson(new AccessResult(false,"注册异常!"));
			return "registerError";
		}
	}
	
	/**
	 * 跳转到运维页面
	 * */
	@Action("yunweiPage")
	public String yunweiPage(){
		try {
			String openId = (String) getSession().getAttribute("wxopenid");
//			System.out.println(openId);
//			openId="o4cvgwDVwYlmzrPWqOM2-uKg5B4U";
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				return "wxnoPermission";
			}
			User user = new User();
			user = personService.getYunWeiByOpenId(openId);
//			System.out.println(user);
			if(user == null){
				return "yunweilogin";
			}else{
				getRequest().setAttribute("regionList", JSONObject.toJSONString(wxuserService.getRegionList()));
				return SUCCESS;
			}
		} catch (Exception e) {
			logger.error("微信跳转运维页面异常", e);
			return "wxException";
		}
	}
	
	/**
	 * 验证运维用户名密码是否正确
	 */
	@Action("checkYunWeiAccount")
	public void checkYunWeiAccount(){
		try {
			String openId = (String) getSession().getAttribute("wxopenid");
			if(StringUtils.isBlank(openId)||"null".equals(openId)){
				writeJson(new AccessResult(false,"微信登录异常，请重新登录"));
			}else{
				writeJson(wxuserService.checkYunWeiAccount(weixinRegister, openId));
			}
		} catch (Exception e) {
			logger.error("校验用户名和密码异常",e);
			writeJson(new AccessResult(false,"校验用户名和密码异常"));
		}
	}
	/**
	 * 运维表单提交前校验设备号和Mac地址是否有效
	 * */
	@Action("checkDevice")
	public void checkDevice(){
		try{
			writeJson(wxuserService.checkDevice(weixinRegister));
		}catch (Exception e) {
			logger.error("校验异常!",e);
			writeJson(new AccessResult(false,"校验异常!!"));
		}
	}
	/**
	 * 运维表单提交
	 * */
	@Action("addDevice")
	public String addDevice(){
		try {
			return wxuserService.addDevice(weixinRegister);
		} catch (Exception e) {
			logger.error("设备绑定异常!",e);
			return "registerError";
		}
	}

	public File getCardFile() {
		return cardFile;
	}

	public void setCardFile(File cardFile) {
		this.cardFile = cardFile;
	}

	public String getApprovalToken() {
		return approvalToken;
	}

	public void setApprovalToken(String approvalToken) {
		this.approvalToken = approvalToken;
	}

	public String getApprovalFlag() {
		return approvalFlag;
	}

	public void setApprovalFlag(String approvalFlag) {
		this.approvalFlag = approvalFlag;
	}
	
}
