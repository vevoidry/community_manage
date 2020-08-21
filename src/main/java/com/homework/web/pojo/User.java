package com.homework.web.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//用户
@Data // lombok，简化getter，setter，toString等基础方法
@Entity // 说明对应一个表
@Table // 说明对应一个表
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" }) // 解决一些问题
@ApiModel(description = "用户表")//swagger文档的注解
public class User {
	@Id // 说明是主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 说明主键类型是自增
	@Column
	@ApiModelProperty("主键")//swagger文档的注解
	Integer id;
	@Column
	@ApiModelProperty("用户名")
	String username;
	@Column
	@ApiModelProperty("密码")
	String password;
	@Column
	@ApiModelProperty("昵称")
	String nickname;
	@Column
	@ApiModelProperty("角色的id")
	Integer role_id;
	@Column
	@ApiModelProperty("手机")
	String phone;
	@Column
	@ApiModelProperty("邮箱")
	String email;
	@Column
	@ApiModelProperty("是否可用（是否可登录）")
	Boolean is_using;
	@Column
	@ApiModelProperty("创建时间")
	Date insert_date;
}
