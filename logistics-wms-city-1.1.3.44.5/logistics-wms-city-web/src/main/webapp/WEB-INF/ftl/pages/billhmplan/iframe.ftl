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
			parent.billhmplan.loading("show","正在导入......");
			var ownerNo = parent.$("#ownerNo").combobox('getValue');
			var planNo = parent.$("#planNo").val();
			//var storelockNo = parent.$("#storelockNo").val();
			// var locno = parent.billhmplan.user.locno;
			//var creator = parent.billhmplan.user.loginName;
			//var creatorName = parent.billhmplan.user.username;
			
			//document.getElementById('fileForm').action= BasePath+ '/bill_hm_plan_dtl/importExcel?'+
			//'locno='+locno+
			//'&storelockNo='+storelockNo+
			//'&ownerNo='+ownerNo+
			//'&creator='+creator+
			//'&creatorName='+creatorName;
			
			document.getElementById('fileForm').action= BasePath+ '/bill_hm_plan_dtl/importExcel?'+
			'planNo='+planNo+
			'&ownerNo='+ownerNo;
			document.getElementById('fileForm').submit();
		}
	}
	
	
	$(function(){
		var result = "${result}";
		var msg = "${msg}";
		var uuId="${uuId}";
		if(result=="success"){
			parent.billhmplan.loading();
			parent.alert(msg,4);
			//parent.billhmplan.importSuccess("导入成功");
		}else {
			parent.billhmplan.loading();
			if(msg != ''){
				parent.alert(msg,2);
			}
		}
	});
</script>
<body>
	<div id="div_1">
		<form name="fileForm"  id="fileForm" method="post"  class="city-form" enctype="multipart/form-data">
	    	<input type="file" name="reportValue" id="reportValueSubmit" onchange="upload_(this)"/>
	    </form>
    </div>
</body>
</html>