package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.dto.bill.BillOmDivideDtlDto;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * @分货单主表
 * @author su.yq
 */
public class BillOmDivide extends BillOmDivideKey {

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date operateDate;// 分货日期

	private String divideType;// 分货类型

	private String batchNo;// 批次

	private String status;// 状态

	private String creator;// 创建人
	
	private String creatorname;// 创建人名称

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;// 创建日期

	private String editor;// 编辑人
	
	private String editorname;// 编辑人名称

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;// 编辑日期
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date expDate;// 出货日期

	private String auditor;// 审核人
	
	private String auditorname;// 审核人名称

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;// 审核时间
	
	private String businessType;

	/** 附加属性 **/

	private String statusStr;// 状态显示

	private String locnoStr;// 委托业主显示

	private Date startCreatetm;// 起始创建日期

	private Date endCreatetm;// 结束创建日期

	private Date startAudittm;// 起始审核日期

	private Date endAudittm;// 结束审核日期

	private Date startExpDate;// 起始出货日期

	private Date endExpDate;// 结束出货日期

	private String assignNames;// 分货人
	private String assignNamesCh;// 分货人

	private String ruleNo;// 按箱分货规则编码

	private String strOutMsg;// 存储过程返回信息

	private String importNo;// 预到货通知单编码

	private String receiptNo;// 收货单号
	
	private String assignName;
	private String assignNameCh;

//	private List<BillOmDivideDtlDto> listBillOmDivideDtlDto;// 分货单明细集合

	private List<BillOmDivideDtl> listBillOmDivideDtl;// 分货单明细集合
	
	private int userid;//用户ID 
	
	private String ownerNo;//委托业主

	private String expNo;
	
	private String ruleName;
	
	
	//下面属性用户查询分货单汇总信息时候的数据展示
	
	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
	}

	/**
	 * 总的箱子数
	 */
	private BigDecimal boxTatalCount;
	

	/**
	 * 总的商品数
	 */
	private BigDecimal itemTotalCount;
	/**
	 * 分货单存储过程传入分货单号
	 */
	private String divideNoS;
	
	private String flag;
	
	private String serialNo;
	
	private String brandNo;
	
	private String businessTypeStr;
	
	public BigDecimal getBoxTatalCount() {
		return boxTatalCount;
	}

	public void setBoxTatalCount(BigDecimal boxTatalCount) {
		this.boxTatalCount = boxTatalCount;
	}

	public BigDecimal getItemTotalCount() {
		return itemTotalCount;
	}

	public void setItemTotalCount(BigDecimal itemTotalCount) {
		this.itemTotalCount = itemTotalCount;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getDivideType() {
		return divideType;
	}

	public void setDivideType(String divideType) {
		this.divideType = divideType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Date createtm) {
		this.createtm = createtm;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Date getAudittm() {
		return audittm;
	}

	public void setAudittm(Date audittm) {
		this.audittm = audittm;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getLocnoStr() {
		return locnoStr;
	}

	public void setLocnoStr(String locnoStr) {
		this.locnoStr = locnoStr;
	}

	public Date getStartCreatetm() {
		return startCreatetm;
	}

	public void setStartCreatetm(Date startCreatetm) {
		this.startCreatetm = startCreatetm;
	}

	public Date getEndCreatetm() {
		return endCreatetm;
	}

	public void setEndCreatetm(Date endCreatetm) {
		this.endCreatetm = endCreatetm;
	}

	public Date getStartAudittm() {
		return startAudittm;
	}

	public void setStartAudittm(Date startAudittm) {
		this.startAudittm = startAudittm;
	}

	public Date getEndAudittm() {
		return endAudittm;
	}

	public void setEndAudittm(Date endAudittm) {
		this.endAudittm = endAudittm;
	}

	public Date getStartExpDate() {
		return startExpDate;
	}

	public void setStartExpDate(Date startExpDate) {
		this.startExpDate = startExpDate;
	}

	public Date getEndExpDate() {
		return endExpDate;
	}

	public void setEndExpDate(Date endExpDate) {
		this.endExpDate = endExpDate;
	}

	public String getAssignNames() {
		return assignNames;
	}

	public void setAssignNames(String assignNames) {
		this.assignNames = assignNames;
	}

//	public List<BillOmDivideDtlDto> getListBillOmDivideDtlDto() {
//		return listBillOmDivideDtlDto;
//	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getStrOutMsg() {
		return strOutMsg;
	}

	public void setStrOutMsg(String strOutMsg) {
		this.strOutMsg = strOutMsg;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public List<BillOmDivideDtl> getListBillOmDivideDtl() {
		return listBillOmDivideDtl;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public void setListBillOmDivideDtl(List<BillOmDivideDtl> listBillOmDivideDtl) {
		this.listBillOmDivideDtl = listBillOmDivideDtl;
	}

	public String getDivideNoS() {
		return divideNoS;
	}

	public void setDivideNoS(String divideNoS) {
		this.divideNoS = divideNoS;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}

	public String getAuditorname() {
		return auditorname;
	}

	public void setAuditorname(String auditorname) {
		this.auditorname = auditorname;
	}

	public String getAssignNamesCh() {
		return assignNamesCh;
	}

	public void setAssignNamesCh(String assignNamesCh) {
		this.assignNamesCh = assignNamesCh;
	}

	public String getAssignNameCh() {
		return assignNameCh;
	}

	public void setAssignNameCh(String assignNameCh) {
		this.assignNameCh = assignNameCh;
	}

	public String getBusinessTypeStr() {
		return businessTypeStr;
	}

	public void setBusinessTypeStr(String businessTypeStr) {
		this.businessTypeStr = businessTypeStr;
	}
	
	
	
	// 分货人员赋值
//	public void setListBillOmDivideDtlDto(List<BillOmDivideDtlDto> listBillOmDivideDtlDto) {
//		this.listBillOmDivideDtlDto = listBillOmDivideDtlDto;
//		if(CommonUtil.hasValue(listBillOmDivideDtlDto)){
//			String assignNamesStr = "";
//			if (CommonUtil.hasValue(listBillOmDivideDtlDto)) {
//				for (BillOmDivideDtlDto b : listBillOmDivideDtlDto) {
//					if(b!=null){
//						if (StringUtils.isNotBlank(b.getAssignName())) {
//							assignNamesStr += b.getAssignName() + ",";
//						}
//					}
//				}
//				if (StringUtils.isNotBlank(assignNamesStr)) {
//					assignNamesStr = assignNamesStr.substring(0,assignNamesStr.length() - 1);
//				}
//			}
//			this.assignNames = assignNamesStr;
//		}
//	}
}