var billuminstockdirect = {};
billuminstockdirect.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["creatorCondition"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};
//仓库
billuminstockdirect.locno;
billuminstockdirect.status ={
	"11":"验收完成",
	"13":"差异验收",
	"30":"已转货",
	"20":"部分预约",
	"25":"预约完成"
};

//收货单的状态
billuminstockdirect.r_statusData = [{
    "label":"11",
    "text":"已审核", 
    "value":"11→已审核",   
},{    
    "label":"20",    
    "text":"部分预约", 
    "value":"20→部分预约"   
},{    
    "label":"25",    
    "text":"预约完成", 
    "value":"25→预约完成"   
}];


billuminstockdirect.statusTask ={
		"10":"新建",
		"13":"已发单"
	};

billuminstockdirect.r_initStatus = function(){
	$('#search_status').combobox({
	     valueField:"label",
	     textField:"value",
	     data:billuminstockdirect.r_statusData,
	     panelHeight:"auto"
	  });
};


//选择退仓通知单号
billuminstockdirect.selectUmNo = function(rowData){
	$("#umNoDialog").window('open'); 
	var queryMxURL=BasePath+'/bill_um_check/selectUmNoForInstock?&locno='+billuminstockdirect.locno;
    $( "#dataPlanNo").datagrid('options').url=queryMxURL;
    $( "#dataPlanNo").datagrid('load');
};

billuminstockdirect.checkUmNo = function(rowData){
 	$("#untreadMmNo").val(rowData.untreadMmNo);
 	$("#umNoDialog").window('close');
};
 
//创建tab
$(function(){ 
 	$('#ttAll').tabs({
         border:true,
         onSelect:function(title){
         		//获取选中的tab
     	    	var pp = $('#ttAll').tabs('getSelected');   
     	    	var index = $('#ttAll').tabs('getTabIndex',pp);//easyui tabs获取选中索引
     	    	
     	    	if(index==0){//按收货
     	    		
     	    	}else if(index==1){
     	    		billuminstockdirect.loadDataGridStockNew('10');
     	    	}else{
     	    		alert('未选中Tab！',1);
     	    	}
         }
        
     });
 }); 

//创建tab
$(function(){ 
 	$('#ttCheck').tabs({
         border:true,
         onSelect:function(title){
         		//获取选中的tab
     	    	var pp = $('#ttCheck').tabs('getSelected');   
     	    	var index = $('#ttCheck').tabs('getTabIndex',pp);//easyui tabs获取选中索引
     	    	if(index==0){//未发单
     	    		//$('#dataGridJG_detail').datagrid('clearData');
     	    		billuminstockdirect.loadDataGridStockNew('10');
     	    	}else if(index==1){//已发单
     	    		billuminstockdirect.loadDataGridStockNew('13');
     	    	}else{
     	    		alert('未选中Tab！',1);
     	    	}
         }
     });
}); 


//查询未发单的信息
billuminstockdirect.searchNoSendOrder = function(status){
	
	var fromObj=$('#searchFormNo');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
	     return ;
	}
	var data={
	     'locno':billuminstockdirect.locno,
		 'itemType':$('#itemTypeCondition').combobox('getValue'),
	     'quality':$('#qualityCondition').combobox('getValue'),
	     'status': status,
	     'sourceNo': $("#noSendSourceNo").val()
	};
	
	var queryMxURL=BasePath+'/bill_um_instock_direct/list_direct.json';
	var tempObj = $('#dataGridJGCheck_direct');
	tempObj.datagrid('options').url=queryMxURL;
	tempObj.datagrid('options').queryParams=data;
	tempObj.datagrid('load');
	
	//billuminstockdirect.loadDataGridStockNew(status);
};


//加载未发单的数据
billuminstockdirect.loadDataGridStockNew = function(status){
	var obj = $("#dataGridJGCheck_direct");
	if(status =='13'){
		obj = $("#dataGridJGCheck_detail");
	}
	var queryMxURL=BasePath+'/bill_um_instock_direct/list_direct.json?status='+status+'&locno='+billuminstockdirect.locno;
	obj.datagrid('options').url=queryMxURL;
	obj.datagrid('load');
};

//单击一行查看验收明细
billuminstockdirect.instockDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJG_detail').datagrid('clearData');
		return;
	}
	var queryMxURL=BasePath+'/bill_um_check_dtl/dtl_list.json?checkNo='+rowData.checkNo+'&locno='+billuminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};

