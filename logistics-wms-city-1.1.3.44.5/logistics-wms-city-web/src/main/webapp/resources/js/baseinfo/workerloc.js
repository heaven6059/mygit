var workerloc= {};

//初始化用户信息，仓库信息
workerloc.initinfo = function(){
	//加载仓库信息
	//$('#dataGridJG_worker_loc').datagrid({'url':BasePath+'/bmdefloc/get_biz','title':'选择员工','pageNumber':1 });
	
	//加载用户信息
	$('#dataGridJG_worker').datagrid({
	    //数据加载完成，默认选中第一个
	    onLoadSuccess:function(data){
	    	$('#dataGridJG_worker').datagrid('selectRow', 0);
	    }
	});
};

//查询用户信息
workerloc.searchUser = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/system_user/list.json';
    //3.加载明细
    $( "#dataGridJG_worker").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG_worker").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_worker").datagrid( 'load' );
};
workerloc.clearSearchUser = function(){
	$('#searchForm').form('clear');
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
};

//点击用户，获取用户的仓库信息
workerloc.loadDefloc = function(rowData){
	if(rowData==null){
		return;
	}
	$.ajax({
		async : true,
		cache : true,
		type : 'GET',
		dataType : "json",
		url : BasePath+'/bs_worker_loc/findWorkerLoc/list?workerNo='+rowData.loginName,// 请求的action路径
		success : function(data) {
			$('#dataGridJG_worker_loc').datagrid('uncheckAll');
			var opts = $('#dataGridJG_worker_loc').datagrid('getData');  
			var rows = opts.rows;
			for(var j=0,jlength = rows.length;j<jlength;j++){
				for(var i=0,length=data.length;i<length;i++){
					if(data[i].locno==rows[j].locno){
						$('#dataGridJG_worker_loc').datagrid('checkRow',j);
					}
				}
			}
		}
	});
};

workerloc.save = function(){
	//获取选中的用户
	var userRow = $("#dataGridJG_worker").datagrid("getSelections");
	var usercode = userRow[0].loginName;
	//获取选中的仓库
	var deflocRows = $("#dataGridJG_worker_loc").datagrid("getChecked");
	if(deflocRows.length<1){
		alert('请选择仓别!',1);
		return;
	}
	$.messager.confirm("确认","你确定要将用户 '"+usercode+"'添加到这"+deflocRows.length+"个仓别中吗？", function (r){  
        if (r) {
        	var deflocStr = [];
        	$.each(deflocRows, function(index, item){
        		deflocStr.push(item.locno);
        	});
        	var data={
            	    "paramStr":usercode+"|"+deflocStr.join(",")
            };
        	workerloc.ajaxRequest(BasePath+'/bs_worker_loc/saveWorkerLoc',data,function(result){
       		 if(result=='success'){
       			 alert('保存成功!');
       		 }else{
       			 alert('保存失败,请联系管理员!',2);
       		 }
       	}); 
        }  
    }); 
};

workerloc.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

$(document).ready(function(){
	workerloc.initinfo();
	//workerloc.loadDefloc();
});