package com.dhht.service.record;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.Person;
import com.dhht.entity.system.User;
import com.dhht.service.device.DeviceService;
import com.dhht.service.region.RegionService;

@Service
public class OpenDoorService {
	
	@Resource
	private BaseDao<OpenDoorRecord> openDoorRecordDao;
	@Resource
	private BaseDao<Person> personDao;
	@Resource
	private RegionService regionService;
	@Resource
	private DeviceService deviceService;
 
	/**
	 * 获取开门记录
	 * @param page
	 * @param rows
	 * @param user
	 * @return  List<OpenDoorRecord>
	 */
	public List<OpenDoorRecord> getList(int page,int rows,User user){
		if(StringUtils.isBlank(user.getRegionId())){  //admin
			String str = DaoUtil.getFindPrefix(OpenDoorRecord.class)+" order by openDate desc";
			return openDoorRecordDao.find(str, page, rows);
		}
		
		//当前地址
		String address = deviceService.getLoginUserAddress(user, null);
		List<OpenDoorRecord> openDoorRecords = null;
		try {
			List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId()); //包涵单元
			Set<String>  regionsIdSet = new HashSet<String>();
			for(Region region :regions){
				regionsIdSet.add(region.getId());
			}
			String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
			String sql = "SELECT * FROM opendoorrecord WHERE deviceMac IN (SELECT deviceMac FROM device WHERE unitId IN ("+regionsIdStr+")) and address like '%"+address+"%' order by openDate desc";
			openDoorRecords = openDoorRecordDao.findBySql(sql,OpenDoorRecord.class, page, rows);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openDoorRecords;
	}

	//获取总条数
	public long getCounts(User user){
		if(StringUtils.isBlank(user.getRegionId())){
			return openDoorRecordDao.count("select count(id) from OpenDoorRecord");
		}
		
		String address = deviceService.getLoginUserAddress(user, null);
		List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId());   //包涵单元
		Set<String>  regionsIdSet = new HashSet<String>();
		for(Region region :regions){
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
		String sql = "SELECT * FROM opendoorrecord WHERE deviceMac IN (SELECT deviceMac FROM device WHERE unitId IN ("+regionsIdStr+")) and address like '%"+address+"%' order by openDate desc";
		List<OpenDoorRecord> openDoorRecords = openDoorRecordDao.findBySql(sql, OpenDoorRecord.class);
		if(openDoorRecords!=null){
			return openDoorRecords.size();
		}
		return 0;
	}
	
	public BaseDao<OpenDoorRecord> getOpenDoorRecordDao() {
		return openDoorRecordDao;
	}

	public void setOpenDoorRecordDao(BaseDao<OpenDoorRecord> openDoorRecordDao) {
		this.openDoorRecordDao = openDoorRecordDao;
	}
}
