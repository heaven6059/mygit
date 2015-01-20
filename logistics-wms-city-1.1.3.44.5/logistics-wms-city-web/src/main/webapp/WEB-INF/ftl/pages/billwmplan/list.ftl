<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂计划</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billwmplan/billwmplan.js?version=1.0.6.1"></script>    
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
     <div data-options="region:'north',border:false">
       <@p.toolbar id="maintoolbar"  listData=[
      		 {"title":"查询","iconCls":"icon-search","action":"billwmplan.searchArea()","type":0},
      		 {"title":"清空","iconCls":"icon-remove","action":"billwmplan.searchLocClear()","type":0},
             {"title":"新增","iconCls":"icon-add","action":"billwmplan.addInfo()","type":1},
             {"title":"修改","iconCls":"icon-edit","action":"billwmplan.editInfo()","type":2},
             {"title":"删除","iconCls":"icon-remove","action":"billwmplan.del()","type":3},
             {"title":"审核","iconCls":"icon-aduit","action":"billwmplan.audit()","type":4},
	         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂计划')","type":0}
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
		   					<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm"/></td>
		   				</tr>
		   				<tr>		
		   					<td class="common-td blank">货主：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo" /></td>
		   					<td class="common-td blank">审核人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
							<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		   					<td class="common-td blank">计划类型：</td>
							<td><input class="easyui-combobox" data-options="editable:false" name="planType" id="planType_search"  style="width:120px"/></td>
		   					<td class="common-td blank">计划单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo"/></td>
		   					<td class="common-td blank">来源单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">所属品牌： </td>
	             			<td colspan="8">
	             				<input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" />
	             			</td>
		   				</tr>
		   				
		   			</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title="退厂计划单列表"
	              isHasToolBar="false"  onClickRowEdit="false"  emptyMsg=""
	               pagination="true" rownumbers="true"  singleSelect = "false"
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'statusName',title : '状态',width : 70,align:'left'},
						{field : 'planNo',title : '计划单号',width :180},
						{field : 'sourceNo',title : '来源单号',width :180},
						{field : 'ownerName',title : '货主',width : 120,align:'center'},
						{field : 'planTypeName',title : '计划类型',width : 100,align:'center'},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 100,sortable:true},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 100,sortable:true},
						{field : 'audittm',title : '审核时间',width : 130,sortable:true},
						{field : 'editor',title : '更新人',width : 100,sortable:true},
						{field : 'editorName',title : '更新人名称',width : 100,sortable:true},
						{field : 'edittm',title : '更新时间',width : 130,sortable:true}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                            // 触发点击方法  调JS方法
	                   },onDblClickRow:function(rowIndex, rowData){
	                   	    billwmplan.showDetail(rowData);
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
      		 	{"id":"addBtn","title":"保存","iconCls":"icon-save","action":"billwmplan.save_main()","type":0},
      		 	{"id":"editBtn","title":"修改","iconCls":"icon-edit","action":"billwmplan.edit_main()","type":0},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billwmplan.coloseDetailDialog()","type":0}
      		 	]
		  	/>
			<form id="detailForm" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">计划单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="planNo" name="planNo" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" required="true" name="ownerNo" id="ownerNo" style="width:120px"/>
							<input name="ownerNo" type="hidden" id="ownerNoHide"/>
						</td>
						<td class="common-td blank">计划类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="planType" id="planType" required="true" style="width:120px"/>
							<input name="planType" type="hidden" id="planTypeHide"/>
						</td>
						<td class="common-td blank">来源单号：</td>
						<td><input class="easyui-validatebox ipt" name="sourceNo" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
					</tr>
					<tr>	
						<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
             			<td colspan="8">
             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo"  required="true"/>
             				<input name="sysNo" type="hidden" id="sysNoHide"/>
             			</td>
					</tr>	
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'" data-options="fit:true">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false">
					<div id="sub_con">
						<@p.toolbar id="opBtn"  listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billwmplan.showitemDialog()","type":0},
			      		 	{"title":"删除明细","iconCls":"icon-del-dtl","action":"billwmplan.deleterow()","type":0},
			      		 	{"title":"保存明细","iconCls":"icon-save-dtl","action":"billwmplan.saveDetail()","type":0}
			      		 	]
		  				/>
					</div>
					<@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					           rownumbers="true"  onClickRowEdit="true" singleSelect="true"  title="明细列表" divToolbar="#sub_con" emptyMsg="" 
					           columnsJsonList="[		
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'itemName',title:'商品名称',width:150,align:'left'},
									{field:'colorName',title:'颜色',width:80,align:'left'},
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
		    
		    <input id="planTypeHid" type="hidden">
		    <input id="statusHid" type="hidden">
		    
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
		<@p.toolbar id="submainviewtoolbar"  listData=[
      		 	{"title":"转移库计划","iconCls":"icon-redo","action":"billwmplan.changeHMPlan();","type":4},
      		 	{"id":"btnToStockLock","title":"转库存锁定","iconCls":"icon-redo","action":"billwmplan.toStoreLock()","type":4},
      		 	{"title":"转退厂申请","iconCls":"icon-redo","action":"billwmplan.changeWMRequest();","type":4},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billwmplan.coloseDetailViewDialog()","type":0}
      		 	]
		  	/>
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form">
	    		<input type="hidden" name="ownerNo" id="ownerNoView">
	    		<table>
					<tr>
						<td class="common-td blank">计划单号：</td>
						<td>
							<input class="easyui-validatebox ipt" name="planNo" id="planNoView" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-validatebox ipt" data-options="editable:false"  name="ownerName" style="width:120px"/>
						</td>
						<td class="common-td blank">计划类型：</td>
						<td>
							<input class="easyui-validatebox ipt" data-options="editable:false" name="planTypeName"   style="width:120px"/>
						</td>
						<td class="common-td blank">来源单号：</td>
						<td><input class="easyui-validatebox ipt" name="sourceNo" style="width:120px"  /></td>
					</tr>
					<tr>	
						<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
             			<td colspan="8">
             				<input name="sysNoName" class="easyui-validatebox ipt"  data-options="editable:false"/>
             			</td>
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
									{field:'colorName',title:'颜色',width:80,align:'left'}
								]"/>
				</div>
		</div>
	</div>
