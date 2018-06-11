<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-form-label {
  			width:100px;
  		}
  		.levelControl {
  			display:none;
  		}
  	</style>
  </head>
  <body>
	<div>
		<form class="layui-form">
			 <div class="layui-form-item">
    			<div class="layui-inline">
      				<label class="layui-form-label">机构名称:</label>
      				<div class="layui-input-inline">
        				<input type="text" name="regionName" lay-verify="required" autocomplete="off" class="layui-input">
      				</div>
    			</div>
    			<div class="layui-inline">
      				<label class="layui-form-label">管理员账号:</label>
      				<div class="layui-input-inline">
        				<input type="text" name="guserName" lay-verify="required" autocomplete="off" class="layui-input">
      				</div>
    			</div>
  			</div>
  			<div class="layui-form-item">
    			<div class="layui-inline">
      				<label class="layui-form-label">管理员电话:</label>
      				<div class="layui-input-inline">
        				<input type="text" name="guserPhone" lay-verify="required|phone" autocomplete="off" class="layui-input">
      				</div>
    			</div>
    			<div class="layui-inline">
      				<label class="layui-form-label">管理员用户名:</label>
      				<div class="layui-input-inline">
        				<input type="text" name="guserRealName" lay-verify="required" autocomplete="off" class="layui-input">
      				</div>
    			</div>
  			</div>
        	<div class="layui-form-item levelControl">
    			<div class="layui-inline">
      				<label class="layui-form-label">巡警账号：</label>
      				<div class="layui-input-inline">
        				<input type="text" name="puserName" lay-verify="required" autocomplete="off" class="layui-input">
      				</div>
    			</div>
    			<div class="layui-inline">
      				<label class="layui-form-label">巡警电话：</label>
      				<div class="layui-input-inline">
        				<input type="text" name="puserPhone" lay-verify="required|phone" autocomplete="off" class="layui-input">
      				</div>
    			</div>
  			</div>
  			<div class="layui-form-item levelControl">
    			<div class="layui-inline">
      				<label class="layui-form-label">巡警用户名：</label>
      				<div class="layui-input-inline">
        				<input type="text" name="puserRealName" lay-verify="required" autocomplete="off" class="layui-input">
      				</div>
    			</div>
    		</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form> 
	</div>
	<script type="text/javascript">
	    var level = ${loginUser.level};
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/regions/saveRegion","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
//         if(!(${loginUser.level}>=4)){
//         	$(".levelControl").css("display","block");	
//         }
        if(level<4){
          $(".levelControl").css("display","block");	
        }else{
          $(".levelControl").css("display","none");	
        }
	</script>                    
  </body>
</html>
