package com.dhht.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dhht.entity.region.Region;


/**
 *   系统资源表
 */
@Entity
@Table(name = "sysResource")
public class SysResource implements Serializable{

	private static final long serialVersionUID = 6532084045795999320L;
	// 主鍵
	@Id
	@Column(nullable = false, length = 32, unique = true)
    private String id;
	
	//资源名称
	@Column(length = 32)
	private String resourceName;
	
	//资源URL
	@Column(length = 50)
	private String resourceUrl;
	
	//是否菜单项
	@Column(nullable = false,length =1)
	private Integer isMenu ;
	
	//是否依赖项
	@Column(nullable = false,length =1 )
	private Integer isRequired ;
	
	//上级资源ID
	@Column(length = 32)
	private String parentId;
	
	//图标样式
	@Column(length = 32)
	private String iconCls = "";
	
	//排序
	@Column(length = 2)
	private Integer sort;
	
	//下级资源
	@Transient
	private List<SysResource> children;
	
	//上级资源名称
	@Transient
	private String parentName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public Integer getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}

	public Integer getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<SysResource> getChildren() {
		return children;
	}

	public void setChildren(List<SysResource> children) {
		this.children = children;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
