<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>仓别与用户关系维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/workerloc.js?version=1.0.5.0"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/workerloc.css?version=1.0.5.0" />
</head>

<body class="easyui-layout">
	
	<#-- 工具菜单div -->
    <div data-options="region:'north',border:false" class="toolbar-region">
	       <@p.toolbar id="toolbar"  listData=[
	       				 {"title":"查询","iconCls":"icon-search","action":"workerloc.searchUser()", "type":0},
	                     {"title":"清除","iconCls":"icon-remove","action":"workerloc.clearSearchUser()", "type":0},
				         {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('仓别与用户关系维护')","type":0}
	                   ]
			  />
	</div>	
	  
	<#-- 仓别查询 -->
	<div data-options="border:false" id="storeSearchDiv" style="padding:10px;">
		<form name="searchForm" id="searchForm" method="post" class="city-form">
			<table>
            	<tr>
                	<td class="common-td">员工代码：</td>
                    <td><input id="loginName" style="width:120px" class="easyui-validatebox ipt" name="loginName" /> </td>
                    <td class="common-td blank">员工名称：</td>
                    <td><input id="username" style="width:120px" class="easyui-validatebox ipt" name="username"/></td>
               	</tr>
           </table>
		</form>
	</div>
	
	
	<#-- 员工查询 -->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			
			<div data-options="region:'west',split:false" style="width:430px;">
				<@p.datagrid id="dataGridJG_worker"  loadUrl="/system_user/get_biz" saveUrl=""  title="选择员工"
			     	isHasToolBar="false" divToolbar="#storeSearchDiv"   onClickRowEdit="false"   pagination="true"
				     rownumbers="true" emptyMsg="" 
				     columnsJsonList="[
					 	{field : 'loginName',title : '员工代码',width : 200,align:'left'},
						{field : 'username',title : '员工姓名',width : 200,align:'left'}
				     ]"
					 jsonExtend='{onSelect:function(rowIndex, rowData){
			                            // 触发点击方法  调JS方法
			         	workerloc.loadDefloc(rowData);
				}}'/>
			</div>
			
			<div data-options="region:'center',split:false" style="margin-left:5px;">
				<div id="saveLocnoDiv">
					<@p.toolbar  id="savetoolbar" listData=[
		                   {"id":"btn-close","title":"保存","iconCls":"icon-save","action":"workerloc.save()","type":0}
			           ]
					/>
				</div>
				
				<@p.datagrid id="dataGridJG_worker_loc"  loadUrl="/bmdefloc/get_biz?locType=2" saveUrl=""   
					defaultColumn=""  title="选择仓别"
			        isHasToolBar="false" divToolbar="#saveLocnoDiv"  onClickRowEdit="false"   pagination="false"
				    rownumbers="true"  singleSelect = "false"  emptyMsg="" 
				    columnsJsonList="[
				          {field : 'locno',checkbox:true},
						  {field : 'locname',title : '仓别名称',width : 243,align:'left'}
					]" 
					jsonExtend='{onSelect:function(rowIndex, rowData){
			             // 触发点击方法  调JS方法
			             // defdock.detail(rowData);
			    }}'/>
			</div>
			
		</div>
	</div>
		
</body>
</html>