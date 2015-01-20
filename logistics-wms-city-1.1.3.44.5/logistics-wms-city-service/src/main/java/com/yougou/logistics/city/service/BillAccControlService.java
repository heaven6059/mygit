package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.BillAccControl;

/*
 * 库存记账类型类 
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
public interface BillAccControlService extends BaseCrudService {
	
	/**
	 *   统一记账过程
	 * 	 I_PAPER_NO	 varchar2	 	 单据编码
 	 *	 I_LOC_TYPE	 char	 	 仓库类型(0：工厂仓；1：中转仓；2:城市仓)
 	 *	 I_PAPER_TYPE	 varchar2	 	 单据类型
 	 *   I_IO_FLAG	 varchar2	 	 进出标识(I-入库 O-出)
 	 *   I_PREPARE_DATA_EXT	 number	 0	 外部准备数据标志(0：内部过程准备数据；1：外部程序准备数据)
	 * @param maps
	 * @throws Exception
	 */
	public void procAccApply(BillAccControlDto controlDto) throws ServiceException;
	
	
	/**
	 *  外部调用数据准备过程
	 *	I_PAPER_NO	varchar2	 	单据编码
	 *	I_PAPER_TYPE	varchar2	 	单据类型
	 *	I_IO_FLAG	varchar2	 	进出标识(I：入 O：出)
	 *	I_CREATOR	varchar2	 	记帐人员
	 *	I_ROW_ID	NUMBER	 	序列号(传入时确保在事务内，临时表TMP_ACC_DATA中唯一)
	 *	I_CELL_ID	NUMBER	null	储位ID
	 * 	I_LOCNO	varchar2	null	仓别
	 *	I_CELL_NO	varchar2	null	储位编码
	 *	I_ITEM_NO	varchar2	null	商品编码
	 *	I_SIZE_NO	varchar2	null	商品尺码
	 *	I_PACK_QTY	NUMBER	1	包装数量
	 * 	I_ITEM_TYPE	varchar2	'0'	商品属性类型
	 * 	I_QUALITY	varchar2	'0'	品质
	 *	I_OWNER_NO	varchar2	null	委托业主编码
	 *	I_SUPPLIER_NO	varchar2	'N'	供应商编码
	 *	I_BOX_NO	varchar2	null	箱号
	 *	I_QTY	NUMBER	 	数量
	 *	I_OUTSTOCK_QTY	NUMBER	0	预下数量
	 *	I_INSTOCK_QTY	NUMBER	0	预上数量
	 *	I_STATUS	varchar2	'0'	盘点时用于锁定(0：可用；1：锁定)
	 *	I_FLAG	varchar2	'0'	库存冻结标识(0-可用；1-冻结)
	 *	I_HM_MANUAL_FLAG	varchar2	'1'	手工移库标识(0:不允许手工移库；1：可手工移库)
	 * @param maps
	 * @throws Exception
	 */
	public void procAccPrepareDataExt(BillAccControlDto controlDto) throws ServiceException;
	
	/**
	 * 查询业务类型名称
	 * @param maps
	 * @return
	 * @throws Exception
	 */
	public List<BillAccControl> findBillAccControlGroupByBillName(Map<String, Object> maps) throws ServiceException;
	
}