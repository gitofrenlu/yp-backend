package com.yuanpeng.h5.services;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yuanpeng.dao.mapper.*;
import com.yuanpeng.dao.model.*;
import com.yuanpeng.h5.common.HttpContant;
import com.yuanpeng.h5.common.OrderStatusEnum;
import com.yuanpeng.h5.dto.*;
import com.yuanpeng.h5.utils.CovertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * Created by  on 2018/08/07.
 */
@Service
@Slf4j
public class GoodsService {

	@Resource
	private YpGoodsMapper ypGoodsMapper;

	@Resource
	private YpOrderMapper ypOrderMapper;

	@Resource
	private YpOrderDetailMapper ypOrderDetailMapper;

	@Resource
	private YpCategoryMapper ypCategoryMapper;

	@Resource
	private HttpContant httpContant;

	@Autowired
	private WeixinApiService weixinApiService;

	@Autowired
	HttpRequestService httpRequestService;

	@Resource
	YpCustomerMapper ypCustomerMapper;

	public List<GoodsCateDto> getGoodsList() {

		List<YpGoods> goods = ypGoodsMapper.getAllGoodsByStatus(0);

		List<YpCategory> cates = ypCategoryMapper.getAllCate();

		List<GoodsCateDto> result = new ArrayList<>();

		HashMap<Integer,List<GoodsDto>> map = new HashMap<>();

		goods.stream().forEach(g -> {
			if(map.containsKey(g.getCateId())){
				List<GoodsDto> odlList = map.get(g.getCateId());
				odlList.add(transGoods(g));
				map.replace(g.getCateId(),odlList);
			}else {
				List<GoodsDto> newList = new ArrayList<>();
				newList.add(transGoods(g));
				map.put(g.getCateId(),newList);
			}
		});

		for(Map.Entry<Integer,List<GoodsDto>> entry:map.entrySet()){
			for (YpCategory cate:cates){
				if(cate.getId().equals(entry.getKey())){
					GoodsCateDto dto = new GoodsCateDto();
					dto.setLabel(cate.getName());
					dto.setGoods(entry.getValue());
					result.add(dto);
				}
			}
		}


		return result;
	}

	public GoodsDto transGoods(YpGoods goods){

		GoodsDto dto = new GoodsDto();
		dto.setId(goods.getId());
		dto.setImgUrl(goods.getImgUrl());
		dto.setName(goods.getName());
		dto.setStandard(goods.getSize()+goods.getUnit());
		return dto;
	}

	/**
	 *  获取订单列表
	 */

	public List<OrderDto> getOrderlist(Integer status, String date, String openid){

		List<OrderGoods> orderGoods = ypOrderMapper.getAllOrderGoods(date,status,openid);

		List<OrderDto> result = new ArrayList<>();

		for(OrderGoods goods:orderGoods){
			boolean flag = false;

			for(OrderDto dto:result){
				if(dto.getId().equals(goods.getId())){
					List<GoodsDto> goodsList = dto.getGoods();
					goodsList.add(transToOrderGood(goods));
					dto.setGoods(goodsList);

					flag =true;
					break;
				}
			}
			if(!flag){
				OrderDto newOrderDto = new OrderDto();
				List<GoodsDto> newGoods = new ArrayList<>();
				newGoods.add(transToOrderGood(goods));
				newOrderDto.setGoods(newGoods);
				newOrderDto.setCreateTime(DateFormat.getDateInstance(DateFormat.DEFAULT).format(goods.getCreateTime()));
				newOrderDto.setId(goods.getId());
				newOrderDto.setStatus(goods.getStatus());
				newOrderDto.setTotalPrice(goods.getTotalPrice().setScale(2).toString());

				result.add(newOrderDto);

			}
		}
		return result;
	}

	public GoodsDto transToOrderGood(OrderGoods orderGoods){

		GoodsDto dto = new GoodsDto();
		dto.setStandard(orderGoods.getSize().toString()+orderGoods.getUnit());
		dto.setName(orderGoods.getName());
		dto.setPrice(orderGoods.getPrice().setScale(2).toString());
		return dto;
	}

	public UserDto getUserInfo(String openId){

		UserDto result = getUserFromData(openId);

		if(result != null){
			return result;
		}

		String accessToken = weixinApiService.getAccessToken();

		if(accessToken == null){
			return null;
		}

		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";

		try {
			String jsonData = httpRequestService.get(url,httpContant.CommonConnectTimeOut, httpContant.CommonSocketTimeOut, null);
			JSONObject jsonobj = JSONObject.parseObject(jsonData);
			String newOpenId = jsonobj.getString("openid");
			String nickName = jsonobj.getString("nickname");
			String headimgurl = jsonobj.getString("headimgurl");

			int id = addCustomer(newOpenId,nickName,headimgurl);

			result = new UserDto();
			result.setImgUrl(headimgurl);
			result.setNickName(nickName);
			result.setId(id);

			return result;
		} catch (Exception e) {
			log.error("get userinfo error  {}",e.getMessage());
		}

		return null;
	}

