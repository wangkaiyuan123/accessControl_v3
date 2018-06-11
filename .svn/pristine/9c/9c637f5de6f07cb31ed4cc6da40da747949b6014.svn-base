<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>东华宏泰</title>
    <jsp:include page="/inc.jsp" />
</head>
<body>
	<div>
		<form class="layui-form">
        	<div class="layui-form-item">
            	<label class="layui-form-label">原密码:</label>
            	<div class="layui-input-block">
                	<input type="password" name="oldPwd" lay-verify="required" autocomplete="off" placeholder="请输入密码" class="layui-input"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">新密码:</label>
            	<div class="layui-input-block">
                	<input type="password" name="newPwd" lay-verify="required" autocomplete="off" placeholder="请输入新密码" class="layui-input" id="newPwd"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">确认密码:</label>
            	<div class="layui-input-block">
                	<input type="password" name="password" lay-verify="required|surePwd" autocomplete="off" placeholder="请再次输入新密码" class="layui-input"/>
            	</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
	 layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            form.verify({
            	"surePwd":function(value,item){
            		if($("#newPwd").val()!=value){
            			return "两次密码输入不一致";
            		}
            	}	
            });
			var submitForm =new submitObj({"type":"post","url":contextPath+"/password/savePwd","refresh":false,"Isform":true});
			submitForm.submit(form);
	});
	</script>
</body>