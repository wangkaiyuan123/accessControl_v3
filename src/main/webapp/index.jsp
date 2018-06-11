<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
 		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/app.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/hm.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/particles.js"></script>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    	<title>东华宏泰杭州路佳</title>
   	</head>
  	<body>
    	<div id="particles-js">
    		<canvas width="1920" height="950" style="width: 100%; height: 100%;"></canvas>
    	</div>
    	<form  id="loginForm"><!--  action="${pageContext.request.contextPath}/checkLogin" method="post"-->
			<dl class="admin_login">
				<dt>
				  	<strong class="animated  jackInTheBox ">社区居民信息采集应用平台</strong>
				  	<em>Management System </em>
				</dt>
			 	<dd class="user_icon">
			 		<i class='layui-icon'>&#xe612;</i>
			  		<input name="UserName" type="text" placeholder="用户名" class="login_txtbx">
			 	</dd>
			 	<dd class="pwd_icon">
			 		<i class='icon-lock' style="left:6px;font-size:2.1em"></i>
			 		<input name="Password" type="password" placeholder="密码" class="login_txtbx">
			 	</dd>
			 	<dd>
			  		<input type="button" value="立即登陆" class="submit_btn">
			 	</dd>
			</dl>
		</form>
		<script type="text/javascript">
			layui.use('layer',function(){
				$(".submit_btn").click(function(){
					var data=$("#loginForm").serialize();
					$.ajax({
						type:'post',
						url:contextPath+"/checkLogin",
						data:data,
						success:function(data){
							if(typeof data == "string"){
								data=JSON.parse(data);
							}
							if(data.success==true){
								window.location.href=contextPath+"/main";
							}else{
								var message=data.resultMsg;
								layer.msg(message);
							}
						}	
					});
				});
				$("body").keydown(function(event) {
    				if (event.keyCode == "13") {//keyCode=13是回车键
        				$(".submit_btn").click();
    				}
				});    
			});
		</script>
  </body>
</html>
