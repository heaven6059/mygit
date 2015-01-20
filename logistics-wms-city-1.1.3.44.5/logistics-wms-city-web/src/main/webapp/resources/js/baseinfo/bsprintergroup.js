var bsprintergroup= {};
bsprintergroup.locno;
//初始化用户信息，仓库信息
bsprintergroup.initinfo = function(){
	//加载打印机组信息
	$('#dataGridJG_printer').datagrid({'url':BasePath+'/bm_defprinter/get_biz?locno='+bsprintergroup.locno});
	
	//加载码头信息
	$('#dataGridJG_printer_group').datagrid({
	    url:BasePath+'/bm_defprinter_group/list.json?locno='+bsprintergroup.locno,
	    'pageNumber':1,
	    //数据加载完成，默认选中第一个
	    onLoadSuccess:function(data){
	    	$('#dataGridJG_printer_group').datagrid('selectRow', 0);
	    }
	});
};

//查询用户信息
bsprintergroup.searchDock = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_defprinter_group/list.json?locno='+bsprintergroup.locno;
    //3.加载明细
    $( "#dataGridJG_printer_group").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG_printer_group").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_printer_group").datagrid( 'load' );
};
bsprintergroup.clearSearchCondition = function(){
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


bsprintergroup.loadbsprintergroup = function(rowData){
	if(rowData==null){
		return;
	}
	$.ajax({
		async : true,
		cache : true,
		type : 'GET',
		dataType : "json",
		url : BasePath+'/bs_printer_group/get_biz?printerGroupNo='+rowData.printerGroupNo+"&locno="+bsprintergroup.locno,// 请求的action路径
		success : function(data) {
			$('#dataGridJG_printer').datagrid('uncheckAll');
			var opts = $('#dataGridJG_printer').datagrid('getData');  
			var rows = opts.rows;
			for(var j=0,jlength = rows.length;j<jlength;j++){
				for(var i=0,length=data.length;i<length;i++){
					if(data[i].printerNo==rows[j].printerNo){
						$('#dataGridJG_printer').datagrid('checkRow',j);
					}
				}
			}
		}
	});
};

bsprintergroup.save = function(){
	//获取选中的打印组
	var dockRow = $("#dataGridJG_printer_group").datagrid("getSelections");
	var printerGroupNo = dockRow[0].printerGroupNo;
	//获取选中的打印机
	var printerRows = $("#dataGridJG_printer").datagrid("getChecked");
	$.messager.confirm("确认","你确定要执行此操作吗？", function (r){  
        if (r) {
        	var printerNoStr = [];
        	$.each(printerRows, function(index, item){
        		printerNoStr.push(item.printerNo);
        	});
        	var data={
            	    "printerGroupNo":printerGroupNo,"printerNoStr":printerNoStr.join(",")
            };
        	bsprintergroup.ajaxRequest(BasePath+'/bs_printer_group/savebsprintergroup',data,function(result){
       		 if(result=='success'){
       			 alert('保存成功!');
       		 }else{
       			 alert('保存失败,请联系管理员!',2);
       		 }
       	}); 
       }  
    }); 
};

bsprintergroup.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

$(document).ready(function(){
	bsprintergroup.loadLoc();
	bsprintergroup.initinfo();
});

bsprintergroup.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bsprintergroup.locno = data.locno;
		}
	});
};

