var wms_city_common = {};

$.extend($.fn.validatebox.methods, {    
    remove: function(jq, newposition){    
        return jq.each(function(){    
            $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');  
        });    
    },  
    reduce: function(jq, newposition){    
        return jq.each(function(){    
           var opt = $(this).data().validatebox.options;  
           $(this).addClass("validatebox-text").validatebox(opt);  
        });    
    },
    reset: function(jq, newposition){ 
    	 return jq.each(function(){    
             $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');  
             var opt = $(this).data().validatebox.options;  
             $(this).addClass("validatebox-text").validatebox(opt);  
         });
    }
}); 

//清空datagrid数据
$.extend($.fn.datagrid.methods,{
	 clearData:function(jq){
	     return jq.each(function(){
	          $(this).datagrid('loadData',{total:0,rows:[]});
	     });
	 }
});

//将委托业主封装
wms_city_common.converOwnerJsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//格式化委托业主
wms_city_common.init_Owner_Text = {};
wms_city_common.columnOwnerFormatter = function(value, rowData, rowIndex){
	return wms_city_common.init_Owner_Text[value];
};

//将供应商封装
wms_city_common.converSupplierJsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].supplierNo +"\":\""+data[i].supplierName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//格式化供应商
wms_city_common.init_Supplier_Text = {};
wms_city_common.columnSupplierFormatter = function(value, rowData, rowIndex){
	if(wms_city_common.init_Supplier_Text != null) {
		return wms_city_common.init_Supplier_Text[value];
	} else {
		return null;
	}
	
};

//加载效果
wms_city_common.loading = function(type,msg){
	if(msg==null||msg==""){
		msg = "请稍后,正在加载......";
	}
	var  body_width =  document.body.clientWidth;
	var  body_height= document.body.clientHeight;
	//展示loading
	if(type=="show"){
		var myload = $("<div id='myload' style='border:2px solid #95B8E7;display:inline-block;padding:10px 8px;;position:absolute;z-index:999999999;top:0px;left:0px;background:#ffffff'>"+
				"<div style='float:left;'><img src='../resources/css/styles/images/loading.gif'></div>"+
				"<div style='float:left;display:inline-block;margin-top:2px;margin-left:5px;'>"+msg+"</div>"+
		 "</div>").appendTo($("body"));
		 var myloadwidth = myload.width();
		 var myloadheight = myload.height();
		 myload.css({"left":(body_width-myloadwidth)/2,"top":(body_height-myloadheight)/2});
		 $("<div id='remote_load' style='position:fixed;width:100%;height:"+body_height+"px;z-index:99999999;top:0px;left:0px;background-color: #ccc;opacity: 0.3;filter: alpha(opacity = 30);'></div>").appendTo($("body"));
	}else{
		$("#myload").remove();
		$("#remote_load").remove();
	}
};

//关闭window删除window中正在弹出的tip提示
wms_city_common.closeTip = function(id){
	$('#'+id).window({
		onBeforeClose:function(){
			$('.tooltip').remove();
		}
	});
};

wms_city_common.beginEditAllLine = function(id){
	var curObj = $("#"+id);
	var rows = curObj.datagrid("getRows");
	for(var i=0,length = rows.length;i<length;i++){
		curObj.datagrid('beginEdit', i);
   	}
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
}

//获取当前日期
wms_city_common.getCurDate = function(){
	var now = new Date(); 
	var SY = now.getFullYear(); 
	var SM = now.getMonth(); 
	var SD = now.getDate(); 
	return SY+"-"+(SM+1)+"-"+SD;
};

//判断开始日期是否小于结束日期可相等
wms_city_common.isStartLessThanEndDate = function(startDate,endDate){   
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
};

wms_city_common.convertArray = function(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
};

//加载品牌
wms_city_common.loadBrand = function(id){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/brand/get_bizDy',
		success : function(data) {
			$(id).combobox({
			    data:data,
			    valueField:"brandNo",
			    textField:"textFieldName",
			    panelHeight:200,
			    loadFilter:function(data){
			    	if(data.length>0){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.textFieldName = tempData.brandName+'→'+tempData.brandNo;
			       		 }
			       	}
			    	return data;
			    }
			});
		}
	});
};
//加载品牌
wms_city_common.loadBrands = function(id, isShowCheckAllArray){
	wms_city_common.comboboxLoadFilter(
			[id],
			"brandNo",
			"brandName",
			"brandName",
			false,
			[isShowCheckAllArray],
			BasePath+'/brand/get_bizDy',
			{},
			null,
			null);
};
	
//加载品牌库
wms_city_common.loadSysNo = function(id){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$(id).combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
		}
	});
};
wms_city_common.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
/**
 * @param ids combobox的id
 * @param valueNo combobox的value字段名如brandNo
 * @param valueName value对应的描述字段名如brandName
 * @param showName combobox该显示的组合字段名
 * @param isDictionary 数据是否来源码表,如果是则valueNo,valueName,showName可为空
 * @param isShowCheckAllArray 是否显示全选,需要与ids一一对应
 * @param url 获取数据路径
 * @param params 获取数据参数
 * @param resultMap 初始化数据用于Formatter
 * @param method 自定义loadFilter方法
 * @param showType 显示类型 '0':No,'1':Name,其他:No→Name
 */
