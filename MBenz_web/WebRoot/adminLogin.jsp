<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.fangchehome.util.Struts2Utils" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
	<title>拇指市场运维系统</title>
	<link rel="shortcut icon" href="images/favicon.ico" />
	<link rel="bookmark" href="images/favicon.ico" />
	
	<style type="text/css">    
	body {
		background-image:url(images/body.png);
		width: 100%;
		height: 100%;
		background-repeat: repeat-x;
		font-size: 20px;
		margin-left: 0px;
		margin-top: 10px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	table {
		font-weight:bold;
	}
	input {
		font-size: 20px;
	}
    </style>
	<script type="text/javascript">
		window.onload = function(){
			document.getElementById('username').focus();
		};
	</script>
</head>
<body>
<form id="loginForm" name="loginForm" action="${ctx}/login!login.action" method="post" style="margin-top: 30px;">
<table align="center" border="0" cellpadding="0" cellspacing="0">
	<tr><td height="50"><%if(request.getParameter("mes") != null){%>&nbsp;&nbsp;&nbsp;&nbsp;<i style="color: red">用户名或密码错误!!</i><%} %>&nbsp;</td></tr>
	<tr>
		<td height="192" width="387" style="background:url(${ctx}/images/frame.png) center no-repeat;">
			<table border="0" cellpadding="5" cellspacing="0" style="margin-top: 20px;margin-left: 80px">
				<tr>
					<td>
						<input maxlength="20" value="" type="text" class="text medium" name="username" id="username" tabindex="1" onblur="this.className='header_searchinput';" onfocus="this.className='header_searchclick'" style="width: 120px; height: 28px;" />
					</td>
				</tr>
				<tr>
					<td>
						<input  maxlength="20"  value="" type="password" class="text medium" name="password" id="password" tabindex="2" onblur="this.className='header_searchinput';" onfocus="this.className='header_searchclick'" style="width:120px;height:28px" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="image" src="${ctx}/images/LOGIN.png" onfocus="this.blur()" onclick="javascript:document.loginForm.submit()"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>