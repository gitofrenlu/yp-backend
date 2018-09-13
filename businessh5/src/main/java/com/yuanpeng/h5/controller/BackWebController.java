package com.yuanpeng.h5.controller;

import com.github.pagehelper.PageInfo;
import com.yuanpeng.dao.model.YpGoods;
import com.yuanpeng.h5.common.HttpContant;
import com.yuanpeng.h5.common.RestDto;
import com.yuanpeng.h5.dto.OrderBackDto;
import com.yuanpeng.h5.services.GoodsService;
import com.yuanpeng.h5.services.UploadService;
import com.yuanpeng.h5.utils.CommonUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by  on 2018/08/07.
 * 后台管理类
 */
@RestController
@RequestMapping("/businessBack")
@Slf4j
public class BackWebController {

	@Autowired
	GoodsService goodsService;

	@Autowired
	UploadService uploadService;

	@Autowired
	HttpContant httpContant;

	@RequestMapping(value = "/getOrders",method = RequestMethod.POST)
	public RestDto getAllOrders(HttpServletRequest request, @RequestBody LinkedMap map){

		Integer currentPage = Integer.parseInt(map.get("current").toString());
		String customerName = String.valueOf(map.get("customerName"));
		String mobile = String.valueOf(map.get("mobile"));
		Integer status = null;
		if(map.get("status") != null) {
			status = Integer.parseInt(map.get("status").toString());
			if(status == 4){
				status = null;
			}
		}

		if(currentPage == null || currentPage <= 0){
			currentPage = 1;
		}

		PageInfo<OrderBackDto> result = goodsService.getOrdersForback(status,mobile,customerName,currentPage);

		return RestDto.resOk(result);
	}

	@RequestMapping(value = "/updateOrderStatus",method = RequestMethod.POST)
	public RestDto updateOrderStatus(HttpServletRequest request,@RequestBody LinkedMap map){
		// 1 修改订单状态  2 修改订单详情状态
		Integer type = Integer.parseInt(String.valueOf(map.get("type")));

		Integer status = Integer.parseInt(String.valueOf(map.get("status")));

		BigDecimal price = BigDecimal.valueOf(Double.parseDouble(String.valueOf(map.get("price"))));

		Integer id = Integer.parseInt(String.valueOf(map.get("id")));

		if(type == null || status == null || id == null){
			log.info("updateOrderStatus param error type{} status{} id{}",String.valueOf(type),String.valueOf(status),String.valueOf(id));
			return RestDto.resFail("参数错误请检查");
		}
		goodsService.updateOrderStatus(type,status,id,price,null);
		return RestDto.resOk(null);
	}

	@RequestMapping(value = "/getCateList",method = RequestMethod.GET)
	public RestDto getCateList(HttpServletRequest request){

		return RestDto.resOk(goodsService.getCatelist());
	}

	@RequestMapping(value = "/updateCategory",method = RequestMethod.POST)
	public RestDto updateCategory(HttpServletRequest request,@RequestBody LinkedMap map){

		Integer id = null;
		if(String.valueOf(map.get("cateId")) != "null"){
			id = Integer.parseInt(String.valueOf(map.get("cateId")));
		}

		String name = String.valueOf(map.get("name"));
		String code = String.valueOf(map.get("code"));

		Integer status = null;
		if(String.valueOf(map.get("status")) != "null"){
			status = Integer.parseInt(String.valueOf(map.get("status")));
		}

		goodsService.updateCategory(id,name,code,status);

		return RestDto.resOk(null);
	}

	@RequestMapping(value = "/removeCate/{id}",method = RequestMethod.GET)
	public RestDto removeCate(HttpServletRequest request, @PathVariable("id") Integer id){

		goodsService.removeCate(id);
		return RestDto.resOk(null);
	}

	@RequestMapping(value = "/getGoodList",method = RequestMethod.POST)
	public RestDto getGoodList(HttpServletRequest request,@RequestBody LinkedMap map){

		Integer status = null;
		if(String.valueOf(map.get("status")) != "null"){
			status = Integer.parseInt(String.valueOf(map.get("status")));
			if(status == 2){
				status = null;
			}
		}

		String name = String.valueOf(map.get("name"));

		return RestDto.resOk(goodsService.getAllGoodsList(status,name));
	}


	@RequestMapping(value = "/updateGoods",method = RequestMethod.POST)
	public RestDto updateGoods(YpGoods good, MultipartFile file, HttpServletResponse response, HttpServletRequest request){

		String filename = UUID.randomUUID().toString() + "."
				+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

		String url = uploadService.uploadPic(file, filename, httpContant.uploadBaseLocation+httpContant.uploadLocation, request, response);

		good.setImgUrl(url);

		String odlImg = goodsService.uploadGoods(good);

		if(odlImg != null){
			uploadService.removeFile(odlImg);
		}

		return RestDto.resOk(null);
	}

	@RequestMapping(value = "/removeGood/{id}",method = RequestMethod.GET)
	public RestDto removeGood(HttpServletRequest request, @PathVariable("id") Integer id){

		goodsService.removeGood(id);
		return RestDto.resOk(null);
	}

	@RequestMapping(value = "/clear/{key}",method = RequestMethod.GET)
	public RestDto deleteAll(HttpServletRequest request,@PathVariable("key") String key){

		if(key.equals("wxbef75e11a55113c4")) {
			goodsService.deleteAll();
			return RestDto.resOk(null);

		}

		return RestDto.resFail("error");
	}
	@RequestMapping(value = "/removeOrder/{id}",method = RequestMethod.GET)
	public RestDto removeOrder(HttpServletRequest request,@PathVariable("id") Integer id){

		goodsService.removeOrder(id);
		return RestDto.resOk(null);
	}

	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public RestDto login(HttpServletRequest request,@RequestBody LinkedMap map){

		String account = String.valueOf(map.get("account"));
		String password = String.valueOf(map.get("password"));

		String passwordMD5 =CommonUtils.getMD5(password);

		if(passwordMD5.equals(httpContant.password) && account.equals(httpContant.account)){
			HttpSession session = request.getSession();
			session.setAttribute("isLogin",true);

			return RestDto.resOk(httpContant.key);

		}else {
			return RestDto.resFail("账号或密码错误");
		}
	}
}
