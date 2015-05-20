package com.fangchehome.web.actions;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.struts.action.Action;

import com.fangchehome.hibernate.HibernateUtil;
import com.fangchehome.util.SHA1;
import com.fangchehome.util.Struts2Utils;

public class LoginAction extends Action {
	private String username;
	private String password;
	
	public void login(){
		if(username == null || password == null){
			try {
				Struts2Utils.getRequest().getRequestDispatcher("adminLogout.jsp").forward(Struts2Utils.getRequest(),Struts2Utils.getResponse());
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		String pwd = new SHA1().getDigestOfString(password.getBytes()).toLowerCase();
		String sql = "SELECT id FROM ss_user WHERE login_name='"+username+"' AND PASSWORD ='"+pwd+"'";
		List<Map> userList = (List<Map>) HibernateUtil.getSQL2MapList(sql);
		try {
			if(userList.size()>0) {
				Struts2Utils.getSession().setAttribute("username", username);
//				Struts2Utils.getRequest().getRequestDispatcher("index.jsp").forward(Struts2Utils.getRequest(),Struts2Utils.getResponse());
				Struts2Utils.getResponse().sendRedirect("index.jsp");
				return;
			}else{
				try {
					Struts2Utils.getRequest().getRequestDispatcher("adminLogout.jsp").forward(Struts2Utils.getRequest(),Struts2Utils.getResponse());
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Struts2Utils.getResponse().sendRedirect("adminLogout.jsp");
				return;
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 转发到index.jsp
	}
	
	public void youlogin(){
		if(username == null || password == null){
			try {
				Struts2Utils.getRequest().getRequestDispatcher("youLogout.jsp").forward(Struts2Utils.getRequest(),Struts2Utils.getResponse());
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		String pwd = new SHA1().getDigestOfString(password.getBytes()).toLowerCase();
		String sql = "SELECT cpid,cpname FROM cp_user WHERE loginname='"+username+"' AND password ='"+pwd+"'";
		List<Map> userList = (List<Map>) HibernateUtil.getSQL2MapList(sql);
		String cpname="";
		try {
			if(userList.size()>0&&userList.size()==1) {
				for(Iterator it = userList.iterator(); it.hasNext();){
					Map<String,String> map = (Map) it.next();
					cpname = String.valueOf(map.get("cpname"));
				}
				Struts2Utils.getSession().setAttribute("username", cpname);
				Struts2Utils.getSession().setAttribute("ypassword", password);
//				Struts2Utils.getRequest().getRequestDispatcher("index.jsp").forward(Struts2Utils.getRequest(),Struts2Utils.getResponse());
				Struts2Utils.getResponse().sendRedirect("youindex.jsp");
				return;
			}else{
				try {
					Struts2Utils.getRequest().getRequestDispatcher("youLogout.jsp").forward(Struts2Utils.getRequest(),Struts2Utils.getResponse());
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Struts2Utils.getResponse().sendRedirect("adminLogout.jsp");
				return;
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 转发到index.jsp
	}
	
	
	private String newpwd;
	private String oldpwd;
	private String checkpwd;
	
	public void changepwd(){
		String username = (String)Struts2Utils.getSession().getAttribute("username");
		String passwd = new SHA1().getDigestOfString(newpwd.getBytes()).toLowerCase();
		String uql = "update cp_user set password = '"+passwd+"' where cpname = '"+username+"'";
		int u = HibernateUtil.sqlupdate(uql);
	}

	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewpwd() {
		return newpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	public String getOldpwd() {
		return oldpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public String getCheckpwd() {
		return checkpwd;
	}

	public void setCheckpwd(String checkpwd) {
		this.checkpwd = checkpwd;
	}
	
	
}
