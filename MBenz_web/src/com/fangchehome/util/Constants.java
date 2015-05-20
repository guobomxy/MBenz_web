package com.fangchehome.util;

public class Constants {
	/**
	 * 分页每页个数
	 */
	public static int PAGE_SIZE = 10;	//通用

	/**
	 * 渠道
	 */
	public static String[] channelValue = {"40009723251","40009723764","40009723765","40009723766","40009723767","40009724707","40009724708","40009723252","40009725855","40009725856","40009725857","40009726386"};
	public static String[] channelText = {"掌盟游戏运营推广渠道","掌盟游戏运营承开推广渠道","掌盟游戏运营鼎为推广渠道","掌盟游戏运营华掌推广渠道","掌盟游戏运营芹菜推广渠道","掌盟游戏运营兴格推广渠道","掌盟游戏运营聘旭推广渠道","掌盟功能机游戏推广渠道","掌盟游戏运营讯虎推广渠道","掌盟游戏运营瑞伟推广渠道","掌盟游戏运营天富德推广渠道","掌盟软件应用商店游戏大厅推广渠道"};
	
	/**
	 * 分页默认起始页
	 */
	public static int PAGEINDEX = 1;
	/**
	 * 导入的Excel表格中数据起始行
	 */
	public static int ROWINIT = 2;
		
	/**
	 * 用户权限0为管理员
	 */
	public static String FTYPEFORADMIN = "0";
	/**
	 * 用户权限1为普通用户
	 */
	public static String FTYPEFORCOMMON = "1";
	
	/**
	 * 日志文件格式
	 */
	public static String LOGFILEEXT = ".html";
	
	
	/**
	 * 翻译字典中的KEY
	 */
	public static String NEWS_TYPE = "news_type";//新闻类型
	public static String NEWS_TYPE_CATE = "new_type_cate";//新闻类型下二级分类类型
}
