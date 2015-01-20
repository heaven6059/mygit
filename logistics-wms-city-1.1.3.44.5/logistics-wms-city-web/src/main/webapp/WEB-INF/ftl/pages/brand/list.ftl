<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>品牌管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/brand.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">

	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
	  		<@p.toolbar id="toolbar"  listData=[
		    	{"title":"查询","iconCls":"icon-search","action":"brand.searchBrand()", "type":0},
		        {"title":"清除","iconCls":"icon-remove","action":"brand.searchBrandClear()", "type":0},
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('品牌管理')","type":0}
		     ]/>
	 </div>
	  
	 <div data-options="region:'center',border:false">
		 
		 <div class="easyui-layout" data-options="fit:true" id="subLayout">
			 <div data-options="region:'center',split:false" >
			 
			  		<div id="brandSearchDiv" data-options="border:false" style="padding:10px;">
			       		 <form name="searchForm" id="searchForm" method="post" class="city-form"> 
			       		 	<table>
				            	<tr>
				                	<td class="common-td">所属品牌库：</td>
				                    <td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition"/></td>
				                    <td class="common-td blank">品牌编码：</td>
				                    <td><input class="easyui-validatebox ipt" style="width:120px" name="brandNo" id="brandNoCondition" /></td>
				                    <td class="common-td blank">品牌名称：</td>
				                    <td><input class="easyui-validatebox ipt" style="width:120px" name="brandName" id="brandNameCondition" /></td>
				               	</tr>
				           </table>
						</form>
					 </div>
		       			
		             <#-- 数据列表div -->
		          	 <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="品牌列表"
				      		isHasToolBar="false" divToolbar="#brandSearchDiv"  onClickRowEdit="false"  pagination="true"
					        rownumbers="true"  emptyMsg="" pageNumber=0
					        columnsJsonList="[
					                  {field : 'brandNo',title : '品牌编码',width : 140,align:'left'},
					                  {field : 'brandName',title : '品牌名称',width : 150,align:'left'},
					                  {field : 'brandClassStr',title : '品牌级别',width : 100},
					                  {field : 'sysNoStr',title : '所属品牌库',width : 100,align:'left'},
					                  {field : 'statusStr',title : '品牌状态',width : 100},
					                  {field:'creator',title:'建档人',width:100,align:'left'},
		 			                  {field:'createtm',title : '建档时间',width : 125,sortable:true},
					                  {field:'editor',title:'修改人',width:100,align:'left'},
		 			                  {field:'edittm',title : '修改时间',width : 125,sortable:true}
					                 ]"   
					                 jsonExtend='{onSelect:function(rowIndex, rowData){
				                            // 触发点击方法  调JS方法
				                            brand.loadDetail(rowData.brandNo);
				          }}'
					/>
				</div>
		
				<div data-options="region:'south',split:false,border:false" style="height:180px;">
					<div id="toolbarEdit" class="easyui-panel" data-options="collapsible:false,height:180,fit:true"  title="明细信息" >
						<div class="easyui-layout" data-options="fit:true">
				    		<form name="dataForm" id="dataForm" method="post" class="city-form" style="padding-left:5px;">
						    	<table style="height:120px;">
					            	<tr>
					                	<td class="common-td">品牌编码：</td>
					                    <td><input class="easyui-validatebox ipt" style="width:150px" name="brandNo" id="brandNo" required="true" missingMessage='品牌编码为必填项!'  data-options="validType:['vnChinese[\'品牌编码不能包含中文\']','vLength[0,10,\'最多只能输入10个字符\']']" readOnly="true"/></td>
					                    <td class="common-td blank">品牌名称：</td>
					                    <td><input class="easyui-validatebox ipt" style="width:150px" name="brandName" id="brandName" data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
					               	</tr>
					               	<tr>
					                	<td class="common-td">品牌级别：</td>
					                    <td><input class="easyui-combobox ipt" style="width:150px" name="brandClass" id="brandClass"  readOnly="true"/></td>
					                    <td class="common-td blank">所属品牌库：</td>
					                    <td><input class="easyui-combobox ipt" style="width:150px" name="sysNo" id="sysNo"  readOnly="true"/></td>
					               	</tr>
					               	<tr>
					                	<td class="common-td">品牌状态：</td>
					                    <td colspan='3'><input id="status" class="easyui-combobox ipt"  style="width:150px" name="status"  readOnly="true"/> </td>
					               	</tr>
					           </table>
							</form>
						</div>
					</div>
			     </div>
		     
		     </div>
		     
	     </div>

</body>
</html>