package com.fangchehome.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelServlet extends HttpServlet {
	private String listData;//表格数据 
	private String title;//表格名
	private String column;//表头(以","隔开)
	private String filename;
	private String type;//多表头页面过来时，type不为空;1.新市场cp产品;2.新市场所有产品;3.新市场广告;4.市场用户情况
	private String titleTwo;//二级表头内容
	
	/**
	 * Constructor of the object.
	 */
	public ExcelServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String currentTIME = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		listData = request.getParameter("listData").replace("\"", "");
		title = request.getParameter("title");
		column = request.getParameter("column");
		filename = request.getParameter("filename");
		type = request.getParameter("type");
		titleTwo = request.getParameter("titleTwo");
		String downloadFileName= currentTIME+filename+".xls";
		downloadFileName=new String(downloadFileName.getBytes(),"ISO8859-1");

		response.reset();
		response.setContentType("application/ms-excel;charset=GBK"); 
		response.setHeader("Content-Disposition", "inline;filename=" + downloadFileName );

		try {
			
			if(type != null) writeDataTwoTitleExcel(response.getOutputStream(),listData, title,type,column,titleTwo);
			else writeDataInfoExcel(response.getOutputStream(),
					listData, title);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void writeDataInfoExcel(OutputStream os,String listData,
			String title) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = wb.createSheet();
			wb.setSheetName(0,title);
			HSSFRow row = null;
			HSSFCell cell;
			HSSFFont font = wb.createFont();
			HSSFFont f = wb.createFont();
			HSSFCellStyle cellStyle = wb.createCellStyle();
			HSSFCellStyle cellAlignStyle = wb.createCellStyle();
			HSSFCellStyle titleStyle = wb.createCellStyle();
			setStyle(font, f, cellStyle, cellAlignStyle, titleStyle);
			String[] columns = column.split(",");
			/**
			 * 行首描述性标题
			 */
			row = sheet.createRow(0);
			cell = row.createCell(0);

			cell.setCellStyle(titleStyle);
			cell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.length-1));// 指定合并区域

			row = sheet.createRow(1);
			//设置表头
			if(columns != null && columns.length > 0){
				for (int i = 0; i < columns.length; i++) {
					cell = row.createCell(i);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(columns[i]);
				}
			}
			String[] excellist = listData.split(";");
			if (excellist != null && excellist.length > 0) {
				
				for (int i = 0; i < excellist.length; i++) {
					row = sheet.createRow(i + 2);
					String[] rowlist = excellist[i].split(",");
					for (int j = 0; j < columns.length; j++) {
						cell = row.createCell(j);
						if(j == 0) cellAlignStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
						else cellAlignStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellAlignStyle);
						sheet.setColumnWidth(j, 5000);// 设置列宽
						if(j<rowlist.length)
							if(rowlist[j] != null && StringUtils.isNotBlank(String.valueOf(rowlist[j])) && String.valueOf(rowlist[j]).matches("[0-9]*")){
							cell.setCellValue(Double.parseDouble(String.valueOf(rowlist[j])));//TODO
							}else{
								cell.setCellValue(String.valueOf(rowlist[j]));
							}
					}
				
				}
			}
			wb.write(os);
			os.flush();
			os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//二级表头
	public void writeDataTwoTitleExcel(OutputStream os,String listData,
			String title,String type,String column,String titleTwo) {
		HSSFWorkbook wb = new HSSFWorkbook();
		String[] columns = column.split(",");
		String[] titleTwos = titleTwo.split(",");
	try {
		HSSFSheet sheet = wb.createSheet();
		wb.setSheetName(0,title);
		HSSFRow row = null;
		HSSFCell cell;
		HSSFFont font = wb.createFont();
		HSSFFont f = wb.createFont();
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCellStyle cellAlignStyle = wb.createCellStyle();
		HSSFCellStyle titleStyle = wb.createCellStyle();
		setStyle(font, f, cellStyle, cellAlignStyle, titleStyle);
		row = sheet.createRow(0);
		cell = row.createCell(0);

		cell.setCellStyle(titleStyle);
		cell.setCellValue(title);	
		row = sheet.createRow(1);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleTwos.length+5));// 指定合并区域
		
		if("3".equals(type)) type="1";
		//新市场cp下载量，新市场所有应用下载量
		if("1".equals(type)){
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));// 指定合并区域
			for(int i = 0; i < titleTwos.length; i++){
				sheet.addMergedRegion(new CellRangeAddress(1, 1, (1+i), (2+i)));// 指定合并区域
			}
			for(int i = titleTwos.length+1; i < titleTwos.length+6; i++){
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));// 指定合并区域
			}
			//设置一级表头
			if(columns != null && columns.length > 0){
				int j = 0;
				for (int i = 0; i < columns.length; i++) {
					if(i != 0 && i<columns.length-5){
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
						j++;
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");
					}else{
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
					}
					j++;
				}
			}
			//二级表头
			row = sheet.createRow(2);
			if(titleTwos != null && titleTwos.length > 0){
				cell = row.createCell(0);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("");
				int x = 0;
				for (int i = 0; i < titleTwos.length; i++) {
					cell = row.createCell(i+1);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titleTwos[i]);
					x = i;
				}
				for (int i = x+2; i < x+5; i++) {
					cell = row.createCell(i);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue("");
				}
			}
			
		}
		//新市场所有产品下载量
		if("2".equals(type)){
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));// 指定合并区域
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));// 指定合并区域
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
			//设置一级表头
			if(columns != null && columns.length > 0){
				int j = 0;
				for (int i = 0; i < columns.length; i++) {
					cell = row.createCell(j);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(columns[i]);
					
					if(i == 1){
						j++;
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");
					}
					j++;
				}
			}
			//二级表头
			row = sheet.createRow(2);
			if(titleTwos != null && titleTwos.length > 0){
				cell = row.createCell(0);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("");
				for (int i = 0; i < titleTwos.length; i++) {
					cell = row.createCell(i+1);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titleTwos[i]);
				}
				cell = row.createCell(3);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("");
				
				cell = row.createCell(4);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("");
			}
		}
		//市场用户情况数据
		if("4".equals(type)){
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));// 指定合并区域
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));// 指定合并区域
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
			//设置一级表头
			if(columns != null && columns.length > 0){
				int j = 0;
				for (int i = 0; i < columns.length; i++) {
					if(i != 0){
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
						j++;
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");
						if(i ==1 ){
							j++;
							cell = row.createCell(j);
							cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
							cell.setCellStyle(cellStyle);
							cell.setCellValue("");
						}
					}else{
						cell = row.createCell(i);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
					}
					j++;
				}
			}
			//二级表头
			row = sheet.createRow(2);
			if(titleTwos != null && titleTwos.length > 0){
				cell = row.createCell(0);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("");
				for (int i = 0; i < titleTwos.length; i++) {
					cell = row.createCell(i+1);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titleTwos[i]);
				}
			}
			
		}
		//总激活量日报表，月报表
		if("5".equals(type)){
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));// 指定合并区域
			for(int i = 0; i < titleTwos.length; i++){
				sheet.addMergedRegion(new CellRangeAddress(1, 1, (1+i), (3+i)));// 指定合并区域
			}
			//设置一级表头
			if(columns != null && columns.length > 0){
				int j = 0;
				for (int i = 0; i < columns.length; i++) {
					if(i != 0){
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
						j++;
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");
						j++;
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");
					}else{
						cell = row.createCell(i);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
					}
					j++;
				}
			}
			//二级表头
			row = sheet.createRow(2);
			if(titleTwos != null && titleTwos.length > 0){
				cell = row.createCell(0);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("");
				for (int i = 0; i < titleTwos.length; i++) {
					cell = row.createCell(i+1);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titleTwos[i]);
				}
			}
					
		}
		//渠道激活量日报表，月报表
		if("6".equals(type)){
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));// 指定合并区域
			for(int i = 2; i < titleTwos.length+2; i++){
				sheet.addMergedRegion(new CellRangeAddress(1, 1, (1+i), (2+i)));// 指定合并区域
			}
			for(int i = 0; i < 3; i++){
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));// 指定合并区域
			}
			//设置一级表头
			if(columns != null && columns.length > 0){
				int j = 0;
				for (int i = 0; i < columns.length; i++) {
					if(i > 2){
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
						j++;
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");
					}else{
						cell = row.createCell(j);
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(columns[i]);
					}
					j++;
				}
			}
			//二级表头
			row = sheet.createRow(2);
			if(titleTwos != null && titleTwos.length > 0){
				for(int i = 0;i<3;i++){
					cell = row.createCell(0);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue("");
				}
				for (int i = 0; i < titleTwos.length; i++) {
					cell = row.createCell(i+3);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titleTwos[i]);
				}
				
			}
					
		}
		
		String[] excellist = listData.split(";");
		if (excellist != null && excellist.length > 0) {
			for (int i = 0; i < excellist.length; i++) {
				row = sheet.createRow(i +3);
				String[] rowlist = excellist[i].split(",");
				for (int j = 0; j < rowlist.length; j++) {
					cell = row.createCell(j);
					if(j == 0) cellAlignStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
					else cellAlignStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellAlignStyle);
					sheet.setColumnWidth(0, 5000);// 设置列宽
					cell.setCellValue(String.valueOf(rowlist[j]));
				}
			
			}
		}
		wb.write(os);
		os.flush();
		os.close();

	} catch (Exception e) {
		e.printStackTrace();
	}
		
	}
	
	
	//样式
	public void setStyle(HSSFFont font ,HSSFFont f,HSSFCellStyle cellStyle ,HSSFCellStyle cellAlignStyle,HSSFCellStyle titleStyle){
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short)200);
        
        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        f.setFontHeight((short)400);
        
        titleStyle.setFont(f);
        cellStyle.setFont(font);
        
        cellAlignStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(HSSFColor.GREEN.index);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(HSSFColor.BLUE.index);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	}
	
	

	
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	public String getListData() {
		return listData;
	}

	public void setListData(String listData) {
		this.listData = listData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitleTwo() {
		return titleTwo;
	}

	public void setTitleTwo(String titleTwo) {
		this.titleTwo = titleTwo;
	}
	
	
	
}
