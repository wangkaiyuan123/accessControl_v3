package com.dhht.action.community;

import java.util.List;

import com.dhht.entity.community.Community;

public class CommunityData {
	private int code = 0;   
	private String msg = "";
	//总条数
	private int count;
	private List<Community> data;
	
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
	public List<Community> getData() {
		return data;
	}
	public void setData(List<Community> data) {
		this.data = data;
	}
}
