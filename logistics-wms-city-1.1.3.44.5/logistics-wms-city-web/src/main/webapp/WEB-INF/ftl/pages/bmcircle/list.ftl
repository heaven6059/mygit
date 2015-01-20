<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>卸货点维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bmcircle.css"/>" />
	<script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
	<script type="text/javascript" src="${domainStatic}/resources/js/bmcircle/bmcircle.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
	      <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	               <@p.toolbar id="toolBarOne"  listData=[
	               				 {"title":"查询","iconCls":"icon-search","action":"bmcircle.searchCircle();","type":0},
		       					 {"title":"清空","iconCls":"icon-remove","action":"bmcircle.searchClear();","type":0},
	                             {"title":"新增","iconCls":"icon-add","action":"bmcircle.showAdd()", "type":1},
	                             {"title":"修改","iconCls":"icon-edit","action":"bmcircle.showModify()","type":2},
	                             {"title":"删除","iconCls":"icon-del","action":"bmcircle.deleteCircle()","type":3},
						         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('卸货点维护')","type":0}
		                       ]
					  />
		  </div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
	       	<div id="locSearchDiv" style="padding:10px;">
	       		 <form name="searchForm" id="searchForm" method="post" class="city-form">
				   	<table>
		       			<tr>
		       				<td class="common-td">卸货点编码：</td>
		       				<td><input class="easyui-validatebox ipt" style="width:120px" name="circleNo" id="circleNoCondition" /></td>
		       				<td class="common-td blank">卸货点名称：</td>
		       				<td><input class="easyui-validatebox ipt" style="width:120px" name="circleName" id="circleNameCondition" /></td>
		       			</tr>
	       			</table>
				</form>
			 </div>
       	</div>		
	          <div data-options="region:'center',border:false">
          	            <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="卸货点列表"
		                   isHasToolBar="false" divToolbar=""  height="418"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false" emptyMsg="" pageNumber=0
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 150, checkbox:true},
			                  {field : 'circleNo',title : '卸货点编码',width : 100,align:'left'},
			                  {field : 'circleName',title : '卸货点名称',width : 200,align:'left'},
			                  {field : 'createFlagStr',title : '创建方式',width : 100,align:'left'},
			                  {field:'creator',title:'创建人',width:100,align:'left'},
 			                  {field:'createtm',title : '创建时间',width : 125,sortable:true},
			                  {field:'editor',title:'编辑人',width:100,align:'left'},
 			                  {field:'edittm',title : '编辑时间',width : 125,sortable:true},
 			                  {field:'memo',title:'备注',width:300,align:'left'}
			                 ]"   
			                 jsonExtend='{
			                      onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  bmcircle.showEdit(rowData);
				                  }
		                     }'
				           />
	          </div>
	  	</div>
	 </div>        
	     <div id="showDialog"  class="easyui-window" title="新增"  
		     style="padding:10px;height:300px;width:500px;"  data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
		     	<table>
                     <tr>
                     	<td class="common-td blank">卸货点编码：</td>
             			<td><input class="easyui-validatebox ipt" style="width:110px" name="circleNo" id="circleNo" required="true" missingMessage='卸货点编码为必填项!'  data-options="validType:['vnCharOrNumber[\'只能输入字母或数字的组合\']','vLength[0,10,\'最多只能输入10个字符\']']"  /></td>
             			<td class="common-td blank">卸货点名称：</td>
             			<td><input class="easyui-validatebox ipt" style="width:130px" name="circleName" id="circleName" required="true" data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
                     </tr>
                     <tr>
                     	<td class="common-td blank">备注：</td>
             			<td colspan='3'><input class="easyui-validatebox ipt" style="width:324px" name="memo" id="memo" data-options="validType:['vLength[0,100,\'最多只能输入100个字符\']']"/></td>
                     </tr>
                     <tr id ="creatorDiv">
                     	<td class="common-td blank">创建人：</td>
             			<td><input class="easyui-validatebox ipt" style="width:110px" name="creator" id="creator"  disabled="disabled"/></td>
             			<td class="common-td blank">创建时间：</td>
             			<td><input class="easyui-validatebox ipt" style="width:130px" name="createtm" id="createtm"  disabled="disabled"/></td>
                     </tr>
                     <tr id ="editorDiv">
                     	<td class="common-td blank">修改人：</td>
             			<td><input class="easyui-validatebox ipt" style="width:110px" name="editor" id="editor"  disabled="disabled"/></td>
             			<td class="common-td blank">修改时间：</td>
             			<td><input class="easyui-validatebox ipt" style="width:130px" name="edittm" id="edittm"  disabled="disabled"/></td>
                     </tr>
                     <tr>
                     	<td colspan='4' style='text-align:center'>
                     		<a id="info_save" href="javascript:bmcircle.addCircle();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">保存</a>
							<a id="info_update" href="javascript:bmcircle.modifyCircle();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>  
							<a id="info_close" href="javascript:bmcircle.closeCircle();" class="easyui-linkbutton" data-options="iconCls:'icon-close'">取消</a>
                     	</td>
                     </tr>
				</table>
			 </form>	
		</div>	
	  </div>
</div>

</body>
</html>