wms_city_common.comboboxLoadFilter = function(ids,valueNo,valueName,showName,isDictionary,isShowCheckAllArray,url,params,resultMap,method,showType){
	var data = null;
	if(url == null || url == ''){
		data = [];
	}else{
		wms_city_common.ajaxRequest(url,params,false,function(r){data = r;});
	}
	if(ids == null || ids.length == 0){
		return;
	}
	if(method != null){
		var obj  = null;
		if(isDictionary){
			valueNo = 'itemvalue';
			valueName = 'itemname';
			showName = 'itemnamedetail';
		}
		if(valueNo == null || valueNo == ''){
			return;
		}
		if(valueName == null || valueName == ''){
			return;
		}
		if(showName == null || showName == ''){
			return;
		}
		for(var idx=0;idx<ids.length;idx++){
			obj  = $("#"+ids[idx]);
			obj.combobox({
				data:data,
				valueField:valueNo,
				textField:showName,
				panelHeight:150,
				loadFilter:method
			});
		}
	}else{
		var obj  = null;
		if(isDictionary){
			valueNo = 'itemvalue';
			valueName = 'itemname';
			showName = 'itemnamedetail';
		}
		if(valueNo == null || valueNo == ''){
			return;
		}
		if(valueName == null || valueName == ''){
			return;
		}
		if(showName == null || showName == ''){
			return;
		}
		for(var idx=0;idx<ids.length;idx++){
			obj  = $("#"+ids[idx]);
			obj.combobox({
				data:data,
				valueField:valueNo,
				textField:showName,
				panelHeight:150,
				loadFilter:function(data){
					var first = {};
					first[valueNo] = '';
					first[showName] = '全选';
					var tempData = [];
					if(isShowCheckAllArray[idx]){
						tempData[tempData.length] = first;
					}
			    	for(var i=0;i<data.length;i++){
			    		data[i][valueNo] = data[i][valueNo];
			    		if(showType == '0'){
			    			data[i][showName] = data[i][valueNo];
			    		}else if(showType == '1'){
			    			data[i][showName] = data[i][valueName];
			    		}else{
			    			if(valueNo != valueName){
				    			data[i][showName] = data[i][valueNo] + '→' + data[i][valueName];
				    		}else{
				    			data[i][showName] = data[i][valueNo];
				    		}
			    		}
			    		tempData[tempData.length] = data[i];
			    		if(resultMap != null){
			    			resultMap[data[i][valueNo]] = data[i][valueName];
			    		}
			    	}
			    	return tempData;
				}
			});
		}
	}
};


/**
 * （多选）
 * @param ids combobox的id
 * @param valueNo combobox的value字段名如brandNo
 * @param valueName value对应的描述字段名如brandName
 * @param showName combobox该显示的组合字段名
 * @param isDictionary 数据是否来源码表,如果是则valueNo,valueName,showName可为空
 * @param isShowCheckAllArray 是否显示全选,需要与ids一一对应
 * @param url 获取数据路径
 * @param params 获取数据参数
 * @param resultMap 初始化数据用于Formatter
 * @param method 自定义loadFilter方法
 * @param showType 显示类型 '0':No,'1':Name,其他:No→Name
 * @param isEventsOff 不添加事件
 */
