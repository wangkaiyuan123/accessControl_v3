<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
<!--         	<div class="rt"> -->
<!--             	<a class="layui-btn layui-btn-normal layui-btn-small add" lay-event="del"><i class="layui-icon">&#xe654;</i>新增</a> -->
<!--         	</div> -->
    	</div>
    	<div class="iframeContent">
    		 <div class="search">
            	<div class="search-title"><span>搜索栏</span></div>
            	<form class="layui-form">
			 		<div class="layui-form-item">
    					<div class="layui-inline">
      						<label class="layui-form-label">门卡:</label>
      						<div class="layui-input-inline">
        						<input type="text" name="cardNo" autocomplete="off" class="layui-input">
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
    	<script type="text/html" id="barDemo">
        	<a class="edit layui-btn layui-btn-mini layui-btn-warm" lay-event="edit"><i class="layui-icon">&#xe614;</i>绑定人员</a>
    	</script>
    	<script type="text/javascript">
    		changeTitle();
    		layui.use('table', function() {
            	var table = layui.table;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit: 10,
                	url:contextPath+'/card/getCardList',
                	request: {
                		limitName: 'rows'
                	},
                	cols:[[
                		{field:'left', width:100,align:'center',title:'序号'},
                    	{field:'cardNo', width:200,align:'center',title:'卡号'},
                    	{field:'recentTime', width:300,align:'center',title:'最近使用时间'},
                    	{field:'name', width:260,align:'center',title:'绑定人'},
//                     	{field:'right', width:260, align:'center', toolbar: '#barDemo',title:'操作'}
                	]],
                	page:true,
                	height:'full-190',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,0);	
                	}
               });
               $(".searchBtn").click(function(){
               		tableIns.reload({
  						where: {
    						cardNo: $("input[name='cardNo']").val()
  						}
					});
               });
               table.on('tool(tableList)', function(obj){
  					var data = obj.data; //获得当前行数据
  					var layEvent = obj.event; //获得 lay-event 对应的值
  					var listStr=JSON.stringify(data);
  					$("#editJson",parent.document).val(listStr);
  					if(layEvent === 'edit'){
  						$(this).createModal({"title":"绑定人员","modalHeight":600,"modalWidth":700,"url":contextPath+"/card/person"});	
  					}
  				});	
            });
              //新增：
            $(".add").click(function(){
            	$(this).createModal({"title":"新增","modalHeight":220,"modalWidth":500,"url":contextPath+"/card/add"});
            });
    	</script>
  </body>
</html>
