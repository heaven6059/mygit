var oslinebuffer = {};
//状态
oslinebuffer.statusData= {
	"0":"空闲",
	"1":"占用",
	"2":"满储"
};
//仓别
oslinebuffer.locno;
oslinebuffer.statusSelectData= [
 {"value":"0","text":"空闲"},
 {"value":"1","text":"占用"},
 {"value":"2","text":"满储"}
];

//页面加载
$(document).ready(function(){
	//初始化状态信息
	oslinebuffer.initStatusData();
	//初始化仓别信息
	oslinebuffer.initLocData();
	
	oslinebuffer.initLineData("search");
	oslinebuffer.initWareData("search");
	oslinebuffer.initLineData();
	oslinebuffer.initWareData();
	
	//加载列表
	//oslinebuffer.loadDataGrid();
	//新增按钮
	$("#info_add").click(oslinebuffer.save_do);
	//修改按钮
	$("#info_edit").click(oslinebuffer.edit_do);
	
	//级联完整路线信息
	$("#lineName").blur(oslinebuffer.setLineFname);
	$('#transportType').combobox({
	    onSelect:function(data){
	    	oslinebuffer.setLineFname();
	    }
	});
	$('#deliverType').combobox({
	    onSelect:function(data){
	    	oslinebuffer.setLineFname();
	    }
	});
});

oslinebuffer.initStatusData = function(){
	$('#search_status').combobox({
	    data:oslinebuffer.statusSelectData,
	    valueField:'value',    
	    textField:'text',
	    panelHeight:"300"
	});
	$('#status').combobox({
	    data:oslinebuffer.statusSelectData,
	    valueField:'value',    
	    textField:'text',
	    panelHeight:"300"
	});
};

//仓别
oslinebuffer.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			oslinebuffer.locno = data.locno;
		}
	});
};

//线路
oslinebuffer.initLineData = function(type){
	var locNo = oslinebuffer.locno;
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/os_defline/get_biz?locno='+locNo,
		success : function(data) {
			if(type=="search"){
				$('#search_lineNo').combobox({
				    data:data,
				    valueField:'lineNo',    
				    textField:'lineName',
				    panelHeight:"300"
				});
			}else{
				$('#lineNo').combobox({
				    data:data,
				    valueField:'lineNo',    
				    textField:'lineName',
				    panelHeight:"300"
				});
			}
			//oslinebuffer.lineData = oslinebuffer.converStr2JsonObj(data);
		}
		
	});
};

//仓区
oslinebuffer.initWareData = function(type){
	var locNo = oslinebuffer.locno;
	$.ajax({
		async : false,
		cache : true,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defware/get_biz?locno='+locNo,
		success : function(data) {
			if(type=="search"){
				$('#search_wareNo').combobox({
			     valueField:"wareNo",
			     textField:"wareName",
			     data:data,
			     panelHeight:"300",
			     onSelect:function(){
			    	 oslinebuffer.initAreaData("search");
				 }
			  });
			}else{
				$('#wareNo').combobox({
			     valueField:"wareNo",
			     textField:"wareName",
			     data:data,
			     panelHeight:"300",
			     onSelect:function(data){
			    	 oslinebuffer.initAreaData();
				 }
			  });
			}
		}
	});
};

//库区
oslinebuffer.initAreaData = function(type){
	var locNo = oslinebuffer.locno;
	var wareNo = $('#search_wareNo').combobox("getValue");
	if(type!="search"){
		wareNo = $('#wareNo').combobox("getValue");
	}
	$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		data:{
			"locno":locNo,
			"wareNo":wareNo
		},
		url:BasePath+'/cm_defarea/get_biz',
		success : function(data) {
				if(type=="search"){
					$('#search_areaNo').combobox({
					     valueField:"areaNo",
					     textField:"areaName",
					     data:data,
					     panelHeight:"300",
					     onSelect:function(data){
					    	 oslinebuffer.initStockData("search");
						 }
					  }).combobox("select",data[0]==""||data[0]==null?"":data[0].areaNo);
				}else{
					$('#areaNo').combobox({
					     valueField:"areaNo",
					     textField:"areaName",
					     data:data,
					     panelHeight:"300",
					     onSelect:function(data){
					    	 oslinebuffer.initStockData();
						 }
					}).combobox("select",data[0]==""||data[0]==null?"":data[0].areaNo);
				}
		}
	});
};

