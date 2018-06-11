package com.dhht.common;

/**
 * 操作结构封装类
 * 
 * @author 赵兴龙
 */

public class AccessResult {

    /** 操作是否成功 */
    private boolean isSuccess = false;

    /** 结果信息 */
    private String resultMsg = "";

    public AccessResult() {

    }

    public AccessResult(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.resultMsg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

}// /:~
