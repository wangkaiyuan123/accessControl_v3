package com.dhht.entity.community;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 小区实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "community")
public class Community implements Serializable{
    //主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//小区名
	@Column(nullable = false,length = 20)
	private String communityName;
	
	//小区地址
	@Column(nullable = false,length = 50)
	private String  address;
    //省
	@Column(length = 32,nullable = false)
	private String provinceId;
	//市
	@Column(length = 32,nullable = false)
	private String cityId;
	//区
	@Column(length = 32,nullable = false)
	private String districtId;
	//街道
	@Column(nullable = false,length = 32)
	private String regionId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	
}
