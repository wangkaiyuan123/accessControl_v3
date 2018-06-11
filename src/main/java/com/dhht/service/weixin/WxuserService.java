package com.dhht.service.weixin;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.MapUtil;
import com.dhht.common.TeaEncodeUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.common.UploadFileUtils;
import com.dhht.common.WebUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.doorguard.Device;
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.entity.record.UserApplyRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.OperatePicture;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.PersonType;
import com.dhht.entity.system.User;
import com.dhht.entity.template.Times;
import com.dhht.entity.unit.PersonUnit;
import com.dhht.entity.unit.Unit;
import com.dhht.entity.unit.UnitDevice;
import com.dhht.entity.visitors.VisitorOfflineFlag;
import com.dhht.entity.visitors.VisitorRecord;
import com.dhht.entity.weixin.DynamicRandom;
import com.dhht.entity.weixin.KeyWord;
import com.dhht.entity.weixin.OfflineRecordFlag;
import com.dhht.entity.weixin.OpenDoorFlag;
import com.dhht.entity.weixin.SMSCode;
import com.dhht.entity.weixin.WeixinDictionary;
import com.dhht.entity.weixin.WeixinRegister;
import com.dhht.entity.weixin.WeixinUser;
import com.dhht.mina.job.SessionBean;
import com.dhht.mina.server.SendDataUtil;
import com.weixin.pool.AccessToken;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil;
@Service
public class WxuserService {
	@Autowired
	private SessionBean sessionBean;
	@Resource
	private BaseDao<WeixinUser> wxuserDao;
	@Resource
	private BaseDao<OperatePicture> operatePictureDao;
	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private BaseDao<UserApplyRecord> userApplyRecordDao;
	@Resource
	private BaseDao<SMSCode> sMSCodeDao;
	@Resource
	private BaseDao<Region> regionDao;
	@Resource
	private BaseDao<Unit> unitDao;
	@Resource
	private BaseDao<PersonType> personTypeDao;
	@Resource
	private BaseDao<DynamicRandom> dynamicRandomDao;
	@Resource
	private BaseDao<PersonUnit> personUnitDao;
	@Resource
	private BaseDao<Device> deviceDao;
	@Resource
	private BaseDao<UnitDevice> unitDeviceDao;
	@Resource
	private BaseDao<OpenDoorRecord> openDoorRecordDao;
	@Resource
	private BaseDao<DoorCard> doorCardDao;
	@Resource
	private BaseDao<User> userDao;
	@Resource
	private BaseDao<WeixinDictionary> weixinDictionaryDao;
	@Resource
	private BaseDao<KeyWord> keyWordDao;
	@Resource
	private SendTemplateMessageService sendTemplateMessageService;
	@Resource
	private BaseDao<OpenDoorFlag> openDoorFlagDao;
	@Resource
	private PollDataService pollDataService;
	@Resource
	private VisitorRecordService visitorRecordService;
	@Resource
	private BaseDao<OfflineRecordFlag> offlineRecordDao;
	
	/**
	 * 根据opendId获取微信用户
	 * @param openId
	 * @return
	 */
	public WeixinUser getWXUserByOpenId(String openId){
		String hql = " from  WeixinUser where openid='"+openId+"'";
		return wxuserDao.get(hql);
	}
	
	/**
	 * 根据id获取微信用户对象
	 */
	public WeixinUser getById(String wxuserId){
		return wxuserDao.get(WeixinUser.class,wxuserId);
	}
	
	/**
	 * 保存微信用户对象
	 */
	public void save(WeixinUser wxuser){
		wxuserDao.save(wxuser);
	}
	
	/**
	 * 更新微信用户对象
	 */
	public void update(WeixinUser wxuser){
		wxuserDao.update(wxuser);
	}
	
	/**
	 * 删除微信用户对象
	 */
	public void deleteByOpenid(String openid){
		String sql ="delete from weixin_user where openid='"+openid+"'";
		wxuserDao.deleteBySql(sql);
	}
	
