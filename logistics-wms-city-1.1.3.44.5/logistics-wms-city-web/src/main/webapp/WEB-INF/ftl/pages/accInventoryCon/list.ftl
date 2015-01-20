<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>容器库存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/accInventoryCon/accInventoryCon.js?version=1.0.8.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"accInventoryCon.searchContainer();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"accInventoryCon.searchClear();", "type":0},
	        {"title":"模板下载","iconCls":"icon-download","action":"accInventoryCon.downloadTemp();","type":0},
	        {"title":"导入","iconCls":"icon-import","action":"accInventoryCon.importConToItem();","type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" style="padding:10px;width:1000px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				        		<td class="common-td blank">容器编码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="conNo" id="conNo" /></td>
				                <td class="common-td blank">容器类型：</td>
			   					<td><input class="easyui-combobox" style="width:120px" name="conType" id="conType"/></td>
			   					<td class="common-td blank">库存状态：</td>
			   					<td><input class="easyui-combobox" style="width:120px" name="status" id="conStatus"/></td>
				                <td class="common-td blank">储位：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:100px" name="cellNo" id="cellNo" /></td>
				        		<td class="common-td blank">商品品质：</td>
						        <td  id="qualityValue"><input class="easyui-combobox" style="width:120px" name="quality" id="quality" data-options="editable:false"/></td>
				        	</tr>
				        	<tr>
				        		<td class="common-td blank">创建日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm" /></td>
			   					<td class="common-line">&mdash;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm"/></td>
			   					<!--<td class="common-td blank">商品编码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>-->
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="容器库存"
		    	isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				rownumbers="true" emptyMsg=""
				columnsJsonList="[
			    	{field : 'conNo',title : '容器编码',width : 150,align:'left'},
			    	{field : 'conType',title : '容器类型',formatter:accInventoryCon.containerTypeFormatter,width : 80,align:'left'},
			    	{field : 'ownerNo',title : '业主',width : 50,align:'left'},
			    	{field : 'cellNo',title : '储位',width : 100,align:'left'},
			    	{field : 'childrenQty',title : '子容器数量',width : 80,align:'left'},
			    	{field : 'skuQty',title : 'SKU数量',width : 80,align:'left'},
			    	{field : 'quality',title : '品质',formatter:accInventoryCon.qualityFormatter,width : 80,align:'left'},
					{field : 'itemType',title : '商品类型',formatter:accInventoryCon.itemTypeFormatter,width : 80,align:'left'},
			    	{field : 'status',title : '容器状态',formatter:accInventoryCon.conStatusFormatter,width : 80,align:'left'},
					{field : 'createtm',title : '创建时间',width : 125,sortable:true}
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		        	// 触发点击方法  调JS方法
		       		accInventoryCon.loadDetail(rowData);
		   	}}'/>
			</div>
        	<#--显示列表end-->
		</div>
	</div>
<#-- 主表end -->
<#-- 明细信息div -->
<div id="detailDialogView" class="easyui-window" title="明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="padding:5px;">
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">容器号：</td>
						<td><input class="easyui-validatebox ipt"  name="conNo" style="width:120px;"/></td>
						<td class="common-td blank">容器类型：</td>
			   			<td><input class="easyui-combobox" style="width:120px" name="conType" id="conType1"/></td>
					</tr>
	    		</table>
		    </form>
		</div>
		<input type="hidden"   id="itemType" />
		<div data-options="region:'center'">
			<@p.datagrid id="dataGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="明细列表"  emptyMsg="" 
					           columnsJsonList="[
					           		{field:'conNo',title:'容器号/子容器号',width:150,align:'left'},
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'styleNo',title:'款号',width:80,align:'left'},
									{field:'sizeNo',title:'尺码',width:80,align:'left'},
									{field:'brandName',title:'品牌',width:80,align:'left'},
									{field:'itemName',title:'商品名称',width:200,align:'left'},
							        {field : 'quality',title : '品质',formatter:accInventoryCon.qualityFormatter,width : 80,align:'left'},
							        {field : 'itemType',title : '商品类型',formatter:accInventoryCon.itemTypeFormatter,width : 80,align:'left'},
				 					{field:'qty',title:'数量',width:50,align:'left',align:'right'},
					           		{field:'status',title:'容器状态',formatter:accInventoryCon.conStatusFormatter,width:150,align:'left'}	
								]"/>
				</div>
		</div>
	</div>
</div>
<#-- 明细信息div -->
<#-- 导入Excel div -->
<div id="importDialogView" class="easyui-window" title="导入"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closable:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="itemtoolbar"   listData=[
	        			<!--{"title":"模板下载","iconCls":"icon-download","action":"accInventoryCon.downloadTemp();","type":0},-->
	        			{"title":"导入","iconCls":"icon-import","action":"accInventoryCon.showImportExcel('showDialog');","type":0},
	        			{"title":"保存","iconCls":"icon-ok","action":"accInventoryCon.saveExcelItem();", "type":0},
	       				{"title":"清空预览数据","iconCls":"icon-remove","action":"accInventoryCon.clearExcelTemp();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"accInventoryCon.closeWindow('importDialogView');","type":0}
			        ]/>
				</div>
				<#--查询end-->
		<div data-options="region:'center',border:false">
			<input type="hidden" name="uuId" id="uuId" />
			<@p.datagrid id="importGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="Excel预览列表"  emptyMsg="" 
					           columnsJsonList="[
					           		{field:'cellNo',title:'储位编码',width:150,align:'left'},	
					           		{field:'conNo',title:'箱号',width:150,align:'left'},		
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'barcode',title:'商品条码',width:150,align:'left'},
									{field:'sizeNo',title:'尺码',width:150,align:'left'},
				 					{field:'qty',title:'数量',width:50,align:'left',align:'right'}
								]"/>
			</div>
		</div>
	</div>
</div>
<#--导入Excel div -->
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