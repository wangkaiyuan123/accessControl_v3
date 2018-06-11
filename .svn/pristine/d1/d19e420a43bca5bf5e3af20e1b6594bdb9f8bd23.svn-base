<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<style>
			.layui-form-label {
				width:150px;
			}
		</style>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
    	</div>
    	<div class="iframeContent">
    		<div class="search">
            	<div class="search-title"><span>搜索栏</span></div>
            	<form class="layui-form">
			 		<div class="layui-form-item">
    					<div class="layui-inline">
      						<label class="layui-form-label">关键字（账号或昵称）:</label>
      						<div class="layui-input-inline">
        						<input type="text" name="account" autocomplete="off" class="layui-input">
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
    		changeTitle();
    		layui.use('table', function() {
            	var table = layui.table;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit: 10,
                	url:contextPath+'/person/getWeiXinUser',
                	request: {
                		limitName: 'rows'
                	},
                	cols:[[
                		{field:'left',width:100,align:'center',title:'序号'},
                    	{field:'nickname',width:250,align:'center',title:'昵称'},
                    	{field:'sex',width:250,align:'center',title:'性别'},
                    	{field:'addtime',width:250,align:'center',title:'关注时间'},
                    	{field:'account',width:250,align:'center',title:'账号'},
                    	{field:'innerPerson',width:350,align:'center',title:'是否内部人员'}
                	]],
                	page:true,
                	height:'full-190',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,0);
                    	//修改显示字段：
                    	changeView();	
                	}
               });
               $(".searchBtn").click(function(){
               		tableIns.reload({
  						where: {
    						account: $("input[name='account']").val()
  						}
					});
               });	
            });
            function changeView(){
           		var len=$(".layui-table-body tbody>tr").length;	
            	for(var i=0;i<len;i++){
            		var tdSex=$(".layui-table-body tbody>tr").eq(i).children("td:eq(2)").children("div");
            		var tdPerson=$(".layui-table-body tbody>tr").eq(i).children("td:eq(5)").children("div");
            		if(tdSex.html()==1){
            			tdSex.html("男");
            		}else if(tdSex.html()==2){
            			tdSex.html("女");
            		}else{
            			tdSex.html("保密");
            		}
            		if(tdPerson.html()==0){
            			tdPerson.html("否");
            		}else if(tdPerson.html()==1){
            			tdPerson.html("是");
            		}else if(tdPerson.html()==2){
            			tdPerson.html("审核中");	
            		}
            	}
           }
    	</script>
  </body>
</html>
