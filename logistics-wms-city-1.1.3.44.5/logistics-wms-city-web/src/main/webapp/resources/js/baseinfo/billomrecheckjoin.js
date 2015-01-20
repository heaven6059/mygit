var billomrecheckjoin = {};
billomrecheckjoin.locno;
billomrecheckjoin.startIndex = 0;
billomrecheckjoin.endIndex = 0;
//状态
billomrecheckjoin.status = {
	"14":"集货完成",
	"13":"复核完成",
	"汇总":"汇总"
};
//客户状态
billomrecheckjoin.storeStatus = {
		"0":"正常",
		"1":"关闭",
		"2":"盘点"
	};
billomrecheckjoin.divideType=[
                              {'value':'','text':'全选'},
                              {'value':'DO','text':'DO→分货任务单'},
                              {'value':'LC','text':'LC→拣货波次单'},
                              {'value':'UC','text':'UC→退仓验收单'},
                              {'value':'OI','text':'OI→其他入库单'}
                              ];
$(document).ready(function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billomrecheckjoin.locno = data.locno;
		}
	});
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#noCheckSearchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objs2 = [];
	objs2.push({
		"sysNoObj" : $('#sysNoSearch2'),
		"brandNoObj" : $('input[id=brandNoIt]', $('#checkedForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs2);
	
	
	//总计
	$('#dataGridJG_NoChecked').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer[1].isselectsum) {
				billomrecheckjoin.realqtycount = data.footer[1].REALQTYCOUNT;
			} else {
				var rows = $('#dataGridJG_NoChecked').datagrid('getFooterRows');
				rows[1]['REALQTYCOUNT'] = billomrecheckjoin.realqtycount;
				$('#dataGridJG_NoChecked').datagrid('reloadFooter');
			}
		}
	});
	
	//总计
	$('#dataGridJG_checked').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer[1].isselectsum) {
				billomrecheckjoin.realQty = data.footer[1].realQty;
			} else {
				var rows = $('#dataGridJG_checked').datagrid('getFooterRows');
				rows[1]['realQty'] = billomrecheckjoin.realQty;
				$('#dataGridJG_checked').datagrid('reloadFooter');
			}
		}
	});
	
	wms_city_common.comboboxLoadFilter(
			["deliverStatus"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_DELIVER_STATUS_TWO',
			{},
			billomrecheckjoin.init_status_Text,
			null);
	//初始化审核时间
	 $('#recheckTimeStart').spinner({
			onSpinUp : function() {
                if(billomrecheckjoin.startIndex == 0){
                	$('#recheckTimeStart').timespinner('setValue','08:30:00');
                }
                billomrecheckjoin.startIndex++;
			},
			onSpinDown : function() {
				 if(billomrecheckjoin.startIndex == 0){
			         $('#recheckTimeStart').timespinner('setValue','08:30:00');
	             }
				 billomrecheckjoin.startIndex++;
			}
		});
	 $('#recheckTimeEnd').spinner({
			onSpinUp : function() {
				if(billomrecheckjoin.endIndex == 0){
                	$('#recheckTimeEnd').timespinner('setValue','17:59:59');
                }
				billomrecheckjoin.endIndex++;
			},
			onSpinDown : function() {
				if(billomrecheckjoin.endIndex == 0){
                	$('#recheckTimeEnd').timespinner('setValue','17:59:59');
                }
				billomrecheckjoin.endIndex++;
			}
		});
	 //初始化来源单类型
	 $('#divideType').combobox({
			data:billomrecheckjoin.divideType,
			multiple:true,
			valueField:'value',
			textField:'text',
			panelHeight:150,
			onSelect:function(record){
		    	var obj = $('#'+this.id);
		    	var values = obj.combobox('getValues');
		    	var value=record['value'];
		    	var newValues=[];
		    	
		    	if(value==''){
		    		newValues.push(value);	
		    		obj.combobox('setValues',newValues);			    		
		    	}else{
		    		for(var i=0; i< values.length;i++){
			    		if(values[i]!=''){				    			
			    			newValues.push(values[i]);
			    		}
			    	}		
		    		obj.combobox('setValues',newValues);
		    	}
		    },onUnselect:function(){
		    	var obj = $('#'+this.id);
		    	var values = obj.combobox('getValues');	
		    	if(values.length==0 && !isShowCheckAllArray[idx]){
		    		obj.combobox('setValues',['']);	
		    	}
		    }
		});
});
billomrecheckjoin.init_status_Text = {};

