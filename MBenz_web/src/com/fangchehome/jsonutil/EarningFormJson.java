package com.fangchehome.jsonutil;

public class EarningFormJson {
	private String rec_date;//日期
	private Integer id;//产品ID
	private String name;//产品名称
	private double scale;//分成比例
	private String earning;//收益数值
	private double scaleearning;//分成收益
	
	
	public String getRec_date() {
		return rec_date;
	}
	public void setRec_date(String rec_date) {
		this.rec_date = rec_date;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public String getEarning() {
		return earning;
	}
	public void setEarning(String earning) {
		this.earning = earning;
	}
	public double getScaleearning() {
		return scaleearning;
	}
	public void setScaleearning(double scaleearning) {
		this.scaleearning = scaleearning;
	}
	
}
