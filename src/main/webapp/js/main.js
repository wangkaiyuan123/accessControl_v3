/**
 * Created by wuxinmeng on 2017/10/17.
 */
function loadLeftSide(){
    $.ajax({
        type:'get',
        url:contextPath+"/getMenuData",
        dataJson:'json',
        cache:false,
        success:function(data){
            var leftSideHtml='',leftSideHtmlSecond,totleLeftSide='';
            if(typeof data=="string"){
            	data=JSON.parse(data);
            }
            if(data.length!=0){
            	$.each(data,function(index,list){
                    if(list.children!=undefined){
                        leftSideHtml = '<li class="layui-nav-item"><i class="icon-sitemap"></i>'+
                        '<a href="javascript:;" data-url="">'+list.resourceName+'</a>'+
                        '<dl class="layui-nav-child">';
                        for(var i=0;i<list.children.length;i++){
                            leftSideHtmlSecond =  '<dd><a class="urlChange" href="javascript:;" data-url="'+list.children[i].resourceUrl+'">'+list.children[i].resourceName+'</a></dd>';
                            leftSideHtml +=leftSideHtmlSecond;
                        }
                        totleLeftSide =totleLeftSide + leftSideHtml+'</dl></li>';
                    }else{
                        leftSideHtml = '<li class="layui-nav-item" >'+
                        '<a href="javascript:;" data-url="'+list.resourceUrl+'" class="urlChange">'+list.resourceName+'</a>'+
                        '</li>';
                        totleLeftSide =totleLeftSide + leftSideHtml;
                    }
                    $(".layui-nav-tree").html(totleLeftSide);
                });
            	var liFirst = $(".layui-nav-tree>li:eq(0)");
                liFirst.addClass("layui-nav-itemed");
                $("ul.layui-nav-tree").on("click","a.urlChange",function(){
                    var changeUrl = $(this).attr("data-url");
                    if(changeUrl!=undefined){
                        $("#mainIframe").attr("src",contextPath+changeUrl);
                        var secondHtml=$(this).text();
                    	var firstHtml=$(this).parent().parent().prev().text();
                        $("#changeIframeTitle").val(firstHtml+' / '+secondHtml);
                    }
                });
                if(!liFirst.children("a").attr("data-url")){
                    liFirst.find("dl>dd:eq(0)").addClass("layui-this").children("a").click();
                }else{
                    liFirst.addClass("layui-this").children("a").click();
                }
                //折叠二级菜单事件：
                $(".leftTree+i").click(function(){
                	if($(this).hasClass("foldNav")){
                		$(this).html("&#xe65b;").removeClass("foldNav");
                		$(".layui-side").animate({'width':'40px'},200,function(){
                			$(".layui-side-scroll").width("40px");
                			$(".leftTree").width("40px");
                		});
                		$(".layui-body").animate({'left':'40px'},200);
                	}else{
                		$(this).html("&#xe65a;").addClass("foldNav");
                		$(".layui-side").animate({'width':'200px'},200,function(){
                			$(".layui-side-scroll").width("220px");
                			$(".leftTree").width("200px");
                		});
                		$(".layui-body").animate({'left':'200px'},200)
                	}
                });
                layui.use(['element','layer'], function(){
                    var element = layui.element;
                    var $ = layui.$;
                    /*element.on('nav(test)', function(elem){
                    	var changeUrl = $(elem).children().attr("data-url");
                    	$("#mainIframe").attr("src",contextPath+changeUrl);
                    	var secondHtml=$(elem).children().text();
                    	var firstHtml=$(elem).parent().prev().text();
                    	console.log( $("#mainIframe").contents().find(".iframeTitle>p>b").length);
                    	$("#mainIframe").contents().find(".iframeTitle>p>b").html(1);
                    });*/
                });
            }  
        }
    });
}