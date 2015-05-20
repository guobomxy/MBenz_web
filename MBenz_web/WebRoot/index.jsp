<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String usr = session.getAttribute("username")==null?"":session.getAttribute("username").toString();
	String tree = "indexdata.js";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>拇指市场运维统计任务系统</title>
    <link href="js/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <style type="text/css"></style>
	<script src="js/jquery-1.8.0.js" type="text/javascript"></script>   
    <script src="js/base.js" type="text/javascript"></script>
    <script src="js/ligerLayout.js" type="text/javascript"></script>
    <script src="js/ligerTree.js" type="text/javascript"></script>
    <script src="js/data/<%=tree %>" type="text/javascript"></script>
    <script src="js/ligerui.min.js" type="text/javascript"></script> 
        <script type="text/javascript">
            var tab = null;
            var accordion = null;
            var tree = null;
            $(function ()
            {
                //布局
                $("#layout1").ligerLayout({ leftWidth: 230, height: '100%',heightDiff:-34,space:4, onHeightChanged: f_heightChanged });

                var height = $(".l-layout-center").height();

                //Tab
                $("#framecenter").ligerTab({ height: height });

                //面板
                $("#accordion1").ligerAccordion({ height: height - 24, speed: null });

                $(".l-link").hover(function ()
                {
                    $(this).addClass("l-link-over");
                }, function ()
                {
                    $(this).removeClass("l-link-over");
                });
                //树
                $("#tree1").ligerTree({
                    data : indexdata,
                    checkbox: false,
                    slide: false,
                    nodeWidth: 120,
                    attribute: ['nodename', 'url'],
                    onSelect: function (node)
                    {
                        if (!node.data.url) return;
                        var tabid = $(node.target).attr("tabid");
                        if (!tabid)
                        {
                            tabid = new Date().getTime();
                            $(node.target).attr("tabid", tabid);
                        } 
                        f_addTab(tabid, node.data.text, node.data.url);
                    }
                });

                tab = $("#framecenter").ligerGetTabManager();
                accordion = $("#accordion1").ligerGetAccordionManager();
                tree = $("#tree1").ligerGetTreeManager();
                $("#pageloading").hide();

            });
            function f_heightChanged(options)
            {
                if (tab)
                    tab.addHeight(options.diff);
                if (accordion && options.middleHeight - 24 > 0)
                    accordion.setHeight(options.middleHeight - 24);
            }
            function f_addTab(tabid,text, url)
            { 
                tab.addTabItem({ tabid : tabid,text: text, url: url });
            } 
             
            
     </script> 
<style type="text/css"> 
    body,html{height:100%;}
    body{ padding:0px; margin:0;   overflow:hidden;}  
    .l-link{ display:block; height:26px; line-height:26px; padding-left:10px; text-decoration:underline; color:#333;}
    .l-link2{text-decoration:underline; color:white; margin-left:2px;margin-right:2px;}
    .l-layout-top{background:#102A49; color:White;}
    .l-layout-bottom{ background:#E5EDEF; text-align:center;}
    #pageloading{position:absolute; left:0px; top:0px; background:white url('images/loading.gif') no-repeat center; width:100%; height:100%;z-index:99999;}
    .l-link{ display:block; line-height:22px; height:22px; padding-left:16px;border:1px solid white; margin:4px;}
    .l-link-over{ background:#FFEEAC; border:1px solid #DB9F00;} 
    .l-winbar{ background:#2B5A76; height:30px; position:absolute; left:0px; bottom:0px; width:100%; z-index:99999;}
    .space{ color:#E7E7E7;}
    /* 顶部 */ 
    .l-topmenu{ margin:0; padding:0; height:31px; line-height:31px; background:url('images/top.jpg') repeat-x bottom;  position:relative; border-top:1px solid #1D438B;  }
    .l-topmenu-logo{ color:#E7E7E7; padding-left:35px; line-height:26px;background:url('images/topicon.gif') no-repeat 10px 5px;}
    .l-topmenu-welcome{  position:absolute; height:24px; line-height:24px;  right:30px; top:2px;color:#070A0C;}
    .l-topmenu-welcome a{ color:#E7E7E7; text-decoration:underline} 

 </style>
</head>
<body style="margin: 0px; padding:0px;background:#EAEEF5;">  
<div id="pageloading"></div>

<div id="topmenu" class="l-topmenu" style="display: none">
    <div class="l-topmenu-logo">拇指市场运维统计任务系统</div>
</div>
  <div id="layout1" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
  		<div position="left" title="运维统计任务系统  <font color='red'><%=session.getAttribute("username") %></font>" id="accordion1"> 
                     <div title="统计任务树" class="l-scroll">
                         <ul id="tree1" style="margin-top:3px;">
                    </div>
        </div>
        <div position="center" id="framecenter"> 
            <div tabid="home" title="欢迎页" style="height:300px" >
                <iframe frameborder="0" name="home" id="home" src="welcome.jsp"></iframe>
            </div>           
        </div>        
    </div>
    <div style="text-align: left;margin-left: 20px;margin-top: 5px"><a href="<%=basePath%>/adminLogout.jsp?mes=2"><img src="images/close.gif" style="vertical-align: bottom" /><font size="+1">退出</font></a></div>
    <div  style="height:32px; line-height:32px; text-align:center;">
            Copyright &copy; 2012-<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %>&nbsp;&nbsp;<a href="http://www.miibeian.gov.cn/" target="_blank">浙ICP备09031305号</a>&nbsp;&nbsp;增值电信业务经营许可证：<a href="http://www.miibeian.gov.cn/" target="_blank">浙ICP证B2-20100385</a>&nbsp;&nbsp;浙网文[2012]0212-018号
    </div>
    <div style="display:none"></div>    
    <div id="maingrid"></div>
  <div style="display:none;">  
</div>
</body>
</html>