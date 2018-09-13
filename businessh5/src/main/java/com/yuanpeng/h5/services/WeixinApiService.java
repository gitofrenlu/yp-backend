package com.yuanpeng.h5.services;

import com.alibaba.fastjson.JSONObject;
import com.yuanpeng.dao.mapper.SysConfigMapper;
import com.yuanpeng.dao.model.SysConfig;
import com.yuanpeng.h5.common.Contans;
import com.yuanpeng.h5.common.HttpContant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信授权 获取用户信息service
 */
@Service
@Slf4j
public class WeixinApiService {

	@Autowired
	private HttpRequestService httpRequestService;

	@Resource
	private SysConfigMapper sysConfigMapper;

	@Resource
	private HttpContant httpContant;
	/**
	 * 获取当前访客的openid
	 * @param code
	 * @return
	 */
	public String getOpenIdByCode(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+httpContant.appId+"&secret="+httpContant.appSecret+"&code="+code+"&grant_type=authorization_code";

		try {
			String jsonData = httpRequestService.get(url,httpContant.CommonConnectTimeOut, httpContant.CommonSocketTimeOut, null);
			JSONObject jsonObj = JSONObject.parseObject(jsonData);
			String openid = jsonObj.get("openid").toString();
			log.info("openId {}",openid);
			return openid;
		} catch (Exception e) {
			log.error("获取微信授权失败 {}", e);
			return null;
		}
	}

	public String getAccessToken(){

		List<String> keys = new ArrayList<>();
		keys.add(Contans.wx_access_token_expire_key);
		keys.add(Contans.wx_access_token_key);

		List<SysConfig> configs = sysConfigMapper.getAccessToken(keys);
		String exprieTime = "";
		String accessToken = "";

		for (SysConfig con:configs){
			if(con.getSysKey().equals(Contans.wx_access_token_expire_key)){
				exprieTime = con.getSysValue();
			}
			if(con.getSysKey().equals(Contans.wx_access_token_key)){
				accessToken = con.getSysValue();
			}
		}

		if(accessToken.trim().equals("") || exprieTime.trim().equals("") || (exprieTime != "" && isExpired(exprieTime))){
			//没有accesstoken 或者超时

			log.info("get new accessToken time {}",new Date().getTime());
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+httpContant.appId+"&secret="+httpContant.appSecret;
			try {
				String jsonData = httpRequestService.get(url,httpContant.CommonConnectTimeOut, httpContant.CommonSocketTimeOut, null);
				JSONObject jsonObj = JSONObject.parseObject(jsonData);
				accessToken = jsonObj.get("access_token").toString();
				long exprie = Long.parseLong(jsonObj.get("expires_in").toString()) - 200;

				exprieTime = String.valueOf(exprie * 1000 + new Date().getTime());

				sysConfigMapper.updateByKey(Contans.wx_access_token_key,accessToken);
				sysConfigMapper.updateByKey(Contans.wx_access_token_expire_key,exprieTime);

				return accessToken;
			} catch (Exception e) {
				log.error("获取accessToken失败 {}", e);
				return null;
			}

		}

		return accessToken;
	}

	boolean isExpired(String expriTime){
		Date now = new Date();

		long time = now.getTime();

		long expire = Long.parseLong(expriTime);

		if(time > expire){
			return true;
		}
		return false;

	}

}
