package com.fangchehome.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

//获取四个段时间集合
public class TimeList {
	//flag(1.一周内;2.一个月内;3.二个月内)
	public static List<Map> getDateList(Integer count){//count 时间间隔
		List<Map> dateList = new ArrayList<Map>();
		//当前日期
		Date time = new Date();
		for(int i = count;i>0;i--){
			Calendar c = Calendar.getInstance();   
		    c.setTime(new Date());   //设置当前日期    
		    c.add(Calendar.DATE, -i); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)    
		    time = c.getTime(); //结果
		    Map map = new HashMap();
		    map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(time));
		    dateList.add(map);
		}
		return dateList;
	}
	
	//根据具体开始时间和结束时间
	public static List<Map> getDateListBytime(Date startTime,Date endTime){
		List<Map> dateList = new ArrayList<Map>();
		Date time = startTime;
		Map ma = new HashMap();
		ma.put("date", new SimpleDateFormat("yyyy-MM-dd").format(startTime));
		dateList.add(ma);
		while(endTime.compareTo(startTime)>0){
			Calendar c = Calendar.getInstance();   
			c.setTime(time);   //设置当前日期    
			c.add(Calendar.DATE, +1); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)    
			time = c.getTime(); //结果
			Map map = new HashMap();
			map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(time));
			dateList.add(map);
			if(time.compareTo(endTime)==0) break;
		}
		return dateList;
	}
	
	//根据具体开始月份和结束月份获取月份集合
	public static List<Map> getMonthListBytime(String startTime,String endTime){
		List<Map> dateList = new ArrayList<Map>();
		try {
			Date time = new SimpleDateFormat("yyyy-MM").parse(endTime);
			Map ma = new HashMap();
			ma.put("date", endTime);
			dateList.add(ma);
			while(endTime.compareTo(startTime)>0){
				Calendar c = Calendar.getInstance();   
				c.setTime(time);   //设置当前日期    
				c.add(Calendar.MONTH, -1); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)    
				time = c.getTime(); //结果
				Map map = new HashMap();
				map.put("date", new SimpleDateFormat("yyyy-MM").format(time));
				dateList.add(map);
				if(time.compareTo(new SimpleDateFormat("yyyy-MM").parse(startTime))==0) break;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateList;
	}
		
	public static void main(String args[]){
		
		Date dteDate = new Date();
		
		System.out.println(dteDate);
		
		List<Map> date = getMonthListBytime("2013-05","2014-01");
		System.out.println(date.size());
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//		try {
//			getDateListBytime(dateFormat.parse("2013-1-14"),
//					dateFormat.parse("2013-1-1"));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
	}
}