	public UserDto getUserFromData(String openId){
		//从数据库获取用户信息

		YpCustomer customer = ypCustomerMapper.selectByOpenid(openId);

		if(customer != null){
			return CovertUtils.covert(customer,UserDto.class);
		}
		return null;
	}

	public int addCustomer(String openId,String name,String headUrl){


		YpCustomer customer = new YpCustomer();

		customer.setCreateTime(new Date());
		customer.setImgUrl(headUrl);
		customer.setNickName(name);
		customer.setOpenId(openId);

		log.info("add customer openid:{} name:{} img:{}",openId,name,headUrl);

		ypCustomerMapper.insertSelective(customer);

		return customer.getId();
	}

	public void addtoCart(String openId,List<YpOrderDetail> details){

		UserDto user = getUserInfo(openId);

		Date now = new Date();
		YpOrder order = new YpOrder();
		order.setCreateTime(now);
		order.setUpdateTime(now);
		order.setCustomerId(user.getId());

		ypOrderMapper.insertSelective(order);

		for(YpOrderDetail de:details){
			YpOrderDetail detail = new YpOrderDetail();
			detail.setCreateTime(now);
			detail.setOrderId(order.getId());
			detail.setGoodsId(de.getGoodsId());
			detail.setUpdateTime(now);
			detail.setSize(de.getSize());

			ypOrderDetailMapper.insertSelective(detail);
		}


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = sdf.format(now);

		Map<String,Object> map = new HashMap<>();
		Map<String,Object> subMap = new HashMap<>();

		subMap.put("first",new MessageModel("您收到了最新的订单任务，请查收",null));
		subMap.put("keyword1",new MessageModel(user.getNickName(),null));
		subMap.put("keyword2",new MessageModel("0.00元",null));
		subMap.put("keyword3",new MessageModel(order.getId().toString(),null));
		subMap.put("keyword4",new MessageModel(time,null));
		subMap.put("remark",new MessageModel("请及时联系客户确认订单",null));

		map.put("data",subMap);
		map.put("touser",httpContant.wxAdminOpenId);
		map.put("template_id",httpContant.wxTemplateBoos);

		String json = JSONObject.toJSONString(map);

		SendMessage(json);
	}

	public void updateUserInfo(String openId,String name,String mobile,String address,String company){

		Date now = new Date();

		YpCustomer customer = new YpCustomer();
		customer.setCreateTime(now);
		customer.setUpdateTime(now);
		customer.setOpenId(openId);
		customer.setNickName(name);
		customer.setAddress(address);
		customer.setMobile(mobile);
		customer.setCompany(company);

		ypCustomerMapper.updateByOpenId(customer);

	}

	/**
	 * 修改订单状态
	 * @param orderId
	 * @param status
	 */
	public void updateOrder(Integer orderId,Integer status){

		ypOrderMapper.updateOrderStatus(orderId,status,null);

	}

	/**
	 * 获取后台的order操作列表
	 * @param status
	 * @param mobile
	 * @param name
	 * @param current
	 * @return
	 */
	public PageInfo<OrderBackDto> getOrdersForback(Integer status,String mobile,String name,Integer current){

		Integer c = 2 * current - 2;

		List<OrderbackModel> orders = ypOrderMapper.getAllOrders(mobile,status,name,c);
		long total = ypOrderMapper.getTotal(mobile,status,name);


		List<OrderBackDto> backDtos = CovertUtils.covertList(orders, OrderBackDto.class, new Function<OrderbackModel, OrderBackDto>() {
			@Override
			public OrderBackDto apply(OrderbackModel model) {
				OrderBackDto dto = new OrderBackDto();
				dto.setCreateTime(model.getCreateTime());
				dto.setId(model.getId());
				dto.setCustomerMobile(model.getCustomerMobile());
				dto.setCustomerName(model.getCustomerName());
				dto.setStatus(model.getStatus());
				dto.setStatusString(OrderStatusEnum.getStringByStatus(model.getStatus()));
				dto.setTotalPrice(model.getTotalPrice());
				dto.setCustomerAddress(model.getCustomerAddress());
				return dto;
			}
		});
		PageInfo<OrderBackDto> result = new PageInfo<>(backDtos);

		result.setTotal(total);
		for (OrderBackDto mo:result.getList()){
			List<OrderBackDetailModel> details = ypOrderDetailMapper.getBackDetails(mo.getId());

			List<OrderDetailBackDto> detaillDto = CovertUtils.covertList(details, OrderDetailBackDto.class, new Function<OrderBackDetailModel, OrderDetailBackDto>() {
				@Override
				public OrderDetailBackDto apply(OrderBackDetailModel model) {
					OrderDetailBackDto dto = CovertUtils.covert(model,OrderDetailBackDto.class);
					return dto;
				}
			});

			mo.setDetails(detaillDto);
		}

		return result;
	}

