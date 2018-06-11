<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-laydate-main {
  			height:200px;
  		}
  		.laydate-month-list,.layui-laydate-content {
  			height:154px;
  		}
  		.layui-laydate-content table {
  			display:none;	
  		}
  		.laydate-month-list>li {
  			margin:10px 0;
  		}
  		.layui-laydate-list>li {
  			height:29px;
  			line-height:29px;
  			margin:0;
  		}
  	</style>
  </head>
  <body>
		<div>
			<form class="layui-form" id="submitForm">
        		<div class="layui-form-item">
      				<label class="layui-form-label">日期选择</label>
      				<div class="layui-input-block">
        				<input type="text" name="dateTime" id="date" lay-verify="date" placeholder="yyyy-MM" autocomplete="off" class="layui-input">
      				</div>
    			</div>
        		<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        	</form>
        </div>
        <script type="text/javascript">	
        		layui.use(['laydate','form'],function(){
					var laydate = layui.laydate;
					var form = layui.form;
					var $ = layui.$;
					laydate.render({ 
  						elem: '#date',
						type: 'month'
					});
					$("#submitForm",parent.document).click(function(){
						$.ajax({
							type:"post",
							url:contextPath+"/opendoor/export",  
							data:{"dateTime":$("#date").val()},
							success:function(){
								//关闭模态框
                            	$(".modal",parent.document).animate({height:0,marginTop:0},400,function(){
                                   $(".modalShadow",parent.document).css("display","none");
                                   $(this).remove();
                            	});	
			                    window.location.href = contextPath+"/opendoor/download";  //页面间的跳转
							}
						});	
					});
					
				});
        </script>    
  </body>
</html>
