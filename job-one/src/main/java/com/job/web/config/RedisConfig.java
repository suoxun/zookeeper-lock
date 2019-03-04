package com.job.web.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	
	/**
	 * redis连接池的属性
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.redis.jedis.pool")
	public JedisPoolConfig getRedisConfig() {
		return new JedisPoolConfig();
	}
	
	/**
	 * redis基本连接属性
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	public RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
		return new RedisStandaloneConfiguration();
	}
	
	/**
	 * RedisConnectionFactory配置
	 * @return
	 */
	@Bean
	public RedisConnectionFactory getConnectionFactory() {
		JedisClientConfiguration.JedisClientConfigurationBuilder jed = JedisClientConfiguration.builder();
		jed.usePooling().poolConfig(getRedisConfig());
		return new JedisConnectionFactory(getRedisStandaloneConfiguration(),jed.build());
	}

	/**
	 * RedisTemplate
	 * @return
	 */
	@Bean
	public RedisTemplate<?, ?> getRedisTemplate() {
		RedisTemplate<?, ?> template = new StringRedisTemplate(getConnectionFactory());
		return template;
	}

}
