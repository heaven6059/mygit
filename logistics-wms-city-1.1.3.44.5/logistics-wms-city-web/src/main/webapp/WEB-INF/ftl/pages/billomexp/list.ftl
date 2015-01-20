<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>发货通知</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/easyui.css?version=1.0.5.0" />
	<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/validator.css?version=1.0.5.0"/>
	<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/icon.css?version=1.0.5.0"/>
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bmdefloc.css?version=1.0.5.0" />
	<script type="text/javascript" src="${domainStatic}/resources/js/billomexp/billomexp.js?1?version=1.0.5.2"></script>
	<script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billomexp.searchBillOmExp()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"billomexp.searchBillOmExpClear();", "type":0},
				{"title":"新增","iconCls":"icon-add","action":"billomexp.showAdd()","type":1},
				{"title":"修改","iconCls":"icon-edit","action":"billomexp.showModify()","type":2},
				{"title":"删除","iconCls":"icon-del","action":"billomexp.deleteBillOmExp()","type":3},
				{"title":"关闭","iconCls":"icon-close","action":"closeWindow('发货通知')","type":0}
			 ]/>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td blank">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
                     			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                     			<td class="common-td blank">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmBegin" id="createtmBeginCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEndCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoCondition" /></td>
                     			<td class="common-td blank">来源单号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceexpNo" id="sourceexpNoCondition" /></td>
                     			<td class="common-td blank">出货日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="expDateBegin" id="expDateBeginCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="expDateEnd" id="expDateEndCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">分货类型：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="classType" id="classTypeCondition"/></td>
                     			<td class="common-td blank">业务类型：</td>
								<td ><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessTypeCondition" /></td>
								<td class="common-td blank">来源类型：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceTypeCondition" /></td>
                     			<td class="common-td blank">拆分状态：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="splitStatus" id="splitStatusCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">合&nbsp;同&nbsp;号：</td>
                     			<td ><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoCondition" /></td>
								<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoSearch"/></td>	
								<td class="common-td blank">所属品牌：</td>
                     			<td colspan="3"><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
                     		</tr>
                     	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="发货通知"
		                   isHasToolBar="false" divToolbar=""  height="430"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false" showFooter="true"
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 50, checkbox:true},
			               	  {field : 'status',hidden : true},
			               	  {field : 'classType',hidden : true},
			                  {field : 'locno',title : '仓库',width : 150,hidden:true},
			                  {field : 'statusStr',title : '单据状态',width : 80,align:'left'},
			                  {field : 'expNo',title : '单据编号',width : 180},
			                  {field : 'sourceexpNo',title : '来源单号',width : 180},
			                  {field : 'ownerNo',title : '货主',width : 100, formatter: wms_city_common.columnOwnerFormatter,align:'left'},
			                  {field : 'sysNoName',title : '品牌库',width : 80,align:'left'},
			                  {field : 'classTypeStr',title : '分货类型',width : 100,align:'left'},
			                  {field : 'businessType',title : '业务类型',width : 120,align:'left', formatter: billomexp.businessTypesFormatter},
			                  {field : 'expType',title : '单据类型',width : 95,formatter:billomexp.columninit_exp_typeFormatter,align:'left'},
			                  {field : 'sourceTypeStr',title : '来源类型',width : 100},
			                  {field : 'splitStatusStr',title : '拆分状态',width : 100},
			                  {field : 'expDate',title : '出货日期',width : 100},
			                  {field : 'deliverType',title : '配送方式',width : 80, formatter: billomexp.columnDELIVER_TYPEFormatter,align:'left'},
			                  {field : 'transportType',title : '运输方式',width : 80, formatter: billomexp.columnTRANSPORT_TYPEFormatter,align:'left'},
			                  {field : 'itemQty',title : '计划数量',width : 80,align:'right'},
			                  {field : 'realQty',title : '复核数量',width : 80,align:'right'},
			                  {field : 'expRemark',title : '备注',width : 180,align:'left'},
			                  {field : 'creator',title : '创建人',width : 100,align:'left'},
			                  {field : 'creatorname',title : '创建人名称',width : 100,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 125,sortable:true}
			                 ]"   
			                 jsonExtend='{
						       	onLoadSuccess:function(data){//合计
						       		billomexp.onLoadSuccess(data);
						       	},
			                 	onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  billomexp.showView(rowData,rowIndex);
				                }
		                     }'
				           />
			</div>
		</div>
	</div>
	<#-- 新增窗口 BEGIN -->
	<div id="showDialog"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="addtoolbar" listData=[
	                             {"id":"btn-add","title":"保存","iconCls":"icon-save","action":"billomexp.saveMain()", "type":1},
	                             {"id":"btn-modify","title":"修改","iconCls":"icon-save","action":"billomexp.modifyBillOmExp('dataForm')", "type":2},
	                             {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billomexp.closeShowWin('showDialog')","type":0}
		                       ]
					  />
		    		<div class="search-div">
		         		<form name="dataForm" id="dataForm" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td blank">单据编号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNo" readOnly='readOnly' missingMessage='编码为必填项!'  data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  /></td>
		                 			<td class="common-td blank">单据类型：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="expType" id="expType"   required="true" />
										<input class="easyui-validatebox ipt" style="width:120px" name="expType" id="expTypeHide" type="hidden"/>
										<label style="display:none;">&nbsp;订&nbsp;单&nbsp;号：<input class="easyui-validatebox" style="width:120px" name="importNo" id="importNo" /></label> 
		                 			</td>
		                 			<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" required="true" />
		                 				<input class="easyui-validatebox" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/>
		                 			</td>
		                 			<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo" required="true" /></td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">分货类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="classType" id="classType" required="true" /></td>
		                 			<td class="common-td blank">业务类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="businessType" id="businessType" required="true" /></td>
		                 			<td class="common-td blank">配送方式：</td>
		                 			<td>
		                 				<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
		                 				<input class="easyui-combobox" style="width:120px" name="deliverType" id="deliverType" required="true"/>
		                 			</td>
		                 			<td class="common-td blank">运输方式：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="transportType" id="transportType" required="true"/></td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">出货日期：</td>
		                 			<td><input class="easyui-datebox" style="width:120px" name="expDate" id="expDate"  required="true"/></td>
		                 		</tr>
		                    </table>
		         		</form>
			         </div>
		    	</div>
		    	<div data-options="region:'center',border:false">
		    		<div class="easyui-layout" data-options="fit:true" id="detailDiv">
		    			<@p.toolbar id="zoneInfoToolDiv" listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billomexp.addItemDetail_module('module')", "type":0}
							{"title":"删除明细","iconCls":"icon-del-dtl","action":"billomexp.removeBySelected('module')", "type":0}
							{"title":"保存明细","iconCls":"icon-save-dtl","action":"billomexp.save('module')", "type":0}
                   		]/>
                   		<div data-options="region:'center',border:false">
                   			<@p.datagrid id="module" name="" title="发货通知明细" loadUrl="" saveUrl="" defaultColumn="" 
					 			isHasToolBar="false"  divToolbar="#zoneInfoToolDiv"  height="320"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
								pagination="true" rownumbers="true" emptyMsg=""
					 			columnsJsonList="[
					 				{field:'id',title:'编号',width:70,hidden:true
					 				},
					 				{field:'storeName',title:'客户名称',width:180,align:'left',
					 					editor:{
					 						type:'validatebox',
					 						options:{
						 						default:'',
						 						required:true,
						 						missingMessage:'客户名称为必填项!'
					 						}
					 					}
					 				},
					 				{field:'poNo',title:'合同号',width:125,align:'left',
					 					editor:{
					 						type:'validatebox',
					 						options:{
						 						default:'',
						 						required:true,
						 						missingMessage:'合同号为必填项!'
					 						}
					 					}
					 				},
					 				{field:'itemNo',title:'商品编码',width:125,align:'left'},
					 				{field:'sizeNo',title:'尺码',width:80,align:'left'},
					 				{field:'itemName',title:'商品名称',width:180,align:'left'},
					 				{field :'tBarcode',title : '商品条码',width : 130,align:'left'},
					 				{field:'styleNo',hidden : true},
					 				{field:'colorNoStr',title:'颜色',width:80,align:'left'},
					 				{field : 'brandNo',title : '品牌编码',width : 100,align:'left'},
					 				{field:'brandNoStr',title:'品牌',width:125,align:'left'},
					 				{field:'itemQty',title:'商品数量',width:60,align:'right',
					 					editor:{
					 						type:'numberbox',
					 						options:{
						 						min:1,
						 						max:9999,
						 						default:0,
						 						required:true,
						 						missingMessage:'商品数量为必填项!'
					 						}
					 					}
					 				},
								    {field:'scheduleQty',title:'已分配数量',width:80,align:'right'},
					 				{field:'deliverQty',title:'实际出库数量',width:90,align:'right'}
					 			]"/>
                   		</div>
		    		<div>
		    	</div>
		    </div>
	</div>
	<#-- 修改/查看 窗口 -->
	<div id="showEditDialog"  class="easyui-dialog" title="修改/查看"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<#-- 工具菜单div -->
	               <@p.toolbar id="edittoolbar" listData=[
	                             {"id":"btn-add-edit","title":"修改","iconCls":"icon-save","action":"billomexp.modifyBillOmExp('dataEditForm')", "type":2},
	                             {"id":"btn-canle-edit","title":"取消","iconCls":"icon-cancel","action":"billomexp.closeShowWin('showEditDialog')", "type":0},
	                             {"id":"btn-canle-edit","title":"关单","iconCls":"icon-ok","action":"billomexp.closeBillOmExp('dataEditForm')", "type":4}
		                       ]
					  />
					<div class="search-div">
		         		<form name="dataEditForm" id="dataEditForm" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNo" readOnly='readOnly' missingMessage='编码为必填项!'  data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  /></td>
		                 			<td class="common-td blank">单据类型：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="expType" id="expType"   required="true" />
		                 			</td>
		                 			<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" required="true" /></td>
		                 			<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo" required="true" />
										<input class="easyui-validatebox" name="expType" id="expTypeHide" type="hidden"/>
										<input class="easyui-validatebox" name="status" id="status" type='hidden'/>
										<input class="easyui-validatebox" name="ownerNo" id="ownerNoHide" type="hidden"/>
		                 			</td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">分货类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="classType" id="classTypeUpdate" required="true" /></td>
		                 			<td class="common-td blank">业务类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="businessType" id="businessType" required="true" /></td>
		                 			<td class="common-td  blank">配送方式：</td>
		                 			<td>
		                 				<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
		                 				<input class="easyui-combobox" style="width:120px" name="deliverType" id="deliverType" required="true"/>
		                 			</td>
		                 			<td class="common-td blank">运输方式：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="transportType" id="transportType" required="true"/></td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">出货日期：</td>
		                 			<td><input class="easyui-datebox" style="width:120px" name="expDate" id="expDate"  required="true"/></td>
		                 		</tr>
		                    </table>
		         		</form>
					</div>
				</div>
			    <div data-options="region:'center',border:false">
		    		<div class="easyui-layout" data-options="fit:true" id="detailEditDiv">
		    			<@p.toolbar id="zoneInfoToolEditDiv" listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billomexp.addItemDetail_module('moduleEdit')", "type":0}
							{"title":"删除明细","iconCls":"icon-del-dtl","action":"billomexp.removeBySelected('moduleEdit')", "type":0}
							{"title":"保存明细","iconCls":"icon-save-dtl","action":"billomexp.save('moduleEdit')", "type":0}
                   		]/>
                   		<div data-options="region:'center',border:false">
                   			<@p.datagrid id="moduleEdit" name="" title="发货通知明细" loadUrl="" saveUrl="" defaultColumn="" 
					 			isHasToolBar="false"  divToolbar="#zoneInfoToolEditDiv"  height="320"  onClickRowEdit="flase" singleSelect="true" pageSize='20'  
								pagination="true" rownumbers="true" emptyMsg=""
					 			columnsJsonList="[
					 				{field:'id',title:'编号',width:70,hidden:true},
					 				{field:'storeName',title:'客户名称',width:180,align:'left',
					 					editor:{
					 						type:'validatebox',
					 						options:{
						 						default:'',
						 						required:true,
						 						missingMessage:'客户名称为必填项!'
					 						}
					 					}
					 				},
					 				{field:'poNo',title:'合同号',width:150,align:'left',
					 					editor:{
					 						type:'validatebox',
					 						options:{
						 						default:'',
						 						required:true,
						 						missingMessage:'合同号为必填项!'
					 						}
					 					}
					 				},
					 				{field:'itemQty',title:'商品数量',width:60,align:'right',
					 					editor:{
					 						type:'numberbox',
					 						options:{
						 						min:1,
						 						max:9999,
						 						default:0,
						 						required:true,
						 						missingMessage:'商品数量为必填项!'
					 						}
					 					}
					 				},
					 				{field:'itemNo',title:'商品编码',width:125,align:'left'},
					 				{field:'sizeNo',title:'尺码',width:80,align:'left'},
					 				{field:'itemName',title:'商品名称',width:180,align:'left'},
					 				{field:'tBarcode',title : '商品条码',width : 130,align:'left'},
					 				{field:'styleNo',hidden : true},
					 				{field:'colorNoStr',title:'颜色',width:80,align:'left'},
					 				{field : 'brandNo',title : '品牌编码',width : 100,align:'left'},
					 				{field:'brandNoStr',title:'品牌',width:125,align:'left'},
								    {field:'scheduleQty',title:'已分配数量',width:80,align:'right'},
					 				{field:'deliverQty',title:'实际出库数量',width:90,align:'right'},
					 				{field:'tt',title:'tt',width:10,hidden:true}
					 			]"
					 			jsonExtend='{
				                 	onDblClickRow:function(rowIndex, rowData){
					                	  //双击方法
					                   	  billomexp.controllEdit(rowData,rowIndex);
					                }
			                     }'/>
                   		</div>
                   	</div>
		    	</div>
		    </div>
	</div>
	<#-- 查看详情窗口 - 带尺码横排  -->
	<div id="showViewDialog"  class="easyui-dialog" title="详情"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="toolbar_dtl" listData=[
						{"title":"导出","iconCls":"icon-export","action":"exportExcel()","type":5},
						{"title":"手工关闭","iconCls":"icon-ok","action":"billomexp.overExpBill()", "type":4},
						{"title":"转存储发货","iconCls":"icon-redo","action":"billomexp.toClass0ForExp()", "type":4},
						{"title":"转分货发货","iconCls":"icon-redo","action":"billomexp.toClass1ForExp()", "type":4},
						{"title":"取消","iconCls":"icon-cancel","action":"billomexp.closeShowWin('showViewDialog');","type":0}
					 ]/>
		    		<div class="search-div">
		         		<form name="dataViewForm" id="dataViewForm" method="post" class="city-form">
		         			<input id="statusView" type='hidden'/>
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNo" readOnly='readOnly' missingMessage='编码为必填项!'  data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  /></td>
		                 			<td class="common-td blank">单据类型：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="expType" id="expType"   required="true" />
		                 				<input class="easyui-validatebox" style="width:120px" name="expType" id="expTypeHide" type="hidden"/>
		                 			</td>
		                 			<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" required="true" /></td>
		                 			<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		                 			<td>
		                 				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo" required="true" />             
										<input class="easyui-validatebox" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/>
		                 			</td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">分货类型：</td>
		                 			<td><input class="easyui-combobox ipt" style="width:120px" name="classType" id="classTypeView" required="true" /></td>
		                 			<td class="common-td blank">业务类型：</td>
		                 			<td><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType" required="true" /></td>
		                 			<td class="common-td blank">配送方式：</td>
		                 			<td>
		                 				<label style="display:none;">订&nbsp;单&nbsp;号：
		                 				<input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNo" /></label>
		                 				<input class="easyui-validatebox ipt" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
		                 				<input class="easyui-combobox ipt" style="width:120px" name="deliverType" id="deliverType" required="true"/>
		                 			</td>
		                 			<td class="common-td blank">运输方式：</td>
		                 			<td><input class="easyui-combobox ipt" style="width:120px" name="transportType" id="transportType" required="true"/></td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">出货日期：</td>
		                 			<td><input class="easyui-datebox ipt" style="width:120px" name="expDate" id="expDate"  required="true"/></td>
		                 			<td class="common-td blank">备注：</td>
		                 			<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="expRemark" id="expRemark" "/></td>
		                 		</tr>
		                    </table>
		         		</form>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false">
		    		<div id="main_exp_DetailId" class="easyui-tabs" fit="true">
		    			<div id = 'detailViewDiv' title='尺码横排' height='100%'>
		    				<div class="easyui-layout" data-options="fit:true">
		    					<div data-options="region:'center',border:false">
		    						<@p.datagrid id="moduleView" name="" loadUrl="" saveUrl="" defaultColumn="" 
									 		isHasToolBar="false"  columnsJsonList=""  divToolbar="#"  height="528"  onClickRowEdit="false" singleSelect="true" pageSize='20'  
											pagination="true" rownumbers="true"  jsonExtend="{}" height="343" showFooter="true"
									 />
		    					</div>
		    				</div>
		    			</div>
		    			<div id = 'detailViewDiv_dtl' title='明细' height='100%'>	
		    				<div class="easyui-layout" data-options="fit:true">
		    					<div data-options="region:'center',border:false">
		    						<@p.datagrid id="moduleView_dtl" name="" loadUrl="" saveUrl="" defaultColumn="" 
							 			isHasToolBar="false"  height="343"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
										pagination="true" rownumbers="true" showFooter="true"
							 			columnsJsonList="[
							 			   {title:'单号',field:'expNo',hidden:true},
							 			   {title:'商品编码',field:'itemNo',width:180,align:'left'},
						                   {title:'颜色',field:'colorNoStr',width:80,align:'left'},
						                   {title:'尺码',field:'sizeNo',width:80},
						                   {title:'商品名称',field:'itemName',width:150,align:'left'},
						                   {title:'品牌',field:'brandNoStr',width:100,align:'left'},
						                   {title:'合同号',field:'poNo',width:100,align:'left'},
						                   {title:'客户名称',field:'storeName',width:150,align:'left'},
						                   {title:'数量',field:'itemQty',width:80,align:'right'},
						                   {title:'已分配数',field:'scheduleQty',width:80,align:'right'},
						                   {title:'已定位数',field:'locateQty',width:80,align:'right'},
						                   {title:'复核数',field:'realQty',width:80,align:'right'},
						                   {title:'出库数',field:'deliverQty',width:80,align:'right'},
							 			]"
							 		/>
		    					</div>
		    				</div>
		    			</div>
		    		</div>
		    	</div>
		    </div>
	</div>
	<#-- 商品选择div -->
