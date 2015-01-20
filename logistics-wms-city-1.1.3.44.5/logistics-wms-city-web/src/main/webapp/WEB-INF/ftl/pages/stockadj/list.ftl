<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>库存调整</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/stockadj/stockadj.js?version=1.0.8.7"/>"></script>
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
    </object>
</head>
<body class="easyui-layout">
      <#-- 工具菜单div开始 -->
      <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
               <@p.toolbar  id="toolBarOne" listData=[
               				 {"title":"查询","iconCls":"icon-search","action":"stockadj.searchStockadj();","type":0},
       					 	 {"title":"清空","iconCls":"icon-remove","action":"stockadj.searchStockadjClear();","type":0},
                             {"title":"新增","iconCls":"icon-add","action":"stockadj.showAdd()", "type":1},
                             {"title":"修改","iconCls":"icon-edit","action":"stockadj.showModify()","type":2},
                             {"title":"删除","iconCls":"icon-del","action":"stockadj.deleteadj()","type":3},
							 {"title":"审核","iconCls":"icon-aduit","action":"stockadj.examineAdj();","type":4},
							 {"title":"打印预览","id":"printBtn","iconCls":"icon-print","action":"stockadj.printDetail()", "type":2},
					         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
	                       ]
				  />
	  </div>
<#--工具菜单div结束-->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
	  		<div data-options="region:'north',border:false" >
	       		<div class="search-div" id="search-div">
	       		 <form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td blank">状态：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
							<td class="common-td blank">创建人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
							<td class="common-td blank">创建日期：</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="createtmStart" id="createtmBeginCondition" /></td>
							<td class="common-line">&nbsp;&mdash;&nbsp;&nbsp;</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="createtmEnd" id="createtmEndCondition" data-options="validType:['vCheckDateRange[\'#createtmBeginCondition\',\'结束日期不能小于开始日期\']']"/></td>
						</tr>
						<tr>
						<td class="common-td blank blank">单据编号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="adjNo" id="importNoCondition" /></td>
							<td class="common-td blank">审核人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition"  /></td>
							<td class="common-td blank">审核日期：</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="audittmStart" id="audittmBeginCondition" /></td>
							<td class="common-line">&nbsp;&mdash;&nbsp;&nbsp;</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="audittmEnd" id="audittmEndCondition" data-options="validType:['vCheckDateRange[\'#audittmBeginCondition\',\'结束日期不能小于开始日期\']']"/> </td>
						</tr>
						<tr>
							<td class="common-td blank">调整类型：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="adjType" id="adjtypeCondition" /></td>
							<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
	             			<td class="common-td blank">品牌：</td>
							<td colspan="3"><input class="easyui-combobox ipt" style="width:264px" name="brandNo" id="brandNoCondition" /></td>
						</tr>
						<tr>
						    <td class="common-td blank">来源单号：</td>
						    <td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNoCondition"/></td>
						    <td class="common-td blank">来源类型：</td>
						    <td><input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceTypeCondition"/></td>
						</tr>
					</table>
				</form>
			 </div>
       	</div>	
      <#-- 显示数据列表div -->
<div data-options="region:'center',border:false">
      <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl="" defaultColumn="" title="库存调整"
		                   isHasToolBar="false" height="460"  onClickRowEdit="false" pagination="true"
			               rownumbers="true" emptyMsg="" singleSelect = "false"  divToolbar="" showFooter="true"
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 50, checkbox:true},
			                  {field : 'status',title : '状态',width : 80,formatter:stockadj.formatterStatus,align:'left'},
			                  {field : 'adjNo',title : '单据编号',width : 180},
			                  {field : 'ownerNo',title : '货主',width : 80,formatter:stockadj.ownerNoFormatter,align:'left'},
			                  {field : 'adjType',title : '调整类型',width : 80,formatter:stockadj.formatteradjType},
			                  {field : 'sPoNo',title : '调整结果',width : 130,formatter:stockadj.formatterResult,align:'left'},
			                  {field : 'adjQty',title : '调整数量',width : 80,align:'right'},
			                  {field : 'sourceNo',title : '来源单号',width : 150},
                              {field : 'sourceType',title : '来源类型',width : 80,formatter:stockadj.formattersourceType},
			                  {field : 'cellAdjFlag',title : '整储位调整',width : 80,align:'left',formatter:stockadj.forcellAdjFlag},
			                  {field : 'creator',title : '创建人',width : 80,align:'left'},
			                  {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 130},
			                  {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                  {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                  {field : 'audittm',title : '审核时间',width : 130}
			                 ]"   
			                 jsonExtend='{
						       	  onLoadSuccess:function(data){//合计
							    	stockadj.onLoadSuccess(data);
							    },
		                     	onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  stockadj.showDetail(rowData,rowIndex);
				                }
		                     }'
				           />
		</div> 
	</div>
