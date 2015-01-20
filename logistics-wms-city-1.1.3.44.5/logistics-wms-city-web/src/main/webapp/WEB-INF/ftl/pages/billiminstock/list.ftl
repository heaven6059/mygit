<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>上架回单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billiminstock/billiminstock.js?version=1.1.1.2"></script>    
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body  class="easyui-layout">
	<#-- 工具菜单div  -->
	<div data-options="region:'north',border:false">
       <@p.toolbar id="main-toolbar"  listData=[
                 {"title":"查询","iconCls":"icon-search","action":"billiminstock.searchArea()","type":0},
    			 {"title":"清空","iconCls":"icon-remove","action":"billiminstock.searchLocClear()","type":0},
    			 {"title":"修改","iconCls":"icon-edit","action":"billiminstock.edit();","type":2},
		    	 {"title":"审核","iconCls":"icon-aduit","action":"billiminstock.check();","type":4},
		    	 {"id":"printDetail","title":"打印预览","iconCls":"icon-print","action":"billiminstock.printDetail()","type":5},
		    	 {"title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
	       ]
		 />
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
			   				<tr>
			   					<td class="common-td blank">状态：</td>
			   					<td><input class="easyui-combobox" style="width:120px" name="status" data-options="editable:false" id="search_status"/></td>
			   					<td class="common-td blank">上架人：</td>
			   					<td><input class="easyui-combobox" style="width:120px" name="instockWorker" data-options="editable:false" id="search_instockWorker"/></td>
			   					<td class="common-td blank">上架日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="instockDate" id="instockDate_start"/></td>
			   					<td class="common-line">&nbsp;&mdash;&nbsp;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="instockDate_end"  id="instockDate_end"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td blank">单据编号：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="instockNo"/></td>
			   					<td class="common-td blank">审核人：</td>
			   					<td><input class="easyui-combobox" style="width:120px" name="auditor" data-options="editable:false" id="search_auditor"/></td>
			   					<td class="common-td blank">审核日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="audittm" id="audittm_start"/></td>
			   					<td class="common-line">&nbsp;&mdash;&nbsp;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="audittm_end" id="audittm_end"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td blank">货主：</td>
			   					<td><input class="easyui-combobox" style="width:120px" data-options="editable:false" name="ownerNo" id="search_ownerNo"/></td>
			   					<td class="common-td blank">创建人：</td>
                         		<td><input class="easyui-combobox" style="width:120px" data-options="editable:false" name="creator" id="search_creator"/></td>
			   					<td class="common-td blank">创建日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="createtm" id="createtm_start"/></td>
			   					<td class="common-line">&nbsp;&mdash;&nbsp;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="createtm_end" id="createtm_end"/></td>
                         	</tr>
                         	<tr>
			   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
	                 			<td class="common-td blank">所属品牌：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
								<td class="common-td blank">来源单号：</td>
			   					<td colspan="3"><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo"/></td>
			   				</tr>
			   		</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title="上架单"
		              isHasToolBar="false" divToolbar="#searchDiv" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "false" height="530"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			           		  {field : 'status',hidden : true},
			           		  {field : 'statusStr',title : '状态',align:'left',width : 80},
			                  {field : 'instockNo',title : '单据编号',width : 180},
			                  {field : 'ownerName',title : '货主',align:'left',width : 100},
			                  {field : 'instockWorker',title : '上架人',align:'left',width : 120},
			                  {field : 'instockName',title : '上架人名称',align:'left',width : 120},
			                  {field : 'instockDate',title : '上架时间',width : 100},
			                  {field : 'creator',title : '创建人',align:'left',width : 120},
			                  {field : 'creatorName',title : '创建人名称',align:'left',width : 120},
			                  {field : 'createtm',title : '创建时间',width : 150},
			                  {field : 'auditor',title : '审核人',align:'left',width : 120},
			                  {field : 'auditorName',title : '审核人名称',align:'left',width : 120},
			                  {field : 'audittm',title : '审核时间',width : 150},
			                  {field : 'editor',hidden:'true'},
			                  {field : 'edittm',hidden:'true'}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                     // billiminstock.selectedCheckBox(rowIndex);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	//双击方法
		                   	   billiminstock.showDetail(rowData,rowIndex)
		                   }}'/>
			</div>
		</div>		
	</div>
<div id="showDialog"  class="easyui-window" title="详情"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
    <div class="easyui-layout" data-options="fit:true">
    	<div data-options="region:'north',border:false">
    		 <@p.toolbar id="show-toolbar"  listData=[
    			 {"title":"导出","iconCls":"icon-export","action":"billiminstock.exportDetail()","type":2}
	      		 ]
			 />
    		<form name="dataForm" id="dataForm" method="post" class="city-form" style="padding-bottom:8px;">
     		<#-- 明细信息div -->
				<table>
					<tr>
						<td class="common-td blank">单据编号：</td>
						<td><input class="easyui-validatebox ipt" style="width:150px" name="instockNo"/></td>
						<td class="common-td blank">货主：</td>
						<td><input class="easyui-validatebox ipt" data-options="editable:false"  style="width:120px" name="ownerName"/></td>
						<td class="common-td blank">上架人：</td>
						<td><input class="easyui-validatebox ipt" style="width:100px" name="instockWorker"/></td>
					</tr>
				</table>
	 		</form>
    	</div>
    	<div data-options="region:'center',border:false">
    		<@p.datagrid id="dataGridJG_detail_default" name="" loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  height="500"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
							pagination="true" rownumbers="true" showFooter="true" title="明细列表" showFooter="true"
				 			columnsJsonList="[
				 			{field:'sourceNo',title:'来源单号',align:'left',width:150,sortable:true},
			 				{field:'itemNo',title:'商品编码',align:'left',width:150,sortable:true},
			 				{field:'itemName',title:'商品名称',align:'left',width:150,sortable:true},
			 				{field:'showItemType',title:'商品属性',align:'left',width:80},
			 				{field:'showQuality',title:'商品品质',align:'left',width:80},
						    {field:'colorName',title:'颜色',align:'left',width:60,sortable:true},
						    {field:'sizeNo',title:'尺码',align:'left',width:60,sortable:true},
						    {field:'destCellNo',title:'预上储位',align:'left',width:80,sortable:true},
						    {field:'itemQty',title:'计划数量',align:'right',width:60,sortable:true},
			 				{field:'realCellNo',title:'实际上架储位',align:'left',width:100,sortable:true},
			 				{field:'realQty',title:'实际上架数量',width:100,align:'right',sortable:true},
			 				{field:'editor',title:'实际上架人',width:100,align:'left',sortable:true},
			 				{field:'editorName',title:'实际上架人名称',width:100,align:'left',sortable:true},
			 				{field:'instockedQty',title:'RF上架数量',width:100,align:'left',sortable:true},
			 				{field:'labelNo',title:'箱号',width:100,align:'center',sortable:true}
			 				]"
				 		/>
    	</div>
	</div>
