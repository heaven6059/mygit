package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.TmpConBoxExcelService;

/**
 * 导入箱明细初始化
 * @author wanghb
 * @date 2014-10-11 上午11:44:58
 * @version 1.1.3.41
 * @copyright yougou.com 
 */
@Service("tmpConBoxExcelManager")
public class TmpConBoxExcelManagerImpl extends BaseCrudManagerImpl implements TmpConBoxExcelManager {

	@Resource
	private TmpConBoxExcelService tmpConBoxExcelService;
//	@Resource
//	private ConBoxService conBoxService;
//	@Resource
//	private ConBoxDtlService conBoxDtlService;
//	@Resource
//	private BmContainerService bmContainerService;
	
	@Override
	public BaseCrudService init() {
		return tmpConBoxExcelService;
	}
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String,Object> importConBoxExcel(List<TmpConBoxExcel> list,SystemUser user)throws ManagerException{
		Map<String,Object> map=new HashMap<String,Object>();
		String locNo=user.getLocNo();
		String msg="";
		String result=ResultEnums.SUCCESS.getResultMsg();
		String uuId ="";
		List<String> conNoList=new ArrayList<String>();//箱号集合
		Map<String,String> itemMap=new HashMap<String,String>();//判断箱是否有混款
		Map<String,Integer> linNoMap=new HashMap<String,Integer>();//行号
		HashSet<String> cellNoList= new HashSet<String>();//储位集合
		Map<String,String> itemNoMap=new HashMap<String,String>();//商品编码、条码，尺码
		try {
			if(null!=list && list.size()>0 ){
				uuId = UUID.randomUUID().toString();
				for(TmpConBoxExcel tmp:list){
					String tmpLocNo=tmp.getLocNo();
					String tmpConNo=tmp.getConNo();
					String tmpItmeNo=tmp.getItemNo();
					String tmpBarCode=tmp.getBarcode();
					String tmpSizeNo=tmp.getSizeNo();
					BigDecimal qty=tmp.getQty();
					if(!tmpLocNo.equals(locNo)){
						result=ResultEnums.FAIL.getResultMsg();
						msg="仓库编码必须是【"+locNo+"】";
						break;
					}
					if(!CommonUtil.checkNumber(qty.toString(), "+")){
						result=ResultEnums.FAIL.getResultMsg();
						msg="数量必须是正整数【"+qty+"】";
						break;
					}
					tmp.setUuId(uuId);
					conNoList.add(tmpConNo);
					cellNoList.add(tmp.getCellNo());
					//box_type字段
					String itemNo=itemMap.get(tmpLocNo+tmpConNo);
					if(StringUtils.isNotEmpty(itemNo)){
						if(itemNo.equals(tmp.getItemNo())){
							tmp.setBoxType("0");
						}else{
							tmp.setBoxType("1");
						}
					}else{
						itemMap.put(tmp.getLocNo()+tmp.getConNo(),tmp.getItemNo());
						tmp.setBoxType("0");
					}
					//行号字段
					Integer linNo=linNoMap.get("linNo"+tmpLocNo+tmpConNo);
					if(null!=linNo){
						Integer cLinNo=linNo+1;
						tmp.setLineNo(cLinNo.toString());
						linNoMap.put("linNo"+tmpLocNo+tmpConNo,cLinNo);
					}else{
						tmp.setLineNo("1");
						linNoMap.put("linNo"+tmpLocNo+tmpConNo, 1);
					}
					//商品编码、条码、尺寸
					String itemNoKey=tmpItmeNo+";"+tmpBarCode+";"+tmpSizeNo;
					String itemNoValue=itemNoMap.get(itemNoKey);
					if(StringUtils.isBlank(itemNoValue)){
						itemNoMap.put(itemNoKey, "1");
					}
				}
				//保存到中间表
				if(result.equals(ResultEnums.SUCCESS.getResultMsg())){
					tmpConBoxExcelService.batchInsertSelective(list);
				}
				//验证储位
				if(result.equals(ResultEnums.SUCCESS.getResultMsg())){
					String resultMsg1=this.validateCellNo(uuId,locNo,cellNoList);
					if(StringUtils.isNotEmpty(resultMsg1)){
						result=ResultEnums.FAIL.getResultMsg();
						msg=resultMsg1;
						this.deleteByUuid(uuId);
					}
				}
				//验证商品
				if(result.equals(ResultEnums.SUCCESS.getResultMsg())){
					String resultMsg=this.validateItemNo(itemNoMap,uuId,locNo);
					if(StringUtils.isNotEmpty(resultMsg)){
						result=ResultEnums.FAIL.getResultMsg();
						msg=resultMsg;
						this.deleteByUuid(uuId);
					}
				}
				
				//验证箱
//				if(result.equals(ResultEnums.SUCCESS.getResultMsg())){
//					String resultMsg=this.validateConNo(uuId, locNo);
//					if(StringUtils.isNotEmpty(resultMsg)){
//						result=ResultEnums.FAIL.getResultMsg();
//						msg=resultMsg;
//						this.deleteByUuid(uuId);
//					}
//				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		map.put("uuId", uuId);
		map.put("result",result);
		map.put("msg", msg);
		return map;
	}
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String,Object>  batchSaveConBoxExcel(Map<String,Object> params,SystemUser user) throws ManagerException{
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> tempMap=new HashMap<String,Object>();
		try {
			String uuId=(String) params.get("uuId");
			String locNo=(String) params.get("locNo");
			String resultMsg=this.validateConNoByUuId(uuId,locNo);
			if(StringUtils.isNotEmpty(resultMsg)){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", resultMsg);
//				this.deleteByUuid(params.get("uuId").toString());
				return map;
			}
			tempMap.put("uuId", params.get("uuId"));
			tempMap.put("locNo", params.get("locNo"));
			tempMap.put("userName", user.getLoginName());
			int acc_control=SysConstans.ACC_CONTROL;
			if (acc_control == 1) {
				tmpConBoxExcelService.batshSaveAccInventory(tempMap);
				tmpConBoxExcelService.batshSaveAccInventorySku(tempMap);
			}
			//增加con_box表
			tmpConBoxExcelService.batshSaveConBoxExcel(tempMap);
			//增加con_box_dtl
			Integer count=tmpConBoxExcelService.batshSaveConBoxDtlExcel(tempMap);
			//增加bm_container
			tmpConBoxExcelService.batshSaveBmContainerExcel(tempMap);
			
			this.deleteByUuid(params.get("uuId").toString());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("count", count);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return map;
	}
	public int deleteByUuid(String uuId)throws ManagerException{
		try{
			tmpConBoxExcelService.deleteByUuid(uuId);
			return 0;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	private String validateConNo(String uuId,String locNo)throws ManagerException{
		String msg="";
		try{
			StringBuffer conSb=new StringBuffer();
			Map<String, Object> conBoxParams=new HashMap<String,Object>();
			conBoxParams.put("uuId",uuId);
			conBoxParams.put("locNo", locNo);
			List<String> resultConBox=tmpConBoxExcelService.selectConBoxByUuId(conBoxParams);
			if(null!=resultConBox && resultConBox.size()>0){
				for(String conBoxNo:resultConBox){
					conSb.append("箱号:【").append(conBoxNo).append("】已经存在，不能初始化\\r\\n");
				}
				msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
						""+conSb.toString()+"</textarea>";
			}
			return msg;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	private String validateConNoByUuId(String uuId,String locNo)throws ManagerException{
		String msg="";
		try{
			StringBuffer conSb=new StringBuffer();
			Map<String, Object> conBoxParams=new HashMap<String,Object>();
			conBoxParams.put("uuId",uuId);
			conBoxParams.put("locNo", locNo);
			List<String> resultConBox=tmpConBoxExcelService.selectConBoxByUuId(conBoxParams);
			if(null!=resultConBox && resultConBox.size()>0){
				for(String conBoxNo:resultConBox){
					conSb.append("箱号:【").append(conBoxNo).append("】已经存在，不能初始化\r\n");
				}
				msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
						""+conSb.toString()+"</textarea>";
			}else {
				List<String> bmResultConBox=tmpConBoxExcelService.selectBmContainerByUuId(conBoxParams);
				for(String conBoxNo:bmResultConBox){
					conSb.append("【").append(conBoxNo).append("】固定容器已存在但不是有效的箱号，不能初始化\r\n");
				}
				msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
						""+conSb.toString()+"</textarea>";
			}
			return msg;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	/**
	 * 验证商品   需要优化
	 * @param itemNoMap
	 * @return
	 * @throws ManagerException
	 */
	private String validateItemNo(Map<String,String> itemNoMap,String uuId,String locNo)throws ManagerException{
		String msg="";
		try{
			StringBuffer sb=new StringBuffer();
			Map<String, Object> params=new HashMap<String,Object>();
			params.put("uuId",uuId);
			params.put("locNo", locNo);
			List<String> resultItemList=tmpConBoxExcelService.selectItemNoBy(params);
			if(null!=resultItemList && resultItemList.size()>0){
				List<String> tempItemNoList=new ArrayList<String>();
				Iterator iter = itemNoMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					tempItemNoList.add((String) entry.getKey());
				}
				List<String> resultList=getDiffrent(tempItemNoList,resultItemList);
				if(null!=resultList && resultList.size()>0){
					for(String itemNo:resultList){
						String [] keys = itemNo.split(";");
						sb.append("商品编码【"+keys[0]+"】商品条码【"+keys[1]+"】尺码【"+keys[2]+"】不存在\\r\\n");
					}
				}
			}else{
				Iterator iter = itemNoMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					String [] keys=key.split(";");
					sb.append("商品编码【"+keys[0]+"】商品条码【"+keys[1]+"】尺码【"+keys[2]+"】不存在\\r\\n");
				  }
			}
			if(sb.length()>0){
				msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
						""+sb.toString()+"</textarea>";
			}
//			Iterator iter = itemNoMap.entrySet().iterator();
//			while (iter.hasNext()) {
//				Map.Entry entry = (Map.Entry) iter.next();
//				String key = (String) entry.getKey();
//				String [] keys=key.split(";");
//				Map<String, Object> params=new HashMap<String,Object>();
//				params.put("itemNo", keys[0]);
//				params.put("barcode", keys[1]);
//				params.put("sizeNo", keys[2]);
//				List<String> list=tmpConBoxExcelService.selectItemNoBy(params);
//				if(!(list!=null && list.size()>0)){
//					sb.append("商品编码【"+keys[0]+"】商品条码【"+keys[1]+"】尺码【"+keys[2]+"】无效数据\\r\\n");
//				}
//			  }
			if(sb.length()>0){
				msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
						""+sb.toString()+"</textarea>";
			}
			return msg;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	private String validateCellNo(String uuId,String locNo,HashSet<String> cellNoList)throws ManagerException{
		String msg="";
		try{
			List<String> tempCellNoList=new ArrayList<String>();
			StringBuffer conSb=new StringBuffer();
			Map<String, Object> params=new HashMap<String,Object>();
			params.put("uuId",uuId);
			params.put("locNo", locNo);
			List<String> resultCellNoList=tmpConBoxExcelService.selectCellNoBy(params);
			if(null!=resultCellNoList && resultCellNoList.size()>0){
				tempCellNoList.addAll(cellNoList);
				List<String> resultList=getDiffrent(resultCellNoList,tempCellNoList);
				if(resultList.size()>0){
					for(String conBoxNo:resultList){
						conSb.append("储位【"+conBoxNo+"】不存在\\r\\n");
					}
				}
				
			}else{
				for (String conBoxNo : cellNoList) {
					conSb.append("储位【" + conBoxNo + "】不存在\\r\\n");
				}
			}
			if(conSb.length()>0){
				msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
						""+conSb.toString()+"</textarea>";
			}
			return msg;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	
    /**
     * 获取两个List的不同元素
     * @param list1
     * @param list2
     * @return
     */
    private  List<String> getDiffrent(List<String> list1, List<String> list2) {
        Map<String,Integer> map = new HashMap<String,Integer>(list1.size()+list2.size());
        List<String> diff = new ArrayList<String>();
        List<String> maxList = list1;
        List<String> minList = list2;
        if(list2.size()>list1.size())
        {
            maxList = list2;
            minList = list1;
        }
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            Integer cc = map.get(string);
            if(cc!=null)
            {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 1);
        }
        for(Map.Entry<String, Integer> entry:map.entrySet())
        {
            if(entry.getValue()==1)
            {
                diff.add(entry.getKey());
            }
        }
        return diff;
        
    }
    
}
