<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-form-selected dl {
  			height:80px;
  		}	
  	</style>
  </head> 
  <body>
    <div>
		<form class="layui-form">
        	<div class="layui-form-item">
        		<div class="layui-inline">
            		<label class="layui-form-label">姓名:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="name" lay-verify="required" autocomplete="off" class="layui-input"/>
                		<!--<input type="hidden" value="" name="regionId"/>-->
            		</div>
            	</div>
            	<div class="layui-inline">
            		<label class="layui-form-label">年龄:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="age" lay-verify="required/number" autocomplete="off" class="layui-input"/>
            		</div>
            	</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">性别:</label>
    				<div class="layui-input-inline">
      					<input type="radio" name="sex" value="1" title="男">
      					<input type="radio" name="sex" value="2" title="女">
    				</div>	
    			</div>
    			<div class="layui-inline">
    				<label class="layui-form-label">卡号:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="cardId"  autocomplete="off" class="layui-input"/>
            		</div>
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">用户类型:</label>
            		<div class="layui-input-inline">
      					<select name="personTypeId" id="personTypeId" lay-filter="personTypeId">
      					</select>
    				</div>
    			</div>
    			<div class="layui-inline dy">
    				<label class="layui-form-label">单元 :</label>
            		<div class="layui-input-inline">
      					<select name="unitId" lay-filter="personTypeId">
      					</select>
    				</div>		
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">身份证号:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="identifyId" lay-verify="required" autocomplete="off" class="layui-input"/>
            		</div>		
    			</div>
        	</div>
        	<div class=layui-form-item>
        		<div class="layui-inline">
    				<label class="layui-form-label">电话:</label>
            		<div class="layui-input-inline">
                		<input type="text" maxlength="11"  name="telephone" lay-verify="required/phone" autocomplete="off" class="layui-input"/>
            		</div>	
    			</div>
    			<div class="layui-inline">
    				<div class="layui-input-inline">
                		<input type="text" name="code" lay-verify="required" autocomplete="off" class="layui-input" placeholder="输入验证码"/>
            		</div>	
            		<button class="layui-btn verifyCode" type="button">获取验证码</button>
    			</div>		
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">微信:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="nickname" autocomplete="off" class="layui-input" readonly="readonly"/>
                		<input type="hidden" name="openid" autocomplete="off" class="layui-input"/>
            		</div>		
    			</div>
    			<div class="layui-inline">
    				<button class="layui-btn selectWeiXin" type="button">选择</button>
    				<button class="layui-btn layui-btn-danger resetWeiXin" type="button">清空选择</button>
    			</div>
    		</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            sendAjax(form,'/person/getPersonType','personTypeId',true);
            sendAjax(form,'/person/getUnits','unitId');
            var submitForm =new submitObj({"type":"post","url":contextPath+"/person/savePersonData","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
        
        /**
            form.on('select(personTypeId)',function(data){
            var seltext = $("#personTypeId option:selected").text();
            alert(seltext);
            $(".dy").css("display","none");
            });
        */
        
        function sendAjax(form,url,name,personTypeId){
        	$.ajax({
        		type:'post',
        		cache:false,
        		url:contextPath+url,
        		success:function(data){
        			if(typeof data=="string"){
        				data=JSON.parse(data);
        			}
        			$("select[name='"+name+"']").html('<option value=""></option>');
        			var optHtml='';
        			$.each(data,function(index,list){
        				if(personTypeId){
        					optHtml+="<option value="+list.id+">"+list.typeName+"</option>";
        				}else{
        					optHtml+="<option value="+list.id+">"+list.communityName+"</option>";
        				}
        			});
        			$("select[name='"+name+"']").append(optHtml);
        			form.render('select');
        		}
        	});
        };
        //微信弹出页面
		$(".selectWeiXin").click(function(){
			$(this).createModal({
				"title":"选择微信号",
				"modalHeight":600,
				"modalWidth":700,
				"url":contextPath+"/person/WeiXinList",
				"sec":true,
				"closed":function(){
					var weixinId=$("#radioWeiXin",parent.document).val();
					$("input[name='openid']").val(weixinId);
					var weixinName=$("#radioWeiXin",parent.document).attr("data-name");
					$("input[name='nickname']").val(weixinName);
				}
			});	
		});
		//清空：
		$(".resetWeiXin").click(function(){
			$("input[name='openid']").val("");
			$("input[name='nickname']").val("");
			$("#radioWeiXin",parent.document).val("");
			$("#radioWeiXin",parent.document).attr("data-name","");	
			$("#radioWeiXin",parent.document).attr("data-number","");
		});
		var n=0;
		$(".verifyCode").click(function(){
			var i=60,
			telephone=$("input[name='telephone']").val();
			var reg = /^1[34578][0-9]{9}$/;
			if(!n && telephone && reg.test(telephone)){
				var timer = setInterval(function(){
					i--;
					$(".verifyCode").html(i+"s");
					n=1;
					if(i==0){
						clearInterval(timer);
						$(".verifyCode").html("重新获取验证码");
						n=0;	
					}
				},1000);
				$.ajax({
					type:'post',
					data:{"phone":telephone},
					dataType:'json',
					url:contextPath+'/weixin/getCode',
					success:function(data){
						if(data.success==true){
							layer.msg("已发送！");
						}	
					}					
				});	
			}else{
				layer.msg("请输入有效手机号！");	
			}	
		});
	</script>
  </body>
</html>
