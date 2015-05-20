package com.fangchehome.util;

public class EnDeCode {
	/**
     * 网址UrlEnCode编码
     * @param strOrg
     * @param strCharset
     * @return
     * @throws Exception
     */
    public String UrlToEncode(String strOrg, String strCharset) {
    	String strRes = "";
    	try {
    		strRes = java.net.URLEncoder.encode(strOrg, strCharset);
    	} catch (Exception e) {
			// TODO: handle exception
		}
    	return strRes;
    }
    
    /**
     * 网址UrlDeCode解码
     * @param strOrg
     * 
     * @param strCharset字符编码
     * @return
     * @throws Exception
     */
    public String UrlToDecode(String strOrg, String strCharset) {
    	String strRes = "";
    	try {
    		strRes = java.net.URLDecoder.decode(strOrg, strCharset);
    	} catch (Exception e) {
			// TODO: handle exception
		}
    	return strRes;
    }

}
