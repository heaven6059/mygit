<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>类别管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/category.js?version=1.0.5.1"></script>
</head>
<body class="easyui-layout">
      <#--
	  <div id='easyui-panel-left' data-options="region:'west',title:'品牌列表'" style="width:150px;">
	            <ul class="easyui-tree" id="dataTreeId"></ul>
	  </div>
	  -->
	  <div data-options="region:'center'">
		  <div class="easyui-layout" data-options="fit:true">		
		  		<div data-options="region:'north',border:false" >
		  			 <@p.toolbar id="toolbar"  listData=[
	       				 {"title":"查询","iconCls":"icon-search","action":"category.searchCategory()", "type":0},
	                     {"title":"清除","iconCls":"icon-remove","action":"category.searchCategoryClear()", "type":0}
	                   ]
			 	 	/>
		  			<form name="searchForm" id="searchForm" method="post" style="padding:8px;">
						所属品牌库：<input class="easyui-combobox" style="width:150px" name="sysNo" id="sysNoCondition"/>
						类别编码：<input class="easyui-validatebox ipt" style="width:90px" name="cateNo" id="cateNoCondition" />
						类别名称：<input class="easyui-validatebox ipt" style="width:90px" name="cateName" id="cateNameCondition" />
					</form>
		  		</div>	
		  		<div data-options="region:'center',border:false" >
		  			<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="类别列表"
			    	isHasToolBar="false" divToolbar="#categorySearchDiv"  height="325"  onClickRowEdit="false"    pagination="true"
				    rownumbers="true"  
				               columnsJsonList="[
				                  {field : 'cateNo',title : '类别编码',width : 80,align:'left'},
				                  {field : 'cateName',title : '类别名称',width : 150,align:'left'},
				                  {field : 'cateLevelid',title : '类别级别',width : 60,align:'left'},
				                  {field : 'headCateNoStr',title : '上级类别',width : 150,align:'left'},
				                  {field : 'statusStr',title : '类别状态',width : 60,align:'left'},
				                  {field : 'sysNoStr',title : '所属品牌库',width : 100,align:'left'},
				                  {field:'creator',title:'建档人',width:100,align:'left'},
		              			  {field:'createtm',title : '建档时间',width : 125,sortable:true},
		              			  {field:'editor',title:'修改人',width:100,align:'left'},
		              	          {field:'edittm',title : '修改时间',width : 125,sortable:true}
				                 ]"
				                 jsonExtend='{onSelect:function(rowIndex, rowData){
			                     	// 触发点击方法  调JS方法
			                        category.loadDetail(rowData.cateNo);
			                   }}' 
					        />
		  		</div>
		  		<div data-options="region:'south',border:false,minSplit:true" style="height:200px;padding-top:10px;">
		  			<form name="dataForm" id="dataForm" method="post" class="city-form">
			         <#-- 明细信息div -->
			         <#-- 2013-08-29 所有输入框全部disable -->
			         <table>
			         	<tr>
			         		<td class="common-td blank">类别编码：</td>
			         		<td><input class="easyui-validatebox ipt" style="width:150px" name="cateNo" id="cateNo"  data-options="validType:['vnChinese[\'类别编码不能包含中文\']','vLength[0,10,\'最多只能输入10个字符\']']" readOnly="true"/></td>
			         		<td class="common-td blank">类别名称：</td>
			         		<td><input class="easyui-validatebox ipt" style="width:150px" name="cateName" id="cateName"  data-options="validType:['vLength[0,50,\'最多只能输入50个字符\']']" readOnly="true"/></td>
			         	</tr>
			         	<tr>
			         		<td class="common-td blank">类别级别：</td>
			         		<td><input class="easyui-validatebox ipt" style="width:150px" name="cateLevelid" id="cateLevelid"   readOnly="true"/></td>
			         		<td class="common-td blank">上级类别：</td>
			         		<td><input class="easyui-validatebox ipt" style="width:150px" name="headCateNoStr" id="headCateNoStr"  readOnly="true"/></td>
			         	</tr>
			         	<tr>
			         		<td class="common-td blank">所属品牌库：</td>
			         		<td><input class="easyui-combobox" style="width:150px" name="sysNo" id="sysNo" data-options="disabled:true"/></td>
			         		<td class="common-td blank">类别状态：</td>
			         		<td><input class="easyui-combobox" id="status" style="width:150px" name="status" data-options="disabled:true"/> </td>
			         	</tr>
			         </table>
					 </form>	
		  		</div>
		  </div>
	  </div>
</body>
</html>