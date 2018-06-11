<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-form-selected dl {
  			height:110px;
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
            		<label class="layui-form-label">单元名:</label>
            		<div class="layui-input-inline" style="width:190px;">
                		<input type="text" name="unitName" lay-verify="required" autocomplete="off" class="layui-input"/>
                		<!--<input type="hidden" value="" name="regionId"/>-->
            		</div>
            	</div>
            	<div class="layui-inline levelControl">
            		<label class="layui-form-label">省:</label>
            		<div class="layui-input-inline">
      					<select name="pid" lay-filter="pid">
      					</select>
    				</div>	
            	</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline levelControl">
    				<label class="layui-form-label">市:</label>
            		<div class="layui-input-inline">
      					<select name="cid" lay-filter="cid">
      					</select>
    				</div>	
    			</div>
    			<div class="layui-inline levelControl">
    				<label class="layui-form-label">区/县:</label>
            		<div class="layui-input-inline">
      					<select name="did" lay-filter="did">
      					</select>
    				</div>		
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline levelControl">
    				<label class="layui-form-label">街道/乡镇:</label>
            		<div class="layui-input-inline">
      					<select name="sid" lay-filter="sid">
      					</select>
    				</div>
    			</div>
    			<div class="layui-inline levelControl">
    				<label class="layui-form-label">小区 :</label>
            		<div class="layui-input-inline">
      					<select name="rid" lay-filter="rid">
      					</select>
    				</div>		
    			</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		if('${loginUser.level}'<5){
			$(".levelControl").css("display","inline-block");	
		}else{
			$(".modal",parent.document).width(420).height(350);
			$(".modal_content",parent.document).height(150);
			$(".levelControl").css("display","none");	
		}
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            sendAjax(form,{level:1,parentId:''},'/community/getRegion','pid');
            form.on('select(pid)', function(data){
            	var dataAjax={level:2,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','cid');	
			});
			form.on('select(cid)', function(data){
            	var dataAjax={level:3,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','did');	
			});
			form.on('select(did)', function(data){
            	var dataAjax={level:4,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','sid');	
			});
			form.on('select(sid)', function(data){
            	var dataAjax={level:5,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','rid');	
			});
            var submitForm =new submitObj({"type":"post","url":contextPath+"/unit/saveUnit","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
        function sendAjax(form,data,url,name){
        	$.ajax({
        		type:'post',
        		cache:false,
        		data:data,
        		url:contextPath+url,
        		success:function(data){
        			if(typeof data=="string"){
        				data=JSON.parse(data);
        			}
        			$("select[name='"+name+"']").html('<option value=""></option>');
        			var optHtml='';
        			$.each(data,function(index,list){
        				optHtml+="<option value="+list.id+">"+list.regionName+"</option>";
        			});
        			$("select[name='"+name+"']").append(optHtml);
        			form.render('select');
        		}
        	});
        };
	</script>
  </body>
</html>
