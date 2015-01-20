<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>店退仓</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billumuntread/billumuntread.js?version=1.0.8.4"></script>   
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object> 
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
     <div data-options="region:'north',border:false">
       <@p.toolbar id="maintoolbar"  listData=[
      		 {"title":"查询","iconCls":"icon-search","action":"billumuntread.searchArea()","type":0},
      		 {"title":"清空","iconCls":"icon-remove","action":"billumuntread.searchLocClear()","type":0},
             {"title":"新增","iconCls":"icon-add","action":"billumuntread.addInfo()","type":1},
             {"title":"修改","iconCls":"icon-edit","action":"billumuntread.editInfo()","type":2},
             {"title":"删除","iconCls":"icon-del","action":"billumuntread.del()","type":3},
             {"title":"打印预览","iconCls":"icon-print","action":"billumuntread.printBatch()","type":0},
             {"title":"审核","iconCls":"icon-aduit","action":"billumuntread.audit()","type":4},
             {"title":"作废","iconCls":"icon-cancel","action":"billumuntread.invalid()","type":4},
	         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('退仓通知单')","type":0}
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
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
		   					<td class="common-td blank">创建人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_creator" name="creator"/></td>
		   					<td class="common-td blank">创建日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">货主：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo" /></td>
		   					
		   					<td class="common-td blank">审核人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">店退仓单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo"/></td>
		   					<td class="common-td blank">店退仓通知单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo"/></td>
		   					<td class="common-td blank">来源单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo"/></td>
		   					<td class="common-td blank">客户名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="storeName" id="search_storeName" /></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank" >退仓类型：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" name="untreadType" id="untreadType_search"  style="width:120px"/></td>
		   					<td class="common-td blank">品质：</td>
							<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>						
		   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
	             			<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
		   				</tr>
		   			</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" defaultColumn="" title="店退仓单"
	              isHasToolBar="false"  onClickRowEdit="false" emptyMsg="" 
	               pagination="true" rownumbers="true"  singleSelect = "false" showFooter="true"
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'status',title : '状态',width : 70,formatter:billumuntread.statusFormatter,align:'left'},
						{field : 'untreadNo',title : '店退仓单号',width :180},
						{field : 'poNo',title : '来源单号',width :180},
						{field : 'itemQty',title:'退仓数量',width:70,align:'left',align:'right'},
						{field : 'receiptQty',title : '收货数量',width :80,align:'right'},
						{field : 'checkQty',title : '验收数量',width :80,align:'right'},
						{field : 'ownerNo',title : '货主',width : 120,align:'left',formatter:billumuntread.ownerFormatter},
						{field : 'quality',title : '品质',width : 100,formatter:billumuntread.qualityFormatter,align:'left'},
						{field : 'storeName',title : '客户名称',width : 120,align:'left'},
						{field : 'untreadType',title : '退仓类型',width : 100,formatter:billumuntread.typeFormatter,align:'left'},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 80,align:'left'},
			            {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			            {field : 'audittm',title : '审核时间',width : 140},
						{field : 'remark',title : '备注',width : 130,sortable:true}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                            // 触发点击方法  调JS方法
	                   },onDblClickRow:function(rowIndex, rowData){
	                   	    billumuntread.showDetail(rowData);
	                   }}'/>
			</div>
		</div>
	</div>
