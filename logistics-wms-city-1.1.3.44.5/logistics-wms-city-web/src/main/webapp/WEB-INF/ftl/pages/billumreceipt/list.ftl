<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓收货单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billumreceipt/billumreceipt.js?version=1.0.8.4"></script>
</head>

<body class="easyui-layout">

	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billumreceipt.searchData()", "type":0},
		 		{"title":"清除","iconCls":"icon-remove","action":"billumreceipt.searchClear('searchForm')", "type":0},
				{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billumreceipt.addUI();","type":1},
			    {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billumreceipt.toUpdate()","type":2},
			    {"id":"btn-check","title":"审核","iconCls":"icon-aduit","action":"billumreceipt.check()","type":2},
			    {"id":"btn-close","title":"删除","iconCls":"icon-del","action":"billumreceipt.doDel()","type":3},
	            {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退仓收货单');","type":0}
	    ]/>	
	</div>
	
	
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
		
			<#-- 查询条件 start -->
			<div  data-options="region:'north',border:false" >
				<div id="searchDiv" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td">状态：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition"/></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition"/></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
								<td class="common-line" width="50">&mdash;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetmCondition" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo" id="receiptNoCondition" /></td>
								<td class="common-td blank">审核人：</td>
								<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
								<td class="common-line">&mdash;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							
							<tr>
								<td class="common-td">店退仓单：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" id="untreadNoCondition" /></td>
								<td class="common-td blank">退仓通知单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo" id="untreadMmNoCondition" /></td>
								<td class="common-td blank" >商品类型：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemType_search"  style="width:120px"/></td>
								<td class="common-td blank">品质：</td>
								<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>
							</tr>
							<tr>
								
								<td class="common-td">货主：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoCondition" /></td>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td><td class="common-td blank">所属品牌：</td>
								<td colspan="3"><input class="easyui-combobox ipt" style="width:293px" name="brandNo" id="brandNo" /></td>
							</tr>
						</table>
					</form>	
				</div>
			</div>
			<#-- 查询条件 end -->
			
			<div  data-options="region:'center',border:false" >
				<input type="hidden" id="action"/>
				<@p.datagrid id="dataGridJG"  loadUrl=""  emptyMsg="" 
          	  		   saveUrl=""  defaultColumn=""  title="退仓收货单列表"
		               isHasToolBar="false" divToolbar="" height="420"  
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" showFooter="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'status',title : '状态 ' ,align:'left',width : 80,formatter:billumreceipt.statusFormatter},
			                {field : 'receiptNo',title : '单据编号',width : 180},
			                {field : 'ownerNo',title : '货主',align:'left',width : 80,formatter:billumreceipt.ownerNoFormatter},
			                {field : 'untreadNo',title : '店退仓单号',width : 180},
			                {field : 'itemType',title : '商品类型',width : 100,align:'left',formatter:billumreceipt.untreadTypeFormatter},
			                {field : 'quality',title : '品质',width : 80,align:'left',formatter:billumreceipt.qualityFormatter},
			                {field : 'storeNo',title : '客户',width : 80,align:'left'},
			                {field : 'storeName',title : '客户名称',width : 150,align:'left'},
			                {field : 'realQty',title : '实际数量',width : 60,align:'right'},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 140},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 140},
			                {field : 'remark',title : '备注',width : 140,align:'left'}
			            ]" 
				        jsonExtend='{onLoadSuccess:function(data){
						billumreceipt.onLoadSuccess(data);
					},onDblClickRow:function(rowIndex, rowData){
		    				// 触发点击方法  调JS方法
		        			billumreceipt.viewDetail(rowData);
				}}'/>
			</div>
		</div>
	
	</div>
		
		
	<#-- 新增修改页面 -->
	<div id="openUI" class="easyui-window" 
	    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
	    	minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					
					<#-- 工具菜单div -->
		            <@p.toolbar id="addtoolbar"  listData=[
		            	{"id":"save_main","title":"保存","iconCls":"icon-save","action":"billumreceipt.saveMainInfo()", "type":0},
		                {"id":"btn-close","title":"取消","iconCls":"icon-cancel","action":"billumreceipt.closeWindow('openUI')","type":0}
		            ]/>
					
					<form name="dataForm" id="dataForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo" id="receiptNo" readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" data-options="required:true"/></td>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" id="untreadNo" data-options="required:true" readOnly="readOnly"/></td>
								<td class="common-td blank">店退仓通知单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo" id="untreadMmNo" data-options="required:true" readOnly="readOnly"/></td>
							</tr>			
							<tr>
								<td class="common-td">退仓类型：</td>
								<td><input class="easyui-combobox" style="width:120px" name="itemType" id="itemType"  data-options="required:true,validType:['vLengtd[0,10,\'最多只能输入10个字符\']']" /></td>
								<td class="common-td blank">品质：</td>
								<td><input class="easyui-combobox" style="width:120px" name="quality" id="quality"  data-options="validType:['vLengtd[0,32,\'最多只能输入32个字符\']']" /></td>
								<td class="common-td blank">客户：</td>
								<td><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNo"  data-options="validType:['vLengtd[0,32,\'最多只能输入32个字符\']']"  /></td>
								<td  colspan='2' style="text-align:right;"><a id="select_untread_but" href="javascript:billumreceipt.openSelectUntread();" class="easyui-linkbutton" data-options="iconCls:'icon-download'">选择店退仓单</a></td>
							</tr>
							<tr>
								<td class="common-td">备注：</td>
								<td colspan='7'><input class="easyui-validatebox ipt" style="width:100%;" name="remark" id="remark"  /></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div id="divUmReceiptDtl" data-options="region:'center',border:false">
					<div id="toolsDiv">
						<@p.toolbar id="toolbarbox"  listData=[
							{"id":"info_add","title":"新增明细","iconCls":"icon-add-dtl","action":"billumreceipt.showAddBox();","type":0},
							{"id":"info_remove","title":"删除明细","iconCls":"icon-del-dtl","action":"billumreceipt.delBox();","type":0},
							{"id":"info_save","title":"保存明细","iconCls":"icon-save","action":"billumreceipt.doSave();","type":0}
						]/>
					</div>
					<@p.datagrid id="receiptDtlDg" name="" title="退仓收货单明细"  loadUrl="" saveUrl="" defaultColumn="" 
							isHasToolBar="false"  divToolbar="#toolsDiv" height="365"  
						 	onClickRowEdit="true" singleSelect="false"   
							pagination="true" rownumbers="false" emptyMsg=""
						 	columnsJsonList="[
						 		{field : 'id',width : 50, checkbox:true},
						 		{field:'boxNo',title:'箱号',width:140,align:'left'},
								{field : 'locno',hidden:true},
								{field:'sumQty',title:'收货数量',width:80,align:'right'}
					]"/>
				</div>
			</div>
		
	</div>
	<#-- 新增修改页面 -->
	    
	    
	<#-- 详情 div -->
	<div id="openUIShow" title="详情" class="easyui-window" 
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
		    
		    <div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				
					<#-- 工具菜单div -->
		            <@p.toolbar id="toolbardetail"  listData=[
		                {"title":"取消","iconCls":"icon-cancel","action":"billumreceipt.closeWindow('openUIShow')","type":0}
		            ]/>
				
					<form name="dataFormShow" id="dataFormShow" metdod="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo" id="receiptNo_detail" readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo_detail" data-options="required:true" /></td>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" id="untreadNo_detail" data-options="required:true" /></td>
								<td class="common-td blank">店退仓通知单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo" id="untreadMmNo_detail" data-options="required:true" /></td>
							</tr>			
							<tr>
								<td class="common-td">退仓类型：</td>
								<td><input class="easyui-combobox" style="width:120px" name="itemType" id="itemType_detail"  data-options="required:true,validType:['vLengtd[0,10,\'最多只能输入10个字符\']']" /></td>
								<td class="common-td blank">品质：</td>
								<td><input class="easyui-combobox" style="width:120px" name="quality" id="quality_detail"  data-options="validType:['vLengtd[0,32,\'最多只能输入32个字符\']']" /></td>
								<td class="common-td blank">客户：</td>
								<td colspan='3'><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNo_detail"  data-options="validType:['vLengtd[0,32,\'最多只能输入32个字符\']']"  /></td>
							</tr>
							<tr>
								<td class="common-td">备注：</td>
								<td colspan='7'><input class="easyui-validatebox ipt" style="width:100%;" name="remark" id="remark_detail"  /></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
					<@p.datagrid id="itemDetailShow" name=""  defaultColumn="" showFooter="true"
						isHasToolBar="false" divToolbar=""  onClickRowEdit="false" 
						singleSelect="false" pageSize='20' title="退仓收货明细列表"
						pagination="true" rownumbers="true"
					 	columnsJsonList="[
					           		{field:'boxNo',title:'箱码',width:120,align:'left',sortable:true},
					           		{field:'itemNo',title:'商品编码',width:140,align:'left',sortable:true},
					 				{field:'itemName',title:'商品名称',width:130,align:'left',sortable:true},
								    {field:'colorName',title:'颜色',width:80,align:'left',sortable:true},
								    {field:'sizeNo',title:'尺码',width:80,align:'left',sortable:true},
								    {field:'brandName',title:'品牌',width:80,align:'left',sortable:true},
								    {field:'itemQty',title:'收货数量',width:80,align:'right',sortable:true}
					   	]"
					/>
				</div>
		</div>	
			
	</div>
		
		
	<#-- 详情 div -->
	<#--箱号选择div -->
	<div id="openUIUn" class="easyui-window"  title="箱号选择"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		maximized:true,minimizable:false,maximizable:false">
		
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				
					<#-- 工具菜单div -->
		            <@p.toolbar id="addboxtoolbar"  listData=[
		            	{"title":"确定","iconCls":"icon-ok","action":"billumreceipt.selectBox();", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumreceipt.closeWindow('openUIUn');","type":0}
		            ]/>
					
				</div>
					
				<div data-options="region:'center',border:false">
					<#-- 箱号选择数据列表div -->
		          	<@p.datagrid id="dataGridUnDtl"  loadUrl=""  saveUrl=""  defaultColumn="" 
				    	isHasToolBar="false" divToolbar=""  onClickRowEdit="false" 
				    	singleSelect="false" pageSize='20'  title="退仓通知单明细列表"
						pagination="true" rownumbers="true"
					  	columnsJsonList="[
					    	{field : 'ck',width : 50, checkbox:true},
					    	{field : 'boxNo',title : '箱号 ',width : 100,align:'left'},
					    	{field : 'sumQty',title : '数量',width : 80,align:'right'}
					]"/>
				</div>
			</div>
		
	</div>
	<div id="openUIUnUntread" class="easyui-window"  title="店退仓单选择"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		maximized:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<#-- 工具菜单div -->
	            <@p.toolbar id="adduntreadtoolbar"  listData=[
	            	{"title":"查询","iconCls":"icon-search","action":"billumreceipt.searchUntread()", "type":0},
	 				{"title":"清除","iconCls":"icon-remove","action":"billumreceipt.searchClear('untreadSearchForm')", "type":0},
	            	{"title":"确定","iconCls":"icon-ok","action":"billumreceipt.selectUntread();", "type":0},
	            	{"title":"批量生成","iconCls":"icon-ok","action":"billumreceipt.selectBatchUntread();", "type":0},
	                {"title":"取消","iconCls":"icon-cancel","action":"billumreceipt.closeWindow('openUIUnUntread');","type":0}
	            ]/>
	            <form name="untreadSearchForm" id="untreadSearchForm" metdod="post" class="city-form" style="padding:10px;">
					<table>
						<tr>
							<td class="common-td">来源单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNo" /></td>
							<td class="common-td">店退仓单：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" id="untreadNoForUntread" /></td>
							<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoForUntread"/></td>
							<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoForUntread"/></td>
						</tr>
						<tr>
							<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoForUntread" /></td>
		   					<td class="common-td blank">创建日期：</td>
		   					<td><input class="easyui-datebox ipt" style="width:120px" name="createtmStart" id="startCreatetmForUntread"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox ipt" style="width:120px" name="createtmEnd" id="endCreatetmForUntread"/></td>
		   					<td class="common-td blank"></td>
		   					<td></td>
		   					<td class="common-line"></td>
		   					<td></td>
		   				</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<#-- 店退仓单列表div -->
	          	<@p.datagrid id="dataGridUntreadDtl"  loadUrl=""  saveUrl=""  defaultColumn="" 
			    	isHasToolBar="false" divToolbar=""  onClickRowEdit="false" 
			    	singleSelect="false" selectOnCheck="true" pageSize='20'  title="店退仓单列表"
					pagination="true" rownumbers="true"
				  	columnsJsonList="[
				    	{field : 'ck',width : 50, checkbox:true},
				    	{field : 'untreadNo',title : '店退仓单号 ',width : 180,sortable:true},
				    	{field : 'poNo',title : '来源单号 ',width : 180,sortable:true},
				    	{field : 'ownerNo',title : '货主',width : 100,align:'left',formatter:billumreceipt.ownerNoFormatter,sortable:true},
				    	{field : 'untreadType',title : '退仓类型',width : 100,align:'left',formatter:billumreceipt.untreadTypeFormatter,sortable:true},
				    	{field : 'quality',title : '品质',width : 100,align:'left',formatter:billumreceipt.qualityFormatter,sortable:true},
				    	{field : 'storeNo',title : '客户编码',width : 100,align:'left',sortable:true},
				    	{field : 'storeName',title : '客户名称',width : 150,align:'left',sortable:true},
				    	{field : 'itemQty',title : '商品总数',width :100,align:'right',sortable:true},
				    	{field : 'creator',title : '创建人',width : 100,align:'left',sortable:true},
				    	{field : 'createtm',title : '创建时间',width : 135,sortable:true},
				    	{field : 'auditor',title : '审核人',width : 100,align:'left',sortable:true},
				    	{field : 'audittm',title : '审核时间',width : 135,sortable:true}
				]"/>
			</div>
		</div>
	</div>
</body>
</html>