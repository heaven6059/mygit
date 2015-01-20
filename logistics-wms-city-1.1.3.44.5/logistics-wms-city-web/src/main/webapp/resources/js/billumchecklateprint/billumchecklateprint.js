
var billumchecklateprint = {};


//加载Grid数据Utils
billumchecklateprint.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

String.prototype.replaceAll  = function(s1,s2){     
    return this.replace(new RegExp(s1,"gm"),s2);     
};

//清空
billumchecklateprint.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNo').combobox("loadData",[]);
};

//清空
billumchecklateprint.printClear = function(id){
	$('#'+id).val("");
};

//查询主表数据
billumchecklateprint.searchData = function(){
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/billumchecklateprint/dtl_list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billumchecklateprint.locno;
	billumchecklateprint.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

//选中到打印文本
billumchecklateprint.toPrintText = function(){
	var selectRows = $('#dataGridJG').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请选择单据!");
		return;
	}
	var result = false;
	var printBarcode = $('#printBarcode').val();
	if($.trim(printBarcode)==''){
		result = true;
	}
	for (var i = selectRows.length - 1; i >= 0; i--) {
		if(selectRows[i].barcode!=''&&selectRows[i].barcode!=null){
			if(result){
				printBarcode+=selectRows[i].barcode;
				result = false;
			}else{
				printBarcode+="\n"+selectRows[i].barcode;
			}
		}
    }
	$('#printBarcode').val(printBarcode);
};

//打印标签
billumchecklateprint.printBarcode = function(){
	
	var labelNos = new Array();
//	var sysNo = $('#sysNoSearch').combobox('getValue');
//	if(sysNo==''||sysNo==null){
//		alert("请选择品牌库");
//		return;
//	}
	
	var qty = $('#qty').numberbox('getValue');
	var printBarcode = $('#printBarcode').val();
	if(qty<=0){
		alert("数量必须大于0");
		return ;
	}
	if($.trim(printBarcode) == null || $.trim(printBarcode) == ''){
		alert("请添加打印条码");
		return ;
	}
	wms_city_common.loading("show","正在生成条码......");
	var url = BasePath+'/billumchecklateprint/getBarcodeList.json';
	var barcodes = printBarcode.replaceAll('\n','@');
	var params = {barcodes:barcodes};
	ajaxRequestAsync(url,params,function(data){
		wms_city_common.loading();
		labelNos = data.list;
		if(labelNos.length==0){
			alert("没有获取到可打印的条码!");
		}
	});
	
	if(labelNos.length>0){
		var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	  	if(LODOP==null){
	  		return;
	  	}
		for(var i=0,length=labelNos.length;i<length;i++){
			for(var j = 0;j < qty;j++){
				LODOP.NewPage();
		        var html = 
		        		"<table border='0' cellpadding='0' cellspacing='0' style='width:100%;'>" +
		        		"<tr bgcolor='#fff'>" +
		        		"<td style='height:28pt;width:100%;text-align:center;'>"+labelNos[i].barcode+"</td>" +
		        		"</tr>" +
		        		"<tr bgcolor='#fff' >" +
		        		"<td style='height:32pt;width:100%;text-align:center;'>"+labelNos[i].itemName+"&nbsp;&nbsp;"+labelNos[i].sizeCode+"</td>" +
		        		"</tr>" +
		        		"<tr bgcolor='#fff'>" +
		        		"<td style='height:50pt;width:100%;'></td>" +
		        		"</tr>" +
		        		"</table>";
		        LODOP.SET_PRINT_PAGESIZE(1,'1000','550',"");
				LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
				LODOP.ADD_PRINT_BARCODE(115,80,220,70,"128A",labelNos[i].barcode);
			}
		}
		//LODOP.PRINT();
		LODOP.PREVIEW();
	}
	
};

//初始化状态
billumchecklateprint.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_UM_CHECK_LATE_PRINT_STATUS',
			{},
			null,
			null);
	$('#statusCondition').combobox('select','01');
};

//初始化客户信息
billumchecklateprint.initStoreNo = function(inputId){
	$('#storeNoCondition').combogrid({
		 panelWidth:400,   
         idField:'storeNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json?storeType=11&status=0',   
         columns:[[  
			{field:'storeNo',title:'客户编码',width:110, align:'left'},  
			{field:'storeName',title:'客户名称',width:243, align:'left'}  
         ]],
         loadFilter:function(data){
        	 if(data && data.rows){
        		 for(var i = 0;i<data.rows.length;i++){
        			 var tempData = data.rows[i];
        			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
        		 }
        	 }
     		 return data;
         }
	});
};

//初始化当前登录的用户 信息
billumchecklateprint.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billumchecklateprint.loginName = data.loginName;
		billumchecklateprint.locno = data.locno;
	});
};

//JS初始化
$(document).ready(function(){
	billumchecklateprint.initCurrentUser();
	billumchecklateprint.initStatus();
	billumchecklateprint.initStoreNo();
	
	//加载品牌
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')});//查询
	wms_city_common.loadSysNo4Cascade(objs);
	
	$('#dataGridJG').datagrid({
		'onLoadSuccess':function(data){
			if(data != null) {
				if(data.footer != null){
					if(data.footer[1].isselectsum){
						billumchecklateprint.itemQty = data.footer[1].itemQty;
						billumchecklateprint.checkQty = data.footer[1].checkQty;
				   	}else{
				   		var rows = $('#dataGridJG').datagrid('getFooterRows');
					   	rows[1]['itemQty'] = billumchecklateprint.itemQty;
					   	rows[1]['checkQty'] = billumchecklateprint.checkQty;
					   	$('#dataGridJG').datagrid('reloadFooter');
				   	}
				}
			}
		 }
	});
	
});