</div>

<#-- 查看明细信息div BEGIN -->
	<div id="stockAdjDtlInfo"  class="easyui-dialog" title="明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="viewtoolbar"   listData=[
		    		{"title":"导出","iconCls":"icon-export","action":"stockadj.exportDtl();","type":0},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"stockadj.closeWindow('stockAdjDtlInfo');","type":0}
		        ]/>
				<div nowrap id="form_detail" class="search-div" style="padding:10px;">
					<form name="stockAdjDtlInfo" id="stockAdjDtlInfo" method="post" class="city-form">
						<table>
					   		<tr>
					   		<td class="common-td">单据编号：</td>
					   		<td><input class="easyui-validatebox ipt" style="width:120px" name="adjNo" id="adjNo" readOnly="true"/></td>
					   		<td class="common-td blank">调整类型：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="adjType" id="adjType" readOnly="true"/></td>
					   		<td class="common-td blank">调&nbsp;整&nbsp;前：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="adjTypeb" id="ajdTypebefore" readOnly="true"/></td>
					   		<td class="common-td blank">调&nbsp;整&nbsp;后：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="adjTypel" id="adjTypeLate" readOnly="true"/></td>
					   		</tr>
					   		<tr>
					   		<td class="common-td blank">整储位调整：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="cellAdjFlag" id="cellAdjFlag" readOnly="true"/></td>
					   		<td class="common-td">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="owner" id="ownerid"  readOnly="true"/></td>
					   		<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					   		<td colspan="3"><input class="easyui-validatebox ipt" style="width:305px" name="remark" id="remarkid" readOnly="true"/></td>
					   		</tr>
						</table>
					</form>	
				</div>
				
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">	
	   			<@p.datagrid id="dataGridLU_Dtl"  loadUrl="" saveUrl=""  defaultColumn=""   title="明细信息"
	       		isHasToolBar="false"  onClickRowEdit="false"    pagination="true" emptyMsg="" showFooter="true"
	      	 	rownumbers="true"
	       		divToolbar=""
	       		columnsJsonList="[
	         	 {field : 'cellNo',title : '储位编码',width : 100,align:'left'},	
	         	 {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
	         	 {field : 'itemName',title : '商品名称',width : 150,align:'left'},
	         	 {field : 'sizeNo',title : '尺码',width : 70,align:'left'},
	         	 {field : 'color',title : '颜色',width : 70},
	         	 {field : 'brandName',title : '品牌',width : 70,align:'left'},
	         	 {field : 'qualityStr',title : '品质',width : 70,align:'left'},
	         	 {field : 'itemTypeStr',title : '商品属性',width:70,align:'left'},	
	         	 {field : 'adjQty',title : '调整数量',width : 70,align:'right'},
	         	 {field : 'labelNo',title : '箱号',width : 130,align:'right'}
	         ]"/>
			</div>
			<#--显示列表end-->	
			</div>
		</div>
		<#-- 查看明细信息div END -->
		

