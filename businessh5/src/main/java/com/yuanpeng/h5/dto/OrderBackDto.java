package com.yuanpeng.h5.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderBackDto implements Serializable {

	private String createTime;

	private int status;

	private String statusString;

	private Integer id;

	private String customerName;

	private String customerMobile;

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	private String customerAddress;


	private BigDecimal totalPrice;

	private List<OrderDetailBackDto> details;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice.setScale(2);
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<OrderDetailBackDto> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailBackDto> details) {
		this.details = details;
	}
}
