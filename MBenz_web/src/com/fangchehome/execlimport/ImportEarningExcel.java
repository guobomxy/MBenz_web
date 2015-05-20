package com.fangchehome.execlimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
public class ImportEarningExcel {

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
		try {
			conn = session.connection();
			// PreparedStatement pstmt =
			// conn.prepareStatement("insert IGNORE into game_mobilebase_data(count_date,province,city,company_code,company_name,gamecode,gamename,channel_code,channel_name,day_sum_money,day_click_money,day_call_money,day_down_user,day_down_count,day_free_user,day_free_count,day_fee_user,day_fee_count,day_login_user,day_login_count,day_txtclick_user,day_txtclick_count,day_arpu,day_new_user,modify_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement pstmt = conn
					.prepareStatement("UPDATE app_download_daily SET earning=?,scale=?,scaleearning=? WHERE id=(SELECT id FROM (SELECT id FROM app_download_daily WHERE app_id=? AND download_day=? order by scale desc LIMIT 0,1) a)  AND download_day=?");
			String sql = "insert app_download_daily(earning,scale,scaleearning,cpid,app_id,download_day) VALUES ";
			String sql0 = "";
			PrintWriter writer = new PrintWriter(new FileWriter(strLogPath
					+ "/ImportRejectExcel_" + search.displayformatDateTime()
					+ "_" + strLoginUser + constants.LOGFILEEXT));
			if (".html".equals(constants.LOGFILEEXT)) {
				writer.write("<html><head></head><body style=\"font-size: 12px; margin-top: 10px\"><fieldset><legend>导入CP产品收入表</legend>");
			}
			/**
			 * 写入数据库前，需要在Excel的list中判断是否有数据已存在在数据库中
			 */			if (dblist != null && dblist.size() > 0) {
				String strSql = ""; // 查询SQL
				List slist = null; // 查询list
				for (int sI = 0, logI = 0; sI < dblist.size(); sI++) {
					Map dbmap = (Map) dblist.get(sI);
					if (StringUtils.isNotBlank(String.valueOf(dbmap
							.get("app_id")))
							&& StringUtils.isNotBlank(String.valueOf(dbmap
									.get("download_day")))) {
						// 查询数据库中是否存在以条件为download_day`,`app_id`相符合的数据
						String sqlselect = "select id from app_download_daily where app_id="+ (int)Double.parseDouble(String.valueOf(dbmap.get("app_id")))+" AND download_day='"+String.valueOf(dbmap.get("download_day"))+"'";
						SQLQuery q = session.createSQLQuery(sqlselect);			
						q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List list = q.list();
						//根据该产品id查询产品表中cpid
						String sqls = "SELECT cpid FROM ftsoft.app_soft_parent WHERE id="+(int)Double.parseDouble(dbmap.get("app_id").toString());
						SQLQuery qs = session.createSQLQuery(sqls);			
						qs.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map> lists = qs.list();
						if(lists != null && lists.size() == 1){
							if(!"0".equals(String.valueOf(lists.get(0).get("cpid")))){
								if(list!= null &&list.size()>0){
									pstmt.setDouble(1, Double.parseDouble(dbmap.get(
											"earning").toString()));
									pstmt.setDouble(2, Double.parseDouble(dbmap
											.get("scale").toString()));
									pstmt.setDouble(3, Double.parseDouble(dbmap.get(
											"scaleearning").toString()));
									pstmt.setString(4, dbmap.get("app_id").toString());
									pstmt.setString(5, dbmap.get("download_day").toString());
									pstmt.setString(6, dbmap.get("download_day").toString());
									pstmt.addBatch();
								}else{
									sql0 += "(" +  Double.parseDouble(dbmap.get("earning").toString())+ "," +
											Double.parseDouble(dbmap.get("scale").toString()) + "," +  
											Double.parseDouble(dbmap.get("scaleearning").toString()) + "," + 
											lists.get(0).get("cpid") + "," + 
											dbmap.get("app_id").toString() + ",'" +  
											dbmap.get("download_day").toString() + 
											"'),";
									
								}
							}else{
								errorflag += ";Fail";
								error = "产品id为"+(int)Double.parseDouble(dbmap.get("app_id").toString())+ "的是非cp产品!";
								errorflag += ";error："+error;
								return errorflag;
							}
						}else{
							errorflag += ";Fail";
							error =  "在产品表中找不到id为"+(int)Double.parseDouble(dbmap.get("app_id").toString())+"的该产品";
							errorflag += ";error："+error;
							return errorflag;
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
						strflag.append(dbmap.get("app_id").toString() + ",");
					}
				}
				if(sql0 != ""){
					sql0 = sql0.substring(0, sql0.length() - 1);
					sql = sql + sql0;
					int m = session.createSQLQuery(sql).executeUpdate();//木有下载数据的添加
				}
				int[] result = pstmt.executeBatch();//更新
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
			if (flag) {
				System.out.println("数据库写入记录成功！");
			}
			if (".html".equals(constants.LOGFILEEXT)) {
				writer.write("</fieldset></body></html>");
			}
			writer.close();
		} catch (Exception e) {
			error = "执行失败!";

			if (conn != null) {
				try {
					// 数据库回滚rollback
					conn.rollback();
					conn.commit();
				} catch (Exception ex) {
					System.out
							.println("MySQL Rollback Fail: " + e.getMessage());
				}
			}
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) conn.close();
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
		insertsql.append("'" + dbmap.get("earning").toString() + "'").append(
				",");
		insertsql.append("'" + dbmap.get("app_id").toString() + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("download_day")) + "'")
				.append(",");
		insertsql.append("'" + String.valueOf(dbmap.get("download_day")) + "'")
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
		HSSFWorkbook wb = null;
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

				FileInputStream fin = new FileInputStream(new File(strResourcePath));
				fs = new POIFSFileSystem(fin);
				wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				fin.close();
				HSSFRow row = null; // 定义行
				HSSFCell cell = null; // 定义单元格
				String cellva = null;

				row = sheet.getRow(constants.ROWINIT - 1); // 如果第二行包括所需列，则进行下面的，否则报错
				row = sheet.getRow(1);
				cols = (int) row.getLastCellNum();
				if (row.getLastCellNum() > 2) {
					// 逐行读取文件写入数据库，这里要做事务处理，一次读取文件，全部写入，中间有错误则回滚。
					// 读取Excel文件内容存入List

					list = new ArrayList();
					HSSFRow firstRow = sheet.getRow(0);
					int rows = sheet.getLastRowNum();
					int cells = firstRow.getLastCellNum();
					for (int Ri = 1; Ri <= rows; Ri++) {
						row = sheet.getRow(Ri);
//					a = Ri;
						try {
							for (int k = 0; k < cells; k++) {
								Map map = new HashMap();
//						b = k;
								if (Ri > 0 && k > 2) {
									if (row == null || row.getCell(0) == null) {
										cellva = "";
									} else {
										/**
										 * 
										 CELL_TYPE_BLANK 空值 CELL_TYPE_BOOLEAN 布尔型
										 * CELL_TYPE_ERROR 错误 CELL_TYPE_FORMULA 公式型
										 * CELL_TYPE_STRING 字符串型 CELL_TYPE_NUMERIC 数值型
										 */

										// 日期
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
										cellva = "0";
										cellva = getvalue(row.getCell(k));
										map.put("app_id", row.getCell(0)
												.getNumericCellValue()); // 产品ID
										// map.put("download_day",
										// ConvertUtil.formatDate(cellValue)); //日期
										map.put("download_day", cellValue); // 日期
										double scale = 0.00, earning = 0.00;
										if (StringUtils.isNotBlank(cellva))
											earning = Double.parseDouble(cellva
													.replace("?", ""));
										if (StringUtils.isNotBlank(getvalue(row
												.getCell(2))))
											scale = Double.parseDouble(getvalue(row
													.getCell(2)));
										map.put("scale", scale); // 日期
										map.put("scaleearning", scale * earning); // 日期
										map.put("earning", earning); // 收益
										// System.out.println("Map = "+map);
										list.add(map);
									}
								}
							}
						} catch (Exception e) {
							error = "第"+Ri+"行读取出错！请校对EXCEL数据!";
							System.out.println("Ri:"+Ri);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (NumberFormatException e) {
				error = "数据格式异常";
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				error = "文件找不到异常";
				e.printStackTrace();
			} catch (IOException e) {
				error = "读取文件出错";
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		
		return list;
	}

	public String getvalue(HSSFCell cell) {
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