</div>
<#-- 商品选择 -->	
<div id="itemDialog" class="easyui-window"  title="商品选择"  style="width:700px;"
		    data-options="modal:true,resizable:true,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				 	<#-- 工具菜单div -->
					<div id="itemSelectDiv">
						<@p.toolbar  id="edittoolbaritem"  listData=[
							{"title":"查询","iconCls":"icon-search","action":"billwmplan.searchItem()", "type":0},
							{"title":"清除","iconCls":"icon-remove","action":"billwmplan.searchItemClear()", "type":0},
							{"title":"确定","iconCls":"icon-ok","action":"billwmplan.selectItemOK()", "type":0},
							{"title":"取消","iconCls":"icon-cancel","action":"billwmplan.closeUI()", "type":0}
						]/>
					</div>
				
					<div id="itemSearchDiv" style="padding:10px;">
						<form name="itemSearchForm" id="itemSearchForm" metdod="post">
							<input type="hidden" name="locno" />
							<input type="hidden" name="checkNo" id="checkNo" />
							<table>
								<tr>
									<td class="common-td">商品编码：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="itemNo" id="itemNo"   disable="true"/></td>
									<td class="common-td blank">商品名称：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="itemName" id="itemName"   disable="true"/></td>
								</tr>
							</table>
						</form>
					</div>
				</div>	
				<div data-options="region:'center',border:false">
				 	<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="#itemSelectCellDiv" 
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="false" title="商品明细"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'itemNo',title : '商品编码 ',width : 150, align:'left'},
			                {field : 'itemName',title : '商品名称',width : 150, align:'left'},
			                {field : 'colorName',title : '颜色',width : 90, align:'left'},
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                	//billimcheck.selectItemOK(rowData);
		            }}'/>
		     </div>
		</div>    
</div>
</body>
</html>