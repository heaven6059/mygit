<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>收货差异登记</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billimdifrecord/billimdifrecord.js?version=1.0.6.2"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"billimdifrecord.searchData();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billimdifrecord.clearSearch('searchForm');", "type":0},
	        {"title":"新增","iconCls":"icon-add","action":"billimdifrecord.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"billimdifrecord.updateUI();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"billimdifrecord.deleteUI();","type":3},
	        {"title":"审核","iconCls":"icon-aduit","action":"billimdifrecord.check();","type":4},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
				                <td class="common-td blank">创&nbsp;建&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
				                <td class="common-td blank">创建日期：</td>
				                <td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
				                <td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
				                <td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetmCondition"
				                 	data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
				            	<td class="common-td">单据编号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="defRecordNo" id="defRecordNoCondition" /></td>
				                <td class="common-td blank">审&nbsp;核&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
				                <td class="common-td blank">审核日期：</td>
				                <td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
				                <td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
				                <td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
				            	<td class="common-td">预到货通知单：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNoCondition" /></td>
				                <td class="common-td blank">供&nbsp;应&nbsp;商：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNoCondition" /></td>
				                <td class="common-td blank">业务类型：</td>
								<td ><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessTypeCondition" /></td>
				                <td class="common-td blank"></td>
				                <td></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl="/bill_im_dif_record/list.json?locno=${session_user.locNo}"
	    	  		saveUrl=""  defaultColumn=""  title="收货差异列表"
		    		isHasToolBar="false" divToolbar="" onClickRowEdit="false"  pagination="true" singleSelect = "false"
					rownumbers="true" emptyMsg=""
					columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'status',title : '状态 ',width : 80,formatter:billimdifrecord.statusFormatter,align:'left'},
			                {field : 'defRecordNo',title : '单据编号',width : 180},
			                {field : 'difType',title : '来源类型',width : 80,align:'left',formatter:billimdifrecord.typeFormatter},
			                {field : 'transNo',title : '预到货通知单',width : 130},
			                {field : 'ownerNo',title : '货主',width : 80,align:'left',formatter:billimdifrecord.ownerNoFormatter},
			                {field : 'supplierNo',title : '供应商编号',hidden : true,align:'left'},
			                {field : 'supplierName',title : '供应商',width : 150,align:'left'},
			                {field : 'poNo',title : '厂商入库单',width : 100,align:'left'},
			                {field : 'sPoNo',title : '合同号',width : 100,align:'left'},
			                {field : 'businessType',title : '业务类型',width : 120,align:'left', formatter: billimdifrecord.businessTypesFormatter},
			                {field : 'recordDate',title : '登记日期 ',width : 100},
			               	{field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130},
			                {field : 'remark',title : '备注',hidden : true,align:'left'}
			            ]" 
					jsonExtend='{onDblClickRow:function(rowIndex, rowData){
				        	// 触发点击方法  调JS方法
				        	billimdifrecord.dtlView(rowData,"view");
					}}'
				/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 查看明细信息div BEGIN -->
	<div id="openDtlUI"  class="easyui-dialog" title="明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="viewtoolbar"   listData=[
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billimdifrecord.closeWindow('openDtlUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
				
					<form name="detailForm" id="detailForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td blank">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="defRecordNo" id="defRecordNoView" readOnly="readOnly"/></td>
								<td class="common-td blank">预到货通知单：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNoView" data-options="required:true" readOnly="readOnly"/></td>
								<td class="common-td blank">来源类型：</td>
								<td><input class="easyui-combobox" style="width:120px" name="difType" id="difTypeView" data-options="required:true" /></td>
								<td class="common-td blank">登记日期：</td>
								<td><input class="easyui-datebox ipt" style="width:120px" name="recordDate" id="recordDateView" data-options="required:true" /></td>
							</tr> 
							<tr>
								<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
								<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoView" data-options="required:true" /></td>
								<td class="common-td blank">厂商入库单：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoView" /></td>
								<td class="common-td blank">供&nbsp;应&nbsp;商：</td>
								<td colspan="3"><input class="easyui-combobox" style="width:316px" name="supplierNo" id="supplierNoView" data-options="required:true" /></td>
							</tr>
							<tr>
								<td class="common-td">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
								<td colspan="7"><input class="easyui-validatebox ipt" style="width:720px;" name="importRemark" id="importRemarkView"/></td>
							</tr>             
						</table>
					</form>
				</div>
				
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<@p.datagrid id="detaildataGrid" name=""   loadUrl="" saveUrl="" defaultColumn="" title="收货差异明细"
					isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="true" pageSize='20'  
					pagination="true" rownumbers="true" emptyMsg="" 
					columnsJsonList="[
					 	{field:'id',width : 50, checkbox:true},
						{field:'itemNo',title:'商品编码',width:130,align:'left'},
						{field:'itemName',title:'商品名称',width : 130,align:'left'},
						{field:'sizeNo',title:'尺码',width:80,align:'left'},
						{field:'brandNoStr',title:'品牌',width:100,align:'left'},
						{field:'colorName',title:'颜色',width:80,align:'left'},
						{field:'qty',title:'数量',width:80,align:'right'}
				 	]"
				/>
			</div>
			<#--显示列表end-->	
			</div>
		</div>
		<#-- 查看明细信息div END -->
		
	<#-- 新增  窗口 -->
	<div id="openUI" class="easyui-window" data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true"  title="新增" >
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<#-- 工具菜单 start -->
		    	<@p.toolbar id="addtoolbar"   listData=[
					{"id":"save_main","title":"保存","iconCls":"icon-save","action":"billimdifrecord.saveMain();", "type":1},
					{"id":"info_save","title":"修改","iconCls":"icon-edit","action":"billimdifrecord.editMain();", "type":2},
					{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billimdifrecord.closeWindow('openUI');","type":0}
				]/>
		  	<#-- 工具菜单END -->
		  	<#--查询 start-->
			<form name="dataForm" id="dataForm" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">单据编号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="defRecordNo" id="defRecordNo" readOnly="readOnly"/></td>
						<td class="common-td blank">预到货通知单：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNo" data-options="required:true" readOnly="readOnly"/><input type="button" size="3" value="..." id="importNoBtn" onclick="javascript:billimdifrecord.openimreceiptWin();"></td>
						<td class="common-td blank">来源类型：</td>
						<td><input class="easyui-combobox" style="width:120px" name="difType" id="difType" data-options="required:true" /></td>
						<td class="common-td blank">登记日期：</td>
						<td><input class="easyui-datebox ipt" style="width:120px" name="recordDate" id="recordDate" data-options="required:true" /></td>
					</tr> 
					<tr>
						<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" data-options="required:true" /></td>
						<td class="common-td blank">厂商入库单：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNo" /></td>
						<td class="common-td blank">供&nbsp;应&nbsp;商：</td>
						<td colspan="3"><input class="easyui-combobox" style="width:313px" name="supplierNo" id="supplierNo" data-options="required:true" /></td>
					</tr>
					<tr>
						<td class="common-td">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan="7"><input class="easyui-validatebox ipt" style="width:748px;" name="importRemark" id="importRemark"/></td>
					</tr>             
				</table>
			</form>
			<#--查询end-->
		</div>
		<div id = 'editDtl' data-options="region:'center'">
			<@p.toolbar id="toolsDiv" listData=[
		    	{"title":"新增明细","iconCls":"icon-add-dtl","action":"billimdifrecord.showAddItem('editDtlDg');", "type":0},
		   		{"title":"删除明细","iconCls":"icon-del-dtl","action":"billimdifrecord.deleteItem('editDtlDg');", "type":0},
		      	{"title":"保存明细","iconCls":"icon-save-dtl","action":"billimdifrecord.doSave('editDtlDg');","type":0}
		     ]/>
			<@p.datagrid id="editDtlDg" name=""   loadUrl="" saveUrl="" defaultColumn="" title="收货差异登记明细"
				isHasToolBar="false" divToolbar="#toolsDiv"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
				pagination="true" rownumbers="true" emptyMsg="" 
				columnsJsonList="[
				{field:'id',width : 50, checkbox:true},
				{field:'itemNo',title:'商品编码',width:130,align:'left'},
				{field:'itemName',title:'商品名称',width : 130,align:'left'},
				{field:'sizeNo',title:'尺码',width:80,align:'left'},
				{field:'brandNoStr',title:'品牌',width:100,align:'left'},
				{field:'colorName',title:'颜色',width:80,align:'left'},
				{field:'qty',title:'数量',width:80,align:'right',
					editor:{
						type:'numberbox',
						options:{
							required:true,
							missingMessage:'数量为必填项!'
						}
					}
				 }
				]"
			/>
		</div>
	</div>	    