wms_city_common.comboboxLoadFilter4Multiple = function(ids,valueNo,valueName,showName,isDictionary,isShowCheckAllArray,url,params,resultMap,method,showType,isEventsOff){
	var data = null;
	if(url == null || url == ''){
		data = [];
	}else{
		wms_city_common.ajaxRequest(url,params,false,function(r){data = r;});
	}
	if(ids == null || ids.length == 0){
		return;
	}
	if(method != null){
		var obj  = null;
		if(isDictionary){
			valueNo = 'itemvalue';
			valueName = 'itemname';
			showName = 'itemnamedetail';
		}
		if(valueNo == null || valueNo == ''){
			return;
		}
		if(valueName == null || valueName == ''){
			return;
		}
		if(showName == null || showName == ''){
			return;
		}
		for(var idx=0;idx<ids.length;idx++){
			obj  = $("#"+ids[idx]);
			obj.combobox({
				data:data,
				multiple:true,
				valueField:valueNo,
				textField:showName,
				panelHeight:150,
				loadFilter:method
			});
		}
	}else{
		var obj  = null;
		if(isDictionary){
			valueNo = 'itemvalue';
			valueName = 'itemname';
			showName = 'itemnamedetail';
		}
		if(valueNo == null || valueNo == ''){
			return;
		}
		if(valueName == null || valueName == ''){
			return;
		}
		if(showName == null || showName == ''){
			return;
		}
		for(var idx=0;idx<ids.length;idx++){
			obj  = $("#"+ids[idx]);
			obj.combobox({
				data:data,
				multiple:true,
				valueField:valueNo,
				textField:showName,
				panelHeight:150,
				loadFilter:function(data){
					var first = {};
					first[valueNo] = '';
					first[showName] = '全选';
					var tempData = [];
					if(isShowCheckAllArray[idx]){
						tempData[tempData.length] = first;
					}
			    	for(var i=0;i<data.length;i++){
			    		data[i][valueNo] = data[i][valueNo];
			    		if(showType == '0'){
			    			data[i][showName] = data[i][valueNo];
			    		 }else if(showType == '1'){
			    			data[i][showName] = data[i][valueName];
			    		 }else{
			    			if(valueNo != valueName){
			    			  data[i][showName] = data[i][valueNo] + '→' + data[i][valueName];
			    			}else{
			    			  data[i][showName] = data[i][valueNo];
			    				 }
			    			}
			    		tempData[tempData.length] = data[i];
			    		if(resultMap != null){
			    			resultMap[data[i][valueNo]] = data[i][valueName];
			    		}
			    	}
			    	return tempData;
				}
			});		
			if(!isEventsOff){
				obj.combobox({
					onSelect:function(record){
				    	var obj = $('#'+this.id);
				    	var values = obj.combobox('getValues');
				    	var value=record[valueNo];
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
			}

			if(isShowCheckAllArray[idx]){
				obj.combobox('setValues',['']);	
			}
		}
	}
};



wms_city_common.loadAllSysNo4Cascade = function(objs){
	//params是一个对象数组  对象obj 包含 两个属性  sysNoObj  1：品牌库combox 对象  2 brandNoObj 品牌combox 对象  如果brandNoObj为空就不做级联
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO&isAll=1',
		success : function(data) {
			for(var i=0,length=objs.length;i<length;i++){
				var obj = objs[i];
				obj.sysNoObj.combobox({
				    data:data,
				    valueField:"itemvalue",
				    textField:"itemnamedetail",
				    panelHeight:150,
				    loadFilter:function(data){
						var tempData = [];
				    	tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
				    	for(var i=0;i<data.length;i++){
				    		tempData[tempData.length] = data[i];
				    	}
				    	return tempData;
				    }
				});
				if(obj.brandNoObj!=undefined){
					obj.sysNoObj.combobox({
						onSelect:function(){
				    		loadBrand4SysNo(obj.brandNoObj,$(this).combobox("getValue"));
						}
					});
		    	}
			}
		}
	});
};
/**
 * isShowAllCheckArray 是否显示全选(默认显示全选)，需与objs中{}的位置一一对应
 */
wms_city_common.loadSysNo4Cascade = function(objs,isShowAllCheckArray){
	//params是一个对象数组  对象obj 包含 两个属性  sysNoObj  1：品牌库combox 对象  2 brandNoObj 品牌combox 对象  如果brandNoObj为空就不做级联
	var map = {};
	if(isShowAllCheckArray == null || isShowAllCheckArray.length == 0){
		isShowAllCheckArray = [];
		for(var i=0,length=objs.length;i<length;i++){
			isShowAllCheckArray[i] = true;
		}
	}
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			for(var i=0,length=objs.length;i<length;i++){
				var obj = objs[i];
				map[obj.sysNoObj.selector] = isShowAllCheckArray[i];
				obj.sysNoObj.combobox({
				    data:data,
				    valueField:"itemvalue",
				    textField:"itemnamedetail",
				    panelHeight:150,
				    loadFilter:function(data){
						var tempData = [];
						if(map["#"+this.id]){
							tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
						}
				    	for(var i=0;i<data.length;i++){
				    		tempData[tempData.length] = data[i];
				    	}
				    	return tempData;
				    }
				});
				if(obj.brandNoObj!=undefined){
					obj.sysNoObj.combobox({
						onSelect:function(){
				    		loadBrand4SysNo(obj.brandNoObj,$(this).combobox("getValue"),map["#"+this.id]);
						}
					});
		    	}
			}
		}
	});
};
function loadBrand4SysNo(obj,sysNo,isShowAllCheck){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/brand/get_bizDy?sysNo='+sysNo,
		success : function(data) {
			obj.combobox({
			    data:data,
			    valueField:"brandNo",
			    textField:"textFieldName",
			    panelHeight:150,
			    loadFilter:function(data){
			    	if(sysNo == null || sysNo == ''){
			    		if(isShowAllCheck){
			    			data = [{brandNo:'',textFieldName:'全选'}];
			    		}else{
			    			data = [];
			    		}
						return data;
					}
			    	if(data.length>0){
						var tempData = [];
						if(isShowAllCheck){
							tempData[tempData.length] = {brandNo:'',textFieldName:'全选'};
						}
				    	
				    	for(var i=0;i<data.length;i++){
				    		var temp = {};
				    		temp.brandNo = data[i].brandNo;
				    		temp.textFieldName = data[i].brandNo + '→' + data[i].brandName;
				    		tempData[tempData.length] = temp;
				    	}
				    	return tempData;
			       	}
			    	return data;
			    }
			});
		}
	});
}


/**
 * isShowAllCheckArray 是否显示全选(默认显示全选)，需与objs中{}的位置一一对应
 */
wms_city_common.loadSysNoMultiple4Cascade = function(objs,isShowAllCheckArray){
	//params是一个对象数组  对象obj 包含 两个属性  sysNoObj  1：品牌库combox 对象  2 brandNoObj 品牌combox 对象  如果brandNoObj为空就不做级联
	var map = {};
	if(isShowAllCheckArray == null || isShowAllCheckArray.length == 0){
		isShowAllCheckArray = [];
		for(var i=0,length=objs.length;i<length;i++){
			isShowAllCheckArray[i] = true;
		}
	}
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			for(var i=0,length=objs.length;i<length;i++){
				var obj = objs[i];
				map[obj.sysNoObj.selector] = isShowAllCheckArray[i];
				obj.sysNoObj.combobox({
				    data:data,
				    valueField:"itemvalue",
				    textField:"itemnamedetail",
				    panelHeight:150,
				    loadFilter:function(data){
						var tempData = [];
						if(map["#"+this.id]){
							tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
						}
				    	for(var i=0;i<data.length;i++){
				    		tempData[tempData.length] = data[i];
				    	}
				    	return tempData;
				    }
				});
				if(obj.brandNoObj!=undefined){
					obj.sysNoObj.combobox({
						onSelect:function(){
							loadBrand4SysNoMultiple(obj.brandNoObj,$(this).combobox("getValue"),map["#"+this.id]);
						}
					});
		    	}
			}
		}
	});
};
function loadBrand4SysNoMultiple(obj,sysNo,isShowAllCheck){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/brand/get_bizDy?sysNo='+sysNo,
		success : function(data) {
			obj.combobox({
			    data:data,
			    valueField:"brandNo",
			    textField:"textFieldName",
			    panelHeight:150,
			    multiple:true,
			    loadFilter:function(data){
			    	if(sysNo == null || sysNo == ''){
			    		if(isShowAllCheck){
			    			data = [{brandNo:'',textFieldName:'全选'}];
			    		}else{
			    			data = [];
			    		}
						return data;
					}
			    	if(data.length>0){
						var tempData = [];
						if(isShowAllCheck){
							tempData[tempData.length] = {brandNo:'',textFieldName:'全选'};
						}
				    	for(var i=0;i<data.length;i++){
				    		var temp = {};
				    		temp.brandNo = data[i].brandNo;
				    		temp.textFieldName = data[i].brandNo + '→' + data[i].brandName;
				    		tempData[tempData.length] = temp;
				    	}
				    	return tempData;
			       	}
			    	return data;
			    },onSelect:function(record){
			    	var obj = $('#'+this.id);
			    	var values = obj.combobox('getValues');
			    	var brandNo=record.brandNo;
			    	var newValues=[];
			    	if(brandNo==''){
			    		newValues.push(brandNo);	
			    		obj.combobox('setValues',newValues);			    		
			    	}else{
			    		for(var i=0; i< values.length;i++){
				    		if(values[i]!=''){				    			
				    			newValues.push(values[i]);
				    		}
				    	}		
			    		obj.combobox('setValues',newValues);
			    	}
			    },onUnselect:function(record){
			    	var obj = $('#'+this.id);
			    	var values = obj.combobox('getValues');	
			    	if(values.length==0 && isShowAllCheck){
			    		obj.combobox('setValues',['']);	
			    	}
			    }
			    
			});
			
			if(isShowAllCheck){
				obj.combobox('setValues',['']);	
			}
		}
	});
}

