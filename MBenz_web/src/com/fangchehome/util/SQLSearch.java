package com.fangchehome.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fangchehome.hibernate.HibernateCommonUtil;
import com.fangchehome.hibernate.HibernateUtil;



public class SQLSearch {	
	
	/**
	 * 返回手机注册总人数
	 * @return
	 */
	public int getRegUserNum() {
		int regInt = 0;
		String strSql = "";
		strSql = "select sale.* from tsale_userinfo sale";
		return HibernateUtil.getSQL2MapList(strSql).size();
	}
	
	/**
	 * 返回指定日期范围内的手机注册人数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getRegUserNum(String startDate, String endDate, String dateflag) {
		String strSql = "";
		if("regdate".equals(dateflag)) {
			strSql = "select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI and sale.FREGDATE >= '" + startDate + "' and sale.FREGDATE <= '" + endDate + "'";
		} else if("saledate".equals(dateflag)) {
			if(startDate.equals(endDate)) {
				strSql = "select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI and sale.FDATE = '" + startDate + "'";
			} else {
				strSql = "select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI and sale.FDATE >= '" + startDate + "' and sale.FDATE <= '" + endDate + "'";
			}
		}
		return HibernateUtil.getSQL2MapList(strSql).size();
	}
	
	/**
	 * 字符串编码转换
	 * @param strOrg
	 * @return
	 */
	public String convertCharset(String strOrg) {
		String strRs = "";
		try {
			strRs = new String(strOrg.getBytes("ISO8859-1"),"GB18030");
		} catch(Exception e) {
			e.printStackTrace();
		}	
		
		return strRs;
	}
	
	/**
	 * 终端打印的字符编码，采用JVM默认编码
	 * @param strOrg
	 * @return
	 */
	public String convertConsoleCharset(String strOrg) {
		String strRs = "";
		try {
			strRs = new String(strOrg.getBytes("GBK"));
		} catch(Exception e) {
			e.printStackTrace();
		}	
		
		return strRs;
	}
	
	/**
	 * 获取当前日期时间
	 * @return
	 */
	public String displayCurrentDateTime() {		  
		return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Calendar.getInstance().getTime());	//获取当前日期时间
	}
	
	/**
	 * 显示可作为文件名的时间日期
	 * @return
	 */
	public String displayformatDateTime() {		  
		return new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(java.util.Calendar.getInstance().getTime());	//获取当前日期时间
	}
	
	/**
	 * 转换字符串格式的日期时间为秒数
	 * @param strDateTime
	 * @return
	 */
	public long convertDateTimeToSecond(String strDateTime) {
		//转换日期时间为秒数，因是毫秒，所以需除以1000
		long sModifyTime = 0L;
		try {
			sModifyTime = ((Date)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDateTime)).getTime() / 1000;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return sModifyTime;
	}
	
	/**
	 * 转换秒数为日期时间
	 * @param longtime 秒数
	 * @return
	 */
	public String converSecondToDateTime(long longtime) {	
		String strDateTime = "";
		try {
			Date dat = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期时间格式
			longtime *= 1000;	//秒转换成毫秒
			dat.setTime(longtime);	//设置转换
			strDateTime = simpleDateFormat.format(dat);	//格式化输出
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strDateTime;
	}
	
	/**
	 * 替换掉省份表中省级名称中的"省"、"市"、"自治区"、"壮族"、"回族"、"维吾尔"、"特别行政区"
	 * @param strProvinceName
	 * @return
	 */
	public String replaceProvinceString(String strProvinceName) {
		String strRes = "";
		strRes = strProvinceName.replace("省", "").replace("市", "").replace("自治区", "").replace("壮族", "").replace("回族", "").replace("维吾尔", "").replace("特别行政区", "");
		return strRes;
	}
	
	/**
	 * 替换掉城市表中城市名称中的"市"、"特别行政区"、"林区"、"地区"、"自治州"、"蒙古族"、"傈傈族"、"景颇族"、"白族"、"傣族"、"哈尼族"、"壮族"、"土家族"、"苗族"、"回族"、"藏族"、"羌族"、"彝族"、"布依族"、"侗族"、"朝鲜族"
	 * @param strCityName
	 * @return
	 */
	public String replaceCityString(String strCityName) {
		String strRes = "";
		strRes = strCityName.replace("鸡市", "鸡城").replace("市", "").replace("特别行政区", "").replace("林区", "").replace("地区", "").replace("自治州", "").replace("蒙古族", "").replace("傈傈族", "").replace("景颇族", "").replace("白族", "").replace("傣族", "").replace("哈尼族", "").replace("壮族", "").replace("土家族", "").replace("苗族", "").replace("回族", "").replace("藏族", "").replace("羌族", "").replace("彝族", "").replace("布依族", "").replace("侗族", "").replace("朝鲜族", "").replace("鸡城", "鸡市");
		return strRes;
	}
	
	/**
	 * 根据KEY查询翻译词典中值(new_config表)
	 * @param strCityName
	 * @return
	 */
	public static Map<String,String> selectTranslate(String key) {
		String sqlNewType = "select cvalue,memo from zm_news.new_config where ckey='" + key + "'";
		List<Map> newTypeList = HibernateCommonUtil.getSQL2MapList(sqlNewType);
		Map<String,String> translateMap = new HashMap<String,String>();
		if(newTypeList != null && newTypeList.size()>0){
			for (Map map : newTypeList) {
				translateMap.put(String.valueOf(map.get("cvalue")), String.valueOf(map.get("memo")));
			}
		}
		return translateMap;
	}
	
}
