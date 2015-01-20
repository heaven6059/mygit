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
			parent.alert("请选择.xlsx或.xls文件格式");
			return false;
		}else{
			parent.stockadj.loading("show","正在导入......");
			document.getElementById('fileForm').submit();
		}
	}
	$(function(){
		var result = "${result}";
		var msg = "${msg}";
		if(result=="success"){
			parent.stockadj.loading();
			parent.stockadj.importSuccess();
		}else {
			parent.stockadj.loading();
			if(msg != ''){
				parent.alert(msg,2);
			}
		}
	});
</script>
<body>
	<div id="div_1">
		<form name="fileForm"  id="fileForm" method="post" action="../bill_stock_adj_dtl/importExcel" class="city-form" enctype="multipart/form-data">
			<input type="hidden" name="adjNo" value="${adjNo}"/>
			<input type="hidden" name="ownerNo" value="${ownerNo}"/>
	    	<input type="file" name="reportValue" id="reportValueSubmit" onchange="upload_(this)" accept='*.xls,*.xlsx'/>
	    </form>
    </div>
</body>
</html>