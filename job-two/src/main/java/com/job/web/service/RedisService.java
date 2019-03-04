package com.job.web.service;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {
	
	/**
	 * 存入字符串到redis
	 * @param key
	 * @param value
	 */
	void setString(String key, String value);
	
	/**
	 * 存入字符串到redis
	 * @param key
	 * @param value
	 * @param time (单位为秒)
	 */
	void setString(String key, String value, long time);
	
	/**
	 * 根据key值取得字符串
	 * @param key
	 * @return
	 */
	String getString(String key);
	
	/**
	 * 模糊查询出所有的key值(无论是以pattern开头、中间还是结尾都能匹配到)
	 * @param pattern
	 * @return
	 */
	Set<Serializable> keys(String pattern);
	
	/**
	 * 根据key值删除单个缓存
	 * @param key
	 */
	void del(String key);
	
	/**
	 * 根据集合中的key值批量删除缓存
	 * @param pattern
	 */
	void delBatch(Set<Serializable> keys);
	
	/**
	 * 给key值设置失效时间
	 * @param key
	 * @param time
	 * @param unit
	 */
	void expire(String key, long time, TimeUnit unit);
	
	/**
	 * 判断key值是否过期,true未过期,false已过期
	 * 这玩意有问题不能用,点多了程序挂了
	 * @param key
	 */
	// boolean exists(String key);
	
	/**
	 * redis锁,false代表key已存在,true代表不存在
	 * 这玩意有问题不能用,点多了程序挂了
	 * @param key
	 * @param value
	 * @return
	 */
	// boolean setnx(String key, String value);
	
	/**
	 * 缓存set操作
	 * @author jzc 2018年5月24日
	 * @param key
	 * @param value
	 * @param time 0永久
	 * @throws Exception
	 */
	boolean cacheSet(String key,String value,long time) throws Exception;
	
	/**
	 * 获取缓存set数据
	 * @author jzc 2018年5月24日
	 * @param key
	 * @return
	 * @throws Exception
	 */
	Set<Serializable> getSet(String key) throws Exception;

	/**
	 * 移除set集合中的values
	 * @author jzc 2018年5月24日
	 * @param key
	 * @param values
	 * @return
	 * @throws Exception
	 */
	Long removeSet(Serializable key, Object... values) throws Exception;

}
