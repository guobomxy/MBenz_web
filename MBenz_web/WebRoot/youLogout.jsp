<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%
if (request.getSession(false) != null) {
    session.invalidate();
}
if(request.getParameter("mes") != null){
	%>
	<c:redirect url="/youLogin.htm"/>
	<%
}else{
	%>
	<c:redirect url="/youLogin.htm?mes=1"/>
	<%
}
%>
