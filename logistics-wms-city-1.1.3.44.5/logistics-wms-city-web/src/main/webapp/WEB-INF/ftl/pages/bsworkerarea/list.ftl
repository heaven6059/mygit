<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>人员区域关系维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/bsworkerarea.js?version=1.0.5.0"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bsworkerarea.css"/>
</head>
<body class="easyui-layout">
	    <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	       <@p.toolbar id="toolBarOne"  listData=[
	       					 {"title":"查询","iconCls":"icon-search","action":"bsworkerarea.searchArea();","type":0},
			       			 {"title":"清空","iconCls":"icon-remove","action":"bsworkerarea.searchLocClear();","type":0},
	                         {"title":"新增","iconCls":"icon-add","action":"bsworkerarea.addInfo()","type":1},
	                         {"title":"修改","iconCls":"icon-edit","action":"bsworkerarea.editInfo()","type":2},
	                         {"title":"删除","iconCls":"icon-del","action":"bsworkerarea.del()","type":3},
							 {"title":"导出","iconCls":"icon-export","action":"bsworkerarea()","type":5},
					         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('人员区域关系维护')","type":0}
		                ]
					  />
		 </div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
	 		 <div class="search-div" id="search-div">
	       		 <form name="searchForm" id="searchForm" method="post" class="city-form">
	       		 	<table>
	       		 		<tr>
	       		 			<td class="common-td">员工 ：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="workerNo" id="search_workerNo" data-options="editable:false" /></td>
	       		 			<td class="common-td blank">仓区：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="wareNo" id="search_wareNo" data-options="editable:false,onChange:function(){
																			bsworkerarea.initAreaData('search');
																		}
																		"/></td>
	       		 			<td class="common-td blank">库区：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="areaNo" id="search_areaNo" data-options="editable:false,onChange:function(){
																			bsworkerarea.initStockData('search');
																		}"/></td>
	       		 			<td class="common-td blank">通道：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="stockNo" id="search_stockNo" data-options="editable:false"/></td>
	       		 		</tr>
	       		 	</table>
				</form>
			 </div>
		</div>
	<div data-options="region:'center',border:false">	 
      	 	<@p.datagrid id="dataGridJG"  loadUrl="/bs_worker_area/list.json?locNo=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="人员区域关系维护"
	              isHasToolBar="false" divToolbar="" onClickRowEdit="false"    pagination="true"
		           rownumbers="true"  singleSelect = "false"  height="430" emptyMsg=""
		           columnsJsonList="[
		           		  {field : ' ',checkbox:true},
		           		  {field : 'workerNo',title : '员工编号',width : 100,align:'left'},
		                  {field : 'workerName',title : '员工名称',width : 150,align:'left'},
		                  {field : 'wareNo',title : '仓区编码',width : 80,align:'left'},
		                  {field : 'wareName',title : '仓区名称',width : 120,align:'left'},
		                  {field : 'areaNo',title : '库区编码',width : 100,align:'left'},
		                  {field : 'areaName',title : '库区名称',width : 200,align:'left'},
		                  {field : 'stockNo',title : '通道编码',width : 100,align:'left'},
		                  {field : 'aStockNo',title : '通道名称',width : 100,align:'left'},
		                  {field : 'operateType',title : '作业类型',width : 150,formatter:bsworkerarea.operateTypeFormatter,align:'left'},
		                  {field : 'workPriority',title : '优先级',width :50,align:'right'},
		                  {field : 'creator',hidden:'true'},
		                  {field : 'createtm',hidden:'true'},
		                  {field : 'editor',hidden:'true'},
		                  {field : 'edittm',hidden:'true'}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                            // 触发点击方法  调JS方法
	                   },onDblClickRow:function(rowIndex, rowData){
	                   	//双击方法
	                   	  bsworkerarea.edit(rowData)
	                   }}'/>
	      		</div>     
 		</div>
 </div>
 
 <div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:460px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
	         	<#-- 明细信息div -->
					<table>
						<tr>
							<td style="padding-left:15px;">员工：</td>
							<td colspan="3">
								<input class="easyui-combobox ipt" style="width:120px" name="workerNo" editable="false"  id="workerNo" required="true" data-options="editable:false"/>
								<!--<input class="hide" style="width:120px" name="workerNo" id="workerNoHide" type="hidden"/>-->
							</td>
						</tr>
						<tr>
							<td>仓区：</td>
							<td>
								<input class="easyui-combobox ipt" style="width:120px" name="wareNo" editable="false" id="wareNo" required="true" data-options="editable:false,onChange:function(){
																			bsworkerarea.initAreaData();
																		}"/>
								<!--<input class="hide" style="width:120px" name="wareNo" id="wareNoHide" type="hidden"/>-->
							</td>
							<td style="padding-left:15px;">库区：</td>
							<td>
								<input class="easyui-combobox ipt" style="width:120px" name="areaNo" editable="false" id="areaNo" required="true" data-options="editable:false,onChange:function(){
																			bsworkerarea.initStockData();
																		}"/>
								<!--<input class="hide" style="width:120px" name="areaNo" id="areaNoHide" type="hidden"/>-->
							</td>
						</tr>
						
						<tr>
							<td>通道：</td>
							<td>
								<input class="easyui-combobox" style="width:120px" name="stockNo" editable="false" id="stockNo" required="true" data-options="editable:false,onChange:function(data){
																										    	 bsworkerarea.writeAStockNo(data);
																											 }"/>
								<input class="hide" style="width:120px" name="stockNo" id="stockNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">通道名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="aStockNo" id="aStockNo"  readonly = "true"/></td>
						</tr>
						<tr>
							<td>作业类型：</td>
							<td>
								<input class="easyui-combobox ipt" style="width:120px" name="operateType" editable="false" id="operateType" required="true" data-options="editable:false"/>
								<input class="hide" style="width:120px" name="operateType" id="operateTypeHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">优先级：</td>
							<td>
								<input class="easyui-numberbox ipt" style="height:22px;width:120px" name="workPriority" id="workPriority" data-options="validType:['vLength[0,3,\'最多只能输入3个字符\']']"/>
							</td>
						</tr>
						<tr id="creatorinfo">
							<td>创建人：</td>
							<td id="creator"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="createtm"></td>
							<td colspan="2"></td>
						</tr>
						
						<tr>
							<td colspan="6" style="text-align:center;">
								<a id="info_add" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
								<a id="info_edit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a> 
							</td>
						</tr>
					</table>
			 </form>	
		</div>	
	</div>
</div>
</body>
</html>