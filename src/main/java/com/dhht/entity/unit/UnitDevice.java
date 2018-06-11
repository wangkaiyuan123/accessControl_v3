package com.dhht.entity.unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "unit_device")
public class UnitDevice {
	// 主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//设备Id
	@Column(nullable = false, length = 32, unique = true)
	private String deviceId;
	
	//单元Id
	@Column(nullable = false, length = 32)
	private String unitId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
}
