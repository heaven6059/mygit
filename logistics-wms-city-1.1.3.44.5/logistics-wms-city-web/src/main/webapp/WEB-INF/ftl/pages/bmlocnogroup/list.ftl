<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>仓库组别维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bmcircle.css"/>" />
	<script type="text/javascript" src="${domainStatic}/resources/js/bmlocnogroup/bmlocnogroup.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
	      <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	               <@p.toolbar id="toolBarOne"  listData=[
	               				 {"title":"查询","iconCls":"icon-search","action":"bmlocnogroup.searchLocnoGroup();","type":0},
		       					 {"title":"清空","iconCls":"icon-remove","action":"bmlocnogroup.searchClear();","type":0},
	                             {"title":"新增","iconCls":"icon-add","action":"bmlocnogroup.showAdd()", "type":1},
	                             {"title":"修改","iconCls":"icon-edit","action":"bmlocnogroup.showModify()","type":2},
	                             {"title":"删除","iconCls":"icon-del","action":"bmlocnogroup.deleteLocnoGroup()","type":3},
						         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('仓库组别维护')","type":0}
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
		       				<td class="common-td blank">组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
                            <td><input class="easyui-combobox" style="width:120px" name="groupCode" id="groupCodeCondition" /></td>
		       				<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		                 	<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		                 	<#--
		                 	<td class="common-td blank">所属品牌：</td>
							<td ><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
							-->
							<td class="common-td blank">业务类型：</td>
						    <td><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessTypeCondition" 
		       			</tr>
	       			</table>
				</form>
			 </div>
       	</div>		
	          <div data-options="region:'center',border:false">
          	            <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="仓库组别列表"
		                   isHasToolBar="false" divToolbar=""  height="418"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false" emptyMsg="" pageNumber=0
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 150, checkbox:true},
			                  {field : 'locno',title : '仓库编码',width : 100,align:'left'},
			                  {field : 'sysNo',title : '品牌库',width : 100,align:'left',formatter: bmlocnogroup.sysNameFormatter},
			                  {field : 'businessType',title : '业务类型',width : 100,align:'left',formatter: bmlocnogroup.businessTypesFormatter },
			                  {field : 'groupCode',title : '组别名称',width : 150,align:'left', formatter: bmlocnogroup.groupCodesFormatter },
			                  {field : 'personNum',title : '人数',width : 100,align:'left'},
			                  {field:'creator',title:'创建人',width:100,align:'left'},
			                  {field:'creatorname',title : '创建人名称',width : 130,sortable:true},
 			                  {field:'createtm',title : '创建时间',width : 125,sortable:true},
			                  {field:'editor',title:'编辑人',width:100,align:'left'},
			                  {field:'editorname',title : '编辑人名称',width : 130},
 			                  {field:'edittm',title : '编辑时间',width : 125}
			                 ]"   
			                 jsonExtend='{
			                      onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  bmlocnogroup.showEdit(rowData);
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
                     	<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
			            <td>
			                <input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo_dataForm" required="true" />
							<input class="easyui-validatebox ipt" style="width:120px" name="sysName" id="sysNameHide_dataForm" type="hidden"/>
			            </td>
			            <td class="common-td blank"> 业务类型：</td>
			            <td>
			                <input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType_dataForm" required="true"/> 
			                <#--
							<input class="easyui-validatebox ipt" style="width:120px" name="businessType" id="businessTypeHide_dataForm" type="hidden"/>
							-->
			            </td>
                     </tr>
                     <tr>
                     	<td class="common-td blank"> 组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
			            <td>
			                <input class="easyui-combobox ipt" style="width:120px" name="groupCode" id="groupCode_dataForm" required="true"/> 
			                <#--
							<input class="easyui-validatebox ipt" style="width:120px" name="groupCode" id="groupCodeHide_dataForm" type="hidden"/>
							-->
			            </td>
			            <td class="common-td blank"> 人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</td>
						<td><input class="easyui-validatebox ipt" style="width:130px" name="personNum" id="personNum_dataForm"  required="true" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[1,999999,\'只能输入1~999999的数字范围\']']"   /></td>
													
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
                     		<a id="info_save" href="javascript:bmlocnogroup.addLocnoGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">保存</a>
							<a id="info_update" href="javascript:bmlocnogroup.modifyLocnoGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>  
							<a id="info_close" href="javascript:bmlocnogroup.closeCircle();" class="easyui-linkbutton" data-options="iconCls:'icon-close'">取消</a>
                     	</td>
                     </tr>
				</table>
			 </form>	
		</div>	
	  </div>
</div>

</body>
</html>