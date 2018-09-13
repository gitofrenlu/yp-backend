package com.yuanpeng.dao.utils.excel;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by  on 2017/11/21.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelResources {

	/**
	 * 属性的标题名称
	 * @return
	 */
	String title();
	/**
	 * 在excel的顺序
	 * @return
	 */
	int order() default 9999;
}
