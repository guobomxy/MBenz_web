package com.fangchehome.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class LoginFilter implements javax.servlet.Filter{
	
	public void destroy(){
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)arg0;
		HttpServletResponse res = (HttpServletResponse)arg1;
		HttpSession session = req.getSession();
		
		String url = req.getRequestURL().toString();
		
		if(session.getAttribute("logininfo") == null){
			if(url.indexOf("login") < 0) {
				PrintWriter out = res.getWriter();   
                out.print("<script>window.top.location.href = '" + req.getContextPath() + "/login.jsp';</script>");
			} else {
				arg2.doFilter(arg0, arg1);
			}
			return;
		}else{
			arg2.doFilter(arg0, arg1);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
