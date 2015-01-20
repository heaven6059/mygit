<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>车辆类型维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/baseinfo/bmdefcartype.js"/>"></script>
    <script type="text/javascript" src="<@s.url "/resources/common/other-lib/common.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/bmdefcartype.css"/>" />
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
                         {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"bmdefcartype.addInfo()","type":1},
                         {"id":"btn-close","title":"修改","iconCls":"icon-remove","action":"bmdefcartype.editInfo()","type":2},
                         {"id":"btn-close","title":"删除","iconCls":"icon-remove","action":"bmdefcartype.del()","type":3},
				         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('车辆类型维护')","type":0}
	                ]
				  />
	 </div>
	<#-- 条件查询区域div	
	<div style="background:#F4F4F4;padding:0px;">
	  	   <@p.queryConditionDiv id="queryConditionDiv" moduleFlag="21010020"
	  	    queryGridId="dataGridJG"  queryDataUrl="${BasePath}/bm_defcartype/list.json" collapsed="true" height="120" />
	</div>
	 -->	
	<div>
		<div id='r1' class="easyui-panel"  >
          	 	<@p.datagrid id="dataGridJG"  loadUrl="/bm_defcartype/list.json" saveUrl=""   defaultColumn=""   title="车辆类型维护"
		              isHasToolBar="false" divToolbar="#storeSearchDiv" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "false"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			           		  {field : 'cartypeNo',title : '车辆编号',width : 120},
			                  {field : 'cartypeName',title : '车辆名称',width : 120},
			                  {field : 'cartypeWeight',title : '载重量',width : 100},
			                  {field : 'cartypeLengthStr',title : '长',width : 100},
			                  {field : 'cartypeWidthStr',title : '宽',width : 150},
			                  {field : 'cartypeHeightStr',title : '高',width : 150},
			                  {field : 'maxLayer',title : '最大举高层数',width : 120},
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
		                   	  bmdefcartype.edit(rowData)
		                   }}'/>
	     </div>
	     <div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:620px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
	         	<#-- 明细信息div -->
					<table border="1">
						<tr>
							<td>车型编码：</td>
							<td>
								<input class="easyui-validatebox" style="width:100px" name="cartypeNo" id="cartypeNo" required="true"  data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"/>
								<input class="easyui-validatebox" style="width:100px" name="cartypeNo" id="cartypeNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">车型名称：</td>
							<td><input class="easyui-validatebox" style="width:120px" name="cartypeName" id="cartypeName" required="true"  data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
							<td style="padding-left:15px;">载重量<input type="hidden"/></td>
							<td><input class="easyui-numberbox" style="width:120px" name="cartypeWeight" id="cartypeWeight" required="true" precision="5" data-options="min:0,validType:'length[0,13]'"/></td>
						</tr>
						<tr>
							<td>长：</td>
							<td><input class="easyui-numberbox" style="width:100px" name="cartypeLength" id="cartypeLength" required="true"  data-options="min:0,validType:['vLength[0,13,\'最多只能输入13个字符\']'],groupSeparator:','"/></td>
							<td style="padding-left:15px;">宽：</td>
							<td><input class="easyui-numberbox" style="width:120px" name="cartypeWidth" id="cartypeWidth" required="true" data-options="min:0,validType:['vLength[0,13,\'最多只能输入13个字符\']']"/></td>
							<td  style="padding-left:15px;">高</td>
							<td><input class="easyui-numberbox" style="width:120px" name="cartypeHeight" id="cartypeHeight" required="true"  data-options="min:0,validType:['vLength[0,13,\'最多只能输入13个字符\']']"/></td>
						</tr>
						<tr>
							<td>最大举高层数：</td>
							<td>
								<input class="easyui-numberbox" style="width:100px" name="maxLayer" id="maxLayer" required="true"  data-options="min:0,validType:['vLength[0,2,\'最多只能输入2个字符\']']"/>
							</td>
							<td clospan="4"></td>
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
							<td style="padding-left:15px;">修改时间：</td>
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