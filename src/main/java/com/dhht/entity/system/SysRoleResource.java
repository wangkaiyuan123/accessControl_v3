package com.dhht.entity.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *   角色资源关联表
 */
@Entity
@Table(name = "sysrole_resource")
public class SysRoleResource implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
    //角色Id
	@Column(length = 32)
	private String roleId;
	
	//资源Id
	@Column(length = 32)
	private String resourceId;

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

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
}