//单击一行查看已定位明细
billuminstockdirect.instockDirectDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJG_direct').datagrid('clearData');
		return;
	}
	var  cellNostr = '';
	var queryMxURL=BasePath+'/bill_um_direct/list.json?untreadMmNo='+rowData.untreadMmNo+'&locno='+billuminstockdirect.locno+'&cellNo='+cellNostr;
	$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_direct").datagrid( 'load' );
};


//委托业主
billuminstockdirect.ownerFormatter = function(value, rowData, rowIndex){
	return billuminstockdirect.ownnerData[value];
};

//查询退仓验收 单信息
billuminstockdirect.searchArea = function(){
	
	var startTm =  $('#createtmStart').datebox('getValue');
	var endTm =  $('#createtmEnd').datebox('getValue');
	if(!isStartEndDate(startTm,endTm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_check/selectCheckNoForInstock?locno='+billuminstockdirect.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
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

billuminstockdirect.searchLocClear = function(){
	$('#brandNo').combobox("loadData",[]);
	$('#searchForm').form("clear");
};

billuminstockdirect.searchLocClearCheck = function(){
	$('#searchFormCheck').form("clear");
};

//生成任务
billuminstockdirect.createTask = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请先选择记录!',1);
		return;
	}
	var isNoTz = 'N';
	var isDiff = 'N';
	$.messager.confirm("确认","你确定要选择这"+checkedRows.length+"条数据，生成任务吗？", function (r){  
        if (r) {
        	var keyStr = [];
        	var untreadMmNo = '';
        	var ownerNo ='';
        	var firstTypeAndQuality;
        	var typeAndQuality; 
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.checkNo);
        		if(index==0){
        			untreadMmNo = item.untreadMmNo;
        			ownerNo = item.ownerNo;
        			firstTypeAndQuality = item.itemType+"-"+item.quality;
        		}
        		//如果有不等的，则给出提示
        		if(untreadMmNo != item.untreadMmNo){
        			isDiff = 'Y';
        			return false;
        		}
        		typeAndQuality = item.itemType+"-"+item.quality;
        		if((item.untreadMmNo == '-' || item.untreadMmNo =='' || item.untreadMmNo == null)
        				&& firstTypeAndQuality != typeAndQuality){
        			isNoTz = 'Y';
        			return false;
        		}
        	});    
        	
        	//如果请选择不相同的退仓通知单
        	if(isDiff == 'Y'){
        		alert('请选择相同的退仓通知单!',1);
    			return;
        	}
        	
        	//如果通知单为空，请选择属性和品质相同的验收单
        	if(isNoTz == 'Y'){
        		alert('请选择属性和品质相同的验收单!',1);
    			return;
        	}
        	
        	//校验退仓验收单是否合法
        	var vdata={
            	    "checkNoStr":keyStr.join(","),
            	    "locno":billuminstockdirect.locno,
            	    "ownerNo":ownerNo
            };
    		var vurl = BasePath+'/bill_um_check/validateIsEnable';
    		billuminstockdirect.ajaxRequest(vurl,vdata,function(result,returnMsg){
                  if(result.flag=='true'){
	                	var url = BasePath+'/bill_um_check/validateIsDiff';
	                  	var data={
	                  	    "checkNoStr":keyStr.join(","),
	                  	    "locno":billuminstockdirect.locno,
	                  	    "untreadMmNo":untreadMmNo
	                  	};
	                  	wms_city_common.loading("show","正在生成任务......");
	                  	billuminstockdirect.ajaxRequest(url,data,function(result){
	                  		wms_city_common.loading();
	                  		if(result=='success'){//有差异
	                  			
	                  			 //弹出差异明细窗口
	                  			 $('#dataUmCheckDiff').datagrid('clearData');
	                 				 //var queryMxURL=BasePath+'/bill_um_direct/list.json?untreadMmNo='+rowData.untreadMmNo+'&locno='+billuminstockdirect.locno+'&cellNo='+cellNostr;
	                 				 var queryMxURL=BasePath+'/bill_um_check/selectDiffDirect';
	                 				 $( "#dataUmCheckDiff").datagrid( 'options' ).queryParams= data;
	                 				 $( "#dataUmCheckDiff").datagrid( 'options' ).url=queryMxURL;
	                 			     $( "#dataUmCheckDiff").datagrid( 'load' );
	                 			     
	                 			     $('#checkNoStr').val(keyStr.join(","));
	                 			     $('#untreadMmNoStr').val(untreadMmNo);
	                 			     $('#ownerNoStr').val(ownerNo);
	                 			     
	                 			     $('#umCheckDiffDialog').window('open');
	                 			     
	          	       		}else if(result=='none'){ //没有差异，直接生成任务；
	          	       		    var taskUrl = BasePath+'/bill_um_instock_direct/createTask';
	          	             	var taskData={
	          	             	    "strCheckNoList":keyStr.join(","),
	          	             	    "locno":billuminstockdirect.locno,
	          	             	    "ownerNo":ownerNo,
	          	             	    "untreadMmNo":untreadMmNo
	          	             	};
	          	             	billuminstockdirect.ajaxRequest(taskUrl,taskData,function(result){
	          	             		if(result.result=='success'){
	          	           			 	alert('生成任务成功!');
	          	    	       		 }else{
	          	    	       			 alert(result.msg,1);
	          	    	       		 }
	          	            	}); 
	          	       		}else{
	          	       			alert("生成任务：匹配定位数据时异常！");
	          	       		}
	                  	}); 
                	  
                  }else if(result.flag=='warn'){
            			 alert(result.resultMsg,1);
                  }else{
            			 alert('检验退仓验收单合法性异常,请联系管理员!',2);
                  }
            }); 
            
        }  
    }); 
};


