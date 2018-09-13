package com.yuanpeng.h5.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanpeng.dao.model.YpOrderDetail;
import com.yuanpeng.h5.common.Contans;
import com.yuanpeng.h5.common.OrderStatusEnum;
import com.yuanpeng.h5.common.RestDto;
import com.yuanpeng.h5.common.HttpContant;
import com.yuanpeng.h5.dto.GoodsCateDto;

import com.yuanpeng.h5.dto.OrderDto;
import com.yuanpeng.h5.dto.UserDto;
import com.yuanpeng.h5.services.GoodsService;
import com.yuanpeng.h5.services.WeixinApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * Created by  on 2018/08/07.
 *
 */
@RestController
@RequestMapping("/business")
@Slf4j
public class MainController {
	/*
	* 移动端h5页面接口
	* */
	@Resource
	private HttpContant httpContant;

	@Autowired
	private WeixinApiService weixinApiService;

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public ModelAndView weixinMenuRedirect() {
		return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+httpContant.appId+"&redirect_uri="+httpContant.rediretUri+"?response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect"));
	}

	@RequestMapping(value = "/redirectToPage",method = RequestMethod.GET)
	public ModelAndView weixinAuthRedirect(HttpServletRequest request,Model model){
		String code = request.getParameter("code");
		log.info("authin code {}", code);
		String openId = weixinApiService.getOpenIdByCode(code);

		String message = "";
		if (openId == null || openId.trim() ==""){
			openId ="";
			message =  "请从公众号进入";
		}
		String redirectUrl = httpContant.indexPage + "?redirect=true&openId="+openId+"&message="+message;

		log.info("微信授权获取成功跳转到首页 redirectUrl = {}, openId = {}",redirectUrl,openId);
		return new ModelAndView(new RedirectView(redirectUrl));

	}

	/**
	* 首页
	 * 获取 全部商品集合
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/getGoods",method = RequestMethod.GET)
	public RestDto getAllGoods(HttpServletRequest request){

		List<GoodsCateDto> goodsList = goodsService.getGoodsList();

		return RestDto.resOk(goodsList);
	}

	/**
	 * 获取 订单集合
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/getOrderList",method = RequestMethod.POST)
	public RestDto getOrderList(HttpServletRequest request,@RequestBody LinkedMap map){

		String dateTime = String.valueOf(map.get("dateTime"));

		String openid = request.getHeader(Contans.wx_header_open_id);

		if(openid == null || openid.trim().equals("")){
			return RestDto.resFail("请从公众号进入");
		}

		Integer status = (Integer) map.get("status");

		if(dateTime == "null"){
			dateTime = "";
		}
		List<OrderDto> result = goodsService.getOrderlist(status,dateTime,openid);

		return RestDto.resOk(result);
	}

	@RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
	public RestDto getUserInfo(HttpServletRequest request){

		String openId = request.getHeader(Contans.wx_header_open_id);

		if(openId == null || openId.trim().equals("")){
			log.info("get userinfo openid is null");
			return RestDto.resFail("请从公众号进入");
		}

		UserDto userDto = goodsService.getUserInfo(openId);

		if(userDto == null){
			return RestDto.resFail("未查询到会员信息，请从公众号重新进入尝试");
		}
		return  RestDto.resOk(userDto);
	}

	@RequestMapping(value = "/addCart",method = RequestMethod.POST)
	public RestDto addCart(HttpServletRequest request,@RequestBody LinkedMap map){

		String listJson = String.valueOf(map.get("list"));

		String openId =  request.getHeader(Contans.wx_header_open_id);

		JSONArray list = JSONArray.parseArray(listJson);

		List<YpOrderDetail> details = new ArrayList<>();

		for(Object obj:list){
			String objJson = obj.toString();

			JSONObject o = JSONObject.parseObject(objJson);
			YpOrderDetail detail = new YpOrderDetail();

			detail.setSize(Integer.parseInt(o.get("size").toString()));
			detail.setGoodsId(Integer.parseInt(o.get("id").toString()));

			details.add(detail);
		}
		UserDto user = goodsService.getUserInfo(openId);

		if(user == null || user.getMobile() == null){
			return RestDto.resFail("请先在会员中心补充个人信息");
		}

		goodsService.addtoCart(openId,details);

		return RestDto.resOk("下单成功");
	}

	@RequestMapping(value = "/setUserInfo",method = RequestMethod.POST)
	public RestDto setUserInfo(HttpServletRequest request,@RequestBody LinkedMap map){

		String name = String.valueOf(map.get("name"));
		String mobile = String.valueOf(map.get("mobile"));
		String address = String.valueOf(map.get("address"));
		String company = String.valueOf(map.get("company"));

		String openId =  request.getHeader(Contans.wx_header_open_id);

		goodsService.updateUserInfo(openId,name,mobile,address,company);

		return RestDto.resOk("信息修改成功!");
	}

	@RequestMapping(value = "/concelOrder",method = RequestMethod.POST)
	public RestDto concelOrder(HttpServletRequest request,@RequestBody LinkedMap map){

		String openId =  request.getHeader(Contans.wx_header_open_id);

		Integer orderId = Integer.parseInt(map.getValue(0).toString());

		goodsService.updateOrderStatus(1,OrderStatusEnum.ORDER_STATUS_CONCEL.getStatus(),orderId,null,openId);

		return  RestDto.resOk(null);
	}

}
