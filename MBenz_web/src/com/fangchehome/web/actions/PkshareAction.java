package com.fangchehome.web.actions;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts2.ServletActionContext;

import com.fangchehome.hibernate.HibernateCommonUtil;

public class PkshareAction extends Action{
	
	private String pkid;//pkid
	private String pid;//产品id
	
	public static void printdata(String json) {
		System.out.println(json);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json,charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(json);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 200 请求成功
	 * 300 非法请求数据
	 * 400 程序出错
	 * 500 抽奖次数不足
	 * 600 非法用户
	 * 700 配置下载软件出错
	 * 800 已经存在的数据
	 * 900 当天已经分享
	 * */
	
	public void killcacheKey(){
		/*try {
			String cacheKey1="downlist"+ad_id;
			String cacheKey2="lotterylist"+ad_id;
			Cache cache = CacheUtils.getLongCache();
			cache.remove(cacheKey1);
			cache.remove(cacheKey2);
			Struts2Utils.getResponse().setContentType("text/html;charset=UTF-8");
			Struts2Utils.getResponse().getWriter().print("缓存重置成功!");
		} catch (Exception e) {
			try {
				Struts2Utils.getResponse().setContentType("text/html;charset=UTF-8");
				Struts2Utils.getResponse().getWriter().print("缓存重置失败，请联系开发人员!"+e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}*/
	}
	
	//PK页面请求
	public void pkproduct(){
		try{
			if(StringUtils.isBlank(pkid)){
				this.printdata("{code:\"300\"}");//非法请求数据
				return;
			}
			
			//查询PK产品的icon
			//先查缓存
			List<Object> appicolist=null;
			Cache cache = CacheUtils.getLongCache();
			String cacheKey="pklist"+pkid;
			Element result = cache.get(cacheKey);
			if (result != null) {
				appicolist = (List)result.getObjectValue();
			}else{
				String pidsql = "SELECT b.id,b.type FROM each_market.activity_pk_detail a,each_market.all_pro_view b WHERE a.pk_id = "+pkid+" AND a.product_id = b.id ORDER BY sequence";
				List<Map> pidslist = (List<Map>) HibernateCommonUtil.getSQL2MapList(pidsql);
				if(pidslist !=null && pidslist.size()>0){
					List<Object> picolist = new ArrayList<Object>();
					Map<String,Object> icomap = new HashMap<String,Object>();
					for(Iterator iterator = pidslist.iterator(); iterator.hasNext();){
						Map<String, Object> map = (Map) iterator.next();
						int pid = Integer.parseInt(map.get("id").toString());
						int type = Integer.parseInt(map.get("type").toString());
						String psql = "";
						String icon="";
						switch(type){
						case(1) : psql = "select concat(icon_url,'') icon_url from each_market.each_product_360 where id = "+pid; 
						break;
						case(2) : psql = "select concat(icon_url,'') icon_url from each_market.each_product_yingyongbao where id = "+pid; 
						break;
						case(3) : psql = "select concat(icon_url,'') icon_url from each_market.each_product_baidu where id = "+pid; 
						break;
						case(4) : psql = "select concat(icon_url,'') icon_url from each_market.each_product_wandoujia where id = "+pid; 
						break;
						case(5) : psql = "select concat(icon_url,'') icon_url from each_market.each_product_xiaomi where id = "+pid; 
						break;
						case(6) : psql = "select concat(icon_url,'') icon_url from each_market.each_product_iphone where id = "+pid; 
						break;
						case(7) : psql = "SELECT concat(icon_url,'') icon_url FROM each_market.base_product_version where product_id = "+pid+" AND main = true"; 
						break;
						}
						List<Map> iconlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(psql);
						icon = iconlist.get(0).get("icon_url").toString();
						icomap.put(String.valueOf(pid), icon);
					}//for end
					picolist.add(icomap);
					appicolist = picolist;
					cache.put(new Element(cacheKey, picolist));
				}
			}
			//查询pkid下pk参与的人数和百分比，拼装json
			String sql = "SELECT partake_num con,product_id proid,clientname cname FROM each_market.activity_pk_detail WHERE pk_id = "+pkid+" ORDER BY sequence";
			List<Map> pks = (List<Map>) HibernateCommonUtil.getSQL2MapList(sql);
			Map<String,Object> imap = (Map<String,Object>)appicolist.get(0);
			String jsons = "";
			if(pks !=null && pks.size()>0){
				if(pks.size()==2){
					Map<String,Object> pkde1 = pks.get(0);
					Map<String,Object> pkde2 = pks.get(1);
					String pid1 = pkde1.get("proid").toString();
					String pid2 = pkde2.get("proid").toString();
					String ico1 = imap.get(pid1).toString();
					String ico2 = imap.get(pid2).toString();
					String name1 = pkde1.get("cname").toString();
					String name2 = pkde2.get("cname").toString();
					double pco1 = Double.parseDouble(pkde1.get("con").toString());
					double pco2 = Double.parseDouble(pkde2.get("con").toString());
					int per1 = (int)Math.round((pco1/(pco1+pco2)*100));
					int per2 = (int)Math.round((pco2/(pco1+pco2)*100));
					jsons += "{id:"+pid1+",icon:\""+ico1+"\",name:\""+name1+"\",per:\""+per1+"%\"},";
					jsons += "{id:"+pid2+",icon:\""+ico2+"\",name:\""+name2+"\",per:\""+per2+"%\"}";
				}else if(pks.size()==3){
					Map<String,Object> pkde1 = pks.get(0);
					Map<String,Object> pkde2 = pks.get(1);
					Map<String,Object> pkde3 = pks.get(2);
					String pid1 = pkde1.get("proid").toString();
					String pid2 = pkde2.get("proid").toString();
					String pid3 = pkde3.get("proid").toString();
					String ico1 = imap.get(pid1).toString();
					String ico2 = imap.get(pid2).toString();
					String ico3 = imap.get(pid3).toString();
					String name1 = pkde1.get("cname").toString();
					String name2 = pkde2.get("cname").toString();
					String name3 = pkde3.get("cname").toString();
					double pco1 = Double.parseDouble(pkde1.get("con").toString());
					double pco2 = Double.parseDouble(pkde2.get("con").toString());
					double pco3 = Double.parseDouble(pkde3.get("con").toString());
					int per1 = (int)Math.round((pco1/(pco1+pco2+pco3)*100));
					int per2 = (int)Math.round((pco2/(pco1+pco2+pco3)*100));
					int per3 = (int)Math.round((pco3/(pco1+pco2+pco3)*100));
					jsons += "{id:"+pid1+",icon:\""+ico1+"\",name:\""+name1+"\",per:\""+per1+"%\"},";
					jsons += "{id:"+pid2+",icon:\""+ico2+"\",name:\""+name2+"\",per:\""+per2+"%\"},";
					jsons += "{id:"+pid3+",icon:\""+ico3+"\",name:\""+name3+"\",per:\""+per3+"%\"}";
				}
					
			}
			//查询点评内容
			String shsql = "SELECT share_text FROM each_market.activity_pk_info WHERE id = "+pkid;
			List<Map> pstext = (List<Map>) HibernateCommonUtil.getSQL2MapList(shsql);
			String text = pstext.get(0).get("share_text").toString();
			
			//response
			this.printdata("{code:200,text:\""+text+"\",soft:["+jsons+"]}");
		}catch(Exception e){
			e.printStackTrace();
			this.printdata("{code:\"400\"}");
		}
	}
	public void proplus(){//点赞加起来
		String psql = "UPDATE each_market.activity_pk_detail SET partake_num = partake_num+1 WHERE pk_id = "+pkid+" AND product_id = "+pid;
		HibernateCommonUtil.sqlupdate(psql);
		this.printdata("{code:200}");
	}
	

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
}

