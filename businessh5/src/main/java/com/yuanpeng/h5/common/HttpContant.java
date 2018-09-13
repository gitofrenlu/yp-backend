package com.yuanpeng.h5.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpContant {


	@Value("${wx.app_id}")
	public  String appId;

	@Value("${wx.redirect.uri}")
	public  String indexPage;

	@Value("${wx.bakend.redirect.uri}")
	public String rediretUri;

	@Value("${wx.secret}")
	public  String appSecret;

	@Value("${api.loan.connectTimeout}")
	public  int CommonConnectTimeOut;//http 连接超时时间

	@Value("${api.loan.socketTimeout}")
	public  int CommonSocketTimeOut;//http 请求超时时间

	@Value("${good.img.upload.location}")
	public String uploadLocation;//图片上传位置

	@Value("${good.img.upload.base.location}")
	public String uploadBaseLocation;

	@Value("${wx_template_customer}")
	public String wxTemplateCustomer;
	@Value("${wx_template_boos}")
	public String wxTemplateBoos;
	@Value("${wx_admin_openId}")
	public String wxAdminOpenId;

	//登陆账号密码
	@Value("${account}")
	public String account;
	@Value("${password}")
	public String password;
	@Value("${key}")
	public String key;
}
