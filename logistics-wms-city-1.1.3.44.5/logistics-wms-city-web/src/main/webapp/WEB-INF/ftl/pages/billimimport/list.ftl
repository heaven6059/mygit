<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>预到货通知单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billimimport/billimimport.js?version=1.0.5.4"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body  class="easyui-layout">
      <#-- 工具菜单div -->
      <div data-options="region:'north',border:false" class="toolbar-region">
	       <@p.toolbar id="toolbar"  listData=[
	       				 {"title":"查询","iconCls":"icon-search","action":"billimimport.searchBillImImport()", "type":0},
	                     {"title":"清空","iconCls":"icon-remove","action":"billimimport.searchBillImImportClear()", "type":0},
	                     {"title":"新增","iconCls":"icon-add","action":"billimimport.showAdd()", "type":0},
	                     {"title":"修改","iconCls":"icon-edit","action":"billimimport.showModify()","type":0},
	                     {"title":"删除","iconCls":"icon-del","action":"deleteImImport()","type":3},
						 {"id":"btn-close","title":"审核","iconCls":"icon-aduit","action":"billimimport.auditImImport()","type":4},
						 {"title":"整单收货","iconCls":"icon-redo","action":"billimimport.receipt();","type":0},
				         {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('预到货通知单')","type":0}
	                   ]
			  />
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
                         			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
                         			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                         			<td class="common-td blank">创建日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px"  name="createtmBegin" id="createtmBeginCondition" /></td>
                         			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEndCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">单据编号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNoCondition" /></td>
                         			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
                         			<td class="common-td blank"> 审核日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="audittmBegin" id="audittmBeginCondition" /></td>
                         			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEndCondition" />
										<input class="easyui-validatebox ipt" style="width:110px" name="locno" id="locno" type="hidden" />
									</td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">业务类型：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessTypeCondition" 
									data-options="editable:false,onChange:function(data){billimimport.loadSupplierNo(data,'supplierNoCondition',true);}"/></td>
                         			<td class="common-td blank">供&nbsp;应&nbsp;商：</td>
                         			<td><input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNoCondition"  /></td>
                         			<td class="common-td blank">发货日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="orderDateBegin" id="orderDateBeginCondition" /></td>
                         			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="orderDateEnd" id="orderDateEndCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		                 			<td class="common-td blank">所属品牌：</td>
									<td ><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
									<td class="common-td blank">预到货日期：</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="requestDateBegin" id="requestDateBeginCondition" /></td>
                         			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                         			<td><input class="easyui-datebox" style="width:120px" name="requestDateEnd" id="requestDateEndCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
                         			<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoCondition"  /></td>
                         			<td class="common-td blank">合&nbsp;同&nbsp;号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="poNoCondition" /></td>
                         			<td class="common-td blank">更新日期：</td>
			                 		<td><input class="easyui-datebox" style="width:120px" name="edittmBegin" id="edittmBeginCondition" /></td>
			                 		<td class="common-line">&nbsp;&mdash;&nbsp;</td>
			                 		<td><input class="easyui-datebox" style="width:120px" name="edittmEnd" id="edittmEndCondition" /></td>
                         		</tr>
                         		<tr>
                         			<td class="common-td blank">厂商入库单号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNoCondition" /></td>
                         			<td class="common-td blank">装车单号：</td>
                         			<td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNoCondition" /></td>
                         			<td class="common-td blank">箱号：</td>
			                 		<td><input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="boxNo_dataForm" /></td>
			                 		<td class="common-td blank"></td>
			                 		<td></td>
                         		</tr>
                         	</table>
						</form>
                     </div>
               </div>
               <!--显示列表-->
               <div data-options="region:'center',border:false">
                     <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="预到货通知单列表"
		                   isHasToolBar="false" divToolbar="#locSearchDiv"  height="460"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false" showFooter="true"
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 50, checkbox:true},
			                  {field : 'locno',title : '仓库编码',width : 100,hidden:true},
			                  {field : 'sysNo',title : '品牌库',width : 100,hidden:true},
			                  {field : 'status',title : '状态',width : 70,align:'left',formatter: billimimport.columnStatusFormatter},
			                  {field : 'importNo',title : '单据编号',width : 180},
			                  {field : 'sPoNo',title : '合同号',width : 120,align:'left'},
			                  {field : 'supplierNo',title : '供应商编码',width : 120,align:'left'},
			                  {field : 'supplierName',title : '供应商',width : 180,align:'left'},
			                  {field : 'orderDate',title : '发货日期',width : 80},
			                  {field : 'transNo',title : '厂商入库单号',width : 180,align:'left'},
			                  {field : 'requestDate',title : '预到货日期',width : 80},
			                  {field : 'ownerNo',title : '货主',width : 120,align:'left', formatter: wms_city_common.columnOwnerFormatter},
			                  {field : 'businessType',title : '业务类型',width : 120,align:'left', formatter: billimimport.businessTypesFormatter},
			                  {title:'商品总数',field:'sumQty',width:100,align:'right'},
			                  {title:'计划箱数',field:'boxNoQty',width:100,align:'right'},
			                  {title:'已收箱数',field:'receiptBoxNoQty',width:100,align:'right'},
			                  {field : 'creator',title : '创建人',width : 100,align:'left'},
			                  {field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 125,sortable:true},
			                  {field:'auditor',title : '审核人',width : 100,align:'left'},
			                  {field:'auditorName',title : '审核人名称',width : 100,align:'left'},
 			                  {field:'audittm',title:'审核时间',width:125,sortable:true},
			                  {field : 'editor',title : '更新人',width : 100,align:'left'},
			                  {field : 'editorName',title : '更新人名称',width : 100,align:'left'},
			                  {field : 'edittm',title : '更新时间',width : 125,sortable:true}
			                 ]"   
			                 jsonExtend='{
		                     	//url:BasePath+"/billimimport/list.json",
					           	//method:"post",
		    				   //	queryParams:{"locno": billimimport.locno,
				                // "orderDateBegin": billimimport.getDate(-6),
				                // "orderDateEnd": billimimport.getDate(0)
						       //	},
						       	onLoadSuccess:function(data){//合计
						       		billimimport.onLoadSuccess(data);
						       	},
		                     	onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  billimimport.showView(rowData,rowIndex);
				                }
		                     }'
				           />
               </div>
         </div>
    </div>
