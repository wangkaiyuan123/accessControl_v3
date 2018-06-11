<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<title>我的门卡</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/lib/weui.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/css/jquery-weui.min.css"/> 
  		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  		<style type="text/css">
  			.weui-cell .weui-cell__hd .weui-label{
  				min-width: 165px;
  			}
  			.weui-cell .weui-cell__bd .weui-label{
  				min-width: 200px;
  			}
  		</style>
  		
  		<script type="text/javascript">
  			$(function(){
  				var unitList = ${map.get("list")};
  				console.log(unitList);
  				var html="";
  				for(var i=0;i<unitList.length;i++){
  					html+='<p style="word-wrap: break-word; white-space:nowrap;overflow: hidden;">';
  					html+=unitList[i].communityName;
  					html+=unitList[i].unitName;
  					html+='</p>';
  				}
  				$("#unitName").append(html);
  			})
  			
  			function addNewUnit(){
  				window.location.href="${pageContext.request.contextPath}/weixinpage/toNewUnit";
  			}
  			
  		</script>
  		
  	</head>
   
  <body>
    <div class="weui-cells weui-cells_form" style="margin-bottom:25px; text-align: left;">
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label">卡号:</label>
    			</div>
    			<div class="weui-cell__bd" style="word-wrap: break-word;">
      				<p>${map.get("cardNo") }</p>
    			</div>
  			</div>	
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label">卡串码:</label>
    			</div>
    			<div class="weui-cell__bd" style="word-wrap: break-word;">
      				<p>${map.get("cardNumber") }</p>
    			</div>
  			</div>	
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label">绑定人员:</label>
    			</div>
    			<div class="weui-cell__bd" style="word-wrap: break-word;">
      				<p>${map.get("personName") }</p>
    			</div>
  			</div>		
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label">最近使用时间:</label>
    			</div>
    			<div class="weui-cell__bd" style="word-wrap: break-word;">
      				<p>${map.get("recentTime") }</p>
    			</div>
  			</div>	
  			<!-- 备注：<label>换成<p>的原因是label里面的属性点击不了，不会响应，原因暂时不清楚，此外图片与文字没有完全对齐，待处理 -->
  			<div class="weui-cell">
				<div class="weui-cell__hd" style="word-wrap: break-word;">
    				<p class="weui-label">绑定单元:
    					<!--<img style="vertical-align:middle;float:right;" alt="新增"
    						src="${pageContext.request.contextPath}/images/add.png" onclick="addNewUnit()">-->
    				</p>					
    			</div>
    			<div class="weui-cell__bd" style="word-wrap: break-word;">
    				<p id="unitName"></p>
    			</div>
  			</div> 				
  			<!--  
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label">备注:</label>
    			</div>
    			<div class="weui-cell__bd" style="word-wrap: break-word;">
    			${map.get("comment") }
    			</div>
  			</div>	
  			-->
            <div class="weui-cell">
                <button class="weui-btn weui-btn_primary" onclick="addNewUnit()">新增绑定单元</button>
            </div>

  				
  		</div>
   
   
    
  </body>
</html>
