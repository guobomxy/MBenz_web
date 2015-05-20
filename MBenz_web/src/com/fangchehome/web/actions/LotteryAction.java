package com.fangchehome.web.actions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts2.ServletActionContext;

import com.fangchehome.hibernate.HibernateCommonUtil;
import com.fangchehome.util.Struts2Utils;

public class LotteryAction extends Action{
	
	public static int lotteryTimes=3;//抽奖次数
	
	public static int sharegold=50;//分享金币个数
	
	private String ad_id;//ad_id
	
	private String user_code;//user_code
	
	private String gold;//gold
	
	private String share_type;//share_type
	
	private String share_id;
	
	private String product_id;
	
	private String son = "";
	
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
		try {
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
		}
	}
	
	//刮奖请求回馈
	public void lottery(){
		try{
			if(StringUtils.isBlank(ad_id) || StringUtils.isBlank(user_code) ){
				this.printdata("{code:\"300\"}");//非法请求数据
				return;
			}
			
			//查询用户金币
			int ugold=0;
			String usergold = "SELECT * FROM each_market.base_user_info WHERE user_code='"+user_code+"'";
			List<Map> usergoldlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(usergold);
			if(usergoldlist !=null && usergoldlist.size()>0){
				Map<String, Object> map = (Map) usergoldlist.get(0);
				ugold=Integer.parseInt(map.get("user_gold").toString());
			}else{
				this.printdata("{code:\"600\"}");//非法用户
				return;
			}
			
			//查询下载产品
			List<Map> downlist=null;
			String downproduct="SELECT a.gold,a.product_id,a.showname,b.icon_url,b.name,b.package_name," +
					"b.name,b.hits,b.size,b.units,b.version,b.last_modify_time,b.owner,b.path_file  "+
							"FROM each_market.activity_product_gold a,each_market.base_product_version b  "+
							"WHERE a.product_id=b.product_id AND b.main=1 AND b.hide=0 AND a.ad_id="+ad_id+" order by a.gold";
			Cache cache = CacheUtils.getLongCache();
			String cacheKey="downlist"+ad_id;
			Element result = cache.get(cacheKey);
			if (result != null) {
				downlist=(List) result.getObjectValue();
			}else{
				String sql = "SELECT * FROM each_market.activity_level_pr WHERE ad_id="+ad_id;
				downlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(downproduct);
				cache.put(new Element(cacheKey, downlist));
			}
			String soft="";
			if(downlist!=null && downlist.size()>0){
				for(int i=0;i<downlist.size();i++){
					//查询是否已经下载领取过金币
					Map<String, Object> map = (Map) downlist.get(i);
					String isg="select * from each_market_log.activity_product_gold_detail " +
						"where ad_id="+ad_id+" and user_code='"+user_code+"' " +
								" and product_id="+Integer.parseInt(map.get("product_id").toString());;
					List<Map> isglist = (List<Map>) HibernateCommonUtil.getSQL2MapList(isg);
					String isdown="0";
					if(isglist!=null && isglist.size()>0){
						isdown="1";
					}
					soft+="{gold:\""+map.get("gold").toString()+"\"," +
							"product_id:\""+map.get("product_id").toString()+"\"," +
							"softname:\""+map.get("showname").toString()+"\"," +
							"hits:\""+map.get("hits").toString()+"\"," +
							"softsize:\""+map.get("size").toString()+"\"," +
							"units:\""+map.get("units").toString()+"\"," +
							"softversion:\""+map.get("version").toString()+"\"," +
							"softupdatetime:\""+map.get("last_modify_time").toString()+"\"," +
							"author:\""+map.get("owner").toString()+"\"," +
							"icon_url:\""+map.get("icon_url").toString()+"\"," +
							"path_file:\""+map.get("path_file").toString()+"\"," +
							"isdown:\""+isdown+"\"," +
							"package_name:\""+map.get("package_name").toString()+"\"}";
					if(i<downlist.size()-1){
						soft+=",";
					}
				}
			}else{
				this.printdata("{code:\"700\"}");//配置下载软件出错
				return;
			}
			
			
			//查询分享结果
			String share="";
//			List<Map> sharelist=null;
//			String sharesql="select * from each_market_log.activity_share_user " +
//					"where ad_id="+ad_id+" and user_code='"+user_code+"' order by status desc";
//			sharelist=(List<Map>) HibernateCommonUtil.getSQL2MapList(sharesql);
//			if(sharelist !=null && sharelist.size()>0){
//				if(sharelist.size()>4){
//					for(int i=0;i<4;i++){
//						Map<String, Object> map = (Map) sharelist.get(i);
//						share+="{share_id:\""+map.get("id").toString()+"\"," +
//								"status:\""+map.get("status").toString()+"\"}";
//						if(i<downlist.size()-1){
//							share+=",";
//						}
//					}
//				}else{
//					for(int i=0;i<sharelist.size();i++){
//						Map<String, Object> map = (Map) sharelist.get(i);
//						share+="{share_id:\""+map.get("id").toString()+"\"," +
//								"status:\""+map.get("status").toString()+"\"}";
//						if(i<downlist.size()-1){
//							share+=",";
//						}
//					}
//				}
//			}
			
			//返回用户的刮奖次数
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
			String utimes = "SELECT * FROM each_market_log.activity_scratch_detail WHERE " +
					"ad_id= "+ad_id+" and user_code='"+user_code+"' and" +
							" DATE_FORMAT(join_time,'%Y-%m-%d')='"+dformat.format(new Date())+"'";
			List<Map> utimeslist = (List<Map>) HibernateCommonUtil.getSQL2MapList(utimes);
			int times=0;
			if(utimeslist !=null && utimeslist.size()>0){
				times=utimeslist.size();
			}
			if(times>=lotteryTimes){
				this.printdata("{code:\"500\",ugold:\""+ugold+"\",times:\""+times+"\",soft:["+soft+"],share:["+share+"]}");//抽奖次数不足
			}else{
				this.printdata("{code:\"200\",ugold:\""+ugold+"\",times:\""+times+"\",soft:["+soft+"],share:["+share+"]}");//返回抽奖次数
			}
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//异常
		}
	}
	//返回中奖信息
	public void golotteryin(){
		try{
			if(StringUtils.isBlank(ad_id)){
				this.printdata("{code:\"300\"}");//非法请求数据
				return;
			}
			
			List<Map> prList=null;
			Cache cache = CacheUtils.getLongCache();
			String cacheKey="lotterylist"+ad_id;
			Element result = cache.get(cacheKey);
			if (result != null) {
				prList=(List) result.getObjectValue();
			}else{
				String sql = "SELECT * FROM each_market.activity_level_pr WHERE ad_id="+ad_id;
				prList = (List<Map>) HibernateCommonUtil.getSQL2MapList(sql);
				cache.put(new Element(cacheKey, prList));
			}
			if(prList!=null && prList.size()>0){
				List<Integer> l=new ArrayList<Integer>();//刮奖奖池
				for(int i=0;i<prList.size();i++){
					Map<String, Object> map = (Map) prList.get(i);
					int gold=Integer.parseInt(map.get("gold").toString());//金币
					int pr=Integer.parseInt(map.get("pr").toString());//概率(%)
					for(int j=0;j<pr;j++){
						l.add(gold);
					}
				}
				//打乱顺序，开始抽奖
				Collections.shuffle(l);
				int x=(int)(Math.random()*99);
				//返回抽奖结果
				this.printdata("{code:\"200\",gold:\""+l.get(x)+"\"}");//返回中奖数值
			}else{
				//返回异常
				this.printdata("{code:\"400\"}");//返回异常
			}
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//返回异常
		}
	}
	
	//返回刮奖次数
	public void backtime(){
		try{
			if(StringUtils.isBlank(ad_id) || StringUtils.isBlank(user_code)){
				this.printdata("{code:\"300\"}");//非法数据
				return;
			}
			//用户的刮奖次数
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
			
			String utimes = "SELECT * FROM each_market_log.activity_scratch_detail WHERE ad_id="+ad_id+" " +
							" and user_code='"+user_code+"' and " +
									"DATE_FORMAT(join_time,'%Y-%m-%d')='"+dformat.format(new Date())+"'";
			List<Map> utimeslist = (List<Map>) HibernateCommonUtil.getSQL2MapList(utimes);
			this.printdata("{code:\"200\",times:\""+utimeslist.size()+"\"}");//抽奖次数不足
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//返回异常
		}
	}
	
	//刮奖中奖入库
	public void lotteryin(){
		try{
			if(StringUtils.isBlank(ad_id) || StringUtils.isBlank(user_code) || StringUtils.isBlank(gold)){
				this.printdata("{code:\"300\"}");//非法数据
				return;
			}
			
			//用户的刮奖次数
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String utimes = "SELECT * FROM each_market_log.activity_scratch_detail WHERE ad_id="+ad_id+" " +
							" and user_code='"+user_code+"' and " +
									"DATE_FORMAT(join_time,'%Y-%m-%d')='"+dformat.format(new Date())+"'";
			List<Map> utimeslist = (List<Map>) HibernateCommonUtil.getSQL2MapList(utimes);
			int times=0;
			if(utimeslist !=null && utimeslist.size()>0){
				times=utimeslist.size();
			}
			if(times>=lotteryTimes){
				//返回数据，不进行中奖入库
				this.printdata("{code:\"500\",times:\""+times+"\"}");//抽奖次数不足
			}else{
				//刮奖信息入库
				String insertscratch = "INSERT INTO each_market_log.activity_scratch_detail " +
						"(ad_id,user_code,gold,join_time) " +
						"VALUES ("+ad_id+",'"+user_code+"',"+gold+",'"+dformat2.format(new Date())+"')";
				int k = HibernateCommonUtil.sqlupdate(insertscratch);
				
				//请求服务端缓存
				serverGold(user_code,"首页刮奖活动中奖",gold);
				
				//同时更新用户表
				this.updateGoldByUser(user_code, gold);
				this.insertlog(user_code, gold, "首页刮奖活动刮奖获得"+gold+"金币",3,ad_id);
				this.printdata("{code:\"200\",times:\""+(times+1)+"\"}");
			}
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//返回异常
		}
	}
	
	public void insertlog(String user_code,String gold,String memo,int type,String signId){
		if(StringUtils.isBlank(gold)||gold.equals("0")){
			return;
		}
		SimpleDateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String insertscratch = "INSERT INTO each_market_log.activity_gold_flow " +
			"(user_code,gold_val,memo,create_date,type,signId) " +
			"VALUES ('"+user_code+"',"+gold+",'"+memo+"','"+dformat2.format(new Date())+"',"+type+","+signId+")";
		int k = HibernateCommonUtil.sqlupdate(insertscratch);
	}
	
	//分享信息入库---未领取(后期修改成直接领取)
	public void shareGold(){
		try{
			if(StringUtils.isBlank(ad_id) || StringUtils.isBlank(user_code) || StringUtils.isBlank(share_type)){
				this.printdata("{code:\"300\"}");//非法数据
				return;
			}
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//查询是否当天是否已经有数据
			String stimes = "SELECT * FROM each_market_log.activity_share_user WHERE " +
					"ad_id="+ad_id+" and user_code='"+user_code+"' and " +
							"DATE_FORMAT(share_time,'%Y-%m-%d')='"+dformat.format(new Date())+"'";
			List<Map> stimeslist = (List<Map>) HibernateCommonUtil.getSQL2MapList(stimes);
			if(stimeslist !=null && stimeslist.size()>0){
				this.printdata("{code:\"900\"}");//当天已经分享
				return;
			}else{
				//分享信息入库
				String status="1";
				String insertscratch = "INSERT INTO each_market_log.activity_share_user " +
						"(ad_id,user_code,share_type,status,share_time,gold) " +
						"VALUES ("+ad_id+",'"+user_code+"',"+share_type+",'"+status+"','"+dformat2.format(new Date())+"',"+sharegold+")";
				System.out.println(insertscratch);
				int k = HibernateCommonUtil.sqlupdate(insertscratch);
				
				//请求服务端缓存
				serverGold(user_code,"首页刮奖活动分享",String.valueOf(sharegold));
				
				//同时更新用户表
				String isql="UPDATE each_market.base_user_info SET " +
						"user_gold=user_gold+"+sharegold+" WHERE user_code='"+user_code+"'";
				int ik = HibernateCommonUtil.sqlupdate(isql);
				this.insertlog(user_code, sharegold+"", "首页刮奖活动分享获得"+sharegold+"金币",3,ad_id);
				this.printdata("{code:\"200\"}");
			}
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//返回异常
		}
	}
	
	//分享获取金币入库---领取(后期剔除)
	public void shareGoldin(){
		try{
			if(StringUtils.isBlank(share_id) || StringUtils.isBlank(user_code)){
				this.printdata("{code:\"300\"}");//非法数据
				return;
			}
			
			//校验这个share_id是否属于user_code
			String testsql="select * from each_market_log.activity_share_user " +
					"where id="+share_id+"and user_code='"+user_code+"'";
			List<Map> tlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(testsql);
			if(tlist!=null && tlist.size()>0){
				String sql="UPDATE each_market_log.activity_share_user SET status='1' where id"+share_id;
				int k = HibernateCommonUtil.sqlupdate(sql);
				
				//同时更新用户表
				String isql="UPDATE each_market.base_user_info SET " +
						"user_gold=user_gold+"+sharegold+" WHERE user_code='"+user_code+"'";
				int ik = HibernateCommonUtil.sqlupdate(isql);
			}
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//返回异常
		}
	}
	
	//下载获取金币入库
	public void productGoldin(){
		try{
			if(StringUtils.isBlank(ad_id) || StringUtils.isBlank(user_code)|| StringUtils.isBlank(product_id)|| StringUtils.isBlank(gold)){
				this.printdata("{code:\"300\"}");//非法数据
				return;
			}
			SimpleDateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String testsql="select * from each_market_log.activity_product_gold_detail " +
					"where ad_id="+ad_id+" and user_code='"+user_code+"' and product_id="+product_id;
			List<Map> tlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(testsql);
			if(tlist!=null && tlist.size()>0){
				this.printdata("{code:\"800\"}");//已经存在的记录
			}else{
				//下载信息入库
				String insertscratch = "INSERT INTO each_market_log.activity_product_gold_detail " +
						"(ad_id,user_code,product_id,gold,create_time) " +
						"VALUES ("+ad_id+",'"+user_code+"',"+product_id+","+gold+",'"+dformat2.format(new Date())+"')";
				int k = HibernateCommonUtil.sqlupdate(insertscratch);
				
				//请求服务端缓存
				serverGold(user_code,"活动下载奖励",gold);
				
				
				//同时更新用户表
				this.updateGoldByUser(user_code, gold);
				this.insertlog(user_code, gold, "活动下载奖励"+gold+"金币",3,ad_id);
				this.printdata("{code:\"200\"}");
			}
		}catch (Exception e) {
			e.printStackTrace();
			//返回异常
			this.printdata("{code:\"400\"}");//返回异常
		}
	}
	
	public void upgold(){//请求用户金币
		int ugold=0;
		String usergold = "SELECT * FROM each_market.base_user_info WHERE user_code='"+user_code+"'";
		List<Map> usergoldlist = (List<Map>) HibernateCommonUtil.getSQL2MapList(usergold);
		if(usergoldlist !=null && usergoldlist.size()>0){
			Map<String, Object> map = (Map) usergoldlist.get(0);
			ugold=Integer.parseInt(map.get("user_gold").toString());
			this.printdata("{code:\"200\",ugold:\""+ugold+"\"}");
		}else{
			this.printdata("{code:\"600\"}");//非法用户
			return;
		}
	}
	
	//根据用户（String）ID更新用户金币
	public void updateGoldByUser(String user_code,String num){
		String sql="UPDATE each_market.base_user_info SET user_gold=user_gold+"+num+" WHERE user_code='"+user_code+"'";
		int k = HibernateCommonUtil.sqlupdate(sql);
	}
	
	//请求服务金币流水记录缓存
	public void serverGold(String ucode,String memo,String gds){
		if(StringUtils.isBlank(gds)||gds.equals("0")){
			return;
		}
		SimpleDateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		URL url;
		//String urlString = "http://192.168.0.122:9066/newUser/exchange";
		String urlString = "http://union.zmapp.com/zmUser/exchange";
		String data = "{\"musercode\":\""+ucode+"\",\"time\":\""+dformat2.format(new Date())+"\",\"gold\":\""+gds+"\",\"memo\":\""+memo+"\"}";
		String method ="webGoldflow";
				HttpURLConnection urlConnection;
				InputStream in = null;
				ByteArrayOutputStream baos = null;
				try {
					url = new URL(urlString);
					String params = "{\"id\":\"1\",\"jsonrpc\":\"2.0\",\"method\":\""+method+"\",\"params\":"+data+"}";
					System.out.println(params);
					byte[] sendcontent = params.getBytes("UTF-8");
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setReadTimeout(1500);
					urlConnection.setRequestProperty("Content-Length", Integer
							.toString(sendcontent.length));
					urlConnection.setRequestProperty("Content-Type",
		              "application/octet-stream");
					urlConnection.setUseCaches(false);
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
//					urlConnection.connect();
					// send request
					DataOutputStream wr = new DataOutputStream(urlConnection
							.getOutputStream());
					wr.write(sendcontent);
					//System.out.println("send data is ");
					wr.flush();
					wr.close();
					// Get Response
					in = urlConnection.getInputStream();
					baos = new ByteArrayOutputStream();
					byte[] buff = new byte[1024];
					int rc = 0;
					while ((rc = in.read(buff, 0, 1024)) > 0) {
						baos.write(buff, 0, rc);
					}
//					byte[] receivedcontent = baos.toByteArray();
//					String result = new String(receivedcontent, "utf-8");
//	 				System.out.println(result);
//					out.write(result);
//	 				JSONObject json = JSONObject.fromObject(result);
//	 				JSONObject t = json.getJSONObject("result");
//	 				String ret = t.getString("ret");
//	 				if(ret.equals("1")){
//	 					String msg = "success";
//	 				}else{
//	 					String msg = "缓存更新失败，请联系管理员";
//	 				}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(in!=null)
							in.close();
						if(baos!=null)
							baos.close();
					} catch (IOException e) {
					}
				}
	}

	public String getAd_id() {
		return ad_id;
	}

	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}

	public String getShare_type() {
		return share_type;
	}

	public void setShare_type(String share_type) {
		this.share_type = share_type;
	}

	public String getShare_id() {
		return share_id;
	}

	public void setShare_id(String share_id) {
		this.share_id = share_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getSon() {
		return son;
	}

	public void setSon(String son) {
		this.son = son;
	}

}

