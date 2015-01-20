<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>盘点定位</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billchrequest/billchrequest.js?version=1.1.1.1"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"billchrequest.searchBillChRequest();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billchrequest.searchClear();", "type":0},
	        {"title":"定位","iconCls":"icon-ok","action":"billchrequest.direct();","type":1},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
        	<#--查询start-->
        	<div  data-options="region:'north',border:false">
        		<div nowrap id="billchrequestSearchDiv" class="search-div" style="padding:10px;">
        			<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
						    <tr>
						    	<td class="common-td">盘点计划单号：</td>
						        <td ><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoCondition" /></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td ><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoCondition"/></td>
						        <td class="common-td blank">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
						        <td ><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition"/></td>
						        <td class="common-td blank">盘点类型：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="planType" id="planTypeCondition"/></td>
						    </tr>             
						 	<tr>
                     			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
                     			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='3'><input class="easyui-combobox ipt" style="width:298px" name="brandNo" id="brandNo" /></td>
                     		</tr>
						 </table>
					</form>
        		</div>
   			</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridBillChRequest"  loadUrl="" saveUrl=""  defaultColumn=""   title="定位列表"
			       isHasToolBar="false" divToolbar="" height="273"  onClickRowEdit="false"    pagination="true" emptyMsg=""
			       rownumbers="true" pageNumber=0
			       columnsJsonList="[
			       		{field : 'id',checkbox :true},
			       		{field : 'status',title : '状态',width : 100,formatter:billchrequest.statusFormatter,align:'left'},
			          	{field : 'planNo',title : '盘点计划单号',width : 180},
			          	{field : 'ownerNo',title : '货主',width : 100,formatter:billchrequest.ownerFormatter,align:'left'},
			          	{field : 'planType',title : '盘点类型',width : 100,formatter:billchrequest.planTypeFormatter,align:'left'},
			          	{field : 'creator',title : '创建人',width : 100,align:'left'},
			          	{field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
			          	{field : 'createtm',title : '创建日期',width : 130}
			         ]" 
			       jsonExtend='{onSelect:function(rowIndex, rowData){
			                // 触发点击方法  调JS方法
			                billchrequest.loadDetail(rowData);
			       }}'
				/>
			</div>
        	<#--显示列表end-->
        	<#--显示列表start-->
        	<div id="tt" data-options="region:'south',border:false,minSplit:true" style="height:220px;border-top: 1px solid #CCCCCC;">
        		
        				<@p.toolbar id="directdtltoolbar"  listData=[
							{"title":"取消定位","iconCls":"icon-cancel","action":"billchrequest.cancelDirect();", "type":2},					       
							{"title":"恢复定位","iconCls":"icon-refresh","action":"billchrequest.regainDirect();", "type":2}					       
					   	]/>	
        				<@p.datagrid id="dataGridBillChRequestDtl"  loadUrl="" saveUrl=""  defaultColumn="" title="定位明细" showFooter="true"
		        			isHasToolBar="false" divToolbar="#directdtltoolbar" height="170"  onClickRowEdit="false"    pagination="true" emptyMsg=""
						   	rownumbers="true" pageNumber=0
						    columnsJsonList="[
						    		{field : 'id',checkbox :true},
						    		{field : 'directSerial',title : '定位流水号',hidden:true},
						          	{field : 'planNo',title : '盘点计划单号',width : 180},
						          	{field : 'status',title : '状态',width : 80,formatter:billchrequest.checkDirectStatusFormatter,align:'left'},
						          	{field : 'wareNo',title : '仓区',width : 80,align:'left'},
						          	{field : 'areaNo',title : '库区',width : 80,align:'left'},
						          	{field : 'stockNo',title : '通道',width : 80,align:'left'},
						          	{field : 'cellNo',title : '储位编码',width : 80,align:'left'},
						          	{field : 'itemNo',title : '商品编码',width : 135,align:'left'},
						          	{field : 'itemName',title : '商品名称',width : 135,align:'left'},
						          	{field : 'sizeNo',title : '尺码',width : 60,align:'left'},
						          	{field : 'itemQty',title : '计划数量',width : 60,align:'right'}
						         ]"
						  	jsonExtend='{onSelect:function(rowIndex, rowData){
						                // 触发点击方法  调JS方法
						                //item.loadDetail(rowData.itemNo);
						   	}}'
						/>
        		
        	</div>
        	<#--显示列表end-->
        </div>
   	</div>
    <#-- 主表end -->
</body>
</html>