	/**
	 * 处理扫码开门事件
	 */
	public String processScancode(String ScanResult, String eventKey, final String openid){  //map.get("FromUserName") 就是openId
		if("openDoor".equals(eventKey)){//如果是开门的扫码事件
			if(!StringUtils.isBlank(ScanResult)){//查询用户，验证参数，通过则发送开门指令
				WeixinUser wxuser = getWXUserByOpenId(openid);
				if(wxuser==null){//服务器关闭期间,此用户关注了，却没有被增加到数据库，因此这里再新增
					wxuser = new WeixinUser();
					wxuser.setId(UUIDUtil.generate());
					wxuser.setOpenid(openid);
					wxuser.setAddtime(new Date());
					wxuser.setInnerPerson(0);
					wxuser.setCity("");
					wxuser.setNickname("");
					wxuser.setSex(0);
					wxuserDao.save(wxuser);
				}
				//***********处理开门逻辑************
				//String random = "";
				String time = "";
				String deviceMac = "";
				//ScanResult = "http://weixin.qq.com/r/Yi8LE7nE9OSLrf1w93pw?random=322424MAC=D8B04CDEC6BB";
				//01.12改成mac+时间：http://mj.donghuahongtai.com/w/o?M=123456123456T=2018011215530005
				try {
					//random = ScanResult.substring(ScanResult.indexOf("random=")+7, ScanResult.indexOf("random=")+13);//动态密码
					deviceMac = ScanResult.substring(ScanResult.indexOf("M=")+2, ScanResult.indexOf("M=")+14);//门禁设备号
					time = ScanResult.substring(ScanResult.indexOf("T=")+2, ScanResult.indexOf("T=")+18);//动态时间
					System.out.println(time);System.out.println(deviceMac);
				} catch (Exception e) {
					return "二维码错误,请重新扫码";
				}
				final String mac = deviceMac;
				//**********************************
				//验证人员权限
				Person person = personDao.get(" from Person where openid='"+openid+"'");
				if(person==null){
					//String back = getVisitor("无权限点击申请",deviceMac,time);
					return getVisitor("无权限点击申请",deviceMac,time);
				}
				if(person.getState()==2){
					return "无权限，请等待审核结果";
				}
				final Device device = deviceDao.get(" from Device where deviceMac='"+deviceMac+"'");
				if(device==null){
					return "效验失败,未找到门禁设备";
				}
				if(StringUtils.isBlank(device.getUnitId())){
					return "效验失败,门禁设备尚未关联单元";
				}
				PersonUnit pu = personUnitDao.get(" from PersonUnit where unitId='"+device.getUnitId()+"' and personId='"+person.getId()+"'");
				if(pu==null){
					String back = getVisitor("我是访客",deviceMac,time);
					String myCard = "<a href=\"http://mj.donghuahongtai.com/weixinpage/myCard\">我的门卡</a>";
					return "无权限，您尚未申请关联此单元,请到"+myCard+"中进行绑定单元或点击"+back+"进行预约";
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
				insertOpenDoorFlag(uuid);  //存入开门判断对象
				/*0208修改：**************************/
				try {
					IoSession session = sessionBean.getSession(deviceMac);
					if(session!=null&&!session.isClosing()){
						SendDataUtil.sendData(session,SendDataUtil.openDoor(secret),"02");  //发送开门指令
						//putDataByMac(deviceMac, openid);
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
						updateDynamicRandom(secret, deviceMac,person.getId());
						OfflineRecordFlag flag =  new OfflineRecordFlag();
						flag.setId(UUIDUtil.generate());
						flag.setMac(deviceMac);
						flag.setPersonId(person.getId());
						flag.setSecret(secret);
						saveOfflineRecord(flag);
						flag.setTime(time);
						return "网络异常，请使用该密码开门:"+secret;
					}
				} catch (Exception e) {
					updateDynamicRandom(secret, deviceMac,person.getId());
					OfflineRecordFlag flag =  new OfflineRecordFlag();
					flag.setId(UUIDUtil.generate());
					flag.setMac(deviceMac);
					flag.setPersonId(person.getId());
					flag.setSecret(secret);
					flag.setTime(time);
					saveOfflineRecord(flag);
					return "服务忙，请使用该密码开门:"+secret;
				}
				System.out.println("***************发送开门指令******************");
				
				//开启线程任务
				final Timer timer2 = new Timer();
				TimerTask task2 = new TimerTask() {
					@Override
					public void run() {
						try {
							/*String flag = getDataByMac(mac);
							if(!StringUtils.isBlank(flag)&&openid.equals(flag)){
								sendWeixinMessageForOpenDoor(2, openid,"",secret);
							}*/
							boolean b = pollDataService.processOpenDoorFlag(uuid, openid, secret);  //false
							if(b){
								sendWeixinMessageForOpenDoor(1, openid,device.getDeviceAddress(),secret);
							}else{
								sendWeixinMessageForOpenDoor(2, openid,"",secret);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						timer2.cancel();
					}
				};
				timer2.schedule(task2, 2000);  //2秒
				
				return "效验通过，请稍等...";
				/*DynamicRandom dynamicRandom = dynamicRandomDao.get(" from DynamicRandom where deviceMAC='"+deviceMac+"'");
				if(dynamicRandom==null){
					return "设备号错误,请重新扫码";
				}
				if(random.equals(dynamicRandom.getRandom1())||random.equals(dynamicRandom.getRandom2())){
					if((new Date().getTime()-dynamicRandom.getLastTime())>20000){
						return "二维码已失效,请重新扫码";
					}
					//匹配动态密码正确，再看此用户有没有权限
					//1根据openid查询人员，再查出人员关联的设备，有则发送开门指令
					//2查看有没有预约记录，有则发送开门指令
					Person person = personDao.get(" from Person where openid='"+openid+"'");
					if(person==null){					
						return getApply("无权限点击申请");
					}
					if(person.getState()==2){
						return "无权限，请等待审核结果";
					}
					Device device = deviceDao.get(" from Device where deviceMac='"+deviceMac+"'");
					if(device==null){
						return "效验失败,未找到门禁设备";
					}
					if(StringUtils.isBlank(device.getUnitId())){
						return "效验失败,门禁设备尚未关联单元";
					}
					PersonUnit pu = personUnitDao.get(" from PersonUnit where unitId='"+device.getUnitId()+"' and personId='"+person.getId()+"'");
					if(pu==null){
						return "无权限，您尚未申请关联此单元\n请到\"我的门卡\"中进行绑定单元操作";
					}
					//*****************发送开门指令*******************
					try {
						IoSession session = sessionBean.getSession(deviceMac);
						if(session!=null&&!session.isClosing()){
							SendDataUtil.sendData(session,SendDataUtil.openDoor(),"02");
							session.removeAttribute("lastopendoorPerson");
							session.removeAttribute("lastopendoorTime");
							session.setAttribute("lastopendoorPerson", person.getId());
							session.setAttribute("lastopendoorTime", new Date().getTime());
						}else{
							return "网络异常，请稍后重试";
						}
					} catch (Exception e) {
						e.printStackTrace();
						return "服务忙，请稍后重试";
					}
					System.out.println("***************发送开门指令******************");
					return "效验通过，等待开门";
				}else{
					return "二维码已失效,请重新扫码";
				}*/
			}else{
				return "参数为空,请重新扫码";
			}
		}
		return null;
	}
	
	/**
	 * 处理关注事件
	 */
	public String processSubscribe(String openid){
		String message = MessageUtil.menuText();
		WeixinUser wxuser = getWXUserByOpenId(openid);
		if (wxuser == null) {
			wxuser = new WeixinUser();
			wxuser.setId(UUIDUtil.generate());
			wxuser.setOpenid(openid);
			wxuser.setInnerPerson(0);
			wxuser.setCity("");
			wxuser.setNickname("");
			wxuser.setSex(0);
			wxuser.setAddtime(new Date());
			wxuserDao.save(wxuser);
		}else{//服务器关闭期间,此用户取消关注了，却没有从数据库中删除，因此这里更新
			wxuser.setAddtime(new Date());
			wxuserDao.update(wxuser);
		}
		return message;
	}
	
	/**
	 * 获取用户类型list
	 */
	public List<PersonType> getPersonType(){
		List<PersonType> list = personTypeDao.find(" from PersonType");
		if(list!=null&&list.size()>0){
			for (PersonType personType : list) {
				personType.setTitle(personType.getTypeName());
				personType.setValue(personType.getId());
			}
		}
		return list;
	}
	
	/**
	 * 获取地区结构： 省 市 区
	 */
	public List<Region> getRegionList(){
		List<Region> list = new ArrayList<Region>();
		String hql1 = " from Region where level=1";
		List<Region> level1 = regionDao.find(hql1);
		if(true){
			Region region3 = new Region();
			region3.setName("--");
			region3.setCode(UUIDUtil.generate());
			List<Region> sub2 = new ArrayList<Region>();
			sub2.add(region3);
			Region region2 = new Region();
			region2.setName("--");
			region2.setCode(UUIDUtil.generate());
			region2.setSub(sub2);
			List<Region> sub1 = new ArrayList<Region>();
			sub1.add(region2);
			Region region1 = new Region();
			region1.setName("--");
			region1.setCode(UUIDUtil.generate());
			region1.setSub(sub1);
			list.add(region1);
		}
		if(level1!=null&&level1.size()>0){
			for (Region region : level1) {
				region.setName(region.getRegionName());
				region.setCode(region.getId());
				String hql2 = " from Region where level=2 and parentId='"+region.getId()+"'";
				List<Region> level2 = regionDao.find(hql2);
				if(level2!=null&&level2.size()>0){
					region.setSub(level2);
					for (Region region2 : level2) {
						region2.setName(region2.getRegionName());
						region2.setCode(region2.getId());
						String hql3 = " from Region where level=3 and parentId='"+region2.getId()+"'";
						List<Region> level3 = regionDao.find(hql3);
						if(level3!=null&&level3.size()>0){
							for (Region region3 : level3) {
								region3.setName(region3.getRegionName());
								region3.setCode(region3.getId());
							}
							region2.setSub(level3);
						}else{
							Region region3 = new Region();
							region3.setName("--");
							region3.setCode(UUIDUtil.generate());
							List<Region> sub2 = new ArrayList<Region>();
							sub2.add(region3);
							region2.setSub(sub2);
						}
					}
				}else{
					Region region3 = new Region();
					region3.setName("--");
					region3.setCode(UUIDUtil.generate());
					List<Region> sub2 = new ArrayList<Region>();
					sub2.add(region3);
					Region region2 = new Region();
					region2.setName("--");
					region2.setCode(UUIDUtil.generate());
					region2.setSub(sub2);
					List<Region> sub1 = new ArrayList<Region>();
					sub1.add(region2);
					region.setSub(sub1);
				}
			}
			list.addAll(level1);
		}
		return list;
	}
	
	/**
	 * 根据所在地区，获取该区下面的：街道
	 */
	public Map<String,Object> getStreetByLocation(String regionIds){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", false);
		if(StringUtils.isBlank(regionIds)){
			return map;
		}
		String ids[] = regionIds.split(",");
		if(ids.length<=0){
			return map;
		}
		String id = ids[ids.length-1];
		Region region = regionDao.get(Region.class, id);
		if(region==null){
			return map;
		}
		String hql4 = " from Region where level=4 and parentId='"+region.getId()+"'";
		List<Region> level4 = regionDao.find(hql4);
		if(level4!=null&&level4.size()>0){
			map.put("success", true);
			Region regionfirst = level4.get(0);
			String hql5 = " from Region where level=5 and parentId='"+regionfirst.getId()+"'";
			List<Region> level5 = regionDao.find(hql5);
			if(level5!=null&&level5.size()>0){
				Region region5first = level5.get(0);
				//获取单元列表
				String hql6 = " from Unit where communityId='"+region5first.getId()+"' and isGurad=0";
				List<Unit> level6 = unitDao.find(hql6);
				List<Region> sub = new ArrayList<Region>();
				if(level6!=null&&level6.size()>0){
					for (Unit unit : level6) {
						Region region6 = new Region();
						region6.setId(unit.getId());
						region6.setName(unit.getUnitName());
						sub.add(region6);
					}
					region5first.setSub(sub);
				}
				regionfirst.setSub(level5);
			}
			map.put("regionList", level4);
		}else{
			return map;
		}
		return map;
	}
	
	/**
	 * 根据街道获取下面的 小区
	 */
	public Map<String,Object> getVillageByStreet(String regionId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", false);
		if(StringUtils.isBlank(regionId)){
			return map;
		}
		Region region = regionDao.get(Region.class, regionId);
		if(region==null){
			return map;
		}
		String hql5 = " from Region where level=5 and parentId='"+region.getId()+"'";
		List<Region> level5 = regionDao.find(hql5);
		if(level5!=null&&level5.size()>0){
			map.put("success", true);
			Region regionfirst = level5.get(0);
			//获取单元列表
			String hql6 = " from Unit where communityId='"+regionfirst.getId()+"' and isGurad=0";
			List<Unit> level6 = unitDao.find(hql6);
			List<Region> sub = new ArrayList<Region>();
			if(level6!=null&&level6.size()>0){
				for (Unit unit : level6) {
					Region region6 = new Region();
					region6.setId(unit.getId());
					region6.setName(unit.getUnitName());
					sub.add(region6);
				}
				regionfirst.setSub(sub);
			}
			map.put("regionList", level5);
		}else{
			return map;
		}
		return map;
	}
	
	/**
	 * 根据小区获取下面的  单元
	 */
	public Map<String,Object> getUnitByVillage(String regionId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", false);
		if(StringUtils.isBlank(regionId)){
			return map;
		}
		Region region = regionDao.get(Region.class, regionId);
		if(region==null){
			return map;
		}
		//获取单元列表
		String hql6 = " from Unit where communityId='"+region.getId()+"' and isGurad=0";
		List<Unit> level6 = unitDao.find(hql6);
		if(level6!=null&&level6.size()>0){
			map.put("success", true);
			map.put("unitList", level6);
		}else{
			return map;
		}
		return map;
	}
	
	/**
	 * 根据“区”，获取该区下面的：街道
	 * 根据街道，获取下面的小区
	 */
	public Map<String,Object> getRegionByLocation1(String regionIds){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", false);
		if(StringUtils.isBlank(regionIds)){
			return map;
		}
		String ids[] = regionIds.split(",");
		if(ids.length<=0){
			return map;
		}
		String id = ids[ids.length-1];
		Region region = regionDao.get(Region.class, id);
		if(region==null){
			return map;
		}
		String hql4 = " from Region where level=4 and parentId='"+region.getId()+"'";
		List<Region> level4 = regionDao.find(hql4);
		if(level4!=null&&level4.size()>0){
			map.put("success", true);
			List<Region> list = new ArrayList<Region>();
			if(true){
				Region region3 = new Region();
				region3.setName("--");
				region3.setCode(UUIDUtil.generate());
				List<Region> sub2 = new ArrayList<Region>();
				sub2.add(region3);
				Region region2 = new Region();
				region2.setName("--");
				region2.setCode(UUIDUtil.generate());
				region2.setSub(sub2);
				List<Region> sub1 = new ArrayList<Region>();
				sub1.add(region2);
				Region region1 = new Region();
				region1.setName("--");
				region1.setCode(UUIDUtil.generate());
				region1.setSub(sub1);
				list.add(region1);
			}
			for (Region region4 : level4) {
				region4.setName(region4.getRegionName());
				region4.setCode(region4.getId());
				String hql5 = " from Region where level=5 and parentId='"+region4.getId()+"'";
				List<Region> level5 = regionDao.find(hql5);
				if(level5!=null&&level5.size()>0){
					for (Region region5 : level5) {
						region5.setName(region5.getRegionName());
						region5.setCode(region5.getId());
						//获取单元列表
						String hql6 = " from Unit where communityId='"+region5.getId()+"' and isGurad=0";
						List<Unit> level6 = unitDao.find(hql6);
						if(level6!=null&&level6.size()>0){
							List<Region> rlevel6 = new ArrayList<Region>();
							for (Unit unit : level6) {
								Region region6 = new Region();
								region6.setCode(unit.getId());
								region6.setName(unit.getUnitName());
								rlevel6.add(region6);
							}
							region5.setSub(rlevel6);
						}else{
							Region region6 = new Region();
							region6.setName("--");
							region6.setCode(UUIDUtil.generate());
							List<Region> sub5 = new ArrayList<Region>();
							sub5.add(region6);
							region5.setSub(sub5);
						}
						region4.setSub(level5);
					}
				}else{
					Region region6 = new Region();
					region6.setName("--");
					region6.setCode(UUIDUtil.generate());
					List<Region> sub5 = new ArrayList<Region>();
					sub5.add(region6);
					Region region5 = new Region();
					region5.setName("--");
					region5.setCode(UUIDUtil.generate());
					region5.setSub(sub5);
					List<Region> sub4 = new ArrayList<Region>();
					sub4.add(region5);
					region4.setSub(sub4);
				}
			}
			list.addAll(level4);
			map.put("addressdata", list);
		}else{
			return map;
		}
		return map;
	}
	
	/**
	 * 表单提交前效验手机号，身份证的唯一性
	 * 同时效验短信验证码是否正确
	 */
	public AccessResult checkPhoneAndIDCard(WeixinRegister weixinRegister){
		Long nowtime = new Date().getTime();
		String hql = "select count(id) from Person where telephone='"+weixinRegister.getPersonPhone()+"' or identifyId='"+weixinRegister.getPersonNumber()+"'";
    	Long count = (Long)personDao.count(hql);
    	if(count!=null&&count>0){
    		return new AccessResult(false, "手机号或身份证已被注册");
    	}
    	SMSCode code = sMSCodeDao.get(" from SMSCode where phone='"+weixinRegister.getPersonPhone()+"'");
    	if(code==null){
    		return new AccessResult(false, "请点击发送验证码");
    	}
    	if(!code.getSmscode().equals(weixinRegister.getCodeNumber())){
    		return new AccessResult(false, "验证码错误");
    	}
    	if(nowtime-code.getLastTime()>300000){
    		return new AccessResult(false, "验证码超时，请重新发送");
    	}
		return new AccessResult(true,"效验通过");
	}
	
	/**
	 * 处理申请表单
	 */
	public String checkRegister(WeixinRegister weixinRegister, File file, String filePath, String uuid){
		String openid = String.valueOf(WebUtil.getSession().getAttribute("wxopenid"));
		if(StringUtils.isBlank(openid)||"null".equals(openid)){
			return "registerFail";
		}
		WeixinUser wxuser = getWXUserByOpenId(openid);
		if(wxuser==null){//服务器关闭期间,此用户关注了，却没有被增加到数据库，因此这里再新增
			wxuser = new WeixinUser();
			wxuser.setId(UUIDUtil.generate());
			wxuser.setOpenid(openid);
			wxuser.setAddtime(new Date());
			wxuser.setInnerPerson(2);
			wxuser.setCity("");
			wxuser.setNickname("");
			wxuser.setSex(0);
			wxuserDao.save(wxuser);
		}else{
			wxuser.setInnerPerson(2);
			wxuserDao.update(wxuser);
		}
		//保存身份证图片
		OperatePicture pic = new OperatePicture();
		pic.setId(uuid);
		pic.setCreateTime(new Date());
		pic.setPicturePath(filePath);
		operatePictureDao.save(pic);
		//保存人员(未审核)
		Person person = new Person();
		person.setId(UUIDUtil.generate());
		person.setOpenid(openid);
		person.setName(weixinRegister.getPersonName());
		person.setTelephone(weixinRegister.getPersonPhone());
		person.setAge(0);//根据身份证计算年龄↓ ↓ ↓
		person.setSex(0);//根据身份证计算性别↓ ↓ ↓
		if(!StringUtils.isBlank(weixinRegister.getPersonNumber())){
			int birthday = DaoUtil.parseInt(weixinRegister.getPersonNumber().substring(6, 10), 0);
			if(birthday!=0){
				Calendar now = Calendar.getInstance();
				int age = now.get(Calendar.YEAR)-birthday;
				if(age>=0&&age<=150){
					person.setAge(age);
				}
			}
			String sexStr = weixinRegister.getPersonNumber().substring(weixinRegister.getPersonNumber().length()-2, weixinRegister.getPersonNumber().length()-1);
			int sex = DaoUtil.parseInt(sexStr,0);
			if(sex!=0){
				if(sex%2==0){
					person.setSex(2);
				}else{
					person.setSex(1);
				}
			}
		}
		PersonType type = personTypeDao.get(" from PersonType where typeName='"+weixinRegister.getPersonType()+"'");
		person.setPersonTypeId(type.getId());
		person.setState(2);
		person.setCardId(null);
		person.setIdentifyId(weixinRegister.getPersonNumber());
		person.setIdentifyPicId(uuid);
		personDao.save(person);
		//保存人员-单元关联表
//		PersonUnit pu = new PersonUnit();
//		pu.setId(UUIDUtil.generate());
//		pu.setPersonId(person.getId());
//		pu.setUnitId(weixinRegister.getPersonAddress());
//		try {
//			personUnitDao.save(pu);
//		} catch (Exception e) {}
		//保存申请记录，等待审批
		UserApplyRecord applyRecord = new UserApplyRecord();
		applyRecord.setId(uuid);
		applyRecord.setApplyDate(pic.getCreateTime());
		applyRecord.setState(0);
		applyRecord.setPersonId(person.getId());
		applyRecord.setImgUrl(filePath);
		//新增
		applyRecord.setCheckPerson("");//审核人
		applyRecord.setName(weixinRegister.getPersonName());//申请记录用户名
		applyRecord.setIdentifyId(weixinRegister.getPersonNumber());//申请记录身份证
		applyRecord.setTelephone(weixinRegister.getPersonPhone());//申请记录电话
		applyRecord.setOpenid(openid);//openid
		applyRecord.setCheckDate(null);//审核时间
		
		applyRecord.setUnitId(weixinRegister.getPersonAddress());
		userApplyRecordDao.save(applyRecord);
		return "registerSuccess";
	}
	
	/**
	 * 校验用户名与密码是否正确
	 */
	public AccessResult checkYunWeiAccount(WeixinRegister weixinRegister,String openId){
		String hql = " from User where userName = '" + weixinRegister.getUserName() + "' and password = '" + weixinRegister.getPassword() + "'";
		User u = new User();
		u = userDao.get(hql);
		if(u == null){
			return new AccessResult(false,"用户名或者密码错误，请重新输入");
		}else{
			u.setOpenid(openId);
			try {
				userDao.update(u);
			} catch (Exception e) {
				return new AccessResult(false,"该账号已绑定其他人员");
			}
			return new AccessResult(true,"校验通过");
		}
	}
	
	/**
	 * 设备表单提交前效验设备号、Mac地址是否存在或者是否已被使用
	 */
	public AccessResult checkDevice(WeixinRegister weixinRegister){
		String hql = " from Device where deviceMac='"+weixinRegister.getDeviceMac()+"' and isdelete=0";
    	Device device = deviceDao.get(hql);
    	if(device==null){
    		return new AccessResult(true,"校验通过!");
//    		return new AccessResult(false,"该设备在系统中未录入!");
    	}
		if (device.getState()==1) {
			return new AccessResult(false, "绑定出错，该设备已接入");
		} else {
			return new AccessResult(true, "校验通过!");
		}
    	
	}
	
	//处理设备申请表单
	public String addDevice(WeixinRegister weixinRegister){
		String openid = String.valueOf(WebUtil.getSession().getAttribute("wxopenid"));
		if(StringUtils.isBlank(openid)||"null".equals(openid)){
			return "registerFail";
		}
		String hql =" from Device where deviceMac='"+weixinRegister.getDeviceMac()+"'";
		Device device = deviceDao.get(hql);
		if(device==null){
			//新增
			device = new Device();
			device.setIsdelete(0);
			device.setId(UUIDUtil.generate());
			device.setDeviceMac(weixinRegister.getDeviceMac().toUpperCase());
			device.setPassword("123456");
			device.setIsReadCard(1);
			device.setAuthotity(0);
			User adduser = userDao.get(" from User where openid='"+openid+"'");
			if(adduser!=null){
				device.setAddPerson(adduser.getId());
			}else{
				device.setAddPerson("");
			}
			device.setUnitId(weixinRegister.getRealUnitId());
			device.setState(1);
			Unit unit = unitDao.get(Unit.class, weixinRegister.getRealUnitId());
			Region region5 = regionDao.get(Region.class, unit.getCommunityId());
			Region region4 = regionDao.get(Region.class, region5.getParentId());
			Region region3 = regionDao.get(Region.class, region4.getParentId());
			Region region2 = regionDao.get(Region.class, region3.getParentId());
			Region region1 = regionDao.get(Region.class, region2.getParentId());
			device.setDeviceAddress(region1.getRegionName()+region2.getRegionName()+region3.getRegionName()+region4.getRegionName()+region5.getRegionName()+unit.getUnitName());
			deviceDao.save(device);
		}
		device.setUnitId(weixinRegister.getRealUnitId());
		device.setState(1);
		device.setIsdelete(0);
		deviceDao.update(device);
		return "yunweiSuccess";
		
	}
	
	//处理单元申请表单
	public AccessResult addNewUnit(WeixinRegister weixinRegister,String personId){
		
//		String openid = String.valueOf(WebUtil.getSession().getAttribute("wxopenid"));
//		if(StringUtils.isBlank(openid)||"null".equals(openid)){
//			return new AccessResult(false,"表单提交超时");
//		}
        try {
            boolean isadd = false;
            String hqlStr = " from Person where id = '" + personId + "'";
            Person personstr = personDao.get(hqlStr);
            String hqlpersonUnit = " from PersonUnit where personId = '" + personId + "' and unitId = '"
                    + weixinRegister.getRealUnitId() + "'";
            PersonUnit personUnit = personUnitDao.get(hqlpersonUnit);
            String hql = " from UserApplyRecord where personId = '" + personId + "' and unitId = '"
                    + weixinRegister.getRealUnitId() + "' order by applyDate desc";
            List<UserApplyRecord> ua = userApplyRecordDao.find(hql);
            if (personUnit != null) {
                isadd = false;
            } else {
                isadd = true;
            }

            if (isadd == true) {
                if(ua != null && ua.size() > 0 ){
                    UserApplyRecord userApplyRecord1 = ua.get(0);//isadd为真表示:没有在PersonUnit里面查出单元，可以添加
                    if (userApplyRecord1.getState() == 0) {
                        return new AccessResult(false, "该单元正在审核中，请等待审核通过!");
                    }
                }
                UserApplyRecord userApplyRecord = new UserApplyRecord();
                userApplyRecord.setId(UUIDUtil.generate());
                userApplyRecord.setPersonId(personId);
                userApplyRecord.setUnitId(weixinRegister.getRealUnitId());
                userApplyRecord.setState(0);
                userApplyRecord.setApplyDate(new Date());

                userApplyRecord.setCheckPerson("");//审核人
                userApplyRecord.setName(personstr.getName());//申请记录用户名
                userApplyRecord.setIdentifyId(personstr.getIdentifyId());//申请记录身份证
                userApplyRecord.setTelephone(personstr.getTelephone());//申请记录电话
                userApplyRecord.setOpenid(personstr.getOpenid());//openid
                userApplyRecord.setCheckDate(null);//审核时间
                userApplyRecord.setImgUrl("");
                if(StringUtils.isNotBlank(personstr.getIdentifyPicId())){
                	OperatePicture pic = operatePictureDao.get(OperatePicture.class, personstr.getIdentifyPicId());
                	if(pic!=null){
                		userApplyRecord.setImgUrl(pic.getPicturePath());
                	}
                }
                userApplyRecordDao.save(userApplyRecord);
                return new AccessResult(true, "提交成功,请等待审核!");

            } else {
                return new AccessResult(false, "该单元已经审核通过并且已经被您绑定!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new AccessResult(false, "绑定单元出错!");
        }

	}	
		
	/**
	 * 更新随机数
	 * @param random
	 * @param machineMac
	 */
	public void updateDynamicRandom(String secret, String machineMac,String personId){
		DynamicRandom dr = dynamicRandomDao.get(" from DynamicRandom where deviceMAC='"+machineMac+"'");
		if(dr!=null){
			dr.setRandom1(secret);
			dr.setRandom2(personId);
			dr.setLastTime(new Date().getTime());
			dynamicRandomDao.update(dr);
		}else{
			dr = new DynamicRandom();
			dr.setId(UUIDUtil.generate());
			dr.setDeviceMAC(machineMac);
			dr.setRandom1(secret);
			dr.setRandom2(personId);
			dr.setLastTime(new Date().getTime());
			dynamicRandomDao.save(dr);
		}
	}
	
	public void updateDynamicRandom1(String secret, String machineMac,String personId){
		DynamicRandom dr = dynamicRandomDao.get(" from DynamicRandom where deviceMAC='"+machineMac+"'");
		if(dr!=null){
			dr.setRandom1(secret);
			dr.setRandom2(personId);
			dr.setLastTime(new Date().getTime());
			dynamicRandomDao.update(dr);
		}else{
			dr = new DynamicRandom();
			dr.setId(UUIDUtil.generate());
			dr.setDeviceMAC(machineMac);
			dr.setRandom1(secret);
			dr.setRandom2(personId);
			dr.setLastTime(new Date().getTime());
			dynamicRandomDao.save(dr);
		}
	}
	
	
	/**
	 * 终端上传刷卡开门记录
	 */
	public void uploadOpenDoorRecord(String cardNo, String mac, Date date){
		System.out.println("卡号："+cardNo);
		OpenDoorRecord record = new OpenDoorRecord();
		record.setId(UUIDUtil.generate());
		String hql = " from Device where deviceMac='"+mac+"'";
		Device device = deviceDao.get(hql);
		if(device==null){
			System.out.println("终端上传开门记录时根据mac未找到设备***********************");
			return;
		}
		record.setDeviceMac(mac);
		DoorCard card = doorCardDao.get(" from DoorCard where cardNo='"+cardNo+"'");
		if(card==null){
			record.setPersonId("");       //未找到卡
			record.setOpenDoorPerson("--");
		}else{
			record.setPersonId(card.getUserId());
			Person person = personDao.get(Person.class, card.getUserId());
			System.out.println("姓名:"+person.getName());
			record.setOpenDoorPerson("--");
			if(person!=null){
				record.setOpenDoorPerson(person.getName());
			}
			card.setRecentTime(date);
			doorCardDao.update(card);
		}
		record.setAddress(device.getDeviceAddress());
		record.setOpenDate(date);
		record.setOpenType(1);
		openDoorRecordDao.save(record);
	}
	
	/**
	 * 终端上传--密码--开门记录
	 */
	public void uploadSecretOpenDoorRecord(String secret, String mac, Date date){
		String hqlStr = DaoUtil.getFindPrefix(OfflineRecordFlag.class)+" where mac = '"+mac+"' and secret = '"+secret+"'";
		OfflineRecordFlag offlineRecordFlag =   offlineRecordDao.get(hqlStr);  //获取离线标记数据
		//非访客离线记录
		if(offlineRecordFlag!=null){
			OpenDoorRecord record = new OpenDoorRecord();    //开门记录
			record.setId(UUIDUtil.generate());
			String hql = " from Device where deviceMac='"+mac+"'";
			Device device = deviceDao.get(hql);
			if(device==null){
				System.out.println("终端上传开门记录时根据mac未找到设备***********************");
				return;
			}
			record.setDeviceMac(mac);
			record.setPersonId(offlineRecordFlag.getPersonId());
			Person person = personDao.get(Person.class, offlineRecordFlag.getPersonId());
			record.setOpenDoorPerson("--");
			if(person!=null){
				record.setOpenDoorPerson(person.getName());
			}
			record.setAddress(device.getDeviceAddress());
			record.setOpenDate(date);
			record.setOpenType(3);
			openDoorRecordDao.save(record);
		}
		
		DynamicRandom dynamicRandom = getDynamicRandomByMAC(mac);  
		if(dynamicRandom!=null){
//			OpenDoorRecord record = new OpenDoorRecord();    //开门记录
//			record.setId(UUIDUtil.generate());
//			String hql = " from Device where deviceMac='"+mac+"'";
//			Device device = deviceDao.get(hql);
//			if(device==null){
//				System.out.println("终端上传开门记录时根据mac未找到设备***********************");
//				return;
//			}
//			record.setDeviceMac(mac);
//			record.setPersonId(dynamicRandom.getRandom2());
//			Person person = personDao.get(Person.class, dynamicRandom.getRandom2());
//			record.setOpenDoorPerson("--");
//			if(person!=null){
//				record.setOpenDoorPerson(person.getName());
//			}
//			record.setAddress(device.getDeviceAddress());
//			record.setOpenDate(date);
//			record.setOpenType(3);
//			openDoorRecordDao.save(record);
		}
		//离线密码访客开门记录上传
		VisitorOfflineFlag flag =  visitorRecordService.getListByMacAndSecret(mac,secret); //获取离线标记记录
		if(flag!=null){
			VisitorRecord visitorRecord =  visitorRecordService.getRecord(flag.getVisitorRecordId());
			if(visitorRecord!=null){
				visitorRecord.setFlag(1); //开门成功
				visitorRecordService.updateRecord(visitorRecord);
			}
		}
	}
	
	/**
	 * 获取用户列表
	 * @return list
	 */
	public List<WeixinUser> getWeixinUsers(int page,int count){
		String hql = " from WeixinUser order by addtime asc";
		return wxuserDao.find(hql, page, count);
	}
	/**
	 * 定时器补全用户信息——获取信息不全的用户列表
	 * @return list
	 */
	public List<WeixinUser> completionWeixinUsers(int page,int count){
		String hql = " from WeixinUser where nickname = ' '";		
		return wxuserDao.find(hql,page,count);
	}
	
	/**
	 * 定时器更新用户昵称——获取用户列表总数
	 * @return Long count
	 */
	public Long getAllCount(){
		return (Long)wxuserDao.count("select count(id) from WeixinUser");
	}
	
	/**
	 * 定时器补全用户信息——获取信息不全的用户列表总数
	 * @return Long count
	 */
	public Long completionAllCount(){
		return (Long)wxuserDao.count("select count(id) from WeixinUser where nickname = ' '");
	}
	
	/**
	 * 获取配置信息(参数可为APPID,APPSECRET,SIGNINFO,TOKEN)
	 */
	public String getWxConfig(String str){

		String hql =" from WeixinDictionary where weixinkey = '" + str + "'";

		WeixinDictionary appid = weixinDictionaryDao.get(hql);
		return appid.getValue();
	}
	
	/**
	 * 获取微信字典表对象
	 */
	public WeixinDictionary getWeixinDictionary(String str){
		String hql =" from WeixinDictionary where weixinkey = '" + str + "'";
		return weixinDictionaryDao.get(hql);
	}
	
	/**
	 * 更新微信字典表
	 */
	public void updateWeixinDictionary(WeixinDictionary w){
		weixinDictionaryDao.update(w);
	}
	
	/**
	 * 处理终端返回的开门成功
	 */
	public void processOpenSuccess(IoSession session, String mac){
		Long lastopenTime = (Long)session.getAttribute("lastopendoorTime");
		if(lastopenTime!=null&&lastopenTime>0){
			long now = new Date().getTime();
			//if(now-lastopenTime<=3000){   //保证即时性,本地测试可以先不考虑
			if(now-lastopenTime<=3000){   
				String uuid = String.valueOf(session.getAttribute("uuid"));
				if(!StringUtils.isBlank(uuid)&&!"null".equals(uuid)){
					updateOpenDoorFlag(uuid, 1); //收到开门成功反馈就设置为1 开门成功
				}
				String personId = String.valueOf(session.getAttribute("lastopendoorPerson"));
				if(!StringUtils.isBlank(personId)&&!"null".equals(personId)){
					//生成开门记录
					OpenDoorRecord record = new OpenDoorRecord();
					record.setId(UUIDUtil.generate());
					String hql = " from Device where deviceMac='"+mac+"'";
					Device device = deviceDao.get(hql);
					if(device==null){
						System.out.println("终端上传开门记录时根据mac未找到设备***********************");
						return;
					}
					record.setDeviceMac(mac);
					record.setAddress(device.getDeviceAddress());
					record.setOpenDate(new Date(lastopenTime));
					record.setOpenType(2);
					record.setPersonId(personId);
					Person person = personDao.get(Person.class, personId);
					record.setOpenDoorPerson("--");
					if(person!=null){
						record.setOpenDoorPerson(person.getName());
					}
					openDoorRecordDao.save(record);
				}
			}
		}
	  
	}

	/**
	 * 处理终端返回的校验失败
	 */
	public void processMatchFail(IoSession session, String mac){
		String func = String.valueOf(session.getAttribute("lastSend"));
		if("01".equals(func)){
			SendDataUtil.sendData(session,SendDataUtil.heartbeat(),"01");
		}else if("02".equals(func)){
			SendDataUtil.sendData(session,SendDataUtil.openDoor(), "02");
		}else if("07".equals(func)){
			SendDataUtil.sendData(session, SendDataUtil.replayGetRecord(),"07");
		}
	}

	/**
	 * 获取关键字回复
	 * @param content 用户输入的关键字
	 * @param openId  发送方的openId(FromUserName	发送方帐号（一个OpenID）)
	 * @return 关键字的回复内容
	 */
	public String getReplyContent(String content,String openId){
		StringBuffer sb = new StringBuffer();
		String hql = " from KeyWord where keyNo = '" + content +"'";
		KeyWord k = keyWordDao.get(hql);
		if(k != null){
			if(k.getReplyContent().indexOf("{account}")>=0){
				String getaccounthql = " from WeixinUser where openid = '" + openId + "'";
				WeixinUser weixinUser = wxuserDao.get(getaccounthql);
				if(weixinUser==null){
					weixinUser = new WeixinUser();
					weixinUser.setId(UUIDUtil.generate());
					weixinUser.setOpenid(openId);
					weixinUser.setAddtime(new Date());
					weixinUser.setInnerPerson(0);
					weixinUser.setCity("");
					weixinUser.setNickname("");
					weixinUser.setSex(0);
					wxuserDao.save(weixinUser);
					k.setReplyContent("系统忙，请重试");
				}else{
					String account = weixinUser.getAccount().toString();
					String n = k.getReplyContent();
					String x = n.replaceAll("\\{account\\}", account);
					k.setReplyContent(x);
				}
			}
			if(k.getReplyContent().indexOf("\\n")>=0){	
				String n = k.getReplyContent();
				String x = n.replace("\\n", "\n");
				k.setReplyContent(x);
			}
			if(k.getReplyContent().indexOf("\\\"")>=0){
				System.out.println("111");
				String n = k.getReplyContent();
				String x = n.replace("\\\"", "\"");
				k.setReplyContent(x);
			}
			sb.append(k.getReplyContent());
			
		}else{
			sb.append("系统未找到此关键字的回复内容");
		}
		return sb.toString();
	}

	
	/**
	 * 先AccessToken token = WeixinUtil.getAccessToken();
	 * if(token.getErrcode()>0){			
			System.out.println("获取access_token时errcode:"+token.getErrcode());					
			return;
		}
	 * 再更新数据库token方法
	 * @param token 使用时一般是token.getToken()
	 */
	public void updateToken(String token){
		WeixinDictionary dic = new WeixinDictionary();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dic = getWeixinDictionary("TOKEN");
		Date date = new Date();
		dic.setValue(token);
		dic.setTime(date);
		updateWeixinDictionary(dic);
		
		SendDataUtil.access_token = token;
		SendDataUtil.TIME = df.format(dic.getTime());
	}
	
	/**
	 * 比较两个时间的大小(用于判断token是否失效)
	 * @return hour
	 */
	public Times getDistanceTime(String time1,String time2){ 
		Times t = new Times();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date1;
		try {
			date1 = sdf.parse(time1);  
	        Date date2 = sdf.parse(time2);
	        long t1 = date1.getTime();  
	        long t2 = date2.getTime();
	        long diff ;  
	        if(t1<t2) {  
	            diff = t2 - t1;  
	        } else {  
	            diff = t1 - t2;  
	        }  
	        t.setDay(diff / (24 * 60 * 60 * 1000));  
	        t.setHour((diff / (60 * 60 * 1000) - t.getDay() * 24));  
	        t.setMin(((diff / (60 * 1000)) - t.getDay() * 24 * 60 - t.getHour() * 60));  
	        t.setSec((diff/1000-t.getDay()*24*60*60-t.getHour()*60*60-t.getMin()*60));
	        System.out.println("token已用时间"+ t.getHour() +"小时" + t.getMin() + "分钟"); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
    } 
	
	/**
	 * 根据personId获取人员信息(person表中的openId)
	 */
	public String getOpenIdByApplyRecordId(String applyRecordId){
		String hql = " from UserApplyRecord where id = '" + applyRecordId +"'";
		UserApplyRecord userApplyRecord = userApplyRecordDao.get(hql);
		return userApplyRecord.getOpenid();
	}
	
	/**
	 * 审核成功接口(type值)
	 * type = 1	帐号审核通过	APPLAY_SUCCESS
	 * type = 2	帐号审核未通过	AUDIT_FAILURE
	 * type = 3	申请审核提醒	AUDIT_NOTICE
	 * --已拿出去用新的方法调用 type = 4	客户预约通知	CUSTOMER_BOOKING_NOTICE
	 * --已拿出去用新的方法调用 type = 5	预约成功通知	APPOINTMENT_SUCCESSFUL
	 * --已拿出去用新的方法调用 type = 6	预约取消通知	CANCELLATION_OF_APPOINTMENT
	 * type = 7	审核成功通知	AUDIT_SUCCESS
	 * type = 8	审核通过提醒	AUDIT_PASSED
	 * type = 9	绑定单元审核失败	AUDIT_FAILURE_UNIT
	 * 
	 * @param applyRecordId 申请记录表Id
	 */
	public AccessResult sendWeixinMessage(String applyRecordId, int type) throws Exception{
		
		boolean back = false;
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String nowtime = df.format(date);
//		Times times = getDistanceTime(nowtime, SendDataUtil.TIME);
		if(StringUtils.isBlank(SendDataUtil.access_token)||"null".equals(SendDataUtil.access_token)){
			AccessToken token = WeixinUtil.getAccessToken();			
			if(token.getErrcode()>0){
				System.out.println("获取access_token时errcode:"+token.getErrcode());
				return new AccessResult(false,"获取token出错");
			}
			updateToken(token.getToken());
		}
		
		String token = "";
		String openId = null;
		try {
			openId = getOpenIdByApplyRecordId(applyRecordId);
		} catch (Exception e) {
			return new AccessResult(false,"获取openId出错");
		}
		System.out.println("111");
		System.out.println(openId);
		switch(type){
			case 1: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage1(token, openId); //帐号审核通过
						if(back){
							break;
						}
					}
					break;
			case 2: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage2(token, openId); //帐号审核未通过
						if(back){
							break;
						}
					}
					break;
			case 3: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage3(token, openId); //申请审核提醒
						if(back){
							break;
						}
					}
					break;
//			case 4: for(int i=0;i<3;i++){
//						try {
//							Thread.currentThread().sleep(500);
//						} catch (Exception e) {}
//						token = SendDataUtil.access_token;
//						back = sendTemplateMessageService.sendMessage4(token, openId,"2017-11-30:14:29:56","服务项目"); //客户预约通知     参数token openId 客户的id time 预约时间  ytype 预约类型
//						if(back){
//							break;
//						}
//					}
//					break;
//			case 5: for(int i=0;i<3;i++){
//						try {
//							Thread.currentThread().sleep(500);
//						} catch (Exception e) {}
//						token = SendDataUtil.access_token;
//						back = sendTemplateMessageService.sendMessage5(token, openId); //预约成功通知				
//						if(back){
//							break;
//						}
//					}
//					break;
//			case 6: for(int i=0;i<3;i++){
//						try {
//							Thread.currentThread().sleep(500);
//						} catch (Exception e) {}
//						token = SendDataUtil.access_token;
//						back = sendTemplateMessageService.sendMessage6(token, openId); //预约取消通知
//						if(back){
//							break;
//						}
//					} 
//					break;
			case 7: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage7(token, openId); //审核成功通知
						if(back){
							break;
						}
					}
					break;
			case 8: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage8(token, openId); //审核通过提醒
						if(back){
							break;
						}
					}
					break;
			case 9: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage9(token, openId,"房型图与户型不符"); //绑定单元审核失败
						if(back){
							break;
						}
					}
					break;
			default:
				return new AccessResult(false,"信息发送失败，不存在此模版消息");
		}
		if(!back){
			return new AccessResult(false,"通知微信用户失败");
		}
		return new AccessResult(true,"已成功通知微信用户");
	}
	