String.prototype.replaceAll  = function(s1,s2){     
    return this.replace(new RegExp(s1,"gm"),s2);     
};

//初始化 品质  为了保证必须先选库存属性，在每次初始化新增弹出窗时请调用此方法；
wms_city_common.resetQuality = function(qualityId){
	var qualityObj = $('#'+qualityId);
	qualityObj.combobox("loadData",[]);
};
//库存属性，品质级联，参数说明  itemId：库存属性下拉框ID   qualityId:品质下拉框ID 弹出新增窗口时请调用wms_city_common.resetQuality 初始化品质
//showAllSelect是否显示全选--JYS
wms_city_common.initItemTypeAndQuality = function(itemId,qualityId,showAllSelect){
	var itemTypeData;
	var qualityData;
	var curQualitData = [];
	var itemTypeObj = $('#'+itemId);
	var qualityObj = $('#'+qualityId);
	//加载所有的库存信息
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
		success : function(data) {
			itemTypeData = data;
			wms_city_common.formatteritemTypeData = converLookupJsonObj(data);
			if(showAllSelect){
				itemTypeData = [];
				itemTypeData[0] = {itemvalue:'',itemnamedetail:'全选'};
				for(var idx=0;idx<data.length;idx++){
					itemTypeData[itemTypeData.length] = data[idx];
				}
				curQualitData[0] = {itemvalue:'',itemnamedetail:'全选'};
			}
		}
	});
	//加载所有的品质
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
		success : function(data) {
			qualityData = data;
			wms_city_common.formatterQualitData = converLookupJsonObj(data);
		}
	});
	itemTypeObj.combobox({
		data:itemTypeData,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:150,
		onChange:function(){
			curQualitData = getCurQualitData($(this).combobox('getValue'),qualityData,showAllSelect);
			qualityObj.combobox('setValue',"");
			qualityObj.combobox("loadData",curQualitData);
		}
	});
	qualityObj.combobox({
		data:curQualitData,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:150
	});
};
//格式化商品类型
wms_city_common.formatteritemTypeData = {};
wms_city_common.columnItemTypeFormatter = function(value, rowData, rowIndex){
	return wms_city_common.formatteritemTypeData[value];
};

//格式化品质
wms_city_common.formatterQualitData = {};
wms_city_common.columnQualityFormatter = function(value, rowData, rowIndex){
	return wms_city_common.formatterQualitData[value];
};
function getCurQualitData(itemType,qualityData,showAllSelect){
	if(itemType == ''){
		if(showAllSelect){
			return [{itemvalue:'',itemnamedetail:'全选'}];
		}else{
			return [];
		}
	}
	itemType = parseInt(itemType);
	var curData = [];
	switch(itemType){
	 case 9:
	 case 0:
		 curData = qualityData;
		 break;
	 case 2:
	 case 6:
	 case 1:
	 case 3:
	 case 5:
	 case 7:
	 case 8:
	 case 10:
	 case 12:
	 case 14:
	 case 15:
	 case 16:
	 case 17:
		 curData.push(qualityData[0]);
		 break;
	 case 4:
	 case 11:
	 case 13:
		 curData.push(qualityData[1]);
		 break;
	 default:
		 curData = qualityData;
	}
	if(showAllSelect){
		var hasAllSelectData = [];
		hasAllSelectData = [];
		hasAllSelectData[0] = {itemvalue:'',itemnamedetail:'全选'};
		for(var idx=0;idx<curData.length;idx++){
			hasAllSelectData[hasAllSelectData.length] = curData[idx];
		}
		return hasAllSelectData;
	}
	return curData;
}

function converLookupJsonObj(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};




//仓区下拉框
wms_city_common.initWareNo = function(locno,wareNoId,areaNoId,stockNoId){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defware/get_biz?locno='+locno,
		success : function(data) {
			$('#'+wareNoId).combobox({
			    data:data,
			    valueField:'wareNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    onSelect:function(){
			    		wms_city_common.initArea(locno,wareNoId,areaNoId,stockNoId);
			    	},
			    loadFilter:function(data){
					if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.wareNo+'→'+tempData.wareName;
			       		 }
			 		}
					return data;
			   }
			});
		}
	});
};
//库区
wms_city_common.initArea = function(locno,wareNoId,areaNoId,stockNoId){
	var wareNo = $("#"+wareNoId).combobox("getValue");
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defarea/get_biz?locno='+locno+"&wareNo="+wareNo,
		success : function(data) {
			$('#'+areaNoId).combobox({
			    data:data,
			    valueField:'areaNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    onSelect:function(){wms_city_common.initStock(locno,wareNoId,areaNoId,stockNoId);},
			    loadFilter:function(data){
					if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.areaNo+'→'+tempData.areaName;
			       		 }
			 		}
					return data;
			   }
			});
		}
	});
	
};
//通道
wms_city_common.initStock = function(locno,wareNoId,areaNoId,stockNoId){
	var wareNo = $("#"+wareNoId).combobox("getValue");
	var areaNo = $("#"+areaNoId).combobox("getValue");
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defstock/get_biz?locno='+locno+"&wareNo="+wareNo+"&areaNo="+areaNo,
		success : function(data) {
			$('#'+stockNoId).combobox({
			    data:data,
			    valueField:'stockNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    loadFilter:function(data){
					if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.stockNo+'→'+tempData.stockNo;
			       		 }
			 		}
					return data;
			   }
			});
		}
	});
};
/**
 * 
 * @param locno 仓库编码
 * @param wareIds 仓区id组
 * @param areaIds 库区id组(为空时之后控件[包含此控件]无级联关系)
 * @param stockIds 通道id组(为空时之后控件[包含此控件]无级联关系)
 * @param cellIds 储位id组(为空时之后控件[包含此控件]无级联关系)
 * @param isShowCheckAllArray 是否显示全选的boolean组(需与wareIds一一对应)
 * @param urls 查询仓区的url组(可为空或者与wareIds一一对应)
 */
