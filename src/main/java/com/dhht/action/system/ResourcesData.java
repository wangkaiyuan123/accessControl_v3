package com.dhht.action.system;

import java.util.List;

import com.dhht.entity.system.SysResource;
/**
 * 前段自定义数据
 * @author Administrator
 *
 */
public class ResourcesData {
	private int code = 0;
	private String msg = "";
	// 总条数
	private int count;
	private List<SysResource> data;
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
	public List<SysResource> getData() {
		return data;
	}
	public void setData(List<SysResource> data) {
		this.data = data;
	}
	
	
}
