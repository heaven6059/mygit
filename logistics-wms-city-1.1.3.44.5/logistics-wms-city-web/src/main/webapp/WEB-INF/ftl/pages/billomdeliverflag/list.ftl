<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>派车计划出库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billomdeliverFlag/billomdeliverFlag.js?version=1.0.6.2"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar1"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"billomdeliverFlag.searchData();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billomdeliverFlag.clearSearchCondition();", "type":0},
	        {"title":"新增","iconCls":"icon-add","action":"billomdeliverFlag.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"billomdeliverFlag.editInfo();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"billomdeliverFlag.deleteRows();","type":3},
	        {"title":"审核","iconCls":"icon-aduit","action":"billomdeliverFlag.check();","type":4},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="status" id="searchStatus"/></td>
				                <td class="common-td blank">发&nbsp;货&nbsp;人：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="sendName" id="searchSendName" /></td>
				                <td class="common-td blank">发货日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="startExpDate"  id="startExpDate"/></td>
				                <td class="common-line" width='40'>&mdash;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="endExpDate"  id="endExpDate"
								data-options="validType:['vCheckDateRange[\'#startExpDate\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
                            <tr>
				            	<td class="common-td">单据编号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" /></td>
				                <td class="common-td blank">创&nbsp;建&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="creator"/></td>
				                <td class="common-td blank">创建日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="startCreatetm" id="startCreatetm" /></td>
				                <td class="common-line">&mdash;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="endCreatetm" id="endCreatetm"
								data-options="validType:['vCheckDateRange[\'#startCreatetm\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
                            <tr>
				            	<td class="common-td">车&nbsp;牌&nbsp;号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="carPlate"/></td>
				                <td class="common-td blank">审&nbsp;核&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" /></td>
				                <td class="common-td blank">审核日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="startAudittm"  id="startAudittm"/></td>
				                <td class="common-line">&mdash;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="endAudittm" id="endAudittm"
								data-options="validType:['vCheckDateRange[\'#startAudittm\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
		             			<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
		             			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='3'><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
		             		</tr>
                    	</table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl=""
	    		 saveUrl=""  defaultColumn=""  title="派车计划列表" emptyMsg=""
	          	 isHasToolBar="false" divToolbar="" height="430"  onClickRowEdit="false"  pagination="true" singleSelect = "false" rownumbers="true"
				 columnsJsonList="[
				 	{field : 'id',checkbox:true},
				  	{field : 'status',title : '状态',width : 70, formatter:billomdeliverFlag.columnStatusFormatter,align:'left'},
				 	{field : 'deliverNo',title : '单据编号',width : 180},
				 	{field : 'ownerNo',title : '货主',width : 100,formatter:billomdeliverFlag.ownerNoFormatter,align:'left'},
				  	{field : 'sendName',title : '发货人',width : 80,formatter:billomdeliverFlag.columnSendNoFormatter,align:'left'},
				  	{field : 'expDate',title : '发货日期 ',width : 120},
				   	{field : 'carPlate',title : '车牌号',width : 80,align:'left'},
				  	{field : 'creator',title : '创建人',width : 80,align:'left'},
				  	{field : 'creatorname',title : '创建人名称',width : 80,align:'left'},
				  	{field : 'createtm',title : '创建日期',width : 130},
				  	{field : 'auditor',title : '审核人',width : 80,align:'left'},
				  	{field : 'auditorname',title : '审核人名称',width : 80,align:'left'},
				  	{field : 'audittm',title : '审核日期',width : 130}
				 ]" 
				 jsonExtend='{onDblClickRow:function(rowIndex, rowData){
			        	// 触发点击方法  调JS方法
			        	billomdeliverFlag.dtlView(rowData,"view");
			     }}'
			   	/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 查看明细信息div BEGIN -->
	<div id="openDtlUI"  class="easyui-dialog" title="明细"
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="viewtoolbar"   listData=[
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billomdeliverFlag.closeDtlUI('openDtlUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="deliverDetailForm" id="deliverDetailForm" metdod="post" class="city-form">
						<table>
							<tr>
						    	<td class="common-td blank">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="deliverDtlNo" id="deliverDtlNo" readOnly="readOnly"/></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="ownerDtlNo" id="ownerDtlNo" /></td>
						        <td class="common-td blank">发货日期：</td>
						        <td><input class="easyui-datebox ipt" style="width:120px" name="expDtlDate" id="expDtlDate"  /></td>
						        <td class="common-td blank">发&nbsp;货&nbsp;人：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="sendDtlName" id="sendDtlName" /></td>
						    </tr>
                            <tr>
						    	<td class="common-td blank">车&nbsp;牌&nbsp;号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="carDtlPlate" id="carDtlPlate"  /></td>
						        <td class="common-td blank">出货码头：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="dockDtlNo" id="dockDtlNo"/></td>
						        <td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						        <td colspan="3"><input class="easyui-validatebox ipt" style="width:306px" name="remarksDtl" id="remarksDtl" /></td>
						    </tr>
						 </table>
					</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<@p.datagrid id="deliverDetail" name=""  loadUrl="" saveUrl="" defaultColumn="" title="派车单明细"
				 	 isHasToolBar="false"  divToolbar="" onClickRowEdit="false" singleSelect="true"   
					 pagination="true" rownumbers="true" emptyMsg=""  showFooter="true"
				 	 columnsJsonList="[
				 		{field : 'boxNo',title:'箱号',width:100,align:'left'},
				 		{field : 'circle',title : '卸货点',width : 100,align:'left'},
						{field : 'storeNo',title : '客户编码',width : 100,align:'left'},
						{field : 'storeName',title : '客户',width : 160,align:'left'},
						{field : 'itemNo',title : '商品编码',width : 135,align:'left'},
						{field : 'itemName',title : '商品名称',width : 160,align:'left'},
						{field : 'sizeNo',title : '尺码',width : 60,align:'left'},
						{field : 'qty',title:'数量',width:60,align:'right'}
				 	 ]"
				 	/>
			</div>
			<#--显示列表end-->	
		</div>
	</div>
	<#-- 查看明细信息div END -->

	<#-- 新增修改页面 BEGIN -->
	<div id="openUI"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="addtoolbar"   listData=[
		        	{"id":"save_main_info","title":"保存","iconCls":"icon-save","action":"billomdeliverFlag.saveMainInfo();", "type":1},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billomdeliverFlag.closeUI('openUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="dataForm" id="dataForm" metdod="post" class="city-form">
						<input type="hidden" id="opt"/>
						<table>
							<tr>
						    	<td class="common-td blank">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNo" readOnly="readOnly"/></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" data-options="required:true" /></td>
						        <td class="common-td blank">发货日期：</td>
						        <td><input class="easyui-datebox ipt" style="width:120px" name="expDate" id="expDate" data-options="required:true" /></td>
						        <td class="common-td blank">发&nbsp;货&nbsp;人：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="sendName" id="sendName" /></td>
						    </tr>
                            <tr>
						    	<td class="common-td blank">车&nbsp;牌&nbsp;号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="carPlate" id="carPlate" data-options="required:true" /></td>
						        <td class="common-td blank">出货码头：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="dockNo" id="dockNo"/></td>
						        <td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						        <td colspan="3"><input class="easyui-validatebox ipt" style="width:306px" name="remarks" id="remarks" /></td>
						    </tr>
						 </table>
					</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false" id = 'detailDiv'>
				<@p.toolbar id="zoneInfoToolDiv" listData=[
		        	{"id":"add_row","title":"新增明细","iconCls":"icon-add-dtl","action":"billomdeliverFlag.addrow('itemDetail');", "type":0},
		        	{"id":"del_row","title":"删除明细","iconCls":"icon-del-dtl","action":"billomdeliverFlag.removeBySelected('itemDetail');", "type":0},
		       		{"id":"save_row","title":"保存明细","iconCls":"icon-save-dtl","action":"billomdeliverFlag.saveDtlInfo('itemDetail');","type":0}
		        ]/>
				<@p.datagrid id="itemDetail" name="" title="派车单明细" loadUrl="" saveUrl="" defaultColumn="" 
					 isHasToolBar="false"  divToolbar="#zoneInfoToolDiv" onClickRowEdit="true" singleSelect="true"   
					 pagination="true" rownumbers="true" emptyMsg="" 
					 columnsJsonList="[
						{field : 'loadproposeNo',title : '派车单号 ',width : 180},
					 	{field : 'containerNo',title:'容器号',width:100,hidden:true,align:'left'},
					 	{field : 'boxNo',title:'箱号',width:100,align:'left'},
						{field : 'storeNo',title : '客户编码',width : 100,align:'left'},
						{field : 'storeName',title : '客户',width : 160,align:'left'},
						{field : 'qty',title:'总数量',width:60,align:'right'}
					]"
				/>
			</div>
			<#--显示列表end-->
		</div>
	</div>
	<#-- 新增修改页面 END -->
	
	<#-- 商品选择div -->
	<div id="openUIItem"  class="easyui-dialog" title="商品选择"  
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
		<div data-options="region:'north',border:false" >
			<@p.toolbar id="itemtoolbar"   listData=[
				{"title":"查询","iconCls":"icon-search","action":"billomdeliverFlag.searchItem();", "type":0},
	       		{"title":"清除","iconCls":"icon-remove","action":"billomdeliverFlag.clearSearch();", "type":0},
	        	{"title":"确定","iconCls":"icon-ok","action":"billomdeliverFlag.confirmItem();", "type":0},
				{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billomdeliverFlag.closeOpenUIItem();","type":0}
			]/>
			<div nowrap class="search-div" style="padding:10px;">
				<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form">
					<input class="easyui-validatebox" style="width:120px" name="showGirdName" id="showGirdName" type ="hidden" />
					<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoForItem" type ="hidden" />
					<table>
						<tr>
							<td class="common-td blank">卸&nbsp;货&nbsp;点：</td>
							<td><input class="easyui-combobox" style="width:120px" name="circleNo" id="circleNo"/></td>
							<td class="common-td blank">客户名称：</td>
							<td><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNo"/></td>
							<td class="common-td blank">复核单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNo"/></td>
						 	<td class="common-td blank">发货通知单：</td>
						 	<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNo"/></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<#--查询end-->
		<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" title="派车单列表"
					     isHasToolBar="false" divToolbar="#itemSearchDiv" onClickRowEdit="false"  singleSelect = "false"
					     pagination="true" rownumbers="true" emptyMsg="" 
						 columnsJsonList="[
						 	{field : 'ck',title : '',width : 50, checkbox:true},
						 	{field : 'loadproposeNo',title : '派车单号 ',width : 180},
						 	{field : 'tmsDeliverNo',title : 'TMS配送单号 ',width : 180},
							{field : 'containerNo',title : '容器号 ',width : 80,hidden:true},
						 	{field : 'boxNo',title : '箱号 ',width : 100,align:'left'},
							{field : 'storeNo',title : '客户编码',width : 100,align:'left'},
						  	{field : 'storeName',title : '客户',width : 160,align:'left'},
						  	{field : 'qty',title : '总数量',width : 60,align:'right'}
						 ]" 
					/>
				</div>
		<#--显示列表end-->
		</div>			
	</div>
</body>
</html>