package com.dhht.action.record;

import java.util.List;

import com.dhht.entity.record.OperateRecord;

public class OperateRecordDate {
	private int code = 0;   
	private String msg = "";
	//总条数
	private long count;
	private List<OperateRecord> date;
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
	public List<OperateRecord> getDate() {
		return date;
	}
	public void setDate(List<OperateRecord> date) {
		this.date = date;
	}
}
