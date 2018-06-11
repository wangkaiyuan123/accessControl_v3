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
			<div class="layui-form-item">
				<label class="layui-form-label">关键字：</label>
				<div class="layui-input-block">
					<input type="text" class="layui-input" lay-verify="required" autocomplete="off" name="keyNo"/>
				</div>
			</div>
			<div class="layui-form-item layui-form-text">
    			<label class="layui-form-label">回复内容：</label>
    			<div class="layui-input-block">
      				<textarea placeholder="请输入内容" class="layui-textarea" name="replyContent" lay-verify="required"></textarea>
    			</div>
			</div>
			<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
		</form>
	</div>
	<script type="text/javascript">
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/keyword/saveAdd","refresh":true,"Isform":true});
			submitForm.submit(form);
        });	
	</script>    
  </body>
</html>
