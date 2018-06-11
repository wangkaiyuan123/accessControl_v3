                                                   <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>申请绑定</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		function goRegister(){
			window.location.href="./weixinpage/userRegister";
		}
		
		function isVistor(){
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
			//alert(mac+"\t"+time);
			window.location.href="${pageContext.request.contextPath}/weixinpage/visitor?mac="+'${mac}'+"&time="+'${time}';
		}
		/*function goVisitor(){
			window.location.href="./weixinpage/visitor";
		}*/
	</script>
  </head>
  
  <body>
    <h2 style="width: 80%;margin-left: 10%;text-align: center;font-size: 40px;font-weight: normal;margin-top: 40px">无权限开门，请点击申请绑定或访客预约</h2> <br>
    <input onclick="goRegister();" type="button" style="height:120px;width: 90%;margin-left: 5%;background-color: rgb(59,181,255);color: rgb(255,255,255);font-size: 45px;border-radius:20px;" value="我要申请"/>
    <br><br>
    <input onclick="isVistor();" type="button" style="height:120px;width: 90%;margin-left: 5%;background-color: rgb(59,181,255);color: rgb(255,255,255);font-size: 45px;border-radius:20px;" value="我是访客"/>
    <br><br><br>
    <img style="width: 60%;margin-left: 10%;" src="${pageContext.request.contextPath}/images/erweima/qrcode.jpg" />
  	<!--<br><br><input onclick="goVisitor();" type="button" style="height:120px;width: 90%;margin-left: 5%;background-color: rgb(26,172,25);
	color: rgb(255,255,255);font-size: 45px;border-radius:20px;" value="我是访客"/>-->
  </body>
</html>
