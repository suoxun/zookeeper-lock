package com.job.web;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;


@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = { "com.job" })
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    return new MethodValidationPostProcessor();
	}
	  
	@Bean(name = "encryptorBean")
	public StringEncryptor stringEncryptor() {
	    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("bAxfuJBOB^Yni68B");
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
	    config.setProviderName("SunJCE");
	    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
	    config.setStringOutputType("base64");
	    encryptor.setConfig(config);
	    return encryptor;
	}
	
	public static void main(String[] args) {
		logger.info("\r\n=====================start=====================");
		SpringApplication.run(Application.class, args);
		logger.info("\r\n=====================started=====================");
	}
}