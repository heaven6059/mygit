<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓上架回单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
     <script type="text/javascript" src="${domainStatic}/resources/js/billuminstock/billuminstock.js?version=1.1.1.4"/>"></script>
    <script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/billuminstock.css"/>" />
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
    <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
       <@p.toolbar id="toolBarOne" listData=[
       					 {"title":"查询","iconCls":"icon-search","action":"billuminstock.searchArea();","type":0},
       					 {"title":"清空","iconCls":"icon-remove","action":"billuminstock.searchLocClear();","type":0},
       					 {"title":"修改","iconCls":"icon-edit","action":"billuminstock.showEdit('edit');","type":0},
       					 {"title":"审核","iconCls":"icon-aduit","action":"billuminstock.check();","type":0},
       					 {"title":"打印预览","iconCls":"icon-print","action":"billuminstock.printDetail()","type":5},
				         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架单')","type":0}
	                ]
				  />
	 </div>
	 					<#--
	 	 				 {"title":"新增","iconCls":"icon-add","action":"billuminstock.addInfo()","type":1},
                         {"title":"删除","iconCls":"icon-remove","action":"billuminstock.del()","type":3},
						 {"title":"导出","iconCls":"icon-export","action":"exportExcel()","type":5},
	 					-->
<div data-options="region:'center',border:false">
	<div class="easyui-layout" data-options="fit:true">
		  	<div data-options="region:'north',border:false" >	 
						<div id="searchDiv" style="padding:8px;">
							<form name="searchForm" id="searchForm" method="post" class="city-form">
							<table>
								<tr>
									<td class="common-td blank">状态：</td>
									<td><input class="easyui-combobox" style="width:120px" name="status" id="search_status"/></td>
									<td class="common-td blank">创建人：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="creator" id="search_creator"/></td>
									<td class="common-td blank">创建日期：</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="createtm"/ id="createtm"></td>
									<td class="common-line">&nbsp;&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="createtm_end"/ id="createtm_end"></td>
								</tr>
								<tr>
									<td class="common-td">单据编号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="instockNo"/></td>
									<td class="common-td blank">审核人：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="auditor" id="search_auditor"/></td>
									<td class="common-td blank">审核日期：</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="audittm" id="audittm"/></td>
									<td class="common-line">&nbsp;&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="audittm_end" id="audittm_end"/></td>
									
								</tr>
								<tr>
									<td class="common-td blank">来源单号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNoSearch"/></td>
									<td class="common-td blank">货主：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="search_ownerNo"/></td>
									<td class="common-td blank" >商品类型：</td>
				   					<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemType_search"  style="width:120px"/></td>
									<td class="common-td blank">品质：</td>
									<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>
								</tr>
								<tr>
									<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
									<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
									<td class="common-td blank">所属品牌：</td>
			             			<td  colspan="5">
			             				<input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" />
			             			</td>
		             			</tr>
							</table>
						</form>
					</div>
			</div>
		<div id='r1' class="easyui-panel"  data-options="region:'center',border:false">
          	 	<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl="" defaultColumn="" title="上架单"
		              isHasToolBar="false" divToolbar="" onClickRowEdit="false" pagination="true"
			           rownumbers="true"  singleSelect = "false" height="460" emptyMsg="" showFooter="true"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			           		  {field : 'status',title : '状态',width : 80,formatter:billuminstock.statusFormatter,align:'left'},
			                  {field : 'instockNo',title : '单据编号',width : 180},
			                  {field : 'ownerNo',title : '货主',width : 100,align:'left',formatter:billuminstock.ownerNoFormatter},
			                  {field : 'itemType',title : '商品类型',width : 100,formatter:billuminstock.typeFormatter,align:'left'},
							  {field : 'quality',title : '品质',width : 100,formatter:billuminstock.qualityFormatter,align:'left'},
			                  {field : 'itemQty',title : '计划数量',width : 80,align:'right'},
                              {field : 'realQty',title : '上架数量',width : 80,align:'right'},
			                  {field : 'instockWorker',title : '上架人',width : 150,align:'left'},
			                  {field : 'creator',title : '创建人',width : 120,align:'left'},
			                   {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 150},
			                  {field : 'auditor',title : '审核人',width : 120,align:'left'},
			                  {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                  {field : 'audittm',title : '审核时间',width : 150},
			                  {field : 'editor',hidden:'true'},
			                  {field : 'edittm',hidden:'true'}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                     // billuminstock.selectedCheckBox(rowIndex);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	//双击方法
		                   	   billuminstock.showEdit(\'show\',rowData)
		                   }}'/>
	     </div>
	 </div>
