package com.dhht.service.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.User;
import com.dhht.service.system.UserService;
@Service
public class RegionService {
     
	@Resource
	private BaseDao<Region> regionDao;
	@Resource
	private UserService userService;
	/**
	 * 获取下级机构列表(不包含小区机构)
	 * @param user
	 * @return
	 */
	public List<Region> getRegionsList(String regionId){
		List<Region> userRegions = new ArrayList<Region>();
    	if("".equals(regionId)||regionId==null){  //超级管理员,运维人员
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = 1";
    		List<Region> regions1 = regionDao.find(hql1);
    		for(Region r1:regions1){  //一级
    			userRegions.add(r1);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level = 2" + " and parentId ='"+r1.getId()+"'";
    			List<Region> regions2 = regionDao.find(hql2);
    			for(Region r2:regions2){  //二级
    				userRegions.add(r2);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level = 3" + " and parentId ='"+r2.getId()+"'";
    				List<Region> regions3 = regionDao.find(hql3);
    				for(Region r3:regions3){  //三级
    					userRegions.add(r3);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level = 4" + " and parentId ='"+r3.getId()+"'";
    					List<Region> regions4 = regionDao.find(hql4);
    					for(Region r4:regions4){    //四级
    						userRegions.add(r4);
//    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level = 5" + " and parentId ='"+r4.getId()+"'";
//    						List<Region> regions5 = regionDao.find(hql5);
//    						for(Region r5:regions5){  //五级(小区)
//    							userRegions.add(r5);
//    						}
    					}
    				}
    			}
    		}
    	}else{
    		//最多只有5级
    		String h = DaoUtil.getFindPrefix(Region.class)+" where id = '"+regionId+"'";
    		Region region = regionDao.get(h);
    		userRegions.add(region);   //顶层
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = "+(region.getLevel()+1) +" and parentId='"+region.getId()+"'";
    		List<Region> regions2 = regionDao.find(hql1);
    		for(Region r2:regions2){    //第二层
    			if(r2.getLevel()<=4){
    				userRegions.add(r2);
    			}
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level="+(region.getLevel()+2)+" and parentId='"+r2.getId()+"'";
    			List<Region> regions3 = regionDao.find(hql2);
    			for(Region r3:regions3){  //第三层
    				if(r3.getLevel()<=4){
    					userRegions.add(r3);
    				}
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+3) +" and parentId='"+r3.getId()+"'";
    				List<Region> regions4 = regionDao.find(hql3);
    				for(Region r4:regions4){  //第四层
    					if(r4.getLevel()<=4){
    						userRegions.add(r4);
    					}
//    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+4) +" and parentId='"+r4.getId()+"'";
//    					List<Region> regions5 = regionDao.find(hql4);
//    					for(Region r5:regions5){
//    						userRegions.add(r5);
//    					}
    				}
    			}
    		}
    	}
    	return userRegions;
	}
	
	/**
	 * 包涵小区
	 * @param regionId
	 * @return
	 */
	public List<Region> getRegions(String regionId){
		List<Region> userRegions = new ArrayList<Region>();
    	if("".equals(regionId)||regionId==null){  //超级管理员
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = 1";
    		List<Region> regions1 = regionDao.find(hql1);
    		for(Region r1:regions1){  //一级
    			userRegions.add(r1);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level = 2" + " and parentId ='"+r1.getId()+"'";
    			List<Region> regions2 = regionDao.find(hql2);
    			for(Region r2:regions2){  //二级
    				userRegions.add(r2);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level = 3" + " and parentId ='"+r2.getId()+"'";
    				List<Region> regions3 = regionDao.find(hql3);
    				for(Region r3:regions3){  //三级
    					userRegions.add(r3);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level = 4" + " and parentId ='"+r3.getId()+"'";
    					List<Region> regions4 = regionDao.find(hql4);
    					for(Region r4:regions4){    //四级
    						userRegions.add(r4);
    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level = 5" + " and parentId ='"+r4.getId()+"'";
    						List<Region> regions5 = regionDao.find(hql5);
    						for(Region r5:regions5){  //五级(小区)
    							userRegions.add(r5);
    						}
    					}
    				}
    			}
    		}
    	}else{
    		//最多只有5级
    		String h = DaoUtil.getFindPrefix(Region.class)+" where id = '"+regionId+"'";
    		Region region = regionDao.get(h);
    		userRegions.add(region);   //顶层
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = "+(region.getLevel()+1) +" and parentId='"+region.getId()+"'";
    		List<Region> regions2 = regionDao.find(hql1);
    		for(Region r2:regions2){    //第二层
    			userRegions.add(r2);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level="+(region.getLevel()+2)+" and parentId='"+r2.getId()+"'";
    			List<Region> regions3 = regionDao.find(hql2);
    			for(Region r3:regions3){  //第三层
    				userRegions.add(r3);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+3) +" and parentId='"+r3.getId()+"'";
    				List<Region> regions4 = regionDao.find(hql3);
    				for(Region r4:regions4){  //第四层
    					userRegions.add(r4);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+4) +" and parentId='"+r4.getId()+"'";
    					List<Region> regions5 = regionDao.find(hql4);
    					for(Region r5:regions5){
    						userRegions.add(r5);
    					}
    				}
    			}
    		}
    	}
    	return userRegions;
	}
	
	/**
	 * 获取组织机构够滤掉单元
	 * @param regionId
	 * @return
	 */
	public List<Region> getUnitRegions(String regionId){
		List<Region> userRegions = new ArrayList<Region>();
    	if("".equals(regionId)||regionId==null){  //超级管理员
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = 1";
    		List<Region> regions1 = regionDao.find(hql1);
    		for(Region r1:regions1){  //一级
    			userRegions.add(r1);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level = 2" + " and parentId ='"+r1.getId()+"'";
    			List<Region> regions2 = regionDao.find(hql2);
    			for(Region r2:regions2){  //二级
    				userRegions.add(r2);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level = 3" + " and parentId ='"+r2.getId()+"'";
    				List<Region> regions3 = regionDao.find(hql3);
    				for(Region r3:regions3){  //三级
    					userRegions.add(r3);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level = 4" + " and parentId ='"+r3.getId()+"'";
    					List<Region> regions4 = regionDao.find(hql4);
    					for(Region r4:regions4){    //四级
    						userRegions.add(r4);
    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level = 5" + " and parentId ='"+r4.getId()+"'";
    						List<Region> regions5 = regionDao.find(hql5);
    						for(Region r5:regions5){  //五级(小区)
    							userRegions.add(r5);
    						}
    					}
    				}
    			}
    		}
    	}else{
    		//最多只有5级
    		String h = DaoUtil.getFindPrefix(Region.class)+" where id = '"+regionId+"'";
    		Region region = regionDao.get(h);
    		userRegions.add(region);   //顶层
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = "+(region.getLevel()+1) +" and parentId='"+region.getId()+"'";
    		List<Region> regions2 = regionDao.find(hql1);
    		for(Region r2:regions2){    //第二层
    			if(r2.getLevel()<=5){
    				userRegions.add(r2);
    			}
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level="+(region.getLevel()+2)+" and parentId='"+r2.getId()+"'";
    			List<Region> regions3 = regionDao.find(hql2);
    			for(Region r3:regions3){  //第三层
    				if(r3.getLevel()<=5){
    					userRegions.add(r3);
    				}
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+3) +" and parentId='"+r3.getId()+"'";
    				List<Region> regions4 = regionDao.find(hql3);
    				for(Region r4:regions4){  //第四层
    					if(r4.getLevel()<=5){
    						userRegions.add(r4);
    					}
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+4) +" and parentId='"+r4.getId()+"'";
    					List<Region> regions5 = regionDao.find(hql4);
    					for(Region r5:regions5){
    						if(r5.getLevel()<=5){
    							userRegions.add(r5);
    						}
    					}
    				}
    			}
    		}
    	}
    	return userRegions;
	}
	
	/**
	 * 过滤掉大门(组织架构)
	 * @param regionId
	 * @return
	 */
	public List<Region> getRegionsTrees(String regionId){
		List<Region> userRegions = new ArrayList<Region>();
    	if("".equals(regionId)||regionId==null){  //超级管理员
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = 1";
    		List<Region> regions1 = regionDao.find(hql1);
    		for(Region r1:regions1){  //一级
    			userRegions.add(r1);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level = 2" + " and parentId ='"+r1.getId()+"'";
    			List<Region> regions2 = regionDao.find(hql2);
    			for(Region r2:regions2){  //二级
    				userRegions.add(r2);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level = 3" + " and parentId ='"+r2.getId()+"'";
    				List<Region> regions3 = regionDao.find(hql3);
    				for(Region r3:regions3){  //三级
    					userRegions.add(r3);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level = 4" + " and parentId ='"+r3.getId()+"'";
    					List<Region> regions4 = regionDao.find(hql4);
    					for(Region r4:regions4){    //四级
    						userRegions.add(r4);
    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level = 5" + " and parentId ='"+r4.getId()+"'";
    						List<Region> regions5 = regionDao.find(hql5);
    						for(Region r5:regions5){  //五级(小区)
    							userRegions.add(r5);
    							String hql6 = DaoUtil.getFindPrefix(Region.class)+" where level = 6" + " and parentId ='"+r5.getId()+"' and isGurad = 0";
    							List<Region> regions6 = regionDao.find(hql6);
    							for(Region r6:regions6){
    								userRegions.add(r6);
    							}
    						}
    					}
    				}
    			}
    		}
    	}else{
    		//最多只有5级
    		String h = DaoUtil.getFindPrefix(Region.class)+" where id = '"+regionId+"'";
    		Region region = regionDao.get(h);
    		userRegions.add(region);   //顶层
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = "+(region.getLevel()+1) +" and parentId='"+region.getId()+"' and isGurad = 0 ";
    		List<Region> regions2 = regionDao.find(hql1);
    		for(Region r2:regions2){    //第二层
    			userRegions.add(r2);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level="+(region.getLevel()+2)+" and parentId='"+r2.getId()+"' and isGurad = 0";
    			List<Region> regions3 = regionDao.find(hql2);
    			for(Region r3:regions3){  //第三层
    				userRegions.add(r3);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+3) +" and parentId='"+r3.getId()+"' and isGurad = 0";
    				List<Region> regions4 = regionDao.find(hql3);
    				for(Region r4:regions4){  //第四层
    					userRegions.add(r4);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+4) +" and parentId='"+r4.getId()+"' and isGurad = 0";
    					List<Region> regions5 = regionDao.find(hql4);
    					for(Region r5:regions5){
    						userRegions.add(r5);
    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+5) +" and parentId='"+r5.getId()+"' and isGurad = 0";
        					List<Region> regions6 = regionDao.find(hql5);
        					for(Region r6:regions6){
        						userRegions.add(r6);
        					}
    					}
    				}
    			}
    		}
    	}
    	return userRegions;
	}
	
	
	/**
	 * 保存大门单元(设备)
	 * @param regionId
	 * @return
	 */
	public List<Region> getDevicesByRegions(String regionId){
		List<Region> userRegions = new ArrayList<Region>();
    	if("".equals(regionId)||regionId==null){  //超级管理员
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = 1";
    		List<Region> regions1 = regionDao.find(hql1);
    		for(Region r1:regions1){  //一级
    			userRegions.add(r1);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level = 2" + " and parentId ='"+r1.getId()+"'";
    			List<Region> regions2 = regionDao.find(hql2);
    			for(Region r2:regions2){  //二级
    				userRegions.add(r2);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level = 3" + " and parentId ='"+r2.getId()+"'";
    				List<Region> regions3 = regionDao.find(hql3);
    				for(Region r3:regions3){  //三级
    					userRegions.add(r3);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level = 4" + " and parentId ='"+r3.getId()+"'";
    					List<Region> regions4 = regionDao.find(hql4);
    					for(Region r4:regions4){    //四级
    						userRegions.add(r4);
    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level = 5" + " and parentId ='"+r4.getId()+"'";
    						List<Region> regions5 = regionDao.find(hql5);
    						for(Region r5:regions5){  //五级(小区)
    							userRegions.add(r5);
    							String hql6 = DaoUtil.getFindPrefix(Region.class)+" where level = 6" + " and parentId ='"+r5.getId()+"'";
    							List<Region> regions6 = regionDao.find(hql6);
    							for(Region r6:regions6){
    								userRegions.add(r6);
    							}
    						}
    					}
    				}
    			}
    		}
    	}else{
    		//最多只有5级
    		String h = DaoUtil.getFindPrefix(Region.class)+" where id = '"+regionId+"'";
    		Region region = regionDao.get(h);
    		userRegions.add(region);   //顶层
    		String hql1 = DaoUtil.getFindPrefix(Region.class)+" where level = "+(region.getLevel()+1) +" and parentId='"+region.getId()+"'";
    		List<Region> regions2 = regionDao.find(hql1);
    		for(Region r2:regions2){    //第二层
    			userRegions.add(r2);
    			String hql2 = DaoUtil.getFindPrefix(Region.class)+" where level="+(region.getLevel()+2)+" and parentId='"+r2.getId()+"'";
    			List<Region> regions3 = regionDao.find(hql2);
    			for(Region r3:regions3){  //第三层
    				userRegions.add(r3);
    				String hql3 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+3) +" and parentId='"+r3.getId()+"'";
    				List<Region> regions4 = regionDao.find(hql3);
    				for(Region r4:regions4){  //第四层
    					userRegions.add(r4);
    					String hql4 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+4) +" and parentId='"+r4.getId()+"'";
    					List<Region> regions5 = regionDao.find(hql4);
    					for(Region r5:regions5){
    						userRegions.add(r5);
    						String hql5 = DaoUtil.getFindPrefix(Region.class)+" where level="+ (region.getLevel()+5) +" and parentId='"+r5.getId()+"'";
        					List<Region> regions6 = regionDao.find(hql5);
        					for(Region r6:regions6){
        						userRegions.add(r6);
        					}
    					}
    				}
    			}
    		}
    	}
    	return userRegions;
	}
	
	
	/**
	 * 保存新增机构
	 * @param region
	 * @return
	 */
	public AccessResult save(Region r,User user,Map<String, String> map){
		Region region = new Region();
		//只能一级一级添加下级机构
		region.setId(UUIDUtil.generate());
		region.setLevel(user.getLevel()+1);
		region.setRegionName(r.getRegionName());
		region.setParentId(user.getRegionId());
		region.setIsGurad(0);
		regionDao.saveOrUpdate(region);  //保存到机构表
		//新增下级机构会在下级机构创建管理员及巡警用户
		userService.addSystemUser(region,user,map);
		return new AccessResult(true,"新增机构成功！");
	}
	/**
	 * 删除机构
	 * @param region
	 * @return AccessResult
	 */
	public AccessResult delete(Region region){
 		Region r = regionDao.get(Region.class, region.getId());
		//删除机构的同时，将机构下的管理员及巡警也给删掉
		userService.deleteUserByRegionId(region.getId());   //
		regionDao.delete(r);
		return new AccessResult(true,"删除机构成功");
	}
	
	/**
	 * 修改机构
	 * @param region
	 * @return
	 */
	public AccessResult updata(Region region){
		Region r = regionDao.get(Region.class, region.getId());
	    r.setRegionName(region.getRegionName());
	    regionDao.update(r);
		return new AccessResult(true,"修改机构成功");
	}
	/**
	 * 根据Id查找Region
	 * @param RegionId
	 * @return
	 */
	public Region getById(String RegionId){
		return regionDao.get(Region.class, RegionId);
	}
	
	/**
	 * 获取机构树
	 * @return
	 */
	public List<Region> getRegionTrees(User user){
		List<Region> userRegions = new ArrayList<Region>();
		List<Region> regions = getRegionsList(user.getRegionId());
		//机构储存
		Map<String, Region> map = new HashMap<String, Region>();
		for(Region region :regions){
			map.put(region.getId(), region);
		}
		//循环归类
		for(Region region :regions){
			if(StringUtils.isNotBlank(region.getParentId())){
				Region parentRegion = map.get(region.getParentId());
				if(null != parentRegion){
					if(null == parentRegion.getChildren()){
						List<Region> children = new ArrayList<Region>();
						parentRegion.setChildren(children);
					}
					parentRegion.getChildren().add(region);
				}
			}
			if(user.getLevel()==0){
				if(region.getLevel()==1)
					userRegions.add(region);
			}else{
				if(user.getLevel()==region.getLevel())
					userRegions.add(region);
			}
		}
    	return userRegions;
	}
	
	public BaseDao<Region> getRegionDao() {
		return regionDao;
	}
	public void setRegionDao(BaseDao<Region> regionDao) {
		this.regionDao = regionDao;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
