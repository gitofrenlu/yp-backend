package com.yuanpeng.h5.dto;

import java.io.Serializable;
import java.util.List;

public class GoodsCateDto implements Serializable {

	private String label;

	private List<GoodsDto> goods;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<GoodsDto> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsDto> goods) {
		this.goods = goods;
	}
}
