package com.yuanpeng.h5.dto;

import java.io.Serializable;

public class MessageModel implements Serializable {

	private String value;
	private String color;

	public MessageModel(){}

	public MessageModel(String value, String color){
		this.value = value;
		if(color != null){
			this.color = color;
		}else{
			this.color = "#173177";
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
