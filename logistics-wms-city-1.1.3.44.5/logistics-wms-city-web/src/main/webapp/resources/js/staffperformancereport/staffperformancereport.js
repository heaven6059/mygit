
var staffperformancereport = {};

//获取表头定义
staffperformancereport.getDataGridJGColumns = function(){
	var cateOneCondition=$("#cateOneCondition").combobox("getValues");
	var cateTwoCondition=$("#cateTwoCondition").combobox("getValues");
	var cateThreeCondition=$("#cateThreeCondition").combobox("getValues");
	var columns="";
	columns+="[[";
	columns+="{title:'工号',field:'loginName',width:100,align:'left',rowspan: 2},";
	columns+="{title:'姓名',field:'userName',width:80,align:'left',rowspan: 2},";
	if (cateOneCondition!=null && cateOneCondition.length > 0 && cateOneCondition[0] != '') {
		columns+="{title:'大类一',field:'cateOneName',width:80,align:'left',rowspan: 2},";
	}
	
	if (cateTwoCondition!=null && cateTwoCondition.length > 0 && cateTwoCondition[0] != '') {
		columns+="{title:'大类二',field:'cateTwoName',width:80,align:'left',rowspan: 2},";
	}
	
	if (cateThreeCondition!=null && cateThreeCondition.length > 0 && cateThreeCondition[0] != '') {
		columns+="{title:'大类三',field:'cateThreeName',width:80,align:'left',rowspan: 2},";
	}
	columns+="{title:'收货',field:null,width:210,colspan: 2},";
	columns+="{title:'分货模块',field:null,width:160},";
	columns+="{title:'拣货模块',field:null,width:160,colspan: 2},";
	columns+="{title:'退仓模块',field:null,width:160,colspan: 2},";
	columns+="{title:'其他',field:null,width:160,colspan: 2},";
	columns+="{title:'库存作业',field:null,width:240,colspan: 3},";
	columns+="{title:'盘点',field:null,width:160,colspan: 2},";
	columns+="{title:'合计',field:'totalQty',width:160,rowspan: 2}";
	columns+="],[";
	columns+="{title:'到货收货',field:'showDhshQty',width:130,align:'right'},";
	columns+="{title:'收货验收',field:'shysQty',width:80,align:'right'},";
	columns+="{title:'分货复核 ',field:'fhfhQty',width:80,align:'right'},";
	columns+="{title:'拣货量',field:'jhlQty',width:80,align:'right'},";
	columns+="{title:'复核打包量',field:'fhdbQty',width:80,align:'right'},";
	columns+="{title:'退货收货',field:'thshQty',width:80,align:'right'},";
	columns+="{title:'退货验收',field:'thysQty',width:80,align:'right'},";
	columns+="{title:'其他入库',field:'qtrkQty',width:80,align:'right'},";
	columns+="{title:'其他出库',field:'qtckQty',width:80,align:'right'},";
	columns+="{title:'入库上架',field:'rksjQty',width:80,align:'right',rowspan: 2},";
	columns+="{title:'退仓上架',field:'tcsjQty',width:80,align:'right'},";
	columns+="{title:'即时移库',field:'jsykQty',width:80,align:'right'},";
	columns+="{title:'初盘',field:'cpQty',width:80,align:'right'},";
	columns+="{title:'复盘',field:'fpQty',width:80,align:'right'}";
	columns+="]]";
	return eval(columns);
};

//加载Grid数据Utils
staffperformancereport.loadGridDataUtil = function(gridDataId,url,queryParams,columns){
    var tempObj = $('#'+gridDataId);    
    tempObj.datagrid({
		queryParams : queryParams,
		url : url,
		columns : columns
	});
};

staffperformancereport.getDate = function(n){
	var nowDate = new Date();
	var newDate = new Date();
	var newTimes = nowDate.getTime() + (n*24*3600000);
	newDate.setTime(newTimes);
	var y = newDate.getFullYear();
	var m = newDate.getMonth()+1;
	var d = newDate.getDate();
	var dateStr = y+"-";
	dateStr += (m<10?("0"+m):(m))+"-";
	dateStr += (d<10?("0"+d):(d));
	return dateStr;
};

//校验开始日期和结束日期的大小
staffperformancereport.isStartEndDate = function(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){  
        	alert("开始日期不能大于结束日期!");
            return false;   
        }else if(((allEndDate.getTime()-allStartDate.getTime())/(24000*3600))>31){
			alert("只能查31天以内的数据,请缩小日期范围!");
			return false;
		}
     }   
     return true;   
};

//后退一天
staffperformancereport.backDate = function(startDate) {
	if(startDate.length>0){  
		var arrStartDate = startDate.split("-");   
		var today = new Date(arrStartDate[0], arrStartDate[1],arrStartDate[2]);
		var yesterday_milliseconds = today.getTime() - 1000 * 60 * 60 * 24;
		var yesterday = new Date();
		yesterday.setTime(yesterday_milliseconds);
		var strYear = yesterday.getFullYear();
		var strDay = yesterday.getDate();
		if (strDay < 10) {
			strDay = "0" + strDay;
		}
		var strMonth = yesterday.getMonth();
		if (strMonth < 10) {
			strMonth = "0" + strMonth;
		}
		var strYesterday = strYear + "-" + strMonth + "-" + strDay;
		return strYesterday;
	}
	return null;
};


