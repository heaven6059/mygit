<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>商品储位库存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/itemcellcontent.js?version=1.0.5.5"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<#--工具条-->
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"itemcellcontent.searchItemCellContent();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"itemcellcontent.searchClear();","type":0},
			{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"itemcellcontent.do_export()","type":5}
		]
	/>
</div>

<#-- 查询面板div -->
 <div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 		<div data-options="region:'north',border:false" >
		  <div class="search-div" id="itemCellContentSearchDiv">
			<form id='exportForm' method="post" action="../item_cell_content/do_export">
				<input type="hidden" name="ownerNo" id="ownerNoCondition_export" />
				<input type="hidden" name="wareNo" id="wareNoCondition_export"/>
				<input type="hidden" name="areaNo" id="areaNoCondition_export"/>
				<input type="hidden" name="cellNo" id="cellNoCondition_export" />
				
				<input type="hidden" name="sysNo" id="sysNoSearch_export"/>
				<input type="hidden" name="brandNo" id="brandNoCondition_export"/>
				<input type="hidden" name="itemType" id="itemTypeCondition_export"/>
				<input type="hidden" name="quality" id="qualityCondition_export" />
				
				<input type="hidden" name="itemNo" id="itemNoCondition_export" />
				<input type="hidden" name="cateOne" id="cateOneCondition_export"/>
				<input type="hidden" name="cateTwo" id="cateTwoCondition_export"/>
				<input type="hidden" name="cateThree" id="cateThreeCondition_export"/>
				
				<input type="hidden" name="barcode" id="barcodeCondition_export" />
				<input type="hidden" name="itemName" id="itemNameCondition_export" />
				<input type="hidden" name="yearsName" id="yearsNameCondition_export" />
				<input type="hidden" name="seasonName" id="seasonNameCondition_export" />
				<input type="hidden" name="genderName" id="genderNameCondition_export" />
				
				<input type="hidden" name="locno" id="locnoCondition_export"/>
				<input type="hidden" name="exportColumns" id="exportColumnsCondition_export"/>
				<input type="hidden" name="fileName" value="商品储位库存报表"/>
			</form>
			
			<form name="searchForm" id="searchForm" method="post" class="city-form" >
				<table>
					<tr>
						<td class="common-td blank">货主：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoCondition"/></td>
						<td class="common-td blank">仓区：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="wareNo" id="wareNoCondition" data-options="
																		onChange:function(){
																			itemcellcontent.selectAreaNoForSearch();
																		}
																		"/></td>
						<td class="common-td blank">库区：</td>
						<td><input class="easyui-combotree ipt" data-options="checkbox:true,multiple:true" style="width:120px" name="areaNo" id="areaNoCondition"/></td>
						<td class="common-td blank">储位：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNoCondition" /></td>	
					</tr>
					<tr>
						<td class="common-td blank"> 品牌库：</td>
		     			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		     			<td class="common-td blank">品牌：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition"/></td>
						<td class="common-td blank">商品类型：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemTypeCondition"/></td>
						<td class="common-td blank">商品品质：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="qualityCondition"/></td>
					</tr>
					<tr>
						<td class="common-td blank">商品编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
						<td class="common-td blank">大类一：</td>
			    	    <td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
						<td class="common-td blank">大类二：</td>
			    		<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
			    		<td class="common-td blank">大类三：</td>
			    		<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
					</tr>
					<tr>
					    <td class="common-td blank">商品条码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeCondition" /></td>
						<td class="common-td blank">年份：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="yearsName" id="yearsNameCondition" /></td>
						<td class="common-td blank">季节：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="seasonName" id="seasonNameCondition" /></td>
						<td class="common-td blank">性别：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="genderName" id="genderNameCondition" /></td>
					</tr>
					<tr>
						<td class="common-td blank">商品名称：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameCondition" /></td>
						<td class="common-td blank"></td>
						<td></td>
					    <td class="common-td blank"></td>
						<td></td>
						<td class="common-td blank"></td>
						<td></td>
					</tr>
					
				</table>
			</form>
	</div>
</div>		 
<#-- 数据列表div -->
<div data-options="region:'center',border:false">
    <@p.datagrid id="dataGridItemCellContent"  loadUrl="" saveUrl=""  defaultColumn=""   title="商品列表"
       isHasToolBar="false"  onClickRowEdit="false"    pagination="true"
       rownumbers="true" emptyMsg=""  showFooter="true"
       columnsJsonList="[
          	{field : 'ownerName',title : '货主',width : 60,align:'left',sortable:true},
          	{field : 'areaName',title : '库区',width : 100,align:'left',sortable:true},
          	{field : 'cellNo',title : '储位',width : 100,align:'left',sortable:true},
          	{field : 'itemNo',title : '商品编码',width : 150,align:'left',sortable:true},
          	{field : 'itemName',title : '商品名称',width : 150,align:'left',sortable:true},
          	{field : 'sizeNo',title : '尺码',width : 60,align:'left',sortable:true},
          	{field : 'colorName',title : '颜色',width : 60,align:'left',sortable:true},
          	{field : 'qty',title : '库存数量',width : 60,align:'right',sortable:true},
          	{field : 'cbqty',title : '箱库存',width : 60,align:'right',sortable:true},
          	{field : 'bulkqty',title : '零散库存',width : 60,align:'right',sortable:true},
          	{field : 'instockQty',title : '预上数量',width : 60,align:'right',sortable:true},
          	{field : 'outstockQty',title : '预下数量',width : 60,align:'right',sortable:true},
          	{field : 'unusualQty',title : '例外数量',width : 60,align:'right',sortable:true},
          	{field : 'usableQty',title : '可用数量',width : 60,align:'right',sortable:true},
          	{field : 'schedulingQty',title : '可调度数量',width : 80,align:'right',sortable:true},
          	{field : 'brandName',title : '品牌',width : 80,align:'left',sortable:true},
          	{field : 'itemTypeName',title : '商品类型',width : 60,align:'left',sortable:true},
          	{field : 'qualityName',title : '商品品质',width : 60,align:'left',sortable:true},
          	{field : 'cateName',title : '商品大类',width : 80,align:'left',sortable:true},
          	{field : 'yearsName',title:'年份',width:70,align:'left',sortable:true},
		    {field : 'seasonName',title:'季节',width:70,align:'left',sortable:true},
		     {field : 'genderName',title:'性别',width:70,align:'left',sortable:true},
          	{field : 'barcode',title : '商品条码',width : 170,align:'left',sortable:true},
          	{field : 'statusName',title : '盘点锁定标识',width : 100,align:'left',sortable:true},
          	{field : 'manualName',title : '手工移库标识',width : 100,align:'left',sortable:true}
          	
         ]" 
          
       jsonExtend='{onSelect:function(rowIndex, rowData){
                // 触发点击方法  调JS方法
                //item.loadDetail(rowData.itemNo);
       }}'/>
 </div>
 </div>
 </div>
</body>
</html>