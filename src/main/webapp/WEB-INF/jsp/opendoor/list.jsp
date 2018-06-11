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
                	url:contextPath+'/opendoor/getList',
                	request: {
                		limitName: 'rows'
                	},
                	response: {
                		dataName: 'date'
                	},
                	cols:[[
                		{field:'left',width:100,align:'center',title:'序号'},
                    	{field:'deviceMac',width:250,align:'center',title:'设备'},
                    	{field:'address',width:360,align:'center',title:'地址'},
                    	{field:'openDate',width:250,align:'center',title:'开门时间'},
                    	{field:'openType',width:250,align:'center',title:'开门类型'},
                    	{field:'openDoorPerson',width:300,align:'center',title:'开门人'}
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
            		var tdHtml=$(".layui-table-body tbody>tr").eq(i).children("td:eq(4)").children("div");
            		if(tdHtml.html()==1){
            			tdHtml.html("门卡");
            		}else if(tdHtml.html()==2){
            			tdHtml.html("微信扫码");
            		}else if(tdHtml.html()==3){
            			tdHtml.html("密码开门");
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