wms_city_common.initWareNoForCascade = function(locno,wareIds,areaIds,stockIds,cellIds,isShowCheckAllArray,urls){
	var map = {};
	var wareLen = 0; 
	var urlLen = urls==null?0:urls.length;
	if(wareIds == null || (wareLen=wareIds.length) == 0){
		return;
	}
	var data = [];
	var defaultUrl = BasePath+'/cm_defware/get_biz';
	var params = {locno:locno};
	for(var idx=0;idx<wareLen;idx++){
		if(urls != null && urls.length != 0){
			defaultUrl = urls[idx];
		}
		if(idx<=urlLen){
			data = [];
			wms_city_common.ajaxRequest(defaultUrl,params,false,function(r){data = r;});
		}
		map[wareIds[idx]] = {
						wareId:wareIds[idx],
						areaId:(areaIds == null || areaIds.length != wareLen)?null:areaIds[idx],
						stockId:(stockIds == null || stockIds.length != wareLen)?null:stockIds[idx],
						cellId:(cellIds == null || cellIds.length != wareLen)?null:cellIds[idx],
						isShowCheckAll:isShowCheckAllArray[idx]
				};
		var obj = $('#'+wareIds[idx]);
		obj.combobox({
		    data:data,
		    valueField:'wareNo',    
		    textField:'valueAndText',
		    panelHeight:150,
		    onSelect:function(){
		    		var wareId = this.id;
		    		var idMap = map[wareId];
		    		wms_city_common.initAreaForCascade(
		    				locno,
		    				wareId,
		    				idMap.areaId,
		    				idMap.stockId,
		    				idMap.cellId,
		    				idMap.isShowCheckAll
		    				);
		    	},
		    loadFilter:function(data){
		    	var first = {};
				first.wareNo = '';
				first.valueAndText = '全选';
				var tempData = [];
				if(isShowCheckAllArray[idx]){
					tempData[tempData.length] = first;
				}
		    	var temp = {};
		    	for(var i=0;i<data.length;i++){
		    		temp = {};
		    		temp.wareNo = data[i].wareNo;
		    		temp.valueAndText = data[i].wareNo + '→' + data[i].wareName;
		    		tempData[tempData.length] = temp;
		    	}
		    	return tempData;
		   }
		});
	}
};
wms_city_common.initAreaForCascade = function(locno,wareId,areaId,stockId,cellId,isShowCheckAll){
	if(areaId == null || areaId == ''){
		return;
	}
	var wareNo = $("#"+wareId).combobox("getValue");
	var data = [];
	var defaultUrl = BasePath+'/cm_defarea/get_biz?locno='+locno+"&wareNo="+wareNo;
	if(wareNo != ''){
		wms_city_common.ajaxRequest(defaultUrl,{},false,function(r){data = r;});
	}
	$('#'+areaId).combobox({
	    data:data,
	    valueField:'areaNo',    
	    textField:'valueAndText',
	    panelHeight:150,
	    onSelect:function(){
	    		wms_city_common.initStockForCascade(
	    				locno,wareId,areaId,stockId,cellId,isShowCheckAll
	    				);
	    	},
	    loadFilter:function(data){
	    	var first = {};
			first.areaNo = '';
			first.valueAndText = '全选';
			var tempData = [];
			if(isShowCheckAll){
				tempData[tempData.length] = first;
			}
	    	var temp = {};
	    	for(var i=0;i<data.length;i++){
	    		temp = {};
	    		temp.areaNo = data[i].areaNo;
	    		temp.valueAndText = data[i].areaNo + '→' + data[i].areaName;
	    		tempData[tempData.length] = temp;
	    	}
	    	return tempData;
	   }
	});
	wms_city_common.initStockForCascade(locno,wareId,areaId,stockId,cellId,isShowCheckAll);
};
wms_city_common.initStockForCascade = function(locno,wareId,areaId,stockId,cellId,isShowCheckAll){
	if(stockId == null || stockId == ''){
		return;
	}
	var wareNo = $("#"+wareId).combobox("getValue");
	var areaNo = $("#"+areaId).combobox("getValue");
	var data = [];
	var defaultUrl = BasePath+'/cm_defstock/get_biz?locno='+locno+"&wareNo="+wareNo+"&areaNo="+areaNo;
	if(wareNo != '' && areaNo != ''){
		wms_city_common.ajaxRequest(defaultUrl,{},false,function(r){data = r;});
	}
	$('#'+stockId).combobox({
	    data:data,
	    valueField:'stockNo',    
	    textField:'valueAndText',
	    panelHeight:150,
	    onSelect:function(){
	    		wms_city_common.initCellForCascade(
	    				locno,wareId,areaId,stockId,cellId,isShowCheckAll
	    				);
	    	},
	    loadFilter:function(data){
	    	var first = {};
			first.stockNo = '';
			first.valueAndText = '全选';
			var tempData = [];
			if(isShowCheckAll){
				tempData[tempData.length] = first;
			}
	    	var temp = {};
	    	for(var i=0;i<data.length;i++){
	    		temp = {};
	    		temp.stockNo = data[i].stockNo;
	    		temp.valueAndText = data[i].stockNo + '→' + data[i].aStockNo;
	    		tempData[tempData.length] = temp;
	    	}
	    	return tempData;
	   }
	});
	wms_city_common.initCellForCascade(locno,wareId,areaId,stockId,cellId,isShowCheckAll);
};
wms_city_common.initCellForCascade = function(locno,wareId,areaId,stockId,cellId,isShowCheckAll){
	if(cellId == null || cellId == ''){
		return;
	}
	var wareNo = $("#"+wareId).combobox("getValue");
	var areaNo = $("#"+areaId).combobox("getValue");
	var stockNo = $("#"+stockId).combobox("getValue");
	var data = [];
	var defaultUrl = BasePath+'/cm_defcell/get_biz?locno='+locno+"&wareNo="+wareNo+"&areaNo="+areaNo+"&stockNo="+stockNo;
	if(wareNo != '' && areaNo != '' && stockNo != ''){
		wms_city_common.ajaxRequest(defaultUrl,{},false,function(r){data = r;});
	}
	$('#'+cellId).combobox({
	    data:data,
	    valueField:'cellNo',    
	    textField:'valueAndText',
	    panelHeight:150,
	    loadFilter:function(data){
	    	var first = {};
			first.cellNo = '';
			first.valueAndText = '全选';
			var tempData = [];
			if(isShowCheckAll){
				tempData[tempData.length] = first;
			}
	    	var temp = {};
	    	for(var i=0;i<data.length;i++){
	    		temp = {};
	    		temp.cellNo = data[i].cellNo;
	    		temp.valueAndText = data[i].cellNo;
	    		tempData[tempData.length] = temp;
	    	}
	    	return tempData;
	   }
	});
};
/**
* 为品牌库下拉绑定级联关系
**/
wms_city_common.sysBind = function(sysId,childIds,childUrls,childFields,isShowAllSelectArray){
	var childLen = childIds.length;
	if(childLen == 0){
		return;
	}
	var url = null;
	var temp = $("#"+sysId).combobox('getData');
	$("#"+sysId).combobox({
		data:temp,
	    valueField:'itemvalue',    
	    textField:'itemnamedetail',
	    panelHeight:150,
	    loadFilter:function(data){
	    	return data;
	    },
		onSelect:function(value){
    		for(var idx=0;idx<childLen;idx++){
    			if(value != null && value != '' && value.itemvalue != ''){
    				url = BasePath + childUrls[idx];
    			}else{
    				url = null;
    			}
    			wms_city_common.comboboxLoadFilter(
    					[childIds[idx]],
    					childFields[idx][0],
    					childFields[idx][1],
    					'valueAndText',
    					false,
    					[isShowAllSelectArray[idx]],
    					url,
    					{'sysNo':value.itemvalue},
    					null,
    					null
    			);
    		}
		}
	});
};
function replaceAll(value,s,d){
	var idx = -1;
	while((idx = value.indexOf(s)) >=0){
		value = value.replace(s,d);
	}
	return value;
}
/**
 * 为parentId绑定级联select事件
 * @param parentId 父控件ID
 * @param parentUrl 父控件查询数据的url
 * @param parentParams 父控件查询数据所需参数
 * @param parentFields 父控件需显示的字段,格式["xx","yy","jj"]
 * @param parentFields 父控件需显示'→'
 * @param parentIsShowAllSelect 父控件是否需要显示全选
 * @param childIds 父控件作用的子控件ID(多个)
 * @param childUrls 子控件查询数据的url,与childIds对应
 * @param childFields 子控件需显示的字段,格式:[["xx","yy"],["ww","zz"]]
 * @param childParamFields 子控件查询数据所需的参数
 * @param childIsShowAllSelectArray 子控件是否需要显示全选
 * @param spaceReplace 将获取子控件的数据参数中的spaceReplace字符替换成""
 * @param showType 显示类型 '0':No,'1':Name,其他:No→Name
 */
