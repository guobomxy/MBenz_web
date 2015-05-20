package com.fangchehome.execlimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fangchehome.hibernate.HibernateUtilCollect;
import com.fangchehome.util.Constants;
import com.fangchehome.util.SQLSearch;
import com.sun.jmx.snmp.Timestamp;

/**
 * 导入cp产品收益
 * 
 * @author
 * 
 */
public class ImportCPTEarningExcel {

	private SQLSearch search = new SQLSearch();
	private Constants constants = new Constants();
	private Integer cols = 0;// 当前表格列数;
	private String error  = "";
	private int col = 7;//该表格约定行数

	/**
	 * 写入数据库
	 * 
	 * @param strResourcePath
	 */
	public String opDatabase(String strResourcePath, String strLogPath,
			String strLoginUser) {
		// 开始逐行读取List
		Connection conn = null;
		List dblist = null;
		dblist = opFromExcel(strResourcePath);
		StringBuffer strflag = new StringBuffer(); // 成功与否、存在相同IMEI串号的标识

		Session session = HibernateUtilCollect.getSession();
		Transaction tx = session.beginTransaction();
		String errorflag = "";
		try {
			conn = session.connection();
			// PreparedStatement pstmt =
			// conn.prepareStatement("insert IGNORE into game_mobilebase_data(count_date,province,city,company_code,company_name,gamecode,gamename,channel_code,channel_name,day_sum_money,day_click_money,day_call_money,day_down_user,day_down_count,day_free_user,day_free_count,day_fee_user,day_fee_count,day_login_user,day_login_count,day_txtclick_user,day_txtclick_count,day_arpu,day_new_user,modify_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement pstmt = conn
					.prepareStatement("INSERT IGNORE INTO app_cptsoftearning(softid,softname,startdate,enddate,money,flag,place) VALUES(?,?,?,?,?,?,?)");
			PrintWriter writer = new PrintWriter(new FileWriter(strLogPath
					+ "/ImportCPTEarningExcel_" + search.displayformatDateTime()
					+ "_" + strLoginUser + constants.LOGFILEEXT));
			if (".html".equals(constants.LOGFILEEXT)) {
				writer.write("<html><head></head><body style=\"font-size: 12px; margin-top: 10px\"><fieldset><legend>导入投诉明细表</legend>");
			}
			/**
			 * 写入数据库前，需要在Excel的list中判断是否有数据已存在在数据库中
			 */
			if (dblist != null && dblist.size() > 0) {
				String strSql = ""; // 查询SQL
				List slist = null; // 查询list
				for (int sI = 0, logI = 0; sI < dblist.size(); sI++) {
					Map dbmap = (Map) dblist.get(sI);
					// 查询数据库中是否存在以条件为download_day`,`app_id`相符合的数据
					pstmt.setString(1,dbmap.get("softid").toString());
					pstmt.setString(2,dbmap.get("softname").toString());
					pstmt.setString(3,dbmap.get("startdate").toString());
					pstmt.setString(4, dbmap.get("enddate").toString());
					pstmt.setString(5, dbmap.get("money").toString());
					pstmt.setString(6, dbmap.get("flag").toString());
					pstmt.setString(7, dbmap.get("place").toString());
					pstmt.addBatch();
					/**
					 * 写入日志文件，以备查本次导入操作详情
					 */
					StringBuffer sbinfo = new StringBuffer();
					sbinfo.append(str(dbmap));
					if (".html".equals(constants.LOGFILEEXT)) {
						writer.write((++logI) + " : " + sbinfo.toString()
								+ "<br />");
					} else {
						writer.write((++logI) + " : " + sbinfo.toString()
								+ "\r\n");
					}
					strflag.append(dbmap.get("softid").toString() + ",");
				}
				int[] result = pstmt.executeBatch();
				// 冠军说不做异常处理,即使产生小的误差也不影响真实情况，这些数据只是用来做参考
				/*
				 * int i=1,j=3;//记录行/列 for (int m : result) { if(m<1){
				 * if(errorflag != "") errorflag +="<br/>"; errorflag +=
				 * "第"+(i+1)+"行第"+j+"列"+"写入失败"; } j++; if(j>(cols)){ i++; j=4; }
				 * }
				 */
				conn.commit();
				tx.commit();
			} else {
				errorflag += ";Fail";
				errorflag += ";error："+error;
			}
			if (".html".equals(constants.LOGFILEEXT)) {
				writer.write("</fieldset></body></html>");
			}
			writer.close();
		} catch (Exception e) {
			if (conn != null) {
				try {
					// 数据库回滚rollback
					conn.rollback();
					conn.commit();
				} catch (Exception ex) {
					System.out.println("MySQL Rollback Fail: " + e.getMessage());
				}
			}
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return errorflag;
	}

	// map中所有属性用逗号隔开组成的字符串
	public StringBuffer str(Map dbmap) {
		StringBuffer insertsql = new StringBuffer();
		insertsql.append("'" + dbmap.get("softid").toString() + "'").append(
				",");
		insertsql.append("'" + dbmap.get("startdate").toString() + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("enddate")) + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("money")) + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("flag")) + "'")
		.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("place")) + "'")
		.append(",");
		return insertsql;
	}

	/**
	 * 读取Excel文件
	 * 
	 * @param strResourcePath
	 * @return
	 */
	public List opFromExcel(String strResourcePath) {
		POIFSFileSystem fs = null;
		Workbook wb = null;
		List list = null;
		int rewNum = 1;
//		int a = 0, b = 0;
		try {
			// 第一步，判断要导入的文件是否是Excel文件，即后缀名为.xls
			// 客户端作限制即可

			// 第二步，判断是否符合模板要求
			// 省 市 县、区 客戶名 联系人 出货日期 机型 颜色 串号 标贴类别
			// 日期 省 市 业务代码 业务名称 渠道商代码 渠道商名称 渠道代码 渠道名称 日总收入 日点数收入 日话费收入 日下载用户数
			// 日下载次数 日免费用户数 日免费次数 日付费用户数 日付费次数 日登录用户数 日登录次数 日图文点播用户数 日图文点播次数
			// 日ARPU 日业务新用户数

			FileInputStream fin = new FileInputStream(new File(strResourcePath));;
			
			String fileName = new File(strResourcePath).getName();  
			String hz = fileName.substring(fileName.lastIndexOf("."),fileName.length());  
			              
			if(hz.equals(".xls"))//针对2003版本  
			{  
			    //创建excel2003的文件文本抽取对象  
			    wb = new HSSFWorkbook(new POIFSFileSystem(fin));
			}else{ //针对2007版本  
			     //wb = new XSSFWorkbook(strResourcePath);
			     wb = new XSSFWorkbook(fin);
			}  

			Sheet sheet = wb.getSheetAt(0);
			fin.close();
			Row row = null; // 定义行
			Cell cell = null; // 定义单元格
			String cellva = null;

			row = sheet.getRow(constants.ROWINIT - 1); // 如果第二行包括所需列，则进行下面的，否则报错
			row = sheet.getRow(0);
			cols = (int) row.getLastCellNum();
			if (row.getLastCellNum() == col) {
				// 逐行读取文件写入数据库，这里要做事务处理，一次读取文件，全部写入，中间有错误则回滚。
				// 读取Excel文件内容存入List

				list = new ArrayList();
				Row firstRow = sheet.getRow(0);
				int rows = sheet.getLastRowNum();
				int cells = firstRow.getLastCellNum();
				for (int Ri = 1; Ri <= rows; Ri++) {
					rewNum = Ri;
					row = sheet.getRow(Ri);
					Map map = new HashMap();
					SimpleDateFormat dformat = new SimpleDateFormat(
							"yyyy-MM");
					String start = dformat.format(dformat.parse(getvalue(row.getCell(2))));
					String end = dformat.format(dformat.parse(getvalue(row.getCell(3))));
					if(!start.equals(end)){
						error = "开始日期与结束日期必须在同一个月";
						return null;
					}else{
						map.put("softid", getvalue(row.getCell(0)));
						map.put("softname", getvalue(row.getCell(1)));
						map.put("startdate", getvalue(row.getCell(2)));
						map.put("enddate", getvalue(row.getCell(3)));
						map.put("flag", getvalue(row.getCell(4)));
						map.put("money", getvalue(row.getCell(5)));
						map.put("place", getvalue(row.getCell(6)));
						list.add(map);
					}
					
				}
			}else{
				error = "Excel导入必须是"+col+"列，上传的Excel列数为："+row.getLastCellNum();
			}
		} catch (Exception e) {
			error = "Excel导入第"+rewNum+"行读取错误!";
//			System.out.println("行a,列b = " + a+1 + "," + b+1+"出错");
			e.printStackTrace();
		}
		return list;
	}

	public String getvalue(Cell cell) {
		// row.getCell(k).getCellType()
		String colName = "";
		if(cell != null){
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				colName = "";
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					double d = cell.getNumericCellValue();
					Date date = HSSFDateUtil.getJavaDate(d);
					Timestamp timestamp = new Timestamp(date.getTime());
					String temp = timestamp.toString();
					if (temp.endsWith("00:00:00.0")) {
						colName = temp.substring(0, temp.lastIndexOf("00:00:00.0"));
					} else if (temp.endsWith(".0")) {
						colName = temp.substring(0, temp.lastIndexOf(".0"));
					} else {
						SimpleDateFormat dformat = new SimpleDateFormat(
								"yyyy-MM-dd");
						colName = dformat.format(date);
					}
				} else {
					double temp = (double) cell.getNumericCellValue();
					colName = String.valueOf(temp);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:
				// 将ascii码为160的空格去掉
				colName = cell.getStringCellValue().replace(
						String.valueOf((char) 160), "");
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				colName = "";
				break;
			default:
				colName = "";
				break;
			}
		}
		return colName;
	}

}


