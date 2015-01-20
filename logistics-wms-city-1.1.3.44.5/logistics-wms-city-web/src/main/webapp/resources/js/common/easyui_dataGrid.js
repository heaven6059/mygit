    
     

     // 添加
     function addDataGridCommon(dataGridId){
    	 var $dg = $("#"+dataGridId+"");
    	 $dg.datagrid('appendRow', {});
          var rows = $dg.datagrid('getRows');
          $dg.datagrid('beginEdit', rows.length - 1);
          $dg.datagrid('selectRow', rows.length - 1);
          
     }
     
     // 删除
     function removeDataGridCommon(dataGridId){
    	   var $dg = $("#"+dataGridId+"");
           var row = $dg.datagrid('getSelected');
            if (row) {
                   var rowIndex = $dg.datagrid('getRowIndex', row);
                   $dg.datagrid('deleteRow', rowIndex);
                   if((rowIndex-1)>=0){
                	   $dg.datagrid('selectRow', rowIndex-1);
                   }
           }
     }
     
     // 删除所有行
     function deleteAllGridCommon(dataGridId){
  	      var $dg = $("#"+dataGridId+"");
	  	   var rows = $dg.datagrid('getRows');
	  	   if(rows){
	  	  	  for ( var i = 0,length=rows.length; i < length; i++) {
	  	  		var rowIndex = $dg.datagrid('getRowIndex', rows[i]);
                $dg.datagrid('deleteRow', rowIndex);
		  	  }
	  	   }
	  	 $('#'+dataGridId).datagrid('loadData', { total: 0, rows: [] });
     }
     
	    
  	//获得当前行号   一般用 var rowIndex=getRowIndex(this);
  	function getRowIndex(target) {
        var tr = $(target).closest('tr.datagrid-row');
        return parseInt(tr.attr('datagrid-row-index'));
      }  
     
  
     // 结束编辑 
     function endEditCommon(dataGridId){
    	    var $dg = $("#"+dataGridId+"");
            var rows = $dg.datagrid('getRows');
            for ( var i = 0,length=rows.length; i < length; i++) {
            	$dg.datagrid('endEdit', i);
            }
     }
     
    // 获取该表格有变动的记录 inserted\deleted\updated
     function getChangeTableDataCommon(dataGridId){
    	 var $dg = $("#"+dataGridId+"");
    	 endEditCommon(dataGridId);
    	 var effectRow = new Object();
    	 if($dg.datagrid('getChanges').length) {
    		  var inserted = $dg.datagrid('getChanges', "inserted");
              var deleted = $dg.datagrid('getChanges', "deleted");
              var updated = $dg.datagrid('getChanges', "updated");
              
              
              if (inserted.length) {
                  effectRow["inserted"] = JSON.stringify(inserted);
              }
              
              if (deleted.length) {
                  effectRow["deleted"] = JSON.stringify(deleted);
              }
              
              if (updated.length) {
                  effectRow["updated"] = JSON.stringify(updated);
              }
    	 }
    	
    	 return effectRow;
     }
     
     // 全选或者全不选   checkstatus 1--全选   0--全不选
     function selectCheckAllRowCommon(dataGridId,checkstatus){
          var rows = $('#'+dataGridId).datagrid('getRows');
          for ( var i = 0; i < rows.length; i++) {
              if(checkstatus==0){
                 $('#'+dataGridId).datagrid('uncheckRow', i);
              }else{
                 $('#'+dataGridId).datagrid('checkRow', i);
              } 
          }
     }
     
	    
  // 返回前一个页面
     function returnTab(tabID,title){
        $('#'+tabID).tabs('select',title);
     }
  

    
     //发达ajax请求
    function ajaxRequest(url,reqParam,callback){
		$.ajax({
			  type: 'POST',
			  url: url,
			  data: reqParam,
			  cache: true,
			  success: callback
		});
    }
    
    //发达ajax请求
    function ajaxRequestAsync(url,reqParam,callback){
		$.ajax({
			  type: 'POST',
			  url: url,
			  data: reqParam,
			  cache: true,
			  async: false,
			  success: callback
		});
    }
   
   //截取时间格式yyyy-MM-dd
  function formatDate(value) {
	  if (value == null || value == '') {
          return '';
      }
	  if(value.length > 10){
		  return value.substring(0,10);
	  }
      return value;  
  }  
    
  //有关日期的控件处理
    function formatDatebox(value) {
        if (value == null || value == '') {
            return '';
        }
        var dt;
	    if (value instanceof Date) {
	        dt = value;
	    } else {
	        dt = new Date(value);
	        if (isNaN(dt)) {
	            value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); 
	            dt = new Date();
	            dt.setTime(value);
	        }
	    }
       return dt.format("yyyy-MM-dd");  
   }  
  
    $.extend(
    	    $.fn.datagrid.defaults.editors, {
    	        datebox: {
    	            init: function (container, options) {
    	                var input = $('<input type="text">').appendTo(container);
    	                input.datebox(options);
    	                return input;
    	            },
    	            destroy: function (target) {
    	                $(target).datebox('destroy');
    	            },
    	            getValue: function (target) {
    	                return $(target).datebox('getValue');
    	            },
    	            setValue: function (target, value) {
    	                $(target).datebox('setValue', formatDatebox(value));
    	            },
    	            resize: function (target, width) {
    	                $(target).datebox('resize', width);
    	            }
    	        }
    	    });
    	Date.prototype.format = function (format) 
    	{
    	    var o = {
    	        "M+": this.getMonth() + 1, //month 
    	        "d+": this.getDate(),    //day 
    	        "h+": this.getHours(),   //hour 
    	        "m+": this.getMinutes(), //minute 
    	        "s+": this.getSeconds(), //second 
    	        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter 
    	        "S": this.getMilliseconds() //millisecond 
    	    }
    	    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
    	    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    	    for (var k in o) if (new RegExp("(" + k + ")").test(format))
    	        format = format.replace(RegExp.$1,
    	      RegExp.$1.length == 1 ? o[k] :
    	        ("00" + o[k]).substr(("" + o[k]).length));
    	    return format;
    	}
    