wms_city_common.bind4BoxSelect =function(parentId,parentUrl,parentParams,parentFields,parentIsSplit,parentIsShowAllSelect,childIds,childUrls,childFields,childParamFields,childIsShowAllSelectArray,spaceReplace,showType){
	var childLen = childIds.length;
	if(childLen == 0){
		return;
	}
	var temp = null;
	var obj = $("#"+parentId);
	if(parentUrl == null || parentUrl == ''){
		temp = obj.combobox('getData');
	}else{
		wms_city_common.ajaxRequest(BasePath+parentUrl,parentParams,false,function(r){temp = r;});
	}
	var valueName = parentFields[0];
	var textName = parentFields[1];
	var parentCodeName = parentFields[2];
	var showName = 'valueAndText';
	obj.combobox({
		data:temp,
	    valueField:valueName,    
	    textField:showName,
	    panelHeight:150,
	    loadFilter:function(data){
	    	var first = {};
			first[valueName] = '';
			first[showName] = '全选';
			var tempData = [];
			if(parentIsShowAllSelect){
				tempData[tempData.length] = first;
			}
	    	for(var i=0;i<data.length;i++){
	    		if(parentIsSplit){
	    			data[i][showName] = data[i][valueName] + '→' + data[i][textName];
	    		}else{
	    			data[i][showName] = data[i][textName];
	    		}
	    		tempData[tempData.length] = data[i];
	    	}
	    	return tempData;
	    },
		onSelect:function(value){
    		for(var idx=0;idx<childLen;idx++){
    			if(value != null && value != '' && value[valueName] != ''){
    				if(childUrls[idx] != null){
    					url = BasePath + childUrls[idx];
    				}else{
    					url = null;
    				}
    			}else{
    				url = null;
    			}
    			var paramName = 'v';
    			if(childParamFields != null && childParamFields.length > idx && childParamFields[idx] != null && childParamFields[idx] != ''){
    				paramName = childParamFields[idx];
    			}
    			var childP = {};
    			spaceReplace = spaceReplace == null?"":spaceReplace;
    			childP[paramName] = value[parentCodeName];
    			wms_city_common.comboboxLoadFilter(
    					[childIds[idx]],
    					childFields[idx][0],
    					childFields[idx][1],
    					'valueAndText',
    					false,
    					[childIsShowAllSelectArray[idx]],
    					url,
    					childP,
    					null,
    					null,
    					'1',
    					showType
    			);
    		}
		}
	});
};
wms_city_common.cateForCascade = function(firstId,secondId,thirdlyId,isShowAllSelect){
	var url = '/category/getCategory4Simple';
	wms_city_common.bind4BoxSelect(
			firstId,
			url+'?cateLevelid=1',
			{},
			['searchCode','cateName','cateCode'],
			false,
			true,
			[secondId,thirdlyId],
			[url+'?cateLevelid=2',null],
			[['searchCode','cateName'],['searchCode','cateName']],
			['headCateCode','headCateCode'],
			[isShowAllSelect,isShowAllSelect],
			"-",
			'1'
	);
	wms_city_common.bind4BoxSelect(
			secondId,
			null,
			{},
			['searchCode','cateName','cateCode'],
			false,
			true,
			[thirdlyId],
			[url+'?cateLevelid=3',],
			[['searchCode','cateName']],
			['headCateCode'],
			[isShowAllSelect],
			"-",
			'1'
	);
};








