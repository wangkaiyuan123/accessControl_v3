<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-input-block {
  			width:360px;
  		}
  	</style>
  </head> 
  <body>
	<div>
		<form class="layui-form">
		    <div class="layui-form-item layui-form-text">
      				<label class="layui-form-label">序号：</label>
      				<div class="layui-input-inline">
        				<input type="text" name="sendSort" lay-verify="required" autocomplete="off" class="layui-input">
      				</div>
    		</div>
			<div class="layui-form-item layui-form-text">
    			<label class="layui-form-label">内容：</label>
    			<div class="layui-input-block">
      				<textarea placeholder="请输入内容" class="layui-textarea" name="content" lay-verify="required"></textarea>
    			</div>
			</div>
			<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
		</form>
	</div>
	<script type="text/javascript">
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/advertising/saveAdd","refresh":true,"Isform":true});
			submitForm.submit(form);
        });	
	</script>    
  </body>
</html>
