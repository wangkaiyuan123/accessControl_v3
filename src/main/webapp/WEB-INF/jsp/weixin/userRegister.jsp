<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>我要申请</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/js/jquery.min.js"></script>
	<script type="text/javascript">
		function goRegister(){
			$.ajax({
    			type:'post',
    			url:"/weixin/getCode",
    			data : {
    				"phone" : "18868447608"
    			},
    			dataType : "json",
    			success:function(obj){
    				if(typeof obj=="string"){
        					obj=JSON.parse(obj);
        				}
        				console.log(obj);
        				if(obj.success!=undefined&&obj.success){
        					var code = obj.resultMsg;
        					$("#code").val(code);
        				}
    			}
    		});
		}
	</script>
  </head>
  
  <body>
   <h1 style="text-align: center;">申请页面</h1> <br>
   <input id="code"/>
    <input type="button" style="height:120px;width: 90%;margin-left: 5%;background-color: rgb(59,181,255);color: rgb(255,255,255);
    font-size: 45px;border-radius:20px;" value="获取验证码" onclick="goRegister();"/>
  </body>
</html>
