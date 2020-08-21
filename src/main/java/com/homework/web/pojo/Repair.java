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

//报修
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "维修记录表")
public class Repair {
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
	@ApiModelProperty("创建时间")
	Date start_time;
	@Column
	@ApiModelProperty("处理时间")
	Date end_time;
	@Column
	@ApiModelProperty("是否已处理")
	Boolean is_handle;
	@Column
	@ApiModelProperty("管理员指定的用户（维修工）的id")
	Integer user_id;
	@Column
	@ApiModelProperty("报修收费（维修工设置价格）")
	Double price;
	@Column
	@ApiModelProperty("维修的过程")
	String process;
	@Column
	@ApiModelProperty("维修的结果")
	String result;
}
