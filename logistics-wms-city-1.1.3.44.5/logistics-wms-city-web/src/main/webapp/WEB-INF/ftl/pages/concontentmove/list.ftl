<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>库存商品变动报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/concontentmove/concontentmove.js?version=1.0.5.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"concontentmove.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"concontentmove.searchClear('searchForm');","type":0}
			{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"concontentmove.do_export()","type":5},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
		]
	/>
</div>


<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 		<div  data-options="region:'north',border:false" >
 		
			<div id="conContentMoveSearchDiv"  style="padding:10px;">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td">库区：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="areaNo" id="areaNo" data-options="onSelect:function(data){
                     																								concontentmove.initCell(data.wareNo,data.areaNo);
                     																							}"/></td>
							<td class="common-td blank">储位：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNo" /></td>
							<td class="common-td blank">商品编码：</td>
                     		<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>
                     		<td class="common-td blank">条码：</td>
                     		<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode" /></td>
						</tr>
						<tr>
							<td class="common-td">库存属性：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemType" /></td>
							<td class="common-td blank">品质：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="quality" /></td>
							<td class="common-td blank">业务类型：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="paperType" id="paperType" /></td>
                     		<td class="common-td blank">单号：</td>
                     		<td><input class="easyui-validatebox ipt" style="width:120px" name="paperNo" id="paperNo" /></td>
						</tr>
						<tr>
							<td class="common-td">记账日期：</td>
				            <td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm" /></td>
				            <td class="common-line">&mdash;</td>
				            <td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm" /></td>
							<td class="common-td blank">进出标示：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="ioFlag" id="ioFlag" /></td>
							<td class="common-td blank">记账类型：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="preFlag" id="preFlag" /></td>
						</tr>
					</table>
				</form>
			</div>
	</div>			 


	<#-- 数据列表div -->
	<div data-options="region:'center',border:false">
	    <@p.datagrid id="dataGridJG"  loadUrl=""
	    	  		saveUrl=""  defaultColumn=""  title="库存商品变动列表"
		    		isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  
		    		pagination="true" singleSelect = "false"
					rownumbers="true" emptyMsg="" showFooter="true"
					columnsJsonList="[
			                {field : 'cellNo',title : '储位',width : 100,align:'left',sortable:true},
			                {field : 'areaName',title : '库区',width : 80,align:'left',sortable:true},
			                {field : 'itemNo',title : '商品编码',width : 150,align:'left',sortable:true},
			                {field : 'itemName',title : '商品名称',width : 170,align:'left',sortable:true},
			                {field : 'sizeNo',title : '尺码',width : 60,align:'left',sortable:true},
			                {field : 'qualityStr',title : '品质',width : 80,align:'left',sortable:true},
			                {field : 'itemTypeStr',title : '库存属性',width : 80,align:'left',sortable:true},
			                {field : 'moveQty',title : '变动数量',width : 70,align:'right',sortable:true},
			                 {field : 'balanceQty',title : '结存库存数',width : 80,align:'right',sortable:true},
			                {field : 'billName',title : '业务类型',width : 100,align:'left',sortable:true},
			                {field : 'paperNo',title : '单号',width : 150,sortable:true},
			                {field : 'createdt',title : '日期',width : 100,sortable:true},
			                {field : 'ioFlagStr',title : '进出标示',width : 80,sortable:true},
			                {field : 'preFlagStr',title : '记账类型',width : 80,sortable:true},
			                {field : 'barcode',title : '条码',width : 160,align:'left',sortable:true}
			            ]" 
						jsonExtend='{
		                     	
		        }'
		/>
	 </div>
 
 </div>
 </div>
</body>
</html>