<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>打印机管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/bmdefprinter/bmdefprinter.js?version=1.0.5.0"></script>    
</head>
<body class="easyui-layout">
      <#-- 工具菜单div 	-->
	<div data-options="region:'north',border:false">
		   <@p.toolbar id="main-toolbar"  listData=[
		   		 {"title":"查询","iconCls":"icon-search","action":"bmdefprinter.searchForm()","type":0},
		   		 {"title":"清除","iconCls":"icon-remove","action":"bmdefprinter.searchClear()","type":0},
				 {"title":"新增","iconCls":"icon-add","action":"bmdefprinter.addUI();","type":1},
				 {"title":"修改","iconCls":"icon-edit","action":"bmdefprinter.editUI();","type":2},
				 {"title":"删除","iconCls":"icon-del","action":"bmdefprinter.deleteRows()","type":3},
				 {"title":"关闭","iconCls":"icon-close","action":"closeWindow('打印机管理')","type":0}
               ]
			 />	
	</div>
	<div data-options="region:'center',border:false">
	  	<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
		  		<div id="searchDiv" style="padding:10px;">
		       		 <form name="searchForm" id="searchForm" method="post">
		       		 				<input type="hidden" name="locno"/>
					   	打印机编号：<input class="easyui-validatebox ipt" style="width:110px" name="printerNo" />
						打印机名称：<input class="easyui-validatebox ipt" style="width:110px" name="printerName"  />
						类型：<input class="easyui-combobox" style="width:120px" name="printerType" id="printerTypeCondition" data-options="editable : false" />
					</form>
				 </div>
			</div>
	         <#-- 数据列表div -->
	         <div data-options="region:'center',border:false">
  	  		<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="打印机列表"
               isHasToolBar="false"  divToolbar="" height="465"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
	           rownumbers="true"
	           columnsJsonList="[
	           		{field : 'id',checkbox:true},
	           		{field : 'printerNo',title : '打印机编号',width : 100,align:'left'},
	                {field : 'printerName',title : '打印机名称',width : 200,align:'left'},
	                {field : 'printerType',title : '类型',width : 100,formatter:bmdefprinter.printerTypeFormatter,align:'left'},
	                {field : 'printerDriver',title : '驱动名称',width : 300,align:'left'},
	                {field : 'printerPort',title : '端口',width : 90,align:'left'},
	                {field : 'status',title : '状态',width : 90,formatter:bmdefprinter.statusFormatter,align:'left'}
	            ]" 
		        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
                	  // 触发点击方法  调JS方法
                   	  bmdefprinter.loadDetail(rowData);
                }}'/>
              </div>
          </div>    
	</div>
	<#-- 明细信息div -->
	     	<div id="openUI" class="easyui-window"  style="width:600px;height:300px;"   data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    minimizable:false">
			     <@p.toolbar id="detail-toolbar"  listData=[
			   		 {"id":"info_save","title":"保存","iconCls":"icon-save","action":"bmdefprinter.manage()","type":0},
			   		 {"id":"info_close","title":"取消","iconCls":"icon-cancel","action":"bmdefprinter.closeUI()","type":0}
	               ]
				 />	
				 <form name="dataForm" id="dataForm" method="post" class="city-form">
				 	<input type="hidden" id="opt"/>
				 	<table>
				 		<tr>
				 			<td class="common-td blank">打印机编号：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="printerNo" id="printerNo"  data-options="required:true,validType:['vnChinese[\'委托业主编号不能包含中文\']','vLength[0,5,\'最多只能输入3个字符\']']" /></td>
				 			<td class="common-td blank">打印机名称：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:150px" name="printerName" id="printerName"  data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
				 		</tr>
				 		<tr>
				 			<td class="common-td blank">类型：</td>
				 			<td><input class="easyui-combobox" style="width:120px" name="printerType" id="printerType"  data-options="editable : false" /></td>
				 			<td class="common-td blank">驱动名称：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:150px" name="printerDriver" id="printerDriver"  data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
				 		</tr>
				 		<tr>
				 			<td class="common-td blank">端口：</td>
				 			<td><input class="easyui-numberbox ipt" style="width:120px" name="printerPort" id="printerPort"  data-options="required:true,validType:['vLength[0,8,\'最多只能输入8个字符\']']"  /></td>
				 			<td class="common-td blank">状态：</td>
				 			<td><input class="easyui-combobox" style="width:150px" name="status" id="status"   data-options="editable : false"/></td>
				 		</tr>
				 		<tr>
				 			<td class="common-td blank">模块编码：</td>
				 			<td colspan="3"><input class="easyui-validatebox ipt" style="width:120px" name="moduleid" id="moduleid"  data-options="required:true,validType:['vLength[0,8,\'最多只能输入8个字符\']']"  /></td>
				 		</tr>
				 	</table>
				</form>	 
			</div>
</body>
</html>