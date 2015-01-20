<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分仓库存按天汇总报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/splitdepotdatesumreport/splitdepotdatesumreport.js?version=1.0.5.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"splitdepotdate.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"splitdepotdate.searchClear('searchForm');","type":0}
       		{"title":"导出","iconCls":"icon-export","action":"splitdepotdate.doExport();","type":0}
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
				            <td><input class="easyui-datebox" style="width:120px" name="startDate" id="startDateCondition" required="true" /></td>
				            <td class="common-line" >&mdash;</td>
				            <td><input class="easyui-datebox" style="width:120px" name="endDate" id="endDateCondition" required="true" /></td>
				            <td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                     		<td ><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
                     		<td class="common-td blank">品牌：</td>
		             		<td ><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
						</tr>
						<tr>
							<td class="common-td">商品编码：</td>
				            <td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
		             		<td class="common-td blank">大类一：</td>
			    			<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			    			<td class="common-td blank">大类二：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
							<td class="common-td blank">大类三：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
						</tr>
						<tr>
							<td class="common-td">性别：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="gender" id="genderCondition" /></td>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="years" id="yearsCondition" /></td>
							<td class="common-td blank">季节：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="season" id="seasonCondition" /></td>
                     		<td class="common-td blank">商品品质：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="qualityCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">商品类型：</td>
				            <td colspan = '7'><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemTypeCondition" /></td>
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
				pagination="false" rownumbers="true" showFooter="true" emptyMsg=" " 
				columnsJsonList="[
							 		{title:'日期',field:'selectDate',width:100,align:'left',rowspan: 2},
							 		{title:'上期库存',field:'qty',width:80,align:'right',rowspan: 2},
							 		{title:'本期库存',field:'thisIssueQty',width:80,align:'right',rowspan: 2},
							 		{title:'入库(已入库)',field:null,width:320,colspan: 5},
							 		{title:'出库',field:null,width:320,colspan: 5},
							 		{title:'盘点',field:null,width:160,colspan: 2},
							 		{title:'库存调整',field:null,width:160,colspan: 2},
							 		{title:'预计到货未收',field:null,width:240,colspan: 6},
							 		{title:'已收未验',field:null,width:240,colspan: 6},
							 		{title:'已收货数',field:null,width:240,colspan: 6}
							 	  ],[
							 		{title:'厂入库',field:'rkCrkQty',width:80,align:'right'},
							 		{title:'仓移入',field:'rkCyrQty',width:80,align:'right'},
							 		{title:'店退货',field:'rkDthQty',width:80,align:'right'},
							 		{title:'其他入库',field:'rkQtrkQty',width:80,align:'right'},
							 		{title:'差异调整',field:'rkCytzQty',width:80,align:'right'},
							 		{title:'仓出店',field:'ckCcdQty',width:80,align:'right'},
							 		{title:'仓移出',field:'ckCycQty',width:80,align:'right'},
							 		{title:'其他出库',field:'ckQtckQty',width:80,align:'right'},
							 		{title:'跨部门转货',field:'ckKbmzhQty',width:80,align:'right'},
							 		{title:'差异调整',field:'ckCytzQty',width:80,align:'right'},
							 		{title:'盘赢',field:'pdPyQty',width:80,align:'right'},
							 		{title:'盘亏',field:'pdPkQty',width:80,align:'right'},
							 		{title:'品质',field:'tzKctzQty',width:80,align:'right'},
							 		{title:'属性',field:'tzKctzTypeQty',width:80,align:'right'},
							 		{title:'厂入',field:'ydhCrQty',width:80,align:'right'},
							 		{title:'厂入箱数',field:'ydhCrBoxQty',width:80,align:'right'},
							 		{title:'仓移入',field:'ydhCyrQty',width:80,align:'right'},
							 		{title:'仓移入箱数',field:'ydhCyrBoxQty',width:80,align:'right'},
							 		{title:'店退仓',field:'ydhDtcQty',width:80,align:'right'},
							 		{title:'店退仓箱数',field:'ydhDtcBoxQty',width:80,align:'right'},
							 		{title:'厂入',field:'yswyCrQty',width:80,align:'right'},
							 		{title:'厂入箱数',field:'yswyCrBoxQty',width:80,align:'right'},
							 		{title:'仓移入',field:'yswyCyrQty',width:80,align:'right'},
							 		{title:'仓移入箱数',field:'yswyCyrBoxQty',width:80,align:'right'},
							 		{title:'店退仓',field:'yswyDtcQty',width:80,align:'right'},
							 		{title:'店退仓箱数',field:'yswyDtcBoxQty',width:80,align:'right'},
							 		{title:'厂入',field:'yshCrQty',width:80,align:'right'},
							 		{title:'厂入箱数',field:'yshCrBoxQty',width:80,align:'right'},
							 		{title:'仓移入',field:'yshCyrQty',width:80,align:'right'},
							 		{title:'仓移入箱数',field:'yshCyrBoxQty',width:80,align:'right'},
							 		{title:'店退仓',field:'yshDtcQty',width:80,align:'right'},
							 		{title:'店退仓箱数',field:'yshDtcBoxQty',width:80,align:'right'}
							 	  ]"
				jsonExtend="{}"/>
						
	 </div>
 
 </div>
 </div>
</body>
</html>