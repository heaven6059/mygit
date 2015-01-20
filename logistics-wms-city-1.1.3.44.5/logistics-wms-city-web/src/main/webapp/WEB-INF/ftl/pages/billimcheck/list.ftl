<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>验收单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billimcheck/billimcheck.js?version=1.0.5.3"></script>    

</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
	    <@p.toolbar id="maintoolbar"  listData=[
	         {"title":"查询","iconCls":"icon-search","action":"billimcheck.searchData()","type":0},
	         {"title":"清空","iconCls":"icon-remove","action":"billimcheck.clearSearch()","type":0},
			 {"title":"新增","iconCls":"icon-add","action":"billimcheck.addUI();","type":1},
			 {"title":"修改","iconCls":"icon-edit","action":"billimcheck.editUI();","type":2},
			 {"title":"删除","iconCls":"icon-del","action":"billimcheck.deleteRows()","type":3},
			 {"title":"审核","iconCls":"icon-aduit","action":"billimcheck.check();","type":4},
			 {"title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
			 ]
		  />	
	</div>
    <div data-options="region:'center',border:false">	
    	<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
		   				<tr>
		   					<td class="common-td blank">状态：</td>
		   					<td><input class="easyui-combobox" style="width:120px" name="status" id="status"/></td>
		   					<td class="common-td blank">创建人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" name="creator" id="creatorCondition"/></td>
		   					<td class="common-td blank">创建日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">单据编号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"  /></td>
		   					<td class="common-td blank">审核人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" name="auditor"  id="auditorCondition"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startAudittm"  id="startAudittm"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">收货单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="sImportNo" /></td>
		   					<td class="common-td blank">验收人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" name="checkWorker"  id="checkWorkerCondition"/></td>
		   					<td class="common-td blank">验收日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="checkStartDate"  id="checkStartDate" /></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="checkEndDate" id="checkEndDate"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">货主：</td>
		   					<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoCondition"/></td>
		   					<td class="common-td blank">预到货通知单：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo"  id="importNoCondition"/></td>
		   					<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                 			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
                 			<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="searchBrandNo" /></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">来源单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNoCondition"/></td>
		   					<td class="common-td blank">业务类型：</td>
		   					<td><input class="easyui-combobox" style="width:120px" name="businessType" id="businessType"/></td>
		   					<td class="common-td blank"></td>
							<td></td>
		   					<td class="common-td blank"></td>
                 			<td></td>
		   				</tr>
		   			</table>
				</form>	
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl=""
               isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
	           rownumbers="true" title="验收单列表" showFooter="true" saveUrl=""  defaultColumn=""
	           columnsJsonList="[
	           		{field : 'id',checkbox:true},
	           		{field : 'status',title : '状态 ',align:'left',width : 80,formatter:billimcheck.statusFormatter},
	                {field : 'checkNo',title : '单据编号',width : 180},
	                {field : 'sImportNo',title : '收货单号',width : 180},
	                {field : 'ownerNo',title : '货主 ',width : 100,align:'left',formatter:billimcheck.ownerNoFormatter},
	                {field : 'showBusinessType',title : '业务类型',width : 100},
	                {field : 'sourceTypeName',title : '单据类型',width : 100},
	                {field : 'checkEndDate',title : '验收日期',width : 120},
	                {field : 'checkWorker',title : '验收人',width : 90,align:'left'},
	                {field : 'checkName',title : '验收人名称',width : 100,align:'left'},
	                {field : 'sourceNo',title : '来源单号(复核单号)',width : 180,align:'left'},
	                {field : 'totalPoQty',title : '总计划数',width : 80,align:'left'},
	                {field : 'totalCheckQty',title : '总验收数',width : 80,align:'left'},
	                {field : 'totalDiffQty',title : '总差异数',width : 80,align:'left'},
	                {field : 'creator',title : '创建人',width : 90,align:'left'},
	                {field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
	                {field : 'createtm',title : '创建时间',width : 135,sortable:true}
	            ]" 
		        jsonExtend='{
		        onLoadSuccess:function(data){//合计
					billimcheck.onLoadSuccess(data);
				},
		        onDblClickRow:function(rowIndex, rowData){
                	  // 触发点击方法  调JS方法
                   	  billimcheck.loadDetail(rowData);
                }}'/>
			</div>
		</div>	
	</div>     
