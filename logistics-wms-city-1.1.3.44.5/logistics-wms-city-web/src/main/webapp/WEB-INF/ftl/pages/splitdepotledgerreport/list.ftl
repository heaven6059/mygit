<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分仓库存总账报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/splitdepotledgerreport/splitdepotledgerreport.js?version=1.0.0.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"splitdepotledgerreport.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"splitdepotledgerreport.searchClear('searchForm');","type":0}
       		{"title":"导出","iconCls":"icon-export","action":"splitdepotledgerreport.doExport();","type":0}
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
							<td class="common-td blank">商品品质：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="qualityCondition" /></td>
							<td class="common-td blank">商品类型：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemTypeCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">大类一：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			             	<td class="common-td blank">大类二：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
			             	<td class="common-td blank">大类三：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
							<td class="common-td">性别：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="gender" id="genderCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="years" id="yearsCondition" /></td>
							<td class="common-td blank">季节：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="season" id="seasonCondition" /></td>
							<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                     		<td ><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition"/></td>
							<td class="common-td blank">品牌：</td>
		             		<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition" /></td>	
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
				jsonExtend="{}"/>
						
	 </div>
 
 </div>
 </div>
</body>
</html>