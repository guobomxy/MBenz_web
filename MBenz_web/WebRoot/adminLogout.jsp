<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%
if (request.getSession(false) != null) {
    session.invalidate();
}
if(request.getParameter("mes") != null){
	%>
	<c:redirect url="/adminLogin.jsp"/>
	<%
}else{
	%>
	<c:redirect url="/adminLogin.jsp?mes=1"/>
	<%
}
%>
