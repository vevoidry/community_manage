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

//菜单
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "菜单表")
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("若是父菜单则该值是0，若是子菜单则该值为其父菜单的id")
	Integer parent_id;
	@Column
	@ApiModelProperty("页面名称")
	String page_name;
	@Column
	@ApiModelProperty("名称")
	String name;
	@Column
	@ApiModelProperty("图标")
	String icon;
	@Column
	@ApiModelProperty("排序值")
	Integer rank;
	@Column
	@ApiModelProperty("是否可用")
	Boolean is_using;
}
