<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>门店箱标签打印</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <#include  "/WEB-INF/ftl/common/header.ftl" >
   <script type="text/javascript" src="${domainStatic}/resources/js/billuslabelprint/billuslabelprint.js?version=1.0.5.1"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<@p.toolbar id="main-toolbar"  listData=[
             {"title":"查询","iconCls":"icon-search","action":"billuslabelprint.searchArea();","type":0},
			 {"title":"清除","iconCls":"icon-remove","action":"billuslabelprint.searchLocClear();","type":0}
			 ]
		/>
		<div>
			<form class="city-form" style="padding:10px;" id="searchForm">
			<table>
				<tr>
					<td class="common-td blank">打印日期：</td>
					<td><input class="easyui-datebox" style="width:150px" name="printTm_start" id="printTm_start"/></td>
					<td class="common-td blank">至：</td>
					<td><input class="easyui-datebox" style="width:150px" name="printTm_end" id="printTm_end"/></td>
					<td class="common-td blank">打印人：</td>
					<td colspan="5"><input class="easyui-combobox" style="width:150px" name="creator" id="printer"/></td>
				</tr>
			</table>
		</form>
		</div>	
		<div style="padding:10px;">
			<form class="city-form" style="border:1px solid #95B8E7;padding:10px;" id="dataForm">
				<#--打印类型：<input type="text" class="easyui-combobox" id="printType" name="printType" data-options="editable:false">&nbsp;&nbsp;&nbsp;&nbsp;-->
				打印数量：<input type="text" class="easyui-numberbox ipt" id="qty" name="qty">&nbsp;&nbsp;&nbsp;&nbsp;
				门店：<input class="easyui-combogrid ipt" style="width:310px" name="storeNo" id="storeNoCondition"/>&nbsp;&nbsp;
				<#--店铺简称：<input type="text" class="easyui-validatebox ipt" id="storeName" name="storeName"  data-options="validType:['vLength[0,4,\'最多只能输入4个字符\']']">-->
				<a href="javascript:billuslabelprint.printUM();" class="easyui-linkbutton" data-options="iconCls:'icon-print'">退仓打印</a>&nbsp;&nbsp;
				<a href="javascript:billuslabelprint.printZD();" class="easyui-linkbutton" data-options="iconCls:'icon-print'">转店打印</a>
			</form>
		</div>
	</div>
	<div data-options="region:'center',border:false">
			<@p.datagrid id="dataGridJG"  loadUrl="/bm_print_log/getList.json?locno=${session_user.locNo}&papertype=US" saveUrl=""   defaultColumn="" 
              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  height="300"  
               pagination="true" rownumbers="true"  singleSelect = "false" title="标签打印日志" emptyMsg="" showFooter="true"
	           columnsJsonList="[
	           		{field : 'startSerial',title : '起始标签',width : 180,align:'center'},
					{field : 'endSerial',title : '结束标签',width : 180},
					{field : 'storeName',title : '店铺简称',width : 180,align:'left'},
					{field : 'getQty',title : '打印数量',width : 100,align:'right'},
					{field : 'createtm',title : '打印时间',width :135},
					{field : 'creator',title : '打印人',width : 120,align:'left'},
					{field : 'creatorName',title : '打印人名称',width : 80,align:'left'}
	                 ]" 
		           jsonExtend='{onSelect:function(rowIndex, rowData){
                            // 触发点击方法  调JS方法
                   },onDblClickRow:function(rowIndex, rowData){
                   	  //  billchrecheck.loadDetail(rowData);
                   }}'/>
    </div>
</div>
</body>
</html>