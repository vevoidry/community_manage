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

//投诉建议
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "建议意见表")
public class Advice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("业主id")
	Integer person_id;
	@Column
	@ApiModelProperty("标题")
	String title;
	@Column
	@ApiModelProperty("内容")
	String content;
	@Column
	@ApiModelProperty("开始时间")
	Date start_time;
	@Column
	@ApiModelProperty("结束时间")
	Date end_time;
	@Column
	@ApiModelProperty("是否已处理")
	Boolean is_handle;
}
