<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>线路暂存区维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/baseinfo/oslinebuffer.js"/>"></script>
    <script type="text/javascript" src="<@s.url "/resources/common/other-lib/common.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/oslinebuffer.css"/>" />
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
                         {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"oslinebuffer.addInfo()","type":1},
                         {"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"oslinebuffer.editInfo()","type":2},
                         {"id":"btn-close","title":"删除","iconCls":"icon-remove","action":"oslinebuffer.del()","type":3},
				         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('线路暂存区维护')","type":0}
	                ]
				  />
	 </div>
	<#-- 条件查询区域div 
	<div style="background:#F4F4F4;padding:0px;">
	  	   <@p.queryConditionDiv id="queryConditionDiv" moduleFlag="21010020"
	  	    queryGridId="dataGridJG"  queryDataUrl="${BasePath}/bm_defcartype/list.json" collapsed="true" height="120" />
	</div>-->	
	<div>
			<div id="locSearchDiv" style="padding:10px;">
	       		 <form name="searchForm" id="searchForm" method="post">
				   	<div nowrap style="width:760px" align="left">
				   		线路：<input class="easyui-combobox" style="width:120px" name="lineNo" id="search_lineNo" />
						仓区：<input class="easyui-combobox" style="width:120px" name="wareNo" id="search_wareNo" />
						库区：<input class="easyui-combobox" style="width:120px" name="areaNo" id="search_areaNo" />
						通道：<input class="easyui-combobox" style="width:120px" name="stockNo" id="search_stockNo" />
						<br><br>
					</div>
					<div nowrap style="width:760px" align="left">
				 		储位：<input class="easyui-combobox" style="width:120px" name="cellNo" id="search_cellNo" />
				 		状态：<input class="easyui-combobox" style="width:120px" name="status" id="search_status" />
				 		<div nowrap style="margin-left:156px;display:inline;" align="right">
				 			<a id="searchBtn" href="javascript:oslinebuffer.searchArea();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<a id="clearBtn"  href="javascript:oslinebuffer.searchLocClear();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清除</a>
						</div>
						<br><br>
					</div>
				</form>
			 </div>
      	 	<@p.datagrid id="dataGridJG"  loadUrl="/os_line_buffer/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="线路暂存区维护"
	              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"    pagination="true"
		           rownumbers="true"  singleSelect = "false"
		           columnsJsonList="[
		           		  {field : ' ',checkbox:true},
		           		  {field : 'lineNo',title : '线路代码',width : 120},
		                  {field : 'lineName',title : '线路名称',width : 140},
		                  {field : 'wareNo',title : '仓区编码',width : 80},
		                  {field : 'wareName',title : '仓区名称',width : 80},
		                  {field : 'areaNo',title : '库区编码',width : 80},
		                  {field : 'areaName',title : '库区名称',width : 80},
		                  {field : 'stockNo',title : '通道编码',width : 80},
		                  {field : 'aStockNo',title : '通道名称',width : 100},
		                  {field : 'cellNo',title : '储位',width :100},
		                  {field : 'bufferName',title : '暂存区名称',width : 180},
		                  {field : 'status',title : '状态',width :50,formatter:oslinebuffer.statsuFormatter},
		                  {field : 'useVolumn',title : '可用体积',width :60},
		                  {field : 'useWeight',title : '可用重量',width :60},
		                  {field : 'useBoxnum',title : '可用箱数',width :60},
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
	                   	  oslinebuffer.edit(rowData)
	                   }}'/>
	     <div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:460px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
	         	<#-- 明细信息div -->
					<table border="1">
						<tr>
							<td style="padding-left:15px;">线路：</td>
							<td colspan="3">
								<input class="easyui-combobox" style="width:120px" name="lineNo" id="lineNo" required="true"/>
								<input class="hide" name="lineNo" id="lineNoHide" type="hidden"/>
							</td>
						</tr>
							<tr>
							<td>仓区：</td>
							<td>
								<input class="easyui-combobox" style="width:120px" name="wareNo"  id="wareNo" required="true"/>
								<input class="hide" name="wareNo" id="wareNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">库区：</td>
							<td>
								<input class="easyui-combobox" style="width:120px" name="areaNo"  id="areaNo" required="true"/>
								<input class="hide" name="areaNo" id="areaNoHide" type="hidden"/>
							</td>
						</tr>
						<tr>
							<td>通道：</td>
							<td>
								<input class="easyui-combobox" style="width:120px" name="stockNo"  id="stockNo" required="true"/>
								<input class="hide" name="stockNo" id="stockNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">通道名称：</td>
							<td><input class="easyui-validatebox" textType="text" style="width:120px" name="aStockNo" id="aStockNo"  readonly = "true"/></td>
						</tr>
						<tr>
							<td>储位：</td>
							<td>
								<input class="easyui-combobox" style="width:120px" name="cellNo" id="cellNo" required="true"/>
								<input class="hide" name="cellNo" id="cellNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">暂存区名称：</td>
							<td> 
								<input class="easyui-validatebox" textType="text" style="width:120px" name="bufferName" id="bufferName" data-options="validType:['vLength[0,30,\'最多只能输入30个字符\']']"/>
							</td>
						</tr>
						<tr>
							<td>状态：</td>
							<td>
								<input class="easyui-combobox" style="width:120px" name="status" editable="false" id="status" required="true"/>
								<input style="width:120px" type="hidden" name="status"  id="statusHide"/>
							</td>
							<td style="padding-left:15px;">可用体积：</td>
							<td>
								<input class="easyui-numberbox" textType="text" style="width:120px" name="useVolumn" id="useVolumn" precision="5" max="9999999999999.99999"/>
							</td>
						</tr>
						<tr>
							<td>可用重量：</td>
							<td>
								<input class="easyui-numberbox" textType="text" style="width:120px" name="useWeight" d="useWeight" precision="5" max="9999999999999.99999"/>
							</td>
							<td style="padding-left:15px;">可用箱数：</td>
							<td>
								<input class="easyui-numberbox" textType="text" style="width:120px" name="useBoxnum" id="useBoxnum" precision="5" max="9999999999999.99999"/>
							</td>
						</tr>
						
						<tr id="creatorinfo">
							<td>创建人：</td>
							<td id="creator"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="createtm"></td>
						</tr>
						<tr id="editorinfo">
							<td>修改人：</td>
							<td id="editor"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="edittm"></td>
						</tr>
						<tr>
							<td colspan="4" style="text-align:center;">
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