<#--修改/新增窗口开始-->
<div id="addWindow"  class="easyui-window" title=""
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
	<div class="easyui-layout" data-options="fit:true">
		    <div data-options="region:'north',border:false">
		    <#-- 工具菜单div -->
	        	<div style="margin-bottom:0px" id="toolDivTow">
	               <@p.toolbar id="toolBarTow"  listData=[
	                             {"id":"btn-add","title":"保存","iconCls":"icon-save","action":"stockadj.saveStockAdjInfo()", "type":0},
	                             {"id":"btn-modify","title":"修改","iconCls":"icon-edit","action":"stockadj.updateStockAdj();", "type":0},
	                             {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"stockadj.closeAddWindow();","type":0}
		                       ]
					  />
				</div>
			    <div style="padding:8px;">
				   <form name="stockAdjDtlInfoAdd" id="stockAdjDtlInfoAdd" method="post" class="city-form">
					   	<table>
					   		<tr>
					   		<td class="common-td">单据编号：</td>
					   		<td><input class="easyui-validatebox ipt" style="width:120px" name="adjNo" id="adjNoupdate" readOnly="true"/></td>
					   		<td class="common-td blank">调整类型：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="adjType" id="adjTypeupdate" data-options="required:true"/></td>
					   		<td class="common-td blank">调&nbsp;整&nbsp;前：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="sItemType" id="ajdTypebeforeupdate" data-options="required:true"/></td>
					   		<td class="common-td blank">调&nbsp;整&nbsp;后：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="dItemType" id="adjTypeLateupdate" data-options="required:true"/></td>
					   		</tr>
					   		<tr>
					   		<td class="common-td blank">整储位调整：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="cellAdjFlag" id="cellAdjFlagUpdate" data-options="required:true"/></td>
					   		<td class="common-td">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
					   		<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="owneridupdate" data-options="required:true"/></td>
					   		<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					   		<td colspan="3"><input class="easyui-validatebox ipt" style="width:305px" name="remark" id="remarkupdate"/></td>
					   		</tr>
					   	</table>
					</form>	
				</div>
			</div>
			<div id="editDtl"  data-options="region:'center',border:false">
				<@p.toolbar id="toolBarTwoThree"   listData=[
									     {"title":"按容器新增明细","id":"itemAddOneBtn","iconCls":"icon-add-dtl","action":"stockadj.showConListWindow()", "type":0},
									     {"title":"按零散新增明细","id":"itemAddTwoBtn","iconCls":"icon-add-dtl","action":"stockadj.showItemWindow()", "type":0},
									     {"title":"新增明细","id":"itemAddThreeBtn","iconCls":"icon-add-dtl","action":"stockadj.showItemWindow()", "type":0},
									     {"title":"删除明细","id":"itemDelBtn","iconCls":"icon-del-dtl","action":"stockadj.removeBySelected('dataGridLU_DtlForAdd')", "type":0},
									     {"title":"保存明细","id":"itemSaveBtn","iconCls":"icon-save-dtl","action":"stockadj.saveStockAdjDtl();","type":0},
							       		 {"title":"模板下载","id":"downBtn","iconCls":"icon-download","action":"stockadj.downloadTemp()","type":0},
						                 {"title":"导入","id":"explortBtn","iconCls":"icon-import","action":"stockadj.showImport('stockAdjDtlInfoAdd','showDialog')","type":0}
									     ]
									/>
			    <@p.datagrid id="dataGridLU_DtlForAdd"  loadUrl="" saveUrl=""  defaultColumn=""   title="调整库存明细"
			       isHasToolBar="false"  height="300"  onClickRowEdit="false"  onClickRowAuto="false"  pagination="true"  divToolbar="#toolBarTwoThree"
			       rownumbers="true" emptyMsg=""
			       columnsJsonList="[
			          	 {field : 'cellNo',title : '储位编码',width : 100,align:'left'},	
			         	 {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
			         	 {field : 'itemName',title : '商品名称',width : 150,align:'left'},
			         	 {field : 'sizeNo',title : '尺码',width : 70,align:'left'},
			         	 {field : 'color',title : '颜色',width : 70,align:'left'},
			         	 {field : 'brandName',title : '品牌',width : 70,align:'left'},
			         	 {field : 'quality',title : '品质',width : 70,formatter:stockadj.formatterQuality,align:'left'},
         	 			 {field : 'itemType',title : '商品属性',width:70,formatter:stockadj.formatterItemType,align:'left'},	
			         	 {field : 'packQty',title : 'packQty',hidden:true},
			          	 {field : 'adjQty',title : '调整数量',width : 70,align:'left',
			                 editor:{
				             type:'numberbox',
				             options:{
				                required:true,
				                validType:[\"vLength[0,10,'最多只能输入10个数字']\"],
				                missingMessage:'项目值为必填项且只能为数字!'
				             }
				          }
			          },
			          {field : 'labelNo',title : '箱号',width : 130,align:'left'}
			         ]"
			 		jsonExtend='{onClickRow:function(rowIndex, rowData){
                	  // 触发点击方法  调JS方法
                   	 stockadj.onClickRowDtl(rowIndex,rowData);
                }}'/>
			</div> 
		</div>
