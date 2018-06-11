package com.dhht.action.device;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.alibaba.fastjson.JSONObject;
import com.dhht.action.BaseAction;
import com.dhht.common.AccessResult;
import com.dhht.entity.doorguard.Device;
import com.dhht.service.device.DeviceService;
import com.dhht.service.record.OperateRecordService;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Namespace("/device")
public class DeviceAction extends BaseAction implements ModelDriven<Device>{
	
	private static final Logger logger = Logger.getLogger(Device.class);
	private Device device = new Device();
	private int page;
	private int rows;
	//根据地址查询
	private String address;
	//变更模式
	private int style;
	//设备Id字符串集数组
	private String deviceIds;
	@Resource
	private DeviceService deviceService;
	@Resource
	private OperateRecordService operateRecordService;
	
	@Override
	public Device getModel() {
		return device;
	}
	
    @Action("list")
    public String list(){
    	return SUCCESS;
    }
    
    @Action("add")
    public String add(){
    	return SUCCESS;
    }
    
    @Action("delete")
    public String delete(){
    	return SUCCESS;
    }
    
    @Action("deleteBatch")
    public String deleteBatch(){
    	return SUCCESS;
    }
    
    @Action("change")
    public String change(){
    	return SUCCESS;
    }
    
    @Action("clear")
    public String clear(){
    	return SUCCESS;
    }
   
    @Action("clearBatch")
    public String clearBatch(){
    	return SUCCESS;
    }
    
    @Action("restartBatch")
    public String restartBatch(){
    	return SUCCESS;
    }
    
    
    //新增设备
    @Action("saveDevice")
    public void saveDevice(){
    	try {
			writeJson(deviceService.saveDevice(device,getLoginUser()));
		} catch (Exception e) {
			logger.error("新增设备失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 13, false, json);
			writeJson(new AccessResult(false,"新增设备失败"));
		}
    }
    
    @Action("deleteDevice")
    public void deleteDevice(){
    	try {
			writeJson(deviceService.deleteDevice(device,getLoginUser()));
		} catch (Exception e) {
			logger.error("删除设备失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 13, false, json);
			writeJson(new AccessResult(false,"删除设备失败"));
		}
    }
    
    //批量删除
    @Action("deleteDeviceBatch")
    public void deleteDeviceBatch(){
    	try {
			writeJson(deviceService.deleteDeviceBatch(deviceIds,getLoginUser()));
		} catch (Exception e) {
			logger.error("删除设备失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 13, false, json);
			writeJson(new AccessResult(false,"删除设备失败"));
		}
    }
     
    
    //获取设备列表
    @Action("getDeviceList")
    public void  getDeviceList(){
    	DeviceData data = new DeviceData();
    	data.setData(deviceService.getDeviceList(address,page,rows,getLoginUser()));
    	data.setCount(deviceService.getCount(address, getLoginUser()));
    	writeJson(data);
    }

    //添加单元获取小区单元列表(包括大门)
    @Action("getUnit")
    public void getUnit(){
    	writeJson(deviceService.getUnit(getLoginUser()));
    }
    
    
    //更改模式
    @Action("changeMode")
    public void changeMode(){
    	try {
			writeJson(deviceService.change(style,device,getLoginUser()));
		} catch (Exception e) {
			logger.error("更改模式失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 10, false, json);
			writeJson(new AccessResult(false,"更改模式失败"));
		}
    }
    
    //清空设备所有的卡号
    @Action("clearCard")
    public void clearCard(){
    	try {
			writeJson(deviceService.clearCard(device,getLoginUser()));
		} catch (Exception e) {
			logger.error("清空所有的卡号失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 12, false, json);
			writeJson(new AccessResult(false,"清空所有的卡号失败"));
		}
    }
    
    //批量清空卡号
    @Action("clearCardBatch")
    public void clearCardBatch(){
    	try {
			writeJson(deviceService.clearCardBatch(deviceIds,getLoginUser()));
		} catch (Exception e) {
			logger.error("清空所有的卡号失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 12, false, json);
			writeJson(new AccessResult(false,"清空所有的卡号失败"));
		}
    }
    
    //设备重启
    @Action("restart")
    public void restart(){
    	try {
			writeJson(deviceService.reStart(deviceIds,getLoginUser()));
		} catch (Exception e) {
			logger.error("重启所选设备失败", e);
			String json = JSONObject.toJSONString(e);
			operateRecordService.saveLog(getLoginUser(), 17, false, json);
			writeJson(new AccessResult(false,"重启所选设备失败"));
		}
    	
    }
    
    
	public DeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}
}
