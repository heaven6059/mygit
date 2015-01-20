<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>移库计划</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billhmplan/billhmplan.js?version=1.0.5.4"></script>    
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<@p.toolbar id="main-toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billhmplan.searchData()","type":0},
			{"title":"清除","iconCls":"icon-remove","action":"billhmplan.searchClear('searchForm')","type":0},
			{"title":"新增","iconCls":"icon-add","action":"billhmplan.addPlanOpen();","type":1},
			{"title":"修改","iconCls":"icon-edit","action":"billhmplan.toUpdatePlan();","type":2},
			{"title":"删除","iconCls":"icon-remove","action":"billhmplan.doDelPlan()","type":3},
			{"title":"审核","iconCls":"icon-aduit","action":"billhmplan.audit()","type":4},
			{"title":"手工关闭","iconCls":"icon-cancel","action":"billhmplan.cancelBillHmPlan()","type":3},
		    {"title":"关闭","iconCls":"icon-close","action":"closeWindow('移库计划');","type":0}
		]/>	
		<form name="searchForm" id="searchForm" metdod="post" class="city-form">
			<table>
				<tr>
					<td class="common-td blank">单据状态：</td>
					<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
					
					<td class="common-td blank">创建人：</td>
					<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
					<td class="common-td blank">创建日期：</td>
					<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
					<td class="common-line">&mdash;</td>
					<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetmCondition" /></td>
				</tr>
				<tr>
					<td class="common-td blank">单据编号：</td>
					<td><input class="easyui-validatebox ipt ipt" style="width:120px" name="planNo"/></td>
					<td class="common-td blank">审核人：</td>
					<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
					<td class="common-td blank">审核日期：</td>
					<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
					<td class="common-line">&mdash;</td>
					<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" /></td>
				</tr>
				<tr>
					<td class="common-td blank">来源单号：</td>
					<td><input class="easyui-validatebox ipt ipt" style="width:120px" name="sourceNo"/></td>
					<td class="common-td blank">货主：</td>
					<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoCondition" /></td>
					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
         			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
         			<td class="common-td blank">品牌：</td>
					<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<@p.datagrid id="dataGridJG" loadUrl=""  saveUrl=""  defaultColumn=""  title="移库计划单列表"
		    	isHasToolBar="false" divToolbar="#searchDiv" height="400" emptyMsg="" onClickRowEdit="false"  pagination="true" singleSelect = "false"
			    rownumbers="true"
			    columnsJsonList="[
			    		{field : 'id',checkbox:true},
			        	{field : 'locno',hidden:true},
			        	{field : 'status',title : '状态 ',width : 60,formatter:billhmplan.statusFormatter,align:'left'},
			        	{field : 'planNo',title : '单据编号',width : 180},
			        	{field : 'sourceNo',title : '来源单号',formatter:billhmplan.formatterSourceNo,width : 180},
			        	{field : 'ownerName',title : '货主',width : 120,align:'left'},
			        	{field : 'creator',title : '创建人',width : 80,align:'left'},
			        	{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			            {field : 'createtm',title : '创建时间',width : 130},
			            {field : 'auditor',title : '审核人',width : 80,align:'left'},
			            {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			            {field : 'audittm',title : '审核时间',width : 130},
			            {field : 'editor',title : '更新人',width : 100,sortable:true},
						{field : 'editorName',title : '更新人名称',width : 100,sortable:true},
						{field : 'edittm',title : '更新时间',width : 130,sortable:true},
			            {field : 'remark',title : '备注',width : 200}
			            
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		    		// 触发点击方法  调JS方法
		        	billhmplan.loadDetail(rowData,"view");
				}}'/>
	</div>
