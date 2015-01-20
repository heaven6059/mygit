<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓通知单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billumuntreadmm/billumuntreadmm.js?version=1.0.6.2"></script>    
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
     <div data-options="region:'north',border:false">
       <@p.toolbar id="maintoolbar"  listData=[
      		 {"title":"查询","iconCls":"icon-search","action":"billumuntreadmm.searchArea()","type":0},
      		 {"title":"清空","iconCls":"icon-remove","action":"billumuntreadmm.searchLocClear()","type":0},
             {"title":"新增","iconCls":"icon-add","action":"billumuntreadmm.addInfo()","type":1},
             {"title":"修改","iconCls":"icon-edit","action":"billumuntreadmm.editInfo()","type":2},
             {"title":"删除","iconCls":"icon-del","action":"billumuntreadmm.del()","type":3},
             {"title":"审核","iconCls":"icon-aduit","action":"billumuntreadmm.audit()","type":4},
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
		   					<td class="common-td blank">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
		   					<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo" /></td>
		   					<td class="common-td blank">退仓类型：</td>
							<td><input class="easyui-combobox" data-options="editable:false" name="untreadType" id="untreadType_search"  style="width:120px"/></td>
							<td class="common-td blank">品&nbsp;&nbsp;&nbsp;&nbsp;质：</td>
		   					<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>
		   				
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">店退仓通知单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo"/></td>
		   					<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_creator" name="creator"/></td>
		   					<td class="common-td blank">创建日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">来源单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo"/></td>
							<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td>
	             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
	             			</td>
		   					<td class="common-td blank">所属品牌：</td>
							<td  colspan="5"><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
		   				</tr>
		   			</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="/bill_um_untread_mm/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="店退仓通知单列表"
	              isHasToolBar="false"  onClickRowEdit="false"  
	               pagination="true" rownumbers="true"  singleSelect = "false"
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'status',title : '状态',width : 70,formatter:billumuntreadmm.statusFormatter,align:'left'},
						{field : 'untreadMmNo',title : '店退仓通知单号',width :180},
						{field : 'poNo',title : '来源单号',width :180},
						{field : 'ownerNo',title : '货主',width : 120,align:'left',formatter:billumuntreadmm.ownerFormatter},
						{field : 'untreadType',title : '退仓类型',width : 100,formatter:billumuntreadmm.typeFormatter,align:'left'},
						{field : 'quality',title : '品质',width : 100,formatter:billumuntreadmm.qualityFormatter,align:'left'},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true,sortable:true},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130,sortable:true}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                            // 触发点击方法  调JS方法
	                   },onDblClickRow:function(rowIndex, rowData){
	                   	    billumuntreadmm.showDetail(rowData);
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
      		 	{"id":"addBtn","title":"保存","iconCls":"icon-save","action":"billumuntreadmm.save_main()","type":0},
      		 	{"id":"editBtn","title":"修改","iconCls":"icon-edit","action":"billumuntreadmm.edit_main()","type":0},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billumuntreadmm.coloseDetailDialog()","type":0}
      		 	]
		  	/>
			<form id="detailForm" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">店退仓通知单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="untreadMmNo" name="untreadMmNo" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" required="true" name="ownerNo" id="ownerNo" style="width:120px"/>
							<input name="ownerNo" type="hidden" id="ownerNoHide"/>
						</td>
						<td class="common-td blank">品牌库：</td>
						<td>
							<input class="easyui-combobox" required="true" name="sysNo" id="sysNo" style="width:120px"/>
							<input name="sysNo" id="sysNoHide" type="hidden"/>
						</td>
						<td class="common-td blank">退仓类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="untreadType" id="untreadType" required="true" style="width:120px"/>
							<input name="untreadType" type="hidden" id="untreadTypeHide"/>
						</td>
					</tr>
					<tr>	
						<td class="common-td blank">品质：</td>
						<td>
							<input class="easyui-combobox" required="true" name="quality" id="quality" style="width:120px;" readonly="readonly"/>
							<input name="quality" type="hidden" id="qualityHide"  required="true"/>
						</td>
						<td class="common-td blank">申请日期：</td>
						<td>
							<input class="easyui-datebox ipt" name="applyDate" style="width:120px" id="applyDate"/>
						</td>
						<td class="common-td blank">来源单号：</td>
						<td><input class="easyui-validatebox ipt" name="poNo" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
						<td class="common-td blank">来源类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="poType" id="poType"   style="width:120px"/>
						</td>
					</tr>	
					<tr>
						<td class="common-td blank">备注：</td>
						<td colspan="7"><input class="easyui-validatebox ipt" name="remark" style="width:100%" data-options="validType:['vLength[0,250,\'最多只能输入250个字符\']']"/></td>
					</tr>		    			
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'" data-options="fit:true">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false">
					<div id="sub_con">
						<@p.toolbar id="opBtn"  listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billumuntreadmm.showitemDialog()","type":0},
			      		 	{"title":"删除明细","iconCls":"icon-del-dtl","action":"billumuntreadmm.deleterow()","type":0},
			      		 	{"title":"保存明细","iconCls":"icon-save-dtl","action":"billumuntreadmm.saveDetail()","type":0}
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
									{field:'sizeNo',title:'尺码',width:50},
									{field:'brandName',title:'品牌',width:100,align:'left'},
									{field:'storeNo',title:'客户编码',width:125,align:'left',
					 					editor:{
					 						type:'validatebox',
					 						options:{
						 						required:true,
						 						missingMessage:'客户编码为必填项!'
					 						}
					 					}
				 					},
				 					{field:'itemQty',title:'计划数量',width:125,align:'left',
					 					editor:{
					 						type:'numberbox',
					 						options:{
						 						required:true,
						 						missingMessage:'计划数量为必填项!',
						 						min:1
					 						}
					 					}
				 					}
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
						<td class="common-td blank">店退仓通知单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="untreadMmNo" name="untreadMmNo" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="ownerNo" id="view_ownerNo" style="width:120px"/>
						</td>
						<td class="common-td blank">品牌库：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="sysNo" id="view_sysNo" style="width:120px"/>
						</td>
						<td class="common-td blank">退仓类型：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="untreadType" id="view_untreadType"  style="width:120px"/>
						</td>
					</tr>
					<tr>	
						<td class="common-td blank">品质：</td>
						<td>
							<input class="easyui-combobox" required="true" name="quality" id="quality_view" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">申请日期：</td>
						<td>
							<input class="easyui-validatebox ipt" name="applyDate" style="width:120px" id="applyDate"/>
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
									{field:'storeNo',title:'客户编码',width:120,align:'left'},
									{field:'storeName',title:'客户名称',width:180,align:'left'},
				 					{field:'itemQty',title:'计划数量',width:80,align:'left',align:'right'}
								]"/>
				</div>
		</div>
	</div>
</div>
<#-- 商品选择 -->	
<div id="itemDialog" class="easyui-window"  title="箱号选择"  style="width:700px;"
		    data-options="modal:true,resizable:true,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				 	<#-- 工具菜单div -->
					<div id="itemSelectDiv">
						<@p.toolbar  id="edittoolbaritem"  listData=[
							{"title":"查询","iconCls":"icon-search","action":"billumuntreadmm.searchItem()", "type":0},
							{"title":"清除","iconCls":"icon-remove","action":"billumuntreadmm.searchItemClear()", "type":0},
							{"title":"确定","iconCls":"icon-ok","action":"billumuntreadmm.selectItemOK()", "type":0},
							{"title":"取消","iconCls":"icon-cancel","action":"billumuntreadmm.closeUI()", "type":0}
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
									<td class="common-td blank">商品条码：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="barcode" id="barcode"   disable="true"/></td>
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
			                {field : 'barcode',title : '商品条码',width : 150, align:'left'},
			                {field : 'styleNo',title : '款号 ',width : 90, align:'left'},
			                {field : 'colorName',title : '颜色',width : 90, align:'left'},
			                {field : 'sizeNo',title : '尺码',width : 90, align:'left'}
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