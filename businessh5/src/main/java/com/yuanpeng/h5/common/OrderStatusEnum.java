package com.yuanpeng.h5.common;

public enum OrderStatusEnum {
	ORDER_STATUS_WAITING(0,"待处理"),
	ORDER_STATUS_REPAREING(1,"处理中"),
	ORDER_STATUS_COMPLETED(2,"已完成"),
	ORDER_STATUS_CONCEL(3,"已取消")
	;

	OrderStatusEnum(Integer status,String statusString){
		this.status = status;
		this.statusString = statusString;
	}

	private Integer status;

	private String statusString;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public static String getStringByStatus(Integer status){

		for(OrderStatusEnum statusEnum:OrderStatusEnum.values()){
			if(statusEnum.getStatus().equals(status)){
				return statusEnum.getStatusString();
			}
		}
		return "---";
	}
}
