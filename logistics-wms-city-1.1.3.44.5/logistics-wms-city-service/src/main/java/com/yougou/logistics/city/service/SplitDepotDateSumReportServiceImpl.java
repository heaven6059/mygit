package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.SplitDepotDateSumReportMapper;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-18 下午12:27:41
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("splitDepotDateSumReportService")
public class SplitDepotDateSumReportServiceImpl extends BaseCrudServiceImpl implements SplitDepotDateSumReportService {

	@Resource
	private SplitDepotDateSumReportMapper splitDepotDateSumReportMapper;

	@Override
	public BaseCrudMapper init() {
		return splitDepotDateSumReportMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return splitDepotDateSumReportMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<SplitDepotDateSumReport> findSplitDepotDateSumReportList(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			
			//查询单据的数据
			Map<Date, SplitDepotDateSumReport> mapBillData = new HashMap<Date, SplitDepotDateSumReport>();
			List<SplitDepotDateSumReport> billList = splitDepotDateSumReportMapper.selectBillDetailByDate(params, authorityParams);
			if(CommonUtil.hasValue(billList)){
				for (SplitDepotDateSumReport sd : billList) {
					if(mapBillData.get(sd.getSelectDate()) == null){
						mapBillData.put(sd.getSelectDate(), sd);
					}
				}
			}
			
			//查询对应库存的数据
			Map<Date, BigDecimal> mapContentData = new HashMap<Date, BigDecimal>();
			List<SplitDepotDateSumReport> contentList = splitDepotDateSumReportMapper.selectConContentHistoryByDate(params, authorityParams);
			if(CommonUtil.hasValue(contentList)){
				for (SplitDepotDateSumReport sd : contentList) {
					if(mapContentData.get(sd.getSelectDate()) == null){
						mapContentData.put(sd.getSelectDate(), sd.getQty());
					}
				}
			}
			
			//写入单据数据、上期库存
			List<SplitDepotDateSumReport> allList = splitDepotDateSumReportMapper.selectAllConContentHistory(params, authorityParams);
			if(CommonUtil.hasValue(allList)){
				for (SplitDepotDateSumReport sd : allList) {
					BigDecimal qty = new BigDecimal(0);
					if(mapContentData.get(sd.getSelectDate()) != null){
						qty = mapContentData.get(sd.getSelectDate());
					}
					sd.setQty(qty);
					if(mapBillData.get(sd.getSelectDate())!=null){
						SplitDepotDateSumReport sd2 = mapBillData.get(sd.getSelectDate());
						setBillQty(sd, sd2);
					}else{
						setBillQty(sd,null);
					}
				}
			}
			
			//计算本期库存
			List<SplitDepotDateSumReport> returnList = new ArrayList<SplitDepotDateSumReport>();
			for (SplitDepotDateSumReport sd : allList) {
				BigDecimal thisIssueQty = new BigDecimal(0);
				if(checkNum(sd)){
					thisIssueQty = getThisIssueQty(sd);
					sd.setThisIssueQty(thisIssueQty);
					
					//差异调整转为正数
					BigDecimal ckCytzQty = sd.getCkCytzQty().abs();
					sd.setCkCytzQty(ckCytzQty);
					returnList.add(sd);
				}
			}
			
			return returnList;
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	//计算上期库存
	public BigDecimal getThisIssueQty(SplitDepotDateSumReport sd) {
		BigDecimal thisIssueQty = 
				sd.getQty()==null?new BigDecimal(0):sd.getQty()
				.add(sd.getRkCrkQty()==null?new BigDecimal(0):sd.getRkCrkQty())
				.add(sd.getRkCyrQty()==null?new BigDecimal(0):sd.getRkCyrQty())
				.add(sd.getRkDthQty()==null?new BigDecimal(0):sd.getRkDthQty())
				.add(sd.getRkQtrkQty()==null?new BigDecimal(0):sd.getRkQtrkQty())
				.subtract(sd.getCkCcdQty()==null?new BigDecimal(0):sd.getCkCcdQty())
				.subtract(sd.getCkCycQty()==null?new BigDecimal(0):sd.getCkCycQty())
				.subtract(sd.getCkQtckQty()==null?new BigDecimal(0):sd.getCkQtckQty())
				.subtract(sd.getCkKbmzhQty()==null?new BigDecimal(0):sd.getCkKbmzhQty())
				.add(sd.getPdPyQty()==null?new BigDecimal(0):sd.getPdPyQty())
				.add(sd.getPdPkQty()==null?new BigDecimal(0):sd.getPdPkQty())
				.add(sd.getRkCytzQty()==null?new BigDecimal(0):sd.getRkCytzQty())//入库差异调整
				.add(sd.getCkCytzQty()==null?new BigDecimal(0):sd.getCkCytzQty())//出库差异调整
				.add(sd.getTzKctzCalcQty()==null?new BigDecimal(0):sd.getTzKctzCalcQty());
		return thisIssueQty;
	}
	
	//当前时间
	public Date getDate(Date date,int num){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, num);  //减1天
		return cal.getTime();
	}
	
	//验证是否显示
	public boolean checkNum(SplitDepotDateSumReport sd){
//		BigDecimal[] Qtys = { sd.getQty(), sd.getRkCrkQty(), sd.getRkCyrQty(), sd.getRkDthQty(), sd.getRkQtrkQty(),
//				sd.getCkCcdQty(), sd.getCkCycQty(), sd.getCkQtckQty(), sd.getCkKbmzhQty(), sd.getPdPyQty(),
//				sd.getPdPkQty(), sd.getTzKctzQty(), sd.getTzKctzTypeQty(), sd.getYdhCrQty(), sd.getYdhCrBoxQty(),
//				sd.getYdhCyrQty(), sd.getYdhCyrBoxQty(), sd.getYdhDtcQty(), sd.getYdhDtcBoxQty(), sd.getYswyCrQty(),
//				sd.getYswyCrBoxQty(), sd.getYswyCyrQty(), sd.getYswyCyrBoxQty(), sd.getYswyDtcQty(),
//				sd.getYswyDtcBoxQty(), sd.getYshCrQty(), sd.getYshCyrQty(), sd.getYshDtcQty(), sd.getYshCrBoxQty(),
//				sd.getYshCyrBoxQty(), sd.getYshDtcBoxQty() };
//		for (BigDecimal value : Qtys) {
//			if(isNull(value)){
//				return true;
//			}
//		}
//		return false;
		
		if(isNull(sd.getQty())||isNull(sd.getRkCrkQty())||isNull(sd.getRkCyrQty())||isNull(sd.getRkDthQty())||isNull(sd.getRkQtrkQty())
				||isNull(sd.getCkCcdQty())||isNull(sd.getCkCycQty())||isNull(sd.getCkQtckQty())||isNull(sd.getCkKbmzhQty())
				||isNull(sd.getPdPyQty())||isNull(sd.getPdPkQty())
				||isNull(sd.getRkCytzQty())||isNull(sd.getCkCytzQty())
				||isNull(sd.getTzKctzQty())||isNull(sd.getTzKctzTypeQty())
				||isNull(sd.getYdhCrQty())||isNull(sd.getYdhCrBoxQty())||isNull(sd.getYdhCyrQty())||isNull(sd.getYdhCyrBoxQty())||isNull(sd.getYdhDtcQty())||isNull(sd.getYdhDtcBoxQty())
				||isNull(sd.getYswyCrQty())||isNull(sd.getYswyCrBoxQty())||isNull(sd.getYswyCyrQty())||isNull(sd.getYswyCyrBoxQty())||isNull(sd.getYswyDtcQty())||isNull(sd.getYswyDtcBoxQty())
				||isNull(sd.getYshCrQty())||isNull(sd.getYshCyrQty())||isNull(sd.getYshDtcQty())||isNull(sd.getYshCrBoxQty())||isNull(sd.getYshCyrBoxQty())||isNull(sd.getYshDtcBoxQty())  ){
			return true;
		}
		return false;
	}
	
	//验证数量是否为空
	public boolean isNull(BigDecimal qty){
		if(qty!=null){
			if(qty.intValue()>0){
				return true;
			}
		}
		return false;
	}
	
	//赋值
	public void setBillQty(SplitDepotDateSumReport sd1,SplitDepotDateSumReport sd2){
		
		//入库
		sd1.setRkCrkQty(sd2==null?new BigDecimal(0):sd2.getRkCrkQty());
		sd1.setRkCyrQty(sd2==null?new BigDecimal(0):sd2.getRkCyrQty());
		sd1.setRkDthQty(sd2==null?new BigDecimal(0):sd2.getRkDthQty());
		sd1.setRkQtrkQty(sd2==null?new BigDecimal(0):sd2.getRkQtrkQty());
		sd1.setRkCytzQty(sd2==null?new BigDecimal(0):sd2.getRkCytzQty());
		
		//出库
		sd1.setCkCcdQty(sd2==null?new BigDecimal(0):sd2.getCkCcdQty());
		sd1.setCkCycQty(sd2==null?new BigDecimal(0):sd2.getCkCycQty());
		sd1.setCkQtckQty(sd2==null?new BigDecimal(0):sd2.getCkQtckQty());
		sd1.setCkKbmzhQty(sd2==null?new BigDecimal(0):sd2.getCkKbmzhQty());
		sd1.setCkCytzQty(sd2==null?new BigDecimal(0):sd2.getCkCytzQty());
		
		//盘点
		sd1.setPdPyQty(sd2==null?new BigDecimal(0):sd2.getPdPyQty());
		sd1.setPdPkQty(sd2==null?new BigDecimal(0):sd2.getPdPkQty());
		
		//预到货
		sd1.setYdhCrQty(sd2==null?new BigDecimal(0):sd2.getYdhCrQty());
		sd1.setYdhCrBoxQty(sd2==null?new BigDecimal(0):sd2.getYdhCrBoxQty());
		sd1.setYdhCyrQty(sd2==null?new BigDecimal(0):sd2.getYdhCyrQty());
		sd1.setYdhCyrBoxQty(sd2==null?new BigDecimal(0):sd2.getYdhCyrBoxQty());
		sd1.setYdhDtcQty(sd2==null?new BigDecimal(0):sd2.getYdhDtcQty());
		sd1.setYdhDtcBoxQty(sd2==null?new BigDecimal(0):sd2.getYdhDtcBoxQty());
		
		//已收未验
		sd1.setYswyCrQty(sd2==null?new BigDecimal(0):sd2.getYswyCrQty());
		sd1.setYswyCrBoxQty(sd2==null?new BigDecimal(0):sd2.getYswyCrBoxQty());
		sd1.setYswyCyrQty(sd2==null?new BigDecimal(0):sd2.getYswyCyrQty());
		sd1.setYswyCyrBoxQty(sd2==null?new BigDecimal(0):sd2.getYswyCyrBoxQty());
		sd1.setYswyDtcQty(sd2==null?new BigDecimal(0):sd2.getYswyDtcQty());
		sd1.setYswyDtcBoxQty(sd2==null?new BigDecimal(0):sd2.getYswyDtcBoxQty());
		
		//已收货
		sd1.setYshCrQty(sd2==null?new BigDecimal(0):sd2.getYshCrQty());
		sd1.setYshCyrQty(sd2==null?new BigDecimal(0):sd2.getYshCyrQty());
		sd1.setYshDtcQty(sd2==null?new BigDecimal(0):sd2.getYshDtcQty());
		sd1.setYshCrBoxQty(sd2==null?new BigDecimal(0):sd2.getYshCrBoxQty());
		sd1.setYshCyrBoxQty(sd2==null?new BigDecimal(0):sd2.getYshCyrBoxQty());
		sd1.setYshDtcBoxQty(sd2==null?new BigDecimal(0):sd2.getYshDtcBoxQty());
		
		//库存调整
		sd1.setTzKctzQty(sd2==null?new BigDecimal(0):sd2.getTzKctzQty());
		sd1.setTzKctzTypeQty(sd2==null?new BigDecimal(0):sd2.getTzKctzTypeQty());
		sd1.setTzKctzCalcQty(sd2==null?new BigDecimal(0):sd2.getTzKctzCalcQty());
	}
	
	public BigDecimal setIsNullValue(BigDecimal value){
		return value==null?new BigDecimal(0):value;
	}
	
}
