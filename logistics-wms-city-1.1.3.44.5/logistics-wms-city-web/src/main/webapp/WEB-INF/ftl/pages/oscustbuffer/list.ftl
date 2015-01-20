<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>客户与暂存区维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/oscustbuffer.js?version=1.0.5.1"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
	<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
		       <@p.toolbar  id="toolBarOne" listData=[
		       					 {"title":"查询","iconCls":"icon-search","action":"oscustbuffer.searchArea();","type":0},
				       			 {"title":"清空","iconCls":"icon-remove","action":"oscustbuffer.searchLocClear();","type":0},
		                         {"title":"新增","iconCls":"icon-add","action":"oscustbuffer.addInfo()","type":1},
		                         {"title":"批量新增","iconCls":"icon-add-dtl","action":"oscustbuffer.addBatchInfo()","type":1},
		                         {"title":"修改","iconCls":"icon-edit","action":"oscustbuffer.editInfo()","type":2},
		                         {"title":"删除","iconCls":"icon-del","action":"oscustbuffer.del()","type":3}
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
	       		 			<td class="common-td blank">仓区：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="wareNo" id="search_wareNo" data-options="
																		onChange:function(){
																			oscustbuffer.initAreaData('search');
																		}
																		"/></td>
	       		 			<td class="common-td blank">库区：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="areaNo" id="search_areaNo" data-options="
																		onChange:function(){
																			oscustbuffer.initStockData('search');
																		}
																		"/></td>
	       		 			<td class="common-td  blank">通道：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="stockNo" id="search_stockNo" data-options="
																		onChange:function(){
																			oscustbuffer.initCellNoData('search');
																		}
																		"/></td>
	       		 			<td class="common-td blank">储位：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="cellNo" id="search_cellNo" /></td>
	       		 		  </tr>
	       		 		  <tr>
	       		 			<td class="common-td  blank">客户代码：</td>
	       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="storeNo" id="search_storeNo"  /></td>
	       		 			<td class="common-td blank">客户名称：</td>
	       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="storeName" id="search_storeName"  /></td>
		   					<td class="common-td blank">卸&nbsp;货&nbsp;点：</td>
							<td><input class="easyui-combobox" style="width:120px" name="circleNo" id="circleNo"/></td>
		   					<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                 			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
 
	       		 		  </tr>
	       		 		</table>
					</form>
				</div>
		 	</div>
			<div id="soneInfoList" data-options="region:'center',border:false">	
      	 		<@p.datagrid id="dataGridJG" loadUrl=""
      	 	 		saveUrl=""   defaultColumn="" title="客户与暂存区维护" height="430"
	         		isHasToolBar="false" divToolbar="" onClickRowEdit="false"    pagination="true"
		     		rownumbers="true"  singleSelect = "false" emptyMsg="" pageNumber=0
		           	columnsJsonList="[
		           		  {field : ' ',checkbox:true},
		           		  {field : 'storeNo',title : '客户代码',width : 80,align:'left'},
		                  {field : 'storeName',title : '客户名称',width : 200,align:'left'},
		                  {field : 'wareNo',title : '仓区编码',hidden:true},
		                  {field : 'wareName',title : '仓区名称',width : 100,align:'left'},
		                  {field : 'areaNo',title : '库区编码',hidden:true},
		                  {field : 'areaName',title : '库区名称',width : 150,align:'left'},
		                  {field : 'stockNo',title : '通道编码',width : 80,align:'left'},
		                  {field : 'aStockNo',title : '通道名称',width : 100,align:'left'},
		                  {field : 'cellNo',title : '储位',width :100,align:'left'},
		                  {field : 'bufferName',title : '暂存区名称',width : 180,align:'left'},
		                  {field : 'useVolumn',title : '可用体积',width :60,align:'right'},
		                  {field : 'useWeight',title : '可用重量',width :60,align:'right'},
		                  {field : 'useBoxnum',title : '可用箱数',width :60,align:'right'},
		                  {field : 'status',title : '状态',width :50,formatter:oscustbuffer.statsuFormatter,align:'left'},
		                  {field : 'creator',title : '创建人',width :60,align:'left'},
		                  {field : 'creatorName',title : '创建人名称',width :80,align:'left'},
		                  {field : 'createtm',title : '创建时间',width :130},
		                  {field : 'editor',hidden:'true'},
		                  {field : 'edittm',hidden:'true'}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                   },onDblClickRow:function(rowIndex, rowData){
	                   		//双击方法
	                   	  	oscustbuffer.edit(rowData,"view");
	                   }}'
	    		/>
			</div>
		</div>
	</div>	    	
	<#-- 编辑div BEGIN -->
	<div id="showDialog"  class="easyui-window" title="编辑"  
		style="width:460px;padding:8px;"   
		data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,minimizable:false"> 
		<form name="dataForm" id="dataForm" method="post">
	    	<#-- 明细信息div -->
			<table>
				<tr height="30" id="storeTypeCon">
					<td>客户类型：</td>
					<td colspan="3">
						<input class="easyui-combobox ipt" style="width:325px" name="storeType" id="storeType"/>
					</td>
				</tr>
				<tr height="30">
					<td>客户代码：</td>
					<td colspan="3">
						<input class="easyui-combobox ipt" data-options="required:true"  style="width:325px" name="storeNo" id="storeNo"/>
					</td>
				</tr>
					<tr height="30" id="storeNameCon">
						<td>客户名称：</td>
						<td colspan="3">
							<input class="easyui-validatebox ipt" textType="text" style="width:325px" name="storeName" id="storeName"/>
						</td>
					</tr>
					<tr height="30">
						<td>仓&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
						<td>
							<input class="easyui-combobox ipt" style="width:120px" name="wareNo"  id="wareNo" data-options="required:true,
																		onChange:function(){
																			oscustbuffer.initAreaData();
																		}
																		"/>
						</td>
						<td style="padding-left:15px;">库&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
						<td>
							<input class="easyui-combobox ipt" style="width:120px" name="areaNo"  id="areaNo" data-options="required:true,
																		onChange:function(){
																			oscustbuffer.initStockData();
																		}
																		"/>
						</td>
					</tr>
					<tr height="30">
						<td>通&nbsp;&nbsp;&nbsp;&nbsp;道：</td>
						<td>
							<input class="easyui-combobox ipt" style="width:120px" name="stockNo"  id="stockNo" data-options="required:true,
																		onChange:function(){
																			oscustbuffer.initCellNoData();
																			oscustbuffer.initAStockData();
																		}
																		"/>
						</td>
						<td style="padding-left:15px;">通道名称：</td>
						<td><input class="easyui-validatebox ipt" textType="text" style="width:120px" name="aStockNo" id="aStockNo" data-options="required:true"/></td>
					</tr>
					<tr height="30">
						<td>储&nbsp;&nbsp;&nbsp;&nbsp;位：</td>
						<td>
							<input class="easyui-combobox ipt" style="width:120px" name="cellNo" id="cellNo" data-options="required:true"/>
						</td>
						<td style="padding-left:15px;">暂存区名称：</td>
						<td> 
							<input class="easyui-validatebox ipt" textType="text" style="width:120px" name="bufferName" id="bufferName" data-options="validType:['vLength[0,30,\'最多只能输入30个字符\']']"/>
						</td>
					</tr>
					<tr height="30">
						<td>可用体积：</td>
						<td>
							<input class="easyui-numberbox ipt" textType="text" style="width:120px" name="useVolumn" id="useVolumn" precision="5" max="9999999999999.99999"/>
						</td>
						<td style="padding-left:15px;">可用重量：</td>
						<td>
							<input class="easyui-numberbox ipt" textType="text" style="width:120px" name="useWeight" id="useWeight" precision="5" max="9999999999999.99999"/>
						</td>
					</tr>
					<tr height="30">
						<td>可用箱数：</td>
						<td>
							<input class="easyui-numberbox ipt" textType="text" style="width:120px" name="useBoxnum" id="useBoxnum" precision="5" max="9999999999999.99999"/>
						</td>
						<td></td>
						<td></td>
					</tr>
					<tr height="50">
						<td colspan="4" style="text-align:center;">
							<a id="info_add" href="javascript:oscustbuffer.save_do();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
							<a id="info_edit" href="javascript:oscustbuffer.edit_do();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
							<a id="closeBtn"  href="javascript:oscustbuffer.closeOpenUIItem('showDialog');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
						</td>
					</tr>
				</table>
			</form>	
		</div>
		
	</div>
	<#-- 编辑div END -->	
	
	<#--批量新增div-->
	<div id="addBatchUI" class="easyui-window" title="批量新增客户暂存区"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	<div class="easyui-layout" data-options="fit:true">
	    		<#-- 1 start -->
				<div data-options="region:'north',border:false">
					<#-- 工具菜单div -->
		            <@p.toolbar id="addtoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"oscustbuffer.searchStoreData()", "type":0},
		            	{"title":"保存","iconCls":"icon-save","action":"oscustbuffer.doBatchSave()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"oscustbuffer.closeWindowTip('addBatchUI')","type":0}
		            ]/>
		            <form name="searchStoreForm" id="searchStoreForm" method="post" class="city-form" style="padding:10px;">
		            	<table>
			   				<tr>
			   					<td class="common-td">暂存区名称：</td>
			   					<td><input class="easyui-validatebox ipt" textType="text" style="width:120px" name="bufferName" id="bufferNameBatch" data-options="validType:['vLength[0,30,\'最多只能输入30个字符\']']"/></td>
			   					<td class="common-td blank">仓区：</td>
			   					<td>
			   						<input class="easyui-combobox ipt" style="width:120px" name="wareNo"  id="wareNoBatch" 
			   							data-options="required:true,onChange:function(){oscustbuffer.initAreaData('batch');}"/>
			   					</td>
			   					<td class="common-td blank">库区：</td>
			   					<td>
			   						<input class="easyui-combobox ipt" style="width:120px" name="areaNo"  id="areaNoBatch" 
			   							data-options="required:true,onChange:function(){oscustbuffer.initStockData('batch');}"/>
			   					</td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">通道：</td>
			   					<td>
			   						<input class="easyui-combobox ipt" style="width:120px" name="stockNo"  id="stockNoBatch" 
			   							data-options="required:true,onChange:function(){oscustbuffer.initCellNoData('batch');oscustbuffer.initAStockData('batch');}"/>
								</td>
			   					<td class="common-td blank">通道名称：</td>
			   					<td><input class="easyui-validatebox ipt" textType="text" style="width:120px" name="aStockNo" id="aStockNoBatch" data-options="required:true"/></td>
			   					<td class="common-td blank">储位：</td>
			   					<td><input class="easyui-combobox ipt" style="width:120px" name="cellNo" id="cellNoBatch" data-options="required:true"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">可用体积：</td>
			   					<td><input class="easyui-numberbox ipt" textType="text" style="width:120px" name="useVolumn" id="useVolumnBatch" precision="5" max="9999999999999.99999"/></td>
			   					<td class="common-td blank">可用重量：</td>
			   					<td><input class="easyui-numberbox ipt" textType="text" style="width:120px" name="useWeight" id="useWeightBatch" precision="5" max="9999999999999.99999"/></td>
			   					<td class="common-td blank">可用箱数：</td>
								<td><input class="easyui-numberbox ipt" textType="text" style="width:120px" name="useBoxnum" id="useBoxnumBatch" precision="5" max="9999999999999.99999"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">客户类型：</td>
								<td colspan='5'>
									<input class="easyui-combobox ipt" style="width:310px" name="storeType" id="storeTypeBatch"
										data-options="required:true,editable:false,onChange:function(){oscustbuffer.changeStoreType('batch');}"/>
								</td>						
			   				</tr>
			   			</table>
					</form>
				</div>
				<#-- 1 end -->
				
				<#-- 2 start-->
				<div data-options="region:'center',border:false" title="">
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'west'" data-options="fit:true" style="width:500px;">
						    <@p.datagrid id="storeDg1" name="" title="待选客户"  
						 			loadUrl="" saveUrl="" defaultColumn=""  singleSelect = "false"
						 			isHasToolBar="false"  divToolbar="" 
						 			onClickRowEdit="false" pagination="true" rownumbers="true" emptyMsg=""
						 			columnsJsonList="[
						 				{field : ' ',checkbox:true},
						 				{field : 'storeNo',title : '客户编码',width :100,align:'left'},
						 				{field : 'storeName',title : '客户名称',width :170,align:'left'}
						 			]"
						 	jsonExtend='{}'/>
						</div>
						<div data-options="region:'center'" >
						    <div class="easyui-layout" data-options="fit:true">
						    	<div data-options="region:'west'" data-options="fit:true" 
						    		style="width:60px;border-left:0px;border-top:0px;border-bottom:0px;">
						    		<div style="text-align:center;padding-top:100px;">
						            	<a class="easyui-linkbutton" href="javascript:oscustbuffer.toRgiht();"> >> </a>
						            	<br/><br/>
						            	<a class="easyui-linkbutton" href="javascript:oscustbuffer.toLeft();"> << </a>
						            </div>
						    	</div>
						        <div data-options="region:'center',border:false" >
						        	<@p.datagrid id="storeDg2" name="" title="已选客户"  singleSelect = "false"
								 			loadUrl="" saveUrl="" defaultColumn="" showFooter="true"
								 			isHasToolBar="false"  divToolbar="" 
								 			onClickRowEdit="false" pagination="false" rownumbers="false" emptyMsg=""
								 			columnsJsonList="[
								 				{field : ' ',checkbox:true},
								 				{field : 'storeNo',title : '客户编码',width :100,align:'left'},
						 						{field : 'storeName',title : '客户名称',width :170,align:'left'}
								 			]"
								 	jsonExtend='{}'/>
						        </div>
						    </div>
						</div>
					</div>
				</div>
				<#-- 2 end-->
				
		</div>
	</div>
	<#--批量新增div-->
	
</body>
</html>