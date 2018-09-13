package com.yuanpeng.h5;

import com.yuanpeng.h5.common.HttpContant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * Created by  on 2018/1/17.
 */
@EnableWebMvc
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	@Resource
	HttpContant contant;

	@Bean
	ApiInterceptor apiInterceptor() {
		return new ApiInterceptor();
	}


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT")
				.maxAge(3600);
	}


	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String path = contant.uploadBaseLocation + contant.uploadLocation;
		registry.addResourceHandler("/static/**").addResourceLocations("file:" + path,"classpath:/public/");

		//registry.addResourceHandler("/static/img/**").addResourceLocations("file:/upload/yp/goods/");

	}

}
