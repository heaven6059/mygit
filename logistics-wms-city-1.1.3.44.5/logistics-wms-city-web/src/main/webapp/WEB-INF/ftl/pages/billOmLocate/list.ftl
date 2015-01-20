<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>拣货波次</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billOmLocate/billOmLocate.js?version=1.1.1.2"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billOmLocate.searchData()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billOmLocate.searchClear('searchForm')", "type":0},
	       <#-- {"title":"打印预览","iconCls":"icon-print","action":"billOmLocate.printDetail()","type":4},-->
	        {"title":"尺码横排打印预览","iconCls":"icon-print","action":"billOmLocate.printDetail4SizeHorizontal()","type":4},
	        {"title":"汇总打印预览","iconCls":"icon-print","action":"billOmLocate.printDetailSummary()","type":4},
	        {"title":"手工关闭","iconCls":"icon-cancel","action":"billOmLocate.overFloc()","type":5},
	        {"title":"删除","iconCls":"icon-del","action":"billOmLocate.deleteOmLocate()","type":3},
	        {"title":"发单还原","iconCls":"icon-redo","action":"billOmLocate.recoveryLocateSend()","type":5},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
		 ]/>
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td">单据状态：</td>
								<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
								<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="startCreatetmStr" id="startCreatetmCondition" /></td>
								<td class="common-line">&mdash;</td>
		                     	<td><input class="easyui-datebox" style="width:120px" name="endCreatetmStr" id="endCreatetmCondition" /></td>
							</tr>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo" id="locateNoCondition" /></td>
								<td class="common-td blank">发货通知单号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoCondition" /></td>
				                <td class="common-td blank">合&nbsp;同&nbsp;号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoCondition" /></td>
				                <td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
							</tr>
							<tr>
								<td class="common-td blank">所属品牌：</td>
				                <td colspan='7'><input class="easyui-combobox ipt" style="width:334px" name="brandNo" id="brandNo" /></td>
							</tr>
						</table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="波次列表"
		    	isHasToolBar="false" divToolbar="" height="460"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			    rownumbers="true" emptyMsg="" showFooter="true"
			    columnsJsonList="[
			    		{field : 'id',checkbox:true},
			        	{field : 'locno',hidden:true},
			        	{field : 'status',hidden:true},
			        	{field : 'statusStr',title : '单据状态 ',width : 80,align:'left'},
			        	{field : 'locateNo',title : '单据编号',width : 180},
			        	{field : 'locateName',title : '单据名称',width : 120,hidden:true},
			        	{field : 'totalItemQty',title : '总品项数',width : 80,align:'right'},
			        	{field : 'totalPlanQty',title : '总数量',width : 80,align:'right'},
			        	{field : 'totalVolumeQty',title : '总体积',width : 80,align:'right'},
			        	{field : 'totalWeightQty',title : '总重量',width : 80,align:'right'},
			        	{field : 'creator',title : '创建人',width : 90,align:'left'},
			        	{field : 'creatorname',title : '创建人名称',width : 90,align:'left'},
			            {field : 'createtm',title : '创建时间',width : 140},
			            {field : 'editor',title : '更新人',width : 100,sortable:true},
						{field : 'editorname',title : '更新人名称',width : 100,sortable:true},
						{field : 'edittm',title : '更新时间',width : 130,sortable:true}
			    ]" 
				jsonExtend='{onLoadSuccess:function(data){
						billOmLocate.onLoadSuccess(data);
					},onDblClickRow:function(rowIndex, rowData){
		    		// 触发点击方法  调JS方法
		        	billOmLocate.loadDetail(rowData);
				}}'/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	
	<#-- 查看明细信息div BEGIN -->
	<div id="openWindowLocate"  class="easyui-dialog" title="调度波次明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="viewtoolbar"   listData=[
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billOmLocate.closeWindow('openWindowLocate');","type":0},
		        	{"title":"导出","iconCls":"icon-export","action":"billOmLocate.do_exportLocateDtl();","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="dataForm" id="dataForm" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo" id="locateNo" readOnly="readOnly" /></td>
		                 		</tr>
		                    </table>
		         		</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				
				
				<div class="easyui-layout" data-options="fit:true">
			    	<div data-options="region:'center',border:false">
				    	<div id="main_order_dtlId" class="easyui-tabs" fit="true">
				    		
				    		<#-- tab1 S-->
				    		<div id="tab_order_dtl1" title="波次明细" height='100%'>
			    				<div class="easyui-layout" data-options="fit:true">
			    					<div  data-options="region:'center',border:false" >
			    						<@p.datagrid id="locateDtlDg" name="" title="波次明细"  loadUrl="" saveUrl="" defaultColumn="" 
											isHasToolBar="false"  divToolbar="#toolsDiv"  height="350"  
											onClickRowEdit="false" singleSelect="true" showFooter="true"
											pagination="true" rownumbers="true" emptyMsg=""
										 	columnsJsonList="[
										 			{field:'storeNo',title:'客户编号',width:110,align:'left'},
										 			{field:'storeName',title:'客户名称',width:180,align:'left'},
										 			{field:'expNo',title:'发货通知单号',width:140},
										 			{field:'itemNo',title:'商品编码',width:135,align:'left'},
										 			{field:'itemName',title:'商品名称',width:160,align:'left'},
										 			{field:'colorName',title:'颜色',width:80,align:'left'},
										 			{field:'sizeNo',title:'尺码',width:80,align:'left'},
										 			{field:'planQty',title:'计划数量',width:80,align:'right'},
										 			{field:'locatedQty',title:'已分配数量',width:80,align:'right'},
										 			{field:'outstockQty',title : '拣货数量',width : 80,align:'right'},
													{field:'recheckQty',title : '复核数量',width : 80,align:'right'}
										 	]"
										 jsonExtend='{}'/>
			    					</div>
			    				</div>
			    			</div>
				    		<#-- tab1 E-->
				    		
				    		
				    		<#-- tab2 S-->
			    			<div id="tab_order_dtl2" title="定位明细" height='100%'>
			    				<div class="easyui-layout" data-options="fit:true">
			    					<div  data-options="region:'center',border:false" >
			    						<@p.datagrid id="locateOutstockDtlDg" name="" title="定位明细"  loadUrl="" saveUrl="" defaultColumn="" 
											isHasToolBar="false"  divToolbar="#toolsDiv"  height="350"  
											onClickRowEdit="false" singleSelect="true" showFooter="true"
											pagination="true" rownumbers="true" emptyMsg=""
										 	columnsJsonList="[
										 			{field:'storeNo',title:'客户编号',width:110,align:'left'},
										 			{field:'storeName',title:'客户名称',width:160,align:'left'},
										 			{field:'expNo',title:'发货通知单号',width:140},
										 			{field:'itemNo',title:'商品编码',width:135,align:'left'},
										 			{field:'itemName',title:'商品名称',width:160,align:'left'},
										 			{field:'colorName',title:'颜色',width:80,align:'left'},
										 			{field:'sizeNo',title:'尺码',width:80,align:'left'},
										 			{field:'cellNo',title:'储位',width:100,align:'left'},
										 			{field:'planQty',title:'计划数量',width:80,align:'right'},
										 			{field:'locatedQty',title:'已分配数量',width:80,align:'right'},
										        	{field : 'outstockQty',title : '已拣货数量',width : 80,align:'right'},
										        	{field : 'recheckQty',title : '已复核数量',width : 80,align:'right'},
										 	]"
										 jsonExtend='{}'/>
			    					</div>
			    				</div>
			    			</div>
			    			
				    		
				    	</div>
				    </div>
			    </div>
			    	
				
			
				
				 
				 
				 
			</div>
			<#--显示列表end-->	
			</div>
		</div>
		<#-- 查看明细信息div END -->
</body>
</html>