</div>
<#--修改/新增窗口结束-->
<#--弹出商品选择窗口开始-->
<div id="item" class="easyui-window" title="商品选择" data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		   <div data-options="region:'north',border:false">
			     <div style="margin-bottom:0px" id="toolDivThree">
	               <@p.toolbar id="toolBarThree"  listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"stockadj.showItem()", "type":0},
	                             {"title":"清空","iconCls":"icon-remove","action":"stockadj.clearItem();", "type":0},
	                             {"title":"确认","iconCls":"icon-ok","action":"stockadj.checkItem();","type":0},
	                             {"title":"取消","iconCls":"icon-cancel","action":"stockadj.cancleItem();","type":0}
		                       ]
					  />
				 </div>
			<div style="padding:8px;">
			   <form name="itemform" id="itemform" method="post" class="city-form">
				   	<table>
				   		<tr>
				   			<td class="common-td blank">商品类型：</td>
							<td><input class="easyui-combobox" style="width:120px" name="itemType" id="itemTypeItem"/></td>
	                     	<td class="common-td blank">季节：</td>
	                     	<td ><input class="easyui-combobox ipt" style="width:120px" name="seasonStr" id="seasonItem" /></td>
	                     	<td class="common-td blank">性别：</td>
	                     	<td ><input class="easyui-combobox ipt" style="width:120px" name="genderStr" id="genderItem" /></td>
	                     	<td class="common-td blank">年份：</td>
	                     	<td ><input class="easyui-validatebox ipt" style="width:120px" name="yearsStr" id="yearsItem" /></td>		   		
				   		</tr>
				   		<tr>
				   			<td class="common-td blank">储位：</td>
				   			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" /></td>
				   			<td class="common-td blank">商品编码：</td>
				   			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" /></td>
				   			<td class="common-td blank">商品名称：</td>
				   			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName"/></td>
				   			<td class="common-td blank">商品条码：</td>
				   			<td><input class="easyui-validatebox ipt" style="width:120px" name="barCode" /></td>
				   		</tr>
				   		<tr>
				   			<td class="common-td blank">商品品质：</td>
							<td><input class="easyui-combobox" style="width:120px" name="quality" id="qualityItem"/></td>
                 			<td class="common-td blank">大类一：</td>
                 			<td><input class="easyui-combobox" style="width:120px" name="cateOne" id="cateOneItem" /></td>
                 			<td class="common-td blank">大类二：</td>
                 			<td><input class="easyui-combobox" style="width:120px" name="cateTwo" id="cateTwoItem" /></td>
                 			<td class="common-td blank">大类三：</td>
                 			<td><input class="easyui-combobox" style="width:120px" name="cateThree" id="cateThreeItem" /></td>
                     	</tr>
				   		<tr>
							<td class="common-td blank">品牌库：</td>
				   			<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoId"/></td>
							<td class="common-td blank">所属品牌：</td>
							<td colspan="3"><input class="easyui-combobox ipt" style="width:264px" name="brandNo" id="brandNoItem" /></td>
							<td class="common-td blank"></td>
							<td></td>
							<td class="common-td blank"></td>
							<td></td>
				   		</tr>
				   		
				   	</table>
				 </form>
			</div>
		</div>		
	<div data-options="region:'center',border:false">			
    <@p.datagrid id="itemdg"  loadUrl="" saveUrl=""  defaultColumn=""   title="商品选择"
       isHasToolBar="false"  height="300"  onClickRowEdit="false"    pagination="true"
       rownumbers="true" singleSelect = "false" emptyMsg=""
       divToolbar=""
       columnsJsonList="[
       		 {field : 'ck',title : '',width : 50, checkbox:true},
         	 {field : 'cellNo',title : '储位编码',width : 80,align:'left'},
         	 {field : 'itemNo',title : '商品编码',width :150,align:'left'},
         	 {field : 'itemName',title : '商品名称',width :150,align:'left'},
         	 {field : 'sizeNo',title : '尺码',width : 60,align:'left'},
         	 {field : 'sysNOName',title : '品牌库',width : 70,align:'left'},
         	 {field : 'sysNo',title : '品牌',width : 70,align:'left'},
         	 {field : 'quality',title : '商品品质',width : 70,formatter:stockadj.formatterQuality,align:'left'},
         	 {field : 'itemType',title : '商品属性',width:70,formatter:stockadj.formatterItemType,align:'left'},	
         	 {field : 'conQty',title : '库存数量',width : 70,align:'right'},
         	 {field : 'barcode',title : '商品条码',width : 150,align:'left'},
         	 {field : 'color',title : '颜色',width : 70,align:'left'},
         	 {field : 'yearsStr',title : '年份',width : 80,align:'left'},
			 {field : 'seasonStr',title : '季节',width : 80,align:'left'},
			 {field : 'genderStr',title : '性别',width : 80,align:'left'},
			 {field : 'adjQty',title : 'adjQty',hidden:true},
         	 {field : 'packQty',title : 'packQty',hidden:true}
         ]"/>
	</div>
</div>
</div>
<#--弹出商品选择窗口结束-->


