var bmcircle = {};
bmcircle.locno;
bmcircle.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bmcircle/list.json?locno='+bmcircle.locno,'title':'商圈列表','pageNumber':1 });
};
bmcircle.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
bmcircle.initLocno = function(data){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bmcircle.locno = data.locno;
		}
	});
};
//弹出新增页面
bmcircle.showAdd=function(){
	
	//设置标题
	$('#showDialog').window({ title:"新增" });
	
	bmcircle.clearFormAndTip();
	//仓库编码设置
	$('#circleNo').removeAttr("readonly");
	$("#creatorDiv,#editorDiv,#info_update").hide();
	$("#info_save,#info_close").show();
	$('#showDialog').window('open'); 
};
bmcircle.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
};
//弹出修改页面
bmcircle.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}
	bmcircle.showEdit(checkedRows[0],1);
	$('#circleNo').attr('readOnly',true);
};
//弹出详情页面
bmcircle.showEdit = function(rowData,flag){
	
	var titleName = "修改";
	if(flag == 1){
		$("#creatorDiv,#editorDiv,#info_update,#info_close").show();
		$("#info_save").hide();
	}else{
		titleName = "查看";
		$("#creatorDiv,#editorDiv").show();
		$("#info_save,#info_update,#info_close").hide();
	}
	//设置标题
	$('#showDialog').window({ title:titleName });
	
	$('#circleNo,#circleName').validatebox('reset');
	
	//仓库编码设置为只读，不可修改
	$('#circleNo').attr("readonly","readonly");
	
	//设置信息
	bmcircle.setDetail(rowData);
	//弹窗
	$("#showDialog").window('open'); 
};
bmcircle.setDetail = function(rowData){
	//var fromObj=$('#dataForm');
	//$("input[id=locno]",fromObj).val(rowData.locno);
	$("#circleNo").val(rowData.circleNo);
	$("#circleName").val(rowData.circleName);
	$("#memo").val(rowData.memo);
	$('#createFlag').combobox("select",rowData.createFlag);
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	$("#creator").val(rowData.creator);
	$("#createtm").val(rowData.createtm);
	$("#editor").val(rowData.editor);
	$("#edittm").val(rowData.edittm);
};
bmcircle.checkExistFun = function(url,checkColumnJsonData){
	var checkExist=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  totalData = parseInt(totalData,10);
			  if(totalData>0){
				  checkExist=true;
			  }
		  }
     });
 	return checkExist;
};
//新增保存
bmcircle.addCircle = function(){
	//debugger;
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
     var checkUrl=BasePath+'/bmcircle/get_count.json';
     var circleNo = $("input[id=circleNo]",fromObj).val();
     var checkDataNo={"circleNo":circleNo};
     checkDataNo.locno = bmcircle.locno;
     if(bmcircle.checkExistFun(checkUrl,checkDataNo)){
    	  alert('商圈编码已存在,不能重复!',1);
    	  $("#circleNo").focus();
    	  return;
     }
     
	 //3. 保存
     var url = BasePath+'/bmcircle/addCircle?locno='+bmcircle.locno;
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(returnMsg){
				if(returnMsg=='success'){
					 alert('新增成功!');
					 $('#showDialog').window('close'); 
					 //4.保存成功,清空表单
					 bmcircle.loadDataGrid();
					 //bmdefloc.clearAll();
					 return;
				 }else{
					 alert('新增异常,请联系管理员!',2);
				 }
		    },
			error:function(){
				alert('新增失败,请联系管理员!',2);
			}
	   });
};
//修改
bmcircle.modifyCircle = function(){
	
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    var url = BasePath+'/bmcircle/modifyCircle?locno='+bmcircle.locno;
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(){
				
   			},
   			success: function(data){
   				if(data=="success"){
   					alert('修改成功!');
					$("#showDialog").window('close'); 
					bmcircle.loadDataGrid();
					//bmdefloc.clearAll();
					return;
				}else{
					alert('修改失败,请联系管理员!',2);
				}
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	});
    
};
//删除
bmcircle.deleteCircle=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要删除这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var circleNoStrs = [];
        	$.each(checkedRows, function(index, item){
        		circleNoStrs.push(item.circleNo);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
        	var data={
        	    "circleNoStrs":circleNoStrs.join(",")
        	};
        	
        	//3校验仓库下是否有绑定用户时
        	var url = BasePath+'/bmcircle/findIsStore';
        	bmcircle.ajaxRequest(url,data,function(result,returnMsg){
        		 if(result=='success'){
        			    //4. 删除
        			    url = BasePath+'/bmcircle/deleteCircle';
        			    bmcircle.ajaxRequest(url,data,function(result,returnMsg){
        	        		 if(result=='success'){
        	        			 //4.删除成功,清空表单
        	        			 bmcircle.loadDataGrid();
        	        			 alert('删除成功!');
        	        		 }else{
        	        			 alert('删除失败,请联系管理员!',2);
        	        		 }
        	        	}); 
        		 }else if(result=='warn'){
        			 alert('商圈有绑定客户，不能删除!',0);
        			 return;
        		 }else {
        			 alert('校验商圈下是否有绑定客户时异常,请联系管理员!',2);
        			 return;
        		 }
        	}); 
        }
	});     
};
//查询商圈信息
bmcircle.searchCircle = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bmcircle/list.json?locno='+bmcircle.locno;
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};
//清楚查询条件
bmcircle.searchClear = function(){
	$('#searchForm').form("clear");
	searchForm('/bmcircle/list.json?locno='+bmcircle.locno);
};
//取消按钮
bmcircle.closeCircle = function(){
	$('#showDialog').window('close');
};
//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
} 
$(document).ready(function(){
	bmcircle.initLocno();
});