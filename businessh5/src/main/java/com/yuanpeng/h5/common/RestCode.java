package com.yuanpeng.h5.common;

/**
 * Created by  on 2017/11/29.
 */
public enum RestCode {

	SUCCESS(0, "success"),
	FAIL(1,"fail"),
	NEED_LOGIN(2,"toLogin")
	;

	private int code;

	private String message;

	RestCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static String getMsg(int code) {

		for (RestCode code1 : RestCode.values()) {

			if (code == code1.code) {

				return code1.message;
			}
		}

		return "";
	}
}
