package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmLabelFullPrintVo;
import com.yougou.logistics.city.common.model.BmPrintLog;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.dal.mapper.BmPrintLogMapper;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 07 17:41:16 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("bmPrintLogService")
class BmPrintLogServiceImpl extends BaseCrudServiceImpl implements BmPrintLogService {
	@Resource
	private BmPrintLogMapper bmPrintLogMapper;

	@Resource
	private ProcCommonService procCommonService;

	@Override
	public BaseCrudMapper init() {
		return bmPrintLogMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<String> getLabelPrefix(SystemUser user, int qty, String printType, String storeName)
			throws ServiceException {
		try {
			String locno = user.getLocNo();
			String userId = user.getLoginName();
			String username = user.getUsername();
			
			String strpapertype = "";
			String papertype = "";
			if("US".equals(printType)){
				papertype = "US";
				strpapertype = "S";
			}else{
				papertype = "UM";
				strpapertype = printType.charAt(0)+"";
			}
			Map<String, String> map = procCommonService.procGetContainerNoBase(locno, strpapertype, userId, "T",
					String.valueOf(qty), "0", "21");
			List<String> labels = bmPrintLogMapper.selectLabelNo(map);
			// 清除条码
			bmPrintLogMapper.clearLabelNo(map);
			if (labels == null || labels.size() == 0) {
				throw new ServiceException("获取标签失败");
			}
			BmPrintLog log = new BmPrintLog();
			log.setLocno(locno);
			log.setPapertype(papertype);
			int maxRow = bmPrintLogMapper.selectMaxRowId(log);
			log.setCreatetm(new Date());
			log.setCreator(userId);
			log.setCreatorName(username);
			log.setStartSerial(labels.get(0));
			log.setEndSerial(labels.get(labels.size() - 1));
			log.setRowId(++maxRow);
			log.setGetQty(labels.size());
			log.setStoreName(storeName);
			bmPrintLogMapper.insertSelective(log);
			return labels;
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Object getLabel4Full(SystemUser user, String printType,
			String dataStr) throws ServiceException {
		Map<String, Object> obj = new HashMap<String, Object>();
		List<BillUmLabelFullPrintVo> list = new ArrayList<BillUmLabelFullPrintVo>();
		try {
			String [] array = dataStr.split("\\|\\|");
			if(array != null && array.length > 0){
				
				BillUmLabelFullPrintVo vo = null;
				for(String str:array){
					String [] rowData = str.split("_");
					if(rowData != null && rowData.length > 0){
						String qtyStr = rowData[3];
						if(StringUtils.isNumeric(qtyStr)){
							String storeNo = rowData[0];
							String storeName = rowData[1];
							String address = rowData[2];
							String bufferName = rowData.length == 5? rowData[4] : "";
							vo = new BillUmLabelFullPrintVo();
							vo.setStoreNo(storeNo);
							vo.setStoreName(storeName);
							vo.setAddress(address);
							vo.setBufferName(bufferName);
							vo.setLabels(getLabelPrefix(user, Integer.parseInt(qtyStr), printType, storeName));
							list.add(vo);
						}
					}
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage());
		}
		if(list.size() == 0){
			obj.put("result", ResultEnums.FAIL);
			obj.put("msg", "获取标签失败");
		}else{
			obj.put("data", list);
			obj.put("result", ResultEnums.SUCCESS);
		}
		return obj;
	}
}