<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>码表定义</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/lookup.js?version=1.0.5.1"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
	<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
		<@p.toolbar  id="toolBarOne" listData=[
       					 {"title":"查询","iconCls":"icon-search","action":"lookup.searchLookup();","type":0},
       					 {"title":"清空","iconCls":"icon-remove","action":"lookup.searchClear();","type":0},
                         {"title":"新增","iconCls":"icon-add","action":"lookup.showAddWindow();","type":1},
                         {"title":"修改","iconCls":"icon-edit","action":"lookup.showUpdateWindow();","type":2},
                         {"title":"删除","iconCls":"icon-del","action":"lookup.deleteLookup();","type":3},
				         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
	                ]
		/>
	</div>
 <div data-options="region:'center',border:false">
	<div class="easyui-layout" data-options="fit:true" id="subLayout">
 		 <div  data-options="border:false" >
             <div class="search-div" id="search-div">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					 <table>
				 		<tr>
				 			<td class="common-td blank">编码：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="lookupcode" id="lookupcodeCondition" /></td>
							<td class="common-td blank">名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="lookupname" id="lookupnameCondition" /></td>
				 		</tr>
            		</table>
            	</form>         
             </div>
         </div>
   	<#-- 显示列表-->         
	<div data-options="region:'center',border:false">
	    <@p.datagrid id="dataGridLU"  loadUrl="/lookup/list.json" saveUrl=""  defaultColumn=""   title="码表定义"
	       isHasToolBar="false"  height="300"  onClickRowEdit="false"    pagination="true" divToolbar="#search-div"
	       rownumbers="true" emptyMsg=""
	       columnsJsonList="[
	       	  {field : 'ck',title : '',width : 50, checkbox:true},
	          {field : 'lookupcode',title : '编码',width : 250,align:'left'},
	          {field : 'lookupname',title : '名称',width : 160,align:'left'},
	          {field : 'systemid',title:'系统编码',width:70,align:'left'},
	          {field : 'lookuplevel',title : '是否可配置',width : 100,align:'left'},
	          {field:'sysNo',title:'品牌库',width:100,align:'left'},
	 		  {field:'lookuptype',title:'编码类型',width:120,align:'left'},
	 		   {field : 'creator',title : '建档人',width : 80,align:'left'},
			   {field : 'createtm',title : '建档时间',width : 125,sortable:true},
			   {field : 'editor',title : '修改人',width : 80,align:'left'},
			   {field : 'edittm',title : '修改时间',width : 125,sortable:true}
	         ]"
	       jsonExtend='{onDblClickRow:function(rowIndex, rowData){
	                // 触发点击方法  调JS方法
	                lookup.loadDetailDtl(rowData);
	       }}'/>
	</div> 
 </div>
 
 
<#--详情窗口-->
<div id="lookupDtlInfo"  class="easyui-window" title="码表定义明细" style="display:none"
	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
	maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" id="form_detail" style="height:80px;padding:8px;">
			<form name="lookupdataForm" id="lookupdataFormDtl" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">编码：</td>
				 		<td><input class="easyui-validatebox ipt" style="width:120px" name="lookupcode" id="lookupcodedialog" readOnly="true" /></td>
						<td class="common-td blank">名称：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="lookupname" id="lookupnamedialog" readOnly="true"/></td>
						<td class="common-td blank">编码类型：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="lookuptype" id="lookuptypedialog"  readOnly="true"/></td>
						<td class="common-td blank">是否可配置：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="lookuplevel" id="lookupleveldialog" readOnly="true"/></td>
				 	</tr>
				 	<tr>
						<td class="common-td blank">品牌库：</td>
				 		<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="sysNodialog" readOnly="true"/></td>
						<td class="common-td blank">备注：</td>
						<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="remarks" id="remarkdialog" readOnly="true"/></td>
				 	</tr>
            	</table>
            </form>	
		</div>
		<div data-options="region:'center',border:false" >
			<@p.datagrid id="dataGridLU_Dtl"  loadUrl="" saveUrl=""  defaultColumn=""   title=""
				isHasToolBar="false"  height="300"  onClickRowEdit="false"    pagination="true"  
				rownumbers="true" emptyMsg=""
				divToolbar=""
			  	columnsJsonList="[
			        	{field:'systemid',title:'系统编码',width:70,align:'left'},
			          	{field : 'itemval',title : '项目值',width : 200,align:'right'},
			          	{field : 'itemname',title : '项目名称',width : 200,align:'left'},
			          	{field : 'remarks',title : '备注',width : 200,align:'left'}
			 	]"/>
		</div>
	</div>