//继续定位
billuminstockdirect.continueDirect = function(){
	
	        if(null== $('#checkNoStr').val() || ''==$('#checkNoStr').val()
	            || null== $('#untreadMmNoStr').val() || ''==$('#untreadMmNoStr').val() ){
	        	alert("退仓通知单和退仓验收单号未获取到！");
	        	return;
	        }
	        //继续定位
            var url = BasePath+'/bill_um_direct/continueDirect';
        	var data={
        	    "locno":billuminstockdirect.locno,
        	    "ownerNo": $('#ownerNoStr').val(),
        	    "untreadMmNo":$('#untreadMmNoStr').val(),
        	    "strCheckNoList": $('#checkNoStr').val()
        	};
        	wms_city_common.loading("show","正在定位......");
        	billuminstockdirect.ajaxRequest(url,data,function(result){
        		if(result.result=='success'){//继续定位成功
        			alert('继续定位并成功生成任务!');
        			//关闭差异窗口
        			$('#umCheckDiffDialog').window('close');
        			
        			billuminstockdirect.searchArea();
        			
        			//调整到发单界面
        			$('#ttAll').tabs("select", 1);
        			//再生成任务
//        			var taskUrl = BasePath+'/bill_um_instock_direct/createTask';
//	             	var taskData={
//	             	    "strCheckNoList": $('#checkNoStr').val(),
//	             	    "locno":billuminstockdirect.locno,
//	             	    "ownerNo":$('#ownerNoStr').val(),
//	             	    "untreadMmNo":$('#untreadMmNoStr').val()
//	             	};
//	             	billuminstockdirect.ajaxRequest(taskUrl,taskData,function(result){
//	             		if(result.result=='success'){
//	             			alert('继续定位并成功生成任务!');
//	    	       		 }else{
//	    	       			 alert(result.msg,1);
//	    	       		 }
//	            	}); 
	       		}else{
	       			alert(result.msg,1);
	       		}
        		wms_city_common.loading();
        	}); 
   
};

//发单
billuminstockdirect.createInstock = function(){
	var checkedRows = $("#dataGridJGCheck_direct").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要发单的上架任务信息!',1);
		return;
	}
	if($("#instockWorker").val()==null ||$("#instockWorker").val()==''){
		alert('请选择用户!',1);
		return;
	}
	$.messager.confirm("确认","你确定要发单吗？", function (r){
        if (r) {
        	var rowStrs = [];
        	var ownerNo;
        	var firstItemtypeAndQuality;
        	var itemtypeAndQuality;
        	var isR = 'N';
        	$.each(checkedRows, function(index, item){
        		rowStrs.push(item.rowId);
        		if(index==0){
        			ownerNo = item.ownerNo;
        			firstItemtypeAndQuality = item.itemType+"-"+item.quality;
        		}
        		itemtypeAndQuality = item.itemType+"-"+item.quality;
        		if(firstItemtypeAndQuality != itemtypeAndQuality){
        			isR = 'Y';
        			return false;
        		}
        	});    
        	
        	//如果不是相同的属性和品质，则不能发单
        	if(isR == 'Y'){
        		alert('请选择属性和品质都相同的明细一起发单!',1);
    			return;
        	}
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_um_instock_direct/sendOrder';
        	var data={
        	    "rowIdList":rowStrs.join(","),
        	    "locno":billuminstockdirect.locno,
        	    "ownerNo":ownerNo,
        	    "sender":$("#instockWorker").val()
        	};
        	wms_city_common.loading("show","正在发单......");
        	billuminstockdirect.ajaxRequest(url,data,function(result){
         		if(result.result=='success'){
       			 	alert('发单成功!');
       			 	//跳转到已发单界面
       			 	$("#ttCheck").tabs("select", 1);
//       				var  cellNostr = '';
//       				var queryMxURL=BasePath+'/bill_um_direct/list.json?untreadMmNo='+untreadMmNo+'&locno='+billumdirect.locno+'&cellNo='+cellNostr;
//       				$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
//       			    $( "#dataGridJG_direct").datagrid( 'load' );
	       		 }else{
	       			 alert(result.msg,1);
	       		 }
         		wms_city_common.loading();
        	}); 
        }  
    }); 
};

