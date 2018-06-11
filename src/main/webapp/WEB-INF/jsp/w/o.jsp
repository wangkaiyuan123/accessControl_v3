<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function goRegister(){
			var mac = '${mac}';
			if(mac==''||mac=="null"){
				alert("二维码错误,请重新扫码");
				return;
			}
			var time = '${time}';
			if(time==''||time=="null"){
				alert("二维码错误,请重新扫码");
				return;
			}
			window.location.href="${pageContext.request.contextPath}/weixinpage/visitor?mac="+'${mac}'+"&time="+'${time}';
		}
		/*定时任务：动态获取开门结果*/
		$().ready(function(){
			window.setInterval(searchResult,400);
		});
		var n = 0;
		function searchResult(){
			var uuid = '${uuid}';
			if(uuid==''||uuid=="null"){
				return;
			}
			if(n==0){
				$.ajax({
					type:'post',
					url:"${pageContext.request.contextPath}/w/searchResult",
					data:{"searchUuid":uuid},
					success:function(data1){
						if(typeof data1=="string"){
							data1=JSON.parse(data1);
						}
						if(data1.success==true){
							n=1;
							$("#message").html(data1.resultMsg);
						}
					}
				});
			}
		}
	</script>
  </head>
  
    <body>
    <h2 style="width: 80%;margin-left: 10%;text-align: center;font-size: 40px;font-weight: normal;margin-top: 40px" id="message">${pageMessage}</h2> <br><br><br><br><br><br><br><br>
    <img style="width: 80%;margin-left: 10%;" src="${pageContext.request.contextPath}/images/erweima/qrcode.jpg" />
    <br><br>
    <input onclick="goRegister();" type="button" style="height:120px;width: 90%;margin-left: 5%;background-color: rgb(26,172,25);
	color: rgb(255,255,255);font-size: 45px;border-radius:20px;" value="我是访客"/>
	
  </body>
</html>