	public void updateOrderStatus(Integer type, Integer status, Integer id, BigDecimal price,String openId){
		// 1 修改订单状态  2 修改订单详情状态

		if(type == 1){
			ypOrderMapper.updateOrderStatus(id,status,price);

			if(status == OrderStatusEnum.ORDER_STATUS_CONCEL.getStatus()){
				//取消订单发送消息

				OrderbackModel od = ypOrderMapper.getOrderMessage(id);

				Map<String,Object> map = new HashMap<>();
				Map<String,Object> subMap = new HashMap<>();

				subMap.put("keyword1",new MessageModel(id.toString()+"-"+od.getCreateTime(),null));
				subMap.put("keyword2",new MessageModel("总价:"+od.getTotalPrice(),null));
				subMap.put("keyword3",new MessageModel(OrderStatusEnum.getStringByStatus(status),null));
				subMap.put("remark",new MessageModel("请及时关注订单状态",null));

				if(openId != null){
					//发送给 主人的消息
					subMap.put("first",new MessageModel("客户 "+od.getCustomerName()+" 取消了"+od.getCreateTime()+" 的订单，订单id："+od.getId(),null));
					map.put("touser",httpContant.wxAdminOpenId);
					map.put("template_id",httpContant.wxTemplateCustomer);
					map.put("data",subMap);
				}else {
					//发送给客户的消息
					subMap.put("first",new MessageModel("您在"+od.getCreateTime()+" 的订单已取消",null));
					map.put("touser",od.getOpenId());
					map.put("template_id",httpContant.wxTemplateCustomer);
					map.put("data",subMap);
				}
				String json = JSONObject.toJSONString(map);

				SendMessage(json);
			}
		}

		if(type == 2){
			ypOrderDetailMapper.updateDetailStatus(id,status,price);
		}
	}

	public List<YpCategory> getCatelist(){

		List<YpCategory> cates = ypCategoryMapper.getAllCate();

		return cates;
	}

	public void updateCategory(Integer id,String name,String code,Integer status){

		YpCategory category = new YpCategory();
		Date now = new Date();
		category.setId(id);
		category.setUpdateTime(now);
		category.setCode(code);
		category.setName(name);
		category.setStatus(status);

		if(id != null){
			ypCategoryMapper.updateByPrimaryKeySelective(category);
		}else {
			category.setCreateTime(now);
			ypCategoryMapper.insertSelective(category);
		}
	}

	public void removeCate(Integer id){

		ypCategoryMapper.deleteByPrimaryKey(id);
	}

	public List<YpGoodsForBack> getAllGoodsList(Integer status,String name){

		List<YpGoods> goods =  ypGoodsMapper.getAllGoods(status,name);

		List<YpCategory> cates = ypCategoryMapper.getAllCate();

		List<YpGoodsForBack> dtos = CovertUtils.covertList(goods, YpGoodsForBack.class, new Function<YpGoods, YpGoodsForBack>() {
			@Override
			public YpGoodsForBack apply(YpGoods good) {
				YpGoodsForBack back = new YpGoodsForBack();
				back = CovertUtils.covert(good,YpGoodsForBack.class);

				for(YpCategory category:cates){
					if(category.getId() == good.getCateId()){
						back.setCategory(category.getName());
					}
				}
				return back;
			}
		});
		return dtos;
	}

	public void removeGood(Integer id){

		ypGoodsMapper.deleteByPrimaryKey(id);
	}

	public String uploadGoods(YpGoods good){

		if(good.getId() == null || good.getId() == 0){
			Date now = new Date();
			good.setCreateTime(now);
			good.setUpdateTime(now);
			ypGoodsMapper.insertSelective(good);
			return null;
		}else {
			String url = ypGoodsMapper.getOldImg(good.getId());
			ypGoodsMapper.updateByPrimaryKeySelective(good);

			return httpContant.uploadBaseLocation+httpContant.uploadLocation+url;
		}
	}

	public void SendMessage(String json) {

		String accessToken = weixinApiService.getAccessToken();

		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;

		try {
			httpRequestService.postWithJson(url,json,httpContant.CommonConnectTimeOut, httpContant.CommonSocketTimeOut, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeOrder(Integer id){

		if(id == null){
			return;
		}

		ypOrderMapper.deleteByPrimaryKey(id);
	}

	public void deleteAll(){

		ypCategoryMapper.deleteAll();
		ypCustomerMapper.deleteAll();
		ypGoodsMapper.deleteAll();
		ypOrderDetailMapper.deleteAll();
		ypOrderMapper.deleteAll();
	}
}
