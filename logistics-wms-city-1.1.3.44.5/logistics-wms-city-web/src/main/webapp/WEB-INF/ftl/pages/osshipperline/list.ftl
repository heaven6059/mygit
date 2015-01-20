<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>线路承运商关系维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/baseinfo/osshipperline.js"/>"></script>
    <script type="text/javascript" src="<@s.url "/resources/common/other-lib/common.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/osshipperline.css"/>" />
</head>
<script>
  $(window).resize(function(){
  
	    $('#queryConditionDiv').panel('resize',{
		    width:document.body.clientWidth
	    });
	    
	    $('#dataGridJG').datagrid('resize', {
	        width:document.body.clientWidth
	    });
	
	    $('#toolbarEdit').panel('resize',{
		    width:document.body.clientWidth
	    });
  });

</script>
<body >
<div>
	<#-- 工具菜单div -->	
    <div style="margin-bottom:0px" id="toolDiv">
       <@p.toolbar   listData=[
                         {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"osshipperline.addInfo()","type":1},
                          {"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"osshipperline.editInfo()","type":2},
                         {"id":"btn-close","title":"删除","iconCls":"icon-remove","action":"osshipperline.del()","type":3},
				         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('线路承运商关系维护')","type":0}
	                ]
				  />
	 </div>
	<#-- 条件查询区域div
	<div style="background:#F4F4F4;padding:0px;">
	  	   <@p.queryConditionDiv id="queryConditionDiv" moduleFlag="21010020"
	  	    queryGridId="dataGridJG"  queryDataUrl="${BasePath}/bm_defcartype/list.json" collapsed="true" height="120" />
	</div> -->		
	<div>
		<div id='r1' class="easyui-panel"  >
          	 	<@p.datagrid id="dataGridJG"  loadUrl="/os_shipper_line/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="线路承运商关系维护"
		              isHasToolBar="false" divToolbar="#storeSearchDiv" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "false"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			                  {field : 'lineName',title : '线路',width : 200},
			                  {field : 'shipperNo',title : '承运商',width : 200,formatter:osshipperline.shipperFormatter},
			                  {field : 'creator',hidden:'true'},
			                  {field : 'createtm',hidden:'true'},
			                  {field : 'editor',hidden:'true'},
			                  {field : 'edittm',hidden:'true'}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                     // defdock.selectedCheckBox(rowIndex);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	//双击方法
		                   	  osshipperline.edit(rowData)
		                   }}'/>
	     </div>
	     <div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:350px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
	         	<#-- 明细信息div -->
					<table border="1">
						<tr>
							<td>线路：</td>
							<td>
								<input class="easyui-combobox" style="width:250px" name="lineNo" id="lineNo" required="true"/>
								<input class="hide" style="width:150px" name="lineNo" id="lineNoHide" type="hidden"/>
							</td>
						</tr>
						<tr>
							<td>承运商：</td>
							<td>
								<input class="easyui-combobox" style="width:250px" name="shipperNo" id="shipperNo" required="true"/>
								<input class="hide" style="width:150px" name="shipperNo" id="shipperNoHide" type="hidden"/>
							</td>
						</tr>
						<tr class="creatorinfo">
							<td>创建人：</td>
							<td id="creator"></td>
						</tr>
						<tr class="creatorinfo">
							<td>创建时间：</td>
							<td id="createtm"></td>
						</tr>
						<tr class="creatorinfo">
							<td>修改人：</td>
							<td id="editor"></td>
						</tr>
						<tr class="creatorinfo">
							<td>创建时间：</td>
							<td id="edittm"></td>
						</tr>
						<tr>
							<td colspan="2" style="text-align:center;">
								<a id="info_add" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
								<a id="info_edit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a> 
							</td>
						</tr>
					</table>
			 </form>	
		</div>	
	</div>
</div>
</body>
</html>