<#-- 新增  窗口 -->
<div id="showDialog"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false" style="height:150px;">
		    			<#-- 工具菜单div -->
		            <@p.toolbar id="addtoolbar"   listData=[
		                         {"id":"btn-add-add","title":"保存","iconCls":"icon-save","action":"billimimport.saveMain()", "type":1},
		                         {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billimimport.modifyFloc('dataForm')", "type":2},
		                         {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billimimport.closeShowWin('showDialog')","type":0}
		                       ]
					  />
			         <div class="search-div">
			         		<form name="dataForm" id="dataForm" method="post" class="city-form">
						     	<table>
			                 		<tr>
			                 			<td class="common-td blank">单据编号：</td>
			                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNo_dataForm" required="true"  readOnly='readOnly'  missingMessage='单据编号为必填项!' /></td>
			                 			<td class="common-td blank">合&nbsp;同&nbsp;号：</td>
			                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="poNo_dataForm" /></td>
			                 			<td class="common-td blank">业务类型：</td>
			                 			<td>
			                 				<input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType_dataForm" required="true" data-options="editable:false,onChange:function(data){
																																						billimimport.loadSupplierNo(data,'supplierNo_dataForm',false);
																																					}"/>
			                 				<input class="easyui-validatebox ipt" style="width:120px" name="businessType" id="businessTypeHide_dataForm" type="hidden"/>
			                 			</td>
			                 			<td class="common-td blank"> 供&nbsp;应&nbsp;商：</td>
			                 			<td>
			                 				<input class="easyui-combobox ipt" style="width:120px" name="supplierNo" id="supplierNo_dataForm" required="true"/> 
							           		<input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoHide_dataForm" type="hidden"/>
			                 			</td>
			                 		</tr>
			                 		<tr>
			                 			<td class="common-td blank">发货日期：</td>
			                 			<td><input class="easyui-datebox ipt" style="width:120px" name="orderDate" id="orderDate_dataForm"/> </td>
			                 			<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
			                 			<td>
			                 				<input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNo_dataForm" required="true" data-options="editable:false"/>
										    <input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNoHide_dataForm" type="hidden"/>
			                 			</td>
			                 			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
			                 			<td>
			                 				<input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo_dataForm" required="true" data-options="editable:false"/>
										  	<input class="easyui-validatebox ipt" style="width:120px" name="sysNo" id="sysNoHide_dataForm" type="hidden"/>
			                 			</td>
			                 			<td class="common-td blank">预到货日期：</td>
			                 			<td><input class="easyui-datebox ipt" style="width:120px" name="requestDate" id="requestDate_dataForm"/></td>
			                 		</tr>
			                 		<tr>
			                 			<td class="common-td blank">厂商入库单号：</td>
			                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNo_dataForm"  data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
			                 			<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			                 			<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="importRemark" id="importRemark_dataForm"/></td>
			                 		</tr>
			                    </table>
				 			</form>
			         </div>
		        </div>
			     <div data-options="region:'center',border:false">
			     		<div id = 'detailDiv'>
			     			<@p.toolbar id="add_detail_detail"   listData=[
		                         {"title":"新增明细","iconCls":"icon-add-dtl","action":"billimimport.addBoxDetail_module('module')", "type":0},
		                         {"title":"删除明细","iconCls":"icon-del-dtl","action":"billimimport.removeBySelected('module')", "type":0},
		                         {"title":"保存明细","iconCls":"icon-save-dtl","action":"billimimport.save('module')","type":0}
		                       ]
					  		/>
			     		</div>
				 		<@p.datagrid id="module" name=""   loadUrl="" saveUrl="" defaultColumn="" title="预到货通知单明细"
				 			isHasToolBar="false" divToolbar="#detailDiv"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
							pagination="true" rownumbers="true" emptyMsg="" 
				 			columnsJsonList="[
				 				{field:'id',title:'编号',width:70,hidden:true
				 				},
				 				{field:'boxNo',title:'箱号',width:125,align:'left'},
				 				{field:'deliverNo',title:'装车单号',width:125,align:'left',
				 					editor:{
				 						type:'validatebox',
				 						options:{
					 						required:true,
					 						missingMessage:'装车单号为必填项!'
				 						}
				 					}
				 				},
				 				{field:'carPlate',title:'车牌号',width:125,align:'left',
				 					editor:{
				 						type:'validatebox',
				 						options:{
					 						required:true,
					 						missingMessage:'车牌号为必填项!'
				 						}
				 					}
				 				},
							    {field:'importQty',title:'数量',width:60,align:'right'}
				 			]"
				 		/>
		        </div>
		    </div>
</div>	

<#-- 修改/查看 窗口 -->
<div id="showEditDialog" class="easyui-dialog" title="修改"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 


	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<#-- 工具菜单div -->
	               <@p.toolbar  id="edittoolbar"  listData=[
	                             {"title":"修改","iconCls":"icon-edit","action":"billimimport.modifyFloc('dataEditForm')", "type":2},
	                             {"title":"取消","iconCls":"icon-cancel","action":"billimimport.closeShowWin('showEditDialog')", "type":0}
		                       ]
					  />
				<div style="padding:8px;">
					<form name="dataEditForm" id="dataEditForm" method="post" class="city-form">
			     		<table>
	                 		<tr>
	                 			<td  class="common-td blank">单据编号：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNo_dataEditForm" required="true"  readOnly='readOnly'  missingMessage='单据编号为必填项!' /></td>
	                 			<td class="common-td blank">合同号：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="poNo_dataEditForm" /></td>
	                 			<td class="common-td blank">业务类型：</td>
			                 	<td>
			                 		<input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType_dataEditForm" required="true" data-options="editable:false,onChange:function(data){
																																						billimimport.loadSupplierNo(data,'supplierNo_dataEditForm',false);
																																					}"/>
			                 		<input class="easyui-validatebox ipt" style="width:120px" name="businessType" id="businessTypeHide_dataEditForm" type="hidden"/>
			                 	</td>
	                 			<td class="common-td blank"> 供应商：</td>
	                 			<td>
	                 				<input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNo_dataEditForm" required="true"/> 
				           			<input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoHide_dataEditForm" type="hidden"/>
	                 			</td>
	                 			</tr>
	                 		<tr>
	                 			<td  class="common-td blank">发货日期：</td>
	                 			<td><input class="easyui-datebox" style="width:120px" name="orderDate" id="orderDate_dataEditForm"/></td>
	                 			<td class="common-td blank">货主：</td>
	                 			<td>
	                 				<input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo_dataEditForm" required="true" />
							 		<input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNoHide_dataEditForm" type="hidden"/>
	                 			</td>
	                 			<td class="common-td blank">品牌库：</td>
	                 			<td>
	                 				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo_dataEditForm" required="true" />
							  		<input class="easyui-validatebox ipt" style="width:120px" name="sysNo" id="sysNoHide_dataEditForm" type="hidden"/>
	                 			</td>
	                 			<td class="common-td blank">预到货日期：</td>
	                 			<td><input class="easyui-datebox" style="width:120px" name="requestDate" id="requestDate_dataEditForm" /></td>
	                 		</tr>
	                 		<tr>
								<td class="common-td blank">厂商入库单号</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNo_dataEditForm"  data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
			                 	<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			                 	<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="importRemark" id="importRemark_dataEditForm"/></td>
			                </tr>
	                    </table>
				 	</form>
				</div>
		</div>
		<div data-options="region:'center',border:false">
	 			 <@p.toolbar  id="zoneInfoToolEditDiv"  listData=[
                             {"id":"btn-add-edit","title":"新增明细","iconCls":"icon-add-dtl","action":"billimimport.addBoxDetail_module('moduleEdit')", "type":0},
                             {"id":"btn-over-edit","title":"删除明细","iconCls":"icon-del-dtl","action":"billimimport.removeBySelected('moduleEdit')", "type":0},
                             {"id":"btn-canle-edit","title":"保存明细","iconCls":"icon-save-dtl","action":"billimimport.save('moduleEdit')", "type":0}
	                       ]
				  />
		 		<@p.datagrid id="moduleEdit" name="" title="预到货通知单明细" loadUrl="" saveUrl="" defaultColumn="" 
		 			isHasToolBar="false"  divToolbar="#zoneInfoToolEditDiv"   onClickRowEdit="true" singleSelect="true" pageSize='20'  
					pagination="true" rownumbers="true" emptyMsg=""
		 			columnsJsonList="[
		 				{field:'id',title:'编号',width:70,hidden:true},
		 				{field:'boxNo',title:'箱号',width:125,align:'left'},
		 				//{field:'brandNo',title:'品牌',width:125,
						//	editor:{
		 				//		type:'validatebox',
		 				//		options:{
		 				//		  required:true,
		 				//		  missingMessage:'品牌为必填项!'
		 				//		}
		 				//	}
		 				//},
		 				{field:'deliverNo',title:'装车单号',width:125,align:'left',
		 					editor:{
		 						type:'validatebox',
		 						options:{
			 						required:true,
			 						missingMessage:'装车单号为必填项!'
		 						}
		 					}
		 				},
		 				{field:'carPlate',title:'车牌号',width:125,align:'left',
		 					editor:{
		 						type:'validatebox',
		 						options:{
			 						required:true,
			 						missingMessage:'车牌号为必填项!'
		 						}
		 					}
		 				},
					    {field:'importQty',title:'数量',width:60,align:'right'}
		 			]"
		 		/>
			 </div>
		</div>    
		   
			
