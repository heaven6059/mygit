<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>车辆管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="<@s.url "/resources/js/baseinfo/bmdefcar.js"/>"></script>
</head>
<body>
<div>
      <#-- 工具菜单div 	-->
       <div style="margin-bottom:0px;height:33px;background:#F4F4F4;padding:3px;" id="toolDiv">
			    <@p.toolbar   listData=[
			    			 {"id":"btn-add","title":"新增","iconCls":"btn-add1","action":"bmdefcar.addUI();","type":1},
			    			 {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"bmdefcar.editUI();","type":2},
			    			 {"id":"btn-close","title":"删除","iconCls":"icon-remove","action":"bmdefcar.deleteRows()","type":3},
					         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('委托业主管理');","type":0}
	                       ]
				  />	
	   </div>
	   
      <#-- 条件查询区域div 	
	  -->	
	  <div>
	  		 <#--
	  		<div id="searchDiv" style="padding:10px;">
	       		 <form name="searchForm" id="searchForm" method="post">
				   	仓库编码：<input class="easyui-validatebox" style="width:110px" name="locno" id="locNoCondition" />
					仓库名称：<input class="easyui-validatebox" style="width:110px" name="locname" id="locNameCondition" />
				 	<a id="searchBtn" href="javascript:bmdefloc.searchLoc();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				 	<a id="clearBtn"  href="javascript:bmdefloc.searchLocClear();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清除</a>
				</form>
			 </div>
			 -->
	         <#-- 数据列表div -->
	          <div  >
          	  		<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="车辆列表"
		               isHasToolBar="false" divToolbar=""  height="500"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'carNo',title : '车辆代码',width : 100},
			                {field : 'carName',title : '车辆名称',width : 100},
			                {field : 'carClass',title : '车辆类型',width : 100,formatter:bmdefcar.carClassFormatter},
			                {field : 'carPlate',title : '车牌号',width : 90},
			                {field : 'careWorker',title : '保养人',width : 90},
			                {field : 'careDate',title : '保养日期',width : 90},
			                {field : 'sanplNo',title : '承运商',width : 100}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                   	  bmdefcar.loadDetail(rowData);
		                }}'/>
	          </div>
	           
         	<#-- 明细信息div -->
	     	<div id="openUI" class="easyui-window" style="padding:10px;height:300px"  data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    minimizable:false">
				<form name="dataForm" id="dataForm" method="post">
					<input type="hidden" id="opt"/>
				车辆代码：<input class="easyui-validatebox" style="width:150px" name="carNo" id="carNo"  data-options="required:true,validType:['vnChinese[\'委托业主编号不能包含中文\']','vLength[0,10,\'最多只能输入10个字符\']']"  />
				车辆类型：<input class="easyui-combobox" style="width:150px" name="carClass" id="carClass"    data-options="editable:false,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></br></br>
				车辆名称：<input class="easyui-validatebox" style="width:150px" name="carName" id="carName"  data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  />
				类型代码：<input class="easyui-combobox" style="width:150px" name="cartypeNo" id="cartypeNo"    data-options="editable:false,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></br></br>
				&nbsp;&nbsp;车牌号：<input class="easyui-validatebox" style="width:150px" name="carPlate" id="carPlate"     data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  />
				&nbsp;&nbsp;保养人：<input class="easyui-validatebox" style="width:150px" name="careWorker" id="careWorker"    data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  /></br></br>
				保养日期：<input class="easyui-datebox" style="width:150px" name="careDate" id="careDate"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  />
				&nbsp;&nbsp;承运商：<input class="easyui-validatebox" style="width:150px" name="sanplNo" id="sanplNo"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  /></br></br>
				 委托业主备注：<input class="easyui-validatebox" style="width:340px" name="ownerRemark" id="remarks"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  /></br></br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="info_save" href="javascript:bmdefcar.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a id="info_close" href="javascript:bmdefcar.closeUI();" class="easyui-linkbutton" data-options="iconCls:'btn-close'">取消</a>  
				 </form>	  
			</div>
		 
	          
	  </div>
	  
</div>



</body>
</html>