</div>	
		
<div id="showEditDialog"  class="easyui-window" title="修改"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			  <@p.toolbar id="edit-toolbar"  listData=[
    			 {"title":"按计划保存","iconCls":"icon-save-dtl","action":"billiminstock.saveByPlan()","type":2},
    			 {"title":"单行保存","iconCls":"icon-save","action":"billiminstock.d_save()","type":2},
    			 {"title":"拆分","iconCls":"icon-cut","action":"billiminstock.d_manage('split')","type":2},
    			 {"title":"删除","iconCls":"icon-del","action":"billiminstock.d_manage('del')","type":3}
	      		 ]
			 />
			 <form name="dataForm" id="dataFormEdit" method="post"  class="city-form">
	         	<#-- 明细信息div -->
				<table>
					<tr>
						<td class="common-td blank">单据编号：</td>
						<td><input class="easyui-validatebox ipt" style="width:150px" name="instockNo" id="detailInstockNo"/></td>
						<td class="common-td blank">货主：</td>
						<td><input class="easyui-validatebox ipt" data-options="editable:false"  style="width:120px" name="ownerName"/>
							<input  type="hidden" name="ownerNo" id="detailOwnerNo"/>
						</td>
						<td class="common-td blank">上架人：</td>
						<td><input class="easyui-validatebox ipt" style="width:100px" name="instockWorker"/></td>
					</tr>
				</table>
			 </form>
		</div>
		<div data-options="region:'center',border:false">
			<@p.datagrid id="dataGridJG_edit" name="" loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  height="500"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
							pagination="true" rownumbers="true" showFooter="true" title="明细列表" showFooter="true"
				 			columnsJsonList="[
			          		{field:'sourceNo',title:'来源单号',align:'left',width:150,sortable:true},
			 				{field:'itemNo',title:'商品编码',align:'left',width:150,sortable:true},
			 				{field:'itemName',title:'商品名称',align:'left',width:150,sortable:true},
			 				{field:'showItemType',title:'商品属性',align:'left',width:80},
			 				{field:'showQuality',title:'商品品质',align:'left',width:80},
						    {field:'colorName',title:'颜色',align:'left',width:60,sortable:true},
						    {field:'sizeNo',title:'尺码',align:'left',width:60,sortable:true},
						    {field:'destCellNo',title:'预上储位',align:'left',width:80,sortable:true},
						    {field:'itemQty',title:'计划数量',align:'right',width:60,sortable:true},
			 				{field:'realCellNo',title:'实际上架储位',align:'left',width:100,
			 					editor:{
			 						type:'validatebox'
			 						},sortable:true
			 				},
			 				{field:'realQty',title:'实际上架数量',width:100,align:'right',
						    	editor:{
						    		type:'numberbox'
						    	},sortable:true
						    },
						    {field:'instockedQty',title:'RF上架数量',width:100,align:'right',sortable:true},
			 				{field:'labelNo',title:'箱号',width:100,align:'center',sortable:true},
			 				{field:'instockNo',title:'instockNo',width:60,hidden:'true',sortable:true},
			 				{field:'locno',title:'locno',width:60,hidden:'true',sortable:true},
			 				{field:'ownerNo',title:'ownerNo',width:60,hidden:'true',sortable:true},
			 				{field:'instockId',title:'instockId',width:60,hidden:'true',sortable:true}
			 			]"
				/>
		</div>
	</div>	    
</div>
<div id="cellNoDialog"  class="easyui-window" title="实际储位"  
		    data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    maximized:false,minimizable:false,maximizable:false" style="width:300px">
	<form id="cellNoForm" method="post" class="city-form">
		<table width="90%">
			<tr>
				<td class="common-td blank" width="30%">实际上架储位:</td>
				<td width="60%"><input type="text" class="easyui-validatebox ipt" style="width:100%" name="realCellNo" id="selectRealCellNo" data-options="validType:['vLength[0,24,\'最多只能输入24个字符\']']" required="true"></td>
			</tr>
			<tr>
				<td class="common-td blank" width="30%">实际上架数量:</td>
				<td width="60%"><input type="text" class="easyui-numberbox ipt" style="width:100%" name="realQty" id="selectRealQty" data-options="validType:['vLength[0,18,\'最多只能输入18个字符\']']" required="true"></td>
			</tr>
			<tr>
				<td colspan="2" class="common-td blank" style="text-align:center"><a href="javascript:billiminstock.d_split_do()" data-options="'iconCls':'icon-ok'" class="easyui-linkbutton">确定</a></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>