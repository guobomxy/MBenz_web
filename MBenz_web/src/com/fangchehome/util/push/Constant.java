package com.fangchehome.util.push;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Constant {
	public final static String LOGIN_CAPTCHA = "LOGIN_CAPTCHA";

	public static Map<String, Object> cacheMap = new HashMap<String, Object>();

	/**
	 * 域名或IP地址，可更换
	 */
	public static String DOMAINNAME = "";
	
	/**
	 * 主域，该URL为唯一请求地址，由Apache服务器接收分配
	 * 正式端口为：80，省略，测试端口为：8080
	 * 可更换
	 */
	public static String DOMAIN = "";
	
	/**
	 * Tomcat服务器1，负责WWW站点和所有下载服务，端口为：9080
	 * web应用目录 对应：appStore
	 */
	public static String WAPDOMAIN = "";
	
	/**
	 * Tomcat服务器2，负责WAP站点、Android本地客户端请求和资源管理后台服务，端口为：28080
	 * web应用目录对应：app
	 * 因主域访问直接返回WWW站点，所以在Tomcat配置server.xml文件配置为根路径 /
	 */
	public static String WWWDOMAIN = "";
	
	/**
	 * APK资源存放目录
	 */
	public String LOCAL_PATH = "";

	/**
	 * APK程序存放目录
	 */
	public static final String SOFT_PATH = "soft/";

	/**
	 * APK上传图片存放目录
	 */
	public static final String UPLOAD_PATH = "upload/";

	/**
	 * APK上传图标存放目录
	 */
	public static final String ICON_PATH = "upload/icon/";

	/**
	 * APK上传截图存放目录
	 */
	public static final String MEMO_IMAGE_PATH = "memo_image/";
	
	/**
	 * 市场样式版本上传存放目录
	 */
	public static final String MARKETSTYLE_PATH = "market/";
	
	/**
	 * 市场样式版本上传图标存放目录
	 */
	public static final String MARKETSTYLE_ICON_PATH = "market/icon/";
	
	/**
	 * 市场插件上传存放目录
	 */
	public static final String PLUGIN_PATH = "plugin/";
	
	/**
	 * 市场插件上传图标存放目录
	 */
	public static final String PLUGIN_ICON_PATH = "plugin/icon/";

	/**
	 * APK截图文件格式
	 */
	public static final String[] imageType = { "jpg", "png", "gif" };
	
	/**
	 * PUSH日志统计目录
	 */
	public String PUSH_COUNT_PATH = "";

	/**
	 * PUSH请求数日志文件存放目录(新)
	 */
	public static final String PUSHREQ_FILE_PATH_NEW = "pushcount/pushreqNew";
	
	/**
	 * PUSH请求数日志文件存放目录(旧)
	 */
	public static final String PUSHREQ_FILE_PATH_OLD = "pushcount/pushreqOld";
	
	/**
	 * PUSH下载数日志文件存放目录
	 */
	public static final String PUSHDOWN_FILE_PATH = "pushcount/pushdown";
	
	/**
	 * 用户信息加密方式
	 */
	public static final String passSalt = "KERMIT";
	
	/**
	 * 用户登录密码加密码KEY
	 */
	public static final String USERLOGINKEY = "USERLOGINKEY";
	
	/**
	 * 用户头像
	 */
	public static final String USER_IMAGE_PATH = "upload/userimage/";
	


	/**
	 * 构造方法，加载配置文件
	 */
	public Constant() {
		Properties props = new Properties();
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		try {
			props.load(new FileInputStream(path + "/path.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DOMAINNAME = props.getProperty("DOMAINNAME");
		DOMAIN = props.getProperty("DOMAIN");
		WAPDOMAIN = props.getProperty("WAPDOMAIN");
		WWWDOMAIN = props.getProperty("WWWDOMAIN");
		LOCAL_PATH = props.getProperty("LOCAL_SOFT_PATH");
		PUSH_COUNT_PATH = props.getProperty("PUSHCOUNT");
	}
	
	
	
	/**
	 * 积分类型：值
	 */
	public static String[] markValue = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
	
	/**
	 * 积分类型：文本
	 */
	public static String[] markText = {"注册","登陆","签到","点击","下载","启动","评论","分享","绑定","上传","充值","消费","活动","任务","社交"};
	
	/**
	 * 消息类型：值
	 */
	public static String[] messageValue = {"0","1"};
	
	/**
	 * 消息类型：文本
	 */
	public static String[] messageText = {"系统","用户行为"};
	
	/**
	 * push消息类型：值
	 */
	public static String[] pushValue = {"0","1"};
	
	/**
	 * push消息类型：文本
	 */
	public static String[] pushText = {"外部url","启动市场"};
	
	
	
	/**
	 * 根据类型值返回类型文本
	 * @param typevar ：当前值
	 * @param arrayText ：数组文本
	 * @param arrayValue ：数组值
	 * @return
	 */
	public static String getSwitchType(String typevar, String[] arrayText, String[] arrayValue) {
		String strRes = "";
		for(int aI = 0; aI < arrayValue.length; aI++) {
			if(typevar.equals(arrayValue[aI])) {
				strRes = arrayText[aI].toString();
				break;
			}
		}
		
		return strRes;
	}
}
