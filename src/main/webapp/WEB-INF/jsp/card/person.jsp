<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<jsp:include page="/inc.jsp"/>
		<style>
			.radioWeinXin:hover {
				cursor:pointer;
			}
			.weixinIcon {
				color:#5FB878;
			}
			.layui-form-item {
				margin:0;
			}
			.layui-form-label {
				width:60px;
			}
		</style>
 	</head>
  	<body>
    	<div class="iframeContent">
    		 <div class="search">
            	<div class="search-title"><span>搜索栏</span></div>
            	<form class="layui-form">
			 		<div class="layui-form-item">
    					<div class="layui-inline">
      						<label class="layui-form-label">姓名:</label>
      						<div class="layui-input-inline" style="width:120px;">
        						<input type="text" name="personName" autocomplete="off" class="layui-input">
      						</div>
    					</div>
    					<div class="layui-inline">
      						<label class="layui-form-label">身份证:</label>
      						<div class="layui-input-inline" style="width:180px;">
        						<input type="text" name="identifyId" autocomplete="off" class="layui-input">
      						</div>
    					</div>
    					<div class="layui-inline">
    						<button class="layui-btn searchBtn" type="button" lay-filter="search">查询</button>
    					</div>
  					</div>
  				</form>
        	</div>
    		 <table class="layui-table" id="tableList" lay-filter="tableList">
    		 </table>
    	</div>
    	<script type="text/javascript">
    		var formData=JSON.parse($("#editJson",parent.document).val());
    		layui.use(['table','form'], function() {
            	var table = layui.table;
            	var form=layui.form;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit: 10,
                	url:contextPath+'/card/getPersonList',
                	request: {
                		limitName: 'rows'
                	},
                	cols:[[
                		{field:'radio',width:91,align:'center',title:'选择'},//91
                		{field:'left',width:91,align:'center',title:'序号'},//91
                    	{field:'name',width:200,align:'center',title:'姓名'},//200
                    	{field:'identifyId',width:240,align:'center',title:'身份证'},//200
                	]],
                	page:true,
                	height:'full-135',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,1);
                    	//单选框
                    	changeRadio(res);
                	}
               });
                $(".searchBtn").click(function(){
               		tableIns.reload({
  						where: {
    						personName: $("input[name='personName']").val(),
    						identifyId: $("input[name='identifyId']").val(),
    					}
					});
               });
               
           });
           function changeRadio(res){
           		var len=$(".layui-table-body tr").length;
           		var tr=$(".layui-table-body tr");
           		var tdHtml='<div class="radioWeinXin">'+
      							'<input type="radio" name="radioWeinXin">'+
      							'<i class="layui-icon weixinIcon" style="font-size:20px;">&#xe63f;</i>'+	
    						'</div>';
           		for(var i =0;i<len;i++){
           			tr.eq(i).children("td:eq(0)").children("div").html(tdHtml);		
           		}
           		//点击事件：
           		$(".layui-table-body").on("click",".radioWeinXin",function(){
           			var trIndex=$(this).parent().parent().parent().index();
           			var submitForm =new submitObj({"type":"post","data":{"id":formData.id,"personId":res.data[trIndex].id},"url":contextPath+"/card/saveBindPerson","refresh":true,"Isform":false});
			  		submitForm.submit();
           			//其余的设置成未选中：
           			$(".radioWeinXin").not(this).children("input").prop("checked",false).end().children("i").html("&#xe63f;");
           			if(!$(this).children("input").prop("checked")){
           				$(this).children("i").html("&#xe643;");
           			}
           		});
           }
    	</script>
  </body>
</html>
