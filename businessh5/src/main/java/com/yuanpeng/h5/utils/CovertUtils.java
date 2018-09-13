package com.yuanpeng.h5.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

/**
 * Created by junqing.li on 16/3/23.
 *
 * 可以参照 spring的covert 做一套
 */
public class CovertUtils {

	/**
	 * bean 对象转换
	 *
	 * domain -> dto
	 *
	 * domain中保持比dto中少
	 * 
	 * @param s
	 * @param t
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> T covert(S s, Class<T> t) {

		if (Objects.isNull(s)) {

			return null;
		}

		T result = (T) ReflectUtils.newInstance(t);

		BeanUtils.copyProperties(s, result);

		return result;
	}

	/**
	 * list 转换
	 * 
	 * @param list
	 * @param t
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> covertList(List<S> list, Class<T> t, Function<S, T> function) {

		List<T> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(list)) {

			return result;
		}

		return list.stream().map(s -> function.apply(s)).collect(Collectors.toList());

	}

	/**
	 * 并发转换list
	 *
	 * @param list
	 * @param t
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> covertListByParallel(List<S> list, Class<T> t, Function<S, T> function) {

		List<T> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(list)) {

			return result;
		}

		return list.parallelStream().map(s -> function.apply(s)).collect(Collectors.toList());

	}

	/**
	 * 默认转换的list
	 * 
	 * @param list
	 * @param t
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> covertDefaultList(List<S> list, Class<T> t) {

		List<T> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(list)) {

			return result;
		}

		return list.stream().map(s -> covert(s, t)).collect(Collectors.toList());

	}





	/**
	 * 集合转换
	 * 
	 * @param s
	 * @param t
	 * @param function
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> T covert(S s, Class<T> t, Function<T, T> function) {

		T result = (T) ReflectUtils.newInstance(t);

		BeanUtils.copyProperties(s, result);

		function.apply(result);

		return result;
	}


}
