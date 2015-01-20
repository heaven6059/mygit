<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>模块管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/easyui.css?version=1.0.5.0" />
<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/validator.css?version=1.0.5.0"/>
<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/icon.css?version=1.0.5.0>"/>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript" src="${domainStatic}/resources/js/authority_module.js?version=1.0.5.0"></script>	
<script type="text/javascript">
	var operationsUrl= BasePath + "/authority_operations/get_biz";
	var data;
	$(function($){
		$.ajax({
			url: operationsUrl,
			async: false,
			success: function(d){
		    	data=d;
		   	}
	 	});
	});
</script>
</head>
<body class="easyui-layout">
	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
			{"title":"新增","iconCls":"icon-add","action":"authority_module.insertRowauthority_module('module')","type":0},
			{"title":"删除","iconCls":"icon-del","action":"authority_module.removeBySelected('module')","type":0},
			{"title":"保存","iconCls":"icon-save","action":"authority_module.save('module')","type":0},
			{"title":"关闭","iconCls":"icon-close","action":"closeWindow('模块管理')","type":0}
		 ]/>
	</div>
	<div data-options="region:'center',border:false">
		<@p.datagrid id="module" name="" title="模块列表" loadUrl="/authority_module/list.json?sort=MODULE_SORT&order=desc" saveUrl="" defaultColumn="" 
 			isHasToolBar="false"  divToolbar=""  height="500" width="" onClickRowEdit="true" singleSelect="true" pageSize='50'  
			pagination="true" rownumbers="true"
 			columnsJsonList="[
 				{field:'id',title:'模块编号',width:70,hidden:true
 				},
 				{field:'name',title:'模块名称',width:150,
 					editor:{
 						type:'validatebox',
 						options:{
	 						required:true,
	 						missingMessage:'模块名称为必填项!'
 						}
 					}
 				},
 				{field:'url',title:'模块链接',width:150,
 					editor:{
 						type:'validatebox',
 						options:{
	 						required:true,
	 						missingMessage:'模块链接为必填项!'
 						}
 					}
 				},
			  {field:'sort',title:'排序',width:100,
 					editor:{
 						type:'numberbox',
 						options:{
	 						min:1,
	 						max:9999,
	 						required:true,
	 						missingMessage:'排序为必填项!'
 						}
 					}
 				},
 				{field:'remark',title:'备注',width:200,
 				 	editor:{
 						type:'validatebox'
 					}
 				},
 				{field:'operations',title:'操作权限',width:200,
		 			formatter: function(value,row,index){
		 				var displayValue='';
		 				if(value!=''&&value!=undefined){
							$.each(value.split(','),function(index, row_value){
								$.each(data,function(j,d){
									if(d.id==row_value){
										displayValue+=displayValue.length==0?d.text:','+d.text;
									}
								});
							});
		 				}
						return displayValue;
					},
					editor:{
 						type:'combotree',
 						options:{
 						  required:true,
 						  multiple:true,
 						  data:data
 						}
 					}
 				}
 			]"
 		/>
	</div>


</body>
</html>