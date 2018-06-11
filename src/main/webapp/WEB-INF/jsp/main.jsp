<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/view/viewer.min.css"/>
	</head>
 	<body class="layui-layout-body">
		<div class="layui-layout layui-layout-admin">
        	<div class="layui-header">
            	<div class="layui-logo">社区居民信息采集应用平台</div>
            	<!--右侧-->
            	<ul class="layui-nav layui-layout-right">
                	<li class="layui-nav-item">
                    	<a href="javascript:;">
                        	<!--<img src="http://t.cn/RCzsdCq" class="layui-nav-img">  -->
                        	<i class="layui-icon userImg">&#xe612;</i>
                        	<span>${loginUser.realName}</span>
                    	</a>
                    	<dl class="layui-nav-child">
                        	<dd><a href="javascript:;" id="changePwd">修改密码</a></dd>
                    	</dl>
                	</li>
                	<li class="layui-nav-item" id="exit"><a href="javascript:void(0);">退出登录</a></li>
            	</ul>
        	</div>
        	<div class="layui-side layui-bg-black">
            	<div class="layui-side-scroll">
                	<ul class="layui-nav layui-nav-tree leftTree" lay-filter="test">
                	</ul>
                	<i class="changeSize layui-icon foldNav">&#xe65a;</i>
            	</div>
        	</div>
        	<div class="layui-body">
            	<!-- 内容主体区域 -->
            	<div style="height:100%;">
                	<div class="iframeContainer">
                    	<iframe src="" width="100%" height="100%" frameborder="0" scrolling="no" id="mainIframe"></iframe>
                	</div>
            	</div>
        	</div>
        	<div class="layui-footer">
            		东华宏泰提供技术支持
        	</div>
        	<input type="hidden" value="" id="changeIframeTitle"/>
    	</div>
    	<div class="modalShadow"></div>
    	<div class="modalShadowSec"></div>
    	<!--存储选择的微信用户  -->
    	<input id="radioWeiXin" type="hidden" data-name="" val="" data-number=""/>
    	<!-- 存储修改所需字段 -->
    	<input type="hidden" id="editJson" value=''/>
    	<!--存储图标  -->
    	<input type="hidden" id="iconClass" value=''/>
    	<!--预览图片  -->
    	<!--<ul id="jq22" style="position:absolute;z-index:1000;visibility:hidden;">
        	<li><img  src="${pageContext.request.contextPath}/img/thumbnails/tibet-1.jpg" alt="图片"></li>
    	</ul>  -->
    	<script type="text/javascript" src="${pageContext.request.contextPath}/js/main.js"></script>
    	<script src="${pageContext.request.contextPath}/framework/view/viewer-jquery.min.js"></script>
    	<script type="text/javascript">
    		loadLeftSide();
    		//退出系统：
    		$("#exit").click(function(){
    			$(this).createModal({"title":"退出","modalHeight":150,"modalWidth":330,"url":contextPath+"/logout"});
    		});
    		//修改密码：
    		$("#changePwd").click(function(){
    			$(this).createModal({"title":"修改密码","modalHeight":280,"modalWidth":500,"url":contextPath+"/password/changePwd"});
    		});
    	</script>
	</body>
</html>
