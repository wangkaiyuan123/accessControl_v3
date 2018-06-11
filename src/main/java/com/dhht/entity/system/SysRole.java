package com.dhht.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *  系统角色表
 */
@Entity
@Table(name = "sysRole")
public class SysRole implements Serializable{

	private static final long serialVersionUID = 3928081046787660104L;
	//主鍵
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
    
	//角色代号
	@Column(length = 32)
	private String roleCode;
	
	//角色名称
	@Column(length = 32)
	private String roleName;
    
	@Column(length =2)
	private Integer roleLevel;
	
	//角色所关联的资源
	@Transient
	private List<String> resourceIds;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
	
}