</div>

<#--新增,修改码表窗口-->
<div id="addWindow"  class="easyui-window" title="新增"
	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
	maximized:true,minimizable:false,maximizable:false"> 
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height:120px">
			<#-- 工具菜单div -->
			<@p.toolbar  id="toolBarTwo" listData=[
				{"id":"btn-add","title":"保存","iconCls":"icon-save","action":"lookup.saveLookupInfo()", "type":0},
				{"id":"btn-modify","title":"修改","iconCls":"icon-save","action":"lookup.updateLookup();", "type":0},
				{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"lookup.closeAddWindow();","type":0}
			]/>
		<div class="search-div">
			<form name="lookupdataForm" id="lookupdataForm" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="lookupcode" id="lookupcodeadd" required="true" missingMessage='编码为必填项!' /></td>
						<td class="common-td blank">名称：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="lookupname" id="lookupnameadd"  data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']" /></td>
						<td class="common-td blank">编码类型：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="lookuptype" id="lookuptypeadd" required="true" /></td>
						<td class="common-td blank">是否可配置：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="lookuplevel" id="lookupleveladd" required="true"/></td>
					</tr>	
					<tr>
						<td class="common-td blank">品牌库：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="sysNoadd"/></td>
						<td class="common-td blank">备注：</td>
						<td colspan="5"><input class="easyui-validatebox ipt" style="width:100%" name="remarks" id="remarkadd"/></td>
					</tr>
				</table>	
			</form>	
		</div>
	</div>
	<div data-options="region:'center',border:false" id="lookupdtl">
		<div class="easyui-layout" data-options="fit:true">
			<@p.toolbar id="toolBarThree"   listData=[
				{"title":"新增明细","iconCls":"icon-add-dtl","action":"lookup.insertRowLookupDtl('dataGridLU_DtlForAdd')", "type":0},
				{"title":"删除明细","iconCls":"icon-del-dtl","action":"lookup.removeDtlBySelected('dataGridLU_DtlForAdd')", "type":0},
				{"title":"保存明细","iconCls":"icon-save-dtl","action":"lookup.saveDtl()","type":0}
			]/>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridLU_DtlForAdd"  loadUrl="" saveUrl=""  defaultColumn=""   title="码表定义明细"
					isHasToolBar="false"  height="300"  onClickRowEdit="true"    pagination="true"
					rownumbers="true"
					divToolbar="#toolBarThree" emptyMsg=""
					columnsJsonList="[
						{field:'lookupcode',title:'字典分类编码',width:70,hidden:true},
						{field:'itemvalOld',title:'itemval',width:70,hidden:true},							
						{field : 'itemval',title : '项目值',width : 200,align:'right',
							editor:{
							type:'validatebox',
							options:{
								required:true,
								validType:[\"vnChinese['项目值不能包含中文']\",\"vLength[0,64,'最多只能输入64个字符']\"],
									missingMessage:'项目值为必填项!'
								}
							}
						},
						{field : 'itemname',title : '项目名称',width : 200,align:'left',
							editor:{
								type:'validatebox',
								options:{
									required:true,
									alidType:[\"vLength[0,64,'最多只能输入64个字符']\"],
										missingMessage:'项目名称为必填项!'
								}
							}
						},
						{field : 'remarks',title : '备注',width : 200,align:'left',
							editor:{
								type:'validatebox',
								options:{
									validType:[\"vLength[0,255,'最多只能输入255个字符']\"]
								}
							}
						}
				]"/>		 	 	
			</div>
		</div>
	</div>
</div>

</body>
</html>