<#-- 明细信息div height:470px;-->
<div id="openWindowPlan" class="easyui-window"
    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="detail-toolbar" listData=[
				{"id":"btn-save","title":"保存","iconCls":"icon-save","action":"billhmplan.saveMain()","type":0},
				{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billhmplan.editMain()","type":0},
				{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"billhmplan.do_export()","type":5},
				{"title":"取消","iconCls":"icon-cancel","action":"billhmplan.closeWindow('openWindowPlan');","type":0}
				
			]/>	
			<form id='exportForm' method="post" action="../bill_hm_plan_dtl/do_export">
				<input type="hidden" name="planNo" id="planNo_export"/>
				<input type="hidden" name="locno" id="locno_export"/>
				<input type="hidden" name="ownerNo" id="ownerNo_export"/>
				<input type="hidden" name="exportColumns" id="exportColumnsCondition_export"/>
				<input type="hidden" name="fileName" value="移库计划"/>
			</form>
			<form name="dataForm" id="dataForm" method="post" class="city-form">
				<table>
				<tr>
					<td class="common-td blank">单据编号：</td>
					<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNo"   readOnly="readOnly"/></td>
					<td class="common-td blank">货主：</td>
					<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNo"  data-options="required:true"/></td>
					<td class="common-td blank">备注：</td>
					<td colspan="5"><input class="easyui-validatebox ipt" style="width:300px" name="remark" id="remark" maxlength="500" /></td>
				</tr>
				<input id="locnoHidden" type="hidden" value=""/>
				<input id="ownerNoHidden" type="hidden" value=""/>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<@p.toolbar id="detail-sub-toolbar" listData=[
				{"id":"btn-add-detail","title":"新增明细","iconCls":"icon-add-dtl","action":"billhmplan.addItemOpen()","type":0},
				{"id":"btn-save-detail","title":"删除明细","iconCls":"icon-del-dtl","action":"billhmplan.doDelPlanDtl()","type":0},
				{"id":"btn-remove-detail","title":"保存明细","iconCls":"icon-save-dtl","action":"billhmplan.saveDetail()","type":0}
				{"title":"模板下载","iconCls":"icon-download","action":"billhmplan.downloadTemp();","type":0},
	       		{"id":"import_row","title":"导入","iconCls":"icon-import","action":"billhmplan.importConToItem();","type":0}
				
			]/>
			<@p.datagrid id="planDtlDg" name="" title="移库计划单明细"  loadUrl="" saveUrl="" defaultColumn="" 
		 			isHasToolBar="false"  divToolbar="#detail-sub-toolbar" height="400"  emptyMsg=""
		 			onClickRowEdit="true" singleSelect="false"   showFooter="true"
					pagination="true" rownumbers="true"
		 			columnsJsonList="[
		 				{field : 'id', checkbox:true},
		 				{field:'brandNo',hidden:true},
		 				{field:'planNo',hidden:true},
		 				{field:'itemNo',title:'商品编码',width:140,align:'left'},
		 				{field:'itemName',title:'商品名称',width:160,align:'left'},
					    {field:'colorName',title:'颜色',width:80,align:'left'},
					    {field:'sizeNo',title:'尺码 ',width:80,align:'left'},
					    {field:'sCellNo',title:'来源储位',width:80,align:'left'},
					    {field:'itemTypeStr',title :'商品属性',width:80,align:'left'},
					    {field:'qualityStr',title :'商品品质',width:80,align:'left'},
					    {field:'dCellNo',title:'目的储位',width:100,align:'left',
					    	editor:{
		 						type:'validatebox',
		 						options:{
			 						required:true,
			 						missingMessage:'目的储位不能为空!'
		 						}
		 					}
		 				},
					    {field:'originQty',title:'计划移库数量',width:90,align:'right',
					    	editor:{
		 						type:'numberbox',
		 						options:{
			 						required:true,
			 						missingMessage:'计划移库数量为必填项!'
		 						}
		 					}}
		 	]"/>
		</div>
	</div>
</div>
<#--商品选择div -->
<div id="openWindowItem" class="easyui-window" title="商品选择"
    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="detail-item-toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billhmplan.searchConContent()","type":0},
				{"title":"清除","iconCls":"icon-remove","action":"billhmplan.searchClear('itemSearchForm')","type":0},
				{"title":"确定","iconCls":"icon-ok","action":"billhmplan.selectItem()","type":0},
				{"title":"取消","iconCls":"icon-cancel","action":"billhmplan.closeItem()","type":0}
			]/>
			<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-combobox ipt" style="width:130px" name="ownerNo" id="ownerNoCondition2" />
							<input  name="ownerNo" type="hidden" id="ownerNoCondition2Hide"/>
						</td>
						<td class="common-td blank">商品编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:130px" name="itemNo" id="itemNoCondition2" /></td>
						<td class="common-td blank">储位编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:130px" name="cellNo" id="cellNoCondition" /></td>
						<td class="common-td blank">品牌：</td>
						<td><input class="easyui-combobox ipt" style="width:160px" name="brandNo" id="brandNoCondition2" /></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<@p.datagrid id="dataGridItem"  loadUrl=""  saveUrl="" title="商品明细"  defaultColumn="" 
		       isHasToolBar="false" divToolbar="" onClickRowEdit="false" 
		       singleSelect="false" pageSize='20'  pagination="true" rownumbers="true"
		       columnsJsonList="[
		       			{field : 'id',width : 50, checkbox:true},
		       			{field:'itemId',hidden:true},
		       			{field:'cellId',hidden:true},
		       			{field:'packQty',hidden:true},
		       			{field:'brandNo',hidden:true},
		       			{field:'itemNo',title:'商品编码',width:150,align:'left'},
		 				{field:'itemName',title:'商品名称',width:150,align:'left'},
					    {field:'styleNo',hidden:true},
					    {field:'colorName',title:'颜色',width:80,align:'left'},
					    {field:'sizeNo',title:'尺码 ',width:80,align:'left'},
					    {field:'cellNo',title:'储位编码 ',width:80,align:'left'},
					    {field:'itemType',title :'商品属性',width:80,formatter:billhmplan.formatterItemType,align:'left'},
					     {field:'quality',title :'商品品质',width:80,formatter:billhmplan.formatterQuality,align:'left'},
					    {field:'sumQty',title:'库存数量 ',width:80,align:'right'},
					    {field:'sumPlanQty',title:'可移数量 ',width:80,align:'right'}
		    	]"
    		jsonExtend='{}'
  		  	/>
		</div>
	</div>
</div>

<#--Excel导入选择框 div -->
<div id="showImportDialog"  class="easyui-window" title="导入"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:false" style="width:450px;" >
			<div class="easyui-layout" data-options="fit:true" style="height:100px;">
				<div data-options="region:'north',border:false">
					<form name="dataViewForm" id="importForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td colspan="2" style="color:red;">只允许上传后缀为.xlsx、.xls的文件</td>
							</tr>
							<tr>
								<td class="common-td">选择文件：</td>
								<td>
									<iframe src="" id="iframe" frameborder="0" height="25"></iframe>
								</td>
							</tr>
						</table>
					</form>
				</div>
</div>
<#--Excel导入选择框 div -->
</body>
</html>