/**
 * 为parentId绑定级联select事件
 * @param parentId 父控件ID
 * @param parentUrl 父控件查询数据的url
 * @param parentParams 父控件查询数据所需参数
 * @param parentFields 父控件需显示的字段,格式["xx","yy","jj"]
 * @param parentFields 父控件需显示'→'
 * @param parentIsShowAllSelect 父控件是否需要显示全选
 * @param childIds 父控件作用的子控件ID(多个)
 * @param childUrls 子控件查询数据的url,与childIds对应
 * @param childFields 子控件需显示的字段,格式:[["xx","yy"],["ww","zz"]]
 * @param childParamFields 子控件查询数据所需的参数
 * @param childIsShowAllSelectArray 子控件是否需要显示全选
 * @param spaceReplace 将获取子控件的数据参数中的spaceReplace字符替换成""
 * @param showType 显示类型 '0':No,'1':Name,其他:No→Name
 */
wms_city_common.bind4MultipleBoxSelect =function(parentId,parentUrl,parentParams,parentFields,parentIsSplit,parentIsShowAllSelect,childIds,childUrls,childFields,childParamFields,childIsShowAllSelectArray,spaceReplace,showType){
	var childLen = childIds.length;
	if(childLen == 0){
		return;
	}
	var temp = null;
	var obj = $("#"+parentId);
	if(parentUrl == null || parentUrl == ''){
		temp = obj.combobox('getData');
	}else{
		wms_city_common.ajaxRequest(BasePath+parentUrl,parentParams,false,function(r){temp = r;});
	}
	var valueName = parentFields[0];
	var textName = parentFields[1];
	var parentCodeName = parentFields[2];
	var showName = 'valueAndText';
	
	obj.combobox({
		data:temp,
		multiple:true,
	    valueField:valueName,    
	    textField:showName,
	    panelHeight:150,
	    loadFilter:function(data){
	    	var first = {};
			first[valueName] = '';
			first[showName] = '全选';
			var tempData = [];
			if(parentIsShowAllSelect){
				tempData[tempData.length] = first;
			}
	    	for(var i=0;i<data.length;i++){
	    		if(parentIsSplit){
	    			data[i][showName] = data[i][valueName] + '→' + data[i][textName];
	    		}else{
	    			data[i][showName] = data[i][textName];
	    		}
	    		tempData[tempData.length] = data[i];
	    	}
	    	return tempData;
	    },
		onSelect:function(record){
			var obj = $('#'+this.id);
	    	var values = obj.combobox('getValues');
	    	var value=record[valueName];
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
	    	var boxData=obj.combobox('getData');
	    	var childParams = [];
	    	for(var i=0;i<boxData.length;i++){
	    		for(var j=0;j<newValues.length;j++){
	    			if(boxData[i][valueName]==newValues[j]){
	    				childParams.push(boxData[i][parentCodeName]);	
	    			}
	    		}
	    	}
	    	
    		for(var idx=0;idx<childLen;idx++){
    			var isEventsOff=true;
	    		if(idx==(childLen-1)){
	    			isEventsOff=false;
	    		}
    			
    			var url;
    			if(newValues.length>0 && newValues[0]!=''){
    				if(childUrls[idx] != null){
    					url = BasePath + childUrls[idx];
    				}else{
    					url = null;
    				}
    			}else{
    				url = null;
    			}
    			var paramName = 'v';
    			if(childParamFields != null && childParamFields.length > idx && childParamFields[idx] != null && childParamFields[idx] != ''){
    				paramName = childParamFields[idx];
    			}
    			var childP = {};
    			spaceReplace = spaceReplace == null?"":spaceReplace;
    			childP[paramName] = childParams.join(',');

    			wms_city_common.comboboxLoadFilter4Multiple(
    					[childIds[idx]],
    					childFields[idx][0],
    					childFields[idx][1],
    					'valueAndText',
    					false,
    					[childIsShowAllSelectArray[idx]],
    					url,
    					childP,
    					null,
    					null,
    					showType,
    					isEventsOff
    			);
    		}
		},onUnselect:function(){
	    	var obj = $('#'+this.id);
	    	var newValues = obj.combobox('getValues');	
	    	if(newValues.length==0 && parentIsShowAllSelect){
	    		obj.combobox('setValues',['']);	
	    		newValues=[''];
	    	}
	    	
	    	var childParams = [];
	    	var boxData=obj.combobox('getData');
	    	for(var i=0;i<boxData.length;i++){
	    		for(var j=0;j<newValues.length;j++){
	    			if(boxData[i][valueName]==newValues[j]){
	    				childParams.push(boxData[i][parentCodeName]);	
	    			}
	    		}
	    	}
	    	
	    	for(var idx=0;idx<childLen;idx++){
	    		var isEventsOff=true;
	    		if(idx==(childLen-1)){
	    			isEventsOff=false;
	    		}
	    		
	    		var url;
    			if(newValues.length>0 && newValues[0]!=''){
    				if(childUrls[idx] != null){
    					url = BasePath + childUrls[idx];
    				}else{
    					url = null;
    				}
    			}else{
    				url = null;
    			}
    			var paramName = 'v';
    			if(childParamFields != null && childParamFields.length > idx && childParamFields[idx] != null && childParamFields[idx] != ''){
    				paramName = childParamFields[idx];
    			}
    			var childP = {};
    			spaceReplace = spaceReplace == null?"":spaceReplace;
    			childP[paramName] = childParams.join(',');
    			wms_city_common.comboboxLoadFilter4Multiple(
    					[childIds[idx]],
    					childFields[idx][0],
    					childFields[idx][1],
    					'valueAndText',
    					false,
    					[childIsShowAllSelectArray[idx]],
    					url,
    					childP,
    					null,
    					null,
    					showType,
    					isEventsOff
    			);
    		}
	    }
	});
	
	if(parentIsShowAllSelect){
		obj.combobox('setValues',['']);	
	}
};
wms_city_common.cateForMultipleCascade = function(firstId,secondId,thirdlyId,isShowAllSelect){
	var url = '/category/getCategory4Simple';
	wms_city_common.bind4MultipleBoxSelect(
			firstId,
			url+'?cateLevelid=1',
			{},
			['searchCode','cateName','cateCode'],
			false,
			true,
			[secondId,thirdlyId],
			[url+'?cateLevelid=2',null],
			[['searchCode','cateName'],['searchCode','cateName']],
			['headCateCode','headCateCode'],
			[isShowAllSelect,isShowAllSelect],
			"-",
			'1'
	);
	wms_city_common.bind4MultipleBoxSelect(
			secondId,
			null,
			{},
			['searchCode','cateName','cateCode'],
			false,
			true,
			[thirdlyId],
			[url+'?cateLevelid=3',],
			[['searchCode','cateName']],
			['headCateCode'],
			[isShowAllSelect],
			"-",
			'1'
	);
};
wms_city_common.initLookupBySysNo = function(lookupcode,sysNo,comboboxId,isMultiple,isShowAllCheckArray){
	var data = null;
	var url = BasePath+'/lookupdtl/selectLookupdtlBySysNo?lookupcode='+lookupcode+'&sysNo='+sysNo;
	wms_city_common.ajaxRequest(url,'',false,function(r){data = r;});
	$('#'+comboboxId).combobox({
		multiple:isMultiple,
		data:data,
		valueField:'itemval',    
	    textField:'itemname',
	    loadFilter:function(data){
	    	var tempData = [];
	    	for(var i=0;i<data.length;i++){
	    		data[i]['itemname'] = data[i]['itemval'] + '→' + data[i]['itemname'];
	    		tempData[tempData.length] = data[i];
	    	}
	    	return tempData;
	    }
	});
};

