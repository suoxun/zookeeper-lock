package com.job.web.serviceImpl;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.job.web.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	@Autowired(required = false)
	public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
	    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
	    redisTemplate.setKeySerializer(stringSerializer);
	    redisTemplate.setValueSerializer(stringSerializer);
	    redisTemplate.setHashKeySerializer(stringSerializer);
	    redisTemplate.setHashValueSerializer(stringSerializer);
	    this.redisTemplate = redisTemplate;
	}
	
	public void setString(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void setString(String key, String value, long time) {
		redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
	}

	public String getString(String key) {
		Object value = redisTemplate.opsForValue().get(key);
		if(value == null){
			return null;
		} else {
			return redisTemplate.opsForValue().get(key).toString();
		}
	}
	
	public Set<Serializable> keys(String pattern) {
		return redisTemplate.keys("*"+pattern+"*");
	}

	public void del(String key) {
		redisTemplate.delete(key);
	}

	public void delBatch(Set<Serializable> keys) {
		redisTemplate.delete(keys);
	}

	public void expire(String key, long time, TimeUnit unit) {
		redisTemplate.expire(key, time, unit);
	}
	
	public boolean cacheSet(String key,String value,long time) throws Exception {
        try {
        	SetOperations<Serializable, Serializable> opsForSet = redisTemplate.opsForSet();
        	opsForSet.add(key, value);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception t) {
        	throw new RuntimeException("set缓存[" + key + "]失败, value[" + value+ "]");
        }
	}

	@Override
	public Set<Serializable> getSet(String key) throws Exception {
        try {
            SetOperations<Serializable, Serializable> setOps = redisTemplate.opsForSet();
            return setOps.members(key);
        } catch (Exception t) {
            throw new RuntimeException("获取set缓存失败key[" + key + "]");
        }
	}
	
	@Override
	public Long removeSet(Serializable key, Object... values) throws Exception {
        try {
            SetOperations<Serializable, Serializable> setOps = redisTemplate.opsForSet();
            return setOps.remove(key, values);
        } catch (Exception t) {
            throw new RuntimeException("set移除[" + key + "]失败");
        }
	}
	
	// 怀疑是getConnectionFactory().getConnection()这个玩意不行

	/*public boolean exists(String key) {
		return redisTemplate.getConnectionFactory().getConnection().exists(key.getBytes());
	}*/

	/*public boolean setnx(String key, String value) {
		return redisTemplate.getConnectionFactory().getConnection().setNX(key.getBytes(), value.getBytes());
	}*/

}
