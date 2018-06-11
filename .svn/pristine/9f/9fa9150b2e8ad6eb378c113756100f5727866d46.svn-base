package com.dhht.entity.unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人-单元关联表
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "person_unit")
public class PersonUnit {
	// 主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//人员Id
	@Column(nullable = false, length = 32)
	private String personId;
	
	//单元Id
	@Column(nullable = false, length = 32)
	private String unitId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
}
