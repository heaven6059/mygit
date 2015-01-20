<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>拣货任务分派</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/easyui.css?version=1.0.5.0" />
	<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/validator.css?version=1.0.5.0"/>
	<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/icon.css?version=1.0.5.0"/>
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bmdefloc.css?version=1.0.5.0" />
	<script type="text/javascript" src="${domainStatic}/resources/js/outstockdirect/outstockdirect.js?version=1.0.5.1"></script>
</head>

<body class="easyui-layout">

	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"outstockdirect.searchLocateNoDataGrid()", "type":0},
				{"title":"清空","iconCls":"icon-remove","action":"outstockdirect.clearSearchForm()", "type":0},
		    	{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('拣货任务分派')","type":0}
		]/>
	</div>
	
	
	<div data-options="region:'center',border:false">
		
		<div class="easyui-layout" data-options="fit:true">
			
			<div data-options="region:'north',split:false" >
			    <div id="locSearchDiv" style="padding:10px;">
		       		<form name="searchForm" id="searchForm" method="post" class="city-form">
		       			<table>
			            	<tr>
			                	<td class="common-td">出货单类型：</td>
			                    <td><input class="easyui-combobox ipt" style="width:120px" name="expType" id="expType" required="true" /></td>
			                    <td>&nbsp;&nbsp;<input class="easyui-validatebox ipt" style="width:120px" name="expTypeName" id="expTypeName" readOnly='readOnly'/></td>
			               		<td class="common-td blank">波次号 ：</td>
			               		<td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo1"/></td>
			               		<td class="common-td blank">合同号：</td>
			               		<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo"/></td>
			               	</tr>
			               	<tr>
			               		<td class="common-td">发货通知单号：</td>
			               		<td colspan='2'><input class="easyui-validatebox ipt" style="width:245px" name="expNo"/></td>
			               		<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td ><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		             			<td class="common-td blank">所属品牌：</td>
                     			<td ><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
			               	</tr>
			           	</table>
		                <input class="easyui-validatebox" style="width:120px" name="batchNo" id="batchNoForProc" type="hidden"/>
		                <input class="easyui-validatebox" style="width:120px" name="locateNo" id="locateNoForProc" type="hidden"/>
		                <input class="easyui-validatebox" style="width:120px" name="operateType" id="operateTypeForProc" type="hidden"/>
		                <input class="easyui-validatebox" style="width:120px" name="locno" id="locnoForProc" type="hidden"/>
		                <input class="easyui-validatebox" style="width:120px" name="areaNo" id="areaNoForProc" type="hidden"/>
		                <input class="easyui-validatebox" style="width:120px" name="storeNo" id="storeNoForProc" type="hidden"/>
		                <input class="easyui-validatebox ipt" style="width:200px" name="pickingPeople" id="pickingPeople2" type="hidden"/>
					</form>
				 </div>
			</div>
			
			
			<div data-options="region:'west',split:false,border:false" style="width:400px;">
		    	<@p.datagrid id="locateNoDataGrid"  loadUrl="" saveUrl=""  defaultColumn=""   title=""
		         	isHasToolBar="false" divToolbar=""  height="220" width="230" onClickRowEdit="false"    
			        rownumbers="true"  singleSelect = "true"  pagination="false" onClickRowAuto="false"
			        checkOnSelect="true"  selectOnCheck="true"
			        columnsJsonList="[
			                  {field : 'id',checkbox :true},
			                  {field:'expType',title:'出货单类型',width:70,hidden:true},
			                  {field : 'locno',title : '仓库编码',width : 100,hidden:true},
			                  {field : 'locateNo',title : '波次号',width : 140},
			                  {field : 'batchNo',title : '批次',width : 115,align:'left',hidden:true},
			                  {field : 'creator',title : '创建人',width : 70,align:'left'},
			                  {field : 'creatorname',title : '创建人名称',width : 70,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 130},
			                  {field : 'editor',title : '更新人',width : 100,sortable:true},
				              {field : 'editorname',title : '更新人名称',width : 100,sortable:true},
				              {field : 'edittm',title : '更新时间',width : 130,sortable:true}
			                 ]"   
			                 jsonExtend='{
			                 	onCheck:function(rowIndex, rowData){
				                	 //双击方法
				                   	 outstockdirect.loadDataOperateType(rowData);
				                }
		                     }'
				/>
			</div>
			
			
			<div data-options="region:'south',split:false,minSplit:true" style="height:250px;">
			    <@p.datagrid id="outstockDirectDataGrid"  loadUrl="" saveUrl=""  defaultColumn=""   title=""
		                   isHasToolBar="false" divToolbar=""  height="320"  onClickRowEdit="false"    pagination="true" pageSize='20'
			               rownumbers="true"  singleSelect = "false" showFooter="true"
			               columnsJsonList="[
			               	  {field : 'expNo',hidden : true},
			               	  {field : 'directSerial',hidden : true},
			                  {field:'expType',title:'出货单类型',width:70,hidden:true},
			                  {field:'locateNo',title:'波次号',width:70,hidden:true},
			                  {field:'batchNo',title:'批次',width:70,hidden:true},
			                  {field:'operateType',title:'作业类型',width:70,hidden:true},
			                  
			                  {field : 'sCellNo',title : '来源储位',width : 150,align:'left'},
			                  {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
			                  {field : 'commdityName',title : '商品名称',width : 200,align:'left'},
			                  {field : 'styleNo',hidden : true},
			                  {field : 'sizeNo',title : '尺码',width : 100,align:'left'},
			                  {field : 'colorName',title : '颜色',width : 100,align:'left'},
			                  {field : 'itemQty',title : '计划数量',width : 100,align:'right'}
			             
			                 ]"   
			                 jsonExtend='{
		                     	onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  //billimimport.showEdit(rowData);
				                }
		                     }'
				/>
			</div>
			
			<div data-options="region:'center'" style="margin-left:5px;padding:5px 5px 0px 5px;border-top:0px;border-bottom:0px;" >
				
				<div class="easyui-layout" data-options="fit:true">
		
					<div data-options="region:'north',border:false" style="height:56px;">
			 			作业类型：<input class="easyui-combobox" style="width:120px" name="operateType" id="operateType" required="true" />
			 			<#-- 
			 			拣货人：<input class="easyui-validatebox ipt" style="width:200px" name="pickingPeople" id="pickingPeople" required="true"/>
			 			<a id="searchBtn" href="javascript:outstockdirect.selectPickingPeople();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">选择</a>&nbsp;&nbsp;
			 			-->
			 			<label style="display:none;">
			 			拣货人：<input class="easyui-combobox" style="width:120px" name="pickingPeople" id="pickingPeople" />
			 			</label>
					 	
					 	&nbsp;&nbsp;
					 	切单规则：<input name = "sortType" value = "0" type = "radio" checked="checked"/>按储位
					 	<input name = "sortType" value = "1" type = "radio" />按商品
					 	
					 	<br/>
					 	
					 	<div style="padding-top:5px;">
					 		<label id='workTitle'>每任务商品数：</label>
			 				<input id="workQty" name="workQty" class="easyui-numberbox ipt" style="width:92px" data-options="min:0,precision:0"  required="true" >
					 		&nbsp;&nbsp;
					 		<label id='floorTitle'><input id='floorCut' name='floorCut' type='checkbox' checked='checked'/>&nbsp;跨楼层拆单</label>
					 		&nbsp;&nbsp;
					 		<a id="searchBtn" href="javascript:outstockdirect.sendOrder();" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">切单</a>
	           			</div>
	           			<input class="easyui-validatebox" style="width:120px" name="expType" id="expTypeHide" type="hidden"/>
	                    <input class="easyui-validatebox" style="width:120px" name="expType" id="locateNoHide" type="hidden"/>
	                    <input class="easyui-validatebox" style="width:120px" name="expType" id="batchNoHide" type="hidden"/>
	                </div>
	                
	                
	                <div data-options="region:'center',border:false" >
	                	
		                	<div id="tt"  data-options="fit:true" >
							    <div id="cuserId" title="按客户" >
						             <@p.datagrid id="storeNoDataGrid"  loadUrl="" saveUrl=""  defaultColumn=""   title=""
						                   isHasToolBar="false" divToolbar=""  onClickRowEdit="false"    pagination="false"
							               rownumbers="true"  singleSelect = "false"
							               columnsJsonList="[
							                  {field : 'id',checkbox :true},
							                  {field:'locno',title:'仓库',width:70,hidden:true},
							                  {field:'expType',title:'出货单类型',width:70,hidden:true},
							                  {field:'locateNo',title:'波次号',width:70,hidden:true},
							                  {field:'batchNo',title:'批次',width:70,hidden:true},
							                  {field:'operateType',title:'作业类型',width:70,hidden:true},
							                  {field : 'storeNo',title : '客户编码',width : 100,align:'left'},
							                  {field : 'storeName',title : '客户名称',width : 180,align:'left'},
							                  {field : 'sumQty',title : '总数量',width : 80,align:'right',sortable:true}
							                 ]"   
							                 jsonExtend='{
							                 	onDblClickRow:function(rowIndex, rowData){
								                	 //双击方法
								                   	 outstockdirect.loadDataOutstockDirectByStore(rowData);
								                }
						                     }'
								     />
						        </div>
						        <div id="warehouseId" title="按库区" >
						             <@p.datagrid id="areaNoDataGrid"  loadUrl="" saveUrl=""  defaultColumn=""   title=""
						                   isHasToolBar="false" divToolbar=""    onClickRowEdit="false"    pagination="false"
							               rownumbers="true"  singleSelect = "false"
							               columnsJsonList="[
							                  {field : 'id',checkbox :true},
							                  {field:'locno',title:'仓库',width:70,hidden:true},
							                  {field:'expType',title:'出货单类型',width:70,hidden:true},
							                  {field:'locateNo',title:'波次号',width:70,hidden:true},
							                  {field:'batchNo',title:'批次',width:70,hidden:true},
							                  {field:'operateType',title:'作业类型',width:70,hidden:true},
							                  {field : 'areaNo',title : '库区编码',width : 100,align:'left'},
							                  {field : 'areaName',title : '库区名称',width : 180,align:'left'},
							                  {field : 'sumQty',title : '总数量',width : 80,align:'right',sortable:true}
							                 ]"   
							                 jsonExtend='{
							                 	onDblClickRow:function(rowIndex, rowData){
								                	 //双击方法
								                   	 outstockdirect.loadDataOutstockDirectByArea(rowData);
								                }
						                     }'
								        />
						       </div>
		                </div>
	            </div>     
	           
			</div>			
			</div>
			
			
			
		</div>
		
	</div>
	
	
 
	<#-- 选择拣货人  窗口 -->
	<div id="showSelectPickingPeopleWin"  class="easyui-window" title="选择拣货人" 
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 

		    <div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<div id="itemSelectDiv">
						<#-- 工具菜单div -->
				       <@p.toolbar id="toolbarpeople" listData=[
				       		{"id":"btn-add","title":"确定","iconCls":"icon-ok","action":"outstockdirect.confirmPickingPeople()", "type":0},
				            {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"outstockdirect.closeShowWin('showSelectPickingPeopleWin')","type":4}
					    ]/>
					</div>
				
					<div id="itemSearchDiv" style="padding:6px;">
						<form name="dataForm" id="dataForm" method="post" >
							<table>
								<tr>
									<td class="common-td">岗位：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="roleid" id="roleid"  /></td>
								</tr>
							</table>
						</form>	
					</div>
				</div>	
				
				<div data-options="region:'center',split:false">
					<@p.datagrid id="pickingPeopleDataGrid" name="" title="" loadUrl="" saveUrl="" defaultColumn="" 
						 	isHasToolBar="false"  divToolbar=""  height="400"  onClickRowEdit="false" 
						 	singleSelect="true" pageSize='20'  selectOnCheck="true" checkOnSelect="true"
							pagination="false" rownumbers="true"
						 	columnsJsonList="[
						 				{field : 'ck',title : '',width : 50, checkbox:true},
						 			    {field:'loginName',title:'人员编码',width:100,align:'left'},
					                    {field:'username',title:'人员名称',width:150,align:'left'},
					                    {field:'roleName',title:'岗位',width:150,align:'left'}
					]"/>
				</div>
				
			</div>
	 </div>	
</body>
</html>