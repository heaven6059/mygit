var billumlabelprint ={};
billumlabelprint.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billumlabelprint.printType = {};
billumlabelprint.initPrintType = function(data){
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
	billumlabelprint.printType = temp;
};
$(document).ready(function(){
	billumlabelprint.loadLoc();
	billumlabelprint.initPrinter();
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isSelectSum!=null){
		   				billumlabelprint.getQty = data.footer[1].getQty;
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[1]['getQty'] = billumlabelprint.getQty;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   			
		   		}
			}
		);
	billumlabelprint.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=UM_PRINT_TYPE',{},false,billumlabelprint.initPrintType);
	
	//品牌库
	wms_city_common.comboboxLoadFilter(
				['sysNo'],
				null,
				null,
				null,
				true,
				[true],
				BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
				{},
				null,
				null,
				null
				);
	//品牌库绑定级联事件
	wms_city_common.sysBind(
			'sysNo',
			['bufferName'],
			['/os_cust_buffer/getBufferBySys?locno='+billumlabelprint.locno+'&storeType=11'],
			[["bufferName","bufferName"]],
			[true]);
	
	radioChange('templateType');
});
billumlabelprint.loadLoc = function(){
	$.ajax({
	async : false,
	cache : false,
	type : 'GET',
	dataType : "json",
	url:BasePath+'/initCache/getCurrentUser',
	success : function(data) {
		billumlabelprint.user = data;
		billumlabelprint.locno = data.locno;
	}
});
};

//查询区域信息
billumlabelprint.searchArea = function(){
//	debugger;
	var audittmStart = $('#printTm_start').datebox('getValue');
	var audittmEnd = $('#printTm_end').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(audittmStart,audittmEnd)){    
		alert("打印日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_print_log/getList.json?locno='+billumlabelprint.locno+'&papertype=UM';
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};
//查询区域信息
billumlabelprint.searchFull = function(){
	var fromObjStr=convertArray($('#fullDataForm').serializeArray());
	var queryMxURL=BasePath+'/os_cust_buffer/fullPrintList?locno='+billumlabelprint.locno+'&storeType=11';
    $( "#full1DataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#full1DataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#full1DataGridJG").datagrid( 'load' );
    
};
billumlabelprint.searchLocClear = function(){
	$('#searchForm').form("clear");
};
billumlabelprint.clearForm = function(id){
	$('#'+id).form("clear");
};
//初始化打印人
billumlabelprint.initPrinter = function(){
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

billumlabelprint.printBatch = function(){
	var labelNos = [];
	var qty = parseInt($("#qty").val());
	var printType = $("#printType").combobox('getValue');
	if(qty<=0){
		alert("数量必须大于0");
		return ;
	}
	if(printType == null || printType == ''){
		alert("请选择打印类型");
		return ;
	}
	wms_city_common.loading("show","正在生成条码......");
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bill_um_labelprint/getLabelPrefix?qty='+qty,
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				labelNos = data.labelNos;
				 $("#dataGridJG").datagrid('load');
			}else{
				alert("获取标签失败!",2);
			}
		}
	});
	if(labelNos.length>0){
		var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	  	if(LODOP==null){
	  		return;
	  	}
		for(var i=0,length=labelNos.length;i<length;i++){
			LODOP.NewPage();
	        var html = "<div><table border='0' cellpadding='1' cellspacing='1' style='background-color: #000;width:100%'>" +
			        "<tr bgcolor='#fff'>" +
					"<td colspan='2' style='width:100%;font-weight:bold;text-align:center;'>门店装箱标签</td>" +
					"</tr>" +		
			        "<tr bgcolor='#fff'>" +
	        		"<td style='width:26%;'>店铺编号:</td><td style='width:75%'>&nbsp;</td>" +
	        		"</tr>" +
	        		"<tr bgcolor='#fff'>" +
	        		"<td style='height:65pt;width:26%;text-align:center'>备注:</td><td style='width:75%'>&nbsp;</td>" +
	        		"</tr>"+
	        		"<tr bgcolor='#fff'>" +
	        		"<td colspan='2' style='height:70pt;width:100%'></td>" +
	        		"</tr>" +
	        		"</table></div>"+
	        		"<div style='margin-top:5px;'><table border='0' cellpadding='1' cellspacing='1' style='background-color: #000;width:100%'>" +
	        		"<tr bgcolor='#fff'>" +
	        		"<td style='height:15%;width:100%;text-align:center;'>装箱副标签:</td>" +
	        		"</tr>" +
	        		"<tr bgcolor='#fff'>" +
	        		"<td style='height:70pt;width:100%;'></td>" +
	        		"</tr>" +
	        		"</table></div>";
	        LODOP.SET_PRINT_PAGESIZE(1,'780','1000',"");
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(160,50,220,70,"128A",labelNos[i]);
			LODOP.ADD_PRINT_BARCODE(280,50,220,70,"128A",labelNos[i]);
		}
		//LODOP.PRINT();
		LODOP.PREVIEW();
	}
};
billumlabelprint.showPrintMenu = function(){
	if(getTemplateType() == '00'){//精简打印
		$('#print_menu').menu('show', {    
			  left: 85,    
			  top: 30  
			});
	}else{//完整打印
		printFull();
	}
	
};
/**
 * jys
 */
