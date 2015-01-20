<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分仓库存进出明细报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/splitdepotinoutreport/splitdepotinoutreport.js?version=1.0.5.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"splitdepot.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"splitdepot.searchClear('searchForm');","type":0},
       		{"title":"导出","iconCls":"icon-export","action":"splitdepot.do_export()","type":5},
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
							<td class="common-td">商品编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
                     		<td class="common-td blank">商品类型：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemTypeCondition" /></td>
							<td class="common-td blank">商品品质：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="qualityCondition" /></td>
				            <td class="common-td blank">性别：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="gender" id="genderCondition" /></td>
				            <#-- 
							<td class="common-td" blank>大类：</td>
				            <td><input class="easyui-validatebox ipt" style="width:120px" name="cateCode" id="cateCodeCondition" /></td>
				            -->
						</tr>
						<tr>
							<td class="common-td">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="years" id="yearsCondition" /></td>
							<td class="common-td blank">季节：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="season" id="seasonCondition" /></td>
							<td class="common-td blank">时间：</td>
				            <td><input class="easyui-datebox" style="width:120px" name="startContentDate" id="startContentDateCondition" required="true" /></td>
				            <td class="common-line" >&mdash;</td>
				            <td><input class="easyui-datebox" style="width:120px" name="endContentDate" id="endContentDateCondition" required="true" /></td>
						</tr>
						<tr>
							<td class="common-td">大类一：</td>
			    			<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			    			<td class="common-td blank">大类二：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
							<td class="common-td blank">大类三：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
							<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		             		<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoSearch"/></td>	
						</tr>
						<tr>
							<td class="common-td">品牌：</td>
                     		<td colspan = '3'><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
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
							 		{title:'商品编码',field:'itemNo',width:140,align:'left',rowspan: 2},
							 		{title:'名称',field:'itemName',width:150,align:'left',rowspan: 2},
							 		{title:'颜色',field:'colorName',width:80,align:'left',rowspan: 2},
							 		{title:'品牌',field:'strBrandNo',width:80,align:'left',rowspan: 2},
							 		{title:'大类',field:'strCateNo',width:60,align:'left',rowspan: 2},
							 		{title:'年份',field:'strYears',width:60,rowspan: 2},
							 		{title:'上期库存',field:'qty',width:80,align:'right',rowspan: 2},
							 		{title:'本期库存',field:'thisIssueQty',width:80,align:'right',rowspan: 2},
							 		
							 		{title:'入库',field:null,width:320,colspan: 5},
							 		{title:'出库',field:null,width:320,colspan: 5},
							 		{title:'盘点',field:null,width:160,colspan: 2}
							 	  ],[
							 		{title:'厂入库',field:'crkQty',width:80,align:'right'},
							 		{title:'仓移入',field:'cyrQty',width:80,align:'right'},
							 		{title:'店退货',field:'dthQty',width:80,align:'right'},
							 		{title:'其他入库',field:'qtrkQty',width:80,align:'right'},
							 		{title:'差异调整',field:'rkCytzQty',width:80,align:'right'},
							 		{title:'仓出店',field:'ccdQty',width:80,align:'right'},
							 		{title:'仓移出',field:'cycQty',width:80,align:'right'},
							 		{title:'其他出库',field:'qtckQty',width:80,align:'right'},
							 		{title:'跨部门转货',field:'kbmzhQty',width:80,align:'right'},
							 		{title:'差异调整',field:'ckCytzQty',width:80,align:'right'},
							 		{title:'盘赢',field:'pyQty',width:80,align:'right'},
							 		{title:'盘亏',field:'pkQty',width:80,align:'right'}
							 	  ]"
				jsonExtend="{}"/>
						
	 </div>
 
 </div>
 </div>
</body>
</html>