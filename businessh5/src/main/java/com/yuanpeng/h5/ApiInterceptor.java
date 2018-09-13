package com.yuanpeng.h5;

import com.alibaba.fastjson.JSONObject;
import com.yuanpeng.h5.common.Contans;
import com.yuanpeng.h5.common.HttpContant;
import com.yuanpeng.h5.common.RestCode;
import com.yuanpeng.h5.common.RestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class ApiInterceptor implements HandlerInterceptor {

	@Resource
	HttpContant httpContant;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

		String path = httpServletRequest.getRequestURI();


		if(path.equals("/business/redirectToPage") || path.equals("/business/redirect") || path.equals("/businessBack/login")){
			return true;
		}


		if(path.startsWith("/businessBack")){
			HttpSession session = httpServletRequest.getSession();
			Object isLogin = session.getAttribute("isLogin");

			String key = httpServletRequest.getHeader("X-key");

			if(isLogin != null && (Boolean)isLogin == true && key.equals(httpContant.key)) {
				return true;
			}else {
				PrintWriter writer = null;
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.setContentType("text/html; charset=utf-8");
				try {
					writer = httpServletResponse.getWriter();
					RestDto<T> dto = new RestDto<>(null,RestCode.NEED_LOGIN.getCode(),"请先登陆");
					writer.write(JSONObject.toJSONString(dto));
					return false;

				} catch (IOException e) {
					log.error("response error",e);
				} finally {
					if(writer != null){
						writer.flush();
					}
				}
			}
		}
		String msg = "请从公众号进入";
		if(path.equals("/error")){
			msg = "出错了！请联系管理员";
		}
		String openId = httpServletRequest.getHeader(Contans.wx_header_open_id);

		if( openId == null || openId.equals("null") || openId.trim().equals("")){
			PrintWriter writer = null;
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setContentType("text/html; charset=utf-8");
			try {
				writer = httpServletResponse.getWriter();
				writer.write(JSONObject.toJSONString(RestDto.resFail(msg)));
				return false;

			} catch (IOException e) {
				log.error("response error",e);
			} finally {
				if(writer != null){
					writer.flush();
				}
			}

		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