billumlabelprint.printBatchNew = function(w,h){
	var labelNos = [];
	if($("#qty").val() == ''){
		alert("请输入打印数量");
		return;
	}
	var qty = parseInt($("#qty").val());
	var printType = $("#printType").combobox('getValue');
	if(qty<=0){
		alert("数量必须大于0");
		return ;
	}
	if(printType == null || printType == ''){
		alert("请选择打印类型");
		return ;
	}
	
	 var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
	var storeName = $("#storeName").val();
	wms_city_common.loading("show","正在生成条码......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_um_labelprint/getLabelPrefix',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				labelNos = data.labelNos;
				 $("#dataGridJG").datagrid('load');
			}else{
				alert("获取标签失败!",2);
			}
		}
	});
	if(labelNos.length>0){
		var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	  	if(LODOP==null){
	  		return;
	  	}
	  	var strStyle="<style> table,td,th {border-width: 0px;border-style: solid;border-collapse: collapse}</style>";
		for(var i=0,length=labelNos.length;i<length;i++){
			LODOP.NewPage();
	        var html = "<table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' style='border-collapse:collapse' bordercolor='#000'>" +
			        "<tr bgcolor='#fff'>" +
					"<td style='font-weight:bold;text-align:right;font-size:12px;height:36px;'>"+billumlabelprint.user.locname+" "+billumlabelprint.printType[printType]+" 装箱标签</td>" +
					"</tr>" +		
			        "<tr  bgcolor='#fff' style='height:50px;'>" +
	        		"<td>&nbsp;</td>" +
	        		"</tr>" +
	        		"<tr bgcolor='#fff'>" +
	        		"<td style='font-weight:bold;font-size:33px;text-align:center'>"+storeName+"</td>" +
	        		"</tr>"+
	        		"</table>";
	        LODOP.SET_PRINT_PAGESIZE(1,w,h,"");
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strStyle+html);
			LODOP.ADD_PRINT_BARCODE(45,17,170,43,"128A",labelNos[i]);
		}
		//LODOP.PRINT();
		LODOP.PREVIEW();
	}
};
function printFull(){
	var rows = $("#full2DataGridJG").datagrid('getRows');
	if(rows.length == 0){
		alert("没有待打印的店铺信息!");
		return;
	}
	var dtStr = '';
	for(var idx=0;idx<rows.length;idx++){
		$("#full2DataGridJG").datagrid('endEdit', idx);
		dtStr += rows[idx].storeNo + '_' + rows[idx].storeName + '_' + rows[idx].address + '_' + rows[idx].qty + '_' + (rows[idx].bufferName==null?"":rows[idx].bufferName);
		
		if(idx != rows.length - 1){
			dtStr += '||';
		}
		if(!$("#full2DataGridJG").datagrid('validateRow', idx)){
			alert("打印数量不能为空!");
			return;
		}
	}
	
	var vos = [];
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
  	LODOP.SET_PRINT_PAGESIZE(1, 1000, 1500, "");
  	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
  	wms_city_common.loading("show","正在生成条码......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:{dtStr:dtStr,printType:'NP'},
		url:BasePath+'/bill_um_labelprint/getLabel4Full',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="SUCCESS"){
				vos = data.data;
			}else{
				alert("获取标签失败!",2);
			}
		}
	});
	if(vos != null && vos.length > 0){
		for(var idx=0;idx<vos.length;idx++){
			var vo = vos[idx];
			var labels = vo.labels;
			if(labels != null && labels.length > 0){
				for(var i=0;i<labels.length;i++){
					LODOP.NewPage();
				  	var html = getFullHtmlBodyTemp(vo.storeName,vo.bufferName,vo.address,labels[i]);
				  	LODOP.ADD_PRINT_TABLE(0,0,"100%","100%",strStyle+html);
				  	LODOP.ADD_PRINT_BARCODE(200,90,240,40,"128A",labels[i]);
				  	LODOP.ADD_PRINT_BARCODE(105,245,80,40,"128A",vo.storeNo.substring(2));
				}
			}
		}
		LODOP.PREVIEW();
	}
}
function getFullHtmlBodyTemp(storeName,bufferName,address,labelNo){
	
	var html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' height='100%' style='border-collapse:collapse;font-size:13px;' bordercolor='#333333'>" +
				"<tr style='height:50px;font-size:24px;'>"+
				"	<td colspan='4' style='text-align:center;font-weight:bold;'>送货装箱</td>"+
				"	<td style='width:16%;'><span style='font-size:13px;'>流道:</span></td>"+
				"	<td style='width:17%;'>&nbsp;</td>"+
				"</tr>" +
				"<tr style='height:50px;'>"+
				"	<td style='width:19%;'>店铺名称:</td>"+
				"	<td colspan='3'>"+storeName+"</td>"+
				"	<td style='width:16%;'>线路:</td>"+
				"	<td style='width:17%;'>"+(bufferName == null ? "" : bufferName)+"</td>"+
				"</tr>"+
				"<tr style='height:50px;'>"+
				"	<td>店铺地址:</td>"+
				"	<td colspan='5'><div style='width:60%;'>"+address+"</div></td>"+
				"</tr>"+
				"<tr style='height:40px;font-size:13px;'>"+
				"	<td>装箱单号:</td>"+
				"	<td colspan='5' style='text-align:center;'><span style='font-size:20px;'>"+labelNo+"</span></td>"+
				"</tr>"+
				"<tr style='height:55px;'>"+
				"	<td colspan='6'><div style='width:19%;'>装箱单号</div></td>"+
				"</tr>"+
				"<tr style='text-align:center;font-size:20px;height:55px;'>"+
				"	<td colspan='2' style='width:33%;'>鞋&nbsp;□</td>"+
				"	<td colspan='2'>服&nbsp;□</td>"+
				"	<td colspan='2' style='width:33%;'>配&nbsp;□</td>"+
				"</tr>"+
				"<tr  style='height:70px;font-size:18px;font-weight:bold;'>"+
				"	<td>数量:</td>"+
				"	<td colspan='5'>&nbsp;</td>"+
				"</tr>"+
				"<tr style='height:70px;font-size:18px;font-weight:bold;'>"+
				"	<td>备注:</td>"+
				"	<td colspan='5'>&nbsp;</td>"+
				"</tr>"+
				"<tr style='height:70px;font-size:18px;'>"+
				"	<td style='font-weight:bold;'>重量:</td>"+
				"	<td colspan='2'>&nbsp;</td>"+
				"	<td style='width:19%;text-align:right;'><span style='font-size:13px;'>发货签字</span></td>"+
				"	<td colspan='2'>&nbsp;</td>"+
				"</tr>"+
				"</table>";
	return html;
}
function getTemplateType(){
	var objs = $("input[name='templateType']");
	for(var idx=0;idx<objs.length;idx++){
		if(objs[idx].checked==true){
			return objs[idx].value;
		}
	}
}
function setTemplateType(val){
	if(val != '00' && val != '01'){
		val = '00';
	}
	setCookie("lastTemplateType",val);
	var objs = $("input[name='templateType']");
	for(var idx=0;idx<objs.length;idx++){
		if(objs[idx].value == val){
			objs[idx].checked=true;
			break;
		}
	}
	if(val == '00'){
		$('#simpleTemplate').show();
		$('#fullTemplate').hide();
		$('#full1_div').hide();
		$('#full2_div').hide();
	}else{
		$('#simpleTemplate').hide();
		$('#fullTemplate').show();
		$('#full1_div').show();
		$('#full2_div').show();
	}
}
function radioChange(name){
	$("input[name='"+name+"']").bind("change",function(e){ 
		var val = this.value;
		setTemplateType(val);
	});
}
function setCookie(key,val){
	document.cookie= key+"="+val;
}
function getCookie(key){
	var _cookie = document.cookie;
	var arr1 = _cookie.split("; ");
	for(var idx=0;idx<arr1.length;idx++){
		if(arr1[idx].indexOf(key, 0) == 0){
			var arr2 = arr1[idx].split("=");
			if(arr2.length == 2 && arr2[0] == key){
				return arr2[1];
			}
		}
	}
	return null;
}
billumlabelprint.add2Ready = function(){
	var rows = $('#full1DataGridJG').datagrid('getChecked');
	var rows2 = $('#full2DataGridJG').datagrid('getRows');
	if(rows.length == 0){
		alert('请选择准备打印的数据!');
		return;
	}
	if(rows2.length > 0){
		for(var idx=0;idx<rows2.length;idx++){
			for(var i=0;i<rows.length;i++){
				if(rows2[idx].storeNo == rows[i].storeNo){
					alert("待打印的店铺信息中已经存在【"+rows[i].storeName+"】,不能添加!");
					return;
				}
			}
		}
	}
	billumlabelprint.insertRowAtEnd('full2DataGridJG',rows);
};
billumlabelprint.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	$.each(rowData, function(index, item){
		item.qty = 1;
		tempObj.datagrid('appendRow', item);
	});
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('selectRow',tempIndex);
	
};
billumlabelprint.clearDataGrid = function(id){
	var tempObj = $('#'+id);
	var rows = $('#full1DataGridJG').datagrid('getRows');
	if(rows.length > 0){
		$.messager.confirm("确认","您确定清空待打印店铺信息吗？", function (r){
			if(r){
				tempObj.datagrid('loadData',{total:0,rows:[]});
			}
		});
		
	}else{
		alert('没有可清空的数据!');
	}
	
};
billumlabelprint.deleteDataGridByCheck = function(id){
	var tempObj = $('#'+id);
	var rows = tempObj.datagrid('getChecked');
    if(rows.length > 0){
    	$.messager.confirm("确认","您确定删除所选择信息吗？", function (r){
			if(r){
				for(var idx=0;idx<rows.length;idx++){
		    		var rowIndex = tempObj.datagrid('getRowIndex', rows[idx]);
		            tempObj.datagrid('deleteRow', rowIndex);
		    	}
			}
		});
    }else{
    	alert('请选择需要删除的数据!');
    }
};