billomrecheckjoin.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billomrecheckjoin.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//状态
billomrecheckjoin.statusFormatter = function(value, rowData, rowIndex){
	return billomrecheckjoin.status[value];
};

var searchType = "";
//弹出查询箱号窗口
billomrecheckjoin.showSearchDialog  = function(type){
	searchType = type;
	$("#searchDialog").form("clear");
	$("#dataGridJG_Search").datagrid("uncheckAll");
	$("#dataGridJG_Search").datagrid("clearData");
	$("#showDialog").window('open'); 
};
//弹出查询来源单窗口
billomrecheckjoin.showSourceDialog  = function(){
	$("#recheck_status").val("13");
	$("#dataGridJG_Source").datagrid("uncheckAll");
	$("#dataGridJG_Source").datagrid("clearData");
	$("#sourceDialog").window('open'); 
};
//查询箱号
billomrecheckjoin.searchRecheckNo = function(){
	var recheckDateStart =  $('#recheckDateStart').datebox('getValue');
	var recheckDateEnd =  $('#recheckDateEnd').datebox('getValue');
	if(!isStartEndDate(recheckDateStart,recheckDateEnd)){    
		alert("复核日期开始日期不能大于结束日期");   
        return;   
    }
	if(searchType=="check"){
		$("#searchStatus").val("13");
	}else{
		$("#searchStatus").val("14");
	}
	
	var fromObjStr=convertArray($('#searchDialog').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck_join/findRecheckNo?locno='+billomrecheckjoin.locno;
	
    $( "#dataGridJG_Search").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG_Search").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_Search").datagrid( 'load' );
    
};
//查询来源单
billomrecheckjoin.search4Source = function(){
	var recheckDateStart =  $('#audittmStart').datebox('getValue');
	var recheckDateEnd =  $('#audittmEnd').datebox('getValue');
	if(!isStartEndDate(recheckDateStart,recheckDateEnd)){    
		alert("开始日期不能大于结束日期");   
		return;   
	}
	
	var fromObjStr=convertArray($('#sourceForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck/list4Source.json?locno='+billomrecheckjoin.locno;
	
	$( "#dataGridJG_Source").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$( "#dataGridJG_Source").datagrid( 'options' ).url=queryMxURL;
	$( "#dataGridJG_Source").datagrid( 'load' );
	
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

function isStartEndDateTime(startTime,endTime){   
    if(startTime.length>0 && endTime.length>0){   
        var arrStartTime = startTime.split(":");   
        var arrEndTime = endTime.split(":");   
        var allStartTime = new Date();   
        var allEndTime = new Date();   
        allStartTime.setHours(arrStartTime[0], arrStartTime[1], arrStartTime[2], 0);
        allEndTime.setHours(arrEndTime[0], arrEndTime[1], arrEndTime[2], 0);
        if(allStartTime.getTime()>allEndTime.getTime()){   
             return false;   
        }   
     }   
     return true;   
} 

billomrecheckjoin.searchNoChecked = function(){
	var recheckTimeStart = $('#recheckTimeStart').timespinner('getValue');
    var recheckTimeEnd = $('#recheckTimeEnd').timespinner('getValue');
    if(!isStartEndDateTime(recheckTimeStart,recheckTimeEnd)){
    	alert("审核开始时间不能大于结束时间");   
        return;  
    }
	var fromObjStr=convertArray($('#noCheckSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck_join/findNoReCheck?locno='+billomrecheckjoin.locno;
	$( "#dataGridJG_NoChecked").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$( "#dataGridJG_NoChecked").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_NoChecked").datagrid( 'load');
   
};

billomrecheckjoin.searchClearNochecked = function(){
	$("#noCheckSearchForm").form("clear");
	$('#brandNo').combobox("loadData",[]);
	billomrecheckjoin.startIndex = 0;
	billomrecheckjoin.endIndex = 0;
};

billomrecheckjoin.searchChecked = function(){
	var searchStartEdittm =  $('#searchStartEdittm').datebox('getValue');
	var searchEndEdittm =  $('#searchEndEdittm').datebox('getValue');
	if(!isStartEndDate(searchStartEdittm,searchEndEdittm)){    
		alert("开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#checkedForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck_join/findReChecked?locno='+billomrecheckjoin.locno;
	$( "#dataGridJG_checked").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$( "#dataGridJG_checked").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_checked").datagrid( 'load' );
};

billomrecheckjoin.searchClearchecked = function(){
	$("#checkedForm").form("clear");
	$('#brandNoIt').combobox("loadData",[]);
};

//确认
billomrecheckjoin.searchAdd = function(){
	var rowData = $("#dataGridJG_Search").datagrid("getChecked");
	if(rowData.length>0){
		if(searchType=="check"){
			$("#searchRecheckNo").val(rowData[0].RECHECK_NO);
		}else{
			$("#searchRecheckedNo").val(rowData[0].RECHECK_NO);
		}
		$("#showDialog").window('close'); 
	}else{
		alert("请选择复核单号");
	}
};
//确认
billomrecheckjoin.searchAdd4Source = function(){
	var rowData = $("#dataGridJG_Source").datagrid("getChecked");
	if(rowData.length>0){
		var divideNoStr = '';
		for(var idx=0;idx<rowData.length;idx++){
			divideNoStr += rowData[idx].divideNo;
			if(idx<rowData.length-1){
				divideNoStr += ',';
			}
		}
		$('#searchDivideNo').val(divideNoStr);
		$("#sourceDialog").window('close'); 
	}else{
		alert("请选择复核单号");
	}
};

billomrecheckjoin.closeWindow = function(value){
	$("#"+value).window('close'); 
};

//清空
billomrecheckjoin.searchClear = function(){
	$("#searchDialog").form("clear");
};
//清空
billomrecheckjoin.formClear = function(id){
	$("#"+id).form("clear");
};
billomrecheckjoin.searchLocClear = function(){
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
};

//交接确认
billomrecheckjoin.sendCheck = function(){
	var checkedRows = $("#dataGridJG_NoChecked").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要交接的的记录!',1);
		return;
	}
	
	var allOk =true;
	$.each(checkedRows,function(index,item){
		var status = item.STORESTATUS;
		if(status != 0){
			allOk = false;
			return false;
		}
	});
	
	if(!allOk){
		alert("所选数据中存在非正常状态的门店，请重新选择！",1);
		return;
	}
	
	$.messager.confirm("确认","你确定要交接这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	wms_city_common.loading('show');
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		//keyStr.push(item.LABELNO+"-"+item.RECHECK_NO+"-"+item.STORE_NO);
        		keyStr[keyStr.length] = {boxNo:item.LABELNO,recheckNo:item.RECHECK_NO,storeNo:item.STORE_NO};
        	});
            //2.绑定数据
            var url = BasePath+'/bill_om_recheck_join/sendReCheck';
        	var data={
        		datas:JSON.stringify(keyStr)
        	    //"rowIdstr":keyStr.join(",")
        	};
        	$.ajax({
    			async : true,
				cache : false,
				type : 'POST',
				dataType : "json",
				data: data,
				url:url,
				success : function(data) {
					wms_city_common.loading();
					if(data.flag=="warn") {
						alert(data.msg,2);
					} else {
						alert(data.msg);
					}
					 billomrecheckjoin.searchNoChecked();
				}
			});
        }  
    }); 
};

billomrecheckjoin.statusSearchFormatter = function(value,rowData,rowIndex){
	return billomrecheckjoin.status[value];
};

billomrecheckjoin.storeStatusFormatter = function(value,rowData,rowIndex){
	return billomrecheckjoin.storeStatus[value];
};

//billomrecheckjoin.statusFormatter = function(value,rowData,rowIndex){
//	return billomrecheckjoin.status["13"];
//};

billomrecheckjoin.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
