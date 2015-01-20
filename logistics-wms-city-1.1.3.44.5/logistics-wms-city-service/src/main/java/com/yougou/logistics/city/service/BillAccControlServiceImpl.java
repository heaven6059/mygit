package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.BillAccControl;
import com.yougou.logistics.city.dal.database.BillAccControlMapper;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Jan 13 19:48:58 CST 2014
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
@Service("billAccControlService")
class BillAccControlServiceImpl extends BaseCrudServiceImpl implements BillAccControlService {
   
	@Resource
    private BillAccControlMapper billAccControlMapper;
	
	@Log
    Logger log;

    @Override
    public BaseCrudMapper init() {
        return billAccControlMapper;
    }

	@Override
	public void procAccApply(BillAccControlDto controlDto) throws ServiceException {
		try {
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("I_PAPER_NO", controlDto.getiPaperNo());
			map.put("I_LOC_TYPE", controlDto.getiLocType());
			map.put("I_PAPER_TYPE", controlDto.getiPaperType());
			map.put("I_IO_FLAG", controlDto.getiIoFlag());
			map.put("I_PREPARE_DATA_EXT", controlDto.getiPrepareDataExt());
			map.put("I_IS_WEB", controlDto.getiIsWeb());
			billAccControlMapper.procAccApply(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(getExceptionMsg(e.getMessage()));
		}
	}

	@Override
	public void procAccPrepareDataExt(BillAccControlDto controlDto) throws ServiceException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("I_PAPER_NO", controlDto.getiPaperNo());
			map.put("I_PAPER_TYPE", controlDto.getiPaperType());
			map.put("I_LOC_TYPE", controlDto.getiLocType());
			map.put("I_IO_FLAG", controlDto.getiIoFlag());
			map.put("I_CREATOR", controlDto.getiCreator());
			map.put("I_ROW_ID", controlDto.getiRowId());
			map.put("I_CELL_ID", controlDto.getiCellId());
			map.put("I_LOCNO", controlDto.getiLocno());
			map.put("I_CELL_NO", controlDto.getiCellNo());
			map.put("I_ITEM_NO", controlDto.getiItemNo());
			map.put("I_SIZE_NO", controlDto.getiSizeNo());
			map.put("I_PACK_QTY", controlDto.getiPackQty());
			map.put("I_ITEM_TYPE", controlDto.getiItemType());
			map.put("I_QUALITY", controlDto.getiQuality());
			map.put("I_OWNER_NO", controlDto.getiOwnerNo());
			map.put("I_SUPPLIER_NO", controlDto.getiSupplierNo());
			map.put("I_BOX_NO", controlDto.getiBoxNo());
			map.put("I_QTY",controlDto.getiQty());
			map.put("I_OUTSTOCK_QTY", controlDto.getiOutstockQty());
			map.put("I_INSTOCK_QTY", controlDto.getiInstockQty());
			map.put("I_STATUS", controlDto.getiStatus());
			map.put("I_FLAG", controlDto.getiFlag());
			map.put("I_HM_MANUAL_FLAG", controlDto.getiHmManualFlag());
			map.put("I_TERMINAL_FLAG", controlDto.getiTerminalFlag());
			billAccControlMapper.procAccPrepareDataExt(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(getExceptionMsg(e.getMessage()));
		}
	}
	
	//返回异常信息
	public String getExceptionMsg(String msg){
		String[] a = msg.split("<font color=red>");
		if(a.length > 1){
			String[] b = a[1].split("</font>");
			return b[0];
		}
		return msg;
	}

	@Override
	public List<BillAccControl> findBillAccControlGroupByBillName(Map<String, Object> maps) throws ServiceException {
		try{
			return billAccControlMapper.selectBillAccControlGroupByBillName(maps);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(getExceptionMsg(e.getMessage()));
		}
	}
	
}