<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<style>
		body{margin:0px;padding:0px;height:25px;}
	</style>
</head>
<script type="text/javascript">
	function upload_(obj){
		var fileInput = document.getElementById('reportValueSubmit');
		var fileName = fileInput.value;
		var type = fileName.substring(fileName.length-5,fileName.length);
		var type2 = fileName.substring(fileName.length-4,fileName.length);
		if(fileName==""){
			parent.alert("请选择文件");
			return false;
		}else if(type!=".xlsx"&&type!=".xlsx"&&type2!=".xls"&&type2!=".xls"){
			parent.alert("请选择Excel文件格式");
			return false;
		}else{
			parent.loading("show","正在导入......");
			document.getElementById('fileForm').submit();
		}
	}
	$(function(){
		var result = "${result}";
		var msg = "${msg}";
		if(result=="fail"){
			parent.loading();
			parent.alert(msg,2);
		}else if(result=="success"){
			parent.loading();
			parent.importSuccess();
		}
	});
</script>
<body>
	<div id="div_1">
		<form name="fileForm"  id="fileForm" method="post" action="../wmrecededtl/import" class="city-form" enctype="multipart/form-data">
			<input type="hidden" name="recedeNo" value="${recedeNo}"/>
			<input type="hidden" name="ownerNo" value="${ownerNo}"/>
	    	<input type="file" name="reportValue" id="reportValueSubmit" onchange="upload_(this)"/>
	    </form>
    </div>
</body>
</html>