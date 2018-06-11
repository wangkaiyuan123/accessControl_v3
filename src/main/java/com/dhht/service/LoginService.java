package com.dhht.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.WebUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.SysResource;
import com.dhht.entity.system.SysRoleResource;
import com.dhht.entity.system.SysRoleUser;
import com.dhht.entity.system.User;
import com.dhht.service.system.ResourceService;
import com.dhht.service.system.UserPasswordService;

@Service
public class LoginService {
	
	@Resource
	private BaseDao<User> userDao;
	@Resource
	private BaseDao<SysResource> sysResourceDao;
	@Resource
	private BaseDao<SysRoleUser> sysRoleUserDao;
	@Resource
	private BaseDao<SysRoleResource> sysRoleResourceDao;
	@Resource
	private UserPasswordService userPasswordService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private BaseDao<Region> regionDao;
	
    public AccessResult login(String userName,String pwd){
    	Map<String, Object> resultMap = checkUser(userName,pwd);
    	AccessResult result =  (AccessResult)resultMap.get("result");
    	if(!result.isSuccess()){  //登录不成功
    		return result;  
    	}
    	//登录成功，加载登录用户的资源
    	processLoginSuccess((User)resultMap.get("userInfo"));
    	return result;
    }
    
    /**
     * 处理登录成功,并将登录用户存入session
     * @param user
     */
    public void processLoginSuccess(User user){
    	//加载用户角色  
    	Map<String,SysResource> userResources = new HashMap<String, SysResource>();
    	//String hql = DaoUtil.getFindPrefix(SysRoleUser.class)+"where userId = '"+user.getId()+"'";
    	String hql = DaoUtil.getFindPrefix(SysRoleUser.class)+"where userId =:userId";
    	Map<String, Object> param1 = new HashMap<String, Object>();
    	Map<String, Object> param2 = new HashMap<String, Object>();
    	param1.put("userId", user.getId());
    	String roleId = sysRoleUserDao.get(hql, param1).getRoleId(); //用户角色Id
    	String sql = "select * from sysrole_resource where roleId =:roleId";
    	param2.put("roleId", roleId);
    	//加载角色资源关系数据
    	List<SysRoleResource> roleResourceList = sysRoleResourceDao.findBySql(sql, param2, SysRoleResource.class);  //用户对应的角色资源关联表
    	for(SysRoleResource roleResource :roleResourceList){
    		//加载资源
    		SysResource resource = sysResourceDao.get(SysResource.class, roleResource.getResourceId());  //用户角色对应的资源(一二级菜单)
    		if(resource!=null){   
    			userResources.put(roleResource.getResourceId(), resource);
    		}
    		//加载依赖资源
    		List<SysResource> requiredResources = resourceService.findByPidAndRequired(roleResource.getResourceId(),1);
    		for(SysResource requiredResource:requiredResources){
    			userResources.put(requiredResource.getId(), requiredResource);
    		}
    	}
    	user.setUserResources(userResources);
    	//user.setUserRegions(userRegions);
    	WebUtil.setLoginUser(user);
    	
    }
    /**
     * 校验用户
     * @param userName
     * @param pwd
     * @return
     */
    public Map<String, Object> checkUser(String userName,String pwd){
    	//String hql = DaoUtil.getFindPrefix(User.class)+"where isDeleted = 0  and userName = '"+userName+"'";
    	String hql = DaoUtil.getFindPrefix(User.class)+"where isDeleted = 0  and userName =:userName";
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("userName", userName);
    	User user= userDao.get(hql, param);
    	//User user= userDao.get(hql);
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(user==null){
             map.put("result", new AccessResult(false,"该用户不存在！"));
             return map;
    	}
    	if(user.getIsLocked()==1){
    		 map.put("result", new AccessResult(false,"该用户已被锁定，请联系管理员！"));
             return map;
    	}
    	AccessResult result = userPasswordService.validateUserPassword(user, pwd);
    	map.put("result", result);
    	if(result.isSuccess()){
    		map.put("userInfo", user);
    	}
    	return map; 
    }

    
    /**
     * 退出登录，注销session
     */
    public void logout() {
		WebUtil.getSession().invalidate();
	}
    
    
	public BaseDao<User> getUserDao() {
		return userDao;
	}

	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}

	public UserPasswordService getUserPasswordService() {
		return userPasswordService;
	}

	public void setUserPasswordService(UserPasswordService userPasswordService) {
		this.userPasswordService = userPasswordService;
	}

	public BaseDao<SysRoleUser> getSysRoleUserDao() {
		return sysRoleUserDao;
	}

	public void setSysRoleUserDao(BaseDao<SysRoleUser> sysRoleUserDao) {
		this.sysRoleUserDao = sysRoleUserDao;
	}

	public BaseDao<SysResource> getSysResourceDao() {
		return sysResourceDao;
	}

	public void setSysResourceDao(BaseDao<SysResource> sysResourceDao) {
		this.sysResourceDao = sysResourceDao;
	}

	public BaseDao<SysRoleResource> getSysRoleResourceDao() {
		return sysRoleResourceDao;
	}

	public void setSysRoleResourceDao(BaseDao<SysRoleResource> sysRoleResourceDao) {
		this.sysRoleResourceDao = sysRoleResourceDao;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public BaseDao<Region> getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(BaseDao<Region> regionDao) {
		this.regionDao = regionDao;
	}
    
}
