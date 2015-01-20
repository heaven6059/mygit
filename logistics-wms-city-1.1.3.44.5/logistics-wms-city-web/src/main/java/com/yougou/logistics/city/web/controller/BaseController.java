package com.yougou.logistics.city.web.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.constans.ErrorCodeConstant;
import com.yougou.logistics.city.common.vo.ResultVo;

@Controller
public abstract class BaseController<ModelType> extends BaseCrudController<ModelType> {
	
	@Log
	private Logger logger;
	
	
	@ExceptionHandler(ManagerException.class)
	@ResponseBody
	public ResultVo  handleServiceException(ManagerException ex,HttpServletRequest request) {
		String errorDetail=getStackTrace(ex);
		logger.info("业务系统内部异常>>>>>>>>>"+request.getRequestURI());
		logger.error(ex.getMessage());
		logger.error(errorDetail);
		ResultVo resultVo = new ResultVo();
		resultVo.setErrorCode(ErrorCodeConstant.E_0001);
		resultVo.setErrorMessage("业务系统内部异常"); //ex.getMessage()
		resultVo.setErrorDetail(errorDetail);
		return resultVo;
	}
	
	

	
	
    /** 
     * 获取异常的堆栈信息 
     *  
     * @param t 
     * @return 
     */  
    private static String getStackTrace(Throwable t)  
    {  
        StringWriter sw = new StringWriter();  
        PrintWriter pw = new PrintWriter(sw);  
      
        try  
        {  
            t.printStackTrace(pw);  
            return sw.toString();  
        }  
        finally  
        {  
            pw.close();  
        }  
    }  
    
}
