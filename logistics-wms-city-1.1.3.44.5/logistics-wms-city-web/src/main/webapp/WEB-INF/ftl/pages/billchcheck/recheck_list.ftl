<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>复盘发单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
   <script type="text/javascript" src="${domainStatic}/resources/js/billchcheck/billchrecheck.js?version=1.0.8.4"></script>
</head>
<body class="easyui-layout">
    <div id="toolDiv" data-options="region:'north',border:false">
       <@p.toolbar id="main-toolbar"  listData=[
             {"title":"查询","iconCls":"icon-search","action":"billchrecheck.searchArea();","type":0},
			 {"title":"清除","iconCls":"icon-remove","action":"billchrecheck.searchClear();","type":0}
			 ]
		/>
		<form id='checkForm' method="post" style="padding:8px;padding-bottom:0px;" class="city-form">
			<table>
				<tr>
					<td class="common-td blank">差异标示：</td>
					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="differentFlag" id="differentFlag"/></td>
					<td class="common-td blank">盘点单号：</td>
					<td><input class="easyui-validatebox ipt" style="width:150px" name="checkNo"/></td>
					<td class="common-td blank">创建日期：</td>
					<td><input class="easyui-datebox" style="width:150px" name="createtm_start" id="createtmStart"/></td>
					<td class="common-td blank">至：</td>
					<td><input class="easyui-datebox" style="width:150px" name="createtm_end" id="createtmEnd"/></td>
				</tr>
				<tr>
					<td class="common-td blank">计划单号：</td>
					<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo"/></td>
					<td class="common-td blank">库区：</td>
         			<td><input class="easyui-combobox" style="width:150px" name="wareNo" id="wareNo"/></td>
					<td class="common-td blank">库区：</td>
					<td><input class="easyui-combobox" style="width:150px" id="areaNo" name="areaNo"/></td>
					<td class="common-td blank">通道：</td>
					<td><input class="easyui-combobox ipt" style="width:150px"  id="stockNo" name="stockNo"/></td>
					
				</tr>
				<tr>
					<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
					<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
					<td class="common-td blank">所属品牌：</td>
					<td colspan="5"><input class="easyui-combobox ipt" style="width:150px" name="brandNo" id="brandNo" /></td>
				</tr>
			</table>
    	</form>
		<div style="padding:10px;">
			<div style="border:1px solid #95B8E7;padding:10px;">   
	        	复盘人员：<input class="easyui-combobox" style="width:120px" name="recheckWorker" id="recheckWorker"/> 
	    		<a id="createTask" href="javascript:billchrecheck.distributionAssignNoBatch();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">发单</a>
			</div>
		</div>
	</div>
	<div id="toolDiv" data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn="" 
		              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"
		               pagination="true" rownumbers="true"  singleSelect = "false" title="盘点单" emptyMsg=""
			           columnsJsonList="[
			           		{field : 'status',title : '状态',width : 100,align:'left',formatter:billchrecheck.statusFormatter},
							{field : 'planNo',title : '计划单号',width : 180},
							{field : 'checkNo',title : '盘点单号',width : 180},
							{field : 'audittm',title : '审核日期',width :135,sortable:true},
							{field : 'checkDate',title : '盘点日期',width : 120},
							{field : 'cellCount',title : '总储位数',width : 75,align:'right'},
							{field : 'itemCount',title : '总商品数',width : 75,align:'right'},
							{field : 'assignNo',title : '盘点人员',width : 90,align:'left'},
							{field : 'assignName',title : '盘点人员名称',width : 90,align:'left'}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	    billchrecheck.loadDetail(rowData);
		                   }}'/>
    </div>
    <div data-options="region:'south',minSplit:true" style="height:200">
					<@p.datagrid id="dataGridJGDetail"  loadUrl="" saveUrl=""   defaultColumn="" showFooter="true"
		              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  
		               pagination="true" rownumbers="true"  singleSelect = "false" title="盘点明细" emptyMsg=""
			           columnsJsonList="[
							{field : ' ',checkbox:true},
							{field : 'cellNo',title : '储位',width : 120},							
							{field : 'itemQty',title : '计划数量',width : 100,align:'right'},
							{field : 'checkQty',title : '初盘数量',width : 100,align:'right'},
							{field : 'diffQty',title : '初盘差异数量',width : 100,align:'right'},
							{field : 'recheckQty',title : '复盘数量',width : 100,align:'right'},
							{field : 'recheckDiffQty',title : '复盘差异数量',width : 100,align:'right'}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	  // billimreceipt.instockDetail(rowData,"view");
		                   }}'/>
	</div>
</body>
</html>