<div id="detailDialog" class="easyui-window" title="新增/修改"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="submaintoolbar"  listData=[
      		 	{"id":"addBtn","title":"保存","iconCls":"icon-save","action":"billumuntread.save_main()","type":0},
      		 	{"id":"editBtn","title":"修改","iconCls":"icon-edit","action":"billumuntread.edit_main()","type":0},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billumuntread.coloseDetailDialog()","type":0}
      		 	]
		  	/>
			<form id="detailForm" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">店退仓单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="untreadNo" name="untreadNo" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">店退仓通知单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="untreadMmNo" required="true" name="untreadMmNo" style="width:120px;" readonly="readonly"/>
							<input type="button" id="untreadMmBtn" value="..." onclick="billumuntread.showUntreadMmNoDialog()">
						</td>
						<td class="common-td blank">客户名称：</td>
						<td>
							<input class="easyui-combobox" required="true" data-options="editable:false" name="storeNo" id="storeNo" style="width:120px;" readonly="readonly"/>
							<input name="storeNo" type="hidden" id="storeNoHide"  required="true"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" required="true" name="ownerNo" id="ownerNo" style="width:120px"/>
							<input name="ownerNo" type="hidden" id="ownerNoHide"/>
						</td>
					</tr>
					<tr>	
						<td class="common-td blank">退仓类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="untreadType" id="untreadType" required="true" style="width:120px"/>
							<input name="untreadType" type="hidden" id="untreadTypeHide"/>
						</td>
						<td class="common-td blank">品质：</td>
						<td>
							<input class="easyui-combobox" required="true" name="quality" id="quality" style="width:120px;" readonly="readonly"/>
							<input name="quality" type="hidden" id="qualityHide"  required="true"/>
						</td>
						<td class="common-td blank">来源单号：</td>
						<td><input class="easyui-validatebox ipt" name="poNo" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
						<td class="common-td blank">来源类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="poType" id="poType"  style="width:120px"/>
						</td>
					</tr>	
					<tr>
						<td class="common-td blank">备注：</td>
						<td colspan="7"><input class="easyui-validatebox ipt" name="remark" id="remark" style="width:100%" data-options="validType:['vLength[0,250,\'最多只能输入250个字符\']']"/></td>
					</tr>		    			
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'" data-options="fit:true">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false">
					<div id="sub_con">
						<@p.toolbar id="opBtn"  listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billumuntread.showBoxDialog()","type":0},
			      		 	{"title":"删除明细","iconCls":"icon-del-dtl","action":"billumuntread.deleterow()","type":0},
			      		 	{"title":"保存明细","iconCls":"icon-save-dtl","action":"billumuntread.saveDetail()","type":0}
			      		 	]
		  				/>
					</div>
					<@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					           rownumbers="true"  onClickRowEdit="true" singleSelect="true"  title="明细列表" divToolbar="#sub_con" emptyMsg="" 
					           columnsJsonList="[		
									{field:'boxNo',title:'箱号',width:150,align:'left'},
									{field:'qty',title:'数量',width:150,align:'right'}
								]"/>
				</div>
			</div>
		</div>
	</div>	    
</div>
<#-- 详情 -->
<div id="detailDialogView" class="easyui-window" title="明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="padding:5px;">
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">店退仓单号：</td>
						<td>
							<input class="easyui-validatebox ipt" name="untreadNo" style="width:120px;"/>
						</td>
						<td class="common-td blank">店退仓通知单号：</td>
						<td>
							<input class="easyui-validatebox ipt"  name="untreadMmNo" style="width:120px;"/>
						</td>
						<td class="common-td blank">客户名称：</td>
						<td>
							<input class="easyui-validatebox ipt"  name="storeName" style="width:120px;"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="ownerNo" id="view_ownerNo" style="width:120px"/>
						</td>
					</tr>
					<tr>	
						<td class="common-td blank">退仓类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="untreadType" id="view_untreadType" style="width:120px"/>
						</td>
						<td class="common-td blank">品质：</td>
						<td>
							<input class="easyui-combobox" required="true" name="quality" id="quality_view" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">来源单号：</td>
						<td><input class="easyui-validatebox ipt" name="poNo" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
						<td class="common-td blank">来源类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="poType" id="view_poType"  style="width:120px"/>
						</td>
					</tr>	
					<tr>
						<td class="common-td blank">备注：</td>
						<td colspan="7"><input class="easyui-validatebox ipt" name="remark" style="width:100%" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"/></td>
					</tr>		    			
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'">
			<@p.datagrid id="dataGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="明细列表"  emptyMsg="" 
					           columnsJsonList="[		
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'itemName',title:'商品名称',width:150,align:'left'},
									{field:'colorName',title:'颜色',width:80,align:'left'},
									{field:'sizeNo',title:'尺码',width:50},
									{field:'brandName',title:'品牌',width:100,align:'left'},
									{field:'boxNo',title:'箱号',width:125,align:'left'},
				 					{field:'itemQty',title:'数量',width:125,align:'left',align:'right'},
				 					{field:'receiptQty',title:'收货数量',width:125,align:'right'},
				 					{field:'checkQty',title:'验收数量',width:125,align:'right'}
								]"/>
				</div>
		</div>
	</div>
