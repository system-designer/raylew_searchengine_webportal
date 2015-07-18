package com.searchengine.webportal.service.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;

import com.searchengine.webportal.service.RedisService;

public class RedisServiceImpl implements RedisService {

    @Autowired
	private RedisTemplate redisTemplate;

	public int add(String key, Object value, long timeout, TimeUnit timeUnit) {
		int isOk = 0;

		if (key == null){
			return isOk;
		}

		try {
			redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
			isOk = 1;
		} catch (RedisConnectionFailureException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

	public Object get(String key) {
		if (key == null)
			return null;

		Object value = null;
		try {
			value= redisTemplate.opsForValue().get(key);
		} catch (RedisConnectionFailureException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public int remove(String key) {
		int isOk = 0;

		if (key == null)
			return isOk;

		try {
			redisTemplate.delete(key);
			isOk = 1;
		} catch (RedisConnectionFailureException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

	public int multiAdd(Map<String, Object> keyValues) {
		int isOk = 0, total = 0;

		if (keyValues == null)
			return isOk;

		for(String key:keyValues.keySet()){
			try {
				redisTemplate.opsForValue().set(key, keyValues.get(key));
				total++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (total == keyValues.size()){
			isOk = 1;
			return isOk;
		} else {
			return isOk;
		}
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
