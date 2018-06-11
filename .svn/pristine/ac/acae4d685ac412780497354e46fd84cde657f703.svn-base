package com.dhht.interceptor;

import java.util.Collection;

import com.dhht.common.WebUtil;
import com.dhht.entity.system.SysResource;



public class PermissionUtils {

    private static String[] commonUrls;
    static {
        //公共资源的URL
    	commonUrls = "/getMenuData,/main,/password/changePwd,/password/savePwd,/logout".split(",");
    	
    }
    //不需要权限:
    

    /**
     * 根据url判断登录用户是否有相应权限
     * 
     * 2015-9-15更新。将indexOf改为equals。避免类似于拥有权限"/user/editPhone"时误认为也拥有权限"/user/edit"的情况发生
     * 
     * @param url
     * @return
     */
    public static boolean hasPerssion(String url) {
        // 过滤公共URL
        for (String commonUrl : commonUrls) {
            if(commonUrl.equals(url)) {
                return true;
            }
        }
        //以下过滤权限*****
        //比对当前url在不在用户资源里面，没有返回false
        Collection<SysResource> userResources = WebUtil.getLoginUser().getUserResources().values();
        for (SysResource resource : userResources) {
            if(resource.getResourceUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}
