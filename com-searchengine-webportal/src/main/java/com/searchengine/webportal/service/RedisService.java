package com.searchengine.webportal.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright （c）2014 iyunxiao
 * @author 刘静
 * @version 1.0 beta
 * @since 2014-07-30 
 *
 *Redis操作接口
 * 			   Redis中的每一条记录格式： key：	类型:String	值：SubDomainUrl  
 * 									 value：	类型:String 	值：(Json)
 * 	
 */
public interface RedisService {
	/**
	 * 添加一条记录在Redis数据库中
	 * @param key the String 一条记录的key
	 * 		  value the String 一条记录的value
	 * @return <code>1</code> 执行成功
	 *		   <code>0</code> 执行失败
	 */
	int add(String key, Object value, long timeout, TimeUnit timeUnit);
	
	/**
	 * 从Redis数据库中获取一条记录为key的value值
	 * @param key the <code>String</code> 要获取记录的key值
	 * @return <code>String</code> 执行成功
	 *		   <code>null</code> 执行失败
	 */
	Object get(String key);
	
	/**
	 * 删除一条记录在Redis数据库中
	 * @param key the String 要删除记录的key
	 * @return <code>1</code> 执行成功
	 *		   <code>0</code> 执行失败
	 */
	int remove(String key);
	
	/**
	 * 批量添加记录到Redis数据库中
	 * @param keyValues 要添加的数据集合
	 * @return <code>1</code> 执行成功
	 *		   <code>0</code> 执行失败
	 */
	int multiAdd(Map<String, Object> keyValues);

}