<div id="openUIItem"  class="easyui-dialog" title="选择商品"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		  <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
			    		  <div style="margin-bottom:0px" id="toolDivThree">
			    		  	 <@p.toolbar id="toolBarThree"  listData=[
		                             {"title":"查询","iconCls":"icon-search","action":"billomexp.searchItem();", "type":0},
		                             {"title":"清空","iconCls":"icon-remove","action":"billomexp.searchItemClear();", "type":0},
		                             {"title":"确认","iconCls":"icon-ok","action":"billomexp.confirmItem();","type":0},
		                             {"title":"取消","iconCls":"icon-cancel","action":"billomexp.closeShowWin('openUIItem');","type":0}
			                       ]
						 	 />
						 </div>
					 	<div style="padding:8px;">
						<form name="itemSearchForm" id="itemSearchForm" method="post" class="city-form">
					   		<table>
							   		<tr>
							   			<td class="common-td">商品编码：</td>
							   			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo"   disable="true"/></td>
							   			<td class="common-td blank">商品条码：</td>
							   			<td><input class="easyui-validatebox ipt" style="width:120px" name="tBarcode" id="tBarcode"   disable="true"/></td>
							   			<td class="common-td blank">商品名称：</td>
							   			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemName"   disable="true"/></td>
							   		</tr>
					   		</table>
			                <input class="easyui-validatebox ipt" style="width:130px" name="showGirdName" id="showGirdName"   type ="hidden" />
							<input class="easyui-validatebox ipt" style="width:130px" name="sysNo" id="sysNoForItem"   type ="hidden" />
						</form>
						</div>
			    </div>
				    <div data-options="region:'center',border:false">
				    	<#-- 商品选择数据列表div -->
		          	  	 <@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
				               isHasToolBar="false" divToolbar="#itemSearchDiv" height="450"  onClickRowEdit="false" singleSelect="false" pageSize='20'  
							   pagination="true" rownumbers="true" emptyMsg=""
					           columnsJsonList="[
					           		{field : 'ck',title : '',width : 50, checkbox:true},
					           		{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					           		{field : 'sizeNo',title : '尺码',width : 60,align:'left'},
					                {field : 'itemName',title : '商品名称',width : 180,align:'left'},
					                {field : 'tBarcode',title : '商品条码',width : 130,align:'left'},
					                {field : 'styleNo',hidden : true},
					                {field : 'colorNoStr',title : '颜色',width : 80,align:'left'},
					                {field : 'brandNo',title : '品牌编码',width : 100,align:'left'},
					                {field : 'brandNoStr',title : '品牌',width : 100,align:'left'}
					            ]"/>
				    </div>
		    </div>
	</div>
</body>
</html>