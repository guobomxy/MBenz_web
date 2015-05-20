package com.fangchehome.util;

import java.util.List;

public class Page {

	List list;
	int	pageSize = 10;
	int	pageIndex = 1;
	int	rowCount = 0;
	int pageCount = 1;
	String url = "";	//请求URI
	String date = "";	//查询日期
	String area = "";
	boolean isFrist;
	boolean isLast;
	boolean ishasNext;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public Page(List list, int pageindex, int pagesize, int rowcount) {
		this.list = list;
		this.rowCount = rowcount;
		this.pageSize = pagesize;
		this.pageIndex = pageindex;
		if (this.pageSize > 0) {
			this.pageCount = (int)Math.ceil( (double)this.rowCount / (double)this.pageSize );
			if( this.pageIndex > pageCount)	{
				this.pageIndex = this.pageCount;
			}
			ishasNext = pageindex < pageCount;
			isFrist = pageindex == 1;
			isLast = pageindex == pageCount;
		}
	}

	public boolean getIsHasNext() {
		return ishasNext;
	}

	public void setIsHasNext(boolean hasNext) {
		this.ishasNext = hasNext;
	}

	public boolean getIsFrist() {
		return isFrist;
	}

	public void setIsFrist(boolean isFrist) {
		this.isFrist = isFrist;
	}

	public boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
}
