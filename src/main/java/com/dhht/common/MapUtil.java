/**
 * 
 */
package com.dhht.common;

import java.util.Map;

/**
 * Map工具类
 * 
 * @author zhaoxl
 * 
 */
public class MapUtil {

    /**
     * 判断一个Map对象是否为空
     * 
     * @param m
     * @return 对象为null或size为0时，返回true，否则返回false
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map m) {
        if(m == null || m.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个Map对象是否非空
     * 
     * @param m
     * @return 对象为null或size为0时，返回false，否则返回true
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Map m) {
        if(m == null || m.size() == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * 获取随机数字，n为位数
     * @param n
     * @return
     */
    public static String getRandom(int n){
    	String random = "";
		for(int i=1;i<=n;i++){
			int d=(int)(Math.random()*9+1);
			random+=d;
		}
    	return random;
    }
}
