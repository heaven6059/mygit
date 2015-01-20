<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include  "/WEB-INF/ftl/common/header.ftl" >
<link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/easyui.css"/>" />
<link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/icon.css" />" />
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript">  
       $(function(){  
           $('#tt1').tree({  
               animate:true,  
               onClick:function(node){  
                   alert('you click '+node.text);  
               }  
           });  
           $('#tt2').tree({  
               checkbox: true,
               //url:'js/loadResourceDate.js',  
               url: 'queryMenuTree.htm',  
               onClick:function(node){  
                   alert('you click '+node.text);  
               }  
           });  
       });  
       function reload(){  
           $('#tt2').tree('reload');  
       }  
       function getChildNodes(){  
           var node = $('#tt2').tree('getSelected');  
           if (node){  
               var children = $('#tt2').tree('getChildNodes', node.target);  
               var s = '';  
               for(var i=0; i<children.length; i++){  
                   s += children[i].text + ',';  
               }  
               alert(s);  
           }  
       }  
       function getChecked(){  
           var nodes = $('#tt2').tree('getChecked');  
           var s = '';  
           for(var i=0; i<nodes.length; i++){  
               if (s != '') s += ',';  
               s += nodes[i].text;  
           }  
           alert(s);  
       }  
       function getSelected(){  
           var node = $('#tt2').tree('getSelected');  
           alert(node.text);  
       }  
       function collapse(){  
           var node = $('#tt2').tree('getSelected');  
           $('#tt2').tree('collapse',node.target);  
       }  
       function expand(){  
           var node = $('#tt2').tree('getSelected');  
           $('#tt2').tree('expand',node.target);  
       }  
       function collapseAll(){  
           $('#tt2').tree('collapseAll');  
       }  
       function expandAll(){  
           $('#tt2').tree('expandAll');  
       }  
       function append(){  
           var node = $('#tt2').tree('getSelected');  
           $('#tt2').tree('append',{  
               parent: node.target,  
               data:[{  
                   text:'new1',  
                   checked:true  
               },{  
                   text:'new2',  
                   state:'closed',  
                   children:[{  
                       text:'subnew1'  
                   },{  
                       text:'subnew2'  
                   }]  
               }]  
           });  
       }  
       function remove(){  
           var node = $('#tt2').tree('getSelected');  
           $('#tt2').tree('remove', node.target);  
       }  
       function update(){  
           var node = $('#tt2').tree('getSelected');  
           if (node){  
               node.text = '<span style="font-weight:bold">new text</span>';  
               node.iconCls = 'icon-save';  
               $('#tt2').tree('update', node);  
           }  
       }  
       function isLeaf(){  
           var node = $('#tt2').tree('getSelected');  
           var b = $('#tt2').tree('isLeaf', node.target);  
            alert(b)  
        }  
    </script>  
</head>  
<body>  
    <h1>Tree</h1>  
    <p>Create from HTML markup</p>  
    <ul id="tt1">  
        <li>  
            <span>Folder</span>  
            <ul>  
                <li>  
                    <span>Sub Folder 1</span>  
                    <ul>  
                        <li>  
                            <span><a href="#">File 11</a></span>  
                        </li>  
                        <li>  
                            <span>File 12</span>  
                        </li>  
                        <li>  
                            <span>File 13</span>  
                        </li>  
                    </ul>  
                </li>  
                <li>  
                    <span>File 2</span>  
                </li>  
                <li>  
                    <span>File 3</span>  
                </li>  
                <li>File 4</li>  
                <li>File 5</li>  
            </ul>  
        </li>  
        <li>  
            <span>File21</span>  
        </li>  
    </ul>  
    <hr></hr>  
    <p>Create from JSON data</p>  
    <div style="margin:10px;">  
        <a href="#" onclick="reload()">reload</a>  
        <a href="#" onclick="getChildNodes()">getChildNodes</a>  
        <a href="#" onclick="getChecked()">getChecked</a>  
        <a href="#" onclick="getSelected()">getSelected</a>  
        <a href="#" onclick="collapse()">collapse</a>  
        <a href="#" onclick="expand()">expand</a>  
        <a href="#" onclick="collapseAll()">collapseAll</a>  
        <a href="#" onclick="expandAll()">expandAll</a>  
        <a href="#" onclick="append()">append</a>  
        <a href="#" onclick="remove()">remove</a>  
        <a href="#" onclick="update()">update</a>  
        <a href="#" onclick="isLeaf()">isLeaf</a>  
    </div>  
  
    <ul id="tt2"></ul>  
</body>  
</html>