<div id="openUI" class="easyui-window" data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="detail-toolbar"  listData=[
		         {"id":"save_main","title":"保存","iconCls":"icon-save","action":"billimcheck.saveMainInfo()","type":1},
		         {"id":"edit_main","title":"修改","iconCls":"icon-edit","action":"billimcheck.editMainInfo()","type":2},
				 {"title":"取消","iconCls":"icon-cancel","action":"billimcheck.closeUI()","type":0}
				 ]
		  	/>
			<form name="dataForm" id="dataForm" method="post" class="city-form">
				<input type="hidden" id="opt"/>
	  			<input type="hidden" id="supplierNo" name="supplierNo"/>
	  			<table>
	  				<tr>
	  					<td class="common-td blank">单据编号：</td>
	  					<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo" id="checkNo"   readOnly="readOnly"/></td>
	  					<td class="common-td blank">收货单号：</td>
	  					<td><input class="easyui-validatebox ipt" style="width:120px" name="sImportNo" id="sImportNo" readOnly="readOnly" data-options="required:true"/><input type="button" size="3" value="..." id="importNoBtn" onclick="javascript:billimcheck.openimreceiptWin();"></td>
	  					<td class="common-td blank">货主：</td>
	  					<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo"  data-options="required:true" /></td>
	  					<td class="common-td blank">验收日期：</td>
	  					<td><input class="easyui-datebox" style="width:120px" name="checkEndDate" id="checkEndDate_form" required="required" /></td>
	  				</tr>
					<tr>
						<td class="common-td blank">验收人：</td>
						<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" id="checkWorkerForAdd" name="checkWorker" /></td>
						<td class="common-td blank">备注：</td>
						<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="remark" id="remark" /></td>
					</tr>	  				
	  			</table>
			</form>
		</div>
		<div data-options="region:'center'">
			<@p.toolbar id="opBtn"  listData=[
				{"title":"新增明细","iconCls":"icon-add-dtl","action":"billimcheck.openItemSlect()","type":0},
				{"title":"删除明细","iconCls":"icon-del-dtl","action":"billimcheck.deleterow()","type":0}
		        {"title":"保存明细","iconCls":"icon-save-dtl","action":"billimcheck.manage()","type":0}
				]
		  	/>
			<@p.datagrid id="itemDetail" name=""  loadUrl="" saveUrl="" defaultColumn=""  onClickRowAuto="false"
 				isHasToolBar="false"  divToolbar="#opBtn"  height="400"  onClickRowEdit="false" 
 				singleSelect="true"  pagination="true" rownumbers="true" emptyMsg="" title="商品明细"
 				checkOnSelect="false"  selectOnCheck="false"
 				columnsJsonList="[
 				{field : 'id',checkbox:true},
 				{field:'sysNo',hidden:true},
 				{field:'barcode',hidden:true},
 				{field:'brandNo',hidden:true},
 				{field:'itemType',hidden:true},
 				{field:'quality',hidden:true},
 			    {field:'packQty',title:'箱数',width:70,hidden:true},
 				{field:'itemNo',title:'商品编码',width:150,align:'left'},
 				{field:'itemName',title:'商品名称',width:150,align:'left'},
			    {field:'sizeNo',title:'尺码',width:80,align:'left'},
			    {field:'itemTypeStr',title:'商品属性',width:80,align:'left'},
			    {field:'qualityStr',title:'品质',width:60},
			    {field:'boxNo',title:'箱号',width:120,align:'left',
			    		editor:{
							type:'combobox',
							options:{
							    valueField:'boxNo',
							    textField:'boxNo',
							    panelHeight:150,
							    required:true
							}
						}},
				{field:'panNo',title:'托盘号',width:130,align:'left'},
			    {field:'poQty',title:'计划数量',width:60,align:'right'},
 				{field:'checkQty',title:'验收数量',width:60,align:'right',
			    	editor:{
			    		type:'validatebox'
			    	}},
 				{field:'sourceType',title:'sourceType',width:150,hidden:true}
 			]"
 			
 			jsonExtend='{onClickRow:function(rowIndex, rowData){
                	  // 触发点击方法  调JS方法
                   	 billimcheck.onClickRowReceiptDtl(rowIndex,rowData);
                }}'/>
		</div>
	</div>	    
