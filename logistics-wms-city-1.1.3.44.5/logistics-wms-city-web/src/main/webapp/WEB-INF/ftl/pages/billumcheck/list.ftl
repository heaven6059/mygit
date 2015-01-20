<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓验收单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billumcheck/billumcheck.js?version=1.1.1.5"></script>
</head>

<body class="easyui-layout">

	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billumcheck.searchData()", "type":0},
		 		{"title":"清除","iconCls":"icon-remove","action":"billumcheck.searchClear('searchForm')", "type":0},
			 	{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billumcheck.add();","type":1},
			    {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billumcheck.edit()","type":2},
			    {"id":"btn-remove","title":"删除","iconCls":"icon-del","action":"billumcheck.doDel()","type":3},
			    {"id":"btn-audit","title":"审核","iconCls":"icon-aduit","action":"billumcheck.doAudit()","type":4},
			    {"id":"btn-tcshoddy","title":"次品转货","iconCls":"icon-redo","action":"billumcheck.tcshoddy()","type":4},
	            {"id":"btn-tcdepa","title":"部门转货","iconCls":"icon-redo","action":"billumcheck.tcdepa()","type":4},
	            {"id":"btn-tostoreconvert","title":"门店转货","iconCls":"icon-redo","action":"billumcheck.showStoreConvert()","type":4},
	            <#--{"id":"btn-tcstore","title":"门店转货","iconCls":"icon-redo","action":"billumcheck.tcstore()","type":4},-->
	            {"id":"btn-preopties","title":"属性转货","iconCls":"icon-redo","action":"billumcheck.property()","type":4},
	            {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退仓验收单');","type":0}
		]/>
	</div>
	
	<div data-options="region:'center',border:false">
		
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<div  data-options="region:'north',border:false" >
				<div id="searchDiv" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td blank">状态：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="status" id="statusCondition" /></td>
								<td class="common-td blank">验收单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo" id="checkNoCondition" /></td>
								<td class="common-td blank">来源单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNo" /></td>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" /></td>
							</tr>
							<tr>
								<td class="common-td blank">退仓装箱单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="loadboxNo" id="loadboxNo" /></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetmCondition" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>								
							<tr>
								<td class="common-td blank">客户名称：</td>
								<td ><input class="easyui-validatebox ipt" style="width:120px" name="storeName" id="storeName" /></td>
								<td class="common-td blank">审核人：</td>
								<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
								<td class="common-td">转货类型：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="convertType" id="convertTypeCondition"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="ownerNoCondition" /></td>
								<td class="common-td blank" >商品类型：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemType_search"  style="width:120px"/></td>
								<td class="common-td blank">品质：</td>
								<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>
							</tr>
							<tr>
							    <td class="common-td">验收类型：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="checkType" id="checkType_search"/></td>
								<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
								<td class="common-td blank">所属品牌：</td>
								<td colspan="5"><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
							</tr>
						</table>
					</form>	
				</div>
			</div>
			
			<div  data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="退仓验收单列表"
		               isHasToolBar="false" divToolbar="" height="410"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" showFooter="true" emptyMsg="" 
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'sysNo',hidden:true},
			           		{field : 'status',title : '状态 ',align:'left',width : 80,formatter:billumcheck.statusFormatter},
			                {field : 'checkNo',title : '验收单号',width : 180},
			                {field : 'untreadNo',title : '店退仓单号',width : 180},
			                {field : 'poNo',title : '来源单号',width : 120,align:'left'},
			                {field : 'checkType',title : '验收类型',width : 100,align:'left',formatter:billumcheck.checkTypeFormatter},
			                {field : 'convertTypeStr',title : '转货类型',width : 100,align:'left'},
			                {field : 'ownerNo',title : '货主',align:'left',width : 100,formatter:billumcheck.ownerFormatter},
			                {field : 'storeName',title : '客户名称',width : 180,align:'left'},
			                {field : 'itemType',title : '商品类型',width : 100,formatter:billumcheck.typeFormatter,align:'left'},
							{field : 'quality',title : '品质',width : 100,formatter:billumcheck.qualityFormatter,align:'left'},
							{field : 'itemQty',title : '计划数量',width : 70,align:'right'},
							{field : 'realQty',title : '实际数量',width : 70,align:'right'},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130,sortable:true},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130}
			            ]" 
				        jsonExtend='{onLoadSuccess:function(data){
								billumcheck.onLoadSuccessSum(data);
							},onDblClickRow:function(rowIndex, rowData){
		    				// 触发点击方法  调JS方法
		        			billumcheck.loadDetail(rowData,1);
				}}'/>
			</div>
		</div>
	</div>
	<#-- 新增/修改div-->
	<div id="openUIDetail" class="easyui-window" 
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<#-- 工具菜单div -->
		            <@p.toolbar id="addtoolbar"  listData=[
		            	{"id":"btn-save-detail","title":"保存","iconCls":"icon-save","action":"billumcheck.save_main()", "type":0},
		            	{"id":"btn-edit-detail","title":"修改","iconCls":"icon-edit","action":"billumcheck.edit_main()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumcheck.closeWindow('openUIDetail')","type":0}
		            ]/>
		            
		            <form name="dataForm" id="dataForm" method="post" class="city-form" style="padding:10px;">
		            	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="checkNo" id="checkNo"  readOnly="readOnly"/></td>
								<td  class="common-td blank"><label id='checkTypeTitle'>店退仓单号</label>：</td>
								<td>
									<input class="easyui-validatebox ipt" style="width:110px" name="untreadNo" id="untreadNo" readOnly="readOnly" required="true"/>
									<input type="button" id="btnUntread" value=".." onclick="billumcheck.openSelect()"/>
								</td>
								<td class="common-td blank">货主：</td>
								<td>
									<input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="ownerNo" id="ownerNo" required="true" />
									<input type="hidden" name="ownerNo" id="ownerNoHide"/>
								</td>
								<td class="common-td blank">商品类型：</td>
								<td>
									<input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="itemType" id="itemType" required="true"/>
									<input type="hidden"  name="itemType" id="itemTypeHide"/>
								</td>
							</tr>
							
							<tr>
								<td class="common-td blank">品质：</td>
								<td>
									<input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="quality" id="quality"/>
									<input type="hidden" name="quality" id="qualityHide"/>
								</td>
								<td class="common-td blank">验收类型：</td>
								<td>
									<input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="checkType" id="checkType" required="true"/>
									<input type="hidden"  name="checkType" id="checkTypeHide"/>
								</td>
								<td class="common-td blank">备注：</td>
								<td colspan='4'><input class="easyui-validatebox ipt" style="width:100%" name="remark" id="remark"  data-options="validType:['vLength[0,225,\'最多只能输入225个字符\']']"/></td>
							</tr>
						</table>
						<input id="untreadType" name="untreadType" type="hidden" />
					</form>
				</div>
				
				
				<div data-options="region:'center',border:false">
					<div id="toolCheckDtlDiv">
						<@p.toolbar id="toolbar_itemdetal"  listData=[
								{"title":"添加差异商品","iconCls":"icon-add-dtl","action":"billumcheck.splitBox();","type":0},
								{"title":"删除明细","iconCls":"icon-del-dtl","action":"billumcheck.delCheckDtl();","type":0},
								{"title":"保存明细","iconCls":"icon-save-dtl","action":"billumcheck.saveCheckDtl();","type":0}
						]/>
				 	</div>
				 	
				 	<@p.datagrid id="checkDtlDg" name="" title="退仓验收单明细"  loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  divToolbar="#toolCheckDtlDiv"  height="295"  
				 			onClickRowEdit="true"
							pagination="true" rownumbers="true" emptyMsg=""
				 			columnsJsonList="[
				 				{field:'itemNo',title:'商品编码',width:150,align:'left'},
				 				{field:'itemName',title:'商品名称',width:150,align:'left'},
							    {field:'colorName',title:'颜色',width:80,align:'left'},
							    {field:'sizeNo',title:'尺码',width:80,align:'left'},
							    {field:'boxNo',title:'箱号',width:100,align:'left'},
							    {field:'itemQty',title:'计划数量',width:80,align:'right'},
							    {field:'checkQty',title:'验收数量',align:'right',width:80,
							    	editor:{
				 						type:'numberbox',
				 						options:{
				 							min:0,
					 						required:true,
					 						missingMessage:'验收数量为必填项!'
				 						}
				 					}
				 				},
							   {field:' ',title:'差异数量',width:80,align:'right',formatter:billumcheck.diffQty},
							   {field:'brandNo',title:'品牌编码',width:80,align:'right',hidden:true}
				 	]"
				 	 jsonExtend='{onLoadSuccess:function(data){
		    				billumcheck.loadSuccess();
					}}'/>
				 	
				</div>
		</div>
			
	</div>
	
	<#-- 详细 -->
	<div id="openUIDetail_view" class="easyui-window" 
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar  id="viewToolBar"  listData=[
						 {"title":"导出","iconCls":"icon-export","action":"billumcheck.doDtlExport()","type":4}
				       ]
					/>
					<#-- 工具菜单div -->
		            <form name="dataForm" id="dataForm_view" method="post" class="city-form" style="padding:10px;">
		            	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="checkNo" id="checkNo_view"  readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="ownerNo" id="ownerNo_view"  /></td>
								<td class="common-td blank">退仓类型：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="itemType" id="itemType_view"/></td>
								<td  class="common-td blank"><label id='checkTypeTitle'>店退仓单号</label>：</td>
								<td>
									<input class="easyui-validatebox ipt" style="width:110px" name="untreadNo" readOnly="readOnly"/>
								</td>
							</tr>
							
							<tr>
								<td class="common-td blank">品质：</td>
								<td>
									<input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="quality" id="quality_view"/>
								</td>
								<td class="common-td blank">验收类型：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="checkType" id="checkType_view"/></td>
								<td class="common-td blank">备注：</td>
								<td colspan='6'><input class="easyui-validatebox ipt" style="width:100%" name="remark" id="remark"  data-options="validType:['vLength[0,225,\'最多只能输入225个字符\']']"/></td>
							</tr>
						</table>
						<input id="untreadType" name="untreadType" type="hidden" />
					</form>
				</div>
				
				
				<div data-options="region:'center',border:false">
				 	<@p.datagrid id="checkDtlDg_view" name="" title="退仓验收单明细"  loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  height="295"   showFooter="true"
				 			onClickRowEdit="false" singleSelect="false"   
							pagination="true" rownumbers="true" emptyMsg=""
				 			columnsJsonList="[
				 				{field:'itemNo',title:'商品编码',width:150,align:'left'},
				 				{field:'itemName',title:'商品名称',width:150,align:'left'},
							    {field:'colorName',title:'颜色',width:80,align:'left'},
							    {field:'sizeNo',title:'尺码',width:80,align:'left'},
							    {field:'brandName',title:'品牌',width:80,align:'left'},
							    {field:'boxNo',title:'箱号',width:100,align:'left'},
							    {field:'itemQty',title:'计划数量',width:80,align:'right'},
							    {field:'checkQty',title:'验收数量',align:'right',width:80},
							    {field:'difQty',title:'差异数量',width:80,align:'right'},
							    {field:'recheckQty',title:'复核数量',width:80,align:'right'}
				 	]"
				 	jsonExtend='{
			     		onLoadSuccess:function(data){
			     			//合计
							billumcheck.onLoadSuccessDtlSum(data);
						}
			     	}'
				 	/>
				 	
				</div>
		</div>
			
	</div>          
	<#--店退仓单选择div -->
	<div id="openUIUn" class="easyui-window"  title="店退仓单选择"
	    data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<#-- 工具菜单div -->
		            <@p.toolbar id="untoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billumcheck.searchUn()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billumcheck.searchClear('unSearchForm')", "type":0},
		            	{"title":"确定","iconCls":"icon-ok","action":"billumcheck.selectUntread()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumcheck.closeWindow('openUIUn')","type":0}
		            ]/>
		            
		            <form name="unSearchForm" id="unSearchForm" metdod="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" style="width:110px" name="ownerNo" id="ownerNo_select" /></td>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="untreadNo" id="untreadNoConUn" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:110px" name="createtmStart" id="createtmStart_select" /></td>
								<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
								<td><input class="easyui-datebox" style="width:110px" name="createtmEnd" id="createtmEnd_select" /></td>
								
							</tr>
							<tr>
								<td class="common-td blank">来源单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="poNo" /></td>
								<td class="common-td blank">店退仓通知单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="untreadMmNo" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:110px" name="audittmStart" id="audittmStart_select" /></td>
								<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
								<td><input class="easyui-datebox" style="width:110px" name="audittmEnd" id="audittmEnd_select" /></td>
							</tr>
						</table>
					</form>
		        </div>
		        
		        <div data-options="region:'center',border:false">
		        	<#-- 退仓通知单选择数据列表div -->
          	  		<@p.datagrid id="dataGridUn"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="" width="726" height="325"  onClickRowEdit="false" 
		               singleSelect="true" pageSize='20'  title="退仓通知单列表" selectOnCheck="true" checkOnSelect="true"
					   pagination="true" rownumbers="true"
			           columnsJsonList="[
			          		{field : 'id',width : 50, checkbox:true},
			           		{field : 'untreadNo',title : '店退仓单号 ',width : 180},
			           		{field : 'poNo',title : '来源单号 ',width : 120,align:'left'},
			           		{field : 'untreadMmNo',title : '店退仓通知单号',width :180},
			           		{field : 'ownerNo',title : '货主 ',width : 130,formatter:billumcheck.ownerFormatter,align:'left'},
			           		{field : 'untreadType',title : '退仓类型 ',width : 130,formatter:billumcheck.typeFormatter,align:'left'},
			           		{field : 'receiptQty',title : '商品总数',width :100,align:'right'},
			                {field : 'storeName',title : '客户',width : 150,align:'left'},
			                {field : 'createtm',title : '创建日期',width : 150},
			                {field : 'audittm',title : '审核日期',width : 150}
			        	]"
			        />
		        </div>
		    </div>
	</div>
  
	<#--商品选择div -->
	<div id="openUIItem" class="easyui-window"  title="商品选择"
	    data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					
					<#-- 工具菜单div -->
		            <@p.toolbar id="itemtoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billumcheck.searchFilterItem()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billumcheck.searchClear('itemSearchForm')", "type":0},
		            	{"title":"确定","iconCls":"icon-ok","action":"billumcheck.selectItem()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumcheck.closeWindow('openUIItem')","type":0}
		            ]/>
				
					<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">品牌库：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="sysNo" id="sysNoConIt" /></td>
								<td class="common-td blank">商品编码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoConIt" /></td>
								<td class="common-td blank">商品名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameConIt"/></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
					<div id="boxDiv">
					箱号：<input class="easyui-combobox" style="width:120px" name="boxNo" id="boxNoList"  data-options="editable:false"/>
					</div>
					<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGridItem"  loadUrl=""  saveUrl=""  defaultColumn="" title="商品列表"
		               isHasToolBar="false" divToolbar="" width="726" height="325" onClickRowEdit="false"  
		               pagination="true" singleSelect = "false" divToolbar="#boxDiv"
			           rownumbers="true"
			           columnsJsonList="[
			           		{field : 'id',width : 50, checkbox:true},
			           		{field:'itemNo',title:'商品编码',width:140,align:'left'},
				 			{field:'itemName',title:'商品名称',width:130,align:'left'},
							{field:'colorName',title:'颜色',width:80,align:'left'},
							{field:'sizeNo',title:'尺码',width:60,align:'left'}
			            ]" 
				    jsonExtend='{}'/>
				</div>
			</div>
					
	</div>
	<#-- 选择目标仓库弹出层  店面转货 -->
	<div id="selectTcstore" title="选择目标门店" class="easyui-window" class="easyui-window"  style="width:366px;height:413px;padding:1px;"   
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    minimizable:false">
           <input class="easyui-combobox" style="width:350px;"  id="storeNo" />
    </div>
    <#-- 选择目标仓库弹出层  部门转货 -->
    <div id="selectTcdepa" title="选择目标仓库" class="easyui-window" class="easyui-window"  style="width:376px;height:413px;padding:1px;"   
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    minimizable:false">
		    	
		    	<input id = "sysNoSelect" type = 'hidden'>
		    	
		    	<div id='sdiv1' style='padding:5px;'>
		    	转货类型：<input name='convertType' id = 'convertType1' type='radio' value = '0' onClick='billumcheck.clickConvertType(this.value)' checked='checked'/>部门转货
		    			<input name='convertType' id = 'convertType2' type='radio' value = '1' onClick='billumcheck.clickConvertType(this.value)'/>跨部门转店
		    			<input name='convertType' id = 'convertType3' type='radio' value = '2' onClick='billumcheck.clickConvertType(this.value)'/>转仓
		    	</div>
		    	
		    	<div id='sdiv2' style='padding:5px;'>
			    	目标仓库：
	           		<input class="easyui-combobox" style="width:350px;"  id="tcdepaNo" />
           		</div>
           		
           		<div id='sdiv3' style='padding:5px;'>
	           		目标门店：
	           		<input class="easyui-combobox" style="width:350px;"  id="storeNoLoc" />
           		</div>
           		
           		<div id='sdiv4' style='padding:5px;'>
			    	目标仓库：
	           		<input class="easyui-combobox" style="width:350px;"  id="tcAlldepaNo" />
           		</div>
           		
           		<div style='padding-top:5px;padding-left:120px;'>
           		<a id="okBtn"  href="javascript:billumcheck.tcdepaSon();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确                  定</a>
           		</div>
    </div>
    
    
    <#--新增门店批量转货-->
	<div id="addStoreUI" class="easyui-window" title="选择目标门店" style="width:650px;height:413px;padding:0px;"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false">
	    	
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'west'" data-options="fit:true" style="width:330px;">
						
							<div class="easyui-layout" data-options="fit:true">
								<div data-options="region:'north',border:false">
									<#-- 工具菜单div -->
						            <@p.toolbar id="storetoolbar"  listData=[
						            	{"title":"查询","iconCls":"icon-search","action":"billumcheck.searchStoreData()", "type":0},
						 				{"title":"清除","iconCls":"icon-remove","action":"billumcheck.searchClear('searchStoreForm')", "type":0}
						            ]/>
				            
						            <form name="searchStoreForm" id="searchStoreForm" method="post" class="city-form" style="padding:10px;">
						            	<table>
							   				<tr>
							   					<td class="common-td">客户：</td>
												<td><input class="easyui-validatebox ipt" name="q" id="storeSearch" style="width:200px;"/></td>						
							   				</tr>
							   			</table>
									</form>
								</div>
								<div data-options="region:'center',border:false">
								    <@p.datagrid id="storeDg" name="" title="客户列表"  
								 			loadUrl="" saveUrl="" defaultColumn=""  singleSelect = "false"
								 			isHasToolBar="false"  divToolbar="" 
								 			onClickRowEdit="false" pagination="true" rownumbers="true" emptyMsg=""
								 			columnsJsonList="[
								 				{field : 'id',checkbox:true},
								 				{field : 'storeNo',title : '客户编码',width : 80,align:'left',sortable:true},
								 				{field : 'storeName',title : '客户名称',width : 140,align:'left',sortable:true}
								 			]"
								 	jsonExtend='{}'/>
							 	</div>
						 	
						 	</div>
						 	
						 	
						</div>
						<div data-options="region:'center'" >
						    <div class="easyui-layout" data-options="fit:true">
						    	<div data-options="region:'west'" data-options="fit:true" 
						    		style="width:60px;border-left:0px;border-top:0px;border-bottom:0px;">
						    		<div style="text-align:center;padding-top:100px;">
						            	<a class="easyui-linkbutton" href="javascript:billumcheck.toRgiht();"> >> </a>
						            	<br/><br/>
						            	<a class="easyui-linkbutton" href="javascript:billumcheck.toLeft();"> << </a>
						            </div>
						    	</div>
						        <div data-options="region:'center',border:false" >
						        	<@p.toolbar id="storeoktoolbar"  listData=[
						            	{"title":"确定转货","iconCls":"icon-ok","action":"billumcheck.doStoreConvert()", "type":0},
						 				{"title":"关闭","iconCls":"icon-close","action":"billumcheck.closeWindow('addStoreUI')", "type":0}
						            ]/>
						        	<@p.datagrid id="storeDg2" name="" title="已添加客户"  singleSelect = "false"
								 			loadUrl="" saveUrl="" defaultColumn="" showFooter="true"
								 			isHasToolBar="false"  divToolbar="" 
								 			onClickRowEdit="false" pagination="false" rownumbers="false" emptyMsg=""
								 			columnsJsonList="[
								 				{field : 'id',checkbox:true},
								 				{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
								 				{field : 'storeName',title : '客户名称',width : 160,align:'left'}
								 			]"
								 	jsonExtend='{}'/>
						        </div>
						    </div>
						</div>
						
						
					</div>
	</div>
    
 <div id="propetyChangeDialog"  class="easyui-window" title="属性转换"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:false" style="width:300px;height:250px" >
			<div class="easyui-layout" data-options="fit:true" style="height:150px;">
			 <form name="unSearchForm" id="unSearchForm" metdod="post" class="city-form" style="padding:10px;">
						<table>
							<tr style="height:40px;" >
								<td class="common-td blank">当前属性：</td>
								<td><input class="easyui-combobox" style="width:120px" name="convertType" id="itemTypevalues" disabled="true"/></td>
							</tr>
							<tr style="height:40px;" >
								<td class="common-td blank">转换后属性：</td>
								<td><input class="easyui-combobox" style="width:120px" name="convertType" id="chooseitemType"/></td>
							</tr>
							
							<tr style="height:40px;" >
								<td class="common-td blank"><a id="changeOkBtn"  href="javascript:billumcheck.propertyChange();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确                  定</a></td>
								<td class="common-td blank"><a id="cancelBtn"  href="javascript:billumcheck.closeWindow('propetyChangeDialog');" class="easyui-linkbutton" data-options="iconCls:'icon-close'">取                  消</a></td>
							</tr>
						</table>
					</form>
			</div>
</div>   
</body>
</html>