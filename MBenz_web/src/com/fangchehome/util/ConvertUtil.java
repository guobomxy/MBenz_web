package com.fangchehome.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.ServletActionContext;

/**
 * 字符串、日期等转换类
 * @author hmilo029
 *
 */
public class ConvertUtil {	
	
		
	/**
	 * 字符串编码转换
	 * @param strOrg
	 * @return
	 */
	public static String convertCharset(String strOrg) {
		String strRs = "";
		try {
			if (System.getProperty("os.name").equals("Linux")) {
				strRs = new String(strOrg.getBytes("ISO8859-1"),"GB18030");
			} else {
				strRs = strOrg;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
		
		return strRs;
	}
	
	/**
	 * 获取当前日期时间
	 * @return
	 */
	public static String displayCurrentDateTime() {		  
		return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Calendar.getInstance().getTime());	//获取当前日期时间
	}
	
	/**
	 * 获取当前日期时间
	 * @return
	 */
	public static String displayCurrentDate() {		  
		return new java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime());	//获取当前日期时间
	}
	
	/**
	 * 获取当前日期时间秒数
	 * @return
	 */
	public static long displayCurrentDateSecondTime() {		  
		return java.util.Calendar.getInstance().getTimeInMillis();	//获取当前日期时间秒数
	}
	
	/**
	 * 显示可作为文件名的时间日期
	 * @return
	 */
	public static String displayformatDateTime() {		  
		return new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(java.util.Calendar.getInstance().getTime());	//获取当前日期时间
	}
	
