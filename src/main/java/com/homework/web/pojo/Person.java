package com.homework.web.pojo;

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

//业主
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "业主表")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("姓名")
	String name;
	@Column
	@ApiModelProperty("性别")
	String gender;
	@Column
	@ApiModelProperty("年龄")
	Integer age;
	@Column
	@ApiModelProperty("身份证号码")
	String id_card_number;
	@Column
	@ApiModelProperty("手机号")
	String phone;
	@Column
	@ApiModelProperty("备注")
	String remark;
	@Column
	@ApiModelProperty("头像")
	String image;
	@Column
	@ApiModelProperty("角色的id")
	Integer role_id;
}