//------------列表小计与合计begin----------------------------
/**
 * 列表小计调用方法
 * 
 * args.columName     需要统计的列名，第一个列名小计合计的属性名(必填)
 * args.documentObj   dataGrid对象(必填)
 * args.data          列表数据源(必填)
 */
wms_city_common.listDataFooter=function(args){
	var columsize=args.columName.length;
	var footerData=[{},{}];
	footerData[0][args.columName[0]]='小计';
	for(var i=1;i<columsize;i++){
		footerData[0][args.columName[i]]=0;
	}
	var data=args.data;
	var list=data.rows;
	var listsize=list.length;
	for(var i=0;i<listsize;i++){
		var obj=list[i];
		for(var n=1;n<columsize;n++){
			footerData[0][args.columName[n]]+=parseFloat(obj[args.columName[n]]+'');
		}
	}
	var foortRows=args.documentObj.datagrid('getFooterRows');
	if(foortRows)footerData[1]=foortRows[1];
	args.documentObj.datagrid('reloadFooter',footerData);
};


/**
 * 列表合计调用方法
 * 
 * args.url           合计接口地址  (选填，默认为与请求列表相同的controller的findSumQty.json方法)
 * args.queryParams   请求接口参数(必填)
 * args.columName     需要统计的列名，第一个列名小计合计的属性名(必填)
 * args.documentObj   dataGrid对象(必填)
 */
wms_city_common.dataSumFooter=function(args){
	var columsize=args.columName.length;
	var footerData=[{},{}];
	footerData[1][args.columName[0]]='合计';
	for(var i=1;i<columsize;i++){
		footerData[1][args.columName[i]]=0;
	}
	var listurl=args.documentObj.datagrid( 'options' ).url;
	var len=listurl.lastIndexOf('/')+1;
	var url=(args.url)?args.url:listurl.substr(0,len)+'findSumQty.json';
	var params=args.queryParams;
	wms_city_common.ajaxRequest(url,params,true,function(data){
		for(var n=1;n<columsize;n++){
			footerData[1][args.columName[n]]+=parseFloat(data[args.columName[n]]+'');
		}
	    var foortRows=args.documentObj.datagrid('getFooterRows');
		if(foortRows)footerData[0]=foortRows[0];
		args.documentObj.datagrid('reloadFooter',footerData);
	});
	
};

//------------列表小计与合计end---------------------------