	/**
	 * 客户预约模板消息接口调用的方法
	 * type = 1	客户预约通知	CUSTOMER_BOOKING_NOTICE 暂无使用
	 * type = 2	预约成功通知	APPOINTMENT_SUCCESSFUL
	 * type = 3	预约取消通知	CANCELLATION_OF_APPOINTMENT
	 */
	public AccessResult sendWeixinMessageForAppointment(int type, String openId) throws Exception{
		
		boolean back = false;
		if(StringUtils.isBlank(SendDataUtil.access_token)||"null".equals(SendDataUtil.access_token)){
			AccessToken token = WeixinUtil.getAccessToken();			
			if(token.getErrcode()>0){
				System.out.println("获取access_token时errcode:"+token.getErrcode());
				return new AccessResult(false,"获取token出错");
			}
			updateToken(token.getToken());
		}	
		String token = "";

		switch(type){
			case 1: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage4(token, openId,"2017-11-30:14:29:56","服务项目"); //客户预约通知     参数token openId 客户的id time 预约时间  ytype 预约类型
						if(back){
							break;
						}
					}
					break;
			case 2: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage5(token, openId); //预约成功通知				
						if(back){
							break;
						}
					}
					break;
			case 3: for(int i=0;i<3;i++){
						try {
							Thread.currentThread().sleep(500);
						} catch (Exception e) {}
						token = SendDataUtil.access_token;
						back = sendTemplateMessageService.sendMessage6(token, openId); //预约取消通知
						if(back){
							break;
						}
					} 
					break;
			default:
				return new AccessResult(false,"信息发送失败，不存在此模版消息");
		}
		if(!back){
			return new AccessResult(false,"通知微信用户失败");
		}
		return new AccessResult(true,"已成功通知微信用户");
	}
	
	/**
	 * 开门结果通知模板消息
	 * type = 1 开门成功
	 * type = 2 开门失败
	 */
	public AccessResult sendWeixinMessageForOpenDoor(int type, String openId,String macName,String secret) throws Exception{
		boolean back = false;
		if(StringUtils.isBlank(SendDataUtil.access_token)||"null".equals(SendDataUtil.access_token)){
			AccessToken token = WeixinUtil.getAccessToken();			
			if(token.getErrcode()>0){
				System.out.println("获取access_token时errcode:"+token.getErrcode());
				return new AccessResult(false,"获取token出错");
			}
			updateToken(token.getToken());
		}	
		String token = "";
		System.out.println("开门结果类型:"+type);
		switch(type){
			case 1: for(int i=0;i<3;i++){
				try {
					Thread.currentThread().sleep(500);
				} catch (Exception e) {}
				token = SendDataUtil.access_token;
				back = sendTemplateMessageService.sendMessageOpneDoor(token, openId, 1, macName,secret); //开门成功
				if(back){
					break;
				}
			} 
			break;
			case 2: for(int i=0;i<3;i++){
				try {
					Thread.currentThread().sleep(500);
				} catch (Exception e) {}
				token = SendDataUtil.access_token;
				back = sendTemplateMessageService.sendMessageOpneDoor(token, openId, 2, macName,secret); //开门失败
				if(back){
					break;
				}
			} 
			break;
			default:
				return new AccessResult(false,"信息发送失败，不存在此模版消息");
		}
		if(!back){
			return new AccessResult(false,"通知微信用户失败");
		}
		return new AccessResult(true,"已成功通知微信用户");
	}
	
	/**
	 * 访客预约结果通知
	 */
	public AccessResult sendWeixinMessageForVisitor(String openId,String personName,String contant,int type) throws Exception{
		boolean back = false;
		if(StringUtils.isBlank(SendDataUtil.access_token)||"null".equals(SendDataUtil.access_token)){
			AccessToken token = WeixinUtil.getAccessToken();			
			if(token.getErrcode()>0){
				System.out.println("获取access_token时errcode:"+token.getErrcode());
				return new AccessResult(false,"获取token出错");
			}
			updateToken(token.getToken());
		}	
		String token = "";
		for(int i=0;i<3;i++){
			try {
				Thread.currentThread().sleep(500);
			} catch (Exception e) {}
			token = SendDataUtil.access_token;
			back = sendTemplateMessageService.sendMessageVisitor(token, openId, personName, contant, type);
			if(back){
				break;
			}
		}
		if(!back){
			return new AccessResult(false,"通知微信用户失败");
		}
		return new AccessResult(true,"已成功通知微信用户");
	}
	
	/**
	 * 当扫码提示无权限点击申请时调用的获取keyword表中的跳转地址
	 */
	public String getApply(String content){
		StringBuffer sb = new StringBuffer();
		String hql = " from KeyWord where keyNo = '" + content +"'";  // <a href="http://mj.donghuahongtai.com/weixinpage/userRegister">您暂无权限，请点击申请</a>
		KeyWord k = keyWordDao.get(hql);
		if(k != null){
			if(k.getReplyContent().indexOf("\\n")>=0){	
				String n = k.getReplyContent();
				String x = n.replace("\\n", "\n");
				k.setReplyContent(x);
			}
			if(k.getReplyContent().indexOf("\\\"")>=0){
				String n = k.getReplyContent();
				String x = n.replace("\\\"", "\"");
				k.setReplyContent(x);
			}
			sb.append(k.getReplyContent());
			
		}
		return sb.toString();
	}
	
	public String getVisitor(String content,String mac,String time){
		StringBuffer sb = new StringBuffer();
		String hql = " from KeyWord where keyNo = '" + content +"'";
		KeyWord k = keyWordDao.get(hql);
		if(k != null){
			String n = k.getReplyContent();
			String x = n.replace("MAC", mac);
			x = x.replace("TIME", time);
			sb.append(x);
		}
		return sb.toString();
	}
	
	
	/**
	 * 根据mac获取动态密码对象
	 */
	public DynamicRandom getDynamicRandomByMAC(String mac){
		return dynamicRandomDao.get(" from DynamicRandom where deviceMAC='"+mac+"'");
	}
	
	/**
	 * 根据mac获取设备
	 */
	public Device getDeviceByMAC(String mac){
		return deviceDao.get(" from Device where deviceMac='"+mac+"'");
	}
	
	/**
	 * 根据人员-单元id获取人员-单元关联对象
	 */
	public PersonUnit getPersonUnitByIds(String unitId, String personId){
		return personUnitDao.get(" from PersonUnit where unitId='"+unitId+"' and personId='"+personId+"'");
	}
	
    public String getDataByMac(String mac){
    	return sessionBean.getData(mac);
    }
    public void putDataByMac(String mac,String openId){
    	sessionBean.putData(mac, openId);
    }
    
	/**
	 * 查询OpenDoorFlag
	 */
	public OpenDoorFlag getOpenDoorFlagById(String uuid){
		return openDoorFlagDao.get(OpenDoorFlag.class, uuid);
	}
    
	/**
	 * 插入一条OpenDoorFlag
	 */
	public void insertOpenDoorFlag(String uuid){
		OpenDoorFlag openDoorFlag = new OpenDoorFlag();
		openDoorFlag.setId(uuid);
		openDoorFlag.setFlag(0);
		openDoorFlag.setAddtime(new Date());
		openDoorFlagDao.save(openDoorFlag);
	}
    
	/**
	 * 更新一条OpenDoorFlag
	 */
	public void updateOpenDoorFlag(String uuid,int flag){
		OpenDoorFlag openDoorFlag = openDoorFlagDao.get(OpenDoorFlag.class, uuid);
		if(openDoorFlag!=null){
			openDoorFlag.setFlag(flag);
			openDoorFlagDao.update(openDoorFlag);
		}
	}
	public void updateOpenDoorFlag(OpenDoorFlag openDoorFlag){
		openDoorFlagDao.update(openDoorFlag);
	}
	
	
    public void saveOfflineRecord(OfflineRecordFlag orf){
    	offlineRecordDao.save(orf);
	}
}