</div>
	<div id="editDialog"  class="easyui-dialog" title="修改"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="edit-toolbar"  listData=[
		    			 {"id":"plan_save","title":"计划保存","iconCls":"icon-save","action":"billuminstock.plan_save()","type":0},
		    			 {"id":"d_save","title":"单行保存","iconCls":"icon-save","action":"billuminstock.single_save()","type":0},
		                 {"id":"d_split","title":"拆分","iconCls":"icon-cut","action":"billuminstock.open_split()","type":0},
		    			 {"id":"d_delete","title":"删除","iconCls":"icon-del","action":"billuminstock.single_delete()","type":0},
		    			 {"id":"main_save","title":"保存主信息","iconCls":"icon-save","action":"billuminstock.main_save()","type":0},
		    			 {"title":"取消","iconCls":"icon-cancel","action":"billuminstock.closeWindow('editDialog')","type":0}
			      		 ]
					 />
		    		<div class="search-div">
		    			<form name="editDataForm" id="editDataForm" method="post" class="city-form">
				         	<#-- 明细信息div -->
							<table>
								<tr>
									<td class="common-td">单据编号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="instockNo" id="instockNo" data-options="required:true" readOnly="true"/></td>
									<td class="common-td blank">货主：</td>
									<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" data-options="required:true"/></td>
									<td class="common-td blank">上架人：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="instockWorker" id="instockWorker" data-options="required:true" readOnly="true"/>
									<a id="select_instockWorker_but" href="javascript:billuminstock.openSelectInstockWorker();" class="easyui-linkbutton" data-options="iconCls:'icon-download'">选择上架人</a></td>
								</tr>
							</table>
						 </form>
		    		</div>
			    </div>
		    	<div data-options="region:'center',border:false">
		    		<@p.datagrid id="dataGridJG_edit"  name=""  loadUrl="" saveUrl="" defaultColumn=""  onClickRowAuto="false"
 				isHasToolBar="false"  divToolbar=""  height="400"  onClickRowEdit="false" 
 				singleSelect="true"  pagination="true" rownumbers="true" emptyMsg="" 
 				checkOnSelect="false"  selectOnCheck="false"
				          columnsJsonList="[
				 				{field:'itemNo',title:'商品编码',align:'left',width:150,sortable:true},
				 				{field:'itemName',title:'商品名称',align:'left',width:150,sortable:true},
							    {field:'colorName',title:'颜色',align:'left',width:60,sortable:true},
							    {field:'sizeNo',title:'尺码',align:'left',width:60,sortable:true},
							    {field:'sourceNo',title:'来源单号',align:'left',width:140,sortable:true},
							    {field:'destCellNo',title:'预上储位',align:'left',width:100,sortable:true},
							    {field:'itemQty',title:'计划数量',align:'right',width:80,sortable:true},
				 				{field:'realCellNo',title:'实际上架储位',align:'left',width:100,
				 					editor:{
				 						type:'validatebox',
				 						options:{
					 						required:true
				 						}
				 					},sortable:true
				 				},
				 				{field:'realQty',title:'实际上架数量',width:100,align:'right',
							    	editor:{
							    		type:'numberbox',
							    		options:{
					 						required:true,
					 						min:0
				 						}
							    	},sortable:true
							    },
				 				{field:'instockedQty',title:'RF上架数量',width:100,align:'right',sortable:true},
				 				{field:'boxNo',title:'箱号',width:100,align:'left',sortable:true},
				 				{field:'itemType',title:'商品类型',align:'left',width:60,formatter:billuminstock.typeFormatter},
							    {field:'quality',title:'品质',align:'left',width:60,formatter:billuminstock.qualityFormatter},
				 				{field:'instockNo',title:'instockNo',width:60,hidden:'true'},
				 				{field:'locno',title:'locno',width:60,hidden:'true'},
				 				{field:'ownerNo',title:'ownerNo',width:60,hidden:'true'},
				 				{field:'instockId',title:'instockId',width:60,hidden:'true'}
				 			]"
				 			jsonExtend='{onClickRow:function(rowIndex, rowData){
			                	  // 触发点击方法  调JS方法
			                   	 billuminstock.onClickRowReceiptDtl(rowIndex,rowData);
			                }}'/>
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
					<td colspan="2" class="common-td blank" style="text-align:center"><a href="javascript:billuminstock.single_split()" data-options="'iconCls':'icon-ok'" class="easyui-linkbutton">确定</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="instockWorkerDialog"  class="easyui-dialog" title="上架人"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
       		<div data-options="region:'north',border:false">
		    		<@p.toolbar id="instockWorkerToolbar"  listData=[
		    			 {"title":"确认","iconCls":"icon-ok","action":"billuminstock.selectInstockWorkerOk()","type":0},
		    			 {"title":"取消","iconCls":"icon-cancel","action":"billuminstock.closeWindow('instockWorkerDialog')","type":0}
			      		 ]
					 />
			   </div>
	 		<div data-options="region:'center',border:false">
		    		<@p.datagrid id="instockWorkerDataGridJG_edit"  loadUrl="/authority_user/user.json" saveUrl=""   defaultColumn="" 
		              isHasToolBar="false" divToolbar="" onClickRowEdit="true"    pagination="true"
			           rownumbers="true"  singleSelect = "true" height="400" emptyMsg=""  showFooter="true"
				          columnsJsonList="[
				          		{field : ' ',checkbox:true},
				 				{field:'workerNo',title:'用户编码',align:'left',width:150},
				 				{field:'workerName',title:'用户名称',align:'left',width:150}
				 			]"/>
	     	</div>
	     	</div>
	</div>
</body>
</html>