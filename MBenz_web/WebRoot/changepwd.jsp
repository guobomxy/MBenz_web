<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">    
    <title>修改密码</title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script>
	function checkForm(){
		var  ol = document.getElementById("oldpwd").value;
		var  ne = document.getElementById("newpwd").value;
		var  ce = document.getElementById("checkpwd").value;
		
		var m1 = document.getElementById("msg").innerHTML;
		var m2 = document.getElementById("msg1").innerHTML;
		if(ne!=""&& ce!=""){
			if(m1==""&&m2==""){
				document.getElementById("cpwdForm").submit();
			}else{
				alert("请检查！");
			}
		}
	}
	function compare(){
		var  ol = document.getElementById("oldpwd").value;
		var  ne = document.getElementById("newpwd").value;
		var  ce = document.getElementById("checkpwd").value;
		var p = <%=session.getAttribute("ypassword")%>;
		if(ol!=p){
			document.getElementById("msg").innerHTML="原密码输入有误！";
		}else{
			document.getElementById("msg").innerHTML="";
		}
		if(ne!=ce){
			document.getElementById("msg1").innerHTML="输入的密码不一致！";
		}else{
			document.getElementById("msg1").innerHTML="";
		}
	}
	</script>
</head>
<body>
    <div align="center" style="font-size: 16px;">
    <div style="margin-top:250px;margin-left:-100px;">
    	<form id="cpwdForm" name="cpwdForm" action="login!changepwd.action" method="post">
    	<table>
    	<tr><td>原密码：</td><td><input type="password"  autocomplete="off" name="oldpwd" id="oldpwd" onblur="compare()"/>&nbsp;&nbsp;<span id="msg" style="color:red"></span></td></tr>
    	<tr><td>新密码：</td><td><input type="password" name="newpwd" id="newpwd"/></td></tr>
    	<tr><td>确认新密码：</td><td><input type="password" name="checkpwd" id="checkpwd" onblur="compare()" />&nbsp;&nbsp;<span id="msg1" style="color:red" ></span></td></tr>
    	</table>
         <input type="button" value="确认" onclick="checkForm()">
         </form>
    </div>
    </div>
</body>
</html>