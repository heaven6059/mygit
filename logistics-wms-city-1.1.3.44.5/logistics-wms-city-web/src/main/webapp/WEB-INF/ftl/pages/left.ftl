<script type="text/javascript" src="<@s.url "/resources/common/js/jquery.easyui.min.js"/>" ></script>
<script type="text/javascript"  src="<@s.url "/resources/js/tree.js"/>" ></script>
<script type="text/javascript" src="<@s.url "/resources/js/resources.js"/>"></script>
<style>
<!--
* {
    margin: 0;
    padding: 0;
}
body {
    font-size: 12px;
}
ul {
    list-style: none outside none;
}
ul li .tree-node {
    background: url("resources/images/barbg.gif") repeat-x scroll center top transparent;
    border-bottom: 1px solid #E6E6E6;
    border-top: 1px solid #E6E6E6;
    color: #2B2B2B;
    display: block;
    font-size: 14px;
    font-weight: bold;
    line-height: 28px;
    margin: 0 1px 3px;
    padding-left: 15px;
    width: 134px;
}
ul li ul li .tree-node {
    background: none repeat scroll 0 0 transparent;
    border: medium none;
    color: #0066FF;
    font-size: 12px;
    font-weight: normal;
    height: 22px;
    line-height: 22px;
    margin-left: 5px;
    text-align: left;
}
ul li ul li .tree-node-selected {
    background: url("resources/images/leftbarnavhover.gif") no-repeat scroll 0 0 transparent;
    color: #FFFFFF;
    line-height: 22px;
    text-align: left;
    width: 130px;
}
ul li ul li .tree-node-selected span {
    color: #FFFFFF;
}
ul li ul li .tree-node-hover {
    background: url("resources/images/leftbarnavhover.gif") no-repeat scroll 0 0 transparent;
    color: #FFFFFF;
    font-size: 12px;
    font-weight: normal;
    height: 22px;
    line-height: 22px;
    text-align: left;
    width: 130px;
}
ul li ul li .tree-node-hover span {
    color: #FFFFFF;
}
ul li ul li ul {
    padding-left: 8px;
}
#borderDiv {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    border-color: -moz-use-text-color #E6E6E6 #E6E6E6 -moz-use-text-color;
    border-image: none;
    border-right: 1px solid #E6E6E6;
    border-style: none solid solid none;
    border-width: medium 1px 1px medium;
    height: 500px;
    width: 142px;
}
-->
</style>
<body>
<div class="borderDiv">
<ul id="resourceTree" class="tree">
<script>leftControllerMenu('leftMenuTree/queryMenuTree.htm');</script>
</ul>
</div>
</body>
