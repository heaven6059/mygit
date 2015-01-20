<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂调度</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billWmRecedeDispatch/billWmRecedeDispatch.js?version=1.0.5.1"></script>
</head>

<body class="easyui-layout">

<div data-options="region:'center',border:false">
	<div id="mainTab" class="easyui-tabs" data-options="tabPosition:'top',fit:true">
		
		<#-- 普通调度 -->
		<div id="mainTab1" title="退厂单定位" style="padding:0px" >
	    	<#include  "/WEB-INF/ftl/pages/billWmRecedeDispatch/listTab_1.ftl" />
		</div>
		
		<#-- 出货续调 -->
		<div id="mainTab2" title="任务分派" style="padding:0px" >
			<#include  "/WEB-INF/ftl/pages/billWmRecedeDispatch/listTab_2.ftl">
		</div>
		
	</div>
</div>

</body>
</html>