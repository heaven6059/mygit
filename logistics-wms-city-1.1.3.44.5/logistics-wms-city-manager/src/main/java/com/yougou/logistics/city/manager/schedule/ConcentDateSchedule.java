package com.yougou.logistics.city.manager.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.enums.JobBizStatusEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.interfaces.RemoteJobService;
import com.yougou.logistics.base.common.model.JobBizLog;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.service.ConContentHistoryService;
import com.yougou.logistics.city.service.ConContentService;

@Service
@ManagedResource(objectName = SysConstans.SYS_NAME+"ConcentDateSchedule", description = StringUtils.EMPTY)
public class ConcentDateSchedule implements RemoteJobService {
	
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
	private ConContentService  conContentService;
	
	@Resource
	private ConContentHistoryService  conContentHistoryService;
	
	private static String Schedule_flag = "Y";

	/**
	 * 执行job
	 */
	@Override
	public void executeJob(String triggerName,String groupName) {
		
		if("N".equals(Schedule_flag)){
			log.error("=====================库存备份，上一调度还未执行完，当前任务不执行! Time :"+System.currentTimeMillis()+"=============");
			return;
		}
		
		Schedule_flag = "N";
		
		jobBizStatusEnum = JobBizStatusEnum.INITIALIZING;
		
		log.error("=====================bak content start! startTime :"+System.currentTimeMillis()+"=============");
		
		Map<String,Object> paras = new HashMap<String,Object>(0);
		try {
			String contentDateStr = DateUtil.format1(DateUtil.addDate(new Date(),-1));
			Date contentDate = DateUtil.getdate(contentDateStr);
			//设置查询日期
			paras.put("contentDate", contentDate);
			
			jobBizStatusEnum = JobBizStatusEnum.INITIALIZED;
			
			log.error("==============备份库存日期："+contentDateStr);
			
			//查询有库存数据的仓库
			List<String>  lstLocnos =  conContentService.getLocnoByContent();
			log.error("==============查询有库存记录的仓库列表："+ this.listToString(lstLocnos,',')+"; 时间:"+System.currentTimeMillis(),triggerName,groupName);
			
			jobBizStatusEnum = JobBizStatusEnum.RUNNING;
			
			//记录日志
			saveJobBizLog("开始备份"+contentDateStr+"的库存,总共"+lstLocnos.size()+"个仓库；开始时间：" + System.currentTimeMillis(),triggerName,groupName);
			
			log.error("==============开始写入历史库存；时间:"+System.currentTimeMillis());
			
			int c = 0 ;
			
			for(String locno : lstLocnos){
				
				paras.put("locno", locno);
				//查询是否已经进行过库存备份
				int a = conContentHistoryService.selectSumByLocno(paras);
				//如果没有进行过备份，则需要进行数据备份；
				if(a < 1){
					int b  = conContentHistoryService.insertByContent(paras);
				    log.error("=====================仓库："+locno+"；日期："+contentDateStr+";备份库存数据"+b+"条");
				    c = c + b;
				}else{
					log.error("=====================仓库："+locno+"；日期："+contentDateStr+";已经备份了库存数据，无需再次更新！");
				}
			}
			
			//记录日志
			saveJobBizLog("完成备份"+contentDateStr+"的库存,总共写入"+c+"条库存数据；结束时间：" + System.currentTimeMillis(),triggerName,groupName);
			
			log.error("==============完成写入历史库存；时间:"+System.currentTimeMillis());
			
		} catch (ServiceException e1) {
			//记录日志
			saveJobBizLog("备份库存时异常:"+e1.getMessage()+";时间：" + System.currentTimeMillis(),triggerName,groupName);
			log.error("=====================插入库存数据异常："+e1.getMessage(),e1);
		} catch (Exception e2) {
			//记录日志
			saveJobBizLog("备份库存时操作异常:"+e2.getMessage()+";时间：" + System.currentTimeMillis(),triggerName,groupName);
			log.error("=====================备份历史库存数据异常 exception："+e2.getMessage(),e2);
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
