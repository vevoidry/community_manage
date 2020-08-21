package com.homework.web;

import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//静态资源映射
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	public String staticString = "file:" + "src/main/resources/static";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/image/**")
				.addResourceLocations(staticString + File.separator + "image" + File.separator);
		registry.addResourceHandler("/css/**")
				.addResourceLocations(staticString + File.separator + "css" + File.separator);
		registry.addResourceHandler("/js/**")
				.addResourceLocations(staticString + File.separator + "js" + File.separator);
	}

}
