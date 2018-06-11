package com.dhht.entity.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dhht.entity.region.Region;
/**
 *  系统用户实体
 */
@Entity
@Table(name="user")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//主鍵
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
    //用户名(如：admin)
	@Column(length = 32)
	private String userName;
	
	//密码
	@Column(length = 64)
	private String password;
	
	//是否被锁定   0:未锁定(默认)   1: 已锁定
	@Column(nullable = false , columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer isLocked;
	
	//连续登陆失败次数
	@Column(nullable = false , columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer loginErrorTimes;
	
	//是否修改密码  0:未修改密码(默认)  1:已修改密码
	@Column(nullable = false , columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer isChangePWD;
	
	//是否已删除  0:未删除(默认)  1:已删除
	@Column(nullable = false , columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer isDeleted;
	
	//系统用户联系方式
	@Column(length = 32)
	private String telephone;
	
	//管理员用户等级      浙江省（1）    杭州市（2） 滨江区（3） 街道（4）  小区（5）主要就是这一级的管理员
	
	@Column(columnDefinition = "TINYINT(1)")
	private Integer level;
	
	//是否是民警,民警只负责查看
	@Column(columnDefinition = "TINYINT(1)  DEFAULT 0")
	private Integer isPolic;
	
	// 关联的微信openId，可以为空
	@Column(nullable = true, length = 50, unique = true)
	// 该字段必须保证唯一性
	private String openid;
	
	@Column
	private String realName;
	
	//用户区域Id
	@Column(length = 32)
	private String regionId;
	
	//父类Id
	@Column(length = 64)
	private String parentId;
	
	@Column(length =2)
	private Integer roleLevel;
	
	//用户加载的资源
	@Transient
    private Map<String,SysResource> userResources;
	
	//用户下级机构列表
	@Transient
	//private Map<String,Region> userRegions;
	private List<Region>  userRegions;
	 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}

	public Integer getLoginErrorTimes() {
		return loginErrorTimes;
	}

	public void setLoginErrorTimes(Integer loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}

	public Integer getIsChangePWD() {
		return isChangePWD;
	}

	public void setIsChangePWD(Integer isChangePWD) {
		this.isChangePWD = isChangePWD;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Map<String, SysResource> getUserResources() {
		return userResources;
	}

	public void setUserResources(Map<String, SysResource> userResources) {
		this.userResources = userResources;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsPolic() {
		return isPolic;
	}

	public void setIsPolic(Integer isPolic) {
		this.isPolic = isPolic;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<Region> getUserRegions() {
		return userRegions;
	}

	public void setUserRegions(List<Region> userRegions) {
		this.userRegions = userRegions;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
