<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>基本线路维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/baseinfo/osdefline.js"/>"></script>
    <script type="text/javascript" src="<@s.url "/resources/common/other-lib/common.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/osdefline.css"/>" />
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
                         {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"osdefline.addInfo()","type":1},
                         {"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"osdefline.editInfo()","type":2},
                         {"id":"btn-close","title":"删除","iconCls":"icon-remove","action":"osdefline.del()","type":3},
						 {"id":"btn-close","title":"导出","iconCls":"btn-export","action":"osdefline()","type":5},
				         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('基本线路维护')","type":0}
	                ]
				  />
	 </div>
	<#-- 条件查询区域div 	
	<div style="background:#F4F4F4;padding:0px;">
	  	   <@p.queryConditionDiv id="queryConditionDiv" moduleFlag="21010020"
	  	    queryGridId="dataGridJG"  queryDataUrl="${BasePath}/bm_defcartype/list.json" collapsed="true" height="120" />
	</div>-->		
	<div>
		<div id='r1' class="easyui-panel"  >
          	 	<@p.datagrid id="dataGridJG"  loadUrl="/os_defline/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="基本线路维护"
		              isHasToolBar="false" divToolbar="#storeSearchDiv" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "false"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			           		  {field : 'lineNo',title : '线路代码',width : 120},
			                  {field : 'lineName',title : '线路名称',width : 150},
			                  {field : 'transportType',title : '运输方式',width : 100,formatter:osdefline.transportTypeFormatter},
			                  {field : 'deliverType',title : '配送方式',width : 100,formatter:osdefline.deliverTypeFormatter},
			                  {field : 'lineFname',title : '线路全程',width : 200,formatter:osdefline.lineFnameFormatter},
			                  {field : 'lineRemark',title : '备注',width : 150},
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
		                   	  osdefline.edit(rowData)
		                   }}'/>
	     </div>
	     <div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:460px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
	         	<#-- 明细信息div -->
					<table border="1">
							<tr>
							<td>线路代码：</td>
							<td>
								<input class="easyui-validatebox" style="width:120px" name="lineNo" id="lineNo" required="true"  data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"/>
								<input class="easyui-validatebox" style="width:120px" name="lineNo" id="lineNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">线路名称：</td>
							<td>
								<input class="easyui-validatebox" style="width:150px" name="lineName" id="lineName"/>
							</td>
						</tr>
						<tr>
							<td>运输方式：</td>
							<td><input class="easyui-combobox" style="width:120px" name="transportType" id="transportType" /></td>
							<td style="padding-left:15px;">配送方式：</td>
							<td><input class="easyui-combobox" style="width:150px" name="deliverType" id="deliverType"/></td>
						</tr>
						<tr>
							<td>线路全程：</td>
							<td colspan="3">
								<input class="easyui-validatebox" style="width:345px" name="lineFname" id="lineFname" readonly = "true"/>
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td colspan="3">
								<input class="easyui-validatebox" style="width:345px" name="lineRemark" id="lineRemark" data-options="validType:['vLength[0,64,\'最多只能输入50个字符\']']"/>
							</td>
						</tr>
						<tr id="creatorinfo">
							<td>创建人：</td>
							<td id="creator"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="createtm"></td>
							<td colspan="2"></td>
						</tr>
						<tr id="editorinfo">
							<td>修改人：</td>
							<td id="editor"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="edittm"></td>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td colspan="6" style="text-align:center;">
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