</div>

<div id="openUIShow" class="easyui-window" title="详情"
   data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
   minimizable:false,maximizable:false,maximized:true">
   	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="padding:5px;">
			<form name="dataForm" id="dataFormShow" metdod="post"  class="city-form">
				<table>
					<tr>
						<td class="common-td blank">单据编号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"  readOnly="readOnly"/></td>
						<td class="common-td blank">收货单号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="sImportNo" /></td>
						<td class="common-td blank">货主：</td>
						<td><input class="easyui-combobox" style="width:120px" name="ownerNo"  data-options="required:true" /></td>
						<td class="common-td blank">验收日期：</td>
						<td><input class="easyui-datebox" type="text" style="width:120px" name="checkEndDate" /></td>
					</tr>
					<tr>
						<td class="common-td blank">验收人：</td>
						<td><input class="easyui-combobox" style="width:120px" name="checkWorker" id="checkWorkerForShow" /></td>
						<td class="common-td blank">备注：</td>
						<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="remark" id="remark" /></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<@p.datagrid id="itemDetailShow" name=""  loadUrl="" saveUrl="" defaultColumn="" 
	 			isHasToolBar="false"  height="430"  onClickRowEdit="false" singleSelect="true"   
				pagination="true" rownumbers="true" title="商品明细" showFooter="true"
	 			columnsJsonList="[
	 				{field:'itemNo',title:'商品编码',width:150,align:'left',sortable:true},
	 				{field:'itemName',title:'商品名称',width:150,align:'left',formatter:billimcheck.itemFormatter,editor:{type:'readonlytext'}},
				    {field:'sizeNo',title:'尺码',width:60,editor:{type:'readonlytext'},sortable:true},
				    {field:'itemTypeStr',title:'商品属性',width:80,align:'left',sortable:true},
			    	{field:'qualityStr',title:'品质',width:80,align:'left',sortable:true},
				    {field:'boxNo',title:'箱号',width:120,align:'left',sortable:true},
				    {field:'loadboxno',title:'装箱箱号',width:120,align:'left',sortable:true},
				    {field:'panNo',title:'托盘号',width:130,align:'left'},
				    {field:'poQty',title:'计划数量',width:60,align:'right',editor:{type:'readonlytext'}},
	 				{field:'checkQty',title:'验收数量',width:60,align:'right'},
	 				{field:'difQty',title:'差异数量',width:60,align:'right'},
	 				{field:'directQty',title:'已定位数量',width:80,align:'right'}
	 			]"/>
		</div>
	</div>
</div>
<div id="openUIItem" class="easyui-window"
 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="item-toolbar"  listData=[
		         {"title":"查询","iconCls":"icon-search","action":"billimcheck.searchItem()","type":0},
				 {"title":"清空","iconCls":"icon-remove","action":"billimcheck.searchItemClear()","type":0},
				 {"title":"确定","iconCls":"icon-ok","action":"billimcheck.selectItemOK()","type":0},
				 {"title":"取消","iconCls":"icon-cancel","action":"billimcheck.closeItemSelect()","type":0}
				 ]
		  	/>
		  	<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form">
		 		<input type="hidden" name="locno" />
		 		<table>
		 			<tr>
		 				<td class="common-td blank">商品状态：</td>
		 				<td><input class="easyui-validatebox ipt" style="width:120px" name="itemStatus" id="itemStatus"   disable="true"/></td>
		 				<td class="common-td blank">商品编码：</td>
		 				<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo"   disable="true"/></td>
		 				<td class="common-td blank">商品名称：</td>
		 				<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemName"   disable="true"/></td>
		 				<td class="common-td blank">商品条码：</td>
		 				<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode"   disable="true"/></td>
		 				</tr>
		 			<tr>
		 				<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                 		<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo"/></td>
                 		<td class="common-td blank">所属品牌：</td>
						<td colspan="5"><input class="easyui-combobox ipt" style="width:500px" name="brandNo" id="brandNo" /></td>
		 			</tr>
		 		</table>
			 </form>
		</div>
		<div data-options="region:'center',border:false">
			<@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
	           isHasToolBar="false" divToolbar="#itemSearchDiv" height="300"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
	           rownumbers="false"
	           columnsJsonList="[
	           		{field : 'id',checkbox:true},
	           		{field:'brandNo',hidden:true},
	           		{field : 'packQty',title:'箱数',width:70,hidden:true},
	           		{field:'boxNo',title:'箱号',width:125,hidden:true},
	           		{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
	                {field : 'itemName',title : '商品名称',width : 150,align:'left'},
	                {field : 'barcode',title : '商品条码',width : 150,align:'left'},
	                {field : 'colorName',title : '颜色',width : 90,align:'left'},
	                {field : 'sizeNo',title : '尺码',width : 90,align:'left'},
	                {field : 'brandName',title : '品牌',width : 90,align:'left'}
	            ]" 
		        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
	            	  // 触发点击方法  调JS方法
	            	//billimcheck.selectItemOK(rowData);
	            }}'/>
		</div>
	</div>
