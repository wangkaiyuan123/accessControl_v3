<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<jsp:include page="/inc.jsp"/>
		<link rel="sheetstyle" href="${pageContext.request.contextPath}/framework/view/viewer.min.css"/>
		<script src="${pageContext.request.contextPath}/framework/view/viewer-jquery.min.js"></script>
		<style>
			.layui-table,.layui-table-view {
				margin:0;
			}
		</style>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
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
    	<input type="hidden" id="levelInput" value=""/>
    	<script type="text/html" id="barDemo">
			{{# if ($("#levelInput").val() > 5){}}
				{{# if (d.state==0){ }}
					<a class="edit layui-btn layui-btn-mini" lay-event="approve"><i class="layui-icon">&#xe614;</i>审批</a>
				{{# }else{ }}
					<a class="edit layui-btn layui-btn-mini layui-btn-primary"><i class="layui-icon">&#xe614;</i>审批</a>		
				{{# } }}		
			{{# }else { }}
				<a href="javascript:;"/>无相关操作</a>
			{{# } }}
    	</script>
    	<script type="text/javascript">
    		//<a class="edit layui-btn layui-btn-mini layui-btn-warm" lay-event="photo"><i class="layui-icon">&#xe64a;</i>查看图片</a>
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
    			url:contextPath+'/applyrecord/getTrees',
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
    							$("#levelInput").val(node.level);
  							}  
    					});
    					$("#organTree a").click(function(){
    						$("#organTree a").not(this).css("color","#333");
    						$(this).css("color","#009688");					
    					}); 
    					$("#organTree a:eq(0)").css("color","#009688").addClass("aColor");
    				});
    				renderTable(firstId);
    				$("#levelInput").val(data[0].level);
    			}
    		});
    		//表格
    		function renderTable(id){
    			layui.use(['table','layer'], function() {
            		var table = layui.table;
            		var layer = layui.layer;
            		var tableIns= table.render({
                		elem:'#tableList',
                		limits: [10,20,50,100],
						limit: 10,
                		url:contextPath+'/applyrecord/getApplyRecord',/*'/applyrecord/getApplyRecord',/data/1.json*/
                		where:{'regionId':id},
                		request: {
                			limitName: 'rows'
                		},
                		cols:[[
                			{field:'left', width:100,align:'center',title:'序号'},
                    		{field:'name', width:80,align:'center',title:'姓名'},
                    		{field:'identifyId', width:200,align:'center',title:'身份证'},
                    		{field:'telephone', width:150,align:'center',title:'电话'},
                    		{field:'state', width:100,align:'center',title:'状态'},
                    		{field:'applyDate', width:150,align:'center',title:'申请时间'},
                    		{field:'checkDate', width:150,align:'center',title:'审核时间'},
                    		{field:'checkPerson', width:150,align:'center',title:'审核人'},
                    		{field:'right', width:200, align:'center', toolbar: '#barDemo',title:'操作'}
                		]],
                		page:true,
                		height:'full-95',
                		id:'tableList',
                		even:true,
                		done:function(res,curr,count){
                			//curr是当前的页数count总条数
                			sortTable(curr,count,0);
                			//修改视图显示：
                			changeView();
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
  						if(layEvent==='approve'){
  							$(this).createModal({"title":"审批","modalHeight":550,"modalWidth":600,"url":contextPath+"/applyrecord/check"});
  						}else if(layEvent==='photo'){
  							if(data.imgUrl){
  								$("#jq22",parent.document).find("img").attr("src",data.imgUrl)
  									.end().viewer({
  										navbar:false	
  								});
  								//$(".viewer-container",parent.document).remove();	
  								$("#jq22",parent.document).find("img").click();		
  							}else{
  								layer.msg('没有相关图片');	
  							}
  						}
  					});	
           	 	});
    		}
            function changeView(){
            	var len=$(".layui-table-body tbody>tr").length;	
            	for(var i=0;i<len;i++){
            		var tdState=$(".layui-table-body tbody>tr").eq(i).children("td:eq(4)").children("div");
            		if(tdState.html()==0){
            			tdState.html("审核中");
            		}else if(tdState.html()==1){
            			tdState.html("审核通过");
            		}else{
            			tdState.html("审核失败");
            		}
            	}
            }
    	</script>
  </body>
</html>
