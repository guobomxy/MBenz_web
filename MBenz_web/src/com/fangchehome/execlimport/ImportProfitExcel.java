package com.fangchehome.execlimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.hibernate.transform.Transformers;

import com.fangchehome.hibernate.HibernateUtil;
import com.fangchehome.hibernate.HibernateUtilCollect;
import com.sun.jmx.snmp.Timestamp;

/**
 * 导入基地收益
 * 
 * @author
 * 
 */
public class ImportProfitExcel {

	private Integer cols = 0;// 当前表格列数;
	private String error  = "",errorflag = "";

	/**
	 * 写入数据库
	 * 
	 * @param strResourcePath
	 */
	public String opDatabase(String strResourcePath, String strLogPath,String strLoginUser) {
		
		boolean flag = true;
		Connection conn = null;
		List dblist = null;
		dblist = opFromExcel(strResourcePath);
		
		Session session = HibernateUtilCollect.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String ssql="";
		String qsql="";
		
		String errcode="";//用于返回没有业务代码录入的
		
		Map mapslist=new HashMap<String, String>();
		
		try{
			conn = session.connection();
			//插入
			PreparedStatement pstmt;
			String sql1="INSERT  INTO app_earning(rec_date,product_id,product_name,type,gamecode,channelcode,channelname,scale,earning,usercount_day_pay,scaleearning,flag) " +
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			//更新
			String sql2="UPDATE app_earning set product_id=?,product_name=?,type=?" +
					",channelname=?,scale=?,earning=?,usercount_day_pay=?,scaleearning=?,flag=? where id=?";
			
			//遍历dblist查询业务代码-----导入时业务代码系统无记录不入库，提示不存在的业务代码
			if(dblist!=null && dblist.size()>0){
				Boolean f=false;
				for(int ii=0;ii<dblist.size();ii++){
					Map dbmap = (Map) dblist.get(ii);
					
					String channelcode=dbmap.get("channelcode").toString();
					if("40009726386".equals(channelcode)){//放弃渠道40009726386导入
						continue;
					}
					
					String gamecode=dbmap.get("gamecode").toString();
					String name=dbmap.get("name").toString();
					ssql="select id,name,scale,cpid from app_soft_parent where softcode like '%,"+gamecode+",%'";
					List<Map<String,String>> list = HibernateUtil.getSQL2MapList(ssql);		
					if(list != null && list.size() > 0){
						Map g = (Map) list.get(0);
						//进行map重新赋值,方便下面插入重复查询
						dbmap.put("product_id", g.get("id"));//产品ID
						dbmap.put("product_name", g.get("name"));//产品名称
						dbmap.put("scale", g.get("scale")==null ? 0 : g.get("scale"));//分成比例
						dbmap.put("cpid", g.get("cpid")==null ? 0 : g.get("cpid"));//放入cpid，方便导入
						
						//数据正确进行插入
//						String channelcode=dbmap.get("channelcode").toString();
//						qsql="select id from app_soft_parent where channelcode ='40009723251' and softcode like '%,"+gamecode+",%'";
//						List<Map<String,String>> listz = HibernateUtil.getSQL2MapList(qsql);			
						if("40009723251".equals(channelcode)){
							//存在，存"主渠道收益"
							dbmap.put("type", 0);
						}else{
							//不存在，存"基地其他渠道收益"
							dbmap.put("type", 3);
						}
						//正常数据进行入库
						String sssql="select id  from app_earning where rec_date='"+dbmap.get("rec_date").toString()+"' and gamecode='"+dbmap.get("gamecode").toString()+"' and channelcode='"+dbmap.get("channelcode").toString()+"'";
						SQLQuery qs = session.createSQLQuery(sssql);			
						qs.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List slist = qs.list();
						int[] result;
						
						//判断是否代计费
						String dsql="select id from app_soft_pay where channelcode='"+channelcode+"' and softcode='"+gamecode+"'";
						List<Map<String,String>> dlist = HibernateUtil.getSQL2MapList(dsql);
						if(dlist!=null && dlist.size()>0){
							dbmap.put("flag", 1);
						}else{
							dbmap.put("flag", 0);
						}
						
						if(slist != null && slist.size() > 0){//进行更新
							pstmt= conn.prepareStatement(sql2);
							pstmt.setString(1,dbmap.get("product_id").toString());
							pstmt.setString(2,dbmap.get("product_name").toString());
							pstmt.setString(3,dbmap.get("type").toString());
							pstmt.setString(4,dbmap.get("channelname").toString());
							pstmt.setString(5,dbmap.get("scale").toString());//分成比例
							pstmt.setString(6,dbmap.get("earning").toString());//收益数值
							pstmt.setString(7,dbmap.get("usercount_day_pay").toString());//日付费用户数
							//计算分成
							double s=Double.parseDouble(dbmap.get("scale").toString())*Double.parseDouble(dbmap.get("earning").toString());
							pstmt.setDouble(8,s);//分成收入
							pstmt.setString(9,dbmap.get("flag").toString());//是否代计费（0－否，1－是)
							Map ss = (Map) slist.get(0);
							pstmt.setString(10,ss.get("id").toString());
						}else{//进行插入
							pstmt= conn.prepareStatement(sql1);
							pstmt.setString(1, dbmap.get("rec_date").toString());
							pstmt.setString(2, dbmap.get("product_id").toString());
							pstmt.setString(3, dbmap.get("product_name").toString());
							pstmt.setString(4, dbmap.get("type").toString());
							pstmt.setString(5, dbmap.get("gamecode").toString());
							pstmt.setString(6, dbmap.get("channelcode").toString());
							pstmt.setString(7, dbmap.get("channelname").toString());
							pstmt.setString(8, dbmap.get("scale").toString());
							pstmt.setString(9, dbmap.get("earning").toString());
							pstmt.setString(10, dbmap.get("usercount_day_pay").toString());
							//计算分成
							double s=Double.parseDouble(dbmap.get("scale").toString())*Double.parseDouble(dbmap.get("earning").toString());
							pstmt.setDouble(11,s);//分成收入
							pstmt.setString(12, dbmap.get("flag").toString());
						}
						pstmt.addBatch();
						result= pstmt.executeBatch();
						
						
						//插入成功后判断下载表是否存在CP>0的记录，没有新增一条
						String downsql="select id  from app_download_daily where download_day='"+dbmap.get("rec_date").toString()+"' and app_id='"+dbmap.get("product_id").toString()+"' and cpid >0";
						SQLQuery ds = session.createSQLQuery(downsql);			
						qs.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List downlist = ds.list();
						
						PreparedStatement pstmts;
						String sql3="INSERT  INTO app_download_daily(soft_id,download_day,download_times,type,day_download_all,app_id,cpid) " +
								"VALUES(?,?,?,?,?,?,?)";
						
						if(downlist!=null && downlist.size()>0){
							
						}else{//导入的一定是CP产品
							int cpids=Integer.parseInt(dbmap.get("cpid").toString());
							if(cpids>0){
								
							}else{
								cpids=99999;
							}
							pstmts= conn.prepareStatement(sql3);
							pstmts.setInt(1, 0);
							pstmts.setString(2, dbmap.get("rec_date").toString());
							pstmts.setInt(3, 0);
							pstmts.setString(4, "0");
							pstmts.setInt(5, 0);
							pstmts.setString(6, dbmap.get("product_id").toString());
							pstmts.setInt(7, cpids);
							pstmts.addBatch();
							pstmts.executeBatch();
						}
						
					}else{
						//代计费第二种输入方式
						ssql="select id,channelcode,channelname,scale from app_soft_pay where softcode like '%"+gamecode+"%'";
						List<Map<String,String>> dlist = HibernateUtil.getSQL2MapList(ssql);		
						if(dlist!=null && dlist.size()>0){
							Map g = (Map) dlist.get(0);
							dbmap.put("product_id", "9000000"+g.get("id"));//产品ID
							dbmap.put("product_name", g.get("channelname"));//产品名称
							dbmap.put("scale", g.get("scale")==null ? 0 : g.get("scale"));//分成比例
							dbmap.put("flag", 1);
							
							
							if("40009723251".equals(dbmap.get("channelcode").toString())){
								dbmap.put("type", 0);
							}else{
								dbmap.put("type", 3);
							}
							
							//正常数据进行入库
							String sssql="select id  from app_earning where rec_date='"+dbmap.get("rec_date").toString()+"' and gamecode='"+dbmap.get("gamecode").toString()+"' and channelcode='"+dbmap.get("channelcode").toString()+"'";
							SQLQuery qs = session.createSQLQuery(sssql);			
							qs.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							List slist = qs.list();
							
							if(slist != null && slist.size() > 0){//进行更新
								pstmt= conn.prepareStatement(sql2);
								pstmt.setString(1,dbmap.get("product_id").toString());
								pstmt.setString(2,dbmap.get("product_name").toString());
								pstmt.setString(3,dbmap.get("type").toString());
								pstmt.setString(4,dbmap.get("channelname").toString());
								pstmt.setString(5,dbmap.get("scale").toString());//分成比例
								pstmt.setString(6,dbmap.get("earning").toString());//收益数值
								pstmt.setString(7,dbmap.get("usercount_day_pay").toString());//日付费用户数
								//计算分成
								double s=Double.parseDouble(dbmap.get("scale").toString())*Double.parseDouble(dbmap.get("earning").toString());
								pstmt.setDouble(8,s);//分成收入
								pstmt.setString(9,dbmap.get("flag").toString());//是否代计费（0－否，1－是)
								Map ss = (Map) slist.get(0);
								pstmt.setString(10,ss.get("id").toString());
							}else{//进行插入
								pstmt= conn.prepareStatement(sql1);
								pstmt.setString(1, dbmap.get("rec_date").toString());
								pstmt.setString(2, dbmap.get("product_id").toString());
								pstmt.setString(3, dbmap.get("product_name").toString());
								pstmt.setString(4, dbmap.get("type").toString());
								pstmt.setString(5, dbmap.get("gamecode").toString());
								pstmt.setString(6, dbmap.get("channelcode").toString());
								pstmt.setString(7, dbmap.get("channelname").toString());
								pstmt.setString(8, dbmap.get("scale").toString());
								pstmt.setString(9, dbmap.get("earning").toString());
								pstmt.setString(10, dbmap.get("usercount_day_pay").toString());
								//计算分成
								double s=Double.parseDouble(dbmap.get("scale").toString())*Double.parseDouble(dbmap.get("earning").toString());
								pstmt.setDouble(11,s);//分成收入
								pstmt.setString(12, dbmap.get("flag").toString());
							}
							pstmt.addBatch();
							pstmt.executeBatch();
							
							
							//插入成功后判断下载表是否存在CP>0的记录，没有新增一条
							String downsql="select id  from app_download_daily where download_day='"+dbmap.get("rec_date").toString()+"' and app_id='"+dbmap.get("product_id").toString()+"' and cpid >0";
							SQLQuery ds = session.createSQLQuery(downsql);			
							qs.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							List downlist = ds.list();
							
							PreparedStatement pstmts;
							String sql3="INSERT  INTO app_download_daily(soft_id,download_day,download_times,type,day_download_all,app_id,cpid) " +
									"VALUES(?,?,?,?,?,?,?)";
							
							if(downlist!=null && downlist.size()>0){
								
							}else{//导入的一定是CP产品
								pstmts= conn.prepareStatement(sql3);
								pstmts.setInt(1, 0);
								pstmts.setString(2, dbmap.get("rec_date").toString());
								pstmts.setInt(3, 0);
								pstmts.setString(4, "0");
								pstmts.setInt(5, 0);
								pstmts.setString(6, dbmap.get("product_id").toString());
								pstmts.setInt(7, 99999);
								pstmts.addBatch();
								pstmts.executeBatch();
							}
							
						}else{
							//判断是否重复to-do
							mapslist.put("A"+gamecode, "第"+(ii+2)+"行,业务名称为《"+name+"》;<br>");
							f=true;
						}
					}
				}
				if(f){
					//返回进行提示
					Iterator it=mapslist.entrySet().iterator();  
					StringBuffer sb = new StringBuffer();
					while(it.hasNext()){   
					        Map.Entry entry = (Map.Entry)it.next();          
					        sb.append(entry.getValue());    
					        
					}   
					errcode = sb.toString();
					
					errorflag += ";Fail";
					errorflag += ";<font style=\"color:red\">error：</font><br><br>"+errcode;
					errorflag +="请录入后再重新导入，补全数据！";
//					return errorflag;	
				}
				conn.commit();
				session.getTransaction().commit();
			}else{
				errorflag += ";Fail";
				errorflag += ";error："+error;
			}
		}catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
					session.getTransaction().commit();
				} catch (Exception ex) {
					System.out.println("MySQL Rollback Fail: " + e.getMessage());
				}
			}
			errorflag += ";Fail";
			errorflag += ";error："+e.getMessage();
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
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
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
	    SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
		
		POIFSFileSystem fs = null;
		Workbook wb = null;
		List list = null;
			try {
				// 第一步，判断要导入的文件是否是Excel文件，即后缀名为.xls
				// 客户端作限制即可
				// 第二步，判断是否符合模板要求
				// 省 市 县、区 客戶名 联系人 出货日期 机型 颜色 串号 标贴类别
				// 日期 省 市 业务代码 业务名称 渠道商代码 渠道商名称 渠道代码 渠道名称 日总收入 日点数收入 日话费收入 日下载用户数
				// 日下载次数 日免费用户数 日免费次数 日付费用户数 日付费次数 日登录用户数 日登录次数 日图文点播用户数 日图文点播次数
				// 日ARPU 日业务新用户数

				FileInputStream fin = new FileInputStream(new File(strResourcePath));
				String fileName = new File(strResourcePath).getName();  
				String hz = fileName.substring(fileName.lastIndexOf("."),fileName.length());  
//				fs = new POIFSFileSystem(fin);
//				wb = new HSSFWorkbook(fs);
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
						try {
							//提取数据
							String dat=getvalue(row.getCell(0)).replace(",", "");//收益日期
							map.put("rec_date", sf2.format(sf1.parse(dat)));
							String gamecode=getvalue(row.getCell(1)).replace(",", "");//业务代码
							map.put("gamecode", gamecode);
							String name=getvalue(row.getCell(2));//业务名称
							map.put("name", name);
							String channelcode=getvalue(row.getCell(7)).replace(",", "");//渠道代码
							map.put("channelcode", channelcode);
							String channelname=getvalue(row.getCell(8));//渠道名称
							map.put("channelname", channelname);
							String earning=getvalue(row.getCell(10)).replace(",", "");//收益数值
							map.put("earning", earning);
							String usercount_day_pay=getvalue(row.getCell(17)).replace(",", "");//日付费用户数
							map.put("usercount_day_pay", usercount_day_pay);
							list.add(map);
						} catch (Exception e) {
							error="解析第"+Ri+"条数据出错";
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
				error = "未知错误";
				e.printStackTrace();
			}
		return list;
	}

	public String getvalue(Cell cell) {
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
					double temp =  (double)cell.getNumericCellValue();
					NumberFormat formatter = NumberFormat.getNumberInstance();
					formatter.setMaximumFractionDigits(2);
					colName= formatter.format(temp);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:
				// 将ascii码为160的空格去掉
				colName = cell.getStringCellValue().replace(String.valueOf((char) 160), "");
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
