package com.yougou.logistics.city.common.vo;

import java.io.Serializable;

/**
 * @异常返回结构实体类
 * @author wei.hj
 * @Date 2013-7-29
 * @version 0.1.0
 * @copyright yougou.com
 */
public class ResultVo  implements Serializable {
	private boolean isSuccess;
    private  String errorCode;
    private  String errorMessage;
    private  String errorDetail;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
    
    
}
