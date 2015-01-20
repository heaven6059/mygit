var commonValidRule = {};

/**
 * 验证是否是中文
 */
commonValidRule.isChineseChar = function(str) {
	var reg = /^[\u4e00-\u9fa5]+$/gi;
	if (reg.test(str)) {
		return true;
	}
	return false;
};

/**
 * 不能包含中文验证
 */
commonValidRule.vnChinese = {
	vnChinese : {
		validator : function(value, param) {
			for ( var i = 0; i < value.length; i++) {
				if (commonValidRule.isChineseChar(value[i])) {
					return false;
				}
			}
			return true;
		},
		message : '{0}'
	}
};

/**
 * 验证是否是数字或字母
 */
commonValidRule.isCharOrNumber = function(str) {
	var reg = /^[A-Za-z0-9]+$/;
	if (reg.test(str)) {
		return true;
	}
	return false;
};

/**
 * 不能包含数字或字母验证
 */
commonValidRule.vnCharOrNumber = {
	vnCharOrNumber : {
		validator : function(value, param) {
			for ( var i = 0; i < value.length; i++) {
				if (!commonValidRule.isCharOrNumber(value[i])) {
					return false;
				}
			}
			return true;
		},
		message : '{0}'

	}
};

/**
 * 长度验证
 */
commonValidRule.vLength = {
	vLength : {
		validator : function(value, param) {
			var chineseCharLength = param[3] || 1;
			var tempLength = 0;
			for ( var i = 0; i < value.length; i++) {
				if (commonValidRule.isChineseChar(value[i])) {
					tempLength += chineseCharLength;
				} else {
					tempLength += 1;
				}
			}
			if (tempLength < param[0] || tempLength > param[1]) {
				return false;
			}
			return true;
		},
		message : '{2}'
	}
};

/**
 * 数字验证
 */
commonValidRule.vNegativeNum = {
	vNegativeNum : {
		validator : function(value, param) {
			if (value < param[0] || value > param[1]) {
				return false;
			}
			return true;
		},
		message : '{2}'
	}
};

/**
 * 长度验证
 */
commonValidRule.vDate = {
	vDate : {
		validator : function(value, param) {
			var startCreatetm = param[0];
			var endCreatetm = param[1];
			alert(startCreatetm);
			return false;
		},
		message : '{2}'
	}
};

/**
 * 起始时间验证
 */
commonValidRule.vCheckDateRange = {
	vCheckDateRange : {
		validator : function(value, param) {
			var startDate = value;
			var endDate = $(param[0]).datebox('getValue');
			return isStartEndDateCompare(startDate, endDate);
		},
		message : '{1}'
	}
};

$(document).ready(function() {
	$.extend($.fn.validatebox.defaults.rules, commonValidRule.vnCharOrNumber);
	$.extend($.fn.validatebox.defaults.rules, commonValidRule.vnChinese);
	$.extend($.fn.validatebox.defaults.rules, commonValidRule.vLength);
	$.extend($.fn.validatebox.defaults.rules, commonValidRule.vDate);
	$.extend($.fn.validatebox.defaults.rules, commonValidRule.vNegativeNum);
	$.extend($.fn.datebox.defaults.rules, commonValidRule.vCheckDateRange);
});

parseParam = function(param) {
	var paramStr = "";
	{
		$.each(param, function(i) {
			paramStr += '&' + i + '=' + param[i];
		});
	}
	return paramStr.substr(1);
};

/**
 * 基础资料的导出
 * 
 * @param dataGridId
 *            导出数据的表格ID
 * @param exportUrl
 *            导出数据的URL 基础资料一般都是 /模块名/do_export.htm *如客户:/store/do_export.htm
 * @param excelTitle
 *            excel文件名
 */
