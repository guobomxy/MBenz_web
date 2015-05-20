package com.fangchehome.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;


public class ContextListener extends HttpServlet implements
		ServletContextListener {

	private static final long serialVersionUID = 1L;
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
	private static final long PERIOD_HOUR = 60 * 60 * 1000;
	
	public ContextListener() { 
		
	} 

	private java.util.Timer timer = null; 
	private java.util.Timer timer1 = null; 
	private java.util.Timer timer2 = null;
	
	private java.util.Timer checkProduct_timer = null; 
	
	public void contextInitialized(ServletContextEvent event) {
		timer = new java.util.Timer(true);
		timer1 = new java.util.Timer(true);
		timer2 = new java.util.Timer(true);
		
		checkProduct_timer = new java.util.Timer(true);
		
		event.getServletContext().log("定时器已启动......");

		//项目启动加载PUSH
		try{
//			UmengPush u=new UmengPush();
//			u.push();
		}catch (Exception e) {
			e.printStackTrace();
		}
		//加载百度PUSH
		try{
//			BaiduPush u=new BaiduPush();
//			u.push();
		}catch (Exception e) {
			e.printStackTrace();
		}
		//加载去哪友盟PUSH
//		try{
//			GoWhereUmengPush u=new GoWhereUmengPush();
//			u.push();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//统计任务----->00:50:30
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		calendar.set(Calendar.MINUTE, 29);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		if (date.before(new Date())) {
			date = this.addDay(date, 1);
		}
//		timer.schedule(new MyTask(event.getServletContext()), date,PERIOD_DAY);
		
		//定时任务1 xjw 检查360搜索库和本地是否一致
		/*Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 2);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		Date dt = c.getTime();
		if (dt.before(new Date())) { 
			dt = this.addDay(dt, 1);
		}
		timer1.schedule(new MyCheckTask(event.getServletContext()), dt,PERIOD_DAY);*/
		
		//定时任务2 xjw 检查各市场数据库中包大小和下载请求包大小是否一致
		/*Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.MINUTE,10);
		Date dt2 = c2.getTime();
		timer2.schedule(new EachCheckTask(event.getServletContext()), dt2,2 * PERIOD_HOUR);*/
		
		//任务检查主版本包大小与服务器一致性
		/*Calendar checkProduct_c = Calendar.getInstance();
		checkProduct_c.set(Calendar.MINUTE, 10);
		Date checkProduct_d = checkProduct_c.getTime();
		checkProduct_timer.schedule(new CheckProductTask(event.getServletContext()), checkProduct_d,5 * PERIOD_HOUR);
		event.getServletContext().log("检验产品任务添加成功......");*/
	}

	public void contextDestroyed(ServletContextEvent event) {
		if(timer!=null)
			timer.cancel();
		if(timer1!=null)
			timer1.cancel();
		if(timer2!=null)
			timer2.cancel();
		if(checkProduct_timer!=null)
			checkProduct_timer.cancel();
		event.getServletContext().log("定时器已销毁......");
	}
	
	// 增加或减少天数
	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

}
