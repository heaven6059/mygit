package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmLabelFullPrint;
import com.yougou.logistics.city.common.model.OsCustBuffer;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.mapper.OsCustBufferMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-11-26 14:47:41
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
@Service("osCustBufferService")
class OsCustBufferServiceImpl extends BaseCrudServiceImpl implements OsCustBufferService {
    @Resource
    private OsCustBufferMapper osCustBufferMapper;

    @Override
    public BaseCrudMapper init() {
        return osCustBufferMapper;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void insertBatch(OsCustBuffer custBuffer, List<Store> storeList) throws ServiceException {
		try{
			//验证客户是否为空
			if(!CommonUtil.hasValue(storeList)){
				throw new ServiceException("已选的客户数据为空!");
			}
			
			//查询已有的客户暂存区
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", custBuffer.getLocno());
			List<OsCustBuffer> checkList = osCustBufferMapper.selectByParams(null, params);
			
			//验证客户暂存区是否重复信息准备
			Map<String, String> checkMap = new HashMap<String, String>();
			if(CommonUtil.hasValue(checkList)){
				for (OsCustBuffer osCustBuffer : checkList) {
					StringBuffer sb = new StringBuffer();
					sb.append(osCustBuffer.getLocno());
					sb.append("|");
					sb.append(osCustBuffer.getStoreNo());
					sb.append("|");
					sb.append(osCustBuffer.getWareNo());
					sb.append("|");
					sb.append(osCustBuffer.getAreaNo());
					sb.append("|");
					sb.append(osCustBuffer.getStockNo());
					sb.append("|");
					sb.append(osCustBuffer.getCellNo());
					String key = sb.toString();
					if(checkMap.get(key) == null){
						checkMap.put(key, key);
					}
				}
			}
			
			//开始验证客户信息是否重复
			List<OsCustBuffer> ocbList = new ArrayList<OsCustBuffer>();
			for (Store store : storeList) {
				StringBuffer sb = new StringBuffer();
				sb.append(custBuffer.getLocno());
				sb.append("|");
				sb.append(store.getStoreNo());
				sb.append("|");
				sb.append(custBuffer.getWareNo());
				sb.append("|");
				sb.append(custBuffer.getAreaNo());
				sb.append("|");
				sb.append(custBuffer.getStockNo());
				sb.append("|");
				sb.append(custBuffer.getCellNo());
				String key = sb.toString();
				if(checkMap.get(key) != null){
					throw new ServiceException("客户："+store.getStoreNo()+"已存在相同的暂存区数据!");
				}
				custBuffer.setStoreNo(store.getStoreNo());
				OsCustBuffer newbuffer = new OsCustBuffer();
				setOsCustBuffer(newbuffer, custBuffer);
				ocbList.add(newbuffer);
			}
			
			//批量插入
			int pageNum = 100;
			for(int idx=0;idx<ocbList.size();){
				idx += pageNum;
				if(idx > ocbList.size()){
					osCustBufferMapper.insertBatch(ocbList.subList(idx-pageNum, ocbList.size()));
				}else{
					osCustBufferMapper.insertBatch(ocbList.subList(idx-pageNum, idx));
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	public void setOsCustBuffer(OsCustBuffer newbuffer, OsCustBuffer oldbuffer) {
		newbuffer.setLocno(oldbuffer.getLocno());
		newbuffer.setStoreNo(oldbuffer.getStoreNo());
		newbuffer.setWareNo(oldbuffer.getWareNo());
		newbuffer.setAreaNo(oldbuffer.getAreaNo());
		newbuffer.setStockNo(oldbuffer.getStockNo());
		newbuffer.setaStockNo(oldbuffer.getaStockNo());
		newbuffer.setCellNo(oldbuffer.getCellNo());
		newbuffer.setBufferName(oldbuffer.getBufferName());
		newbuffer.setCreator(oldbuffer.getCreator());
		newbuffer.setCreatorName(oldbuffer.getCreatorName());//创建人中文名称
		newbuffer.setEditor(oldbuffer.getEditor());
		newbuffer.setEditorName(oldbuffer.getEditorName());//修改人中文名称
		newbuffer.setUseVolumn(oldbuffer.getUseVolumn());
		newbuffer.setUseWeight(oldbuffer.getUseWeight());
		newbuffer.setUseBoxnum(oldbuffer.getUseBoxnum());
	}

	@Override
	public List<BillUmLabelFullPrint> findBufferBySys(Map<String, Object> params) {
		return osCustBufferMapper.selectBufferBySys(params);
	}

	@Override
	public int findFullPrintCount(Map<String, Object> params) {
		return osCustBufferMapper.selectFullPrintCount(params);
	}

	@Override
	public List<BillUmLabelFullPrint> findFullPrintByPage(SimplePage page,
			Map<String, Object> params) {
		return osCustBufferMapper.selectFullPrintByPage(page, params);
	}
	
}