//通道
oslinebuffer.initStockData = function(type){
	var locNo = oslinebuffer.locno;
	var wareNo =  $('#search_wareNo').combobox("getValue");
	var areaNo =  $('#search_areaNo').combobox("getValue");
	if(type!="search"){
		wareNo = $('#wareNo').combobox("getValue");
		areaNo =  $('#areaNo').combobox("getValue");
	}
	$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		data:{
			"locno":locNo,
			"wareNo":wareNo,
			"areaNo":areaNo
		},
		url:BasePath+'/cm_defstock/get_biz',
		success : function(data) {
			if(type=="search"){
				$('#search_stockNo').combobox({
				     valueField:"stockNo",
				     textField:"stockNo",
				     data:data,
				     panelHeight:"300",
				     onSelect:function(data){
				    	 oslinebuffer.initCellNoData("search");
				     }
				  }).combobox("select",data[0]==""||data[0]==null?"":data[0].stockNo);
				
			}else{
				$('#stockNo').combobox({
				     valueField:"stockNo",
				     textField:"stockNo",
				     data:data,
				     panelHeight:"300",
				     onSelect:function(data){
				    	 oslinebuffer.initCellNoData();
				    	 oslinebuffer.initAStockData();
					 }
				  }).combobox("select",data[0]==""||data[0]==null?"":data[0].stockNo);
			}
		}
	});
};
//储位
oslinebuffer.initCellNoData = function(type){
	var locNo = oslinebuffer.locno;
	var wareNo =  $('#search_wareNo').combobox("getValue");
	var areaNo =  $('#search_areaNo').combobox("getValue");
	var stockNo = $('#search_stockNo').combobox("getValue");
	if(type!="search"){
		wareNo = $('#wareNo').combobox("getValue");
		areaNo =  $('#wareNo').combobox("getValue");
		stockNo = $('#stockNo').combobox("getValue");
	}
	$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		data:{
			"locno":locNo,
			"wareNo":wareNo,
			"areaNo":areaNo,
			"stockNo":stockNo
		},
		url:BasePath+'/cm_defcell/get_biz',
		success : function(data) {
			if(type=="search"){
				$('#search_cellNo').combobox({
				     valueField:"cellNo",
				     textField:"cellNo",
				     data:data,
				     panelHeight:"300"
				  }).combobox("select",data[0]==""||data[0]==null?"":data[0].cellNo);
			}else{
				$('#cellNo').combobox({
				     valueField:"cellNo",
				     textField:"cellNo",
				     data:data,
				     panelHeight:"3"
				  }).combobox("select",data[0]==""||data[0]==null?"":data[0].cellNo);
			}
		}
	});
};

oslinebuffer.initAStockData = function(){
	var wareNo =  $('#wareNo').combobox("getValue");
	var areaNo =  $('#areaNo').combobox("getValue");
	var stockNo = $('#stockNo').combobox("getValue");
	$("#aStockNo").val(wareNo+areaNo+stockNo);
};
//将数组封装成一个map
oslinebuffer.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].lineNo +"\":\""+data[i].lineName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


oslinebuffer.statsuFormatter = function(value,rowData,rowIndex){
	return oslinebuffer.statusData[value];
};


//查询区域信息
oslinebuffer.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/os_line_buffer/list.json?locno='+oslinebuffer.locno;
	
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

oslinebuffer.searchLocClear = function(){
	$('#searchForm').form("clear");
};

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

