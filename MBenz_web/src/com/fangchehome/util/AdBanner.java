package com.fangchehome.util;

public enum AdBanner {
	
	/**
	 * 0、PC版网站轮换广告
	 * 1、客户端首页图片广告[精品推荐]
	 * 2、客户端首页图片广告[时下热门]
	 * 3、客户端类别专题顶部图片广告
	 * 4、客户端类别应用顶部图片广告
	 * 5、客户端类别游戏顶部图片广告
	 * 6、客户端首页列表广告[精品推荐] 6,7,8,9 --》6
	 * 10、客户端首页列表广告[时下热门] 10 --》 10
	 *  
	 * 将四次请求类型整合为一个，即type=6
	 * CLIENT_INDEX_COMMEND_1("客户端首页推荐 第一次请求",6),
	 * CLIENT_INDEX_COMMEND_2("客户端首页推荐 第二次请求",7),
	 * CLIENT_INDEX_COMMEND_3("客户端首页推荐 第三次请求",8),
	 * CLIENT_INDEX_COMMEND_4("客户端首页推荐 第四次请求",9),	
	 */
	TYPE_CLIENT_INDEX("客户端首页图片广告[精品推荐]", 1),
	TYPE_CLIENT_TOPHOT("客户端首页图片广告[时下热门]", 2),
	//TYPE_CLIENT_GAME_AD("客户端类别专题顶部图片广告", 3),
	//TYPE_CLIENT_THEME_AD("客户端类别应用顶部图片广告", 4),
	//TYPE_CLIENT_APP_AD("客户端类别游戏顶部图片广告", 5),
	CLIENT_INDEX_COMMEND("客户端首页列表广告[精品推荐]", 6),
	CLIENT_INDEX_HOT("客户端首页列表广告[时下热门]", 10);

	//private static AdBanner[] URL_LINKS = {TYPE_WEB_HEADER};
	private static AdBanner[] PKG_LINKS = {TYPE_CLIENT_INDEX,
					TYPE_CLIENT_TOPHOT,
					//TYPE_CLIENT_THEME_AD,
					//TYPE_CLIENT_APP_AD,
					//TYPE_CLIENT_GAME_AD,
					CLIENT_INDEX_COMMEND,
					CLIENT_INDEX_HOT};
	
	private String name;
	
	private Integer value;
	
	public static AdBanner getAdBannerByVal(int val){
		AdBanner[] bs=AdBanner.values();
		for(AdBanner b:bs){
			if(val==b.getValue().intValue()){
				return b;
			}
		}
		return null;
	}
	
	/**
	public boolean isUrlLink(){
		AdBanner[] urlLinks=getUrlLinks();
		for(AdBanner b:urlLinks){
			if(this.getValue().intValue()==b.getValue().intValue()){
				return true;
			}
		}
		return false;
	}*/
	
	public boolean isPkgLink(){
		for(AdBanner b:PKG_LINKS){
			if(this.getValue().intValue()==b.getValue().intValue()){
				return true;
			}
		}
		return false;
	}
	/**
	public static AdBanner[] getUrlLinks(){
		return URL_LINKS;
	}*/
	
	private AdBanner(String name,Integer value){
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}
	
	
}
