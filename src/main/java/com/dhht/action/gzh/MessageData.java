package com.dhht.action.gzh;

import java.util.List;

import com.dhht.entity.weixin.WeixinDictionary;

public class MessageData {
	private int code = 0;   
	private String msg = "";
	//总条数
	private int count;
	private List<WeixinDictionary> data;
	
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
	public List<WeixinDictionary> getData() {
		return data;
	}
	public void setData(List<WeixinDictionary> data) {
		this.data = data;
	}
}
