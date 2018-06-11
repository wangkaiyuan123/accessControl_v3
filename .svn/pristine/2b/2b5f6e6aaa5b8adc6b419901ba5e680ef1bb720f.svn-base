package com.dhht.entity.record;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 设备记录表
 * @author Administrator
 *
 */
@Entity
@Table(name = "devicerecord")

public class DeviceRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	//主键
	@Id
	@Column(nullable = false,length = 32 , unique = true)
	private String id;
	
	//设备号
	@Column(length = 10)
	private String deviceNo;
	
	//设备地址
	@Column(length = 50)
	private String deviceAddress;
	
	//设备状态
	@Column(length = 1)
	private Integer state;
	
	//记录时间
	@Column
	private Date recordTime;
	
	//备注
	@Column(length = 50)
	private String comments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
