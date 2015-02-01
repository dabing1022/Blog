#coding:utf-8

import urllib
import urllib2
import cookielib
import re
from bs4 import BeautifulSoup

html_doc = """



<!DOCTYPE HTML >
<!-- jstl 标签 -->


<!-- struts2 标签
taglib prefix="s" uri="/struts-tags"
 -->
 


<html id="html1">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!--ipad 兼容-->
<meta name="viewport" content"width=device-width; initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5,user-scalable=yes">
<meta name="MobileOptimized" content="960" >
<!-- <script src="http://cn.yimg.com/script/2012/0130/html5_v3.js"></script> -->
<!-- <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script> -->

<!--[if lte IE 8]>
    <script src="/sixtteacher/js/html5.js"></script>
<![endif]--> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
	
<title>智康教师系统</title>
<!-- 引入公共的js、css 如：jquery相关-->
<link href="/sixtteacher/css/style.css"  rel="stylesheet" type="text/css" />
<script src="/sixtteacher/js/jquery.min.js" language="javascript" type="text/javascript"></script>
<script src="/sixtteacher/js/public.js" language="javascript" type="text/javascript"></script>
<script src="/sixtteacher/js/base.js" language="javascript" type="text/javascript"></script>
<script src="/sixtteacher/js/thickbox_plus.js" language="javascript" type="text/javascript"></script>
<!-- 引入项目自定义css -->
<link href="/sixtteacher/css/sixtteacher.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript"	src="/sixtteacher/js/sixtteacher.js"></script>
<script type="text/javascript">
//<![CDATA[
var basePath = "/sixtteacher/";
var cachePath = "/sixtteacher/";
$.ajaxSetup({cache:false});
var async_token_static = '<s:property value="#session.asyncTokenId"/>';
window.onload = function() {
	if ($("input[name='async_token_static']").length > 0) {
		$("input[name='async_token_static']").attr("value", async_token_static);
	} 
	/*返回旧版和鼠标滚动特效*/
	var help_flag= $("#help_wrap");	//帮助按钮
    var av_height = $(window).height();
    var av_width = $(window).width();
    var go_top= $("#go_top");
    var Gotop_w = go_top.width()+2;
    var Gotop_h = go_top.height()+2;
    var doc_width = 1010;
    var Gotop_x = (av_width>doc_width?0.5*av_width+0.5*doc_width:av_width-Gotop_w)+ 20;
    var Gotop_y = av_height-Gotop_h;
    var ie6Hack = "<style>.go_top{position:absolute; top:expression(documentElement.scrollTop+documentElement.clientHeight - this.offsetHeight-40);</style>}";
    if ($.browser.msie && ($.browser.version == "6.0")){
        $("body").append(ie6Hack);
    }
    help_flag.css({
    	left:Gotop_x
    });
    function setGotop(){
        av_height = $(window).height();
        av_width = $(window).width();
        Gotop_y = av_height-Gotop_h-40;
        Gotop_x = (av_width>doc_width?0.5*av_width+0.5*doc_width:av_width-Gotop_w) + 20;
        if($(window).scrollTop()>0){
            go_top.fadeIn(200);
        }else{
            go_top.fadeOut(200);
        }
        if ($.browser.msie && ($.browser.version == "6.0")){
            go_top.animate({"left":Gotop_x},0);
            return false;
        }
        go_top.animate({"left":Gotop_x,"top":Gotop_y},0);
    }
     //返回旧版
    var backi= $("#backi");
    var ie6Hack1 = "<style>.backi{position:absolute; top:expression(documentElement.scrollTop+documentElement.clientHeight - this.offsetHeight-40);</style>}";
    if ($.browser.msie && ($.browser.version == "6.0")){
        $("body").append(ie6Hack1);
    }
    function setbacki(){
        av_height = $(window).height();
        av_width = $(window).width();
        Gotop_y = av_height-Gotop_h-87;
        Gotop_x = (av_width>doc_width?0.5*av_width+0.5*doc_width:av_width-Gotop_w);
        if ($.browser.msie && ($.browser.version == "6.0")){
            backi.animate({"left":Gotop_x},0);
            return false;
        }
        backi.animate({"left":Gotop_x,"top":Gotop_y},0);
    }
    setbacki();
    setGotop();
    $(window).resize(function(){
        setbacki();
        setGotop();
    });
    $(window).scroll(function(){
        setbacki();
        setGotop();
    });
     go_top.click(function(){
        $("html , body").animate({scrollTop:"0"},100);
    });

	//点击中学库关注按钮弹出层
	//$(".right_module").find(".ico_focus").click(function(){
		//$("#set_focus_box").show();
    //	});
	//中学库弹出层输入框效果
	$(".search_main").find(".search_input").focus(function(){
		if($(this).val() == "请输入中学名称/关键字"){
			$(this).val("");
		}else{
			$(this).css({
				"color": "#333" 
			});
		}
		$(this).css({
				"color": "#333" 
		});
	}).blur(function(){
		if($(this).val() == ""){
			$(this).val("请输入中学名称/关键字");
			$(this).css({
				"color": "#a5a5a5" 
			});
		}else{
			$(this).css({
				"color": "#333" 
			});
		}
	});
	
};

/**TIPS: 
0.提交数据时，在需要提交的FORM里加入名为'async_token_static'的hidden，或在请求中添加async_token_static参数
1.同步提交、页面载入或刷新时无需调用
2.异步提交需要在回调函数中手动调用
3.关闭子页面后 需要在父页面手动调用(如果父页面用到TOKEN)
*/


var sync_token_from_session = function (asyncTokenId) {
	async_token_static = asyncTokenId;
	$("input[name='async_token_static']").attr("value", asyncTokenId);
};


//]]>
</script>

	 <link href="/sixtteacher/jsp/lecture/FlexPaper/css/style.css" rel="stylesheet" type="text/css" />
	 <script src="/sixtteacher/jsp/lecture/FlexPaper/js/flexpaper_flash.js" type="text/javascript"></script> 
	 <script src="/sixtteacher/jsp/lecture/FlexPaper/js/flexpaper_flash_debug.js" type="text/javascript"></script>
	 <script src="/sixtteacher/jsp/lecture/FlexPaper/js/jquery.js" type="text/javascript"></script> 
	 <script src="/sixtteacher/jsp/lecture/FlexPaper/js/index.js" type="text/javascript"></script>
<!--[if lte IE 8]>
<script src="/sixtteacher/js/html5.js"></script>
<![endif]-->
</head>
<body id="body1">
   <!--讲义详情-->
<div class="crumb_cont clearfix">
	<div class="crumb_left">
		<div class="crumb_detail">
			<div class="detail_cont" style="display:block">
				<a id="viewerPlaceHolder" style="width:900px;height:800px;display:block"></a>
				<script type="text/javascript">
				var fp = new FlexPaperViewer(  
					'/sixtteacher/jsp/lecture/FlexPaper/FlexPaperViewer',    /* 对应FlexPaperViewer.swf文件*/  
					'viewerPlaceHolder', { config : {  
				//SwfFile : 'http://sharedir.zhikang.org/lecture/swf/f43dd9db-5f79-4b7b-a9bd-4c176ae480a2.swf',  
				
				SwfFile : 'http://sharedir.izhikang.com/lecture/swf/0f4f98e8-117d-4e16-a2c5-3457df638945.swf',
				Scale : 0.6,  
				ZoomTransition : 'easeOut',  
				ZoomTime : 0.5,  
				ZoomInterval : 0.2,  
				FitPageOnLoad : true,  
				FitWidthOnLoad : true,  
				FullScreenAsMaxWindow : false,  
				ProgressiveLoading : false,  
				MinZoomSize : 0.2,  
				MaxZoomSize : 5,  
				SearchMatchAll : false,  
				InitViewMode : 'Portrait',  
				ViewModeToolsVisible : false,  
				ZoomToolsVisible : false,  
				NavToolsVisible : true,  
				CursorToolsVisible : true,  
				SearchToolsVisible : true,  
				localeChain: 'zh_CN'  
					}});  
				</script>
			</div>
			<div class="detail_cont">
				<a id="viewerPlaceHolder2" style="width:900px;height:800px;display:block"></a>
				<script type="text/javascript">
				varfp = new FlexPaperViewer(  
					'/sixtteacher/jsp/lecture/FlexPaper/FlexPaper',    /* 对应FlexPaperViewer.swf文件*/  
					'viewerPlaceHolder2', { config : {  
						//SwfFile : 'http://sharedir.zhikang.org/lecture/swf/f43dd9db-5f79-4b7b-a9bd-4c176ae480a2.swf',  
						SwfFile : 'http://sharedir.izhikang.com/lecture/swf/25e63cce-eece-44fc-b1ae-78fa638c36b6.swf',
						Scale : 0.6,  
						ZoomTransition : 'easeOut',  
						ZoomTime : 0.5,  
						ZoomInterval : 0.2,  
						FitPageOnLoad : true,  
						FitWidthOnLoad : true,  
						FullScreenAsMaxWindow : false,  
						ProgressiveLoading : false,  
						MinZoomSize : 0.2,  
						MaxZoomSize : 5,  
						SearchMatchAll : false,  
						InitViewMode : 'Portrait',  
						ViewModeToolsVisible : false,  
						ZoomToolsVisible : false,  
						NavToolsVisible : true,  
						CursorToolsVisible : true,  
						SearchToolsVisible : true,  
						localeChain: 'zh_CN'  
					}});  
				</script>
			</div>
		</div>
	</div>
	<!--右侧信息-->
	<div class="crumb_right">
		<!--tab标签-->
		<div class="crumb_list tab_list">
           	<span>
                   <a href="#" class="current">学<br />生<br />版</a>
                   
                   <a href="#">教<br />师<br />版</a>
                   
                   
               </span>
               <b>
               	
				<a href="/sixtteacher/download!fromRemote.action?file=lecture/doc/0f4f98e8-117d-4e16-a2c5-3457df638945.doc&name=1 现代文．阅读延伸．分门别类立结构（一）（2014-2015）" class="ico_download">下载</a>
               </b>
           </div>
	</div>
</div>	
<!--加载js-->
<script type="text/javascript">
	$(function(){
		//tab标签切换
		$(".crumb_list span").find("a").each(function(index){
			$(this).click(function(){
				$(this).addClass("current").siblings().removeClass("current");
				$(".crumb_detail").find(".detail_cont").hide();
				$(".crumb_detail").find(".detail_cont").eq(index).show();
			});
		});
		//滚动时，判断层级固定
		$(window).scroll(function(){
			var scroll_amount = $(window).scrollTop();
			if(scroll_amount > 25){
				$(".crumb_tab").css({
					"position" : "fixed",
					"top" : "40px"
				});
				$(".crumb_fix ").css({
					"position" : "fixed",
					"top" : "40px"
				});
			}else{
				$(".crumb_tab, .crumb_fix ").css({
					"position" : "static"
				});
			}
		});
	});
</script>
</body>
</html>
"""

soup = BeautifulSoup(html_doc)
print soup.title
print soup.title.name
print soup.title.string

print soup.title.parent.name
print soup.a
print soup.find_all('a')

swfLinkPattern = re.compile('http://.+\.swf')
swfsLinks = swfLinkPattern.findall(html_doc)
for r in swfsLinks:
	print r
teacherSwfFilePath = swfsLinks[-1]

studentDocDownloadPath = soup.find_all('a')[-1].get('href')
print studentDocDownloadPath