package com.dhht.action.person;

import java.util.List;

import com.dhht.entity.system.Person;
/**
 * 数据类
 * @author Administrator
 *
 */
public class PersonData {
	private int code = 0;
	private String msg = "";
	// 总条数
	private long count;
	private List<Person> data;
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
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Person> getData() {
		return data;
	}
	public void setData(List<Person> data) {
		this.data = data;
	}
	
	
}