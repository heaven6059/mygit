<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>储位包装设置</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/cscellpacksetting.js?version=1.0.6.1"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/cscellpacksetting.css"/>" />
</head>
<body class="easyui-layout">


	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
	        {"title":"新增","iconCls":"icon-add","action":"cscellpacksetting.addInfo();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"cscellpacksetting.editInfo();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"cscellpacksetting.del();","type":3},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl="/cs_cell_pack_setting/getByPage.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="储位包装设置"
		       		isHasToolBar="false" divToolbar="#lineSearchForm" onClickRowEdit="false"   pagination="true"
			       	rownumbers="true" singleSelect = "false" height="520" emptyMsg=""  showFooter="true"
			       	columnsJsonList="[
			           	{field : ' ',width : 50, checkbox:true},
			           	{field : 'ownerNo',title : '货主',width : 80,formatter:cscellpacksetting.ownerNoFormatter,align:'left'},
						{field : 'areaType',title : '储位类型',width :150,formatter:cscellpacksetting.areaTypeFormatter,align:'left'},
						{field : 'packSpec',title : '包装规格',width :150,align:'left'},
						{field : 'limitQty',title : '存储数量',width :150,align:'right'}
			      	]"
				   	jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		            		cscellpacksetting.edit(rowData);
		            }}'
		    	/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 明细信息div -->
	<div id="showDialog" class="easyui-dialog" style="padding:10px;height:350px;width:620px;"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
			</div>
			<div data-options="region:'center',border:false" id = 'detailDiv'>
				<form name="dataForm" id="dataForm" method="post" class="city-form">	
						<input type="hidden" name="lineNo" id="lineNo">
						<table width='580'>
					    	<tr>
								<td>货主：</td>
								<td><input class="easyui-combobox" data-options="editable:false" style="width:150px" name="ownerNo" id="ownerNo" required="true"/></td>
								<td>储位类型：</td>
								<td><input class="easyui-combobox" data-options="editable:false" style="width:150px"  name="areaType" id="areaType" required="true"/>
									<input class="hide" name="areaType" id="areaTypeHide" type="hidden"/></td>
							</tr>
							<tr>
								<td>包装规格：</td>
								<td><input class="easyui-combobox"  data-options="editable:false" style="width:150px" name="packSpec" id="packSpec" required="true"/>
									<input class="hide" name="packSpec" id="packSpecHide" type="hidden"/></td>
								<td>存储数量：</td>
								<td><input class="easyui-numberbox ipt" precision="5" max="99999999.99999" style="width:150px" name="limitQty" id="limitQty" required="true"/></td>
							</tr>
							<tr>
								<td colspan="4" style="text-align:center;">
									<a id="info_add" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									<a id="info_edit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									<a id="info_close" href="javascript:cscellpacksetting.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
								</td>
							</tr>
						</table>
					</form>
			</div>
		</div>
	</div>
	<#-- 明细信息div -->
	
</body>
</html>