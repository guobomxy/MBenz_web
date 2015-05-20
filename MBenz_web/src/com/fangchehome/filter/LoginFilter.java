package com.fangchehome.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter extends HttpServlet implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		   RequestDispatcher dispatcher = request.getRequestDispatcher("adminLogin.jsp");
		   HttpServletRequest req =(HttpServletRequest)request;
		   HttpServletResponse res =(HttpServletResponse)response;
		   HttpSession session =req.getSession(true);
		   //从session 里面获取用户名的信息
		   String user =(String)session.getAttribute("username");
		   //判断如果没有取到用户信息，就跳转到登陆页面，提示用户进行登陆
		   if(user == null || "".equals(user)){
		//跳转到登陆的页面，进行用户登录
		   dispatcher.forward(request,response);
		   }else{
		  }
		  chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
