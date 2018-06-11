<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/framework/layui/css/layui.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/framework/layui/layui.js"></script>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-form-selected dl {
  			height:110px;
  		}	
  	</style>
  </head> 
  <body>
    <div>
		<form class="layui-form">
<!-- 			<div class="layui-form-item"> -->
<!--         		<div class="layui-inline"> -->
<!--             		<label class="layui-form-label">设备号:</label> -->
<!--             		<div class="layui-input-inline"> -->
<!--                 		<input type="text" name="deviceNo" lay-verify="required" autocomplete="off" class="layui-input"/> -->
<!--             		</div> -->
<!--             	</div> -->
<!--             	<div class="layui-inline"> -->
<!--             		<label class="layui-form-label">TCP端口:</label> -->
<!--             		<div class="layui-input-inline"> -->
<!--                 		<input type="text" name="TcpPort" lay-verify="required" autocomplete="off" class="layui-input"/> -->
<!--             		</div> -->
<!--             	</div> -->
<!--             </div> -->
        	<div class="layui-form-item">
<!--         		<div class="layui-inline"> -->
<!--             		<label class="layui-form-label">设备端口:</label> -->
<!--             		<div class="layui-input-inline"> -->
<!--                 		<input type="text" name="devicePort" lay-verify="required" autocomplete="off" class="layui-input"/> -->
<!--             		</div> -->
<!--             	</div> -->
            	<div class="layui-inline">
            		<label class="layui-form-label">Mac地址:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="deviceMac" lay-verify="required" autocomplete="off" class="layui-input"/>
            		</div>
            	</div>
        	</div>
        	<!--<div class="layui-form-item">
        		<div class="layui-inline">
            		<label class="layui-form-label">省:</label>
            		<div class="layui-input-inline">
      					<select name="provinceId" lay-filter="provinceId">
      					</select>
    				</div>	
            	</div>
    			<div class="layui-inline">
    				<label class="layui-form-label">市:</label>
            		<div class="layui-input-inline">
      					<select name="cityId" lay-filter="cityId">
      					</select>
    				</div>	
    			</div>
        	</div>
        	<div class="layui-form-item">
        		<div class="layui-inline">
    				<label class="layui-form-label">区/县:</label>
            		<div class="layui-input-inline">
      					<select name="districtId" lay-filter="districtId">
      					</select>
    				</div>		
    			</div>
    			<div class="layui-inline">
    				<label class="layui-form-label">街道/乡镇:</label>
            		<div class="layui-input-inline">
      					<select name="streetId" lay-filter="streetId">
      					</select>
    				</div>
    			</div>
        	</div>  -->
        	<div class="layui-form-item">
        		<!--<div class="layui-inline">
    				<label class="layui-form-label">小区:</label>
            		<div class="layui-input-inline">
      					<select name="communityId" lay-filter="communityId">
      					</select>
    				</div>
    			</div>  -->
    			<div class="layui-inline">
    				<label class="layui-form-label">单元/大门:</label>
            		<div class="layui-input-inline">
      					<select name="unitId" lay-filter="unitId">
      					</select>
    				</div>
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">密码:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="password" lay-verify="required" autocomplete="off" class="layui-input"/>
            		</div>		
    			</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            sendAjax2(form);
            /*sendAjax(form,{level:1,parentId:''},'/community/getRegion','provinceId');
            form.on('select(provinceId)', function(data){
            	var dataAjax={level:2,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','cityId');	
			});
			form.on('select(cityId)', function(data){
            	var dataAjax={level:3,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','districtId');	
			});
			form.on('select(districtId)', function(data){
            	var dataAjax={level:4,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','streetId');	
			});
			form.on('select(streetId)', function(data){
            	var dataAjax={level:5,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','communityId');	
			});
			
			form.on('select(communityId)', function(data){
            	var dataAjax={level:6,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','unitId');	
			});*/
            var submitForm =new submitObj({"type":"post","url":contextPath+"/device/saveDevice","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
        /*function sendAjax(form,data,url,name){
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
        			form.render();
        		}
        	});
        };*/
        function sendAjax2(form){
        	$.ajax({
        		type:'post',
        		cache:false,
        		dataType:'json',
        		url:contextPath+'/device/getUnit',
        		success:function(data){
        			$("select[name='unitId']").html('<option value=""></option>');
        			var optHtml='';
        			$.each(data,function(index,list){
        				optHtml+="<option value="+list.id+">"+list.unitName+"</option>";
        			});
        			$("select[name='unitId']").append(optHtml);
        			form.render();	
        		}	
        	})
        }
	</script>
  </body>
</html>
