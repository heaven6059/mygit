<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂拣货</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billwmoutstock/billwmoutstock.js?version=1.0.5.1"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/billwmoutstock.css?version=1.0.5.0" />
    <!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>

<body class="easyui-layout">
	
	<#-- 工具菜单div -->
    <div data-options="region:'north',border:false" class="toolbar-region">
    	<@p.toolbar id="toolbar" listData=[
    		{"title":"查询","iconCls":"icon-search","action":"billwmoutstock.search()", "type":0},
		 	{"title":"清除","iconCls":"icon-remove","action":"billwmoutstock.searchClear('searchForm')", "type":0},
		 	{"title":"修改","iconCls":"icon-edit","action":"billwmoutstock.showModify()", "type":2},
		 	{"title":"打印预览","iconCls":"icon-print","action":"billwmoutstock.printDetail()","type":4},
			{"title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂拣货')","type":0}
	    ]/>
	</div>
	
	<div data-options="region:'center',border:false">
		<#-- 查询条件 start -->
		<div class="easyui-layout" data-options="fit:true">
			
			<div data-options="region:'north',border:false" >
				<input id="ownerNoHide" type="hidden"/>
				<form id="searchForm" class="city-form" style="padding:5px;">
					<table>
						<tr>
							<td class="common-td">单据状态：</td>
							<td><input class="easyui-combobox ipt" name="status" id="searchStatus" style="width:120px;"/></td>
							<td class="common-td blank">单据编号：</td>
							<td><input class="easyui-validatebox ipt" name="outstockNo" style="width:120px;"/></td>
							<td class="common-td blank">创建时间：</td>
							<td><input class="easyui-datebox" name="createTmStart" id="createTmStart" style="width:120px;"/></td>
							<td class="common-line" width='50'>&mdash;</td>
							<td><input class="easyui-datebox" name="createTmEnd" id="createTmEnd" style="width:120px;"/></td>
						</tr>
						
						<tr>
							<td class="common-td">品&nbsp;牌&nbsp;库：</td>
							<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
							<td class="common-td blank">拣货人：</td>
							<td><input class="easyui-combobox" style="width:120px" name="assignName" id="assignName" /></td>
							<td class="common-td blank">拣货时间：</td>
							<td><input class="easyui-datebox" name="outstockDateStart" id="outstockDateStart" style="width:120px;"/></td>
							<td class="common-line">&mdash;</td>
							<td>
								<input class="easyui-datebox" name="outstockDateEnd" id="outstockDateEnd" style="width:120px;"/>
							</td>
						</tr>
						<tr>
							<td class="common-td blank">所属品牌：</td>
	             			<td colspan="7">
	             				<input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" />
	             			</td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl="" defaultColumn=""
			      isHasToolBar="false" onClickRowEdit="false"    pagination="true"
			       rownumbers="true" emptyMsg="" singleSelect = "false" divToolbar="" title="退厂拣货列表"
			       columnsJsonList="[
			       			{field : 'ck',title : '',width : 50, checkbox:true},
			       			{field : 'ownerNo',hidden:true},
							{field : 'status',title : '单据状态',width :150,align:'left',formatter:billwmoutstock.statusFormatter},
							{field : 'outstockNo',title :'单据编号',width :180},
							{field : 'createtm',title :'创建日期',width : 150},
							{field : 'outstockDate',title :'拣货日期',width : 150},
							{field : 'assignName',title :'拣货人',width : 150,align:'left'},
							{field : 'assignChName',title :'拣货人名称',width : 150,sortable:true},
							{field : 'editor',title : '更新人',width : 100,sortable:true},
							{field : 'editorName',title : '更新人名称',width : 100,sortable:true},
						    {field : 'edittm',title : '更新时间',width : 130,sortable:true}
			             ]"
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
					    	//双击方法
					       billwmoutstock.detail(rowData,\"1\")
					     }}'
			   />
			</div>
			
		</div>
	</div>
	
	 
	<div id="showDialog" class="easyui-window"  title="拣货单明细"  
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:true">
		
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<form id="dtlForm" class="city-form" style="padding:5px;" >
						<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td>
									<input class="easyui-validatebox ipt" style="width:130px" name="outstockNo" id="outstockNo"/>
									<input type="hidden" name="outstockNo" id="outstockNoHide"/>
								</td>
								<td class="common-td blank">单据状态：</td>
								<td>
									<input class="easyui-validatebox ipt" style="width:130px" name="status" id="status"/>
								</td>
								<td>
									&nbsp;&nbsp;<a id="checkBtn" href="javascript:billwmoutstock.check();" class="easyui-linkbutton">拣货确认</a>
								</td>
								<td class="common-td blank" style="display:none;">实际拣货人：</td>
								<td style="display:none;"><input class="easyui-combobox ipt" style="width:130px" id="outstockName" name="outstockName" /></td>
								<td style="display:none;">&nbsp;<input class="easyui-combobox ipt" style="width:130px" id="outstockNameTemp" /></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false" >
					<div id="dataGridJG_Detail_div" class="easyui-layout"  data-options="fit:true">
						<div data-options="region:'center',border:false">
							<@p.datagrid id="dataGridJG_Detail"  loadUrl="" saveUrl=""   defaultColumn=""
					      		isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					       		rownumbers="true"  singleSelect = "false"  title="退厂拣货明细列表" showFooter="true"
					       		columnsJsonList="[
					       		  {field : 'sourceNo',title : '来源单号',width :140},
					       		  {field : 'sCellNo',title : '来源储位',width :100,align:'left'},
					       		  {field : 'itemNo',title :'商品编码',width : 140,align:'left'},
					              {field : 'itemName',title :'商品名称',width : 160,align:'left'},
					              {field : 'styleNo',title : '款号',width :80,align:'left',hidden:true},
					              {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					              {field : 'colorName',title : '颜色',width : 80,align:'left'},
								  {field : 'itemQty',title : '计划数量',width : 60,align:'right'},
					              {field : 'realQty',title : '实际数量',width : 60,align:'right'},
					              {field : 'assignName',title : '拣货人',width : 100,align:'left'},
					              {field : 'assignChName',title : '拣货人名称',width : 100,align:'left'},
					              {field:'outstockName',title:'实际拣货人',width:100,align:'left'},
					              {field:'outstockChName',title:'实际拣货人名称',width:100,align:'left'}
					             ]" 
					         />		
						</div>
					</div>
					
					<div id="dataGridJG_Detail_div2" class="easyui-layout"  data-options="fit:true">
						<div data-options="region:'center',border:false">
							<@p.datagrid id="dataGridJG_Detail2"  loadUrl="" saveUrl=""   defaultColumn=""
					      		isHasToolBar="false" onClickRowEdit="true"  pagination="true"
					       		rownumbers="true"  singleSelect = "false"  title="退厂拣货明细列表" showFooter="true"
					       		columnsJsonList="[
					       		  {field : 'sourceNo',title : '来源单号',width :140},
					       		  {field : 'sCellNo',title : '来源储位',width :100,align:'left'},
					       		  {field : 'itemNo',title :'商品编码',width : 140,align:'left'},
					              {field : 'itemName',title :'商品名称',width : 160,align:'left'},
					              {field : 'styleNo',title : '款号',width :80,align:'left',hidden:true},
					              {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					              {field : 'colorName',title : '颜色',width : 80,align:'left'},
								  {field : 'itemQty',title : '计划数量',width : 60,align:'right'},
								  {field:'realQty',title:'实际数量',width:60,align:'right',formatter:billwmoutstock.realQty,
								  		editor:{
											type:'numberbox',
											options:{
										 		required:true
									 		}
										}
								  },
								  {field:'outstockName',title:'实际拣货人',width:100,formatter:billwmoutstock.outstockNameFormatter,align:'left',
								  		editor:{
									 		type:'combobox',
									 		options:{
									 			url:'${BasePath}/authority_user/user.json',
							                   	valueField:'workerNo',
							                    textField:'unionName',
							                    panelHeight:'auto'
									 		}
									 	}
								       }
					         ]" />		
						</div>
					</div>
				</div>
				
				
			</div>
	</div>
	
</body>
</html>