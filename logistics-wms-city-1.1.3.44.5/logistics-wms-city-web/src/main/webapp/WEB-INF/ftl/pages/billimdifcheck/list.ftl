<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>差异入库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billimdifcheck/billimdifcheck.js?version=1.0.5.3"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body  class="easyui-layout">
      <#-- 工具菜单div -->
      <div data-options="region:'north',border:false" class="toolbar-region">
	       <@p.toolbar id="toolbar"  listData=[
	       				 {"title":"查询","iconCls":"icon-search","action":"billimdifcheck.searchBillImImport()", "type":0},
	                     {"title":"清除","iconCls":"icon-remove","action":"billimdifcheck.searchBillImImportClear()", "type":0},
	                     {"title":"手工关闭","iconCls":"icon-ok","action":"billimdifcheck.overFlocByDif()", "type":4},
	                     {"title":"分批上传","iconCls":"icon-upload","action":"billimdifcheck.bathUpload()", "type":4},
				         {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('差异入库')","type":0}
	                   ]
			  />
	  </div>
	  <div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
               <!--搜索start-->
               <div  data-options="region:'north',border:false" >
                     <div class="search-div">
                         <form name="searchForm" id="searchForm" method="post" class="city-form">
                         	<table>
                         		<tr>
                         			<td class="common-td blank">货主：</td>
                         			<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoCondition"  /></td>
                         			<td class="common-td blank">业务类型：</td>
									<td ><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessTypeCondition" data-options="editable:false,onChange:function(data){
																																						billimdifcheck.loadSupplierNo(data);
																																					}"/></td>
                         			<td class="common-td blank">供&nbsp;应&nbsp;商：</td>
                         			<td><input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNoCondition"  /></td>
                         			<td class="common-td blank">装车单号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNoCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		                 			<td> <input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
                         			<td class="common-td blank">所属品牌：</td>
									<td ><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
                         			<td class="common-td">发货日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="orderDateBegin" id="orderDateBeginCondition" /></td>
                         			<td class="common-line">&mdash;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="orderDateEnd" id="orderDateEndCondition" />   </td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">单据编号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNoCondition" /></td>
                         			<td class="common-td blank">合&nbsp;同&nbsp;号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="poNoCondition" /></td>
                         			<td class="common-td blank"> 预到货日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="requestDateBegin" id="requestDateBeginCondition" /></td>
                         			<td class="common-line">&mdash;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="requestDateEnd" id="requestDateEndCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">厂商入库单号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNoCondition" /></td>
                         			<td class="common-td">创建人：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                         			<td class="common-td">创建日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px"  name="createtmBegin" id="createtmBeginCondition" /></td>
                         			<td class="common-line">&mdash;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEndCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">审核人：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
                         			<td class="common-td blank"> 审核日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="audittmBegin" id="audittmBeginCondition" /></td>
                         			<td class="common-line">&mdash;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEndCondition" />
									<input class="easyui-validatebox ipt" style="width:110px" name="locno" id="locno" type="hidden" />
									</td>
									<input type="hidden" name="status" value="30">
                         		</tr>
                         	</table>
						</form>
                     </div>
               </div>
               <!--显示列表-->
               <div data-options="region:'center',border:false">
                     <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="差异入库列表"
		                   isHasToolBar="false" divToolbar="#locSearchDiv"  height="460"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false" 
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 50, checkbox:true},
			                  {field : 'locno',title : '仓库编码',width : 100,hidden:true},
			                  {field : 'sysNo',title : '品牌库',width : 100,hidden:true},
			                  {field : 'status',title : '状态',width : 70,align:'left',formatter: billimdifcheck.columnStatusFormatter},
			                  {field : 'importNo',title : '单据编号',width : 180},
			                  {field : 'supplierName',title : '供应商',width : 160,align:'left'},
			                  {field : 'ownerNo',title : '货主',width : 120,align:'left', formatter: wms_city_common.columnOwnerFormatter},  
			                  {field : 'sPoNo',title : '合同号',width : 120,align:'left'},
			                  {field : 'transNo',title : '厂商入库单号',width : 180,align:'left'},
			                  {field : 'businessType',title : '业务类型',width : 120,align:'left', formatter: billimdifcheck.businessTypesFormatter},
			                  {field : 'requestDate',title : '预到货日期',width : 80},
			                  {field : 'transTime',title : '最后上传时间',width : 125},
			                  {title:'商品总数',field:'sumQty',width:100,align:'right'},
			                  {field:'receiptQty',title:'收货数量',width:100,align:'right'},
						      {field:'importQty',title:'验收数量',width:100,align:'right'},
						      {field:'differQty',title:'差异数量',width:100,align:'right'},
			                  {field : 'creator',title : '创建人',width : 100,align:'left'},
			                  {field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 125,sortable:true},
			                  {field:'auditor',title : '审核人',width : 100,align:'left'},
			                  {field:'auditorName',title : '审核人名称',width : 100,align:'left'},
 			                  {field:'audittm',title:'审核时间',width:125,sortable:true}
			                 ]"   
			                 jsonExtend='{
		                     	//url:BasePath+"/billimimport/list.json?status=30",
					           	//method:"post",
		    				   	//queryParams:{"locno": billimdifcheck.locno,
				                // "orderDateBegin": billimdifcheck.getDate(-6),
				                // "orderDateEnd": billimdifcheck.getDate(0)
						       //	},
		                     	onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  billimdifcheck.showView(rowData,rowIndex);
				                }
		                     }'
				           />
               </div>
         </div>
    </div>
    