</div>

<div id="receiptSelect" class="easyui-window" 
 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="receipt-toolbar"  listData=[
		         {"title":"查询","iconCls":"icon-search","action":"billimcheck.searchReceipt()","type":0},
		         {"title":"清空","iconCls":"icon-remove","action":"billimcheck.searchLocClear()","type":0},
				 {"title":"确定","iconCls":"icon-ok","action":"billimcheck.selectReceiptOK()","type":0},
				 {"title":"取消","iconCls":"icon-cancel","action":"billimcheck.closeReciiptSelect()","type":0}
				 ]
		  	/>
			<form name="receiptSelectSearchForm" id="receiptSelectSearchForm" metdod="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">收货单号：</td>
						<td><input class="easyui-validatebox ipt" style="width:130px" name="receiptNo" id="receiptNo"   disable="true"/></td>
						<td class="common-td blank">收货人：</td>
						<td><input class="easyui-validatebox ipt" style="width:130px" name="receiptWorker" id="receiptWorker"   disable="true"/></td>
						<td class="common-td blank">车牌号：</td>
						<td><input class="easyui-validatebox ipt" style="width:130px" name="carPlate" id="carPlate"   disable="true"/></td>
					</tr>
					<tr>
						<td class="common-td blank">收货日期：</td>
						<td><input class="easyui-datebox" style="width:130px" name="startCreatetm" id="startCreatetmCondition" /></td>
						<td class="common-line">&mdash;</td>
						<td colspan="3"><input class="easyui-datebox" style="width:130px" name="endCreatetm" id="endCreatetmCondition" /></td>
					</tr>
				</table>
		 		<input type="hidden" name="locno"/>
			 </form>
		</div>
		<div data-options="region:'center',border:false">
			<@p.datagrid id="dataGridJGreceiptSelect"  loadUrl=""  saveUrl=""  defaultColumn="" 
	           isHasToolBar="false"   onClickRowEdit="false"  pagination="true" singleSelect = "true"
	           rownumbers="false"
	           columnsJsonList="[
	           		{field : 'receiptNo',title : '收货单号 ',width : 180},
	                {field : 'recivedate',title : '收货日期',width : 100},
	                {field : 'receiptWorker',title : '收货人',width : 100,align:'left'},
	                {field : 'receiptName',title : '收货人名称',width : 100,align:'left'},
	                {field : 'supplierName',title : '供应商',width : 160,align:'left'},
	                {field : 'ownerName',title : '货主 ',width : 90,align:'left'},
	                {field : 'carPlate',title : '车牌号 ',width : 90,align:'left'},
	                {field : 'receiptqty',title : '商品总数',width : 100,align:'right'}
	            ]" 
		        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
	            	  // 触发点击方法  调JS方法
	            	billimcheck.selectReceiptOK(rowData);
	            }}'/>
		</div>
	</div>
</div>
</body>
</html>