</div>
	
<#-- 箱号选择div -->
<div id="openUIItem"  class="easyui-dialog" title="选择箱号"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
	    <div class="easyui-layout" data-options="fit:true">
	    	<div data-options="region:'north',border:false" >
	    		<@p.toolbar  id="boxtoolbar"  listData=[
                             {"id":"btn-add-edit","title":"查询","iconCls":"icon-search","action":"billimimport.searchBox()", "type":0},
                             {"id":"btn-add-edit","title":"确定","iconCls":"icon-ok","action":"billimimport.confirmBox()", "type":0},
                             {"id":"btn-add-edit","title":"取消","iconCls":"icon-cancel","action":"billimimport.closeShowWin('openUIItem')", "type":0}
	                       ]
				  />
	    		
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<div id="itemTools">
		    		<form name="itemSearchForm" id="itemSearchForm" metdod="post" >
		    			箱号：<input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="BOXNoHide" />
					 	<input class="easyui-validatebox ipt" style="width:130px" name="showGirdName" id="showGirdName"   disable="true"  type ="hidden" />
					 	<input class="easyui-validatebox ipt" style="width:120px" name="locno" id="locnoBox" type ="hidden"/> 
					 	<input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNoBox" type ="hidden"/> 
					 	<input class="easyui-validatebox ipt" style="width:130px" name="sysNo" id="sysNoForItem"   type ="hidden" />
					 	<input type="hidden" name="supplierNo" id="supplierNoHidden"/>
				 	</form>
			 	</div>
				  <@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
	               isHasToolBar="false"  height="450"  onClickRowEdit="false" singleSelect="false" pageSize='20'  
				   pagination="true" rownumbers="true" divToolbar="#itemTools"
		           columnsJsonList="[
	           		{field : 'ck',title : '',width : 50, checkbox:true},
	           		{field : 'boxNo',title : '箱号 ',width : 100,align:'left'},
	                {field : 'ownerName',title : '货主',width : 100,align:'left'},
	                {field : 'qty',title : '数量',width : 90,align:'right'}
	            ]" 
		    />
	    	</div>
	   	</div>
