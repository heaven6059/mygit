<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>拣货回单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billomoutstock/billomoutstock.js?version=1.0.5.1"></script>
    <script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/billomoutstock.css?version=1.0.5.0" />
<!--object需放在head结尾会截断jquery的html函数获取内容-->
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
</object>
</head>

<body class="easyui-layout">

	<!-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billomoutstock.search()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"billomoutstock.clear()", "type":0},
	            {"title":"修改","iconCls":"icon-edit","action":"billomoutstock.edit()", "type":2},
	            {"title":"发单","iconCls":"icon-redo","action":"billomoutstock.openSendOrder()", "type":2},
	            {"title":"审核","iconCls":"icon-aduit","action":"billomoutstock.auditOutstock();","type":4},
	            {"id":"print","title":"拣货单打印","iconCls":"icon-print","action":"","type":2},
	            {"title":"拣货标签打印","iconCls":"icon-print","action":"billomoutstock.printByOutstockSendType()", "type":2},
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('出库拣货')","type":0}
			 ]/>
	</div>
	<div id="print_menu" class="easyui-menu">
	    <div data-options="iconCls:'icon-print'" onclick="billomoutstock.printDetail4SizeHorizontal('A4');">A4预览</div>
	    <div data-options="iconCls:'icon-print'" onclick="billomoutstock.printDetail4SizeHorizontal('A5');">A5预览</div>
	</div>
	
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">单据状态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" data-options="editable:false"  id="searchStatus" /></td>
                     			<td class="common-td blank">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="outstockNo" /></td>
                     			<td class="common-td blank">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createTmStart" id="createTmStart"/></td>
                     			<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createTmEnd" id="createTmEnd"/></td>
                     		</tr>
                     		<tr>
                     			
                     			<td class="common-td">拣货类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="taskType" data-options="editable:false" id="searchLabelNo" /></td>
                     			<td class="common-td blank">拣货切单类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="outstockSendType" id="outstockSendType"/></td>
                     			<td class="common-td blank">拣货日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="outstockDateStart" id="outstockDateStart" /></td>
                     			<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="outstockDateEnd" id="outstockDateEnd"/></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">波&nbsp;次&nbsp;号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo"  id="locateNo" /></td>
		             			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
		             			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='3'><input class="easyui-combobox ipt" style="width:267px" name="brandNo" id="brandNo" /></td>
		             		</tr>
                     	</table>
				 	</form>
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""
			      isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
			       rownumbers="true"  singleSelect = "false" height="460" divToolbar="#searchArea" title="拣货单列表"
			       columnsJsonList="[
			       			{field : ' ',checkbox:true},
							{field : 'status',hidden : true},
							{field : 'statusStr',title : '单据状态',width :100,align:'left'},
							{field : 'outstockNo',title :'单据编号',width : 160},
							{field : 'outstockSendTypeStr',title :'拣货切单类型',width : 100,align:'left'},
							{field : 'storeName',title :'客户/库区名称',width : 180,align:'left'},
							{field : 'outstockDate',title :'拣货日期',width : 120},
							{field : 'taskType',title :'拣货类型',width : 100,formatter:billomoutstock.typeFormatter,align:'left'},
							{field : 'remark',title : '备注',width : 80,align:'left',sortable:true},
							{field : 'itemQty',title : '计划数量',width : 80,align:'right'},
							{field : 'realQty',title:'实际数量',width:80,align:'right'},
							{field : 'outstockedQty',title : 'RF数量',width : 80,align:'right'},
							{field : 'assignName',title :'指定拣货人员',width : 100,align:'left'},
							{field : 'assignNameCh',title :'指定拣货人员名称',width : 100,align:'left'},
							{field : 'outstockName',title :'实际拣货人员',width : 100,hidden:true,align:'left'},
							{field : 'locateNo',title :'拣货波次',width : 150},
							{field : 'creator',title :'创建人',width : 100,align:'left'},
							{field : 'creatorname',title :'创建人名称',width : 100,align:'left'},
							{field : 'createtm',title :'创建日期',width : 150},
							{field : 'editor',title : '更新人',width : 100,sortable:true},
						    {field : 'editorname',title : '更新人名称',width : 100,sortable:true},
						    {field : 'edittm',title : '更新时间',width : 130,sortable:true},
							{field : 'locno',hidden:'true'}
			             ]"
							jsonExtend='{
								onDblClickRow:function(rowIndex, rowData){
					           		//双击方法
					           		billomoutstock.detailView(rowData)
					        	},
						       	onLoadSuccess:function(data){//合计
						       		billomoutstock.onLoadSuccess(data);
						       	}}'
			             />
            </div>
		</div>
	</div>
	<!-- 拣货单明细窗口S -->
	<div id="showDialog"  class="easyui-dialog" title="拣货单明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="dtltoolbar" listData=[
	                             {"id":"checkPlanBtn","title":"按计划保存","iconCls":"icon-save-dtl","action":"billomoutstock.saveByPlan()","type":2},
    							 {"id":"checkBtn","title":"保存当页","iconCls":"icon-save","action":"billomoutstock.check()","type":2},
	                             {"title":"取消","iconCls":"icon-cancel","action":"billomoutstock.closeWindow('showDialog')","type":0}
		                       ]
					  />
		    		<div class="search-div">
		         		<form name="detailForm" id="detailForm" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：</td>
		                 			<td>
		                 				<input class="easyui-validatebox ipt" style="width:120px" name="outstockNo" id="outstockNo"/>
		                 				<input type="hidden" name="outstockNo" id="outstockNoHide"/>
										<input type="hidden" name="locno" id="locnoHide">
		                 			</td>
		                 			<td class="common-td blank">单据状态：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="status" id="status"/></td>
									<td style="padding-left:10px;display:none;"><input class="easyui-combobox" style="width:120px" id="outstockNameTemp" /></td>
		                 		</tr>
		                    </table>
		         		</form>
			         </div>
				</div>
				<div data-options="region:'center',border:false">
					
					<div class="easyui-layout" data-options="fit:true" id="dataGridJG_Detail_div2">
						<div data-options="region:'center',border:false">
							<@p.datagrid id="dataGridJG_Detail2"  loadUrl="" saveUrl=""   defaultColumn=""
						      isHasToolBar="false" onClickRowEdit="true"    pagination="true"
						       rownumbers="true"  singleSelect = "false"  height="300"  singleSelect="false"
						       columnsJsonList="[
						       		  {field : 'sCellNo',title : '来源储位',width :80,align:'left'},
						       		  {field : 'storeName',title :'店铺名称',width : 150,align:'left'},
						       		  {field : 'itemNo',title :'商品编码',width : 140,align:'left'},
						              {field : 'itemName',title :'商品名称',width : 150,align:'left'},
						              {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
						              {field : 'colorName',title : '颜色',width : 80,align:'left'},
									  {field : 'itemQty',title : '计划数量',width : 80,align:'right'},
									  {field:'realQty',title:'实际数量',width:80,align:'right',
													    	editor:{
													    		type:'numberbox'
													    	}
													    },
							          {field : 'outstockedQty',title : 'RF数量',width : 80,align:'right'},
									  {field:'outstockName',title:'实际拣货人',width:100,formatter:billomoutstock.outstockNameFormatter,align:'left',
								  		editor:{
									 		type:'combobox',
									 		options:{
									 			url:'${BasePath}/authority_user/user.json',
							                   	valueField:'workerNo',
							                    textField:'unionName',
							                    panelHeight:150
									 		}
									 	}
								       }
								      
						             ]" 
						           />
						</div>
					</div>
				</div>
			</div>
	</div>
	
	<#-- 详情 -->
