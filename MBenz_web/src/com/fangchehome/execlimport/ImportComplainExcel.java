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
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

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
public class ImportComplainExcel {

	private SQLSearch search = new SQLSearch();
	private Constants constants = new Constants();
	private Integer cols = 0;// 当前表格列数;
	private String error  = "",errorflag = "";

	/**
	 * 写入数据库
	 * 
	 * @param strResourcePath
	 */
	public String opDatabase(String strResourcePath, String strLogPath,
			String strLoginUser) {
		// 开始逐行读取List
		boolean flag = true;
		Connection conn = null;
		List dblist = null;
		dblist = opFromExcel(strResourcePath);
		StringBuffer strflag = new StringBuffer(); // 成功与否、存在相同IMEI串号的标识

		Session session = HibernateUtilCollect.getSession();
		Transaction tx = session.beginTransaction();
		//String errorflag = "";
		try {
			conn = session.connection();
			// PreparedStatement pstmt =
			// conn.prepareStatement("insert IGNORE into game_mobilebase_data(count_date,province,city,company_code,company_name,gamecode,gamename,channel_code,channel_name,day_sum_money,day_click_money,day_call_money,day_down_user,day_down_count,day_free_user,day_free_count,day_fee_user,day_fee_count,day_login_user,day_login_count,day_txtclick_user,day_txtclick_count,day_arpu,day_new_user,modify_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement pstmt = conn
					.prepareStatement("UPDATE app_complain SET partnercode=?,partnername=?,channel=?,businesscode=?,complaints=?,makecounts=?,payusercount=?,downusercount=? WHERE countdate=? AND province=? AND channelcode=? AND businessname=?");
			/*PreparedStatement pstmt = conn
					.prepareStatement("INSERT IGNORE INTO app_complain(countdate,province,partnercode,partnername,channelcode,channel,businesscode,businessname,complaints,makecounts,payusercount,downusercount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			*/
			String sql = "insert app_complain(countdate,province,partnercode,partnername,channelcode,channel,businesscode,businessname,complaints,makecounts,payusercount,downusercount) VALUES ";
			String sql0 = "";
			PrintWriter writer = new PrintWriter(new FileWriter(strLogPath
					+ "/ImportComplainExcel_" + search.displayformatDateTime()
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
					// 查询数据库中是否存在以条件为countdate, province, channelcode, businessname相符合的数据
					String sqlselect = "select id from app_complain where countdate='"+ String.valueOf(dbmap.get("countdate"))+"' AND province='"+String.valueOf(dbmap.get("province"))+"' AND channelcode='"+String.valueOf(dbmap.get("channelcode"))+"' AND businessname='"+String.valueOf(dbmap.get("businessname"))+"'";
					SQLQuery q = session.createSQLQuery(sqlselect);			
					q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List list = q.list();
					
					if(list != null && list.size() > 0){
						if(list.size() == 1){
							pstmt.setString(1, dbmap.get("partnercode").toString());
							pstmt.setString(2, dbmap.get("partnername").toString());
							pstmt.setString(3, dbmap.get("channel").toString());
							pstmt.setString(4, dbmap.get("businesscode").toString());
							pstmt.setDouble(5,
									Double.parseDouble(dbmap.get("complaints").toString()));
							pstmt.setDouble(6, Double.parseDouble(dbmap.get(
									"makecounts").toString()));
							pstmt.setDouble(7, Double.parseDouble(dbmap.get(
									"payusercount").toString()));
							pstmt.setDouble(8, Double.parseDouble(dbmap.get(
									"downusercount").toString()));
							pstmt.setString(9,dbmap.get("countdate").toString());
							pstmt.setString(10,dbmap.get("province").toString());
							pstmt.setString(11, dbmap.get("channelcode").toString());
							pstmt.setString(12, dbmap.get("businessname").toString());
							pstmt.addBatch();
						}else{
							errorflag += ";Fail";
							error = "日期为:"+String.valueOf(dbmap.get("countdate"))+ "、省份为:"+String.valueOf(dbmap.get("province")) + "、渠道代码为:"+String.valueOf(dbmap.get("channelcode")) + "、业务名称为:"+String.valueOf(dbmap.get("businessname"))+ "的是数据存在多条，请联系开发人员查询数据!";
							errorflag += ";error："+error;
							return errorflag;	
								
						}
					}else{
						sql0 += "('" + String.valueOf(dbmap.get("countdate")) + "'" +
								",'" + String.valueOf(dbmap.get("province")) + "'" +
								",'" + String.valueOf(dbmap.get("partnercode")) + "'" +
								",'" + String.valueOf(dbmap.get("partnername")) + "'" +
								",'" + String.valueOf(dbmap.get("channelcode")) + "'" +
								",'" + String.valueOf(dbmap.get("channel")) + "'" +
								",'" + String.valueOf(dbmap.get("businesscode")) + "'" +
								",'" + String.valueOf(dbmap.get("businessname")) + "'" +
								
								"," + Integer.parseInt(String.valueOf(dbmap.get("complaints"))) + 
								"," + Integer.parseInt(String.valueOf(dbmap.get("makecounts"))) + 
								"," + Integer.parseInt(String.valueOf(dbmap.get("payusercount"))) + 
								"," + Integer.parseInt(String.valueOf(dbmap.get("downusercount"))) + 
								"),";
					}
					
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
					strflag.append(dbmap.get("countdate").toString() + "-"+dbmap.get("province").toString() + ","+dbmap.get("channelcode").toString() + ","+dbmap.get("businesscode").toString() + ",");
				}
				int[] result = pstmt.executeBatch();
				if(sql0 != ""){
					sql0 = sql0.substring(0, sql0.length() - 1);
					sql = sql + sql0;
					int m = session.createSQLQuery(sql).executeUpdate();//木有下载数据的添加
				}
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
			}
			if (flag) {
				System.out.println("数据库写入记录成功！");
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
		insertsql.append("'" + dbmap.get("countdate").toString() + "'").append(
				",");
		insertsql.append("'" + dbmap.get("province").toString() + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("partnercode")) + "'")
		.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("partnername")) + "'")
		.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("channelcode")) + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("channel")) + "'")
				.append(",");
		insertsql.append("'" + dbmap.get("businesscode").toString() + "'").append(
				",");
		insertsql.append("'" + dbmap.get("businessname").toString() + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("complaints")) + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("makecounts")) + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("payusercount")) + "'")
		.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("downusercount")) + "'")
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
			row = sheet.getRow(1);
			cols = (int) row.getLastCellNum();
			if (row.getLastCellNum() > 2) {
				// 逐行读取文件写入数据库，这里要做事务处理，一次读取文件，全部写入，中间有错误则回滚。
				// 读取Excel文件内容存入List

				list = new ArrayList();
				Row firstRow = sheet.getRow(0);
		 		int rows = sheet.getLastRowNum();
				int cells = firstRow.getLastCellNum();
				for (int Ri = 1; Ri <= rows; Ri++) {
					row = sheet.getRow(Ri);
					Map map = new HashMap();
//					a = Ri;
					/*for (int k = 0; k < cells; k++) {
						
//						b = k;
						if (Ri > 0 && k >=0) {
							if (row.getCell(k) == null) {
								cellva = "";
							} else {*/
								/**
								 * 
								 CELL_TYPE_BLANK 空值 CELL_TYPE_BOOLEAN 布尔型
								 * CELL_TYPE_ERROR 错误 CELL_TYPE_FORMULA 公式型
								 * CELL_TYPE_STRING 字符串型 CELL_TYPE_NUMERIC 数值型
								 */

								/*// 日期
								String cellValue = "";
								switch (firstRow.getCell(k).getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC:
									if (HSSFDateUtil
											.isCellDateFormatted(firstRow
													.getCell(k))) {
										double d = firstRow.getCell(k)
												.getNumericCellValue();
										Date date = HSSFDateUtil.getJavaDate(d);
										SimpleDateFormat dformat = new SimpleDateFormat(
												"yyyy-MM-dd");
										cellValue = dformat.format(date);
									} else {
										NumberFormat nf = NumberFormat
												.getInstance();
										nf.setGroupingUsed(false);// true时的格式：1,234,567,890
										cellValue = nf.format(firstRow.getCell(
												k).getNumericCellValue());// 数值类型的数据为double，所以需要转换一下
									}
									break;
								case HSSFCell.CELL_TYPE_STRING:
									cellValue = firstRow.getCell(k)
											.getStringCellValue();
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									cellValue = String.valueOf(firstRow
											.getCell(k).getBooleanCellValue());
									break;
								case HSSFCell.CELL_TYPE_FORMULA:
									cellValue = String.valueOf(firstRow
											.getCell(k).getCellFormula());
									break;

								default:
									cellValue = "";
									break;
								}

								cellva = getvalue(row.getCell(k));*/
								for(int i = 0; i <= row.getLastCellNum(); i++) {
									if(row.getCell(i)!=null){
										  if(i<row.getLastCellNum()-4) row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
										  else row.getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);
								    }
								}
								SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
							    SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
							    //sfstr = sf2.format(sf1.parse(str));
								map.put("countdate", sf2.format(sf1.parse(getvalue(row.getCell(0))))); // 日期
								map.put("province", getvalue(row.getCell(1))); // 省份
								map.put("partnercode", getvalue(row.getCell(2))); // 渠道商代码
								map.put("partnername", getvalue(row.getCell(3))); // 渠道商名称
								map.put("channelcode", getvalue(row.getCell(4))); // 渠道代码
								map.put("channel", getvalue(row.getCell(5))); // 渠道名称
								map.put("businesscode", getvalue(row.getCell(6))); // 业务代码
								map.put("businessname", getvalue(row.getCell(7))); // 业务名称
								map.put("complaints", getvalue(row.getCell(8))); // 投诉量
								map.put("makecounts", getvalue(row.getCell(9))); // 不知情定制
								map.put("payusercount", getvalue(row.getCell(10))); //付费用户数
								map.put("downusercount", getvalue(row.getCell(11))); //下载用户数
								list.add(map);
							}
						}
					//}
				//}
			//}
		} catch (Exception e) {
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
						colName = timestamp.toString();
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
