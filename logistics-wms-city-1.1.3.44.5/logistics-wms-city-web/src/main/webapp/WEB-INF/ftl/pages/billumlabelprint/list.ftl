<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>箱标签打印</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <#include  "/WEB-INF/ftl/common/header.ftl" >
   <script type="text/javascript" src="${domainStatic}/resources/js/billumlabelprint/billumlabelprint.js?version=1.0.5.1"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout" onload="setTemplateType(getCookie('lastTemplateType'))">
	<div id="tags" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
		<div title="打印"> 
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" style="height:140px;">
					<@p.toolbar id="pint-toolbar"  listData=[
			             {"title":"打印预览","iconCls":"icon-print","action":"billumlabelprint.showPrintMenu()","type":0}
						 ]
					/>
					<div style="padding:10px;border:1px solid #95B8E7;padding:10px;">
						<input type='radio' name='templateType' value='00'/>精简模板&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type='radio' name='templateType' value='01'/>完整模板(10cm*15cm)
					</div>
					
					<div id='simpleTemplate'>
						<div style="padding:10px;">
							<form class="city-form" style="border:1px solid #95B8E7;padding:10px;" id="dataForm">
								打印类型：<input type="text" class="easyui-combobox" id="printType" name="printType" data-options="editable:false">&nbsp;&nbsp;&nbsp;&nbsp;
								打印数量：<input type="text" class="easyui-numberbox ipt" id="qty" name="qty">
								店铺简称：<input type="text" class="easyui-validatebox ipt" id="storeName" name="storeName"  data-options="validType:['vLength[0,4,\'最多只能输入4个字符\']']">
								<!--<a href="javascript:billumlabelprint.showPrintMenu();" class="easyui-linkbutton" data-options="iconCls:'icon-print'">打印标签</a>-->
							</form>
						</div>
						<div id="print_menu" class="easyui-menu">
						    <div data-options="iconCls:'icon-print'" onclick="billumlabelprint.printBatchNew('600','500');">6.0 x 5.0</div>
						    <div data-options="iconCls:'icon-print'" onclick="billumlabelprint.printBatchNew('600','400');">6.0 x 4.0</div>
						</div>
					</div>
					<div id='fullTemplate'>
						<div style="padding:10px;">
							<form class="city-form" style="border:1px solid #95B8E7;padding:10px;" id="fullDataForm">
								 品&nbsp;牌&nbsp;库：<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo"/>
								 线路：<input class="easyui-combobox" style="width:120px" id="bufferName" name="bufferName" data-options="editable:false"/>
								 店铺编码：<input class="easyui-validatebox ipt" style="width:120px" id="storeNo" name="storeNo"/>
								 店铺名称：<input class="easyui-validatebox ipt" style="width:120px" id="storeName" name="storeName"/>
							</form>
						</div>
					</div>
				</div>
				<div data-options="region:'center',border:true" id='full1_div'>
					<@p.toolbar id="full1-toolbar"  listData=[
			             {"title":"查询","iconCls":"icon-search","action":"billumlabelprint.searchFull()","type":0},
						 {"title":"清除","iconCls":"icon-remove","action":"billumlabelprint.clearForm('fullDataForm')","type":0},
						 {"title":"添加","iconCls":"icon-add","action":"billumlabelprint.add2Ready()","type":0}
						 ]
					/>
					<@p.datagrid id="full1DataGridJG"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#full1-toolbar" onClickRowEdit="false"  height="300"  
			               pagination="true" rownumbers="true"  singleSelect = "false" title="打印店铺信息" emptyMsg="" showFooter="true"
				           columnsJsonList="[
				           		{field : ' ',checkbox:true},
				           		{field : 'storeNo',title : '店铺编码',width : 120,align:'left'},
				           		{field : 'storeName',title : '店铺名称',width : 180,align:'left'},
				           		{field : 'address',title : '店铺地址',width : 250,align:'left'},
				           		{field : 'bufferName',title : '线路',width : 100,align:'left'}
				                 ]" />
				</div>
				<div  data-options="region:'south'" style="height:200px;" id='full2_div'>
					<@p.toolbar id="full2-toolbar"  listData=[
						 {"title":"清空","iconCls":"icon-remove","action":"billumlabelprint.clearDataGrid('full2DataGridJG')","type":0},
						 {"title":"删除","iconCls":"icon-del","action":"billumlabelprint.deleteDataGridByCheck('full2DataGridJG')","type":0}
						 ]
					/>
					<@p.datagrid id="full2DataGridJG"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#full2-toolbar" onClickRowEdit="true"  height="300"  
			               pagination="true" rownumbers="true"  singleSelect = "true" title="待打印店铺信息" emptyMsg="" showFooter="true"
				           columnsJsonList="[
				           		{field : ' ',checkbox:true},
				           		{field : 'storeNo',title : '店铺编码',width : 120,align:'left'},
				           		{field : 'storeName',title : '店铺名称',width : 180,align:'left'},
				           		{field : 'qty',title : '打印数量',width : 80,align:'left',editor:{
					 				type:'numberbox',
					 				options:{
					 					required:true,
						 				min:1
					 				}
					 			}},
				           		{field : 'address',title : '店铺地址',width : 250,align:'left'},
				           		{field : 'bufferName',title : '线路',width : 100,align:'left',
				 					editor:{
				 						type:'validatebox',
				 						options:{}
			 					}}
				                 ]" />
				</div>
			</div>
		</div>
		<div title="查询"> 
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar id="main-toolbar"  listData=[
			             {"title":"查询","iconCls":"icon-search","action":"billumlabelprint.searchArea();","type":0},
						 {"title":"清除","iconCls":"icon-remove","action":"billumlabelprint.searchLocClear();","type":0}
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
					
				</div>
				<div data-options="region:'center',border:false">
						<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn="" 
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
				                 ]"/>
			    </div>
			   </div>
		</div>
	</div>
</body>
</html>