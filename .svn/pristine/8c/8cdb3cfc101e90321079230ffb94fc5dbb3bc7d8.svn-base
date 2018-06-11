<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<title>开门记录</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/lib/weui.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/css/jquery-weui.min.css"/> 
  		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  	<script type="text/javascript">
  	var pageNumber = 1;
  	
  	$(function(){
  		var recordList = ${listStr};
  		console.log(recordList);
  		var html="";
  		for(var i=0;i<recordList.length;i++){
  			html+='<div class="weui-form-preview__bd" style="color:#000000;">';
			html+='<div class="weui-form-preview__item" style="color:#000000;">';
			html+='<label class="weui-form-preview__label" style="color:#000000;display:block;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">';
  			html+="地点:";
  			html+=recordList[i].address;
  			html+="</label>";
  			html+="<br>";
  			html+='<label class="weui-form-preview__label" style="color:#000000;">';
  			html+="开门方式:";
  			html+=recordList[i].openTypeStr;
  			html+="</label>";
  			html+='<span class="weui-form-preview__value" style="color:#000000;">';
  			html+="时间:";
  			html+=recordList[i].openDateStr;
  			html+="</span>";
  			html+="</div>";
  			html+="</div>";
  			html+="<hr>";
  		}
  		$("#openDoorRecord").append(html);
  	});
  	function addlist(){
  	pageNumber+=1;
  		//ajax
  		$.ajax({
            url:"${pageContext.request.contextPath}/weixinpage/openDoorRecordPage",
            type:"post",
            data: {pageNumber: pageNumber },
            async:true,
            dataType:'json',
            success:function(data){
            		if(typeof data=="string"){
	        						data=JSON.parse(data); //处理date
	        					}
	        					//console.log(obj); 浏览器控制台打印日志或者另外一种处理方法——弹窗
	        					if(data.success!=undefined&&data.success){
	        						var list = data.listStr;
	        						var html="";
			  						for(var i=0;i<list.length;i++){
							  			html+='<div class="weui-form-preview__bd" style="color:#000000;">';
							  			html+='<div class="weui-form-preview__item" style="color:#000000;">';
							  			html+='<label class="weui-form-preview__label" style="color:#000000;display:block;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">';
							  			html+="地点:";
							  			html+=list[i].address;
							  			html+="</label>";
							  			html+="<br>";
							  			html+='<label class="weui-form-preview__label" style="color:#000000;">';
							  			html+="开门方式:";
							  			html+=list[i].openTypeStr;
							  			html+="</label>";
							  			html+='<span class="weui-form-preview__value" style="color:#000000;">';
							  			html+="时间:";
							  			html+=list[i].openDateStr;
							  			html+="</span>";
							  			html+="</div>";
							  			html+="</div>";
							  			html+="<hr>";
			  						}
			  					$("#openDoorRecord").append(html);
	        					}else{
	        						alert(data.info);
	        						pageNumber = pageNumber-1;
	        					}
            },
            error:function(e){
                console.log("失败！！");            
            }
        });
  	}
  	</script>
  	
  	</head>
  
  <body>
    <br>
     <div class="weui-form-preview" style="font-weight:bolder;color:#000000;" id="openDoorRecord">	
	</div>
	<span style="color:#009900; text-align: center;font-size: 18px;"><div id="Loading" onclick="addlist()" style="cursor: pointer;height: 32px;padding-top: 5px;">点击加载更多</div></span>
  </body>
  
</html>
