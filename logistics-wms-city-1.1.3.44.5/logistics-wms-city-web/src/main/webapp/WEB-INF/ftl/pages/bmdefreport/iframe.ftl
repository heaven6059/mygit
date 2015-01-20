<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
</head>
<script type="text/javascript">
	function upload_(obj){
		document.getElementById('locnoSubmit').value=parent.bmdefreport.user.locno;
		document.getElementById('loginNameSubmit').value=parent.bmdefreport.user.loginName;
		document.getElementById('fileNameSubmit').value=obj.value;
		document.getElementById('fileForm').submit();
	}
	function removeFile(){
		$('#div_1').show();
		$('#div_2').hide();
		parent.removeFileName(fileName);
	}
	function setFileName(fileName){
		$('#div_1').hide();
		$('#div_2').show();
		$('#fileName').html(fileName);
		parent.setFileName(fileName);
	}
	var fileName = "${fileName}";
	var status = "${status}";
	$(function(){
		if(status==null||status==""){
			removeFile();
		}else if(status=="fail"){
			parent.alert("上传失败,请联系管理员");
			$('#div_1').show();
			$('#div_2').hide();
		}else{
			setFileName(fileName);
		}
	});
</script>
<body>
	<div id="div_1">
		<form name="fileForm" id="fileForm" method="post" action="../bm_defreport/upLoad" class="city-form" enctype="multipart/form-data">
			<input type="hidden" name="locno" id="locnoSubmit"/>
			<input type="hidden" name="loginName" id="loginNameSubmit"/>
			<input type="hidden" name="fileName" id="fileNameSubmit"/>
	    	<input type="file" name="reportValue" id="reportValueSubmit" onchange="upload_(this)"/>
	    </form>
    </div>
    <div id="div_2">
    	<button onclick="removeFile()">删除</button><span id="fileName"></span>
    </div>
</body>
</html>