/**
 * Created by wuxinmeng on 2017/10/16.
 */
function containerHeight(parent,children,size){
    var iframeHeight = $(parent).height()-size;
    $(children).height(iframeHeight);
}
//中间显示标题部门：
function changeTitle(){
	var html=$("#changeIframeTitle",parent.document).val();
	$(".iframeTitle>p>b").html(html);
}
//表格排序：
function sortTable(curr,count,n,pageFalse){
	if(pageFalse){
		//alert(1);
		var startNum=1;
		var tr=$(".layui-table-body tbody tr");
		for(var i=0;i<count;i++){
			tr.eq(i).find("td:eq("+n+")>div").html(startNum+i);
		}	
	}else{
		var limit = $(".layui-laypage-limits>select").val();//一页的显示条数
		var totalPage=Math.ceil(count/limit);//总页数
		var startNum=1;
		/*if(totalPage>1){
			startNum = count - (totalPage-curr)*limit;	
		}*/
		if(curr==1){
			startNum=1;
		}else{
			startNum=limit*(curr-1)+1;
		}
		var tr=$(".layui-table-body tbody tr");
		for(var i=0;i<limit;i++){
			tr.eq(i).find("td:eq("+n+")>div").html(startNum+i);
		}	
	}
	
}

//左侧机构树点击完之后改变颜色：
/*function changeOrganColor(){
	$("#organTree").on("click","li",function(){
		alert(1);
		$(this).addClass("changeAColor");	
	})
}*/

