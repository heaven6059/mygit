<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>发货查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billomdeliverexport/billomdeliverexport.js?version=1.0.5.2"></script>
</head>
<body class="easyui-layout">
		<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
			<@p.toolbar id="toolBarOne"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billomdeliverexport.search();","type":0},
			    {"title":"清空","iconCls":"icon-remove","action":"billomdeliverexport.searchClear();","type":0}
				{"title":"导出","iconCls":"icon-export","action":"billomdeliverexport.do_export()","type":5}
		   	]/>	
		</div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
	  		<div id="searchDiv" style="padding:10px;">
		  		
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td blank">品牌库：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCon"  required="true"/></td>
							<td class="common-td blank">品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCon"/></td>
							<td class="common-td blank">发货日期：</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="startExpDate" id="startExpDateCon"/></td>
							<td class="common-line">&mdash;</td>	
							<td><input class="easyui-datebox ipt" style="width:120px" name="endExpDate"  id="endExpDateCon" data-options="validType:['vCheckDateRange[\'#startExpDateCon\',\'结束日期不能小于开始日期\']']"/></td>
						</tr>
						<tr>
							<td class="common-td blank">客户：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="storeNo" id="storeNoCon"/></td>
							<td class="common-td blank">季节：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="seasonName" id="seasonNameCondition" /></td>
							<td class="common-td blank">性别：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="genderName" id="genderNameCondition" /></td>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="yearsName" id="yearsNameCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">发货通知单：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoCon"/></td>
							<td class="common-td blank">大类一：</td>
			    			<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			    			<td class="common-td blank">大类二：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
							<td class="common-td blank">大类三：</td>
						    <td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">商品编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCon"/></td>
							<td class="common-td blank">商品条码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeCon"/></td>
							<td class="common-td blank">商品名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameCon"/></td>
							<td class="common-td blank"></td>
							<td></td>
						</tr>
					</table>
				</form>		
		</div>
	</div>	
			<div data-options="region:'center',border:false">
	          	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="发货报表列表"
	          	 isHasToolBar="false" divToolbar="" height="430"  onClickRowEdit="false"  pagination="true" singleSelect = "false" 
	          	 rownumbers="true" columnsJsonList="" emptyMsg="" showFooter="true"
			   	/>
		   	</div>
		</div>	  
</div>
</body>
</html>