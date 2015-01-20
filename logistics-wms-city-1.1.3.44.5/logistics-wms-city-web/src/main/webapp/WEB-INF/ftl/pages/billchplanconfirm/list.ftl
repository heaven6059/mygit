<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>盘点计划确认</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billchplanconfirm/billchplanconfirm.js?version=1.0.8.2"></script>
</head>
<body class="easyui-layout">
	<!-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billchplanconfirm.searchbillchplanconfirm()", "type":0},
	            {"id":"btn-clear","title":"清除","iconCls":"icon-remove","action":"billchplanconfirm.searchClear()", "type":0},
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('盘点计划单确认')","type":0}
			 ]/>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">状态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition"/></td>
                     			<td class="common-td blank">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoCondition" /></td>
                     			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                     			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="createtmStartCondition"/></td>
                     			<td class="common-line">&mdash;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEndCondition"/></td>
                     			<td class="common-td blank">审核日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="audittmStartCondition"/></td>
                     			<td class="common-line">&mdash;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEndCondition"/></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
                     			<td class="common-td blank">所属品牌：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
                     			<td class="common-td blank">盘点日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="planDateStart" id="startPlanDateCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="planDateEnd" id="endPlanDateCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">盘点类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="planType" id="planTypeCondition"/></td>
                     		</tr>
                     	</table>
				 	</form>
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="dataGridBillChPlanConfirm"  loadUrl="" saveUrl=""  defaultColumn=""   title="盘点确认列表"
			       isHasToolBar="false" divToolbar="#billchplanconfirmSearchDiv" height="500"  onClickRowEdit="false"    pagination="true"
			       rownumbers="true"
			       columnsJsonList="[
			          	{field : 'status',title : '状态',width : 100,formatter:billchplanconfirm.statusFormatter,align:'left'},
			          	{field : 'planNo',title : '单据编号',width : 180},
			          	{field : 'planType',title : '盘点类型',width : 100,formatter:billchplanconfirm.planTypeFormatter},
			          	{field : 'planDate',title : '盘点日期',width : 100},
			          	{field : 'beginDate',title : '开始日期',width : 100},
			          	{field : 'endDate',title : '结束日期',width : 100},
			          	{field : 'creator',title : '创建人',width : 80,align:'left'},
			          	{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			          	{field : 'createtm',title : '创建时间',width : 130},
			          	{field : 'auditor',title : '审核人',width : 80,align:'left'},
			          	{field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			          	{field : 'audittm',title : '审核时间',width : 130},
			          	{field : 'planRemark',title : '备注',width : 120,align:'left'}
			         ]" 
			          
			       jsonExtend='{onDblClickRow:function(rowIndex, rowData){
			                // 触发点击方法  调JS方法
			                billchplanconfirm.loadDetail(rowData);
			       }}'/>
            </div>
		</div>
	</div>
	
		
	<!-- 盘点明细窗口S -->
	<div id="showDetailDialog" class="easyui-dialog" title="盘点明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="difftoolbar"  listData=[
				        {"id":"btn-diff","title":"结案","iconCls":"icon-ok","action":"billchplanconfirm.createDiff();","type":4},
						{"title":"关闭","iconCls":"icon-close","action":"billchplanconfirm.closeWindow('showDetailDialog');","type":0}
				   	]/>	
		    		<div class="search-div">
		         		<form name="planMainForm" id="planMainForm" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoCheck" readOnly="true" /></td>
		                 			<td class="common-td blank">盘点类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="planType" id="planTypeCheck" />
		                 			<td class="common-td blank">计划日期：</td>
		                 			<td><input class="easyui-datebox" style="width:120px" name="planDate" id="planDateCheck"/></td>
		                 			<td class="common-td blank">开始日期：</td>
		                 			<td><input class="easyui-datebox" style="width:120px" name="beginDate" id="beginDateCheck"/></td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td">状态：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="status" id="statusCheck" readOnly="true" />
		                 				<input type="hidden" id="ownerNoCheck"/>
		                 			</td>
		                 		</tr>
		                    </table>
		         		</form>
			         </div>
		    	</div>
		    	<!--显示列表-->
            	<div data-options="region:'center',border:false">
            		<@p.datagrid id="dtl_check_dataGrid"    defaultColumn=""  title="差异信息"
						isHasToolBar="false" divToolbar="" height="350"    pageSize="20" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" showFooter="true"
						rownumbers="true" 
						columnsJsonList="[
						 	  {field:'checkNo',title:'盘点单号',width:180},    
						 	  {field:'cellNo',title:'储位',width:100,align:'left'},    
    				          	{field:'itemNo',title:'商品编码',width:150,align:'left'},    
    				          	{field:'itemName',title:'商品名称',width:150,align:'left'},    
    				          	{field:'colorName',title:'颜色',width:100,align:'left'},    
    				          	{field:'sizeNo',title:'尺码',width:100,align:'left'},    
    				          	{field:'itemQty',title:'计划数量',width:100,align:'right'},    
    				          	{field:'checkQty',title:'初盘数量',width:100,align:'right'},    
    				          	{field:'recheckQty',title:'复盘数量',width:100,align:'right'},    
    				          	{field:'brandName',title:'品牌',width:100,align:'left'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
            	</div>
		    </div>
	</div>
</body>
</html>