</div>
<#-- 箱号选择 -->	
<div id="boxDialog" class="easyui-window"  title="箱号选择"  style="width:700px;"
		    data-options="modal:true,resizable:true,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				 	<#-- 工具菜单div -->
					<div id="itemSelectDiv">
						<@p.toolbar  id="edittoolbaritem"  listData=[
							{"title":"查询","iconCls":"icon-search","action":"billumuntread.searchBox()", "type":0},
							{"title":"清除","iconCls":"icon-remove","action":"billumuntread.searchBoxClear()", "type":0},
							{"title":"确定","iconCls":"icon-ok","action":"billumuntread.selectBoxOK()", "type":0},
							{"title":"取消","iconCls":"icon-cancel","action":"billumuntread.closeWindow('boxDialog')", "type":0}
						]/>
					</div>
					<div id="itemSearchDiv" style="padding:10px;">
						<form id="boxSearchForm"  metdod="post">
							<table>
								<tr>
									<td class="common-td">箱号：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="boxNo"/></td>
									<td class="common-td blank">商品编码：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="itemNo"/></td>
								</tr>
							</table>
						</form>
					</div>
				</div>	
				<div data-options="region:'center',border:false">
				 	<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGridJGBox"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="#itemSelectCellDiv" 
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="false" title="箱号列表"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'boxNo',title : '箱号 ',width : 150, align:'left'},
			                {field : 'qty',title : '数量',width : 150, align:'right'}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                	//billimcheck.selectItemOK(rowData);
		            }}'/>
		     </div>
		</div>    
</div>

<#-- 退仓通知单选择 -->	
<div id="umtreadMmDialog" class="easyui-window"  title="退仓通知单列表"  style="width:420px;height:300px;"
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false">
				 	<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGrid_umtreadMm"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="#itemSelectCellDiv" 
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="false"
			           columnsJsonList="[
			           		{field : 'untreadMmNo',title : '退仓通知单 ',width :400, align:'center'}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                	billumuntread.selectUntreadMmNo(rowData);
		            }}'/>
		     </div>
		</div>    
</div>
<#-- 客户选择 -->	
<div id="storeDialog" class="easyui-window"  title="客户列表"  style="width:420px;height:350px;"
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				 	<#-- 工具菜单div -->
					<div>
						<@p.toolbar  id="storeToolBar"  listData=[
							{"title":"查询","iconCls":"icon-search","action":"billumuntread.searchStore()", "type":0},
							{"title":"清空","iconCls":"icon-remove","action":"billumuntread.clearSearchStore()", "type":0}
						]/>
					</div>
					<div style="padding:10px;">
						<form id="storeForm" metdod="post">
							<input type="hidden" name="locno" />
							<input type="hidden" name="checkNo" id="checkNo" />
							<table>
								<tr>
									<td class="common-td">客户编码：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="storeNo"/></td>
									<td class="common-td">客户名称：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="storeName"/></td>
								</tr>
							</table>
						</form>
					</div>
				</div>	
				<div data-options="region:'center',border:false">
				 	<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGrid_storeNo"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="#itemSelectCellDiv" 
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="false" 
			           columnsJsonList="[
			           		{field : 'storeNo',title : '客户编码 ',width :80, align:'center'},
			           		{field : 'storeName',title : '客户名称 ',width :300, align:'left'}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                	billumuntread.selectStore(rowData);
		            }}'/>
		     </div>
		</div>    
</div>

</body>
</html>