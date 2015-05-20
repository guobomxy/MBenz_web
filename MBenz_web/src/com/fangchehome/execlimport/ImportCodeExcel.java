package com.fangchehome.execlimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
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

import com.fangchehome.hibernate.HibernateUtil;
import com.sun.jmx.snmp.Timestamp;

/**
 * 导入业务代码
 * 
 * @author
 * 
 */
public class ImportCodeExcel {

	private Integer cols = 0;// 当前表格列数;
	private String error  = "",errorflag = "";

	/**
	 * 写入数据库
	 * 
	 * @param strResourcePath
	 */
	public String opDatabase(String strResourcePath, String strLogPath,String strLoginUser) {
		
		List dblist= opFromExcel(strResourcePath);
		String errcode="";//用于返回错误信息
		try{
			if(dblist!=null && dblist.size()>0){
				//数据入库，更新机制
				for(int i=0;i<dblist.size();i++){
					Map s = (Map) dblist.get(i);
					String softcode="";
					String parentid=s.get("parentid").toString();
					String channelcode=s.get("channelcode").toString();
					String channelname=s.get("channelname").toString();
					String scale=s.get("scale").toString();
					
					//取到原来的业务ID
					String ssql="select softcode  from app_soft_parent WHERE id="+parentid;
					List<Map<String,String>> list = HibernateUtil.getSQL2MapList(ssql);		
					if(list!=null && list.size()>0){
						Map map=(Map)list.get(0);
						String ss="";
						if(map.get("softcode")==null){
							
						}else{
							ss=map.get("softcode").toString();
						}
						
						if("".equals(ss)){
							softcode=","+s.get("softcode").toString()+",";
						}else{
							if(ss.indexOf(s.get("softcode").toString())==-1){
								softcode=ss+s.get("softcode").toString()+",";
							}else{
								softcode=ss;
							}
						}
					}
					
					
					String usql="UPDATE app_soft_parent " +
							"SET channelcode='"+channelcode+"',channelname='"+channelname+"',scale="+scale+",softcode='"+softcode+"' " +
									"WHERE id="+parentid;
					HibernateUtil.sqlupdate(usql);
				}
			}else{
				error="导入的数据为空！";
				errorflag += ";Fail";
				errorflag += ";error："+error;
			}
		}catch (Exception e) {
			errorflag += ";Fail";
			errorflag += ";error："+e.getMessage();
			e.printStackTrace();
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
							String parentid=getvalue(row.getCell(0)).replace(",", "");//产品ID
							map.put("parentid", parentid);
							
							String softcode=getvalue(row.getCell(1)).replace(",", "");//业务代码
							map.put("softcode", softcode);
							
							String channelcode=getvalue(row.getCell(2)).replace(",", "");//渠道代码
							map.put("channelcode", channelcode);
							
							String channelname=getvalue(row.getCell(3));//渠道名称
							map.put("channelname", channelname);
							
							String scale=getvalue(row.getCell(4));//分成比例
							map.put("scale", scale);
							
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