function exportExcelBaseInfo(dataGridId, exportUrl, excelTitle) {
	var $dg = $("#" + dataGridId + "");
	var paginationObj = $dg.datagrid('getPager').data("pagination");
	if (paginationObj) {
		var tempTotal = paginationObj.options.total;
		if (tempTotal > 10000) {
			alert('导出文件数据超出限制最大数量10000!', 1);
			return;
		}
	}
	var params = $dg.datagrid('options').queryParams;
	var columns = $dg.datagrid('options').columns;

	// 注意是数组里面放数组
	var exportColumnsArr = [ [] ];
	for ( var i = 0; i < columns[0].length; i++) {
		var tempColumn = columns[0][i];
		if (!tempColumn.checkbox && typeof (tempColumn.title) != "undefined") {
			exportColumnsArr[0][exportColumnsArr[0].length] = tempColumn;
		}
	}

	var exportColumns = JSON.stringify(exportColumnsArr);

	// var queryParam=parseParam(params);

	var url = BasePath + exportUrl;

	// if(exportUrl.indexOf('?')>0){
	// url=BasePath+exportUrl+'&'+queryParam;
	// }else{
	// url=BasePath+exportUrl+'?'+queryParam;
	// }

	var dataRow = $dg.datagrid('getRows');

	$("#exportExcelForm").remove();

	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
	;

	var fromObj = $('#exportExcelForm');

	if (dataRow.length > 0) {
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.exportColumns = exportColumns;
				param.fileName = excelTitle;

				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}

			},
			success : function() {

			}
		});
	} else {
		alert('查询记录为空，不能导出!', 1);
	}

}

/**
 * 订单功能的导出
 * 
 * @param dataGridId
 *            表格ID
 * @param sysNo
 *            品牌库的ID
 * @param preColNames
 *            前面显示业务列 公用查询动态生成的参数
 * @param endColNames
 *            后面显示的业务列
 * @param sizeTypeFiledName
 * @param excelTitle
 *            excel文件名
 */
function exportExcelBill(dataGridId, sysNo, preColNames, endColNames,
		sizeTypeFiledName, excelTitle) {

	var url = BasePath + '/initCache/do_export.htm';

	var $dg = $("#" + dataGridId + "");

	var tempTotal = -1;
	var paginationObj = $dg.datagrid('getPager').data("pagination");
	if (paginationObj) {
		tempTotal = paginationObj.options.total;
		if (tempTotal > 10000) {
			alert('导出文件数据超出限制最大数量10000!', 1);
			return;
		}
	}

	$("#exportExcelForm").remove();

	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
	;

	var fromObj = $('#exportExcelForm');

	var dataRow = $dg.datagrid('getRows');

	if (dataRow.length > 0) {
		if (tempTotal > dataRow.length) {
			var dataGridUrl = $dg.datagrid('options').url;
			var dataGridQueryParams = $dg.datagrid('options').queryParams;
			dataGridQueryParams.page = 1;
			dataGridQueryParams.rows = tempTotal;
			$.ajax({
				type : 'POST',
				url : dataGridUrl,
				data : dataGridQueryParams,
				cache : true,
				async : false, // 一定要
				success : function(resultData) {
					dataRow = resultData.rows;
				}
			});
		}
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.sysNo = sysNo;
				param.preColNames = JSON.stringify(preColNames);
				param.endColNames = JSON.stringify(endColNames);
				param.sizeTypeFiledName = sizeTypeFiledName;
				param.fileName = excelTitle;
				param.dataRow = JSON.stringify(dataRow);

			},
			success : function() {

			}
		});
	} else {
		alert('数据为空，不能导出!', 1);
	}

}

// function
// exportExcelBill(dataGridId,sysNo,preColNames,endColNames,sizeTypeFiledName,excelTitle){
//	
// var url=BasePath+'/initCache/do_export.htm';
//	
// var $dg = $("#"+dataGridId+"");
//	
// $("#exportExcelForm").remove();
//	
// $("<form id='exportExcelForm' method='post'></form>").appendTo("body"); ;
//	
// var fromObj=$('#exportExcelForm');
//	
// var dataRow=$dg.datagrid('getRows');
//	
// if(dataRow.length>0){
// fromObj.form('submit', {
// url: url,
// onSubmit: function(param){
//				
// param.sysNo=sysNo;
// param.preColNames=JSON.stringify(preColNames);
// param.endColNames=JSON.stringify(endColNames);
// param.sizeTypeFiledName=sizeTypeFiledName;
// param.fileName=excelTitle;
// param.dataRow=JSON.stringify(dataRow)
// },
// success: function(){
//				
// }
// });
// }else{
// alert('数据为空，不能导出!',1);
// }
//	
//	
// }

