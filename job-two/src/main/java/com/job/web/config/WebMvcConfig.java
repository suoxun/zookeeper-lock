package com.job.web.config;

import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;  
  
@Configuration  
public class WebMvcConfig implements WebMvcConfigurer {
	
	/**
	 * 解决跨域问题
	 */
	public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE",
	            "OPTIONS", "TRACE");
	}
	
	/**
	 * 指定静态资源路径
	 */
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	  
	/**
	 * 解决直接返回字符串到浏览器乱码问题
	 * @return
	 */
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
	    StringHttpMessageConverter converter = new StringHttpMessageConverter(
	            Charset.forName("UTF-8"));
	    return converter;
	}
  
}  