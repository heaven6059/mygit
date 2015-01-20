package com.yougou.logistics.city.service;

import java.util.HashMap;
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
import com.yougou.logistics.city.common.dto.CsInstockSettingDto;
import com.yougou.logistics.city.common.model.CsInstockSettingdtl;
import com.yougou.logistics.city.common.model.CsInstockSettingdtl2;
import com.yougou.logistics.city.dal.mapper.CsInstockSettingMapper;
import com.yougou.logistics.city.dal.mapper.CsInstockSettingdtl2Mapper;
import com.yougou.logistics.city.dal.mapper.CsInstockSettingdtlMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Oct 08 15:13:38 CST 2013
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
@Service("csInstockSettingService")
class CsInstockSettingServiceImpl extends BaseCrudServiceImpl implements CsInstockSettingService {
	@Resource
	private CsInstockSettingMapper csInstockSettingMapper;

	@Resource
	private CsInstockSettingdtl2Mapper csInstockSettingdtl2Mapper;

	@Resource
	private CsInstockSettingdtlMapper csInstockSettingdtlMapper;

	@Override
	public BaseCrudMapper init() {
		return csInstockSettingMapper;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int addCsInstockSetting(CsInstockSettingDto dto) throws ServiceException {
		//新增主表
		int count = 0;
		String checkItemStr = checkItem(dto);
		if(checkItemStr!=null){
			throw new ServiceException(checkItemStr);
		}
		String checkCellStr = checkCell(dto);
		if(checkCellStr!=null){
			throw new ServiceException(checkCellStr);
		}
		try {
			count = add(dto);
			if (count >= 0) {
				//增加储位
				addCellInfo(dto);
				//增加商品
				addFoodsInfo(dto);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(e);

		}
		return count;
	}

	private void addFoodsInfo(CsInstockSettingDto dto) {
		String foodstr = dto.getSelectValue();
		if (StringUtils.isNotBlank(foodstr)) {
			String[] items = foodstr.split(",");
			CsInstockSettingdtl dtl = null;
			for (String item : items) {
				dtl = new CsInstockSettingdtl();
				dtl.setLocno(dto.getLocno());
				dtl.setOwnerNo(dto.getOwnerNo());
				dtl.setSettingNo(dto.getSettingNo());
				dtl.setSelectValue(item);
				csInstockSettingdtlMapper.insert(dtl);
			}
		}
	}

	private void addCellInfo(CsInstockSettingDto dto) {
		String cellstr = dto.getCellNo();
		if (StringUtils.isNotBlank(cellstr)) {
			String[] cellNos = cellstr.split(",");
			CsInstockSettingdtl2 dtl2 = null;
			for (String cellNo : cellNos) {
				dtl2 = new CsInstockSettingdtl2();
				dtl2.setLocno(dto.getLocno());
				dtl2.setOwnerNo(dto.getOwnerNo());
				dtl2.setSettingNo(dto.getSettingNo());
				dtl2.setCellNo(cellNo);
				csInstockSettingdtl2Mapper.insert(dtl2);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public int editCsInstockSetting(CsInstockSettingDto dto) throws ServiceException {
		int count = 0;
		try {
			//删除商品信息
			csInstockSettingdtlMapper.deleteBySettingNo(dto);
			String checkItemStr = checkItem(dto);
			if(checkItemStr!=null){
				throw new ServiceException(checkItemStr);
			}
			String checkCellStr = checkCell(dto);
			if(checkCellStr!=null){
				throw new ServiceException(checkCellStr);
			}
			//删除储位信息
			csInstockSettingdtl2Mapper.deleteBySettingNo(dto);
			//增加储位
			addCellInfo(dto);
			//增加商品
			addFoodsInfo(dto);
			//更新主表信息
			count = this.modifyById(dto);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}

	@Override
	public int deleteCsInstockSetting(String keyStr) throws ServiceException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			CsInstockSettingDto dto = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					dto = new CsInstockSettingDto();
					dto.setLocno(substr[0]);
					dto.setSettingNo(substr[1]);
					count += this.deleteById(dto);
					//删除商品信息
					csInstockSettingdtlMapper.deleteBySettingNo(dto);
					//删除储位信息
					csInstockSettingdtl2Mapper.deleteBySettingNo(dto);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
			}
		}
		return count;
	}
	
	public String checkItem(CsInstockSettingDto dto){
		try {
			StringBuffer sb = new StringBuffer();
			Map<String, String> itemMap = new HashMap<String, String>();
			int num = 0;
			String foodstr = dto.getSelectValue();
			if (StringUtils.isNotBlank(foodstr)) {
				String[] items = foodstr.split(",");
				Map<String, Object> map = null;
				map = new HashMap<String, Object>();
				map.put("locno", dto.getLocno());
				map.put("setType", dto.getSetType());
				map.put("detailType", dto.getDetailType());
				for (String item : items) {
					if(itemMap.get(item) != null){
						return "生效对象编码["+item+"]不能重复,保存失败!";
					}
					itemMap.put(item,item);
					map.put("selectValue", item);
					num = csInstockSettingdtlMapper.selectSelectValueCount(map);
					if(num > 0){
						sb.append(item+",");
					}
				}
			}
			if(sb.length() > 0){
				return "生效对象编码["+sb.toString().substring(0,sb.length()-1)+"]在此场景下已经存在,保存失败!";
			}else{
				return null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	public String checkCell(CsInstockSettingDto dto){
		try {
			Map<String, String> cellMap = new HashMap<String, String>();
			String foodstr = dto.getCellNo();
			if (StringUtils.isNotBlank(foodstr)) {
				String[] cells = foodstr.split(",");
				for (String cell : cells) {
					if(cellMap.get(cell) != null){
						return "上架范围编码["+cell+"]不能重复,保存失败!";
					}
					cellMap.put(cell, cell);
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}