function exportExcelRepInfo(dataGridId, exportUrl, excelTitle) {
	var $dg = $("#" + dataGridId + "");
	var params = $dg.datagrid('options').queryParams;
	// var params = $("#searchForm").serializeArray();
	var columns = $dg.datagrid('options').columns;
	var exportColumns = JSON.stringify(columns);
	var url = BasePath + exportUrl;
	var dataRow = $dg.datagrid('getRows');

	$("#exportExcelForm").remove();

	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
	;

	var fromObj = $('#exportExcelForm');

	if (dataRow.length > 0) {
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.exportColumns = exportColumns;
				param.fileName = excelTitle;

				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}

			},
			success : function() {

			}
		});
	} else {
		alert('查询记录为空，不能导出!', 1);

	}

}
/**
 * 下单下单公用方法
 * 
 * @param dataGridId
 * @param rowIndex
 * @param type
 *            1--上单 2--下单
 * @param callBack
 *            回调函数名
 */
function preBill(dataGridId, rowIndex, type, callBack) {
	var $dg = $("#" + dataGridId + "");
	var curRowIndex = rowIndex;

	var options = $dg.datagrid('getPager').data("pagination").options;
	var currPage = options.pageNumber;
	var total = options.total;
	var max = Math.ceil(total / options.pageSize);
	var lastIndex = Math.ceil(total % options.pageSize);
	var pageSize = options.pageSize;
	var rowData = [];
	if (type == 1) {
		if (curRowIndex != 0) {
			curRowIndex = curRowIndex - 1;
			$dg.datagrid('unselectAll');
			$dg.datagrid('selectRow', curRowIndex);
			var rows = $dg.datagrid('getRows');
			if (rows) {
				rowData = rows[curRowIndex];
			}

			callBack(curRowIndex, rowData);
		} else { // 跳转到上一页的最后一行
			if (currPage <= 1) {
				$dg.datagrid('unselectAll');
				$dg.datagrid('selectRow', curRowIndex);
				callBack(curRowIndex, null);
			} else {
				$dg.datagrid('getPager').pagination({
					pageSize : options.pageSize,
					pageNumber : (currPage - 1)
				});
				$dg.datagrid('getPager').pagination('select', currPage - 1);

				curRowIndex = pageSize - 1;
				$dg.datagrid({
					onLoadSuccess : function(data) {
						if (type == 1) {
							$dg.datagrid('unselectAll');
							$dg.datagrid('selectRow', curRowIndex);
							var rows = $dg.datagrid('getRows');
							if (rows) {
								rowData = rows[curRowIndex];
							}
							callBack(curRowIndex, rowData);
						}

					}
				});

			}
		}
	} else if (type == 2) {

		if (curRowIndex != (pageSize - 1)) {
			if (currPage == max && lastIndex != 0
					&& curRowIndex == (lastIndex - 1)) {
				$dg.datagrid('unselectAll');
				$dg.datagrid('selectRow', curRowIndex);
				callBack(curRowIndex, null);
			} else {
				curRowIndex = curRowIndex + 1;
				$dg.datagrid('unselectAll');
				$dg.datagrid('selectRow', curRowIndex);
				var rows = $dg.datagrid('getRows');
				if (rows) {
					rowData = rows[curRowIndex];
				}

				callBack(curRowIndex, rowData);
			}

		} else {

			if (currPage >= max) {
				$dg.datagrid('unselectAll');
				$dg.datagrid('selectRow', curRowIndex);
				callBack(curRowIndex, null);
			} else {
				$dg.datagrid('getPager').pagination({
					pageSize : options.pageSize,
					pageNumber : (currPage + 1)
				});
				$dg.datagrid('getPager').pagination('select', currPage + 1);

				curRowIndex = 0;
				$dg.datagrid({
					onLoadSuccess : function(data) {
						if (type == 2) {

							$dg.datagrid('unselectAll');
							$dg.datagrid('selectRow', curRowIndex);
							var rows = $dg.datagrid('getRows');
							if (rows) {
								rowData = rows[curRowIndex];
							}
							callBack(curRowIndex, rowData);
						}

					}
				});
			}
		}

	}

}

