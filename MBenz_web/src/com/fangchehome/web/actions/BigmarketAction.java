package com.fangchehome.web.actions;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts2.ServletActionContext;

import com.fangchehome.hibernate.HibernateCommonUtil;

public class BigmarketAction extends Action{
	
	private String pkid;//pkid
	private String pid;//产品id
	
	public static void printdata(String http) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json,charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			response.sendRedirect(http);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//下载地址请求
	public void download(){
		String https = "http://122.225.203.70:18191/";
		try{
			String sql = "SELECT path_file FROM each_market.base_product_version WHERE id = 21612";
			List<Map> urlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(sql);
			String url = urlist.get(0).get("path_file").toString();
			System.out.println(https+url);
			this.printdata(https+url);
		}catch(Exception e){
			e.printStackTrace();
			this.printdata(https);
		}
		
	}

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
}

