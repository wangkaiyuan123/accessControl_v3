<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<title>运维人员账号登录</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/lib/weui.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/css/jquery-weui.min.css"/> 
  		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  		<style type="text/css">
  			body{
  				background-image: url("${pageContext.request.contextPath}/images/loginbg3.png");
  				background-repeat: no-repeat;
  			}
  		</style>
  	<script type="text/javascript">
  		
  		$(function(){
  			alert("请先登录运维账号！");
  		});
  		
  	</script>
  	
  	</head>
  
  <body>
  
  		<form id="pageForm" method="post" action="${pageContext.request.contextPath}/weixinpage/yunweiPage }">
  			<div class="weui-cells weui-cells_form" style="margin-bottom:25px;">
  				<div class="weui-cell">
  					<div class="weui-cell_hd">
  						<label class="weui-label"><span class="label_red">*</span>用户名:</label>
  					</div>
  					<div class="weui-cell_bd">
  						<input class="weui-input" type="text" name="userName" id="userName" placeholder="请输入您的用户名">
  					</div>
  				</div>
  				
  				<div class="weui-cell">
  					<div class="weui-cell_hd">
  						<label class="weui-label"><span class="label_red">*</span>密码:</label>
  					</div>
  					<div class="weui-cell_bd">
  						<input class="weui-input" type="text" name="password" id="password" placeholder="请输入您的密码">
  					</div>
  				</div>
  			</div>	
  		</form>
  		<div style="text-align:center;color:#e4393c;margin-bottom:15px;">
  			<span class="label_red">* </span>以上信息请填写完再提交
  		</div>
  		<a href="javascript:;" class="weui-btn weui-btn_primary" id="submitButton" style="width: 94%;margin-left: 3%;margin-right: 3%;">登录</a>	<br><br>
  		
  		<script type="text/javascript">
  			//提交
  			var clickNum=0;
  			$("#submitButton").click(function(){
  				
	  				var userName = $("#userName").val();
	  				var password = $("#password").val();
	  				
	  				if(userName==null||userName==''){
	  					alert("用户名不能为空!");
	  					return;
	  				}
	  				if(password==null||password==''){
	  					alert("密码不能为空!");
	  					return;
	  				}
	  				if(clickNum==0){
	  					clickNum++;
	  					//先验证，后提交
	  					$.ajax({
			    			type:'post',
			    			url:"${pageContext.request.contextPath}/weixinpage/checkYunWeiAccount",
			    			data : {
			    				"userName" : userName,
			    				"password" : password,
			    			},
			    			dataType : "json",
			    			success:function(obj){
			    				if(typeof obj=="string"){
	        						obj=JSON.parse(obj);
	        					}
	        					if(obj.success!=undefined&&obj.success){
	        						alert(obj.resultMsg);
	        						$("#pageForm").submit();
	        					}else{
	        						alert(obj.resultMsg);
	        						window.location.reload();//重新加载
	        						clickNum=0;
	        					}
			    			},error : function(XMLHttpRequest, textStatus, errorThrown) {
								clickNum=0;
							}
	    			});
	  				}
  			});
  		</script>
  			
  </body>
  
</html>