//加载数据
oslinebuffer.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/os_line_buffer/list.json?locno='+oslinebuffer.locno,
    			'pageNumber':1
    		});
};

//格式化数据
oslinebuffer.transportTypeFormatter = function(value, rowData, rowIndex){
	return oslinebuffer.transportType[value];
};

oslinebuffer.deliverTypeFormatter = function(value, rowData, rowIndex){
	return oslinebuffer.deliverType[value];
};
oslinebuffer.lineFnameFormatter = function(value, rowData, rowIndex){
	return oslinebuffer.deliverType[rowData.deliverType]+oslinebuffer.transportType[rowData.transportType]+rowData.lineName;
};

oslinebuffer.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};



//新增
oslinebuffer.addInfo = function(){
	$('#showDialog').window({
		title:"新增"
	});
	$("#creatorinfo").hide();
	$("#editorinfo").hide();
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
	
	var wareNoFirst = $('#wareNo').combobox("getData");
	$('#wareNo').combobox("select",wareNoFirst[0].wareNo);
	
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('enable');
		}
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",true);
		}
		if($(this).attr("textType")=="text"){
			$(this).val("");
		}
	});
	
	//清除数据
	//$("#dataForm").form("clear");
	/*
	var statusDataTemp = $("#status").combobox("getData");
		$("#status").combobox("select",statusDataTemp[0].value).combobox('disable');
		$("#statusHide").val(statusDataTemp[0].value);*/
	
};

//清空表单
oslinebuffer.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};
//清除校验
oslinebuffer.clearAalidate = function(){
	$(this).validatebox('reset');
};


oslinebuffer.checkExist = function(){
	var lineNo = $("#lineNo").combobox("getValue");
	var wareNo = $("#wareNo").combobox("getValue");
	var areaNo = $("#areaNo").combobox("getValue");
	var stockNo = $("#stockNo").combobox("getValue");
	var cellNo = $("#cellNo").combobox("getValue");
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/os_defline/checkExist',
		  data:{
			"lineNo":lineNo,
			"wareNo":wareNo,
			"areaNo":areaNo,
			"stockNo":stockNo,
			"cellNo":cellNo
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该线路暂存区已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

//保存
oslinebuffer.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     
     if(!oslinebuffer.checkExist()){
     	return;
     }
     
	 //2. 保存
     var url = BasePath+'/os_line_buffer/addOsLineBuffer';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 oslinebuffer.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

//删除
oslinebuffer.del = function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.lineNo+"|"+item.wareNo+"|"+item.areaNo+"|"+item.stockNo+"|"+item.cellNo);
        	}); 
            //2.绑定数据
            var url = BasePath+'/os_line_buffer/deleteOsLineBuffer';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	oslinebuffer.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 oslinebuffer.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

oslinebuffer.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		oslinebuffer.edit(checkedRows[0],"edit");
	}	
};

//编辑
oslinebuffer.edit = function(rowData,type){
	var title = "详情";
	if(type=="edit"){
		title = "修改";
	}
	//设置标题
	$('#showDialog').window({
		title:title
	});
	$("#creatorinfo").show();
	$("#editorinfo").show();
	//设置信息
	oslinebuffer.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

oslinebuffer.detail = function(rowData,type){
	
	$('#dataForm').form('load',rowData);
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('disable');
		}
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",false);
		}
	});
	$("#status").combobox('enable');
	$("#statusHide").attr("disabled",true);
	
	//用户信息
	$("#creator").text(rowData.creator);
	$("#createtm").text(rowData.createtm);
	$("#editor").text(rowData.editor);
	$("#edittm").text(rowData.edittm);
	
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	if(type=="edit"){
		$("#info_edit").show();
	}else{
		$("#info_edit").hide();
	}
};

//修改
oslinebuffer.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/os_line_buffer/editOsLineBuffer';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 oslinebuffer.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};