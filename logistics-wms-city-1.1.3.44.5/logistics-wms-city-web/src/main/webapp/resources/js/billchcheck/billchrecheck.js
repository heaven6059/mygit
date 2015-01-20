var billchrecheck = {};
billchrecheck.locno;
//状态
billchrecheck.statusData;
billchrecheck.planTypeData;
billchrecheck.searchDirectParams ={};
//页面加载
$(document).ready(function(){
	billchrecheck.initLocData();
	billchrecheck.initStatusData();
	billchrecheck.initDifferentFlag();
	billchrecheck.initRecheckWorker();
	//创建日期初始为前两天
	$("#createtmStart").datebox('setValue',getDateStr(-2));
	var objs2 = [];
	objs2.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#checkForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs2);
	wms_city_common.initWareNoForCascade(
				billchrecheck.locno,
				['wareNo'],
				['areaNo'],
				['stockNo'],
				null,
				[true],
				null
			)
	$('#dataGridJGDetail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer!=null){
						if(data.footer[1].isselectsum){
			   				billchrecheck.itemQty = data.footer[1].itemQty;
			   				billchrecheck.checkQty = data.footer[1].checkQty;
			   				billchrecheck.recheckQty = data.footer[1].recheckQty;
			   				billchrecheck.diffQty = data.footer[1].diffQty;
			   				billchrecheck.recheckDiffQty= data.footer[1].recheckDiffQty;
			   			}else{
			   				var rows = $('#dataGridJGDetail').datagrid('getFooterRows');
				   			rows[1]['itemQty'] = billchrecheck.itemQty;
				   			rows[1]['checkQty'] = billchrecheck.checkQty;
				   			rows[1]['recheckQty'] = billchrecheck.recheckQty;
				   			rows[1]['diffQty'] = billchrecheck.diffQty;
				   			rows[1]['recheckDiffQty'] = billchrecheck.recheckDiffQty;
				   			$('#dataGridJGDetail').datagrid('reloadFooter');
			   			}
					}		   			
		   		}
			}
		);
});

//状态信息
billchrecheck.initStatusData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_STATUS',
		success : function(data) {
			billchrecheck.statusData=billchrecheck.converStr2JsonObj2(data);
		}
	});
};

//仓库
billchrecheck.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billchrecheck.locno = data.locno;
		}
	});
};

//差异标识
billchrecheck.initDifferentFlag = function(){
	
	wms_city_common.comboboxLoadFilter(
			["differentFlag"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_DIF_FLAG',
			{},
			null,
			null);
	
//	$.ajax({
//		async : false,
//		cache : false,
//		type : 'GET',
//		dataType : "json",
//		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_DIF_FLAG',
//		success : function(data) {
//			
//			wms_city_common.comboboxLoadFilter(
//					["differentFlag","ownerNo","ownerNoView","ownerNoCheckSearch"],
//					"ownerNo",
//					"ownerName",
//					"ownerName",
//					false,
//					[true, false, false, false],
//					BasePath+'/entrust_owner/get_biz',
//					{},
//					billconconvertgoods.ownnerData,
//					null);
//			
//			$("#differentFlag").combobox({
//			     valueField:"itemvalue",
//			     textField:"valueAndText",
//			     data:data,
//			     panelHeight:"auto",
//			     loadFilter:function(data){
//	    			if(data){
//			       		 for(var i = 0;i<data.length;i++){
//			       			 var tempData = data[i];
//			       			 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
//			       		 }
//	       	 		}
//	    			return data;
//	   			 } 
//			}).combobox("select",data[1].itemvalue);
//		}
//	});
};

//盘点人员
billchrecheck.initRecheckWorker = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#recheckWorker').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			  }).combobox("select",data[0].workerNo);
		}
	});
};

billchrecheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//格式化
billchrecheck.statusFormatter = function(value,rowData,rowIndex){
	return billchrecheck.statusData[value];
};

//格式化
billchrecheck.planTypeFormatter = function(value,rowData,rowIndex){
	return billchrecheck.planTypeData[value];
};

function isStartEndDate(startDate,endDate){   
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){   
             return false;   
        }   
     }   
     return true;   
} 

billchrecheck.searchClear = function(){
	$('#checkForm').form("clear");
	//$("#differentFlag").combobox("select","1");
};

billchrecheck.searchArea = function(){
	var createtm_start =  $('#createtmStart').datebox('getValue');
	var createtm_end =  $('#createtmEnd').datebox('getValue');
	if(!isStartEndDate(createtm_start,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	var fromObjStr=convertArray($('#checkForm').serializeArray());
	var queryMxURL=BasePath+'/bill_ch_check_dtl/selectReChCheck?status=25&locno='+billchrecheck.locno;
	var params = eval("(" +fromObjStr+ ")");
	params.orderByField = 'checkDate';
	params.orderBy = 'desc';
    $( "#dataGridJG").datagrid( 'options' ).queryParams= params;
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    $('#dataGridJGDetail').datagrid('loadData',{total:0,rows:[]});
    $('#dataGridJGDetail').datagrid('reloadFooter',[]);
};

//复盘发单
billchrecheck.distributionAssignNoBatch=  function(){
	var checkedRows = $("#dataGridJGDetail").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要分配的盘点单的记录!',1);
		return;
	}
	//var reCheckWorkerText = $("#recheckWorker").combobox("getText");
   // var reCheckWorkerName = reCheckWorkerText.substr(reCheckWorkerText.indexOf("→")+1,reCheckWorkerText.length);
	$.messager.confirm("确认","你确定要分配这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在发单......");
        	$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				data:{
					checkedRows:encodeURIComponent(JSON.stringify(checkedRows)),
					reCheckWorker:$("#recheckWorker").combobox("getValue")
					//reCheckWorkerName:reCheckWorkerName
				},
				dataType : "json",
				url:BasePath+'/bill_ch_recheck_dtl/saveReCheckDtl',
				success : function(data) {
					if(data.result=="success"){
						alert("发单成功");
						$("#dataGridJGDetail").datagrid('load');
					}else{
						alert(data.msg,2);
					}
					wms_city_common.loading();
				}
			});
        }  
    });
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
};

//加载数据
billchrecheck.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_ch_check_direct/list.json?locno='+billchrecheck.locno,
    			'pageNumber':1
    		});
};

billchrecheck.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//差异数量
billchrecheck.difQty = function(value,rowData,rowIndex){
	if(rowData.checkType=='1'){//出盘差异
		return  rowData.checkQty-rowData.itemQty;
	}else if(rowData.checkType=='2'){
		return  rowData.recheckQty-rowData.checkQty;
	}
};

billchrecheck.loadDetail = function(rowData){
	var differentFlag = $('#differentFlag').combobox('getValue');
	var url = BasePath+'/bill_ch_check_dtl/re_dtl_list.json?status=13&locno='+billchrecheck.locno+'&checkNo='+rowData.checkNo+"&differentFlag="+differentFlag;
	
    $('#dataGridJGDetail').datagrid(
		{
			'url':url,
			'pageNumber':1
		});
};