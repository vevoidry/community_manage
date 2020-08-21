package com.homework.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//shiro的配置类
@Configuration
public class ShiroConfig {

	// 加密/解密器
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列两次，相当于 md5(md5(""));
		return hashedCredentialsMatcher;
	}

	// UserRealm
	@Bean
	UserShiroRealm userShiroRealm() {
		UserShiroRealm userShiroRealm = new UserShiroRealm();
		userShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());// 设置加密解密器
		return userShiroRealm;
	}

	// PersonRealm
	@Bean
	PersonShiroRealm personShiroRealm() {
		PersonShiroRealm personShiroRealm = new PersonShiroRealm();
		personShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());// 设置加密解密器
		return personShiroRealm;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	DefaultSecurityManager securityManager() {
		// 多个realm
		ArrayList<Realm> reamlList = new ArrayList<Realm>();
		reamlList.add(userShiroRealm());
		reamlList.add(personShiroRealm());
		// 多模块认证器
		ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
		modularRealmAuthenticator.setRealms(reamlList);
		// 安全管理器
		DefaultSecurityManager manager = new DefaultWebSecurityManager();
		manager.setAuthenticator(modularRealmAuthenticator);
		return manager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	ShiroFilterFactoryBean shiroFilterFactoryBean() {
		Map<String, String> filterMap = new HashMap<String, String>();
		// 不需要认证
		filterMap.put("/image/**", "anon");// 静态资源
		filterMap.put("/layui/**", "anon");// 静态资源
		filterMap.put("/login", "anon");// 登录页面
		filterMap.put("/doLogin", "anon");// 进行登录操作
		// 需要认证
//		filterMap.put("/**", "authc");
		filterMap.put("/**", "anon");
		// Shiro的过滤器
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager());// 配置安全管理器
		bean.setFilterChainDefinitionMap(filterMap);// 添加过滤器链
		bean.setLoginUrl("/login"); // 认证页面
		bean.setUnauthorizedUrl("/403");// 无权限跳转页面
		return bean;
	}

	// 开启shiro aop注解支持.
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

	// shiro注解模式下，登录失败或者是没有权限都是抛出异常，并且默认的没有对异常做处理，配置一个异常处理
//	@Bean(name = "simpleMappingExceptionResolver")
//	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
//		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//		Properties mappings = new Properties();
//		mappings.setProperty("DatabaseException", "databaseError");// 数据库异常处理
//		mappings.setProperty("UnauthorizedException", "/403");
//		r.setExceptionMappings(mappings); // None by default
//		r.setDefaultErrorView("error"); // No default
//		r.setExceptionAttribute("exception"); // Default is "exception"
//		return r;
//	}

}
