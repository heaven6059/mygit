<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓验收条码补印</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <#include  "/WEB-INF/ftl/common/header.ftl" >
   <script type="text/javascript" src="${domainStatic}/resources/js/billumchecklateprint/billumchecklateprint.js?version=1.0.5.1"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>

<body class="easyui-layout">
	
	<div data-options="region:'west'" data-options="fit:true" style="width:200px;border-left:0px;">
		<@p.toolbar id="print-toolbar"  listData=[
			{"title":"打印","iconCls":"icon-print","action":"billumchecklateprint.printBarcode();","type":0},
			{"title":"清空","iconCls":"icon-remove","action":"billumchecklateprint.printClear('printBarcode');","type":0}
		]/>
		<div style="padding:5px;">
			<form class="city-form" id="dataForm">
				打印份数：<input id="qty" name="qty" class="easyui-numberbox ipt" style="width:120px" data-options="value:1"/>
			</form>
		</div>
		<div style="padding:5px;">
			条码导入列表：<br/>
			<textarea id="printBarcode"  name="printBarcode"  
			style="width:180px;height:450px;resize:none;"></textarea>
		</div>
	</div>
	
		
	<div data-options="region:'center'" >
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west'" data-options="fit:true" 
				 style="width:60px;border-left:0px;border-top:0px;border-bottom:0px;">
				<div style="text-align:center;padding-top:250px;vertical-align:middle;">
					<a class="easyui-linkbutton" href="javascript:billumchecklateprint.toPrintText();"><<</a>
				</div>
			</div>
				
			<div data-options="region:'center',border:false" >
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false">
						<@p.toolbar id="main-toolbar"  listData=[
							{"title":"查询","iconCls":"icon-search","action":"billumchecklateprint.searchData();","type":0},
							{"title":"清除","iconCls":"icon-remove","action":"billumchecklateprint.searchClear('searchForm');","type":0},
							{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退仓验收条码补印');","type":0}
						]/>
					<div>
					<form class="city-form" style="padding:10px;" id="searchForm">
						<table>
			   				<tr>
			   					<td class="common-td">创建日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetm"/></td>
			   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetm"/></td>
			   					<td class="common-td blank">商品条码：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">店退仓单号：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo"/></td>
			   					<td class="common-td blank">验收单号：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/></td>
			   					<td class="common-td blank">客户名称：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="storeNo" id="storeNoCondition" /></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">状态：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>				
			   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		             			<td class="common-td blank">所属品牌：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
			   				</tr>
			   			</table>
					</form>
				</div>	
			</div>
				 
				 
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"   saveUrl=""  defaultColumn="" 
						isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  height="300"  
						pagination="true" rownumbers="true"  singleSelect = "false" title="补打信息列表" 
						emptyMsg="" showFooter="true"
						columnsJsonList="[
							{field : 'id',checkbox:true},
							{field : 'barcode',hidden : true},
							{field : 'untreadNo',title : '店退仓单号',width : 140},
							{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
							{field : 'storeName',title : '客户名称',width : 160,align:'left'},
							{field : 'itemNo',title : '商品编码',width : 130,align:'left'},
							{field : 'itemName',title : '商品名称',width :135,align:'left'},
							{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
							{field : 'colorName',title : '颜色',width : 80,align:'left'},
							{field : 'brandName',title : '品牌',width : 100,align:'left'},
							{field : 'boxNo',title : '箱号',width : 110,align:'left'},
							{field : 'itemQty',title : '计划数量',width : 60,align:'right'},
							{field : 'checkQty',title : '验收数量',width : 60,align:'right'}
						 ]" 
						 jsonExtend='{}'/>
			</div>
				
		</div>
	</div>
			
</body>
</html>