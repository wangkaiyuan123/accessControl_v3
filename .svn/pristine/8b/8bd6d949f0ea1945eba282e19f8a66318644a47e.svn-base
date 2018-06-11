package com.dhht.entity.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *   角色用户关联表
 */
@Entity
@Table(name = "sysrole_user")
public class SysRoleUser implements Serializable{

	private static final long serialVersionUID = -3494069563890559704L;
	// 主鍵
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//角色ID
	@Column(length = 32)
	private String roleId;
	
	//用户Id
	@Column(length = 32)
	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