<div id="detailDialogView" class="easyui-window" title="拣货单明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="detailbar"  listData=[
      		 	{"id":"detail-export","title":"导出","iconCls":"icon-export","action":"billomoutstock.exportDetail()","type":5},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billomoutstock.closeWindow('detailDialogView')","type":0}
      		]/>
	    	<form id='exportForm' method="post" action="../bill_om_outstock_dtl/do_dtlexport">
				<input type="hidden" name="fileName" value="拣货单明细报表"/>
				<input type="hidden" name="exportColumns" id="exportColumnsCondition_export"/>
				<input type="hidden" name="locno" id="locnoCondition_export"/>
				<input type="hidden" name="outstockNo" id="outstockNo_export"/>
			</form>
	    	<form id="detailForm_view" name="detailForm_view" method="post" class="city-form">
	    		<table>
	    			<tr>
		            	<td class="common-td">单据编号：</td>
		            	<td>
		                	<input class="easyui-validatebox ipt" style="width:120px" name="outstockNo" id="outstockNoView"/>
							
							<input type="hidden" name="locno" id="locnoHide">
		             	</td>
		             	<td class="common-td blank">单据状态：</td>
		             	<td><input class="easyui-validatebox ipt" style="width:120px" name="status" id="statusView"/></td>
						<td style="padding-left:10px;display:none;"><input class="easyui-combobox" style="width:120px" id="outstockNameTemp" /></td>
		      		</tr>
		    	</table>
	    	</form>
		</div>
		<div data-options="region:'center'">
			<div id="main_imimport_DetailId" class="easyui-tabs" data-options="fit:true,border:false">   
				   <div id = 'detailViewDiv' title='尺码横排' > 
						 <@p.datagrid id="moduleView" name="" loadUrl="" saveUrl="" defaultColumn=""  
						 		isHasToolBar="false"  columnsJsonList=""  divToolbar="#"  onClickRowEdit="false" singleSelect="true" pageSize='20'   
								pagination="true" rownumbers="true"  jsonExtend="{}" height="500" showFooter="true"  emptyMsg=""
						 /> 
					</div>
					<div id = 'detailViewDiv_dtl' title='明细' >
						 <@p.datagrid id="dataGridJG_Detail"  loadUrl="" saveUrl=""   defaultColumn=""
						      isHasToolBar="false" onClickRowEdit="false"    pagination="true"
						       rownumbers="true"  singleSelect = "false"  height="445" 
						       onClickRowEdit="true" singleSelect="false" showFooter="true"
						       columnsJsonList="[
						       		  {field : 'sCellNo',title : '来源储位',width :80,align:'left'},
						       		  {field : 'storeName',title :'店铺名称',width : 150,align:'left'},
						       		  {field : 'itemNo',title :'商品编码',width : 140,align:'left'},
						              {field : 'itemName',title :'商品名称',width : 150,align:'left'},
						              {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
						              {field : 'colorName',title : '颜色',width : 80,align:'left'},
						              {field : 'scanLabelNo',title : '箱号',width : 100,align:'left'},
									  {field : 'itemQty',title : '计划数量',width : 80,align:'right'},
									  {field:'realQty',title:'实际数量',width:80,align:'right'},
									  {field : 'outstockedQty',title : 'RF数量',width : 80,align:'right'},
									  {field:'diffQty',title:'差异数量',width:80,align:'right'},
									  {field:'outstockName',title:'实际拣货人',width:100,align:'left'},
									  {field:'outstockNameCh',title:'实际拣货人名称',width:100,align:'left'}
						             ]" 
						           />
					</div>
			</div>
		</div>
	</div>
</div>
	
	<#-- 复核人选择-->
	<div id="check_user_dialog" class="easyui-window" title="指定拣货人选择" 
		style="width:300px;height:140px;padding:5px;"   
		data-options="modal:true,resizable:false,draggable:true,
		collapsible:false,closed:true,minimizable:false,maximizable:false">  
			
			<div style="padding-top:10px;">
			<table width="100%" >
				<tr>
					<td class="common-td">指定拣货人：</td>
					<td><input class="easyui-combobox" style="width:130px" id="assignName" name="assignName" /></td>
				</tr>
				<tr>
					<td colspan='2' align='center' height="60">
						<a href="javascript:billomoutstock.sendOrder();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a>
						&nbsp;
						<a href="javascript:billomoutstock.closeWindow('check_user_dialog');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
					</td>
				</tr>
			</table>
			</div>
	</div> 
	<#-- 复核人选择end-->
	
</body>
</html>