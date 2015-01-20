package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillAccControlMapper;
import com.yougou.logistics.city.dal.database.BillOmDivideBoxMapper;
import com.yougou.logistics.city.dal.database.BillOmLocateDtlMapper;

/**
 * 
 * TODO: 分货任务单Service实现
 * 
 * @author hou.hm
 * @date 2014-08-06 下午6:09:22
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDivideBoxService")
class BillOmDivideBoxServiceImpl extends BaseCrudServiceImpl implements BillOmDivideBoxService {

	private final static String RESULTY = "Y";

	@Resource
	private BillOmDivideBoxMapper billOmDivideBoxMapper;
	
	@Resource
	private BillOmLocateDtlMapper billOmLocateDtlMapper;
	
	@Resource
	private BillAccControlMapper billAccControlMapper;
	

	//用来保存批量新增分播单明细数据 
	private List<Map<String,Object>> divideDtls = new ArrayList<Map<String,Object>>();
   	//保存批量更新发货通知单数据
	private List<Map<String,Object>> expDtls = new ArrayList<Map<String,Object>>();
    //批量  回写收货单明细数量 
	private List<Map<String, Object>> updateBillImReceiptDtlByList=new ArrayList<Map<String, Object>>();
    //批量  更新箱码头档信息  
	private List<Map<String, Object>> updateConBoxByList=new ArrayList<Map<String, Object>>();
    //批量 更新箱码明细 
	private List<Map<String, Object>>  updateBoxDtlByList=new ArrayList<Map<String, Object>>();
   
//   Integer boxDisFlag = 0; //拼箱是否有分配过 0 没有 1 有
	
	//保存发货通知单  在有多张发货通知单的情况下使用
	LinkedHashSet<String> expNos = new LinkedHashSet<String>();
   
	
	@Override
	public BaseCrudMapper init() {
		return billOmDivideBoxMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> addBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ServiceException {

		if (!CommonUtil.hasValue(listBillOmDivide)) {
			throw new ServiceException("参数非法!");
		}

		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";

		try {

			String expNo = "";
			String checkExpNos = "";
			for (BillOmDivide b : listBillOmDivide) {
				if (StringUtils.isNotBlank(b.getReceiptNo())) {
					expNo += b.getExpNo() + ",";
					checkExpNos += "'" + b.getExpNo() + "',";
				}
			}
			
			//验证是否已经调度
			if (StringUtils.isNotBlank(checkExpNos)){
				checkExpNos = checkExpNos.substring(0, checkExpNos.length() - 1);
				String checkBusinessType = listBillOmDivide.get(0).getBusinessType();
				String locno = listBillOmDivide.get(0).getLocno();
				if ("1".equals(checkBusinessType)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("locno", locno);
					map.put("checkExpNos", checkExpNos);
					List<BillOmLocateDtl> locateDtlList = billOmLocateDtlMapper.selectLocateDtlByExpNo(map);
					if(CommonUtil.hasValue(locateDtlList)){
						mapObj.put("flag", "warn");
						mapObj.put("msg", locateDtlList.get(0).getExpNo()+"发货通知单已经做调度,不能分货！");
						return mapObj;
					}
				}
			}
			
			if (StringUtils.isNotBlank(expNo)) {
				expNo = expNo.substring(0, expNo.length() - 1);
				String divideNoS = "N";
				String flagValue = "1";
				if (expNo != null && !expNo.equals("")) {
					BillOmDivide b = listBillOmDivide.get(0);
					String receiptNo = b.getReceiptNo();
					//					String[] res = receiptNo.split(",");

					b.setSerialNo(receiptNo);
					b.setDivideNoS(divideNoS);
					b.setFlag(flagValue);
					b.setExpNo(expNo);
					b.setReceiptNo("12345");
					this.insertBillOmDivide(b);//改造存储过程

					divideNoS = b.getDivideNo();

					String strOutMsg = b.getStrOutMsg();
					if (!RESULTY.equals(strOutMsg)) {
						if (StringUtils.isNotBlank(strOutMsg)) {
							String[] msgs = strOutMsg.split("\\|");
							msg = msgs[1];
						}
						flag = "warn";
						throw new ServiceException(msg);
					}
				}
			} else {
				throw new ServiceException("无可操作发货单！");
			}

			flag = "success";
			msg = "创建成功!";
			mapObj.put("flag", flag);
			mapObj.put("msg", msg);

			return mapObj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(msg);
		}
	}
	
	private List<Map<String,Object>> expMapList;//发货单
	public List<Map<String, Object>> getExpMapList() {
		return expMapList;
	}
	
	private Map<String, Object> ruleMap;//规则
	public Map<String, Object> getRuleMap() {
		return ruleMap;
	}
	
	private List<Map<String, Object>>  storeGroupList=new ArrayList<Map<String,Object>>();//店分组集合
	//获取店分组数据
	public  List<Map<String, Object>>  getStoreGroupList(){
		return this.storeGroupList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void insertBillOmDivide(BillOmDivide divide) throws ServiceException,DaoException{
		String boxNo;       //箱号 
		BigDecimal cargoSort;  //分货排序
		BigDecimal boxSort;    //箱号排序
		String boxType;     //箱类型
		String divideNo = "";   //分货单号
		Integer boxOk;      //当前箱是否已经完成配货
		String tempNo;      //模板名称
		String businessType = "";//从箱取出来的  与 divide.getBusinessType不是同一个定义
			
		expNos.clear();
		Map<String, Object> paramRuleMap = new HashMap<String, Object>();
	    paramRuleMap.put("locNo", divide.getLocno());//
	    paramRuleMap.put("ruleNo", divide.getRuleNo());//
	
	    //获取按箱分货规规则
		List<Map<String, Object>> ruleList=billOmDivideBoxMapper.selectStoreRurle(paramRuleMap);
		if(ruleList==null ||ruleList.size()<1){
			divide.setStrOutMsg("N | 规则为空");
		}
	    ruleMap=(Map<String, Object>)ruleList.get(0);
		cargoSort  = (BigDecimal)ruleMap.get("CARGO_SORT");
		boxSort = (BigDecimal)ruleMap.get("BOX_SORT");
		boxType = ruleMap.get("BOX_TYPE").toString();
		tempNo  = ruleMap.get("TEMP_NO").toString();
		if(null == tempNo || "".equals(tempNo)){
			divide.setStrOutMsg("N | 规则模板为空");
		}
		
		//查询发货通知单（用于拆箱配货时的第一层循环）
	   	Map<String, Object> paramMap1 = new HashMap<String, Object>();
	   	paramMap1.put("locNo", divide.getLocno());
	   	paramMap1.put("expNo", divide.getExpNo().toString());
	    expMapList=billOmDivideBoxMapper.selectBillOmExpBoxCodeByMap(paramMap1);
	    
		divide.setStatus("10");//主表状态默认值
		
		if(null == divide.getDivideNoS() || "".equals(divide.getDivideNoS()) || "N".equals(divide.getDivideNoS())){
			Map<String, String> callmap = new HashMap<String, String>();
			callmap.put("strlocno", divide.getLocno());
			callmap.put("strPaperType", "DO");
			billOmDivideBoxMapper.getsheetNo(callmap);
			divideNo = callmap.get("csheetno").toString();//获取新的分货单号
			divide.setDivideNo(divideNo);
			//新增分货单主表
			billOmDivideBoxMapper.insertSelective(divide);
		}else{
			divide.setDivideNo(divide.getDivideNoS());//单号已经存在 应该是表示修改的意思
		}
		
		//开始门店分组
		if("1".equals(divide.getFlag())){
		    Map<String,Object> para = new HashMap<String,Object>();
 		    para.put("locNo", divide.getLocno());
 		    para.put("ownerNo", divide.getOwnerNo().toString());
 		    para.put("serialNo", divide.getSerialNo());
 		    para.put("receiptNo", divide.getReceiptNo());
 		    para.put("expNo", divide.getExpNo().toString());
 		    para.put("divideNo",divide.getDivideNo());
 		    para.put("ruleNo",divide.getRuleNo());
 		    this.getStoreGroupList().clear();
			Map<String, Object> storeGrp = this.revertStoreGrp(para,ruleMap);
//				groupName = storeGrp.get("groupType").toString();
			if(storeGrp.get("outMsg").toString().indexOf("N") != -1){
				divide.setStrOutMsg("N | 门店分组失败");
				return;
			}
		}
		
		//获取箱的信息
		List<Map<String,Object>> boxSortInfo = billOmDivideBoxMapper.getBoxSort(divide,boxSort.toString());
		//用来保存已经分货的箱信息
		List<Map<String,Object>> boxSort_ = new ArrayList<Map<String,Object>>();
		
		//获取门店信息
		List<Map<String,Object>> storeSortInfo =  billOmDivideBoxMapper.getStoreInfo(divide,cargoSort.toString());
		
		if(boxSortInfo.size() ==0){
			divide.setStrOutMsg("N | 收货单未匹配到任何出库订单");
			return;
		}
		
		//开始客户配货
		for(Map<String,Object> box_info : boxSortInfo){
		   boxNo =  box_info.get("BOX_NO").toString();
           businessType = box_info.get("BUSINESS_TYPE").toString();
           boxOk = 0;
           //开始门店配货
           LinkedHashSet<String> stores = new LinkedHashSet<String>();
           for(Iterator it = storeSortInfo.iterator(); it.hasNext(); ) {
        	   Map<String,Object>  rs = (Map<String, Object>) it.next();
        	   if(box_info.get("BOX_NO").toString().equals(rs.get("BOX_NO"))){
        		   stores.add(rs.get("STORE_NO").toString());
        	   }
           } 
           
           Map<String, Object> paramMap = new HashMap<String, Object>();
           paramMap.put("expNo", divide.getExpNo());
           paramMap.put("locNo", divide.getLocno());
           paramMap.put("boxNo", boxNo);
           paramMap.put("ownerNo", divide.getOwnerNo());
           //获取分货信息
           List<Map<String,Object>>  boxMapList = billOmDivideBoxMapper.selectBoxMStoreByMap(paramMap);
           if(boxMapList.size() <= 0){
        	   divide.setStrOutMsg("N | 获取分货信息失败");
			   return;
           }
           
//               List<Map<String,Object>>  boxMaplist_new = new ArrayList<Map<String,Object>>();
	       if("1".equals(boxType)){//独码优先（也就是整箱优先）
        	   for(String store_no : stores){
//            		   index = 1;//当前收货单有匹配到出库订单
        		   
        		   //匹配箱里面对应的门店的分货数据
	               List<Map<String,Object>> mdfhmx = new ArrayList<Map<String,Object>>();
	               for(Iterator it = boxMapList.iterator(); it.hasNext(); ) {
	            	   Map<String,Object>  rs = (Map<String, Object>) it.next();
	            	   int qty= Integer.parseInt(rs.get("QTY").toString());
       	  	           int divideQty=Integer.parseInt(rs.get("DIVIDE_QTY").toString());
       	  	           if(null==rs.get("ITEM_QTY")){
       	  	        		   continue;
       	  	           }
       	  	           int itemQty=Integer.parseInt(rs.get("ITEM_QTY").toString());
       	  	           if(store_no.equals(rs.get("STORE_NO"))  && ((qty - divideQty)> 0) && itemQty > 0){
	            		   //刷选门店过后的分货数据
	            		   Map<String,Object> m = new HashMap<String,Object>();
	            		   m.put("BOX_NO", rs.get("BOX_NO"));
	            		   m.put("BOX_ID", rs.get("BOX_ID"));
	            		   m.put("ITEM_NO", rs.get("ITEM_NO"));
	            		   m.put("SIZE_NO", rs.get("SIZE_NO"));
	            		   m.put("QTY", rs.get("QTY"));
	            		   m.put("DIVIDE_QTY", rs.get("DIVIDE_QTY"));
	            		   m.put("CQTY", rs.get("CQTY"));
	            		   m.put("ITEM_QTY", rs.get("ITEM_QTY"));
	            		   m.put("DISQTY", rs.get("DISQTY"));
	            		   m.put("CELL_NO", rs.get("CELL_NO"));
	            		   m.put("CELL_ID", rs.get("CELL_ID"));
	            		   m.put("BRAND_NO", rs.get("BRAND_NO"));
	            		   m.put("BUSINESS_TYPE", rs.get("BUSINESS_TYPE"));
	            		   m.put("EXP_TYPE", rs.get("EXP_TYPE"));
	            		   m.put("EXP_NO", rs.get("EXP_NO"));
	            		   m.put("STORE_NO", rs.get("STORE_NO"));
	            		   m.put("PACK_QTY", rs.get("PACK_QTY"));
	            		   m.put("BATCH_NO", rs.get("BATCH_NO"));
	            		   m.put("SCHEDULE_QTY", rs.get("SCHEDULE_QTY"));//下一箱可能会变
	            		   mdfhmx.add(m);
	            	   }
	               }
        		   
	             //整箱配门店 (false为拆箱 true为继续走整箱)
	               boolean isIntegral = false;
        		   
        		   
        		   Object[] mdfhmxs = mdfhmx.toArray();
	                
	                for(int i =0;i<mdfhmxs.length;i++){
	                    Map<String, Object> boxmap =( Map<String, Object> ) mdfhmxs[i];
        			    String serailNo;//
        			    int statusID=0;
        			    int nStatus ;
        			    int nCount=0;
        			    int nQty=0;
        			    String  strcellNo;
        			    int nCellId;
        			    
       					int disqty= Integer.parseInt(boxmap.get("DISQTY").toString());
       					int qty= Integer.parseInt(boxmap.get("QTY").toString());
       					int divideQty=Integer.parseInt(boxmap.get("DIVIDE_QTY").toString());
       					
       				  if(disqty<0){
       					  isIntegral=false;
       					  break;
       				  }else{
       					isIntegral=true;
       				  }
       				  nQty = qty-divideQty;
       				  
       				  if(nQty <= 0){
       	  	        	   break;
       	  	          }
       				  //  取当前分播单最大序列号
       				  serailNo = "";
       				  //根据分货单号、门店找流道编号
       					Map<String, Object> paramMap_serail = new HashMap<String, Object>();
       					paramMap_serail.put("storeNo", store_no);
       					paramMap_serail.put("divideNo", divideNo);
       			
       					serailNo=""+billOmDivideBoxMapper.selectStoreNoSerialNo(paramMap_serail);
       					if("".equals(serailNo) ||"0".equals(serailNo)){
	       					divide.setStrOutMsg("N | 获取流道号失败，请联系管理员！");
       						return;
       					}
       					String businessType_box=(String)boxmap.get("BUSINESS_TYPE");
       					if("1".equals(businessType_box)){
       						strcellNo=(String)boxmap.get("CELL_NO");
       						nCellId=Integer.parseInt(boxmap.get("CELL_ID").toString());
       					}else{
       						strcellNo=box_info.get("CELL_NO").toString();
       						nCellId=-1;
       					}
       							
       					//批量写分播单
       					Map<String, Object> paramMap_Divide = new HashMap<String, Object>();
       					paramMap_Divide.put("locNo", divide.getLocno());
       					paramMap_Divide.put("ownerNo", divide.getOwnerNo());
       					paramMap_Divide.put("batchNo", boxmap.get("BATCH_NO"));
       					paramMap_Divide.put("receiptNo", box_info.get("RECEIPT_NO").toString());
       					paramMap_Divide.put("divideNo", divideNo);
       					paramMap_Divide.put("storeNo", boxmap.get("STORE_NO"));
       					paramMap_Divide.put("expType", boxmap.get("EXP_TYPE"));
       					paramMap_Divide.put("expNo", boxmap.get("EXP_NO"));
       					paramMap_Divide.put("cellNO", strcellNo);
       					paramMap_Divide.put("nCellId", nCellId);
       					paramMap_Divide.put("itemNo", boxmap.get("ITEM_NO"));
       					paramMap_Divide.put("sizeNo", boxmap.get("SIZE_NO"));
       					paramMap_Divide.put("packQty", boxmap.get("PACK_QTY"));
       					paramMap_Divide.put("nQty", nQty);//分配数量
       					paramMap_Divide.put("lineNo", boxmap.get("LINE_NO"));
       					paramMap_Divide.put("userId", divide.getCreator());
       					paramMap_Divide.put("userName", divide.getCreatorname());
       					paramMap_Divide.put("serailNo", serailNo);
       					paramMap_Divide.put("brandNo", boxmap.get("BRAND_NO"));
       					paramMap_Divide.put("boxNO", boxmap.get("BOX_NO"));
       					divideDtls.add(paramMap_Divide); 
       					
	       				//单据跟踪中是否有部分分配的状态       
	       				nStatus =0;
	       				Map<String, Object> paramMap4 = new HashMap<String, Object>();
	       				paramMap4.put("locNo", divide.getLocno());
	       				paramMap4.put("expNo", boxmap.get("EXP_NO"));
	       				nStatus=billOmDivideBoxMapper.selectBillStatusCount(paramMap4);  
	       		       if(nStatus<=0){
	       		    	   //获取状态中单据最大序列
	       		    		Map<String, Object> paramMap5 = new HashMap<String, Object>();
	       			   		paramMap5.put("locNo", divide.getLocno());
	       			   		paramMap5.put("expNo", boxmap.get("EXP_NO"));
	       		   		
		       		   		statusID=billOmDivideBoxMapper.selectBillStatusMax(paramMap5);
		       		   		statusID=statusID+1;
		       		   		//写单据状态跟踪表
		       		   		Map<String, Object> paramMap6 = new HashMap<String, Object>();
		       		   		paramMap6.put("locNo", divide.getLocno());
		       		   		paramMap6.put("expNo", boxmap.get("EXP_NO"));
		       		   		paramMap6.put("billType", "OM");
		       		   		paramMap6.put("statusID", statusID);
		       		   		paramMap6.put("status", "12");
		       		   		paramMap6.put("description", "更新发货通知单状态为部分分货");
		       		   		paramMap6.put("userId", divide.getCreator());
		       		   		
		       		   		billOmDivideBoxMapper.insertBillStatusLogByMap(paramMap6);
	       		       }
       		       
	       		       //批量更新出货单明细
	       		  		Map<String, Object> paramMap_exp = new HashMap<String, Object>();
		       		  	paramMap_exp.put("nQty", nQty);
		       		  	paramMap_exp.put("locNo",  divide.getLocno());
			       		paramMap_exp.put("ownerNo",  divide.getOwnerNo());
			       		paramMap_exp.put("itemNo", boxmap.get("ITEM_NO"));
			       		paramMap_exp.put("sizeNo", boxmap.get("SIZE_NO"));
			       		paramMap_exp.put("expNo", boxmap.get("EXP_NO"));
			       		paramMap_exp.put("storeNo", boxmap.get("STORE_NO"));
	       		  		expDtls.add(paramMap_exp);
	       		  		expNos.add(boxmap.get("EXP_NO").toString());
	       		  		
		       		  	for(int j=0;j<mdfhmxs.length;j++){
	            			 Map<String,Object> m = ( Map<String, Object> )mdfhmxs[j];
	            			 if(m.get("BOX_NO").equals(boxmap.get("BOX_NO")) && m.get("ITEM_NO").equals(boxmap.get("ITEM_NO")) && m.get("SIZE_NO").equals(boxmap.get("SIZE_NO"))){
		            			 int rs_qty = Integer.parseInt(m.get("QTY").toString());//修改这个箱这个尺码的剩余数量
		            			 m.put("QTY", rs_qty-nQty);
		            			 mdfhmxs[j] = m;
	            			 }
	           			}
	       		  		
//		       		  	    //改变数组里面的数量
	       		  		for(int l = 0;l<boxMapList.size();l++){	
		            	   Map<String,Object>  rs =  boxMapList.get(l);
		            	   if(rs.get("BOX_NO").equals(boxmap.get("BOX_NO")) && rs.get("ITEM_NO").equals(boxmap.get("ITEM_NO")) && rs.get("SIZE_NO").equals(boxmap.get("SIZE_NO"))){
		            		   int rs_qty = Integer.parseInt(rs.get("QTY").toString());//修改这个尺码的剩余数量
		            		   rs.put("QTY", rs_qty-nQty);
		            	   }
		            	   if(null != rs.get("STORE_NO") && rs.get("STORE_NO").equals(boxmap.get("STORE_NO")) && rs.get("BOX_NO").equals(boxmap.get("BOX_NO")) && rs.get("ITEM_NO").equals(boxmap.get("ITEM_NO")) && rs.get("SIZE_NO").equals(boxmap.get("SIZE_NO"))){
		            		   rs.put("DIVIDE_QTY", nQty);
		            	   }
		                }
		       		  	
		                //更新箱码明细
		              	Map<String, Object> paramMap12 = new HashMap<String, Object>();
		       	  		paramMap12.put("nQty", nQty);
		       			paramMap12.put("locNo", divide.getLocno());
		       	  		paramMap12.put("ownerNo", divide.getOwnerNo());
		       	  		paramMap12.put("boxNo", boxmap.get("BOX_NO"));
		       	  		paramMap12.put("itemNo", boxmap.get("ITEM_NO"));
		       	  		paramMap12.put("sizeNo", boxmap.get("SIZE_NO"));
		       	  		paramMap12.put("boxId", boxmap.get("BOX_ID"));
		       	  		paramMap12.put("storeNo", boxmap.get("STORE_NO"));
		       	  		updateBoxDtlByList.add(paramMap12);
		       	  		
		       	        //更新箱码头档信息
		             	Map<String, Object> paramMap13 = new HashMap<String, Object>();
		       	  		paramMap13.put("nQty", nQty);
		       			paramMap13.put("locNo", divide.getLocno());
		       	  		paramMap13.put("ownerNo", divide.getOwnerNo());
		       	  		paramMap13.put("boxNo", boxmap.get("BOX_NO"));
		       	  		updateConBoxByList.add(paramMap13);
		       	  		
		                //回写收货单明细数量
		             	Map<String, Object> paramMap14 = new HashMap<String, Object>();
		       	  		paramMap14.put("nQty", nQty);
		       			paramMap14.put("locNo", divide.getLocno());
		       	  		paramMap14.put("ownerNo", divide.getOwnerNo());
		       	  		paramMap14.put("receiptNo", box_info.get("RECEIPT_NO").toString());
		       	  		paramMap14.put("boxNo", boxmap.get("BOX_NO"));
		       	  		paramMap14.put("itemNo", boxmap.get("ITEM_NO"));
		       	  		paramMap14.put("sizeNo", boxmap.get("SIZE_NO"));
		       		    updateBillImReceiptDtlByList.add(paramMap14);
        		   }
        	   }
    		   batchUpdateList();//统一批量提交
	       }
    	   
	       //拆箱配货
           if(boxOk == 0){
        	   boxDivideCX(divide, divideNo, paramMap, stores, storeSortInfo, box_info);
        	   batchUpdateList();//统一批量提交
           }

		   	Map<String,Object> para_box = new HashMap<String,Object>();
			para_box.put("locNo", divide.getLocno());
			para_box.put("ownerNo", divide.getOwnerNo().toString());
			para_box.put("divideNo", divide.getDivideNo());
			para_box.put("boxNo", box_info.get("BOX_NO"));
			para_box.put("ruleNo", divide.getRuleNo());
			boxSort_.add(para_box);
		
			//处理收货单状态
			if("0".equals(divide.getBusinessType())){//区分到货分货还是存储分货
        	   if("1".equals(businessType)){
        		   int rt = billOmDivideBoxMapper.revertUpdtReceipt(divide.getLocno().toString(),box_info.get("RECEIPT_NO").toString());
        		   if(rt <= 0){
        			   divide.setStrOutMsg("N | (整箱配门店)分货失败");
        			   return;
        		   }
               }
			}
			
			//出货单明细是否全部分货完成 //多个发货通知单处理
			for(String expNo_item : expNos){
           		Map<String, Object> paramMap8 = new HashMap<String, Object>();
    			paramMap8.put("locNo", divide.getLocno());
    			paramMap8.put("ownerNo", divide.getOwnerNo());
    			paramMap8.put("expNo", expNo_item);
    			paramMap8.put("userId",divide.getCreator());
    			int rs_update = billOmDivideBoxMapper.updateOmExpStatusByMap(paramMap8);
    			if(rs_update>0){
    				Map<String, Object> paramMap10 = new HashMap<String, Object>();
               		paramMap10.put("locNo", divide.getLocno());
               		paramMap10.put("expNo", expNo_item);
               	
                	 int nCount=billOmDivideBoxMapper.selectBillOmExpCountByMap(paramMap10);
                	 if(nCount>0){//出货单已经全部分配完成 
    					//获取状态中单据最大序列
    			 		int statusID=billOmDivideBoxMapper.selectBillStatusMax(paramMap10);
    			           statusID = statusID+1;
    			           //写单据状态跟踪表
    			   		Map<String, Object> paramMap11 = new HashMap<String, Object>();
    			   		paramMap11.put("locNo", divide.getLocno());
    			   		paramMap11.put("expNo", expNo_item);
    			   		paramMap11.put("billType", "OM");
    			   		paramMap11.put("statusID", statusID);
    			   		paramMap11.put("status", "15");
    			   		paramMap11.put("description", "更新发货通知单状态为分货完成");
    			   		paramMap11.put("userId", divide.getCreator());
    				   	billOmDivideBoxMapper.insertBillStatusLogByMap(paramMap11);
                	 }
    			}
           	}
			expNos.clear();
		}
		//分货完成处理
		int rnum = finishedGoods(divide, boxSort_, businessType, divideNo);
		if(rnum > 0){
//				long endTime=System.currentTimeMillis(); //获取结束时间
//				System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
			divide.setStrOutMsg(RESULTY);
		}
	}
	
	/**
	 * 按箱分货 ----店分组  按门店数 分组   入口
	 * @param params 参数
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> revertStoreGrp(Map<String, Object> params,Map<String, Object> ruleMap) throws ServiceException{
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";
	    //获取传入的参数
	    String locNo=(String)params.get("locNo");//仓别
	    String ownerNo=(String)params.get("ownerNo");//委托业主
//	    String serialNo=(String)params.get("serialNo");//--收货单号【一次性批量传入】( 单据编号序列 )
	    String receiptNo=(String)params.get("receiptNo");//收货单号
	    String expNo=(String)params.get("expNo");//发货通知单
	    String divideNo=(String)params.get("divideNo");//分货单号
	    String ruleNo =(String)params.get("ruleNo");//按箱分货规则代号
	    String storeNo=(String)params.get("storeNo");//店编号 //参数基本上没有用
	    String groupType="";//分组类型  out 
	    String outMsg="N|[E00025]";//输出参数 out 
		 //定义参数
	     //店规则
	    String storeBasic="";//店分组依据
	    int nStoreSort=0;//店排序规则
	    String tempNo="";//模板名称
	    int nRowId=0;
	    int nGroupCount=0;//分组个数
	    int nGroupA=0;//组别
		try{
			//检查当前分货单号是否已做店分组
			Map<String, Object> paramMap1 = new HashMap<String, Object>();
	 	     paramMap1.put("divideNo", divideNo);//分货单号
	 	    nGroupCount=billOmDivideBoxMapper.selectStorGroupCount(paramMap1);
	 	    if(nGroupCount==0){
	 	    	billOmDivideBoxMapper.deleteStoreGroupByParam(paramMap1);
	 	    }
	 	    //删除了 按照店查询分组代码
			//取值
			storeBasic=(String)ruleMap.get("STORE_BASIC");//店分组依据
			nStoreSort=((BigDecimal)ruleMap.get("STORE_SORT")).intValue();//店排序规则
		    nGroupA=((BigDecimal)ruleMap.get("GROUP_A")).intValue();
		    tempNo=(String)ruleMap.get("TEMP_NO");//模板编号
		    nRowId=0;
		   if("001".equals(tempNo)){
			//按门店预发货量  按发货量   
			 //检查规则设置是否有效
			   if(nGroupA<=0){
				   outMsg="N|按箱分货规则设置无效,请检查！";
				   return mapObj;
			   }
			   if(nStoreSort==1){
				   //大到小
				   //v_cursor_get_store_001A
				   Map<String, Object> paramStoreMapA = new HashMap<String, Object>();
				   paramStoreMapA.put("locNo", locNo);//仓库类别
				   paramStoreMapA.put("ownerNo", ownerNo);//委托业主
				   paramStoreMapA.put("expNo", expNo);//发货通知单
				   paramStoreMapA.put("sortType", "desc");//排序方式
				   
				   List<Map<String, Object>>  listtemp=billOmDivideBoxMapper.selectStoreSendNumByParam(paramStoreMapA);

				   Map<String, Object> paramStoreQtyMapA = new HashMap<String, Object>();
				   for(Map<String, Object> storeMap:listtemp){
					   
					   nRowId=nRowId+1;
					   paramStoreQtyMapA.put("locNo", locNo);//仓别
					   paramStoreQtyMapA.put("sumQTy", ((BigDecimal)storeMap.get("SUMQTY")).intValue());//预发货量
					   paramStoreQtyMapA.put("storeSort", nStoreSort);//排序方式
					   paramStoreQtyMapA.put("ruleNo", ruleNo);//按箱分货规则代号
					   paramStoreQtyMapA.put("storeNo", (String)storeMap.get("STORE_NO"));//店编号
					   paramStoreQtyMapA.put("divideNo", divideNo);//分货单号
					   paramStoreQtyMapA.put("receiptNo", receiptNo);//收货单号
					   
					  // log20140416 modi by chenhaitao 增加仓别参数
					   Map<String, Object>  resultStoreQtyMapA= this.revertStoreByQty(paramStoreQtyMapA,ruleMap); 
				   }
			   }else{
				   //小到大
				   //v_cursor_get_store_001B
				   Map<String, Object> paramStoreMapB = new HashMap<String, Object>();
				   paramStoreMapB.put("locNo", locNo);//仓库类别
				   paramStoreMapB.put("ownerNo", ownerNo);//委托业主
				   paramStoreMapB.put("expNo", expNo);//发货通知单
				  // paramStoreMap.put("sortType", "desc");//排序方式
				   List<Map<String, Object>>  listtemp=billOmDivideBoxMapper.selectStoreSendNumByParam(paramStoreMapB);
				   // log20140416 modi by chenhaitao 增加仓别参数
				   Map<String, Object> paramStoreQtyMapB = new HashMap<String, Object>();
				   for(Map<String, Object> storeMap:listtemp){
					   
					   nRowId=nRowId+1;
					   paramStoreQtyMapB.put("locNo", locNo);//仓别
					   paramStoreQtyMapB.put("sumQTy", ((BigDecimal)storeMap.get("SUMQTY")).intValue());//预发货量
					   paramStoreQtyMapB.put("storeSort", nStoreSort);//排序方式
					   paramStoreQtyMapB.put("ruleNo", ruleNo);//按箱分货规则代号
					   paramStoreQtyMapB.put("storeNo", (String)storeMap.get("STORE_NO"));//店编号
					   paramStoreQtyMapB.put("divideNo", divideNo);//分货单号
					   paramStoreQtyMapB.put("receiptNo", receiptNo);//收货单号
					  // log20140416 modi by chenhaitao 增加仓别参数
					   Map<String, Object>  resultStoreQtyMapB= this.revertStoreByQty(paramStoreQtyMapB,ruleMap);
				   }
   
			   }

		   }else if("003".equals(tempNo)){
			 //  按预发货量 按门店个数
			 //检查规则设置是否有效
			   if(nGroupA<=0){
				   outMsg="N|按箱分货规则设置无效,请检查！";
				   return mapObj;
			   }
			   
			   if("1".equals(storeBasic) && nStoreSort==1){
				   //预发货量 大到小 按门店数
				   //v_cursor_get_store_001A
				   Map<String, Object> paramStoreMapA = new HashMap<String, Object>();
				   paramStoreMapA.put("locNo", locNo);//仓库类别
				   paramStoreMapA.put("ownerNo", ownerNo);//委托业主
				   paramStoreMapA.put("expNo", expNo);//发货通知单
				   paramStoreMapA.put("sortType", "desc");//排序方式
				   List<Map<String, Object>>  listtemp=billOmDivideBoxMapper.selectStoreSendNumByParam(paramStoreMapA);
				   
				   Map<String, Object> paramStoreCountMapA = new HashMap<String, Object>();
			
				   //log20140416 modi by chenhaitao 增加仓别
                     for(Map<String, Object> storeMap:listtemp){
                    	   nRowId=nRowId+1;
                    	   paramStoreCountMapA.put("locNo", locNo);//仓别
                    	   paramStoreCountMapA.put("rowNum", ((BigDecimal)storeMap.get("ROWNUM")).intValue());//预发货量
                    	   //未启动2个参数
                    	   paramStoreCountMapA.put("storeBasic", nStoreSort);//店分组依据
                    	   paramStoreCountMapA.put("storeSort", nStoreSort);//排序方式
                    	   
                    	   paramStoreCountMapA.put("ruleNo", ruleNo);//按箱分货规则代号
                    	   paramStoreCountMapA.put("storeNo", (String)storeMap.get("STORE_NO"));//店编号
                    	   paramStoreCountMapA.put("divideNo", divideNo);//分货单号
                    	   paramStoreCountMapA.put("receiptNo", receiptNo);//收货单号
                    	   Map<String, Object>  resultStoreCountMapA= this.revertStoreByCount(paramStoreCountMapA,ruleMap);
                     }
				   
			   }else if("1".equals(storeBasic) && nStoreSort==2){
				   //预发货量 小到大 按门店数
				 //v_cursor_get_store_001B
				   Map<String, Object> paramStoreMapB = new HashMap<String, Object>();
				   paramStoreMapB.put("locNo", locNo);//仓库类别
				   paramStoreMapB.put("ownerNo", ownerNo);//委托业主
				   paramStoreMapB.put("expNo", expNo);//发货通知单
				   //paramStoreMapA.put("sortType", "desc");//排序方式
				   List<Map<String, Object>>  listtemp=billOmDivideBoxMapper.selectStoreSendNumByParam(paramStoreMapB);
				   
				   Map<String, Object> paramStoreCountMapB = new HashMap<String, Object>();
			
				   //log20140416 modi by chenhaitao 增加仓别
                     for(Map<String, Object> storeMap:listtemp){
                    	   nRowId=nRowId+1;
                    	   paramStoreCountMapB.put("locNo", locNo);//仓别
                    	   paramStoreCountMapB.put("rowNum", ((BigDecimal)storeMap.get("ROWNUM")).intValue());//预发货量
                    	   //未启动2个参数
                    	   paramStoreCountMapB.put("storeBasic", nStoreSort);//店分组依据
                    	   paramStoreCountMapB.put("storeSort", nStoreSort);//排序方式
                    	   
                    	   paramStoreCountMapB.put("ruleNo", ruleNo);//按箱分货规则代号
                    	   paramStoreCountMapB.put("storeNo", (String)storeMap.get("STORE_NO"));//店编号
                    	   paramStoreCountMapB.put("divideNo", divideNo);//分货单号
                    	   paramStoreCountMapB.put("receiptNo", receiptNo);//收货单号
                    	   Map<String, Object>  resultStoreCountMapA= this.revertStoreByCount(paramStoreCountMapB,ruleMap);
                     }
			   }else if("2".equals(storeBasic) && nStoreSort==1){
				   //按门店降序  按门店数
				   //v_cursor_get_store_003A
				   Map<String, Object> paramStoreNOMapA = new HashMap<String, Object>();
				   paramStoreNOMapA.put("locNo", locNo);//仓库类别
				   paramStoreNOMapA.put("ownerNo", ownerNo);//委托业主
				   paramStoreNOMapA.put("expNo", expNo);//发货通知单
				   paramStoreNOMapA.put("sortType", "desc");//排序方式
				   List<Map<String, Object>>  listtemp=billOmDivideBoxMapper.selectStoreStoreNoByParam(paramStoreNOMapA);
				   Map<String, Object> paramStoreCountMapA = new HashMap<String, Object>();
					
				   //log20140416 modi by chenhaitao 增加仓别
                     for(Map<String, Object> storeMap:listtemp){
                  	   nRowId=nRowId+1;
                	   paramStoreCountMapA.put("locNo", locNo);//仓别
                	   paramStoreCountMapA.put("rowNum", ((BigDecimal)storeMap.get("ROWNUM")).intValue());//预发货量
                	   //未启动2个参数
                	   paramStoreCountMapA.put("storeBasic", nStoreSort);//店分组依据
                	   paramStoreCountMapA.put("storeSort", nStoreSort);//排序方式
                	   paramStoreCountMapA.put("ruleNo", ruleNo);//按箱分货规则代号
                	   paramStoreCountMapA.put("storeNo", (String)storeMap.get("STORE_NO"));//店编号
                	   paramStoreCountMapA.put("divideNo", divideNo);//分货单号
                	   paramStoreCountMapA.put("receiptNo", receiptNo);//收货单号
                	   Map<String, Object>  resultStoreCountMapA= this.revertStoreByCount(paramStoreCountMapA,ruleMap);
                	   outMsg=(String)resultStoreCountMapA.get("outMsg");
                	   if("N".equals(outMsg.substring(0, 1))){
                		   
                		  return mapObj;
                	   }
                     }
			   }else /**if("2".equals(storeBasic) && nStoreSort==2)**/{
				   //按门店升序  按门店数
				   //v_cursor_get_store_003B
				   Map<String, Object> paramStoreNOMapB = new HashMap<String, Object>();
				   paramStoreNOMapB.put("locNo", locNo);//仓库类别
				   paramStoreNOMapB.put("ownerNo", ownerNo);//委托业主
				   paramStoreNOMapB.put("expNo", expNo);//发货通知单
				   //paramStoreNOMapA.put("sortType", "desc");//排序方式
				   List<Map<String, Object>>  listtemp=billOmDivideBoxMapper.selectStoreStoreNoByParam(paramStoreNOMapB);
				   Map<String, Object> paramStoreCountMapB = new HashMap<String, Object>();
					
				   //log20140416 modi by chenhaitao 增加仓别
                     for(Map<String, Object> storeMap:listtemp){
                  	   nRowId=nRowId+1;
                	   paramStoreCountMapB.put("locNo", locNo);//仓别
                	   paramStoreCountMapB.put("rowNum", ((BigDecimal)storeMap.get("ROWNUM")).intValue());//预发货量
                	   //未启动2个参数
                	   paramStoreCountMapB.put("storeBasic", nStoreSort);//店分组依据
                	   paramStoreCountMapB.put("storeSort", nStoreSort);//排序方式
                	   
                	   paramStoreCountMapB.put("ruleNo", ruleNo);//按箱分货规则代号
                	   paramStoreCountMapB.put("storeNo", (String)storeMap.get("STORE_NO"));//店编号
                	   paramStoreCountMapB.put("divideNo", divideNo);//分货单号
                	   paramStoreCountMapB.put("receiptNo", receiptNo);//收货单号
                	   Map<String, Object>  resultStoreCountMapA= this.revertStoreByCount(paramStoreCountMapB,ruleMap);
                	   outMsg=(String)resultStoreCountMapA.get("outMsg");
                	   if("N".equals(outMsg.substring(0, 1))){
                		   return mapObj;
                	   }
                     }
			   }
			   
		   }else{
			   outMsg="N|店分组失败,规则模板不存在,请配置！";
			   return mapObj ;
			 
		   }

		   if(nRowId==0){
			   outMsg=" N|店分组失败,收货单发货单未匹配到数据";
			   return mapObj ;
		   }
	 	    //返回当前门店所属组

		   if(null  !=storeNo && !"".equals(storeNo.trim())){
			   Map<String, Object> paramGroupTypeMap= new HashMap<String, Object>();
			   paramGroupTypeMap.put("storeNo", storeNo);//店编号
			   paramGroupTypeMap.put("divideNo", divideNo);//分货单号
			   paramGroupTypeMap.put("receiptNo", receiptNo);//收货单号
			   List<Map<String, Object>>  listtemp=  billOmDivideBoxMapper.selectStorGroupByParam(paramGroupTypeMap);
			   if(listtemp !=null &&listtemp.size()>0){
				   Map<String, Object> gtypeMap=listtemp.get(0);
				   groupType=(String)gtypeMap.get("GROUP_NAME");
			   }else{
				   outMsg = "N|根据分货单号、客户获取分组名称失败";
				   return mapObj ;
			   }
			
		   }
		   
		   // //批量插入店分组
		   billOmDivideBoxMapper.batchInsertStroreGroup(this.getStoreGroupList());

		   flag = "success";
		   msg="OK";
		   outMsg="Y";
	    } catch (DaoException e) {
			msg=e.getMessage();
			throw new ServiceException(e);
		}finally{
			mapObj.put("flag", flag);
			mapObj.put("msg", msg);
			mapObj.put("outMsg", outMsg);
			mapObj.put("groupType", groupType);
		}
		return mapObj;
		
	}
	
	/**
	 * 按箱分货 ----店分组  按门店数 分组   count
	 * @param params 参数
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> revertStoreByCount(Map<String, Object> params,Map<String, Object> ruleMap) throws ServiceException{
		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";

	    String locNo=(String)params.get("locNo");//仓别
	    int  rowNum=(Integer)params.get("rowNum");// in ,--预发货量

	    String ruleNo =(String)params.get("ruleNo");//按箱分货规则代号
	    String storeNo=(String)params.get("storeNo");//店编号
	    String divideNo=(String)params.get("divideNo");//分货单号
	    String receiptNo=(String)params.get("receiptNo");//收货单号
	    String groupName="";//分组名
	    String outMsg="N|[E00025]";//输出参数
	   //定义参数
	    int nGroupA=0;//组别
	    int nGroupB=0;//组别
	    int nGroupC=0;//组别
	    int nGroupD=0;//组别
	    int nGroupE=0;//组别
	    int nGroupF=0;//组别
	    int nGroupG=0;//组别
	    int nGroupH=0;//组别
	    int nGroupI=0;//组别
	    int nGroupJ=0;//组别
	    
	    int nCount=0;//当前流道号
	    int serialNo=0;//流道编号
	   
	    try {
	    	 Map<String, Object> paramMap1 = new HashMap<String, Object>();
	 	     paramMap1.put("divideNo", divideNo);//分货单号
	 	     paramMap1.put("receiptNo", receiptNo);//收货单号
	    	//1、获取当前最大流道ID
			nCount=storeGroupList.size();
			// Count ==???
			if(nCount>0){
				//Count ==??? 获取最大流道ID
				serialNo=Integer.parseInt(((Map)this.storeGroupList.get(nCount-1)).get("serialNo").toString());  	
			}
			serialNo=serialNo+1;
			//取值
		    nGroupA=((BigDecimal)ruleMap.get("GROUP_A")).intValue();
		    nGroupB=((BigDecimal)ruleMap.get("GROUP_B")).intValue();
		    nGroupC=((BigDecimal)ruleMap.get("GROUP_C")).intValue();
		    nGroupD=((BigDecimal)ruleMap.get("GROUP_D")).intValue();
		    nGroupE=((BigDecimal)ruleMap.get("GROUP_E")).intValue();
		    nGroupF=((BigDecimal)ruleMap.get("GROUP_F")).intValue();
		    nGroupG=((BigDecimal)ruleMap.get("GROUP_G")).intValue();
		    nGroupH=((BigDecimal)ruleMap.get("GROUP_H")).intValue();
		    nGroupI=((BigDecimal)ruleMap.get("GROUP_I")).intValue();
		    nGroupJ=((BigDecimal)ruleMap.get("GROUP_J")).intValue();
			
		    //判断是否属于哪个组
		    if(rowNum <= nGroupA){
		    	groupName="A";
		    }else if(rowNum > nGroupA && rowNum <= nGroupB){
		    	groupName="B";
		    }else if(rowNum > nGroupB && rowNum <= nGroupC){
		    	groupName="C";
		    }else if(rowNum > nGroupC && rowNum <= nGroupD){
		    	groupName="C";
		    }else if(rowNum > nGroupD && rowNum <= nGroupE){
		    	groupName="D";
		    }else if(rowNum > nGroupE && rowNum <= nGroupF){
		    	groupName="E";
		    }else if(rowNum > nGroupF && rowNum <= nGroupG){
		    	groupName="F";
		    }else if(rowNum > nGroupG && rowNum <= nGroupH){
		    	groupName="G";
		    }else if(rowNum > nGroupH && rowNum <= nGroupI){
		    	groupName="H";
		    }else if(rowNum > nGroupI && rowNum <= nGroupJ){
		    	groupName="I";
		    }else {
		    	groupName="J";
		    }
		    //新增门店分组临时表
		    Map<String, Object> storeMap=new  HashMap<String, Object>();
		    storeMap.put("storeNo", storeNo);//店编号
		    storeMap.put("groupName", groupName);//分组编号A B C
		    storeMap.put("divideNo", divideNo);//分货单号
		    storeMap.put("receiptNo", receiptNo);//收货单号
		    storeMap.put("serialNo", serialNo);//流道号
		    storeMap.put("ruleNo", ruleNo);//模板编号

		    //新增到店分组的数据里List<Map>
		    this.storeGroupList.add(storeMap);//
		} catch (Exception e) {
			outMsg="N|新增门店分组临时表失败！";
			msg=e.getMessage()+"N|新增门店分组临时表失败！";
			throw new ServiceException(e);
		}finally{
			mapObj.put("flag", flag);
			mapObj.put("msg", msg);
			mapObj.put("outMsg", outMsg);
		}
		
			return mapObj;
	
	}
	
	/**
	 * 按箱分货 ----店分组  按门店数 分组   Qty
	 * @param params 参数
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> revertStoreByQty(Map<String, Object> params,Map<String, Object> ruleMap) throws ServiceException{
		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";
		
	    //获取传入的参数

	    String locNo=(String)params.get("locNo");//仓别
	    int  sumQTy=(Integer)params.get("sumQTy");// 预发货量
	    int storeSort=(Integer)params.get("storeSort");//排序方式
	    String ruleNo =(String)params.get("ruleNo");//按箱分货规则代号
	    String storeNo=(String)params.get("storeNo");//店编号
	    String divideNo=(String)params.get("divideNo");//分货单号
	    String receiptNo=(String)params.get("receiptNo");//收货单号
	    String groupName="";//分组名
	    String outMsg="N|[E00025]";//输出参数
		   //定义参数
	    int nGroupA=0;//组别
	    int nGroupB=0;//组别
	    int nGroupC=0;//组别
	    int nGroupD=0;//组别
	    int nGroupE=0;//组别
	    int nGroupF=0;//组别
	    int nGroupG=0;//组别
	    int nGroupH=0;//组别
	    int nGroupI=0;//组别
	    int nGroupJ=0;//组别
	    
	    int nCount=0;//当前流道号
	    int serialNo=0;//流道编号
	    
	    try {
	    	 Map<String, Object> paramMap1 = new HashMap<String, Object>();
	 	     paramMap1.put("divideNo", divideNo);//分货单号
	 	     paramMap1.put("receiptNo", receiptNo);//收货单号
	    	//1、获取当前最大流道ID  --???
			nCount=storeGroupList.size();
			if(nCount>0){
				//--???
				serialNo=Integer.parseInt(((Map)this.storeGroupList.get(nCount-1)).get("serialNo").toString()); 
				
			}
			serialNo=serialNo+1;
			//取值
		    nGroupA=((BigDecimal)ruleMap.get("GROUP_A")).intValue();
		    nGroupB=((BigDecimal)ruleMap.get("GROUP_B")).intValue();
		    nGroupC=((BigDecimal)ruleMap.get("GROUP_C")).intValue();
		    nGroupD=((BigDecimal)ruleMap.get("GROUP_D")).intValue();
		    nGroupE=((BigDecimal)ruleMap.get("GROUP_E")).intValue();
		    nGroupF=((BigDecimal)ruleMap.get("GROUP_F")).intValue();
		    nGroupG=((BigDecimal)ruleMap.get("GROUP_G")).intValue();
		    nGroupH=((BigDecimal)ruleMap.get("GROUP_H")).intValue();
		    nGroupI=((BigDecimal)ruleMap.get("GROUP_I")).intValue();
		    nGroupJ=((BigDecimal)ruleMap.get("GROUP_J")).intValue();
		    
		    //店排序规则 1 大到小  2 小到大
		    if(storeSort==1){
		    	if(sumQTy >= nGroupA){
		    		groupName="A";
		    	}else if(sumQTy < nGroupA && sumQTy >= nGroupB){
			    	groupName="B";
			    }else if(sumQTy < nGroupB && sumQTy >= nGroupC){
			    	groupName="C";
			    }else if(sumQTy < nGroupC && sumQTy >= nGroupD){
			    	groupName="D";
			    }else if(sumQTy < nGroupD && sumQTy >= nGroupE){
			    	groupName="E";
			    }else if(sumQTy < nGroupE && sumQTy >= nGroupF){
			    	groupName="F";
			    }else if(sumQTy < nGroupF && sumQTy >= nGroupG){
			    	groupName="G";
			    }else if(sumQTy < nGroupG && sumQTy >= nGroupH){
			    	groupName="H";
			    }else if(sumQTy < nGroupH && sumQTy >= nGroupI){
			    	groupName="I";
			    }else {
			    	groupName="J";
			    }
		    	
		    }else{
		        if(sumQTy <= nGroupA){
			    	groupName="A";
			    }else if(sumQTy > nGroupA && sumQTy <= nGroupB){
			    	groupName="B";
			    }else if(sumQTy > nGroupB && sumQTy <= nGroupC){
			    	groupName="C";
			    }else if(sumQTy > nGroupC && sumQTy <= nGroupD){
			    	groupName="D";
			    }else if(sumQTy > nGroupD && sumQTy <= nGroupE){
			    	groupName="E";
			    }else if(sumQTy > nGroupE && sumQTy <= nGroupF){
			    	groupName="F";
			    }else if(sumQTy > nGroupF && sumQTy <= nGroupG){
			    	groupName="G";
			    }else if(sumQTy > nGroupG && sumQTy <= nGroupH){
			    	groupName="H";
			    }else if(sumQTy > nGroupH && sumQTy <= nGroupI){
			    	groupName="I";
			    }else {
			    	groupName="J";
			    }
		    }
		    //新增门店分组临时表
		    Map<String, Object> storeMap=new  HashMap<String, Object>();
		    storeMap.put("storeNo", storeNo);//店编号
		    storeMap.put("groupName", groupName);//分组编号A B C
		    storeMap.put("divideNo", divideNo);//分货单号
		    storeMap.put("receiptNo", receiptNo);//收货单号
		    storeMap.put("serialNo", serialNo);//流道号
		    storeMap.put("ruleNo", ruleNo);//模板编号
		    //新增到店分组的数据里List<Map>
		    this.storeGroupList.add(storeMap);//
	    } catch (Exception e) {
			
			outMsg="N|新增门店分组临时表失败！";
			msg=e.getMessage()+"N|新增门店分组临时表失败！";
			throw new ServiceException(e);
		}finally{
			mapObj.put("flag", flag);
			mapObj.put("msg", msg);
			mapObj.put("outMsg", outMsg);
		}
		return mapObj;
	}
	

		
		//店箱分组集合
		private List<Map<String, Object>>  storeGroupBoxList=new ArrayList<Map<String,Object>>();
		//店箱分组集合
		public  List<Map<String, Object>>  getStoreGroupBoxList(){
			return this.storeGroupBoxList;
		}
		
		//批量更新更新箱码组别信息 参数
		private List<Map<String, Object>>  paramBoxGroupList=new ArrayList<Map<String,Object>>();
		//批量更新更新箱码组别信息 参数
		public  List<Map<String, Object>>  getParamBoxGroupList(){
			return this.paramBoxGroupList;
		}
		
		//批量更新箱码状态 参数
		private List<Map<String, Object>>  paramBoxStatuList=new ArrayList<Map<String,Object>>();
		//批量更新箱码状态  参数
		public  List<Map<String, Object>>  getParamBoxStatuList(){
			return this.paramBoxStatuList;
		}
		/**
		 * 按箱分货 ----箱分组 入口
		 * @param params
		 * @return
		 * @throws ServiceException
		 */
		public Map<String, Object> revertBoxGrpEntrance(Map<String, Object> params,Map<String, Object> ruleMap) throws ServiceException{
			Map<String, Object> mapObj = new HashMap<String, Object>();
			String flag = "fail";
			String msg = "";
		
			
			 //获取传入的参数
		    String locNo=(String)params.get("locNo");//仓别
		    String ownerNo=(String)params.get("ownerNo");//委托业主
		    String divideNo=(String)params.get("divideNo");//分货单号
		    String boxNo=(String)params.get("boxNo");//箱码
		    String ruleNo =(String)params.get("ruleNo");//按箱分货规则代号
		    
		    String outMsg="N|[E00025]";//输出参数 out 
		   
		     // 定义参数
		    
		    String groupName ="";//分组名
		    String  serailNo ="";
		    String storeNo="N";//店编号


			try{
				outMsg="N|[E00025]";//输出参数 out 
				storeNo="N";//店编号
				
				Map<String, Object> paramMap1 = new HashMap<String, Object>();
				paramMap1.put("locNo", locNo);//仓别
				paramMap1.put("ownerNo", ownerNo);//委托业主
				paramMap1.put("divideNo", divideNo);//分货单号
				paramMap1.put("boxNo", boxNo);//箱码
				paramMap1.put("ruleNo", ruleNo);//按箱分货规则代号
			
				//箱分组 取出组编码
				Map<String, Object> resultMap=this.revertBoxGrp(paramMap1,ruleMap);
				
			
				outMsg=(String)resultMap.get("outMsg");
	     	   if("N".equals(outMsg.substring(0, 1))){
	     		
	     		  return mapObj;
	     	   }
	     	   // 更新箱码组别信息
	     
			   groupName=(String)resultMap.get("groupType");
			   serailNo=(String)resultMap.get("serailNo");
		
				Map<String, Object> paramMap2 = new HashMap<String, Object>();
				paramMap2.put("locNo", locNo);
				paramMap2.put("ownerNo", ownerNo);
				paramMap2.put("divideNo", divideNo);
				paramMap2.put("boxNo", boxNo);
				paramMap2.put("groupName", groupName);
				
				/**
				if(billOmDivideBoxMapper.updateBillOmDivideDtlForGroupNameByMap(paramMap2)>0){
					
				}else{
					outMsg="N|更新分货单箱号所在组失败";
					return mapObj;
				}
				**/
				//批量更新更新箱码组别信息 ??
				this.paramBoxGroupList.add(paramMap2);
			     //更新箱码状态
				Map<String, Object> paramMap3 = new HashMap<String, Object>();
				paramMap3.put("locNo", locNo);
				paramMap3.put("ownerNo", ownerNo);
				paramMap3.put("boxNo", boxNo);
			     /**
	            if(billOmDivideBoxMapper.updateConBoxForStatusByMap(paramMap3)>0){
				}else{
					outMsg="N|更新箱码头档状态为[已按箱分货]失败";
					return mapObj;
				}
	            **/
				//批量更新箱码状态 
				this.paramBoxStatuList.add(paramMap3);
	           
	            flag = "success";
				outMsg="Y";
				
			} catch (Exception e) {
				msg=e.getMessage();
				throw new ServiceException(e);
			}finally{
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				mapObj.put("outMsg", outMsg);
			}
			return mapObj;
			
		 }

		
		/**
		 * 按箱分货 ----箱分组
		 * @param params
		 * @return
		 * @throws ServiceException
		 */
		public Map<String, Object> revertBoxGrp(Map<String, Object> params,Map<String, Object> ruleMap) throws ServiceException{
			Map<String, Object> mapObj = new HashMap<String, Object>();
			String flag = "fail";
			String msg = "";
			
		    //获取传入的参数
		    String locNo=(String)params.get("locNo");//仓别
		    String ownerNo=(String)params.get("ownerNo");//委托业主
		    String divideNo=(String)params.get("divideNo");//分货单号 ----箱码
		    String boxNo =(String)params.get("boxNo");//箱码
	        //输出
		    String groupType="";//组编码
		    String serailNo ="";//流道号
		    String outMsg="N|[E00025]";//输出参数 out 
			 
		  //定义参数
		   //箱规则
		    String boxBasic="";// varchar2(20);--箱分组依据 1 按预发货量  2  按门店
		    int  nBoxFlag; //是否拼箱为一组   0  否  1  是

		    String boxType="";//箱码类型  0  整箱  1  拼箱
			
			try{
				
					//取值
					boxBasic=(String)ruleMap.get("BOX_BASIC");
					nBoxFlag=((BigDecimal)ruleMap.get("BOX_FLAG")).intValue();
					boxType=(String)ruleMap.get("BOX_TYPE");
			
					if(nBoxFlag==1){
						if("1".equals(boxType)){
							//拼箱为一组
							groupType="P";
						}else{
							//非拼箱正常分组
							if("1".equals(boxBasic)){
								//按发货量
								//v_cursor_get_box_info
								Map<String, Object> paramMap6 = new HashMap<String, Object>();
								paramMap6.put("locNo", locNo);
								paramMap6.put("ownerNo", ownerNo);
								paramMap6.put("divideNo", divideNo);
								//paramMap6.put("boxNo", boxNo);
								paramMap6.put("sortType", "1");
								// 1、根据箱码按组汇总门店数量  按发货量降序 2、根据箱码按门店汇总预发货数量 按门店数降序
								List<Map<String, Object>>  tempList=null;
								if(this.getStoreGroupBoxList() !=null && this.getStoreGroupBoxList().size()>0){
									tempList=this.getStoreGroupBoxList();
								}else{	
								tempList=billOmDivideBoxMapper.selectBoxNoByMap(paramMap6);
								this.storeGroupBoxList=tempList;
								}
								
								for(Map<String, Object> boxMap:tempList){
									if(boxNo.equals(boxMap.get("BOX_NO").toString())){
										groupType=(String)boxMap.get("GROUP_NAME");
										// 后 续需考虑预发货量均衡
							              break;
									}
								}
							}else{
								//按门店数
								//v_cursor_get_Store_Count
								Map<String, Object> paramMap7 = new HashMap<String, Object>();
								paramMap7.put("locNo", locNo);
								paramMap7.put("ownerNo", ownerNo);
								paramMap7.put("divideNo", divideNo);
								//paramMap7.put("boxNo", boxNo);
								paramMap7.put("sortType", "2");
								// 1、根据箱码按组汇总门店数量  按发货量降序 2、根据箱码按门店汇总预发货数量 按门店数降序
								List<Map<String, Object>>  tempList=null;
								if(this.getStoreGroupBoxList() !=null && this.getStoreGroupBoxList().size()>0){
									tempList=this.getStoreGroupBoxList();
								}else{	
								tempList=billOmDivideBoxMapper.selectBoxNoByMap(paramMap7);
								this.storeGroupBoxList=tempList;
								}
								
								for(Map<String, Object> boxMap:tempList){
									if(boxNo.equals(boxMap.get("BOX_NO").toString())){
										groupType=(String)boxMap.get("GROUP_NAME");
										// 后 续需考虑预发货量均衡
							              break;
									}
								}
							}
						}
						
					}
					
					if(nBoxFlag==0){
						//非拼箱正常分组
						if("1".equals(boxBasic)){
							//按发货量
							//v_cursor_get_box_info
							Map<String, Object> paramMap6 = new HashMap<String, Object>();
							paramMap6.put("locNo", locNo);
							paramMap6.put("ownerNo", ownerNo);
							paramMap6.put("divideNo", divideNo);
							//paramMap6.put("boxNo", boxNo);
							paramMap6.put("sortType", "1");
							// 1、根据箱码按组汇总门店数量  按发货量降序 2、根据箱码按门店汇总预发货数量 按门店数降序
							List<Map<String, Object>>  tempList=null;
							if(this.getStoreGroupBoxList() !=null && this.getStoreGroupBoxList().size()>0){
								tempList=this.getStoreGroupBoxList();
							}else{	
							tempList=billOmDivideBoxMapper.selectBoxNoByMap(paramMap6);
							this.storeGroupBoxList=tempList;
							}
							
							for(Map<String, Object> boxMap:tempList){
								if(boxNo.equals(boxMap.get("BOX_NO").toString())){
									groupType=(String)boxMap.get("GROUP_NAME");
									// 后 续需考虑预发货量均衡
						              break;
								}
							}
						}else{
							//按门店数
							//v_cursor_get_Store_Count
							Map<String, Object> paramMap7 = new HashMap<String, Object>();
							paramMap7.put("locNo", locNo);
							paramMap7.put("ownerNo", ownerNo);
							paramMap7.put("divideNo", divideNo);
							//paramMap7.put("boxNo", boxNo);
							paramMap7.put("sortType", "2");
							// 1、根据箱码按组汇总门店数量  按发货量降序 2、根据箱码按门店汇总预发货数量 按门店数降序
							List<Map<String, Object>>  tempList=null;
							if(this.getStoreGroupBoxList() !=null && this.getStoreGroupBoxList().size()>0){
								tempList=this.getStoreGroupBoxList();
							}else{	
							tempList=billOmDivideBoxMapper.selectBoxNoByMap(paramMap7);
							this.storeGroupBoxList=tempList;
							}
							
							for(Map<String, Object> boxMap:tempList){
								if(boxNo.equals(boxMap.get("BOX_NO").toString())){
									groupType=(String)boxMap.get("GROUP_NAME");
									// 后 续需考虑预发货量均衡
						              break;
								}
							}
						}
					}
					
					
					flag = "success";
					outMsg="Y";
			} catch (DaoException e) {
				msg=e.getMessage();
				throw new ServiceException(e);
			}finally{
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				mapObj.put("groupType", groupType);
				mapObj.put("serailNo", serailNo);
				mapObj.put("outMsg", outMsg);
			}
			return mapObj;
			
		 }
		
		public void boxDivideCX(BillOmDivide divide,String divideNo,Map<String, Object> paramMap,LinkedHashSet<String> stores,List<Map<String,Object>> storeSortInfo,Map<String,Object> box_info) throws DaoException{//拆箱分货

     	   //获取分货信息
            List<Map<String,Object>>  boxMapList_c = billOmDivideBoxMapper.selectBoxMStoreBoxCodeByMap(paramMap);//拆箱的语句不同
//            List<Map<String,Object>>  boxMaplist_new_c = new ArrayList<Map<String,Object>>();//
            boolean isFulfil = false;//标记这箱货已经分完了
     	   for(String store_no : stores){
     		   for(String stores_sub : stores){//先循环所有门店 看看是否有完全匹配的门店 不管从大到小或者从小到大
     			   boolean isIdoneity = false;//箱剩下的量是否完全匹配(包括尺码)这个客户
        	  		  int n_Qty = 0;//箱可以分配数量 (单个尺码)
        	  		  int n_Qtys = 0;//箱可以分配数量(所有尺码)
        	  		  int s_Qty = 0;//店需要的数量
        	  		  
        	  		  //找门店需要的数量
        	  		  for(Iterator it = storeSortInfo.iterator(); it.hasNext(); ) {
		            	   Map<String,Object>  rs = (Map<String, Object>) it.next();
		            	   if(box_info.get("BOX_NO").toString().equals(rs.get("BOX_NO")) && stores_sub.equals(rs.get("STORE_NO").toString())){
		            		   s_Qty = Integer.parseInt(rs.get("SUMQTY").toString());
		            		   break;
		            	   }
        	  		  } 
        	  		  int c_qty = 0;
        	  		  int divide_qty = 0;//箱已分配数量
        	  		  for(Map<String, Object> boxqty:boxMapList_c){//算箱剩余的数量  所有尺码
        	  			divide_qty +=  Integer.parseInt(boxqty.get("DIVIDE_QTY").toString());
        	  			c_qty = Integer.parseInt(boxqty.get("CQTY").toString());
        	  		  }
        	  		  n_Qtys = c_qty - divide_qty;//计算箱可以分配的数量
        	  		  
	           	  	  if(n_Qtys==s_Qty){//数量完全满足再计算尺码是否匹配
	           	  		  for(Map<String, Object> boxmap:boxMapList_c){//
	           	  			  if(boxmap.get("STORE_NO").equals(stores_sub)){
	           	  				  n_Qty= Integer.parseInt(boxmap.get("QTY").toString()); //nQty箱这个尺码可以分配数量
	           	  				  if(n_Qty ==Integer.parseInt(boxmap.get("ITEM_QTY").toString())){//ITEM_QTY   现在客户要的数量=（客户要的总数量 - 已经分配给客户的数量）  表示箱的剩余的数量是否 == 现在客户要的数量
	           	  					  //这个尺码匹配成功
	           	  				  }else{
	           	  					  isIdoneity = false;
	           	  					  break;//不完全匹配  继续走原始逻辑  分要货量最大的
	           	  				  }
	           	  				  isIdoneity = true;//尺码完全匹配
	           	  			  }
			 	  	       }
	           	  	  }
         		   
         		   if(isIdoneity){//对完全匹配的门店配货
         			   store_no = stores_sub;
         			   isFulfil = true;
	            			 break;
         		   }else{
         			   continue;
         		   }
     		   }
     		   
//     		   index = 1;//当前收货单有匹配到出库订单
    	  			//匹配箱里面对应的门店的分货数据
		            List<Map<String,Object>> mdfhmx = new ArrayList<Map<String,Object>>();
	                for(Iterator it = boxMapList_c.iterator(); it.hasNext(); ) {
	            	   Map<String,Object>  rs = (Map<String, Object>) it.next();
	            	   int qty= Integer.parseInt(rs.get("QTY").toString());
    	  	           int divideQty=Integer.parseInt(rs.get("DIVIDE_QTY").toString());
    	  	           if(null==rs.get("ITEM_QTY")){
    	  	        	   continue;
    	  	           }
    	  	           int itemQty=Integer.parseInt(rs.get("ITEM_QTY").toString());
	            	   if(store_no.equals(rs.get("STORE_NO"))  && ((qty - divideQty)> 0) && itemQty > 0){
	            		  //刷选门店过后的分货数据
	            		   Map<String,Object> m = new HashMap<String,Object>();
	            		   m.put("BOX_NO", rs.get("BOX_NO"));
	            		   m.put("BOX_ID", rs.get("BOX_ID"));
	            		   m.put("ITEM_NO", rs.get("ITEM_NO"));
	            		   m.put("SIZE_NO", rs.get("SIZE_NO"));
	            		   m.put("QTY", rs.get("QTY"));
	            		   m.put("DIVIDE_QTY", rs.get("DIVIDE_QTY"));
	            		   m.put("CQTY", rs.get("CQTY"));
	            		   m.put("ITEM_QTY", rs.get("ITEM_QTY"));
	            		   m.put("DISQTY", rs.get("DISQTY"));
	            		   m.put("CELL_NO", rs.get("CELL_NO"));
	            		   m.put("CELL_ID", rs.get("CELL_ID"));
	            		   m.put("BRAND_NO", rs.get("BRAND_NO"));
	            		   m.put("BUSINESS_TYPE", rs.get("BUSINESS_TYPE"));
	            		   m.put("EXP_TYPE", rs.get("EXP_TYPE"));
	            		   m.put("EXP_NO", rs.get("EXP_NO"));
	            		   m.put("STORE_NO", rs.get("STORE_NO"));
	            		   m.put("PACK_QTY", rs.get("PACK_QTY"));
	            		   m.put("BATCH_NO", rs.get("BATCH_NO"));
	            		   m.put("SCHEDULE_QTY", rs.get("SCHEDULE_QTY"));
	            		   mdfhmx.add(m);
	            	   }
	                }
	                
	                int nIndex = 0;
	                int nBoxQty; //--实际写入量
	                String serailNo;
	                int nDivideId;
	                int statusID;
	                int nStatus;
	                int nCount;;
	                int nQty ;
	                String strcellNo ;
	                int nCellId;
	  		
	               Object[] mdfhmxs = mdfhmx.toArray();
	                
	                for(int i =0;i<mdfhmxs.length;i++){
	                   Map<String, Object> boxmap =( Map<String, Object> ) mdfhmxs[i];
    	  		       //检查是否执行循环
    	  	           nIndex = nIndex +1;
    	  	           int qty= Integer.parseInt(boxmap.get("QTY").toString());
    	  	           int divideQty=Integer.parseInt(boxmap.get("DIVIDE_QTY").toString());
    	  	           nQty= qty - divideQty; //nQty箱可以分配数量  = 箱的数量 - 箱已经分配的数量
    	  	           int itemqty = Integer.parseInt(boxmap.get("ITEM_QTY").toString());
    	  	           
    	  	           if(nQty <= 0){
    	  	        	   break;
    	  	           }
    	  	           //配量
    	  	           if(nQty>=itemqty){//ITEM_QTY   现在客户要的数量=（客户要的总数量 - 已经分配给客户的数量）  表示箱的剩余的数量是否>= 现在客户要的数量
    	  	        	 nBoxQty = itemqty;//如果箱剩余的数量 大于客户所需要的数量  那么就直接取客户需要的数量
    	  	           }else{
    	  	        	 nBoxQty=nQty;//如果客户要的比箱剩余的数量要大  则把箱剩余的数量配给客户
    	  	           }
    	  	           //匹配
    	  	          nDivideId= 0;
    	  	         serailNo = "";
    	  	        //根据分货单号、门店找流道编号
					Map<String, Object> paramMap_serail = new HashMap<String, Object>();
					paramMap_serail.put("storeNo", store_no);
					paramMap_serail.put("divideNo", divideNo);
			
					serailNo=""+billOmDivideBoxMapper.selectStoreNoSerialNo(paramMap_serail);
					if("".equals(serailNo) ||"0".equals(serailNo)){
    					divide.setStrOutMsg("N | 获取流道号失败，请联系管理员！");
						return;
					}
					
    				String business_Type=(String)boxmap.get("BUSINESS_TYPE");
    				if("1".equals(business_Type)){
    					strcellNo=(String)boxmap.get("CELL_NO");
    					nCellId=((BigDecimal)boxmap.get("CELL_ID")).intValue();
    				}else{
    					strcellNo=box_info.get("CELL_NO").toString();
    					nCellId=-1;
    				}
    				//写分播单
    				Map<String, Object> paramMap_Divide = new HashMap<String, Object>();
    				paramMap_Divide.put("locNo", divide.getLocno());
    				paramMap_Divide.put("ownerNo", divide.getOwnerNo());
    				paramMap_Divide.put("batchNo", boxmap.get("BATCH_NO"));
    				paramMap_Divide.put("receiptNo", box_info.get("RECEIPT_NO").toString());
    				paramMap_Divide.put("divideNo", divideNo);
    				paramMap_Divide.put("storeNo", boxmap.get("STORE_NO"));
    				paramMap_Divide.put("expType", boxmap.get("EXP_TYPE"));
    				paramMap_Divide.put("expNo", boxmap.get("EXP_NO"));
    				paramMap_Divide.put("cellNO", strcellNo);
    				paramMap_Divide.put("nCellId", nCellId);
    				paramMap_Divide.put("itemNo", boxmap.get("ITEM_NO"));
    				paramMap_Divide.put("sizeNo", boxmap.get("SIZE_NO"));
    				paramMap_Divide.put("packQty", boxmap.get("PACK_QTY"));
    				paramMap_Divide.put("nQty", nBoxQty);
    				paramMap_Divide.put("lineNo", boxmap.get("LINE_NO"));
    				paramMap_Divide.put("userId", divide.getCreator());
    				paramMap_Divide.put("userName", divide.getCreatorname());
    				paramMap_Divide.put("serailNo", serailNo);
    				paramMap_Divide.put("brandNo", boxmap.get("BRAND_NO"));
    				paramMap_Divide.put("boxNO", boxmap.get("BOX_NO"));
    				divideDtls.add(paramMap_Divide); 
    		     
    				//单据跟踪中是否有部分分配的状态
    				nStatus =0;
    				Map<String, Object> paramMap5 = new HashMap<String, Object>();
    				paramMap5.put("locNo", divide.getLocno());
    				paramMap5.put("expNo", boxmap.get("EXP_NO"));
    				
    				nStatus=billOmDivideBoxMapper.selectBillStatusCount(paramMap5);
    				
    			       if(nStatus<=0){
        		    	   //获取状态中单据最大序列
        			   		statusID=billOmDivideBoxMapper.selectBillStatusMax(paramMap5);
        			   		statusID=statusID+1;
        			   		//写单据状态跟踪表
        			   		Map<String, Object> paramMap7 = new HashMap<String, Object>();
        			   		paramMap7.put("locNo", divide.getLocno());
        			   		paramMap7.put("expNo", boxmap.get("EXP_NO"));
        			   		paramMap7.put("billType", "OM");
        			   		paramMap7.put("statusID", statusID);
        			   		paramMap7.put("status", "12");
        			   		paramMap7.put("description", "更新发货通知单状态为部分分货");
        			   		paramMap7.put("userId", divide.getCreator());
        			   		
        			   		billOmDivideBoxMapper.insertBillStatusLogByMap(paramMap7);
    			       }
    			 
    			       //更新出货单明细
    			  		Map<String, Object> paramMap_exp = new HashMap<String, Object>();
        			  	paramMap_exp.put("nQty", nBoxQty);//变化点
        			  	paramMap_exp.put("locNo", divide.getLocno());
        			  	paramMap_exp.put("ownerNo", divide.getOwnerNo());
        			  	paramMap_exp.put("itemNo", boxmap.get("ITEM_NO"));
        			  	paramMap_exp.put("sizeNo", boxmap.get("SIZE_NO"));
        			  	paramMap_exp.put("expNo", boxmap.get("EXP_NO"));
        			  	paramMap_exp.put("storeNo", boxmap.get("STORE_NO"));
        			  	expDtls.add(paramMap_exp);
        			  	expNos.add(boxmap.get("EXP_NO").toString());
        			  	
	           			 for(int j=0;j<mdfhmxs.length;j++){
	            			 Map<String,Object> m = ( Map<String, Object> )mdfhmxs[j];
	            			 if(m.get("BOX_NO").equals(boxmap.get("BOX_NO")) && m.get("ITEM_NO").equals(boxmap.get("ITEM_NO")) && m.get("SIZE_NO").equals(boxmap.get("SIZE_NO"))){
		            			 int rs_qty = Integer.parseInt(m.get("QTY").toString());//修改这个箱这个尺码的剩余数量
		            			 m.put("QTY", rs_qty-nBoxQty);
		            			 mdfhmxs[j] = m;
	            			 }
	           			 }
        			  	
//         			 boxMaplist_new_c.clear();
	            	   for(int l = 0;l<boxMapList_c.size();l++){	
		            	   Map<String,Object>  rs =  boxMapList_c.get(l);
		            	   if(rs.get("BOX_NO").equals(boxmap.get("BOX_NO")) && rs.get("ITEM_NO").equals(boxmap.get("ITEM_NO")) && rs.get("SIZE_NO").equals(boxmap.get("SIZE_NO"))){
		            		   int rs_qty = Integer.parseInt(rs.get("QTY").toString());//修改这个箱这个尺码的剩余数量
		            		   rs.put("QTY", rs_qty-nBoxQty);
		            		   
		            	   }
		            	   if(null != rs.get("STORE_NO") && rs.get("EXP_NO").equals(boxmap.get("EXP_NO")) && rs.get("STORE_NO").equals(boxmap.get("STORE_NO")) && rs.get("BOX_NO").equals(boxmap.get("BOX_NO")) && rs.get("ITEM_NO").equals(boxmap.get("ITEM_NO")) && rs.get("SIZE_NO").equals(boxmap.get("SIZE_NO"))){
		            		   int y_divideqty = Integer.parseInt(rs.get("DIVIDE_QTY").toString());
		            		   rs.put("DIVIDE_QTY", y_divideqty + nBoxQty);
		            		   int item_qty = Integer.parseInt(rs.get("ITEM_QTY").toString());
		            		   rs.put("ITEM_QTY", item_qty - nBoxQty);
		            	   }
//		            	   boxMaplist_new_c.add(rs);
	       		  		}
	       		  		
//	       		  		if(boxMaplist_new_c.size() >0 ){
//		       		  		boxMapList_c.clear();
//		       		  		for(Iterator it = boxMaplist_new_c.iterator(); it.hasNext(); ){
//		       		  			Map<String,Object>  rs = (Map<String, Object>) it.next();
//		       		  			boxMapList_c.add(rs);
//		       		  		}
//		       		  	
//	            		}
	       		  		
    			  		//更新箱码明细
    		       		Map<String, Object> paramMap13 = new HashMap<String, Object>();
    			  		paramMap13.put("nQty", nBoxQty);
    					paramMap13.put("locNo", divide.getLocno());
    			  		paramMap13.put("ownerNo", divide.getOwnerNo());
    			  		paramMap13.put("boxNo", boxmap.get("BOX_NO"));
    			  		paramMap13.put("itemNo", boxmap.get("ITEM_NO"));
    			  		paramMap13.put("sizeNo", boxmap.get("SIZE_NO"));
    			  		paramMap13.put("boxId", boxmap.get("BOX_ID"));
    			  		paramMap13.put("storeNo", boxmap.get("STORE_NO"));
    			  		//批量更新 更新箱码明细
    			  		updateBoxDtlByList.add(paramMap13);
    			  		
    			        //更新箱码头档信息
    		      		Map<String, Object> paramMap14 = new HashMap<String, Object>();
    			  		paramMap14.put("nQty", nBoxQty);
    					paramMap14.put("locNo", divide.getLocno());
    			  		paramMap14.put("ownerNo", divide.getOwnerNo());
    			  		paramMap14.put("boxNo", boxmap.get("BOX_NO"));
    			  		//批量更新箱码头档信息
    			  		updateConBoxByList.add(paramMap14);
    			  		
    		            //回写收货单明细数量
    		      		Map<String, Object> paramMap15 = new HashMap<String, Object>();
    			  		paramMap15.put("nQty", nBoxQty);
    					paramMap15.put("locNo", divide.getLocno());
    			  		paramMap15.put("ownerNo", divide.getOwnerNo());
    			  		paramMap15.put("receiptNo", box_info.get("RECEIPT_NO").toString());
    			  		paramMap15.put("boxNo", boxmap.get("BOX_NO"));
    			  		paramMap15.put("itemNo", boxmap.get("ITEM_NO"));
    			  		paramMap15.put("sizeNo", boxmap.get("SIZE_NO"));
    			  		//批量回写收货单明细数量
    			  		updateBillImReceiptDtlByList.add(paramMap15);
    	  		  } 
         	  if(isFulfil){
         		 break;//这箱货已经分完了
              }
     	   }
        
		}
		
		public void batchUpdateList() throws DaoException{
			//批量插入分播单明细数据
     	   if(divideDtls.size()>0){
     		   billOmDivideBoxMapper.insertBillOmDivideByMap(divideDtls);
     		   divideDtls.clear();
     	   }
     	   //批量更新发货通知单明细数据
     	   if(expDtls.size()>0){
     		   billOmDivideBoxMapper.updateOmExpDtlStatusByMap(expDtls);
     		   expDtls.clear();
     	   }
     	   //批量更新 更新箱码明细
 	  	   if(updateBoxDtlByList.size()>0){  
 	  		 billOmDivideBoxMapper.batchUpdateBoxDtlByList(updateBoxDtlByList);
 	  		 updateBoxDtlByList.clear();
 	       }
 	  	   //批量更新箱码头档信息
 	  	   if(updateConBoxByList.size()>0){
 	  		 billOmDivideBoxMapper.batchUpdateConBoxByList(updateConBoxByList); 
 	  		 updateConBoxByList.clear();
 	       }
   		     //批量回写收货单明细数量
 		   if(updateBillImReceiptDtlByList.size()>0){ 
 			   billOmDivideBoxMapper.batchUpdateBillImReceiptDtlByList(updateBillImReceiptDtlByList);
 			   updateBillImReceiptDtlByList.clear();
 		   }
 		   
		}
		
		public int finishedGoods(BillOmDivide divide,List<Map<String,Object>> boxSort_,String businessType,String divideNo) throws DaoException, ServiceException{
			int rnum = 0;
			//分完货时跳流道处理
			List<Map<String,Object>> divideSerialNos =  billOmDivideBoxMapper.selectDivideSerialNo(divide.getDivideNo());
			if(divideSerialNos.size() < this.getStoreGroupList().size()){//如果临时表里面的流道多于分货的流道 就表示有门店没有分货 可能存在跳流道的情况需要处理
				List<Map<String, Object>> divideSerialNoList = new ArrayList<Map<String, Object>>();//用于批量更新
				for(int i=0;i<divideSerialNos.size();i++){
					Map<String,Object> divideSerialNo = divideSerialNos.get(i);
					
					Map<String, Object> param_divideSerialNo = new HashMap<String, Object>();
					param_divideSerialNo.put("serialNoNew", i+1);
					param_divideSerialNo.put("divideNo", divide.getDivideNo());
					param_divideSerialNo.put("storeNo", divideSerialNo.get("STORE_NO"));
					param_divideSerialNo.put("serialNo", divideSerialNo.get("SERIAL_NO"));
					divideSerialNoList.add(param_divideSerialNo);
				}
				
				billOmDivideBoxMapper.batchUpdateDivideSerialNo(divideSerialNoList);
			}
			
			this.getParamBoxGroupList().clear();
			this.getParamBoxStatuList().clear();
			this.getStoreGroupBoxList().clear();
			//箱分组
			for( Map<String,Object>  boxgroupMap:boxSort_ ){
				Map<String,Object> outmsg4 = this.revertBoxGrpEntrance(boxgroupMap,ruleMap);
			}
			
			//批量更新更新箱码组别信息
			billOmDivideBoxMapper.batchUpdateBillOmDivideDtlForGroupNameByMap(this.getParamBoxGroupList());
			//批量更新箱码状态
			billOmDivideBoxMapper.batchUpdateConBoxForStatusByMap(this.getParamBoxStatuList());
			
			//更新主表状态
			 if("1".equals(divide.getBusinessType())){//存储分货才会走 根据存储过程proc_RevertDivideBoxCC改造
				 if("1".equals(businessType)){
					 List<Map<String,Object>> maps = billOmDivideBoxMapper.selectBillImReceiptBySerialNo(divide.getSerialNo());
					 for(Map<String,Object> receiptNos : maps){
						 int rt = billOmDivideBoxMapper.revertUpdtReceipt(divide.getLocno().toString(),receiptNos.get("RECEIPT_NO").toString());
						 if(rt <= 0){
	            			   divide.setStrOutMsg("N | (处理收货单状态)分货失败");
	            			   return 0;
	            		  }
					 }
				 }
			 }
			rnum = billOmDivideBoxMapper.updateBillOmDivide(businessType, divide.getLocno(), divide.getDivideNo());
			
			if("1".equals(businessType)){//库存分货，保存分货任务时，释放分货单中多余箱子(modi by crm 2014.08.14)
				//1.查询出未匹配到的分货箱子
				Map<String, Object> paramMap_NoDivideBox = new HashMap<String, Object>();
				paramMap_NoDivideBox.put("locNo", divide.getLocno());
				paramMap_NoDivideBox.put("divideNo", divideNo);
				int NoDivideBox_rs =  billOmDivideBoxMapper.insertNoDivideBox(paramMap_NoDivideBox);
				if(NoDivideBox_rs>0){
					List<Map<String,Object>> noDivideBoxs = billOmDivideBoxMapper.selectNoDivideBox();
					int  v_row_id = 0;
					for(Map<String,Object> noDivideBox : noDivideBoxs){
						v_row_id = v_row_id + 1;
						Map<String, Object> paramMap_PrepareData = new HashMap<String, Object>();
						paramMap_PrepareData.put("I_PAPER_NO", noDivideBox.get("RECEIPT_NO"));
						paramMap_PrepareData.put("I_PAPER_TYPE", "IR");
						paramMap_PrepareData.put("I_IO_FLAG", "O");
						paramMap_PrepareData.put("I_CREATOR", divide.getCreator());
						paramMap_PrepareData.put("I_ROW_ID", v_row_id);
						paramMap_PrepareData.put("I_CELL_ID", noDivideBox.get("CELL_ID"));
						paramMap_PrepareData.put("I_LOCNO", noDivideBox.get("LOCNO"));
						paramMap_PrepareData.put("I_CELL_NO", noDivideBox.get("CELL_NO"));
						paramMap_PrepareData.put("I_ITEM_NO", noDivideBox.get("ITEM_NO"));
						paramMap_PrepareData.put("I_SIZE_NO", noDivideBox.get("SIZE_NO"));
						paramMap_PrepareData.put("I_PACK_QTY", 1);
						paramMap_PrepareData.put("I_ITEM_TYPE", noDivideBox.get("ITEM_TYPE"));
						paramMap_PrepareData.put("I_QUALITY", noDivideBox.get("QUALITY"));
						paramMap_PrepareData.put("I_OWNER_NO", noDivideBox.get("OWNER_NO"));
						paramMap_PrepareData.put("I_SUPPLIER_NO", noDivideBox.get("SUPPLIER_NO"));
						paramMap_PrepareData.put("I_BOX_NO", noDivideBox.get("BOX_NO"));
						paramMap_PrepareData.put("I_QTY",0);
						paramMap_PrepareData.put("I_OUTSTOCK_QTY",  -Integer.parseInt(noDivideBox.get("RECEIPT_QTY").toString()));
						paramMap_PrepareData.put("I_INSTOCK_QTY", 0);
						paramMap_PrepareData.put("I_STATUS", "0");
						paramMap_PrepareData.put("I_FLAG", "0");
						paramMap_PrepareData.put("I_HM_MANUAL_FLAG", "1");
						
						try {
							billAccControlMapper.procAccPrepareDataExt(paramMap_PrepareData);
						} catch (Exception e) {
							e.printStackTrace();
							String erroutmsg = e.getMessage().substring(e.getMessage().indexOf("<font color=red>")+16,e.getMessage().indexOf("</font>"));
							divide.setStrOutMsg("N | "+erroutmsg);
	            		    return 0;
						}
						
						Map<String, Object> paramMap_AccApply = new HashMap<String, Object>();
						paramMap_AccApply.put("I_PAPER_NO", noDivideBox.get("RECEIPT_NO"));
						paramMap_AccApply.put("I_LOC_TYPE", "2");
						paramMap_AccApply.put("I_PAPER_TYPE", "IR");
						paramMap_AccApply.put("I_IO_FLAG", "O");
						paramMap_AccApply.put("I_PREPARE_DATA_EXT", 1);
						paramMap_AccApply.put("I_IS_WEB", 1);
						try {
							billAccControlMapper.procAccApply(paramMap_AccApply);
						} catch (Exception e) {
							e.printStackTrace();
							String erroutmsg = e.getMessage().substring(e.getMessage().indexOf("<font color=red>")+16,e.getMessage().indexOf("</font>"));
							divide.setStrOutMsg("N | "+erroutmsg);
	            		    return 0;
						}
						
						Map<String, Object> paramMap_ReceiptStatus = new HashMap<String, Object>();
						paramMap_ReceiptStatus.put("locno", noDivideBox.get("LOCNO"));
						paramMap_ReceiptStatus.put("receipt_no", noDivideBox.get("RECEIPT_NO"));
						paramMap_ReceiptStatus.put("Box_No", noDivideBox.get("BOX_NO"));
						paramMap_ReceiptStatus.put("item_no", noDivideBox.get("ITEM_NO"));
						paramMap_ReceiptStatus.put("size_no", noDivideBox.get("SIZE_NO"));
						int status_rs = billOmDivideBoxMapper.updateReceiptStatus(paramMap_ReceiptStatus);
						if(status_rs<=0){
							divide.setStrOutMsg("N | (释放分货单)失败");
	            		    return 0;
						}
						
						billOmDivideBoxMapper.updateBoxStruts(paramMap_ReceiptStatus);
						
					}
				}
				
			}
			return rnum;
		}

}