//一键发单
billuminstockdirect.aKeySendOrder = function(){
	
	var itemType = $('#itemTypeCondition').combobox('getValue');
	var quality = $('#qualityCondition').combobox('getValue');
	if(itemType == null || itemType == ""){
		alert("请选择商品属性!");
		return;
	}
	if(quality == null || quality == ""){
		alert("请选择商品品质!");
		return;
	}
	
	if($("#instockWorker").val()==null ||$("#instockWorker").val()==''){
		alert('请选择用户!',1);
		return;
	}
	
	$.messager.confirm("确认","你确定要一键发单吗？", function (r){
		if (r) {
			var url = BasePath+'/bill_um_instock_direct/akeySendOrder';
        	var data={
        	    "locno":billuminstockdirect.locno,
        	    "itemType":itemType,
        	    "quality":quality,
        	    "sendUser":$("#instockWorker").val()
        	};
        	wms_city_common.loading("show","正在发单......");
        	billuminstockdirect.ajaxRequest(url,data,function(result){
         		if(result.result=='success'){
       			 	alert('发单成功!');
       			 	//跳转到已发单界面
       			 	$("#ttCheck").tabs("select", 1);
	       		 }else{
	       			 alert(result.msg,1);
	       		 }
         		wms_city_common.loading();
        	}); 
		}
	});
	
};

//查看发单人
billuminstockdirect.initInstockWorker = function(rowData){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#instockWorker').combobox({
			     valueField:"workerNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:150,
			    // multiple:true,
			     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.workerNo+'→'+tempData.workerName;
			       		 }
	       	 		}
	    			return data;
	   			 } 
			  }).combobox("select",data[0].workerNo);
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
} ;




//退仓类型编码
billuminstockdirect.typeData = {};
billuminstockdirect.initUntreadTypeAndPoType = function(){
	wms_city_common.comboboxLoadFilter(
			["itemTypeCondition","itemType_search"],
			null,
			null,
			null,
			true,
			[true, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			billuminstockdirect.typeData,
			null);
};

billuminstockdirect.qualityDataObj ={};
billuminstockdirect.initQuality = function(){
	wms_city_common.comboboxLoadFilter(
			["qualityCondition","quality_search"],
			null,
			null,
			null,
			true,
			[true, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billuminstockdirect.qualityDataObj,
			null);
};

//加载委托业主信息
billuminstockdirect.loadOwner = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(data) {
			$('#search_workerNo,#search_check_workerNo').combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'valueAndText',
			    panelHeight:"auto",
			    loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.ownerNo+'→'+tempData.ownerName;
			       		 }
	       	 		}
	    			return data;
	   			 } 
			});  
			billuminstockdirect.ownnerData = billuminstockdirect.converStr2JsonObj(data);
		}
	});
};

//委托业主
billuminstockdirect.ownnerData={};


$(document).ready(function(){
	$("#createtmStart").datebox('setValue',getDateStr(-2));
	
	//初始化业主
	billuminstockdirect.loadOwner();
	//初始化仓库
	billuminstockdirect.loadLoc();
	//加载列表数据
	//billuminstockdirect.loadDataGrid();
	
	//收货单的状态
	billuminstockdirect.r_initStatus();
	
	//billuminstockdirect.initInstockWorker();
	
	billuminstockdirect.initUntreadTypeAndPoType();
	
	billuminstockdirect.initQuality();
	
	billuminstockdirect.initUsers();
	
	//billuminstockdirect.initInstockWorker();
	
	$("#info_add").click(billuminstockdirect.save_do);
	$("#info_edit").click(billuminstockdirect.edit_do);

	$("#info_add").show();
	$("#info_edit").hide();
	
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	$('#dataGridJG_detail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data != null) {
						if(data.footer != null){
							if(data.footer[1].isselectsum){
								billuminstockdirect.itemQty = data.footer[1].itemQty;
								billuminstockdirect.checkQty = data.footer[1].checkQty;
								billuminstockdirect.difQty = data.footer[1].difQty;
				   			}else{
				   				var rows = $('#dataGridJG_detail').datagrid('getFooterRows');
					   			rows[1]['itemQty'] = billuminstockdirect.itemQty;
					   			rows[1]['checkQty'] = billuminstockdirect.checkQty;
					   			rows[1]['difQty'] = billuminstockdirect.difQty;
					   			$('#dataGridJG_detail').datagrid('reloadFooter');
				   			}
						}
					}
		   			
		   		}
			}
		);
	
	$('#dataGridJGCheck_direct').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
							billuminstockdirect.estQty = data.footer[1].estQty;
			   			}else{
			   				var rows = $('#dataGridJGCheck_direct').datagrid('getFooterRows');
				   			rows[1]['estQty'] = billuminstockdirect.estQty;
				   			$('#dataGridJGCheck_direct').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	
	$('#dataGridJGCheck_detail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
							billuminstockdirect.estQty = data.footer[1].estQty;
			   			}else{
			   				var rows = $('#dataGridJGCheck_detail').datagrid('getFooterRows');
				   			rows[1]['estQty'] = billuminstockdirect.estQty;
				   			$('#dataGridJGCheck_detail').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);

});