	/**
	 * 转换字符串格式的日期时间为秒数
	 * @param strDateTime
	 * @return
	 */
	public static long convertDateTimeToSecond(String strDateTime) {
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
	 * 转换字符串格式的日期时间为毫秒数
	 * @param strDateTime
	 * @return
	 */
	public static long convertDateTimeToMillesSecond(String strDateTime) {
		//转换日期时间为秒数，因是毫秒，所以需除以1000
		long sModifyTime = 0L;
		try {
			sModifyTime = ((Date)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDateTime)).getTime();
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
	public static String convertSecondToDateTime(long longtime) {	
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
	 * 字符串转换为浮点数，金额，两位小数
	 * @param strString
	 * @return
	 */
	public static String convertString2Float(String strString) {
		String strRes = "";
		if(strString == null) {
			strRes = "0.00";
		} else {
			strRes = String.valueOf(Float.parseFloat((strString) + "e-2"));
			if(strString.charAt(strString.length() - 1) == '0') {
				strRes += "0";
			}
		}
		return strRes;
	}
	
	/**
	 * 对字符串过长或无内容时的显示进行长度控制
	 * @param strInfo
	 * @return
	 */
	public static String StringNotNull(String strInfo, int length) {
		if(strInfo == null || "null".equals(strInfo)) {
			strInfo = "";
		} else {
			if(strInfo.length() > (length + 1)) {
				strInfo = strInfo.substring(0,length) + "...";
			}
		}
		return strInfo;
	}
	
	/**
	 * 处理Map映射时无内容显示null的情况
	 * @param obj
	 * @return
	 */
	public static String MapObjectNull(Object obj) {
		String strRes = "";
		if(obj == null) {
			strRes = "";
		} else {
			strRes = String.valueOf(obj);
		}
		return strRes;
	}
	
	/**
	 * 十进制数字转换为十六进制
	 * 不足八位表示，补0
	 * @param strDec
	 * @return
	 */
	public static String DecToHex(String strDec) {
		String strRes = "";
		if(strDec == null || "".equals(strDec)) strDec = "0";
		strRes = Integer.toHexString(Integer.parseInt(strDec));
		String strTmp = "00000000";
		strRes = strTmp.substring(strRes.length()) + strRes;
		
		return strRes.toUpperCase();
	}
	
	/**
	 * 位与运算
	 * 判断第一个值 是否包含  第二个值
	 * @param OneValue
	 * @param TwoValue
	 * @return
	 */
	public static boolean CompareToEqual(String OneHexValue, String TwoHexValue) {
		boolean cflag = true;
		if(OneHexValue != null || TwoHexValue != null) {
			/**
			 * 传入形如0x00000001的值，需要处理掉0x
			 */
			String strHex = "0x";
			if(OneHexValue.indexOf(strHex) != -1) {
				OneHexValue = OneHexValue.substring(strHex.length());
			}
			if(TwoHexValue.indexOf(strHex) != -1) {
				TwoHexValue = TwoHexValue.substring(strHex.length());
			}
			if((Integer.parseInt(OneHexValue, 16) & Integer.parseInt(TwoHexValue, 16)) == Integer.parseInt(TwoHexValue, 16)) {
				cflag = true;
			} else {
				cflag = false;
			}		
		}
		return cflag;
	}
	
	/**
	 * 比较两个数组，从第一个数组中去掉包含第二个数组中相同的元素
	 * @param arg0 原数组
	 * @param arg1 待比较数组
	 * @return
	 */
	public static String getCompareToArray(String[] arg0, String[] arg1) {		
		String strRes = "";		
		//直接转的话，生成的List不支持removeAll
        List<String> ls01 = new ArrayList<String>();
        for(String str : arg0) {
        	ls01.add(str);
        }
       
        //同上      
        List<String> ls02 = new ArrayList<String>();      
        for(String str : arg1) {
        	ls02.add(str);
        }
                
        //去除arr01中存在于arr02中的元素
        ls01.removeAll(ls02);
              
        // 取得结果      
        Object[] arr03 = ls01.toArray();      
        for(Object str : arr03) {
            strRes += str.toString() + ",";
        }
        if(strRes.lastIndexOf(",") != -1) strRes = strRes.substring(0, strRes.lastIndexOf(","));
        
        return strRes;
	}
	
	/**
	 * 将一行字符串转换成一个整型数组
	 */
    public static int[] sortNum(String string) {
    	//这里用到了分隔符比喻string.split(","，把string字符串用逗号隔开；
    	String str[] = string.split(",");
    	int a[] = new int[str.length];
    	for(int i = 0; i < str.length; i++) {
    		a[i] = Integer.parseInt(str[i]);
    	}
    	return  a;
    }
    
    /**
     * 文件重命名策略
     * @param file ：文件
     * @param strDesc ： 截取开始字符
     * @param strExtand ：追加字符
     * @return
     */
    public static File rename(File file, String strDesc, String strExtand) {
    	String body = "";
		String ext = "";
		int pot = file.getName().lastIndexOf(strDesc);
		if(pot != -1) {
			body = file.getName().substring(0, pot) + strExtand + "";		//追加字符至文件名中
			ext = file.getName().substring(pot + strDesc.length() - 1);
		} else {
			body = file.getName().substring(0, pot) + strExtand + "";
			ext = "";
		}
		
		String newName = body + ext;
		file = new File(file.getParent(), newName);
		return file;
	}
    
    /**
     * 判断是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){ 
    	Pattern pattern = Pattern.compile("[0-9]*"); 
    	return pattern.matcher(str).matches(); 
    }
    
    /**
     * 按字节判断检测
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION 
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	
    /**
     * 判断是否为中文
     * 如果参数为字符串，需要调用字节检测函数
     * @param strName
     */
	public static boolean isChinese(String strName) {
		boolean res = false;
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if(isChinese(c) == true) {
				res = isChinese(c);
			} else {
				res = isChinese(c);
			}
		}
		return res;
	}
    
    /**
	 * Java运行时环境版本[java.version]
	 * @return
	 */
	public static String getJREVersion() {
		return System.getProperty("java.version");
	}
	
	/**
	 * Java运行时环境供应商[java.vendor]
	 * @return
	 */
	public static String getJREVendor() {
		return System.getProperty("java.vendor");
	}
	
	/**
	 * Java供应商的URL[java.vendor.url]
	 * @return
	 */
	public static String getVendorURL() {
		return System.getProperty("java.vendor.url");
	}
	
	/**
	 * Java安装目录[java.home]
	 * @return
	 */
	public static String getJavaHome() {
		return System.getProperty("java.home");
	}
	
	/**
	 * Java类格式版本号[java.class.version]
	 * @return
	 */
	public static String getClassVersion() {
		return System.getProperty("java.class.version");
	}
	
	/**
	 * Java类路径[java.class.path]
	 * @return
	 */
	public static String getClassPath() {
		return System.getProperty("java.class.path");
	}
	
	/**
	 * 操作系统的名称[os.name]
	 * @return
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}
	
	/**
	 * 操作系统的架构[os.arch]
	 * @return
	 */
	public static String getOSArch() {
		return System.getProperty("os.arch");
	}
	
	/**
	 * 操作系统的版本[os.version]
	 * @return
	 */
	public static String getOSVersion() {
		return System.getProperty("os.version");
	}
	
	/**
	 * 用户的账户名称[user.name]
	 * @return
	 */
	public static String getUserName() {
		return System.getProperty("user.name");
	}
	
	/**
	 * 用户的主目录[user.home]
	 * @return
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}
	
	/**
	 * 用户的当前工作目录[user.dir]
	 * @return
	 */
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * Java虚拟机规范版本[java.vm.specification.version]
	 * @return
	 */
	public static String getVMSpecVersion() {
		return System.getProperty("java.vm.specification.version");
	}
	
	/**
	 * Java虚拟机规范供应商[java.vm.specification.vendor]
	 * @return
	 */
	public static String getVMSpecVendor() {
		return System.getProperty("java.vm.specification.vendor");
	}
	
	/**
	 * Java虚拟机规范名称[java.vm.specification.name]
	 * @return
	 */
	public static String getVMSpecName() {
		return System.getProperty("java.vm.specification.name");
	}
	
	/**
	 * Java虚拟机实现版本[java.vm.version]
	 * @return
	 */
	public static String getVMVersion() {
		return System.getProperty("java.vm.version");
	}
	
	/**
	 * Java虚拟机实现供应商[java.vm.vendor]
	 * @return
	 */
	public static String getVMVendor() {
		return System.getProperty("java.vm.vendor");
	}
	
	/**
	 * Java虚拟机实现名称[java.vm.name]
	 * @return
	 */
	public static String getVMName() {
		return System.getProperty("java.vm.name");
	}
	
	/**
	 * Java运行时环境规范版本[java.specification.version]
	 * @return
	 */
	public static String getJRESpecVersion() {
		return System.getProperty("java.specification.version");
	}
	
	/**
	 * Java运行时环境规范供应商[java.specification.vendor]
	 * @return
	 */
	public static String getJRESpecVendor() {
		return System.getProperty("java.specification.vendor");
	}
	
	/**
	 * Java运行时环境规范名称[java.specification.name]
	 * @return
	 */
	public static String getJRESpecName() {
		return System.getProperty("java.specification.name");
	}
	
	/**
	 * 加载库时搜索的路径列表[java.library.path]
	 * @return
	 */
	public static String getLibPath() {
		return System.getProperty("java.library.path");
	}
	
	/**
	 * 默认的临时文件路径[java.io.tmpdir]
	 * @return
	 */
	public static String getIOTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
	/**
	 * 要使用的JIT编译器的名称[java.compiler]
	 * @return
	 */
	public static String getJITCompilerName() {
		return System.getProperty("java.compiler");
	}
	
	/**
	 * 一个或多个扩展目录的路径[java.ext.dirs]
	 * @return
	 */
	public static String getExtDirs() {
		return System.getProperty("java.ext.dirs");
	}
	
	/**
	 * 文件分隔符[file.separator]
	 * @return
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * 路径分隔符[path.separator]
	 * @return
	 */
	public static String getPathSeparator() {
		return System.getProperty("path.separator");
	}
	
	/**
	 * 换行分隔符[line.separator]
	 * @return
	 */
	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}
	
	// 打印字符串返回给js
	public static void printdata(String jsonss) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json,charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-cache");// 清除缓存
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(jsonss);
			out.flush();
			out.close();
		} catch (Exception e) {
				// TODO: handle exception
		}

	}
	
	
	
	//连接数据库
	public static Connection conn(){
		Connection conn = null;
		try {
			Properties props = new Properties();
			String path = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
			props.load(new FileInputStream(path + "/path.properties"));
			conn = DriverManager.getConnection(
					props.getProperty("jdbc.soft.url"),
					props.getProperty("jdbc.soft.username"),
					props.getProperty("jdbc.soft.password"));
			conn.setAutoCommit(false);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	//把yyyymmdd转成yyyy-MM-dd格式
	public static String formatDate(String str){
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
	    SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
	    String sfstr = "";
	    try {
	    	sfstr = sf2.format(sf1.parse(str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sfstr;
	}
	
	public static void main(String args[]){
		System.out.println(ConvertUtil.formatDate("20120101"));
	}
}