</div>


<#-- 查看详情窗口 - 带尺码横排  -->
<div id="showViewDialog"  class="easyui-dialog" title="查看" 
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false" style="overflow: hidden;"> 
		      <div class="easyui-layout" data-options="fit:true">
		     		<div data-options="region:'north',border:false">
		     			<@p.toolbar  id="printtoolbar"  listData=[
	                             {"id":"btn-add-edit","title":"打印预览","iconCls":"icon-print","action":"billimimport.printBillImImport2()", "type":0}
		                       ]
					  	/>
					  	<div style="padding:8px;">
						     <form name="dataViewForm" id="dataViewForm" method="post" class="city-form">
						     	<table>
				                 		<tr>
				                 			<td class="common-td">单据编号：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNo_dataViewForm" required="true"  readOnly='readOnly'  missingMessage='单据编号为必填项!' /></td>
				                 			<td class="common-td blank">合同号：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="sPoNo" id="poNo_dataViewForm" /></td>
				                 			<td class="common-td blank">业务类型：</td>
			                 				<td>
						                 		<input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType_dataViewForm" required="true" data-options="editable:false,onChange:function(data){
																																						billimimport.loadSupplierNo(data,'supplierNo_dataViewForm',false);
																																					}"/>
						                 		<input class="easyui-validatebox ipt" style="width:120px" name="businessTypeHide" id="businessTypeHide_dataViewForm" type="hidden"/>
						                 	</td>
				                 			<td class="common-td blank"> 供应商：</td>
				                 			<td>
				                 				<input class="easyui-combobox" style="width:120px" name="supplierNo" id="supplierNo_dataViewForm" required="true" />
				                 				<input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoHide_dataViewForm" type="hidden"/>
				                 			</td>
				                 			</tr>
				                 		<tr>
				                 			<td class="common-td">发货日期：</td>
				                 			<td><input class="easyui-datebox" style="width:120px" name="orderDate" id="orderDate_dataViewForm"  required="true"/> </td>
				                 			<td class="common-td blank">货主：</td>
				                 			<td>
				                 				<input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo_dataViewForm" required="true" />
				                 				<input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNoHide_dataViewForm" type="hidden"/>
				                 			</td>
				                 			<td class="common-td blank">品牌库：</td>
				                 			<td>
				                 				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo_dataViewForm" required="true" />
										 		<input class="easyui-validatebox ipt" style="width:120px" name="sysNo" id="sysNoHide_dataViewForm" type="hidden"/>
				                 			</td>
				                 			<td class="common-td blank">预到货日期：</td>
				                 			<td><input class="easyui-datebox" style="width:120px" name="requestDate" id="requestDate_dataViewForm"  required="true"/></td>
				                 		</tr>
				                 		<tr>
			                 				<td class="common-td blank">厂商入库单号</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNo_dataViewForm"  data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
			                 				<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			                 				<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="importRemark" id="importRemark_dataViewForm"/></td>
			                 			</tr>
				                    </table>
							 </form>	
						</div>
		     		</div>
		     		<div data-options="region:'center',border:false" >
		     			<div id="main_imimport_DetailId" class="easyui-tabs" data-options="fit:true,border:false">   
							<div id = 'detailViewDiv' title='尺码横排' > 
								 <@p.datagrid id="moduleView" name="" loadUrl="" saveUrl="" defaultColumn=""  
								 		isHasToolBar="false"  columnsJsonList=""  divToolbar="#"  onClickRowEdit="false" singleSelect="true" pageSize='20'   
										pagination="true" rownumbers="true"  jsonExtend="{}" height="500" showFooter="true"  emptyMsg=""
								 /> 
							</div>
							<div id = 'detailViewDiv_dtl' title='明细' >
								 <@p.datagrid id="moduleView_dtl" name="" loadUrl="" saveUrl="" defaultColumn="" 
							 			isHasToolBar="false"  height="500"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
										pagination="true" rownumbers="true" showFooter="true"
							 			columnsJsonList="[
						                   {title:'商品编码',field:'itemNo',width:150},
						                   {title:'颜色',field:'colorName',width:80,align:'left'},
						                   {title:'尺码',field:'sizeNo',width:100,align:'left'},
						                   {title:'商品名称',field:'itemName',width:150,align:'left'},
						                   {title:'箱号',field:'boxNo',width:120,align:'left'},
						                   {title:'装车单号',field:'deliverNo',width:120,align:'left'},
						                   {title:'品牌',field:'brandName',width:100,align:'left'},
						                   {title:'计划入库数量',field:'poQty',width:100,align:'right'},
						                   {title:'收货数量',field:'receiptQty',width:100,align:'right'},
						                   {title:'验收数量',field:'importQty',width:100,align:'right'},
						                   {title:'差异数量',field:'differQty',width:100,align:'right'}
							 			]"
							 		/>
							</div>
						</div>
		     		</div>
		      </div>
		      
</div>	
<#--  预到货通知单  -->
</body>
</html>