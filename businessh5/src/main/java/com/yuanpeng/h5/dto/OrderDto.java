package com.yuanpeng.h5.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车 订单返回实体类
 */
public class OrderDto implements Serializable {

     private String createTime;
     private String totalPrice;
     private int status;
     private List<GoodsDto> goods;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer id;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<GoodsDto> getGoods() {
		if(goods == null){
			return new ArrayList<>();
		}
		return goods;
	}

	public void setGoods(List<GoodsDto> goods) {
		this.goods = goods;
	}
}