// 验证时间范围
function isStartEndDateCompare(startDate, endDate) {
	if (startDate.length > 0 && endDate.length > 0) {
		var arrStartDate = startDate.split("-");
		var arrEndDate = endDate.split("-");
		var allStartDate = new Date(arrStartDate[0], arrStartDate[1],
				arrStartDate[2]);
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2]);
		if (allStartDate.getTime() >= allEndDate.getTime()) {
			return true;
		}
	}
	return false;
}
/**
 * 返回从今天起n后的日期
 * @param n n大于零为n天后的日期,n小于零为(-n)天前的日期
 * @returns {String}
 */
function getDateStr(n){
	var d = new Date();
	d.setDate(d.getDate()+n);
	var y = d.getFullYear();
	var m = d.getMonth()+1;
	var dd = d.getDate();
	
	var rs = y + "-";
	if(m<10){
		rs += "0"+m;
	}else{
		rs += m;
	}
	rs += "-";
	if(dd<10){
		rs += "0"+dd;
	}else{
		rs += dd;
	}
	return rs;
}
function getLodop(oOBJECT, oEMBED) {
	/***************************************************************************
	 * 本函数根据浏览器类型决定采用哪个对象作为控件实例： IE系列、IE内核系列的浏览器采用oOBJECT，
	 * 其它浏览器(Firefox系列、Chrome系列、Opera系列、Safari系列等)采用oEMBED,
	 * 对于64位浏览器指向64位的安装程序install_lodop64.exe。
	 **************************************************************************/
	var strHtmInstall = "打印控件未安装!安装install_lodop64.exe后请刷新页面或重新进入"
			+ "<br><br>"
			+ "<a href='../lodop/install_lodop32.exe' target='_blank' style='color:#0f0;text-decoration: underline;'>点击下载</a>";
	var strHtmUpdate = "install_lodop32.exe打印控件需要升级!升级后请重新进入。"
			+ "<br><br>"
			+ "<a href='../lodop/install_lodop32.exe' target='_blank' style='color:#0f0;text-decoration: underline;'>点击下载</a>";
	var strHtm64_Install = "打印控件未安装!安装install_lodop64.exe后请刷新页面或重新进入。"
			+ "<br><br>"
			+ "<a href='../lodop/install_lodop32.exe' target='_blank' style='color:#0f0;text-decoration: underline;'>点击下载</a>";
	var strHtm64_Update = "install_lodop64.exe打印控件需要升级!升级后请重新进入。"
			+ "<br><br>"
			+ "<a href='../lodop/install_lodop32.exe' target='_blank' style='color:#0f0;text-decoration: underline;'>点击下载</a>";
	var strHtmFireFox = "注意：1：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它。"
			+ "<br><br>"
			+ "<a href='../lodop/install_lodop32.exe' target='_blank' style='color:#0f0;text-decoration: underline;'>点击下载</a>";
	var LODOP = oEMBED;
	var isIE = (navigator.userAgent.indexOf('MSIE') >= 0)
			|| (navigator.userAgent.indexOf('Trident') >= 0);
	var is64IE = isIE && (navigator.userAgent.indexOf('x64') >= 0);
	try {
		if (isIE)
			LODOP = oOBJECT;
		if ((LODOP == null) || (typeof (LODOP.VERSION) == "undefined")) {
			if (navigator.userAgent.indexOf('Firefox') >= 0) {
				// document.documentElement.innerHTML=strHtmFireFox+document.documentElement.innerHTML;
				alert(strHtmFireFox);
				return null;
			}
			;
			if (is64IE) {
				// document.write(strHtm64_Install);
				alert(strHtm64_Install);
				return null;
			} else if (isIE) {
				// document.write(strHtmInstall);
				alert(strHtmInstall);
				return null;
			} else {
				// document.documentElement.innerHTML=strHtmInstall+document.documentElement.innerHTML;
				alert(strHtmInstall);
				return null;
			}
			;
			//return LODOP;
		} else if (LODOP.VERSION < "6.1.0.9") {
			if (is64IE) {
				// document.write(strHtm64_Update);
				alert(strHtm64_Update);
				return null;
			} else if (isIE) {
				// document.write(strHtmUpdate);
				alert(strHtmUpdate);
				return null;
			} else {
				// document.documentElement.innerHTML=strHtmUpdate+document.documentElement.innerHTML;
				alert(strHtmUpdate);
				return null;
			}
			//return LODOP;
		}
		// =====如下空白位置适合调用统一功能:=====

		// =======================================
		//return LODOP;
	} catch (err) {
		if (is64IE)
		// document.documentElement.innerHTML="Error:"+strHtm64_Install+document.documentElement.innerHTML;
		{
			alert(strHtm64_Install);
			return null;
		} else
		// document.documentElement.innerHTML="Error:"+strHtmInstall+document.documentElement.innerHTML;
		{
			alert(strHtmInstall);
			return null;
		}

	}
	LODOP.SET_LICENSES("新百丽鞋业(深圳)有限公司", "89BC87DC44E17DE99EB3CDC37A83CAE9",
			"新百麗鞋業(深圳)有限公司", "4FE00A78ABD2F89234B82E25ECCA0596");
	LODOP.SET_LICENSES("THIRD LICENSE", "",
			"New Belle footwear (Shenzhen) Co., Ltd.",
			"30252C904E5D65B871E2034C81EEE7A2");
	return LODOP;
};


