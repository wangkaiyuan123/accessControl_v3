package com.dhht.action.device;

import java.util.List;

import com.dhht.entity.doorguard.Advertising;

public class AdvertisingData {
	private int code = 0;   
	private String msg = "";
	//总条数
	private int count;
	private List<Advertising> data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Advertising> getData() {
		return data;
	}
	public void setData(List<Advertising> data) {
		this.data = data;
	}
}
