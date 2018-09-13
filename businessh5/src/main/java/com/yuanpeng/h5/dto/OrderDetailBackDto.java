package com.yuanpeng.h5.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetailBackDto implements Serializable {

	private Integer id;

	private String goodName;

	private String standard;

	private String status;

	private BigDecimal price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getPrice() {
		return price.setScale(2);
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}


}