var CommonLookupdtl={
		loadType:0,	//加载方式：0 js缓存，1后台接口
		userbrand:null,//用户拥有的品牌
		sysnolist:null,//品牌
		
		/**
		 * 系统字典加载
		 * @author wugyp
		 * @param lookupcode
		 */
		getLookupDtlsList:function(lookupcode){
			//0--/resources/js/lookupdtl/lookupcode_CITY_BILL_CH_CHECK_STATUS.js
			//1--/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_STATUS
			var f="";
			if(CommonLookupdtl.loadType==0){
				f="/resources/js/lookupdtl/lookupcode_"+lookupcode+".js";
			}
			else{
				f="/initCache/getLookupDtlsList.htm?lookupcode="+lookupcode;
			}
			return f;
		},
		
		/**
		 * 系统品牌
		 * @param lookupDtlsList
		 * @returns {Array}
		 */
		sysNofilter:function(lookupDtlsList){
			if(CommonLookupdtl.userbrand==null){
				CommonLookupdtl.getLookupDtlsUserBrandList();
			}
			var arr=[];
			var k=0;
			var userbrandData=CommonLookupdtl.userbrand;
			for(var i=0;i<userbrandData.length;i++){
				for(var j;j<lookupDtlsList.length;j++){
					//品牌及系统id相同的
					if(userbrandData[i].sysNo==lookupDtlsList[j].itemvalue&& userbrandData[i].systemId==lookupDtlsList[j].systemid){
						arr[k++]=userbrandData[i];
						break;
					}
				}
			}
			return arr;
		},
		
		/**
		 * 返回用户拥有的品牌
		 * @param lookupcode
		 * @returns {String}
		 */
		getLookupDtlsUserBrandList:function(){
			$.ajax({
				async : false,
				type : 'GET',
				dataType : "json",
				url:BasePath+'/initjsCache/getLookupDtlsUserBrandList',
				success : function(data) {
					CommonLookupdtl.userbrand=data;
				}
			});
		},
		
		/**
		 * 刷新字典JS缓存
		 * @param lookupcode
		 * @returns {String}
		 */
		reflushLookupDtlsList:function(lookupcode){
			$.ajax({
				async : true,
				cache : false,
				type : 'GET',
				dataType : "json",
				url:BasePath+'/initjsCache/createInitLookupdMapJsFile?lookupcode=',
				success : function(data) {
					return data;
				}
			});
		}
		
};
