<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<style>
			span {
				float:left;
				font-size:25px;
				margin:0 4px;
				text-align:center;
			}
			span:hover {
				cursor:pointer;
			}
		</style>
  	</head>
  	<body>
    	<div class="icon"></div>
    	<script type="text/javascript" src="${pageContext.request.contextPath}/js/iconData.js"></script>
    	<script type="text/javascript">
    		var len=iconData.length;
    		var spanHtml="";
    		for(var i=0;i<len;i++){
    			spanHtml+='<span class="'+iconData[i].name+'" style="width:35px;"></span>';
    		}
    		$("div.icon").append(spanHtml);	
    		//点击事件
    		$("div.icon").on("click","span",function(){
    			$(this).css({background:"#009F95",color:"#fff"}).siblings("span").css({background:"#fff",color:"#000"});
    			$("#iconClass",parent.document).val($(this).attr("class"));
    		});
    	</script>
  	</body>
</html>
