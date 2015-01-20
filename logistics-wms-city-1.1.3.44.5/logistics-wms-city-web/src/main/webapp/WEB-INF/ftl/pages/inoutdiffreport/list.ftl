<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>差异报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/inoutdiffreport/inoutdiffreport.js?version=1.0.0.1"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"inoutdiffreport.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"inoutdiffreport.searchClear('searchForm');","type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
		]
	/>
</div>


<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 		<div  data-options="region:'north',border:false" >
 		
			<div id="splitdepotSearchDiv"  style="padding:10px;">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td">日期：</td>
				            <td><input class="easyui-datebox" style="width:120px" name="startDate" id="startDateCondition" /></td>
				            <td class="common-line" >&mdash;</td>
				            <td><input class="easyui-datebox" style="width:120px" name="endDate" id="endDateCondition"/></td>
				            <td class="common-td blank">来源单据：</td>
                     		<td><input class="easyui-validatebox ipt" style="width:120px" name="billNo" id="billNoCondition" /></td>
				            <td class="common-td blank">单据类型：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="billType" id="billTypeCondition" /></td>
						</tr>
						<tr>
				            <td class="common-td blank">状态：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
							<td class="common-td blank">商品编码：</td>
			             	<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
							<td class="common-td blank">大类一：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			             	<td class="common-td blank">大类二：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
						</tr>
						<tr>
			             	<td class="common-td blank">大类三：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
							<td class="common-td blank">商品品质：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="qualityCondition" /></td>
							<td class="common-td">性别：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="gender" id="genderCondition" /></td>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="years" id="yearsCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">季节：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="season" id="seasonCondition" /></td>
							<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                     		<td ><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition"/></td>
							<td class="common-td blank">品牌：</td>
		             		<td colspan = '3'><input class="easyui-combobox ipt" style="width:295px" name="brandNo" id="brandNoCondition" /></td>	
						</tr>
					</table>
				</form>
			</div>
	</div>			 


	<#-- 数据列表div -->
	<div data-options="region:'center',border:false">
	     <@p.datagrid id="dataGridJG"  defaultColumn=""  title='查询结果列表'  
		  		isHasToolBar="false" height="525"  pageSize="20" 
				onClickRowEdit="false" onClickRowAuto="false" 
				pagination="true" rownumbers="true" showFooter="true" emptyMsg=" " 
				columnsJsonList="[
							 		{title:'日期',field:'poDate',width:80,align:'left'},
							 		{title:'来源单据',field:'billNo',width:130,align:'left'},
							 		{title:'单据类型',field:'billTypeName',width:80,align:'left'},
							 		{title:'状态',field:'statusName',width:80,align:'left'},
							 		{title:'业务类型',field:'bizTypeName',width:80,align:'left'},
							 		{title:'门店编码',field:'storeNo',width:80,align:'left'},
							 		{title:'门店名称',field:'storeName',width:120,align:'left'},
							 		{title:'操作单据(收货/拣货)',field:'receiptNo',width:130,align:'left'},
							 		{title:'单据类型',field:'receiptTypeName',width:80,align:'left'},
							 		{title:'操作单据(验收/复核)',field:'checkNo',width:130,align:'left'},
							 		{title:'单据类型',field:'checkTypeName',width:80,align:'left'},
							 		{title:'来源箱号',field:'boxNo',width:110,align:'left'},
							 		{title:'商品编码',field:'itemNo',width:130,align:'left'},
							 		{title:'尺码',field:'sizeNo',width:80,align:'left'},
							 		{title:'计划数量',field:'itemQty',width:60,align:'right'},
							 		{title:'数量(收货/拣货)',field:'receiptQty',width:100,align:'right'},
							 		{title:'数量(验收/复核)',field:'checkQty',width:100,align:'right'},
							 		{title:'差异数量',field:'diffQty',width:60,align:'right'},
							 		{title:'操作人员',field:'operor',width:80,align:'left'},
							 		{title:'操作人员名',field:'operorName',width:80,align:'left'},
							 		{title:'操作时间',field:'opertm',width:80,align:'left'}
							 	  ]"
				jsonExtend="{}"/>
						
	 </div>
 
 </div>
 </div>
</body>
</html>