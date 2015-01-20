<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>拣货调度</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billOmExpDispatch/billOmExpDispatch.js?version=1.0.5.2"></script>
	<script type="text/javascript" src="${domainStatic}/resources/common/other-lib/datagrid-detailview.js?version=1.0.5.1"></script>
	<style>
		.ddv table tr td{border:none}
	</style>
</head>

<body class="easyui-layout">
<div data-options="region:'center',border:false">
	<div id="mainTab" class="easyui-tabs" data-options="tabPosition:'top',fit:true">
	
	<#-- 普通调度 -->
	<div id="mainTab1" title="普通调度" style="padding:0px" height='100%' width='100%' >
    	<#include  "/WEB-INF/ftl/pages/billOmExpDispatch/listTab_1.ftl" />
	</div>
	
	
	<#-- 出货续调 -->
	<div id="mainTab2" title="出货续调" style="padding:0px" height='100%' width='100%' >
		<#include  "/WEB-INF/ftl/pages/billOmExpDispatch/listTab_2.ftl">
	</div>
	
	
	<#-- 调度异常查看 -->
	<div id="mainTab3" title="调度异常查看" style="padding:0px" height='100%' width='100%' >
		<#include  "/WEB-INF/ftl/pages/billOmExpDispatch/listTab_3.ftl">
	</div>
</div>
</div>
</body>
</html>