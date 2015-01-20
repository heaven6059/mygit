package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.BillUmLoadBoxStatusEnums;
import com.yougou.logistics.city.common.model.BillUmBoxDtl;
import com.yougou.logistics.city.common.model.BillUmLoadbox;
import com.yougou.logistics.city.common.model.BillUmLoadboxDtl;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmBoxDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmLoadboxDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmLoadboxMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:21:11 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmBoxDtlService")
class BillUmBoxDtlServiceImpl extends BaseCrudServiceImpl implements BillUmBoxDtlService {
	@Resource
	private BillUmBoxDtlMapper billUmBoxDtlMapper;

	@Resource
	private BillUmLoadboxMapper billUmLoadboxMapper;

	@Resource
	private BillUmLoadboxDtlMapper billUmLoadboxDtlMapper;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private ConBoxDtlService  conBoxDtlService;
	@Resource
	private ConBoxService  conBoxService;

	private static final String NOSEALED = "Y";

	private static final String STATUS13 = "13";

	@Override
	public BaseCrudMapper init() {
		return billUmBoxDtlMapper;
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billUmBoxDtlMapper.selectSumQty(map, authorityParams);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void createBoxDtl(BillUmLoadbox box, String keyStr, SystemUser user) throws ServiceException {
		try {
			if (keyStr == null) {
				throw new ServiceException("参数错误");
			} else {
				String[] keys = keyStr.split(",");
				Integer rowId = billUmBoxDtlMapper.selectMaxRowId(box);
				Map<String, String> map = procCommonService.procGetContainerNoBase(box.getLocno(), "T",
						box.getCreator(), "", "1", "1", "21");
				String boxNo = map.get("strOutLabelNo");
				Date curDate = new Date();
				
				//获取品质
				String  v_strQuality = billUmLoadboxMapper.selectQuaByLoadBoxNo(box.getLoadboxNo());
				
				//获取储位编码
				String cellNo = procCommonService.getSpecailCellNo(box.getLocno(),"1",v_strQuality,"1","6",box.getItemType());
				
				
				int  i = 0;
				int boxNum =0 ;
				for (String key : keys) {
					String[] subKey = key.split("\\|");
					BillUmBoxDtl dtl = new BillUmBoxDtl();
					
					//添加箱明细
					ConBoxDtl  conBoxDtl  = new   ConBoxDtl();
					conBoxDtl.setBoxNo(boxNo);
					conBoxDtl.setBrandNo(subKey[3]);
					conBoxDtl.setItemNo(subKey[0]);
					conBoxDtl.setSizeNo(subKey[1]);
					conBoxDtl.setBoxId(new BigDecimal(++i));
					conBoxDtl.setStyleNo("N");
					conBoxDtl.setLocno(box.getLocno());
					conBoxDtl.setOwnerNo(box.getOwnerNo());
					conBoxDtl.setQty(new BigDecimal(subKey[2]));
					conBoxDtl.setAddFlag("1");
					conBoxDtlService.add(conBoxDtl);
					
					boxNum += Integer.valueOf(subKey[2]);
					
					dtl.setLoadboxNo(box.getLoadboxNo());
					dtl.setLocno(box.getLocno());
					dtl.setOwnerNo(box.getOwnerNo());
					dtl.setRowId(++rowId);
					dtl.setItemNo(subKey[0]);
					dtl.setSizeNo(subKey[1]);
					dtl.setBrandNo(subKey[3]);
					dtl.setItemQty(new BigDecimal(subKey[2]));
					dtl.setBoxNo(boxNo);
					dtl.setStatus(BillUmLoadBoxStatusEnums.STATUS10.getStatus());
					dtl.setBoxingDate(curDate);
					dtl.setCreatetm(curDate);
					dtl.setCreator(user.getLoginName());
					dtl.setCreatorName(user.getUsername());
					billUmBoxDtlMapper.insertSelective(dtl);
					//回写装箱单明细的装箱数量
					BillUmLoadboxDtl loadDtl = billUmLoadboxDtlMapper.selectByItemNoAndSizeNo(dtl);
					loadDtl.setBoxingQty(new BigDecimal(loadDtl.getBoxingQty().intValue() + dtl.getItemQty().intValue()));
					if (loadDtl.getBoxingQty().intValue() == loadDtl.getItemQty().intValue()) {
						loadDtl.setStatus(BillUmLoadBoxStatusEnums.STATUS13.getStatus());
					}
					billUmLoadboxDtlMapper.updateBoxingQty(loadDtl);
					
				}
				//判断封箱时 是否出现是已装箱数大于实际数量
				List<BillUmLoadboxDtl>  bList = billUmLoadboxDtlMapper.judgeLoadboxDtlBoxNo(box.getLocno(),box.getLoadboxNo());
			    if(0 < bList.size()){
			    	String msg ="";
			    	for (BillUmLoadboxDtl billUmLoadboxDtl : bList) {
			    		msg+= billUmLoadboxDtl.getItemNo()+","+billUmLoadboxDtl.getSizeNo()+",";
					}
			    	throw new ServiceException(msg+"这"+bList.size()+"条记录,已装箱数量大于实际数量");
			    }
				//写箱主档
				ConBox  conBox = new  ConBox();
				conBox.setLocno(box.getLocno());
				conBox.setOwnerNo(box.getOwnerNo());
				conBox.setBoxNo(boxNo);
				conBox.setCreatDate(curDate);
				conBox.setStatus("2");
				conBox.setQty(new BigDecimal(boxNum));
				conBox.setImportDate(curDate);
				conBox.setCellNo(cellNo);
				conBox.setItemType(box.getItemType());
				conBox.setFlag("1");
				conBoxService.add(conBox);
				
				//判断主档下所有明细是否已经全部装箱，然后更改状态
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("locno", box.getLocno());
				param.put("ownerNo", box.getOwnerNo());
				param.put("loadboxNo", box.getLoadboxNo());
				int allCount = billUmLoadboxDtlMapper.selectCount(param, null);
				int count = billUmLoadboxDtlMapper.checkHaveBoxing(box);
				if (allCount == count) {//装箱任务已经全部
					box.setAuditor(user.getLoginName());
					box.setAuditorName(user.getUsername());
					box.setAudittm(curDate);
					box.setStatus(BillUmLoadBoxStatusEnums.STATUS13.getStatus());
					billUmLoadboxMapper.updateByPrimaryKeySelective(box);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void createRfNoSealed(BillUmBoxDtl box, SystemUser user) throws ServiceException {
		try {
			Date date = new Date();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", box.getLocno());
			params.put("loadboxNo", box.getLoadboxNo());
			params.put("noSealed", NOSEALED);
			List<BillUmBoxDtl> list = billUmBoxDtlMapper.selectByParams(null, params);
			
			//获取品质
			String  v_strQuality = billUmLoadboxMapper.selectQuaByLoadBoxNo(box.getLoadboxNo());
			//获取储位编码
			String cellNo = procCommonService.getSpecailCellNo(box.getLocno(),"1",v_strQuality,"1","6",box.getItemType());
			
			String  ownerNo = null;
			int i = 0;
			for (BillUmBoxDtl dtl : list) {
				
				if( i==0){
					ownerNo = dtl.getOwnerNo();
				}
				i++;
				
				//获取箱号
				Map<String, String> map = procCommonService.procGetContainerNoBase(box.getLocno(), "T",
						box.getCreator(), "", "1", "1", "21");
				String boxNo = map.get("strOutLabelNo");
				
				//添加箱明细
				ConBoxDtl  conBoxDtl  = new   ConBoxDtl();
				conBoxDtl.setBoxNo(boxNo);
				conBoxDtl.setBrandNo(dtl.getBrandNo());
				conBoxDtl.setItemNo(dtl.getItemNo());
				conBoxDtl.setSizeNo(dtl.getSizeNo());
				conBoxDtl.setBoxId(new BigDecimal(1));
				conBoxDtl.setStyleNo("N");
				conBoxDtl.setLocno(box.getLocno());
				conBoxDtl.setOwnerNo(dtl.getOwnerNo());
				conBoxDtl.setQty(dtl.getItemQty());
				conBoxDtl.setAddFlag("1");
				conBoxDtlService.add(conBoxDtl);
				
				
				//写箱主档
				ConBox  conBox = new  ConBox();
				conBox.setLocno(box.getLocno());
				conBox.setOwnerNo(dtl.getOwnerNo());
				conBox.setBoxNo(boxNo);
				conBox.setCreatDate(new Date());
				conBox.setStatus("2");
				conBox.setQty(dtl.getItemQty());
				conBox.setImportDate(new Date());
				conBox.setCellNo(cellNo);
				conBox.setItemType(box.getItemType());
				conBox.setFlag("1");
				conBoxService.add(conBox);
				
				
//				//更改装箱单明细的状态
//				BillUmLoadboxDtl loadDtl = billUmLoadboxDtlMapper.selectByItemNoAndSizeNo(dtl);
//				//loadDtl.setBoxingQty(new BigDecimal(loadDtl.getBoxingQty().intValue() + dtl.getItemQty().intValue()));
//				if (loadDtl.getBoxingQty().intValue() == loadDtl.getItemQty().intValue()) {
//					loadDtl.setStatus(BillUmLoadBoxStatusEnums.STATUS13.getStatus());
//				}
//				billUmLoadboxDtlMapper.updateBoxingQty(loadDtl);
				
				
				dtl.setBoxNo(boxNo);
				dtl.setStatus(STATUS13);
				billUmBoxDtlMapper.updateByPrimaryKeySelective(dtl);
			}
			
			//判断主档下所有明细是否已经全部装箱，然后更改状态
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("locno", box.getLocno());
			param.put("ownerNo", ownerNo);
			param.put("loadboxNo", box.getLoadboxNo());
			int allCount = billUmLoadboxDtlMapper.selectCount(param, null);
			
			BillUmLoadbox  boxM = new BillUmLoadbox();
			boxM.setLoadboxNo(box.getLoadboxNo());
			boxM.setLocno(box.getLocno());
			boxM.setOwnerNo(ownerNo);
			int count = billUmLoadboxDtlMapper.checkHaveBoxing(boxM);
			
			if (allCount == count) {//装箱任务已经全部
				boxM.setAuditor(user.getLoginName());
				boxM.setAuditorName(user.getUsername());
				boxM.setAudittm(date);
				boxM.setStatus(BillUmLoadBoxStatusEnums.STATUS13.getStatus());
				billUmLoadboxMapper.updateByPrimaryKeySelective(boxM);
			}
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}