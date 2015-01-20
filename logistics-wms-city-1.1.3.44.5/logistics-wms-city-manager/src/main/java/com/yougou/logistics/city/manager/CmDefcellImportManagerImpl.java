package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpCmDefCellExcel;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.service.CmDefcellImportService;

/**
 * 导入储位
 * @author wanghb
 * @date 2014-10-15 上午9:40:08
 * @version 1.1.3.41
 * @copyright yougou.com
 */
@Service("cmDefcellImportManager")
class CmDefcellImportManagerImpl extends BaseCrudManagerImpl implements CmDefcellImportManager {
 
	public static Map<String, List<LookupDtl>> lookupdMap = com.yougou.logistics.city.common.utils.SystemCache.lookupdMap;
	@Resource
	private CmDefcellImportService cmDefcellImportService;
	
	@Override
	public BaseCrudService init() {
		return cmDefcellImportService;
	}
	@SuppressWarnings("rawtypes")
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String,Object> importConBoxExcel(List<TmpCmDefCellExcel> list,SystemUser user)throws ManagerException{
		StringBuffer sb=new StringBuffer();
		Map<String,Object> map=new HashMap<String,Object>();
		String msg="";
		String result=ResultEnums.SUCCESS.getResultMsg();
		String locNo=user.getLocNo();
		String uuId ="";
		HashSet<String> wareNoList= new HashSet<String>();//仓区编码集合
		HashSet<String> areaNoList= new HashSet<String>();//库区编码 
		HashSet<String> stockNoList= new HashSet<String>();//通道编码
		HashSet<String> stockXList= new HashSet<String>();//储格列	
		HashSet<String> bayXList= new HashSet<String>();//储格位
		HashSet<String> stockYList= new HashSet<String>();//储格层	
		HashSet<String> cellNoList= new HashSet<String>();//储位编码
		Map<String,String> cellNoPieMap= new HashMap<String,String>();//储位编码拼凑
		HashSet<String> areaNoTypeList= new HashSet<String>();//库区类型
		HashSet<String> areaAList= new HashSet<String>();//库区属性
		HashSet<String> itemTypeList= new HashSet<String>();//商品类型
		HashSet<String> qualityList= new HashSet<String>();//商品品质
		HashSet<String> mixFlagList= new HashSet<String>();//混载标示
		Map<String,String> stockNoMap= new HashMap<String,String>();//key:通道编码 vlaue:商品类型+商品品质+混载标志
		Map<String,Boolean> stockNoBoolean= new HashMap<String,Boolean>();//不同品类型+商品品质+混载标志通道
		try {
			if(null!=list && list.size()>0 ){
				uuId = UUID.randomUUID().toString();
				for(TmpCmDefCellExcel cm:list){
					wareNoList.add(cm.getWareNo());
					areaNoList.add(cm.getAreaNo());
					stockNoList.add(cm.getStockNo());
					stockXList.add(cm.getStockX());
					bayXList.add(cm.getBayX());
					stockYList.add(cm.getStockY());
					cellNoList.add(cm.getCellNo());
					areaNoTypeList.add(cm.getAreaType());
					areaAList.add(cm.getAreaAttribute());
					itemTypeList.add(cm.getItemType());
					qualityList.add(cm.getQuality());
					mixFlagList.add(cm.getMixFlag());
					String temp=cm.getItemType()+cm.getQuality()+cm.getMixFlag();
					if(stockNoMap.size()>0){
						String stockIQM=stockNoMap.get(cm.getStockNo());
						if(StringUtils.isNotEmpty(stockIQM)){
							if(!stockIQM.equals(temp)){
								stockNoBoolean.put(cm.getStockNo(), false);
							}
						}else{
							stockNoMap.put(cm.getStockNo(), cm.getItemType()+cm.getQuality()+cm.getMixFlag());
						}
					}else{
						stockNoMap.put(cm.getStockNo(), cm.getItemType()+cm.getQuality()+cm.getMixFlag());
					}
					String cellNo=cm.getWareNo()+cm.getAreaNo()+
							cm.getStockNo()+cm.getStockX()+cm.getBayX()+cm.getStockY();
					if(!cellNo.equals(cm.getCellNo())){//储位=仓区编码 +库区编码 +通道编码 +储格 +位 +层
						cellNoPieMap.put(cm.getCellNo(), cellNo);
					}
					cm.setLocNo(locNo);
					cm.setItemTypeValue(this.getLookUp("ITEM_TYPE", cm.getItemType()));
					cm.setQualityValue(this.getLookUp("AREA_QUALITY", cm.getQuality()));
					cm.setMixFlagValue(this.getLookUp("MIX_FLAG", cm.getMixFlag()));
				}
				for(String wareNo:wareNoList){
					if(!(wareNo.length()==1&&StringUtils.isAlpha(wareNo))){
	                    sb.append("仓区编码【"+wareNo+"】必须是1位字母\\r\\n");
					}
				}
				for(String areaNo:areaNoList){
					if(!(areaNo.length()==2//&&StringUtils.isNumeric(areaNo)
							)){
	                    sb.append("库区编码【"+areaNo+"】必须是2位字母或数字\\r\\n");
					}
				}
				//校验库区类型是否存在
				this.validateType("AREA_TYPE", areaNoTypeList, "库区类型",sb);
				//校验库区属性是否存在
				this.validateType("AREA_ATTRIBUTE", areaAList, "库区属性",sb);
				for(String areaNo:stockNoList){
					if(!(areaNo.length()==2//&&StringUtils.isAlpha(areaNo)
							)){
	                    sb.append("通道编码【"+areaNo+"】必须是2位字母或数字\\r\\n");
					}
				}
				for(String areaNo:stockXList){
					if(!(areaNo.length()==2&&StringUtils.isNumeric(areaNo))){
	                    sb.append("储格列【"+areaNo+"】必须是2位数字\\r\\n");
					}
				}
				for(String areaNo:stockYList){
					if(!(areaNo.length()==1&&StringUtils.isNumeric(areaNo))){
	                    sb.append("储格层【"+areaNo+"】必须是1位数字\\r\\n");
					}
				}
				for(String areaNo:bayXList){
					if(!(areaNo.length()==1&&StringUtils.isNumeric(areaNo))){
	                    sb.append("储格位【"+areaNo+"】必须是1位数字\\r\\n");
					}
				}
				if(cellNoPieMap.size()>0){
					Iterator<Entry<String, String>> iter = cellNoPieMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						String key = (String) entry.getKey();
						sb.append("储位编码【"+key+"】不符合规则，储位=仓区编码 +库区编码 +通道编码 +储格 +位 +层；\\r\\n");
					}
					if(sb.length()>0){
						result=ResultEnums.FAIL.getResultMsg();
					}
				}
				//校验商品属性
				this.validateType("ITEM_TYPE", itemTypeList, "商品类型",sb);
				//校验商品品质 是否存在
				this.validateType("AREA_QUALITY", qualityList, "商品品质",sb);
				//校验混载标志是否存在
				this.validateType("MIX_FLAG", mixFlagList, "混载标示",sb);
//				if(sb.length()==0){
//					this.validateStockAtt(stockNoBoolean, sb);
//				}
				if(sb.length()>0){
					result=ResultEnums.FAIL.getResultMsg();
				}else{
					//先保存临时表
					cmDefcellImportService.batchInsertSelective(list,uuId);
				}
				//校验仓区编码是否存在
				if(result.equals(ResultEnums.SUCCESS.getResultMsg())){
					sb=this.validateWareNo(uuId,locNo,wareNoList);
					if(sb.length()>0){
						result=ResultEnums.FAIL.getResultMsg();
					}
				}
				//校验库区编码是否存在
				if(result.equals(ResultEnums.SUCCESS.getResultMsg())){
					sb=this.validateAreaNo(uuId,locNo,areaNoList);
					if(sb.length()>0){
						result=ResultEnums.FAIL.getResultMsg();
					}
				}
				if(result.equals(ResultEnums.FAIL.getResultMsg())){
					this.deleteByUuid(uuId);
					if(sb.length()>0)
					msg="<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>" +
							""+sb.toString()+"</textarea>";
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		map.put("uuId", uuId);
		map.put("result",result);
		map.put("msg", msg);
		return map;
	}
	public int deleteByUuid(String uuId)throws ManagerException{
		try {
			return cmDefcellImportService.deleteByUuid(uuId);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String,Object>  batchSaveExcel(Map<String,Object> params,SystemUser user) throws ManagerException{
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> tempMap=new HashMap<String,Object>();
		try {
			String uuId=(String) params.get("uuId");
			String locNo=(String) params.get("locNo");
			String resultMsg=this.validateCellNoByUuId(uuId,locNo);//验证储位是否存在
			if(StringUtils.isNotEmpty(resultMsg)){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", resultMsg);
//				this.deleteByUuid(params.get("uuId").toString());
				return map;
			}
			tempMap.put("uuId", params.get("uuId"));
			tempMap.put("locNo", params.get("locNo"));
			tempMap.put("userName", user.getLoginName());
			cmDefcellImportService.batshSaveCmDdefstockExcel(tempMap);//增加通道表
			int count =cmDefcellImportService.batshSaveCmDefcellExcel(tempMap);//增加储位表
			this.deleteByUuid(params.get("uuId").toString());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("count", count);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return map;
	}
	/**
	 * 校验仓区编码
	 * @param uuId
	 * @param locNo
	 * @param wareNoList
	 * @return
	 * @throws ManagerException
	 */
	private StringBuffer validateWareNo(String uuId,String locNo,HashSet<String> wareNoList)throws ManagerException{
		try{
			List<String> tempList=new ArrayList<String>();
			StringBuffer sb=new StringBuffer();
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("locNo",locNo);
			map.put("uuId", uuId);
			List<String> resultList=cmDefcellImportService.selectCmDefwareBy(map);
			if(null!=resultList && resultList.size()>0){
				tempList.addAll(wareNoList);
				List<String> diffList=getDiffrent(resultList,tempList);
				if(diffList.size()>0){
					for(String wNO:diffList){
						sb.append("仓区编码【").append(wNO).append("】不存在\\r\\n");
					}
				}
			}else{
				for (String wNO : wareNoList) {
					sb.append("仓区编码【").append(wNO).append("】不存在\\r\\n");
				}
			}
			return sb;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	/**
	 * 校验库区编码
	 * @param uuId
	 * @param locNo
	 * @param wareNoList
	 * @return
	 * @throws ManagerException
	 */
	private StringBuffer validateAreaNo(String uuId,String locNo,HashSet<String> areaNoList)throws ManagerException{
		try{
			List<String> tempList=new ArrayList<String>();
			StringBuffer sb=new StringBuffer();
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("locNo",locNo);
			map.put("uuId", uuId);
			List<String> resultList=cmDefcellImportService.selectCmDefareaBy(map);
			if(null!=resultList && resultList.size()>0){
				tempList.addAll(areaNoList);
				List<String> diffList=getDiffrent(resultList,tempList);
				if(diffList.size()>0){
					for(String wNO:diffList){
						sb.append("库区编码【").append(wNO).append("】不存在\\r\\n");
					}
				}
			}else{
				for (String wNO : areaNoList) {
					sb.append("库区编码【").append(wNO).append("】不存在\\r\\n");
				}
			}
			return sb;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	private String validateCellNoByUuId(String uuId,String locNo)throws ManagerException{
		String msg="";
		try{
			StringBuffer conSb=new StringBuffer();
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("uuId",uuId);
			map.put("locNo", locNo);
			List<String> resultConBox=cmDefcellImportService.selectCellNoByUuId(map);
			if(null!=resultConBox && resultConBox.size()>0){
				for(String conBoxNo:resultConBox){
					conSb.append("储位【").append(conBoxNo).append("】已经存在\r\n");
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
    private StringBuffer validateType(String key,HashSet<String> list,String type,StringBuffer sb)throws ManagerException{
		try{
			List<LookupDtl> tempList=lookupdMap.get(key);
			for(String str:list){
				boolean f=false;
				for(LookupDtl lookup:tempList){
					if(str.equals(lookup.getItemname())){
						f=true;
					}
				}
				if(!f){
					sb.append(type).append("【").append(str).append("】不存在\\r\\n");
				}
			}
			return sb;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
    private void validateStockAtt(Map<String,Boolean> stockNoBoolean,StringBuffer sb)throws ManagerException{
		try{
			if(stockNoBoolean.size()>0){
				Iterator iter = stockNoBoolean.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					sb.append("通道编码【").append(entry.getKey()).append("】的商品类型、商品品质、混载标志必须一致；\\r\\n");
				}
			}
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
    private String getLookUp(String key,String type)throws ManagerException{
    	String itemValue="";
		try{
			List<LookupDtl> tempList=lookupdMap.get(key);
			for(LookupDtl lookup:tempList){
				if(type.equals(lookup.getItemname())){
					itemValue=lookup.getItemvalue();
				}
			}
			return itemValue;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
}