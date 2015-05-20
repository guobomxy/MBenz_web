package com.fangchehome.util;

import java.util.List;
import java.util.Map;

import org.hibernate.JDBCException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.fangchehome.exception.StatException;
import com.fangchehome.hibernate.HibernateUtil;


/**
 * 根据IMEI串号查询，返回List
 * @author hmilo029
 *
 */
public class SearchList {
	
	SQLSearch sqlsearch = new SQLSearch();
	
	/**
	 * 检查数据库中是否已存在该数据,过滤字段(日期-count_date,省-province,市-city,业务代码-gamecode,业务名称-gamename,渠道代码-company_code) 注:因为渠道代码与渠道名称是一对一的关系,所以只判断一个即可
	 * @param 
	 * @return
	 */
	public List getShipmentList(Map dbmap,Session session) {
		StringBuffer strSql = new StringBuffer("select * from game_mobilebase_data where count_date = '" + dbmap.get("count_date").toString() + "' and province = '"+String.valueOf(dbmap.get("province"))+"' and city = '"+String.valueOf(dbmap.get("city"))+"' and gamecode = '"+String.valueOf(dbmap.get("gamecode"))+"' and gamename = '"+String.valueOf(dbmap.get("gamename"))+"' and channel_code = '"+String.valueOf(dbmap.get("channel_code"))+"'");
		List list = null;
		try {
			SQLQuery q = session.createSQLQuery(strSql.toString());			
			q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = q.list();
		} catch (JDBCException ex) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			throw new StatException(ex.getSQLException());
		} 
		return list;
	}
	
	/**
	 * 返回销售情况
	 * @param strIMEI
	 * @return
	 */
	public List getSaleList(String strIMEI) {
		StringBuffer strSql = new StringBuffer("select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI and sale.FIMEI = '" + strIMEI + "'");
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 返回城市列表
	 * @param intProvinceFID
	 * @return
	 */
	public List getCityList(int intProvinceFID) {
		StringBuffer strSql = new StringBuffer("select * from tsale_city where FPROVINCEID = " + intProvinceFID);
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 重载，返回城市列表，根据省份名称
	 * @param strProvince
	 * @return
	 */
	public List getCityList(String strProvince) {
		StringBuffer strSql = new StringBuffer("select city.FNAME from tsale_city city, tsale_province province where city.FPROVINCEID = province.FID and instr(province.FNAME, '" + strProvince + "') > 0");
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	
	/**
	 * 返回地区汇总情况
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZArea(String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select sale.FPROVINCE,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		sql.append(" and sale.FREGDATE >= '" + startDate + "' and sale.FREGDATE <= '" + endDate + "') group by sale.FPROVINCE");
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 根据时间范围返回机型出货汇总情况
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZModelShipment(String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select FMODEL,count(*) COUNTNUM from tsale_phonesalelog");
		if(startDate.equals(endDate)) {
			sql.append(" where FDATE = '" + endDate + "' group by FMODEL");
		} else {
			sql.append(" where FDATE >= '" + startDate + "' and FDATE <= '" + endDate + "' group by FMODEL");
		}
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 根据机型、时间范围获取出货汇总情况
	 * @param strModel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZModelShipment(String strModel, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select FMODEL,count(*) COUNTNUM from tsale_phonesalelog");
		String strMidSql = "";
		if(!"allModel".equals(strModel)) {
			strMidSql += " and FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		
		if(startDate.equals(endDate)) {
			sql.append(" where FDATE = '" + endDate + "' group by FMODEL");
		} else {
			sql.append(" where FDATE >= '" + startDate + "' and FDATE <= '" + endDate + "' group by FMODEL");
		}
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 根据机型、颜色、时间范围获取出货汇总情况
	 * @param strModel
	 * @param strColor
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZModelShipment(String strModel, String strColor, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select FMODEL,count(*) COUNTNUM from tsale_phonesalelog");
		String strMidSql = "";
		if(!"allModel".equals(strModel)) {
			strMidSql += " and FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		
		if(startDate.equals(endDate)) {
			sql.append(" where FDATE = '" + endDate + "'" + strMidSql + " group by FMODEL");
		} else {
			sql.append(" where FDATE >= '" + startDate + "' and FDATE <= '" + endDate + "'" + strMidSql + " group by FMODEL");
		}
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 返回机型销售汇总情况
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZModelSale(String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select sale.FMODEL,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		sql.append(" and sale.FREGDATE >= '" + startDate + "' and sale.FREGDATE <= '" + endDate + "' group by sale.FMODEL");
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 根据机型、时间范围获取销售汇总情况
	 * @param strModel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZModelSale(String strModel, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select sale.FMODEL,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"allModel".equals(strModel)) {
			strMidSql = " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql = "";
		}
		sql.append(" and sale.FREGDATE >= '" + startDate + "' and sale.FREGDATE <= '" + endDate + "'" + strMidSql + " group by sale.FMODEL");
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 根据机型、颜色、时间范围获取销售汇总情况
	 * @param strModel
	 * @param strColor
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getHZModelSale(String strModel, String strColor, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer("select sale.FMODEL,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"allModel".equals(strModel)) {
			strMidSql = " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql = "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and shipment.FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		sql.append(" and sale.FREGDATE >= '" + startDate + "' and sale.FREGDATE <= '" + endDate + "'" + strMidSql + " group by sale.FMODEL");
		return HibernateUtil.getSQL2MapList(sql.toString());		
	}
	
	/**
	 * 得到出货总数
	 * @return
	 */
	public List getShipmentTotalNum() {
		String strSql = "";
		strSql = "select * from tsale_phonesalelog";
		return HibernateUtil.getSQL2MapList(strSql);
	}
		
	/**
	 * 根据省、时间范围等获取出货总数
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getShipmentTotalNum(String strProvince, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select * from tsale_phonesalelog");
		
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and instr(FPROVINCE, '" + strProvince.substring(0, 2) + "') > 0";
		} else {
			strMidSql = "";
		}
		
		if(strStart.equals(strEnd)) {
			strSql.append(" where FDATE = '" + strEnd + "'" + strMidSql);
		} else {
			strSql.append(" where FDATE >= '" + strStart + "' and FDATE <= '" + strEnd + "'" + strMidSql);
		}
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 根据机型、时间范围获取出货总数
	 * @param strModel
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getModelShipmentTotalNum(String strModel, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select * from tsale_phonesalelog");
		
		String strMidSql = "";
		if(!"allModel".equals(strModel)) {
			strMidSql = " and FMODEL = '" + strModel + "'";
		} else {
			strMidSql = "";
		}
		
		if(strStart.equals(strEnd)) {
			strSql.append(" where FDATE = '" + strEnd + "'" + strMidSql);
		} else {
			strSql.append(" where FDATE >= '" + strStart + "' and FDATE <= '" + strEnd + "'" + strMidSql);
		}
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 根据省、市、时间范围等获取出货总数
	 * @param strProvince
	 * @param strCity
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getShipmentTotalNum(String strProvince, String strCity, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select * from tsale_phonesalelog");
		
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and instr(FPROVINCE, '" + sqlsearch.replaceProvinceString(strProvince) + "') > 0";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and instr(FCITY, '" + sqlsearch.replaceCityString(strCity) + "') > 0";
		} else {
			strMidSql += "";
		}
		
		if(strStart.equals(strEnd)) {
			strSql.append(" where FDATE = '" + strEnd + "'" + strMidSql);
		} else {
			strSql.append(" where FDATE >= '" + strStart + "' and FDATE <= '" + strEnd + "'" + strMidSql);
		}
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 根据省、市、区、机型、颜色、时间范围等获取出货总数
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strModel
	 * @param strColor
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getShipmentTotalNum(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select * from tsale_phonesalelog");
		
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and instr(FPROVINCE, '" + sqlsearch.replaceProvinceString(strProvince) + "') > 0";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and instr(FCITY, '" + sqlsearch.replaceCityString(strCity) + "') > 0";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		if(!"allModel".equals(strModel)) {
			strMidSql += " and FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		
		if(strStart.equals(strEnd)) {
			strSql.append(" where FDATE = '" + strEnd + "'" + strMidSql);
		} else {
			strSql.append(" where FDATE >= '" + strStart + "' and FDATE <= '" + strEnd + "'" + strMidSql);
		}
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	
	/**
	 * 得到销售总数
	 * @return
	 */
	public List getSaleTotalNum() {
		String strSql = "";
		strSql = "select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 根据省、时间范围获取销售总数
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getSaleTotalNum(String strProvince, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		strSql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql);
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 根据机型、时间范围获取销售总数
	 * @param strModel
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getModelSaleTotalNum(String strModel, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		
		String strMidSql = "";
		if(!"allModel".equals(strModel)) {
			strMidSql = " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql = "";
		}
		strSql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql);
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 根据省、市、时间范围获取销售总数
	 * @param strProvince
	 * @param strCity
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getSaleTotalNum(String strProvince, String strCity, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and sale.FCITY = '" + strCity + "'";
		} else {
			strMidSql += "";
		}
		strSql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql);
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 根据省、市、区、机型、颜色、时间范围获取销售总数
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getSaleTotalNum(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and sale.FCITY = '" + strCity + "'";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(sale.FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		
		if(!"allModel".equals(strModel)) {
			strMidSql += " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and shipment.FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		strSql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql);
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
		
	/**
	 * 根据手机型号得到出货省份集合
	 * @param strModel
	 * @return
	 */
	public List getModelShipmentProvince(String strModel) {
		String strSql = "";
		strSql = "select FPROVINCE, count(*) COUNTNUM from tsale_phonesalelog where FMODEL = '" + strModel + "' group by FPROVINCE";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 根据手机型号得到出货地的集合
	 * @param strProvince 省
	 * @param strCity  城市
	 * @param strArea  地区
	 * @param strModel 机型
	 * @param strColor 颜色
	 * @param strStart 开始日期
	 * @param strEnd   结束日期
	 * @param strField 数据库字段名
	 * @return
	 */
	public List getModelShipmentProvince(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd, String strField) {
		StringBuffer sql = new StringBuffer("select " + strField + ",count(*) COUNTNUM from tsale_phonesalelog ");
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and instr(FPROVINCE, '" + sqlsearch.replaceProvinceString(strProvince) + "') > 0";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and instr(FCITY, '" + sqlsearch.replaceCityString(strCity) + "') > 0";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		if(!"allModel".equals(strModel)) {
			strMidSql += " and FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		
		if(strStart.equals(strEnd)) {
			sql.append(" where FDATE = '" + strEnd + "' " + strMidSql + " group by " + strField);
		} else {
			sql.append(" where FDATE >= '" + strStart + "' and FDATE <= '" + strEnd + "' " + strMidSql + " group by " + strField);
		}
		return HibernateUtil.getSQL2MapList(sql.toString());
	}
	
	/**
	 * 根据手机型号得到销售省份集合
	 * @param strModel
	 * @return
	 */
	public List getModelSaleProvince(String strModel) {
		String strSql = "";
		strSql = "select sale.FPROVINCE,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI and sale.FMODEL = '" + strModel + "' group by sale.FPROVINCE";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 根据手机型号得到销售地集合
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strModel
	 * @param strColor
	 * @param strStart
	 * @param strEnd
	 * @param strField
	 * @return
	 */
	public List getModelSaleProvince(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd, String strField) {
		StringBuffer sql = new StringBuffer("select sale." + strField + ",count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and sale.FCITY = '" + strCity + "'";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(sale.FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		
		if(!"allModel".equals(strModel)) {
			strMidSql += " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and shipment.FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		sql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql + " group by sale." + strField);
		return HibernateUtil.getSQL2MapList(sql.toString());
	}
	
	/**
	 * 显示某一个省份的窜货详情
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strModel
	 * @param strColor
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getModelCuanhuoOneProvince(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd) {
		StringBuffer sql = new StringBuffer("select sale.*, shipment.FCOLOR from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and sale.FCITY = '" + strCity + "'";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(sale.FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		
		if(!"allModel".equals(strModel)) {
			strMidSql += " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and shipment.FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		sql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql);
		return HibernateUtil.getSQL2MapList(sql.toString());
	}
	
	/**
	 * 获取出货表中所有的IMEI串号
	 * @return
	 */
	public List getAllIMEI() {
		String strSql = "";
		strSql = "select FIMEI from tsale_phonesalelog group by FIMEI";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 根据手机型号得到IMEI串号集合
	 * @param strModel
	 * @return
	 */
	public List getModelIMEI(String strModel) {
		String strSql = "";
		strSql = "select FIMEI from tsale_phonesalelog where FMODEL = '" + strModel + "' group by FIMEI";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 从手机用户注册数据表中Group by省份列表
	 * @return
	 */
	public List getProvinceList() {
		String strSql = "";
		strSql = "select FPROVINCE from tsale_userinfo group by FPROVINCE";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 获取全国省份列表
	 * @return
	 */
	public List getAllProvinceList() {
		String strSql = "";
		strSql = "select * from tsale_province order by FID";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 获取所有年龄值
	 * @return
	 */
	public List getAllAgeList() {
		String strSql = "";
		strSql = "select FAGE, count(*) COUNTNUM from tsale_userinfo group by FAGE";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 根据省、市、地区、机型、颜色、时间范围获取所有年龄值
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strModel
	 * @param strColor
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getAllAgeList(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select sale.FAGE,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and sale.FCITY = '" + strCity + "'";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(sale.FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		if(!"allModel".equals(strModel)) {
			strMidSql += " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		if(!"allColor".equals(strColor)) {
			strMidSql += " and shipment.FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		strSql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql + " group by sale.FAGE");
		
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 获取所有职业值
	 * @return
	 */
	public List getAllJobList() {
		String strSql = "";
		strSql = "select FJOB, count(*) COUNTNUM from tsale_userinfo group by FJOB";
		return HibernateUtil.getSQL2MapList(strSql);
	}
	
	/**
	 * 根据省、市、地区、机型、颜色、时间范围获取所有职业值
	 * @param strProvince
	 * @param strCity
	 * @param strArea
	 * @param strModel
	 * @param strColor
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public List getAllJobList(String strProvince, String strCity, String strArea, String strModel, String strColor, String strStart, String strEnd) {
		StringBuffer strSql = new StringBuffer("select sale.FJOB,count(*) COUNTNUM from tsale_userinfo sale, tsale_phonesalelog shipment where shipment.FIMEI = sale.FIMEI");
		String strMidSql = "";
		if(!"-选择省份-".equals(strProvince)) {	//不限省份
			strMidSql = " and sale.FPROVINCE = '" + strProvince + "'";
		} else {
			strMidSql = "";
		}
		if(!"--不限--".equals(strCity)) {	//不限城市
			strMidSql += " and sale.FCITY = '" + strCity + "'";
		} else {
			strMidSql += "";
		}
		if(!"allArea".equals(strArea)) {	//不限地区
			strMidSql += " and instr(sale.FAREA, '" + strArea + "') > 0";
		} else {
			strMidSql += "";
		}
		if(!"allModel".equals(strModel)) {
			strMidSql += " and sale.FMODEL = '" + strModel + "'";
		} else {
			strMidSql += "";
		}
		
		if(!"allColor".equals(strColor)) {
			strMidSql += " and shipment.FCOLOR = '" + strColor + "'";
		} else {
			strMidSql += "";
		}
		strSql.append(" and sale.FREGDATE >= '" + strStart + "' and sale.FREGDATE <= '" + strEnd + "'" + strMidSql + " group by sale.FJOB");
		
		return HibernateUtil.getSQL2MapList(strSql.toString());
	}
	
	/**
	 * 获取所有手机型号
	 * @return
	 */
	public List getAllMODEL() {
		String strSql = "";
		strSql = "select FMODEL from tsale_phonesalelog group by FMODEL";
		return HibernateUtil.getSQL2MapList(strSql);
	}

}
