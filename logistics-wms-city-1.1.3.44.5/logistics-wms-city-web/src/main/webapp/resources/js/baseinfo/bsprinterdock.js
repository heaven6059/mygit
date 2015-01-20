var bsprinterdock= {};
bsprinterdock.locno;
//初始化用户信息，仓库信息
bsprinterdock.initinfo = function(){
	//加载打印机组信息
	//$('#dataGridJG_worker_loc').datagrid({'url':BasePath+'/bm_defprinter_group/get_biz?locno='+bsprinterdock.locno+'&status=1'});
	
	//加载码头信息
	$('#dataGridJG_dock').datagrid({
	    onLoadSuccess:function(data){
	    	$('#dataGridJG_dock').datagrid('selectRow', 0);
	    }
	});
};

//查询用户信息
bsprinterdock.searchDock = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_defdock/list.json?locno='+bsprinterdock.locno;
    //3.加载明细
    $( "#dataGridJG_dock").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG_dock").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_dock").datagrid( 'load' );
};

//查询用户信息
bsprinterdock.clearDockForm = function(){
    $( "#searchForm").form('clear');
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

//点击码头，获取打印机组信息
bsprinterdock.loadBsprinterDock = function(rowData){
	if(rowData==null){
		return;
	}
	$.ajax({
		async : true,
		cache : true,
		type : 'GET',
		dataType : "json",
		url : BasePath+'/bs_printer_dock/get_biz?dockNo='+rowData.dockNo+'&locno='+bsprinterdock.locno,// 请求的action路径
		success : function(data) {
			$('#dataGridJG_worker_loc').datagrid('uncheckAll');
			var opts = $('#dataGridJG_worker_loc').datagrid('getData');  
			var rows = opts.rows;
			for(var j=0,jlength = rows.length;j<jlength;j++){
				for(var i=0,length=data.length;i<length;i++){
					if(data[i].printerGroupNo==rows[j].printerGroupNo){
						$('#dataGridJG_worker_loc').datagrid('checkRow',j);
					}
				}
			}
		}
	});
};

bsprinterdock.save = function(){
	//获取选中的码头
	var dockRow = $("#dataGridJG_dock").datagrid("getSelections");
	var dockNo = dockRow[0].dockNo;
	//获取选中的打印组
	var deflocRows = $("#dataGridJG_worker_loc").datagrid("getChecked");
	$.messager.confirm("确认","你确定要执行此操作吗？", function (r){  
        if (r) {
        	var groupStr = [];
        	$.each(deflocRows, function(index, item){
        		groupStr.push(item.printerGroupNo);
        	});
        	var data={
            	    "paramStr":dockNo+"|"+groupStr.join(",")
            };
        	bsprinterdock.ajaxRequest(BasePath+'/bs_printer_dock/saveBsPrinterDock',data,function(result){
       		 if(result=='success'){
       			 alert('保存成功!');
       		 }else{
       			 alert('保存失败,请联系管理员!',2);
       		 }
       	}); 
        }  
    }); 
};

bsprinterdock.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

$(document).ready(function(){
	bsprinterdock.loadLoc();
	bsprinterdock.initinfo();
});

bsprinterdock.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bsprinterdock.locno = data.locno;
		}
	});
};