// 查询脚本
staffperformancereport.searchData = function() {
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    var starttime = $("#startDateCondition").datebox('getValue');
	var endTime = $("#endDateCondition").datebox('getValue');
    if(starttime==""){
		alert("请选择开始日期");
		return;
	}
	if(endTime==""){
		alert("请选择结束日期");
		return;
	}
	if(!staffperformancereport.isStartEndDate(starttime,endTime)){
		return;
	}
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/staffperformancereport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = staffperformancereport.locno;
	var columns=staffperformancereport.getDataGridJGColumns();
	staffperformancereport.loadGridDataUtil('dataGridJG', queryMxURL, reqParam, columns);
};


//清除查询条件
staffperformancereport.searchClear = function(id){
	$('#'+id).form("clear");
};


// 初始化用户信息
staffperformancereport.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		staffperformancereport.locno = data.locno;
		$('#startDateCondition').datebox('setValue',staffperformancereport.getDate(-30));
		$('#endDateCondition').datebox('setValue',data.currentDate10Str);
	}); 
};

$(document).ready(function() {
	
	staffperformancereport.initCurrentUser();
	
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
	);
	
	//初始化员工姓名
	wms_city_common.comboboxLoadFilter(
			["loginNameCondition"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
			
	$('#dataGridJG').datagrid(
		{
			'onLoadSuccess':function(data){
	   			if(data.footer[1].isselectsum){
	   				staffperformancereport.dhshQty = data.footer[1].showDhshQty;
	   				staffperformancereport.shysQty = data.footer[1].shysQty;
	   				staffperformancereport.fhfhQty = data.footer[1].fhfhQty;
	   				staffperformancereport.jhlQty = data.footer[1].jhlQty;
	   				staffperformancereport.fhdbQty = data.footer[1].fhdbQty;
	   				staffperformancereport.thshQty = data.footer[1].thshQty;
	   				staffperformancereport.thysQty = data.footer[1].thysQty;
	   				staffperformancereport.qtrkQty = data.footer[1].qtrkQty;
	   				staffperformancereport.qtckQty = data.footer[1].qtckQty;
	   				staffperformancereport.rksjQty = data.footer[1].rksjQty;
	   				staffperformancereport.tcsjQty = data.footer[1].tcsjQty;
	   				staffperformancereport.jsykQty = data.footer[1].jsykQty;
	   				staffperformancereport.cpQty = data.footer[1].cpQty;
	   				staffperformancereport.fpQty = data.footer[1].fpQty;
	   				staffperformancereport.totalQty = data.footer[1].totalQty;
	   			}else{
	   				var rows = $('#dataGridJG').datagrid('getFooterRows');
		   			rows[1]['showDhshQty'] = staffperformancereport.dhshQty;
		   			rows[1]['shysQty'] = staffperformancereport.shysQty;
		   			rows[1]['fhfhQty'] = staffperformancereport.fhfhQty;
		   			rows[1]['jhlQty'] = staffperformancereport.jhlQty;
		   			rows[1]['fhdbQty'] = staffperformancereport.fhdbQty;
		   			rows[1]['thshQty'] = staffperformancereport.thshQty;
		   			rows[1]['thysQty'] = staffperformancereport.thysQty;
		   			rows[1]['qtrkQty'] = staffperformancereport.qtrkQty;
		   			rows[1]['qtckQty'] = staffperformancereport.qtckQty;
		   			rows[1]['rksjQty'] = staffperformancereport.rksjQty;
		   			rows[1]['tcsjQty'] = staffperformancereport.tcsjQty;
		   			rows[1]['jsykQty'] = staffperformancereport.jsykQty;
		   			rows[1]['cpQty'] = staffperformancereport.cpQty;
		   			rows[1]['fpQty'] = staffperformancereport.fpQty;
		   			rows[1]['totalQty'] = staffperformancereport.totalQty;
		   			$('#dataGridJG').datagrid('reloadFooter');
	   			}
	   		}
		}
	);
	
});

staffperformancereport.doExport = function(){
	var url = BasePath + '/staffperformancereport/doExport';
	var excelTitle = '员工绩效报表';
	var $dg = $("#dataGridJG");
	var dataRow = $dg.datagrid('getRows');
	if (dataRow.length > 0) {
		var columns = $dg.datagrid('options').columns;
		var rsCol = staffperformancereport.formatCol(columns);
		var params = $dg.datagrid('options').queryParams;
		var firstRowNum = rsCol[0].length;
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.exportColumns = JSON.stringify(rsCol);
				param.fileName = excelTitle;
				param.firstRowNum = firstRowNum;

				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}

			},
			success : function() {

			}
		});
	}else{
		alert('查询记录为空，不能导出!', 1);
	}
	
};
staffperformancereport.formatCol = function(columns){
	var rs = [];
	for(var idx=0;idx<columns.length;idx++){
		var array = columns[idx];
		var temp = [];
		for(var i=0;i<array.length;i++){
			var p = {};
			p.field = array[i].field;
			p.title = array[i].title;
			temp[i] = p;
		}
		rs[idx] = temp;
	}
	return rs;
};
