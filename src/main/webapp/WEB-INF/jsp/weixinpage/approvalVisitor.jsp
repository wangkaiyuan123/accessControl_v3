<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>开门申请</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript">
		var token = '${token}';
		function goRegister1(){
			if(token==''||token=="null"){
				alert("参数错误！");
			}
			$.ajax({
					type:'post',
					url:"${pageContext.request.contextPath}/weixinpage/doApproval",
					//cache:false,
					data:{"approvalToken":token,"approvalFlag":"1"},
					success:function(data1){
						if(typeof data1=="string"){
							data1=JSON.parse(data1);
						}
						alert(data1.resultMsg);
					}
				});
		}
		
		function goRegister2(){
			if(token==''||token=="null"){
				alert("参数错误");
			}
			$.ajax({
					type:'post',
					url:"${pageContext.request.contextPath}/weixinpage/doApproval",
					//cache:false,
					data:{"approvalToken":token,"approvalFlag":"2"},
					success:function(data1){
						if(typeof data1=="string"){
							data1=JSON.parse(data1);
						}
						alert(data1.resultMsg);
					}
				});
		}
	</script>
  </head>
  
  <body>
    <h2 style="width: 80%;margin-left: 10%;text-align: center;font-size: 40px;font-weight: normal;margin-top: 40px">您的好友正在申请开门，请审批</h2>
    <input onclick="goRegister1();" type="button" style="height:120px;width: 30%;margin-left: 18%;background-color: rgb(26,172,25);color: rgb(255,255,255);font-size: 45px;border-radius:20px;" value="同意"/>
  	<input onclick="goRegister2();" type="button" style="height:120px;width: 30%;margin-left: 4%;background-color: rgb(240,72,6);color: rgb(255,255,255);font-size: 45px;border-radius:20px;" value="拒绝"/>
  </body>
</html>