</div>	
	<#-- 新增  窗口 END-->
	<#-- 预到货通知单选择 Begin -->
	<div id="receiptSelect" class="easyui-window" 
 			data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
			maximized:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="receipt-toolbar"  listData=[
		         {"title":"查询","iconCls":"icon-search","action":"billimdifrecord.searchReceipt()","type":0},
		         {"title":"清空","iconCls":"icon-remove","action":"billimdifrecord.searchLocClear()","type":0},
				 {"title":"取消","iconCls":"icon-cancel","action":"billimdifrecord.closeWindow('receiptSelect');","type":0}
				 ]
		  		/>
				<form name="receiptSelectSearchForm" id="receiptSelectSearchForm" metdod="post" class="city-form">
					<table>
						<tr>
							<td class="common-td blank">状态：</td>
							<td><input class="easyui-combobox" style="width:120px" name="status" id="statusDr" disable="true"/></td>
							<td class="common-td blank">预到货通知单：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="receiptNoDr" disable="true"/></td>
							<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
							<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoDr" disable="true"/></td>
							<td class="common-td blank">供应商：</td>
							<td><input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNoDr" disable="true"/></td>
							
						</tr>
						<tr>
							<td class="common-td blank">合同号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="sPoNoDr" disable="true"/></td>
							<td class="common-td blank">厂商入库单：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNoDr" disable="true"/></td>
							<td class="common-td blank">预到货日期：</td>
							<td><input class="easyui-datebox" style="width:120px" name="orderDateBegin" id="startorderDateDr" /></td>
							<td class="common-line">&mdash;</td>
							<td><input class="easyui-datebox" style="width:120px" name="orderDateEnd" id="endorderDateDr" /></td>
						</tr>
						<tr>
							<td class="common-td blank">业务类型：</td>
							<td ><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessTypeForItem" /></td>
							<td class="common-td"></td>
							<td></td>
							<td class="common-td"></td>
							<td></td>
							<td class="common-td"></td>
							<td></td>
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
	           			{field : 'status',title : '状态 ',width : 90,align:'left',formatter:billimdifrecord.columnStatusFormatter},
	           			{field : 'importNo',title : '预到货通知单 ',width : 150},
	                	{field : 'ownerNo',title : '货主 ',width : 90,align:'left',formatter:billimdifrecord.ownerNoFormatter},
			            {field : 'supplierNo',title : '供应商',width : 160,align:'left', formatter: wms_city_common.columnSupplierFormatter},
			            {field : 'sPoNo',title : '合同号',width : 120,align:'left'},
			            {field : 'transNo',title : '厂商入库单',width : 120,align:'left'},
			            {field : 'businessType',title : '业务类型',width : 120,align:'left', formatter: billimdifrecord.businessTypesFormatter},
			            {field : 'orderDate',title : '预到货日期',width : 80},
			            {field : 'creator',title : '创建人',width : 80,align:'left'},
			            {field : 'createtm',title : '创建时间',width : 130}
	            	]"
		        	jsonExtend='{onDblClickRow:function(rowIndex, rowData){
	            	  	// 触发点击方法  调JS方法
	            		billimdifrecord.selectReceiptOK(rowData);
	            	}}'
	            />
			</div>
		</div>
	</div>
	<#-- 预到货通知单选择 END-->
	<#-- 商品选择div -->
		<div id="openUIItem"  class="easyui-dialog" title="商品选择"  
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:true,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="itemtoolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billimdifrecord.searchItem();", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billimdifrecord.clearSearch('itemSearchForm');", "type":0},
	        			{"title":"确定","iconCls":"icon-ok","action":"billimdifrecord.confirmItem();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billimdifrecord.closeWindow('openUIItem');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form">
						 	<input class="easyui-validatebox" name="showGirdNameForItem" id="showGirdNameForItem" disable="true" type ="hidden" />
						 	<table>
								<tr>
							    	<td class="common-td">品&nbsp;牌&nbsp;库：</td>
							        <td ><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo"/></td>
							        <td class="common-td blank">商品编码：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoForItem"/></td>
							        <td class="common-td blank">商品名称：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameForItem"/></td>
							        <td class="common-td blank">商品条码：</td>
							        <td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeForItem"/></td>
							    </tr>
							 </table>
						</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJGItemSelect" name=""   loadUrl="" saveUrl="" defaultColumn="" title="明细"
						isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="true" pageSize='20'  
						pagination="true" rownumbers="true" emptyMsg="" 
						columnsJsonList="[
						 	{field : 'ck',title : '',width : 50, checkbox:true},
			           		{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
			                {field : 'itemName',title : '商品名称',width : 130,align:'left'},
			                {field : 'sizeNo',title : '尺码',width : 60,align:'left'},
			                {field : 'barcode',title : '商品条码',width : 150,align:'left'},
			                {field : 'colorNo',title : '颜色编码',hidden : true,align:'left'},
			                {field : 'colorName',title : '颜色',width : 90,align:'left'},
			                {field : 'sysNo',title : '品牌库编码',hidden : true,align:'left'},
			                {field : 'brandNo',title : '品牌编码',hidden : true,align:'left'},
			                {field : 'brandNoStr',title : '品牌',width : 90,align:'left'}
					 	]"
					/>
				</div>
				<#--显示列表end-->
			</div>			
		</div>
		<#-- 商品选择END -->
		
</body>
</html>