<#--弹出储位选择窗口开始-->
	<div id="cell_select_dialog"  id="showDialog"  class="easyui-dialog" title="储位选择"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
                     <div data-options="region:'north',border:false">
                     		<@p.toolbar id="celltoolbar" listData=[
			                             {"title":"查询","iconCls":"icon-search","action":"stockadj.searchCell()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"stockadj.clearForm('selectCellSearchForm')", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"stockadj.cellSelectOK()","type":0}
			                             {"title":"取消","iconCls":"icon-cancel","action":"stockadj.closeWindow('cell_select_dialog')","type":0}
				                       ]
							  />
							<div class="search-div">
				         		<form name="selectCellSearchForm" id="selectCellSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td">仓区：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoItem" data-options="
												panelHeight:50,
												valueField:'wareNo',
												textField:'wareName'
												"/>
												</td>
				                 			<td class="common-td blank">库区：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNoItem" data-options="
												panelHeight:50,
												valueField:'value',
												textField:'text'
												"/>
											</td>
				                 			<td class="common-td blank">通道：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNoItem" data-options="
												panelHeight:50,
												valueField:'value',
												textField:'text'
												"/>
											</td>
				                 			<td class="common-td blank">储位：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNoItem" /></td>
				                 		</tr>
				                    </table>
				         		</form>
				         	</div>
                     </div>	
                     <div data-options="region:'center',border:false">
	                     <@p.datagrid id="cell_select_datagrid"  loadUrl=""  saveUrl=""  defaultColumn="" height="300"
			               isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				           rownumbers="true"
				           columnsJsonList="[
				           		{field:'id',checkbox:true},
				           		{field : 'cellNo',title : '储位 ',width : 100,align:'left'},
				                {field : 'stockNo',title : '通道',width : 100,align:'left'},
				                {field : 'areaNo',title : '库区',width : 100,align:'left'},
				                {field : 'wareNo',title : '仓区 ',width : 90,align:'left'}
				            ]"/>
			         </div>
		    </div>
	</div>
<#--弹出储位选择窗口结束-->

<#--弹出容器选择窗口开始-->
	<div id="con_select_dialog"  id="showConNoDialog"  class="easyui-dialog" title="容器选择"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
                     <div data-options="region:'north',border:false">
                     		<@p.toolbar id="contoolbar" listData=[
			                             {"title":"查询","iconCls":"icon-search","action":"stockadj.searchCon()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"stockadj.clearConForm()", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"stockadj.conSelectOK()","type":0}
			                             {"title":"取消","iconCls":"icon-cancel","action":"stockadj.closeWindow('con_select_dialog')","type":0}
				                       ]
							  />
							<div class="search-div">
				         		<form name="selectConSearchForm" id="selectConSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td blank">容器编码：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="conNo" id="conNo" /></td>
				                 			<td class="common-td blank">储位：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNoItem" /></td>
				                 			<td class="common-td blank">商品类型：</td>
											<td><input class="easyui-combobox" style="width:120px" name="itemType" id="conItemTypeItem"/></td>
											<td class="common-td blank">商品品质：</td>
											<td><input class="easyui-combobox" style="width:120px" name="quality" id="conQualityItem"/></td>
											<td class="common-td blank">容器类型：</td>
											<td><input class="easyui-combobox" style="width:120px" name="conType" id="conType"/></td>
				                 		</tr>
				                    </table>
				         		</form>
				         	</div>
                     </div>	
                     <div data-options="region:'center',border:false">
	                     <@p.datagrid id="con_select_datagrid"  loadUrl=""  saveUrl=""  defaultColumn="" height="300"
			               isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				           rownumbers="true"
				           columnsJsonList="[
				           		{field:'id',checkbox:true},
				           		{field : 'conNo',title : '容器编号 ',width : 160,align:'left'},
				                {field : 'conType',title : '容器类型',width : 100,formatter:stockadj.containerTypeFormatter,align:'left'},
				                {field : 'cellNo',title : '储位 ',width : 90,align:'left'}
				            ]"/>
			         </div>
		    </div>
	</div>
<#--弹出容器选择窗口结束-->

	
<div id="showImportDialog"  class="easyui-window" title="导入"
	data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
	minimizable:false,maximizable:false,maximized:false" style="width:450px;" >
		<div class="easyui-layout" data-options="fit:true" style="height:100px;">
			<div data-options="region:'north',border:false">
				<form name="dataViewForm" id="importForm" method="post" class="city-form" style="padding:10px;">
					<table>
						<tr>
							<td colspan="2" style="color:red;">只允许上传后缀为.xlsx、.xls的文件</td>
						</tr>
						<tr>
							<td class="common-td">选择文件：</td>
							<td>
								<iframe src="" id="iframe" frameborder="0" height="25"></iframe>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
</div>
</body>
</html>