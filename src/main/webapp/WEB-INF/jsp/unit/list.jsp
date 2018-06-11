<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<style>
			.layui-table,.layui-table-view {
				margin:0;
			}
		</style>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
        	<div class="rt">
            	<a class="layui-btn layui-btn-normal layui-btn-small add" lay-event="del"><i class="layui-icon">&#xe654;</i>新增</a>
        	</div>
    	</div>
    	<div class="iframeContent">
    		<!--<div class="search">
            	<div class="search-title"><span>搜索栏</span></div>
            	<form class="layui-form">
			 		<div class="layui-form-item">
    					<div class="layui-inline">
      						<label class="layui-form-label">地址:</label>
      						<div class="layui-input-inline">
        						<input type="text" name="communityAddress" autocomplete="off" class="layui-input">
      						</div>
    					</div>
    					<div class="layui-inline">
    						<button class="layui-btn searchBtn" type="button" lay-filter="search">查询</button>
    					</div>
  					</div>
  				</form>
        	</div>  -->
        	<div class="searchRes">
        		<div class='treeContainer lf'>
        			<p>机构列表</p>
        			<ul id="organTree"></ul>
        		</div>
        		<div class="tableContainer">
        			<table class="layui-table" id="tableList" lay-filter="tableList">
    				</table>
        		</div>
        		<div class="clear"></div>
        	</div>
    	</div>
    	<script type="text/html" id="barDemo">
        	<a class="del layui-btn layui-btn-danger layui-btn-mini" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>
    	</script>
    	<script type="text/javascript">
    		//设置标题：
    		changeTitle();
    		//设置下部分高度：
    		containerHeight("body",".iframeContent",70);
    		containerHeight(".iframeContent",".searchRes",25);
    		containerHeight(".tableContainer","#organTree",30);	
    		$(window).resize(function(){
    			containerHeight("body",".iframeContent",70);
    			containerHeight(".iframeContent",".searchRes",25);
    			containerHeight(".tableContainer","#organTree",30);	
    		});
    		//organTree
    		$.ajax({
    			type:'post',
    			cache:false,
    			url:contextPath+'/unit/getTrees',
    			dataType:'json',
    			success:function(data){
    				var firstId=data[0].id;
    				if(typeof data=="string"){
    					data=JSON.parse(data);
    				}
    				//递归调用查找regionName全部替换成name
    				function changeName(list){
    					for(var i=0;i<list.length;i++){
    						list[i].spread=true;
    						if(list[i].hasOwnProperty("regionName")){
    							list[i].name=list[i].regionName;
    						}
    						if(list[i].hasOwnProperty("children")){
    							changeName(list[i].children);
    						}
    					}
    				}
    				changeName(data);	
    				layui.use('tree', function(){
    					var $=layui.$;
    					layui.tree({
    						elem:'#organTree',
    						nodes:data,
    						click: function(node){
    							renderTable(node.id);
  							}  
    					});
    					$("#organTree a").click(function(){
    						$("#organTree a").not(this).css("color","#333");
    						$(this).css("color","#009688");					
    					}); 
    					$("#organTree a:eq(0)").css("color","#009688").addClass("aColor");
    				});
    				renderTable(firstId);
    			}
    		});
    		//表格
    		function renderTable(id){
    			layui.use('table', function() {
            		var table = layui.table;
            		var tableIns= table.render({
                		elem:'#tableList',
                		limits: [10,20,50,100],
						limit: 10,
                		url:contextPath+'/unit/getUnitList',
                		where:{'regionId':id},
                		request: {
                			limitName: 'rows'
                		},
                		cols:[[
                			{field:'left', width:100,align:'center',title:'序号'},
                    		{field:'unitName', width:200,align:'center',title:'单元名'},
                    		{field:'communityName', width:350,align:'center',title:'小区'},
                    		{field:'right', width:150, align:'center', toolbar: '#barDemo',title:'操作'}
                		]],
                		page:true,
                		height:'full-95',
                		id:'tableList',
                		even:true,
                		done:function(res,curr,count){
                		//curr是当前的页数count总条数
                			sortTable(curr,count,0);
                		}
               		});
               		/*$(".searchBtn").click(function(){
               			tableIns.reload({
  							where: {
    							communityAddress: $("input[name='communityAddress']").val()
  							}
						});
              	 	});*/
               		table.on('tool(tableList)', function(obj){
  						var data = obj.data;
  						var layEvent = obj.event;
  						var listStr=JSON.stringify(data);
  						$("#editJson",parent.document).val(listStr);
  						if(layEvent === 'del'){
  							$(this).createModal({"title":"删除","modalHeight":150,"modalWidth":330,"url":contextPath+"/unit/delete"});
  						}
  					});	
           	 	});
    		}
            //新增：
            $(".add").click(function(){
            	$(this).createModal({"title":"新增","modalHeight":320,"modalWidth":720,"url":contextPath+"/unit/add"});
            });
    	</script>
  </body>
</html>