<#-- 查看详情窗口 - 带尺码横排  -->
<div id="showViewDialog"  class="easyui-dialog" title="查看" 
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false" style="overflow: hidden;"> 
		      <div class="easyui-layout" data-options="fit:true">
		     		<div data-options="region:'north',border:false">
		     			<@p.toolbar  id="printtoolbar"  listData=[
	                             {"id":"btn-add-edit","title":"打印预览","iconCls":"icon-print","action":"billimdifcheck.printBillImImport()", "type":0}
		                       ]
					  	/>
					  	<div style="padding:8px;">
						     <form name="dataViewForm" id="dataViewForm" method="post" class="city-form">
						     	<table>
				                 		<tr>
				                 			<td class="common-td">单据编号：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNo" required="true"  readOnly='readOnly'  missingMessage='单据编号为必填项!' /></td>
				                 			<td class="common-td blank">合同号：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="poNo" /></td>
				                 			<td class="common-td blank"> 供应商：</td>
				                 			<td>
				                 				<input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNo" required="true"/>
				                 				<input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoHide" type="hidden"/>
				                 			</td>
				                 			<td class="common-td blank">厂商入库单号</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNo"  data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
				                 		</tr>
				                 		<tr>
				                 			<td class="common-td">发货日期：</td>
				                 			<td><input class="easyui-datebox" style="width:120px" name="orderDate" id="orderDate"  required="true"/> </td>
				                 			<td class="common-td blank">货主：</td>
				                 			<td>
				                 				<input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" required="true" />
				                 				<input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/>
				                 			</td>
				                 			<td class="common-td blank">品牌库：</td>
				                 			<td>
				                 				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo" required="true" />
										 		<input class="easyui-validatebox ipt" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
				                 			</td>
				                 			<td class="common-td blank">预到货日期：</td>
				                 			<td><input class="easyui-datebox" style="width:120px" name="requestDate" id="requestDate"  required="true"/></td>
				                 		</tr>
				                 		<tr>
				                 			<td>备注</td>
				                 			<td  colspan="7"><input class="easyui-validatebox ipt" style="width:100%" name="importRemark" id="importRemark"/></td>
				                 		</tr>
				                    </table>
							 </form>	
						</div>
		     		</div>
		     		<div data-options="region:'center',border:false" >
		     			<div id="main_imimport_DetailId" class="easyui-tabs" data-options="fit:true,border:false">   
							<div id = 'detailViewDiv' title='尺码横排' > 
								 <@p.datagrid id="moduleView" name="" loadUrl="" saveUrl="" defaultColumn=""  
								 		isHasToolBar="false"  columnsJsonList=""  divToolbar="#"  onClickRowEdit="false" singleSelect="true" pageSize='20'   
										pagination="true" rownumbers="true"  jsonExtend="{}" height="500" showFooter="true"
								 /> 
							</div>
							<div id = 'detailViewDiv_dtl' title='明细' >
								 <@p.datagrid id="moduleView_dtl" name="" loadUrl="" saveUrl="" defaultColumn="" 
							 			isHasToolBar="false"  height="500"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
										pagination="true" rownumbers="true" showFooter="true"
							 			columnsJsonList="[
						                   {title:'商品编码',field:'itemNo',width:150},
						                   {title:'颜色',field:'colorName',width:80,align:'left'},
						                   {title:'尺码',field:'sizeNo',width:100,align:'left'},
						                   {title:'商品名称',field:'itemName',width:150,align:'left'},
						                   {title:'箱号',field:'boxNo',width:120,align:'left'},
						                   {title:'装车单号',field:'deliverNo',width:120,align:'left'},
						                   {title:'品牌',field:'brandName',width:100,align:'left'},
						                   {title:'计划入库数量',field:'poQty',width:100,align:'right'},
						                   {title:'收货数量',field:'receiptQty',width:100,align:'right'},
						                   {title:'验收数量',field:'importQty',width:100,align:'right'},
						                   {title:'差异数量',field:'differQty',width:100,align:'right'},
						                   {title:'分批上传时间',field:'transTime',width:130}
							 			]"
							 		/>
							</div>
						</div>
		     		</div>
		      </div>
		      
</div>	
<#--  预到货通知单  -->
</body>
</html>