package com.fangchehome.web.actions;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class CacheUtils {
	
	private static CacheManager appCacheManager;
	
	public static synchronized CacheManager getAppCacheManager(){
		if(appCacheManager==null){
			appCacheManager=new CacheManager(CacheUtils.class.getResource("/ehcache/ehcache-application.xml"));
		}
		return appCacheManager;
	}
	
	/**
	 * 短时间,5分钟或者3分钟未访问
	 * @return
	 */
	public static Cache getShortCache(){
		return getAppCacheManager().getCache("SHORT_CACHE");
	}	
	/**
	 * 普通时间，15分钟或者10分钟未访问
	 * @return
	 */
	public static Cache getAvgCache(){
		return getAppCacheManager().getCache("AVG_CACHE");
	}	
	/**
	 * 长时间，30分钟或者20分钟未访问
	 * @return
	 */
	public static Cache getLongCache(){
		return getAppCacheManager().getCache("LONG_CACHE");
	}	
	/**
	 * 较长时间,1小时或者50分钟未访问
	 * @return
	 */
	public static Cache getVeryLongCache(){
		return getAppCacheManager().getCache("VERY_LONG_CACHE");
	}
	/**
	 * 超长时间，5小时或者2.5小时未访问
	 * @return
	 */
	public static Cache getLargeLongCache(){
		return getAppCacheManager().getCache("LARGE_LONG_CACHE");
	}
}
