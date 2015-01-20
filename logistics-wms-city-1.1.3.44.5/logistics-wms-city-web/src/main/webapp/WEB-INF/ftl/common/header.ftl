<script type="text/javascript"  src="${domainStatic}/boot.js?version=${resourceVersion}" ></script>
<script type="text/javascript" src="${domainStatic}/resources/js/common/easyui_dataGrid.js?version=${resourceVersion}"/></script>
<script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=${resourceVersion}"/></script>
<script type="text/javascript" src="${domainStatic}/resources/js/common/wms_city_common.js?version=1.5.0.3"></script>
<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/city-common.css?version=1.5.0.0"/>
<!--界面上直接用   ${basePath}  -->
<#assign BasePath = springMacroRequestContext.getContextPath()/>

<script>
	   var BasePath = '${springMacroRequestContext.getContextPath()}';
	    
	   function closeWindow(menuName){
		    var tab = parent.$('#mainTabs').tabs('getSelected');
		    var index = parent.$('#mainTabs').tabs('getTabIndex',tab);
		    parent.$('#mainTabs').tabs('close',index);
	   }

       
       // 公用弹出框
       function alert(msg,type){
         //info-0,warning-1,error-2,question-3 ,success-4 
          var typeStr="info";
          if(type==1){
              typeStr='warning';
          }else if(type==2){
              typeStr='error';
          }else if(type==3){
              typeStr='question';
          }else if(type==4){
              typeStr='success';
          }else{
              typeStr='info';
          }
          $.messager.alert('提示框',msg,typeStr); 
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
	    
	     function checkPowerJS(powerValue,index){
               var flag=false;
               var  temp =parseInt(Math.pow(2,index));
                 var result = powerValue&temp;
                   if(result== temp){
                        flag=true;
                   }
               return flag;
        }
        
        /**
		 * 禁用按钮(toolbar除外)
		 */
		function disableLinkButton(id){
			var obj = $("#"+id);
			var cls = obj.attr('class');
			if(cls.indexOf('disable')<0){
				obj.linkbutton('disable');
			}
		};
		/**
		 * 重用按钮(toolbar除外)
		 */
		function enableLinkButton(id){
			var obj = $("#"+id);
			var cls = obj.attr('class');
			if(cls.indexOf('disable')>=0){
				obj.linkbutton('enable');
			}
		};
	   
	   
	   $.extend($.fn.linkbutton.methods, {
	    /**
	     * 激活选项（覆盖重写）
	     * @param {Object} jq
	     */
	    enable: function(jq){
	        return jq.each(function(){
	            var state = $.data(this, 'linkbutton');
	            if ($(this).hasClass('l-btn-disabled')) {
	                var itemData = state._eventsStore;
	                //恢复超链接
	                if (itemData.href) {
	                    $(this).attr("href", itemData.href);
	                }
	                //回复点击事件
	                if (itemData.onclicks) {
	                    for (var j = 0; j < itemData.onclicks.length; j++) {
	                        $(this).bind('click', itemData.onclicks[j]);
	                    }
	                }
	                //设置target为null，清空存储的事件处理程序
	                itemData.target = null;
	                itemData.onclicks = [];
	                $(this).removeClass('l-btn-disabled');
	            }
	        });
	    },
	    /**
	     * 禁用选项（覆盖重写）
	     * @param {Object} jq
	     */
	    disable: function(jq){
	        return jq.each(function(){
	            var state = $.data(this, 'linkbutton');
	            
	            if (!state._eventsStore) 
	                state._eventsStore = {};
	            if (!$(this).hasClass('l-btn-disabled')) {
	                var eventsStore = {};
	                eventsStore.target = this;
	                eventsStore.onclicks = [];
	                //处理超链接
	                var strHref = $(this).attr("href");
	                if (strHref) {
	                    eventsStore.href = strHref;
	                    $(this).attr("href", "javascript:void(0)");
	                }
	                //处理直接耦合绑定到onclick属性上的事件
	                var onclickStr = $(this).attr("onclick");
	                if (onclickStr && onclickStr != "") {
	                    eventsStore.onclicks[eventsStore.onclicks.length] = new Function(onclickStr);
	                    $(this).attr("onclick", "");
	                }
	                //处理使用jquery绑定的事件
	                var eventDatas = $(this).data("events") || $._data(this, 'events');
	                if (eventDatas["click"]) {
	                    var eventData = eventDatas["click"];
	                    for (var i = 0; i < eventData.length; i++) {
	                        if (eventData[i].namespace != "menu") {
	                            eventsStore.onclicks[eventsStore.onclicks.length] = eventData[i]["handler"];
	                            $(this).unbind('click', eventData[i]["handler"]);
	                            i--;
	                        }
	                    }
	                }
	                state._eventsStore = eventsStore;
	                $(this).addClass('l-btn-disabled');
	            }
	        });
	    }
	});
	   
</script>