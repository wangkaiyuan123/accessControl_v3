/**
 * Created by wuxinmeng on 2017/10/17.
 */
;(function($){
    $.fn.extend({
        "createModal":function(options){
            ////配置默认设置：
            var  defaultSettings = {
                modalHeight:'300',
                modalWidth:'500',
                title:'提示',
                skin:'green',
                url:'',
                parentContent:'',
                data:'',//数据信息
                btnSelect:[{"title":"确定"},{"title":"取消"}],
                sec:false,//默认没有第二层modal,
                closed:function(){}
            };
            var opt = $.extend(defaultSettings,options);
            return this.each(function(){
                //插件主要代码
                var modalHeight = opt.modalHeight;
                var modalContentHeight = opt.modalHeight-50-40 ;
                var modalMarginTop = parseInt(-opt.modalHeight/2);
                var modalWidth = opt.modalWidth;
                var modalMarginLeft = parseInt(-opt.modalWidth/2);
                var title = opt.title;
                var skin = opt.skin;
                var btnSelect = opt.btnSelect;
                if(btnSelect.length == 2){
                    var btnConfig = '<button class="layui-btn layui-btn-small" lay-submit="" lay-filter="demo1" id="submitForm">'+btnSelect[0].title+'</button>'+
                                    '<button class="modal_close layui-btn layui-btn-primary layui-btn-small">'+btnSelect[1].title+'</button>';
                }else if(btnSelect.length == 1){
                    var btnConfig ='<button class="layui-btn layui-btn-small">知道了</button>';
                }else{
                    modalContentHeight = opt.modalHeight-40 ;
                    var btnConfig=""
                }
                if(opt.data ==''){
                    var url = opt.url;
                }else{
                    var url = opt.url +"?formData="+opt.data;
                }
                var modalConfig = '<div class="modal" style="margin-top:'+modalMarginTop+'+px;margin-left:'+modalMarginLeft+'px;width:'+modalWidth+'px">'+
                                        '<div class="modal_title">'+
                                            '<p>'+title+'</p>'+
                                            '<i class="layui-icon modal_close">&#x1006;</i>'+
                                        '</div>'+
                                        '<div class="modal_content" style="height:'+modalContentHeight+'px;">'+
                                            '<iframe class="iframeModal" style="padding:10px;box-sizing:border-box;" width="100%" height="100%" frameborder="0" scrolling="none" src='+url+'></iframe>'+
                                        '</div>'+
                                        '<div class="modal_footer">'+btnConfig+'</div>'+
                                    '</div>';
                $("body",parent.document).append(modalConfig);
                if(opt.btnSelect){
                	
                }
                if(opt.sec){
                	$(".modal",parent.document).eq(1).css("zIndex",1100);
                	var btnConfig='<button class="layui-btn layui-btn-small radioModal modal_close" type="button">确定</button>';
                	$(".modal",parent.document).eq(1).children(".modal_footer").html(btnConfig);
                }
                init();
                function init(){
                    openModal();
                }
                //打开模态框动画：
                function openModal(){
                    if(opt.sec){
                    	$(".modal",parent.document).eq(1).animate({height:modalHeight+"px",marginTop:(-modalHeight/2)+"px"},modalHeight);
                    	$(".modalShadowSec",parent.document).css("display","block");
                    }else{
                    	$(".modal",parent.document).animate({height:modalHeight+"px",marginTop:(-modalHeight/2)+"px"},modalHeight);
                        $(".modalShadow",parent.document).css("display","block");
                    }
                }
                //关闭模态框动画：
                function closeModal(){

                }
                $(".modal_close",parent.document).click(function(){
                	if(opt.sec){
                		$(".modal",parent.document).eq(1).animate({height:0,marginTop:0},modalHeight,function(){
                            $(".modalShadowSec",parent.document).css("display","none");
                            $(this).remove();
                            opt.closed();
                        });
                	}else{
                		$(".modal",parent.document).animate({height:0,marginTop:0},modalHeight,function(){
                            $(".modalShadow",parent.document).css("display","none");
                            $(this).remove();
                            opt.closed();
                        });
                	} 
                });
            })
        }
    })
})(jQuery);