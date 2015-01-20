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
import com.yougou.logistics.base.common.interfaces.RemoteJobService;
import com.yougou.logistics.base.common.model.JobBizLog;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.service.AccTaskService;

/**
 * sku及容器记账
 * 
 * @author wu.gy
 * @date 2014-7-23 下午4:48:06
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service
@ManagedResource(objectName = SysConstans.SYS_NAME+"AccSkuStockSchedule", description = StringUtils.EMPTY)
public class AccSkuStockSchedule implements RemoteJobService {

	private static Logger log = org.slf4j.LoggerFactory.getLogger("SKUCONSTOCK"); 
	
	/**
	 * 调度错误日志
	 */
	private static final List<JobBizLog> JOB_BIZ_LOG = new ArrayList<JobBizLog>();
	
	/**
	 * 调度执行状态
	 */
	private static JobBizStatusEnum jobBizStatusEnum;
	
	@Resource
	private AccTaskService  accTaskService;
	
	private static String Schedule_flag = "Y";

	/**
	 * 执行job
	 */
	@SuppressWarnings("static-access")
	@Override
	public void executeJob(String triggerName,String groupName) {
		log.info("AccSkuStockSchedule start! ");
		long startTime=System.currentTimeMillis();
		if("N".equals(Schedule_flag)){
			log.error("上一调度还未执行完，当前任务不执行! ");
			return;
		}
		
		Schedule_flag = "N";
		jobBizStatusEnum = JobBizStatusEnum.INITIALIZING;
		try {
			jobBizStatusEnum = JobBizStatusEnum.INITIALIZED;
			
			jobBizStatusEnum = JobBizStatusEnum.RUNNING;
			accTaskService.executeAcctask();
			
			log.info("AccSkuStockSchedule end! usedtime:"+(System.currentTimeMillis()-startTime)+"ms");
		} catch (Exception e1) {
			//记录日志
			saveJobBizLog("sku及容器记账异常:"+e1.getMessage()+";时间：" + System.currentTimeMillis(),triggerName,groupName);
			log.error("sku及容器记账异常："+e1.getMessage(),e1);
		} 
		
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

}
