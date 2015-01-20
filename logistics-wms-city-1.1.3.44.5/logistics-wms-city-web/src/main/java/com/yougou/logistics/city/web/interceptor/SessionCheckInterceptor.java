package com.yougou.logistics.city.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.web.interceptor.AnnotationBasedIgnoreableInterceptor;

/**
 * 
 * Session检查
 * 
 * @author wei.b
 * @date 2013-8-6 下午7:53:08
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class SessionCheckInterceptor extends AnnotationBasedIgnoreableInterceptor {
    protected static final XLogger logger = XLoggerFactory.getXLogger(SessionCheckInterceptor.class);
    
    private String sessionKey;
    
    private String redirectUrl;
    

	/**
     *  (non-Javadoc)
     * @see com.yougou.logistics.base.web.interceptor.AnnotationBasedIgnoreableInterceptor#preHandleInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     * {@inheritDoc}
     */
    @Override
    protected boolean preHandleInternal(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
    	String loginURL=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    	String ojectName=request.getContextPath();
    	//与用户中心整合后,调整到用户中心的登录
    	//String path=loginURL+ojectName+redirectUrl;
    	String path=redirectUrl;
    	logger.info(redirectUrl);
    	if(null==request.getSession()){
    		response.sendRedirect(path);
    	}else{
    		//安全验证   redirectUrl其中设置为 /login
            if(null==request.getSession().getAttribute(sessionKey)){
            	response.sendRedirect(path);
            }else{
            	return true;
            }
    	}
        
        response.flushBuffer();

        return false;
    }

    @Override
    protected void postHandleInternal(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    protected void afterCompletionInternal(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
    }

    public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