billuminstockdirect.inititemType = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumcheck.lookupcode_untread_type,
		success : function(data) {
			billumcheck.untreadTypeData = billumcheck.converStr2JsonObj(data);
			billumcheck.typeData = billumcheck.untreadTypeData;
			$('#itemType,#itemType_view,#itemType_search').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:190
			});
		}
	});
};

//加载仓库信息
billuminstockdirect.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billuminstockdirect.locno = data.locno;
		}
	});
};

billuminstockdirect.statusFormatter = function(value, rowData, rowIndex){
	return billuminstockdirect.status[value];
};

billuminstockdirect.statusTaskFormatter = function(value, rowData, rowIndex){
	return billuminstockdirect.statusTask[value];
};


billuminstockdirect.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billuminstockdirect.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billuminstockdirect.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//退仓类型
billuminstockdirect.typeFormatter  = function(value, rowData, rowIndex){
	return billuminstockdirect.typeData[value];
};
//品质
billuminstockdirect.qualityFormatter = function(value, rowData, rowIndex){
	return billuminstockdirect.qualityDataObj[value];
};

billuminstockdirect.initInstockWorker = function(rowData){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#instockWorker').combobox({
			     valueField:"workerNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:150,
			    // multiple:true,
			     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.workerNo+'→'+tempData.workerName;
			       		 }
	       	 		}
	    			return data;
	   			 } 
			  }).combobox("select",data[0].workerNo);
		}
	});
};

//选择用户
billuminstockdirect.selectPickingPeople = function(){
	$('#showSelectPickingPeopleWin').show().window('open');  
	$('#pickingPeopleDataGrid').datagrid('clearData');
	billuminstockdirect.loadRoleList();
};
//加载岗位信息
billuminstockdirect.loadRoleList = function(){	
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/authority_organization/role.json',
		success : function(data) {
			$('#roleid').combobox({
			    data:data,
			    valueField:'roleId',    
			    textField:'roleName',
			    panelHeight:150,
			    onSelect:function(record){
			    	 var uList=[];
			    	 $.each(record.userList,function(i,o){
			    		 o.roleName=record.roleName;
			    		 uList.push(o);
			    	 });
			    	 $('#pickingPeopleDataGrid').datagrid({data:uList});
			     }
			});
		},error:function(){
			alert('加载岗位信息异常,请联系管理员!',2);
		}
	});
};
//确定用户
billuminstockdirect.confirmPickingPeople = function(){
	var checkedRows = $("#pickingPeopleDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请至少选择一个人员信息！',1);
		return;
	}
	$.messager.confirm("确认","您确定要选择这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var noStrs = [];
        	$.each(checkedRows, function(index,item){
        		noStrs.push(item.loginName);
        	}); 
        	$("#instockWorker").val();
        	$('#instockWorker').val(noStrs.join(","));
        	$('#showSelectPickingPeopleWin').window('close').hide();  
        }
	});   
};
//根据角色ID查询对应下的所有用户信息 
billuminstockdirect.loadDataUserInfoByRoleId = function(roleId){
	$('#pickingPeopleDataGrid').datagrid('clearData');
	var queryMxURL=BasePath+'/bill_im_instock_direct/getUserListByRoleId?roleId='+roleId;
    //3.加载明细
    $( "#pickingPeopleDataGrid").datagrid( 'options' ).url=queryMxURL;
    $( "#pickingPeopleDataGrid").datagrid( 'load' );
	
};
billuminstockdirect.closeShowWin = function(id){
	$('#'+id).window('close');
};
