var billuslabelprint ={};
billuslabelprint.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billuslabelprint.printType = {};
billuslabelprint.initPrintType = function(data){
	$('#printType').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	});
	var temp = {};
	for(var idx=0;idx<data.length;idx++){
		temp[data[idx].itemvalue] = data[idx].itemname;
	}
	billuslabelprint.printType = temp;
};
$(function(){
	billuslabelprint.loadLoc();
	billuslabelprint.initPrinter();
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isSelectSum!=null){
		   				billuslabelprint.getQty = data.footer[1].getQty;
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[1]['getQty'] = billuslabelprint.getQty;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   			
		   		}
			}
		);
	billuslabelprint.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=UM_PRINT_TYPE',{},false,billuslabelprint.initPrintType);
	billuslabelprint.initStoreNo();
});
//初始化客户信息
billuslabelprint.initStoreNo = function(){
	$('#storeNoCondition').combogrid({
		 panelWidth:400,   
         idField:'storeNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json?storeType=11',   
         columns:[[  
			{field:'storeNo',title:'门店编码',width:110, align:'left'},  
			{field:'storeName',title:'门店名称',width:243, align:'left'}  
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
billuslabelprint.loadLoc = function(){
	$.ajax({
	async : false,
	cache : false,
	type : 'GET',
	dataType : "json",
	url:BasePath+'/initCache/getCurrentUser',
	success : function(data) {
		billuslabelprint.user = data;
		billuslabelprint.locno = data.locno;
	}
});
};

//查询区域信息
billuslabelprint.searchArea = function(){
//	debugger;
	var audittmStart = $('#printTm_start').datebox('getValue');
	var audittmEnd = $('#printTm_end').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(audittmStart,audittmEnd)){    
		alert("打印日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_print_log/getList.json?locno='+billuslabelprint.locno+'&papertype=US';
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

billuslabelprint.searchLocClear = function(){
	$('#searchForm').form("clear");
};

//初始化打印人
billuslabelprint.initPrinter = function(){
	wms_city_common.comboboxLoadFilter(
			["printer"],
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
billuslabelprint.getLabel = function(qty,type){
	var labels = null;
	var prarms = {
			printType:type,
			qty:qty
	};
	wms_city_common.loading("show","正在生成条码......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:prarms,
		url:BasePath+'/bill_us_labelprint/getLabelPrefix',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				 $("#dataGridJG").datagrid('load');
				 labels = data.labelNos;
			}else{
				alert("获取标签失败!",2);
			}
		}
	});
	return labels;
};
billuslabelprint.printUM = function(){
	var qtys = $("#qty").val();
	if(qtys == ''){
		alert("请输入打印数量");
		return;
	}
	var qty = parseInt(qtys);
	if(qty<=0){
		alert("数量必须大于0");
		return ;
	}
	var storeNo = $('#storeNoCondition').combogrid('getValue');
	if(storeNo == ''){
		alert("门店不能为空!");
		return;
	}
	var rows = $('#storeNoCondition').combogrid('grid').datagrid('getRows');
	if(rows == null || rows.length == 0){
		alert("该门店无效,请重新输入!");
		return;
	}
	var storeName = null;
	for(var idx=0;idx<rows.length;idx++){
		if(rows[idx].storeNo == storeNo){
			storeName = rows[idx].storeName;
			break;
		}
	}
	if(storeName == null){
		alert("该门店无效,请重新输入!");
		return;
	}
	
	
  	var labels = billuslabelprint.getLabel(qty,"US");
  	if(labels != null && labels.length > 0){
  		var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	  	if(LODOP==null){
  	  		return;
  	  	}
  	  	LODOP.SET_PRINT_PAGESIZE(1,'1000','900',"");
  	  	var html = billuslabelprint.templateUM(storeNo,storeName);
  	  	for(var idx=0;idx<labels.length;idx++){  	  		
  	  		LODOP.NewPage();
  	  		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
  	  		LODOP.ADD_PRINT_BARCODE(48,85,260,39,"128A",labels[idx]);
  	  		LODOP.ADD_PRINT_BARCODE(273,85,260,35,"128A",labels[idx]);
  	  	}
  	  	LODOP.PREVIEW();
  	}else{
  		alert("没有获取到箱标签!");
  	}
  	
};
billuslabelprint.templateUM = function(storeNo,storeName){
	var html = "<table border='1' cellpadding='1' cellspacing='1' width='100%' height='100%' style='border-collapse:collapse;font-size:13px;' bordercolor='#333333'>";
	html += "<tr style='text-align:center;font-weight:bold;height:30px;font-size:16px;'>"+
				"<td colspan='4'>店铺&nbsp;&nbsp;装箱单</td>"+
			"</tr>"+
			"<tr>"+
				"<td rowspan='2'>箱号</td>"+
				"<td colspan='3' style='height:40px;'>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='text-align:center;'>&nbsp;</td>"+
				"<td>数量</td>"+
				"<td>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='width:18%;'>收货仓库</td>"+
				"<td style='text-align:center;font-weight:bold;font-size:14px;'>"+billuslabelprint.user.locname+"</td>"+
				"<td style='width:12%;'>编号</td>"+
				"<td style='width:25%;text-align:center;'>"+billuslabelprint.user.locno+"</td>"+
			"</tr>"+
			"<tr>"+
				"<td>发货店铺</td>"+
				"<td>&nbsp;</td>"+
				"<td>编号</td>"+
				"<td style='text-align:center;'>"+storeNo+"</td>"+
			"</tr>"+
			"<tr>"+
				"<td>备注</td>"+
				"<td colspan='3' style='height:60px;'>&nbsp;</td>"+
			"</tr>"+
			"<tr style='height:1px;'>"+
				"<td colspan='4'></td>"+
			"</tr>"+
			"<tr style='text-align:center;'>"+
				"<td colspan='4'>店铺&nbsp;&nbsp;装箱单&nbsp;&nbsp;副标(退仓库)</td>"+
			"</tr>"+
			"<tr>"+
				"<td>收货方</td>"+
				"<td style='text-align:center;font-weight:bold;'>"+billuslabelprint.user.locname+"</td>"+
				"<td>编号</td>"+
				"<td style='text-align:center;'>"+billuslabelprint.user.locno+"</td>"+
			"</tr>"+
			"<tr>"+
				"<td>发货店</td>"+
				"<td>&nbsp;</td>"+
				"<td>编号</td>"+
				"<td style='text-align:center;'>"+storeNo+"</td>"+
			"</tr>"+
			"<tr>"+
				"<td>箱号</td>"+
				"<td colspan='3' style='height:36px;'>&nbsp;</td>"+
			"</tr>"+
		"</table>";
	return html;
};
billuslabelprint.printZD = function(){
	var qtys = $("#qty").val();
	if(qtys == ''){
		alert("请输入打印数量");
		return;
	}
	var qty = parseInt(qtys);
	if(qty<=0){
		alert("数量必须大于0");
		return ;
	}
  	var labels = billuslabelprint.getLabel(qty,"US");
  	if(labels != null && labels.length > 0){
  		var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	  	if(LODOP==null){
  	  		return;
  	  	}
  	  	LODOP.SET_PRINT_PAGESIZE(1,'1000','900',"");
  	  	var html = billuslabelprint.templateZD();
  	  	for(var idx=0;idx<labels.length;idx++){  	  		
  	  		LODOP.NewPage();
  	  		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
  	  		LODOP.ADD_PRINT_BARCODE(48,85,260,39,"128A",labels[idx]);
  	  		LODOP.ADD_PRINT_BARCODE(273,85,260,35,"128A",labels[idx]);
  	  	}
  	  	LODOP.PREVIEW();
  	}else{
  		alert("没有获取到箱标签!");
  	}
  	
};
billuslabelprint.templateZD = function(){
	var html = "<table border='1' cellpadding='1' cellspacing='1' width='100%' height='100%' style='border-collapse:collapse;font-size:13px;' bordercolor='#333333'>";
	html += "<tr style='text-align:center;font-weight:bold;height:30px;font-size:16px;'>"+
				"<td colspan='4'>店铺&nbsp;&nbsp;装箱单&nbsp;(转店铺)</td>"+
			"</tr>"+
			"<tr>"+
				"<td rowspan='2'>箱号</td>"+
				"<td colspan='3' style='height:40px;'>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='text-align:center;'>&nbsp;</td>"+
				"<td>数量</td>"+
				"<td>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='width:18%;'>收货店铺</td>"+
				"<td style='text-align:center;font-weight:bold;font-size:14px;'>&nbsp;</td>"+
				"<td style='width:12%;'>编号</td>"+
				"<td style='width:25%;text-align:center;'>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td>发货店铺</td>"+
				"<td>&nbsp;</td>"+
				"<td>编号</td>"+
				"<td style='text-align:center;'>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td>备注</td>"+
				"<td colspan='3' style='height:60px;'>&nbsp;</td>"+
			"</tr>"+
			"<tr style='height:1px;'>"+
				"<td colspan='4'></td>"+
			"</tr>"+
			"<tr style='text-align:center;'>"+
				"<td colspan='4'>店铺&nbsp;&nbsp;装箱单&nbsp;&nbsp;副标(转店铺)</td>"+
			"</tr>"+
			"<tr>"+
				"<td>收货方</td>"+
				"<td style='text-align:center;font-weight:bold;'>&nbsp;</td>"+
				"<td>编号</td>"+
				"<td style='text-align:center;'>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td>发货店</td>"+
				"<td>&nbsp;</td>"+
				"<td>编号</td>"+
				"<td style='text-align:center;'>&nbsp;</td>"+
			"</tr>"+
			"<tr>"+
				"<td>箱号</td>"+
				"<td colspan='3' style='height:36px;'>&nbsp;</td>"+
			"</tr>"+
		"</table>";
	return html;
};