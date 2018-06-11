package com.dhht.service.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.community.Community;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.SysRole;
import com.dhht.entity.system.SysRoleUser;
import com.dhht.entity.system.User;


@Service
public class UserService {
	@Resource
	private BaseDao<User> userDao;
	@Resource
	private BaseDao<SysRoleUser> sysRoleUserDao;
	@Resource
	private BaseDao<SysRole> sysRoleDao;
	/**
	 * 新增下级机构系统用户
	 * @param region
	 * @param user
	 * @param map
	 * @return
	 */
	public AccessResult addSystemUser(Region region,User user,Map<String, String> map) {
		User guser = new User();
		User puser = new User();
		//保存管理员账号
		guser.setId(UUIDUtil.generate());
		guser.setLevel(user.getLevel()+1);
		guser.setIsChangePWD(0);
		guser.setIsDeleted(0);
		guser.setIsLocked(0);
		guser.setIsPolic(0);
		guser.setLoginErrorTimes(0);
		guser.setPassword("123456");
		guser.setRealName(map.get("guserRealName"));
		guser.setRegionId(region.getId());
		guser.setTelephone(map.get("guserPhone"));
		guser.setUserName(map.get("guserName"));
		guser.setParentId(user.getId());
		if(user.getLevel()==0){
			guser.setRoleLevel(user.getRoleLevel()+1);
		}else{
			guser.setRoleLevel(user.getRoleLevel()+2);
		}
		userDao.save(guser);
		//保存巡警账号    到了4级机构就只能添加小区管理员，不能添加巡警
		if(user.getLevel()!=4){
			puser.setId(UUIDUtil.generate());
			puser.setLevel(user.getLevel()+1);
			puser.setPassword("111111");
			puser.setRealName(map.get("puserRealName"));
			puser.setRegionId(region.getId());
			puser.setTelephone(map.get("puserPhone"));
			puser.setUserName(map.get("puserName"));
			puser.setParentId(user.getId());
			puser.setIsChangePWD(0);
			puser.setIsDeleted(0);
			puser.setIsLocked(0);
			puser.setIsPolic(1);
			puser.setLoginErrorTimes(0);
			if(user.getLevel()==0){
				puser.setRoleLevel(user.getRoleLevel()+2);
			}else{
				puser.setRoleLevel(user.getRoleLevel()+3);
			}
			userDao.save(puser);
			//保存到关联表
			SysRoleUser psysRoleUser = new SysRoleUser();
			psysRoleUser.setId(UUIDUtil.generate());
			psysRoleUser.setRoleId(getSubRoleId(user,1).getId());
			psysRoleUser.setUserId(puser.getId());
			sysRoleUserDao.save(psysRoleUser);
		}
		
		//保存到角色用户关联表
		SysRoleUser gsysRoleUser = new SysRoleUser();
		gsysRoleUser.setId(UUIDUtil.generate());
		gsysRoleUser.setRoleId(getSubRoleId(user,0).getId());
		gsysRoleUser.setUserId(guser.getId());
		sysRoleUserDao.save(gsysRoleUser);
		return new AccessResult(true,"");
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public SysRole getSubRoleId(User user,int isPolice){
		if(user.getRoleLevel()==0 && isPolice==0){ 
			String hql = DaoUtil.getFindPrefix(SysRole.class)+" where roleLevel = 1";
			return sysRoleDao.get(hql);
		}
		if(user.getRoleLevel()==0 && isPolice == 1){
			String hql = DaoUtil.getFindPrefix(SysRole.class)+" where roleLevel = 2";
			return sysRoleDao.get(hql);
		}
		if(user.getRoleLevel()!=0 && isPolice==0){
			String hql = DaoUtil.getFindPrefix(SysRole.class)+" where roleLevel = "+(user.getRoleLevel()+2);
			return sysRoleDao.get(hql);
		}
		if(user.getRoleLevel()!=0 && isPolice==1){
			String hql = DaoUtil.getFindPrefix(SysRole.class)+" where roleLevel = "+(user.getRoleLevel()+3);
			return sysRoleDao.get(hql);
		}
		return null;
	}
	/**
	 * 根据机构Id删除机构下的用户及 用户角色关联表
	 * @param regionId  机构Id
	 */
	public void deleteUserByRegionId(String regionId){
		String hql = DaoUtil.getFindPrefix(User.class)+" where regionId ='"+regionId+"'";
		List<User> users =  userDao.find(hql);
		for(User u :users){
			String h = DaoUtil.getFindPrefix(SysRoleUser.class)+" where userId = '"+u.getId()+"'";
			SysRoleUser sysRoleUser = sysRoleUserDao.get(h);
			sysRoleUserDao.delete(sysRoleUser);
			userDao.delete(u);
		}
	}
	
	/**
	 * 获取运维人员列表
	 * @return
	 */
	public  List<User> getOperators(String userName,String telephone,int page,int rows){
		String hql = DaoUtil.getFindPrefix(User.class)+" where roleLevel =10";
		StringBuffer sb = new StringBuffer(hql);
		if(!StringUtils.isBlank(userName)){
			sb.append(" and userName like '%"+userName+"%'");
		}
		if(!StringUtils.isBlank(telephone)){
			sb.append(" and telephone like '%"+telephone+"%'");
		}
		return userDao.find(sb.toString(), null, page, rows);
	}
	
	//获取总条数
	public int getCounts(String userName,String telephone){
		String hql = DaoUtil.getFindPrefix(User.class)+" where roleLevel =10";
		StringBuffer sb = new StringBuffer(hql);
		if(!StringUtils.isBlank(userName)){
			sb.append(" and userName like '%"+userName+"%'");
		}
		if(!StringUtils.isBlank(telephone)){
			sb.append(" and telephone like '%"+telephone+"%'");
		}
		return userDao.find(hql).size();
	}
	
	/**
	 * 更新运维人员
	 * @return
	 */
	public AccessResult updataOperator(Map<String, String> map) {
		User opUser = userDao.get(User.class, map.get("operatorId"));
		opUser.setRealName(map.get("operatorRealName"));
		opUser.setUserName(map.get("operatorName"));
		opUser.setTelephone(map.get("operatorPhone"));
		userDao.update(opUser);
		return new AccessResult(true,"修改成功");
	}
	
	/**
	 * 获取运维人员总条数
	 * @return
	 */
	public int getOperatorCount(){
		String hql = DaoUtil.getFindPrefix(User.class)+" where roleLevel =10";
		return userDao.find(hql).size();
	}
	
	
	/**
	 * 新增运维人员(先添加对应的角色，在来新增)
	 */
	public AccessResult savaOperator(Map<String, String> map,User user){
		User operator = new User();
		operator.setId(UUIDUtil.generate());
		operator.setIsChangePWD(0);
		operator.setIsDeleted(0);
		operator.setIsLocked(0);
		operator.setIsPolic(0);
		operator.setLevel(0);
		operator.setLoginErrorTimes(0);
		operator.setPassword("123456");
		operator.setRealName(map.get("operatorRealName"));
		operator.setUserName(map.get("operatorName"));
		operator.setRegionId("");
		operator.setTelephone(map.get("operatorPhone"));
		operator.setParentId(user.getId());
		operator.setRoleLevel(10);  //运维人员角色等级
		userDao.save(operator);
		//同时添加到用户角色关联表中去
		SysRoleUser sysRoleUser = new SysRoleUser();
		sysRoleUser.setId(UUIDUtil.generate());
		sysRoleUser.setRoleId(getRoleByLevel(10).getId());
		sysRoleUser.setUserId(operator.getId());
		sysRoleUserDao.save(sysRoleUser);
		return new AccessResult(true,"新增运维人员成功");
	}
	
	/**
	 * 根据角色等级获取对象
	 * @param level
	 * @return
	 */
	public SysRole getRoleByLevel(int level){
		String hql = DaoUtil.getFindPrefix(SysRole.class)+" where roleLevel ="+level;
		return sysRoleDao.get(hql);
	}
	
	/**
	 * 根据Id删除运维人员
	 * @param operatorId
	 * @return
	 */
	public AccessResult deleteOperatorById(String operatorId){
		User opUser = userDao.get(User.class, operatorId);  //持久态
		userDao.delete(opUser);
		String hql = DaoUtil.getFindPrefix(SysRoleUser.class)+" where userId = '"+ operatorId +"'";
		SysRoleUser sysRoleUser =  sysRoleUserDao.get(hql);
		sysRoleUserDao.delete(sysRoleUser);
		return new AccessResult(true,"删除运维人员成功");
	}
	
	/**
	 * 重置运维人员密码
	 * @return
	 */
	public AccessResult resetPwd(String operatorId){
		User opUser = userDao.get(User.class, operatorId);
		opUser.setPassword("111111");
		userDao.update(opUser);
		return new AccessResult(true,"重置成功");
	}
	
	public AccessResult addCommunityGly(Community community,Region region,User user,String glyName,String glyPhone){
		User cUser = new User();
		cUser.setId(UUIDUtil.generate());
		cUser.setIsChangePWD(0);
		cUser.setIsDeleted(0);
		cUser.setIsLocked(0);
		cUser.setIsPolic(0);
		cUser.setLoginErrorTimes(0);
		cUser.setPassword("123456");
		cUser.setLevel(5);
		cUser.setRealName(community.getCommunityName()+"管理员");
		//设置所在小区Id
		cUser.setRegionId(region.getId());
		cUser.setTelephone(glyPhone);
		cUser.setUserName(glyName);  //账号
		cUser.setParentId(user.getId());
		cUser.setRoleLevel(9);   //角色等级
		userDao.save(cUser);
		//角色用户关联表
		SysRoleUser sysRoleUser = new SysRoleUser();
		sysRoleUser.setId(UUIDUtil.generate());
		sysRoleUser.setRoleId(getRoleByLevel(9).getId());
		sysRoleUser.setUserId(cUser.getId());
		sysRoleUserDao.save(sysRoleUser);
		return new AccessResult(true,"");
	}
	
	public BaseDao<User> getUserDao() {
		return userDao;
	}

	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}

	public BaseDao<SysRoleUser> getSysRoleUserDao() {
		return sysRoleUserDao;
	}

	public void setSysRoleUserDao(BaseDao<SysRoleUser> sysRoleUserDao) {
		this.sysRoleUserDao = sysRoleUserDao;
	}

	public BaseDao<SysRole> getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(BaseDao<SysRole> sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}
  
} 
