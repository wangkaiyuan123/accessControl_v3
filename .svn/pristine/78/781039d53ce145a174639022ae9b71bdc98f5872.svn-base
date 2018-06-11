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
        	<div class="rt">
           		<a class="layui-btn layui-btn-normal layui-btn-small export" lay-event=""><i class="layui-icon">&#xe61d;</i>导出</a>
       		</div>
    	</div>
    	<div class="iframeContent">
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
					limit:10,
                	url:contextPath+'/visitor/getList',
                	request: {
                		limitName: 'rows'
                	},
                	response: {
                		dataName: 'date'
                	},
                	cols:[[
                		{field:'left',width:100,align:'center',title:'序号'},
                    	{field:'visitorName',width:200,align:'center',title:'申请人'},
                    	{field:'appointTime',width:360,align:'center',title:'申请时间'},
                    	{field:'approverName',width:250,align:'center',title:'授权人'},
                    	{field:'openDate',width:250,align:'center',title:'授权时间'},
                    	{field:'flag',width:300,align:'center',title:'状态'}
                	]],
                	page:true,
                	height:'full-95',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,0);
                    	//修改显示字段：
                    	changeView();	
                	}
               });
            });
            function changeView(){
           		var len=$(".layui-table-body tbody>tr").length;	
            	for(var i=0;i<len;i++){
            		var tdHtml=$(".layui-table-body tbody>tr").eq(i).children("td:eq(5)").children("div");
            		
            		if(tdHtml.html()==1){
            			tdHtml.html("开门成功");
            		}else if(tdHtml.html()==2){
            			tdHtml.html("开门失败");
            		}else if(tdHtml.html()==3){
            			tdHtml.html("同意授权");
            		}else if(tdHtml.html()==4){
            			tdHtml.html("拒绝授权");
            		}
            	}
           }
           //导出：
           $(".export").click(function(){
				$(this).createModal({"title":"导出","modalHeight":400,"modalWidth":500,"url":contextPath+"/opendoor/exportPage"});
				
           });
    	</script>
  </body>
</html>
