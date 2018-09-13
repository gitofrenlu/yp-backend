package com.yuanpeng.dao.model;

import java.io.Serializable;

public class AccessTokenModel implements Serializable {

	private String accessToken;
	private String AccessTokenExpire;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenExpire() {
		return AccessTokenExpire;
	}

	public void setAccessTokenExpire(String accessTokenExpire) {
		AccessTokenExpire = accessTokenExpire;
	}
}
