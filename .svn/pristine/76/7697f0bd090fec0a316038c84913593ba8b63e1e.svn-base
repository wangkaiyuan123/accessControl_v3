package com.dhht.service.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.EncodeUtils;
import com.dhht.common.WebUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.system.User;
import com.dhht.service.record.OperateRecordService;
@Service
public class UserPasswordService {
    @Resource
	private BaseDao<User> userDao;
    @Resource
    private OperateRecordService operateRecordService;
	/**
	 * 如果密码输入错误，还未到连续10次时，错误一次errorTimes + 1 
     * 如果密码已经连续输入错误5次，则锁定该账号<br />
     * 密码输入成功  设置登录错误次数为0
	 * @param user
	 * @param pwd
	 * @return
	 */
	public AccessResult validateUserPassword(User user,String pwd){
	    boolean  pwdIsOK = false;
		if(user.getIsChangePWD()==1){  
			pwdIsOK = user.getPassword().equals(EncodeUtils.sha256Encode(pwd));   //密码已修改
		}else{
			pwdIsOK = user.getPassword().equals(pwd);     //密码未修改的
		}
		//密码不正确
		if(!pwdIsOK){
			if(user.getLoginErrorTimes()>9){
				user.setIsLocked(1);
				user.setLoginErrorTimes(user.getLoginErrorTimes()+1);
				userDao.saveOrUpdate(user);
				return new AccessResult(false,"密码连续输入10次,账户已被锁定！");
			}else{
				user.setLoginErrorTimes(user.getLoginErrorTimes()+1);
				userDao.saveOrUpdate(user);
				return new AccessResult(false,"用户名或密码不正确");
			}
		}
		user.setLoginErrorTimes(0);
		userDao.saveOrUpdate(user);
		return new AccessResult(pwdIsOK,"验证成功！");
	}
   
	/**
	 * 修改密码
	 * @param user  登录用户
	 * @param OldPwd   原密码
	 * @param newPwd  新密码
	 * @return
	 */
 	public AccessResult changePwd(User user,String OldPwd,String newPwd){
		AccessResult ar = validatePassword(user,OldPwd);
		//密码验证不成功
		if(!ar.isSuccess()){
			return ar;
		}
		if(newPwd.equals(OldPwd)){
			return new AccessResult(false,"新密码与原密码不能相同");
		}
		AccessResult result = pwdRules(newPwd);
		if(!result.isSuccess()){
			result.setResultMsg("修改密码失败("+result.getResultMsg()+")");
			return result;
		}
		
		//user.setPassword(EncodeUtils.sha256Encode(newPwd));
		user.setPassword(EncodeUtils.sha256Encode(newPwd));
		
		//若未修改密码，改变session中的loginUser,防止修改密码后不跳转
		if(user.getIsChangePWD()==0){  //未修改密码
			user.setIsChangePWD(1);
			WebUtil.updateChangePwdFlagInSession();
		}
		userDao.saveOrUpdate(user);
		String content = user.getRealName()+"将密码修改为"+newPwd;
		operateRecordService.saveLog(user, 15, true, content);
		return new AccessResult(true,"修改密码成功");
	}
	
	/**
	 * 登录之后,修改密码时进行的验证
	 * @return
	 */
	public AccessResult validatePassword(User user,String pwd){
		boolean  pwdIsOK = false;
		if(user.getIsChangePWD()==1){  
			pwdIsOK = user.getPassword().equals(EncodeUtils.sha256Encode(pwd));   //密码已修改
		}else{
			pwdIsOK = user.getPassword().equals(pwd);     //密码未修改的
		}
		if(!pwdIsOK){
			//密码验证不成功
			return new AccessResult(false,"密码不正确");
		}
		return new AccessResult(pwdIsOK,"");
	}
	
	
	public AccessResult pwdRules(String newPwd){
		if(newPwd.length()<6){
			return new AccessResult(false,"新密码长度至少为6位");
		}
		return new AccessResult(true,"");
	}
	
	
	public BaseDao<User> getUserDao() {
		return userDao;
	}

	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}
	
}
