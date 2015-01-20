package com.yougou.logistics.city.manager.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.enums.JobBizStatusEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.interfaces.RemoteJobService;
import com.yougou.logistics.base.common.model.JobBizLog;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.AuthorityUserinfoService;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-30 下午4:48:06
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service
@ManagedResource(objectName = SysConstans.SYS_NAME+"StaffPerformanceSchedule", description = StringUtils.EMPTY)
public class StaffPerformanceSchedule implements RemoteJobService {

	@Log
	private Logger log;
	
	/**
	 * 调度错误日志
	 */
	private static final List<JobBizLog> JOB_BIZ_LOG = new ArrayList<JobBizLog>();
	
	/**
	 * 调度执行状态
	 */
	private static JobBizStatusEnum jobBizStatusEnum;
	
	@Resource
	private AuthorityUserinfoService  authorityUserinfoService;
	
	private static String Schedule_flag = "Y";

	/**
	 * 执行job
	 */
	@Override
	public void executeJob(String triggerName,String groupName) {
		
		if("N".equals(Schedule_flag)){
			log.error("=====================获取用户中心所有用户，上一调度还未执行完，当前任务不执行! Time :"+System.currentTimeMillis()+"=============");
			return;
		}
		
		Schedule_flag = "N";
		
		jobBizStatusEnum = JobBizStatusEnum.INITIALIZING;
		
		log.error("=====================bak content start! startTime :"+System.currentTimeMillis()+"=============");
		
		try {
			
			
			jobBizStatusEnum = JobBizStatusEnum.INITIALIZED;
			
			//查询有库存数据的仓库
			List<AuthorityUserinfo> authorityUserinfoList =  authorityUserinfoService.selectAuthorityUser4UserCenter();
			
			jobBizStatusEnum = JobBizStatusEnum.RUNNING;
			
			//记录日志
			saveJobBizLog("开始插入用户信息，总共"+authorityUserinfoList.size()+"个用户；开始时间：" + System.currentTimeMillis(),triggerName,groupName);
			
			log.error("==============开始写入历史库存；时间:"+System.currentTimeMillis());
			
			List<AuthorityUserinfo> userList = new ArrayList<AuthorityUserinfo>();
			for(AuthorityUserinfo userinfo : authorityUserinfoList){
				userList.add(userinfo);
			}
			
			//如果查询数据为空不备份
			if(!CommonUtil.hasValue(userList)){
			    log.error("=====================日期："+System.currentTimeMillis()+"=====================写入用户数据失败,没有查询到用户数据");
			}else{
				//先删除所有用户
				authorityUserinfoService.deleteAllAuthorityUserinfo();
				authorityUserinfoService.insertBatchUserInfo(userList);
//				if(a < 1){
//					throw new ServiceException("=====================日期："+System.currentTimeMillis()+";插入用户数据失败！");
//					//log.error("=====================日期："+System.currentTimeMillis()+";插入用户数据失败！");
//				}
			}
			
			//记录日志
			saveJobBizLog("完成用户数据写入,总共写入"+userList.size()+"条用户信息数据；结束时间：" + System.currentTimeMillis(),triggerName,groupName);
			
			log.error("==============完成写入用户数据；时间:"+System.currentTimeMillis());
			
		} catch (ServiceException e1) {
			//记录日志
			saveJobBizLog("插入用户信息数据异常:"+e1.getMessage()+";时间：" + System.currentTimeMillis(),triggerName,groupName);
			log.error("=====================插入用户数据异常："+e1.getMessage(),e1);
		} catch (Exception e2) {
			//记录日志
			saveJobBizLog("插入用户信息数据操作异常:"+e2.getMessage()+";时间：" + System.currentTimeMillis(),triggerName,groupName);
			log.error("=====================插入用户信息数据异常 exception："+e2.getMessage(),e2);
		}
		log.error("=====================bak content end ! endTime :"+System.currentTimeMillis()+"=============");
		
		jobBizStatusEnum = jobBizStatusEnum.FINISHED;
		
		Schedule_flag = "Y";
		
	}

	/***
	 * 获取job的状态
	 */
	@Override
	public JobBizStatusEnum getJobStatus(String arg0, String arg1) {
		return jobBizStatusEnum;
	}
	
	/**
	 * 给调度传日志
	 */
	@Override
	public String getLogs(String triggerName,String groupName,long lastDate) {
		String listStr="[]";
		List<JobBizLog> list=JOB_BIZ_LOG;
		if(list.size()==0){
			return listStr;
		}
		Iterator<JobBizLog> it=list.iterator();
		while(it.hasNext()){
			JobBizLog log=it.next();
			if(null!=log.getGmtDate()&&log.getGmtDate()<=lastDate){
				it.remove();
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			listStr=mapper.writeValueAsString(list);
		} catch (JsonGenerationException e) {
			log.error("给调度框架传数据报错！",e);
		} catch (JsonMappingException e) {
			log.error("给调度框架传数据报错！",e);
		} catch (IOException e) {
			log.error("给调度框架传数据报错！",e);
		}
		return listStr;
	}

	
	private void saveJobBizLog(String errorMsg, String triggerName, String groupName) {
		JobBizLog jobBizLog = new JobBizLog();
		jobBizLog.setTriggerName(triggerName);
		jobBizLog.setGroupName(groupName);
		jobBizLog.setType(jobBizStatusEnum.name());
		jobBizLog.setGmtDate(System.currentTimeMillis());
		jobBizLog.setRemark(errorMsg);
		JOB_BIZ_LOG.add(jobBizLog);
	}
	
	@Override
	public void initializeJob(String arg0, String arg1) {
		System.out.println("远程任务初始化1");
	}

	@Override
	public void pauseJob(String arg0, String arg1) {
		System.out.println("远程任务暂停1");
	}

	@Override
	public void restartJob(String arg0, String arg1) {
		System.out.println("远程任务重启1");
	}

	@Override
	public void resumeJob(String arg0, String arg1) {
		System.out.println("远程任务恢复1");
	}

	@Override
	public void stopJob(String arg0, String arg1) {
		System.out.println("远程任务停止1");
	}
	
	/**
	 * 将List转换为以逗号隔开的字符串
	 * @param list
	 * @param separator
	 * @return
	 */
	private  String listToString(List<String> list, char separator) {  
		return org.apache.commons.lang.StringUtils.join(list.toArray(),separator);  
    }  

}
