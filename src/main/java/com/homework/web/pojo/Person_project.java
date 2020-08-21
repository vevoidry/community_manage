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

//业主-收费项目关联表
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "缴费记录表")
public class Person_project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("业主id")
	Integer person_id;
	@Column
	@ApiModelProperty("项目id")
	Integer project_id;
	@Column
	@ApiModelProperty("发起收费的时间")
	Date create_time;
	@Column
	@ApiModelProperty("是否已收费")
	Boolean is_handle;
	@Column
	@ApiModelProperty("收费方式，微信/支付宝")
	String method;
	@Column
	@ApiModelProperty("收费费用")
	Double money;
	@Column
	@ApiModelProperty("缴费时间")
	Date end_time;
	@Column
	@ApiModelProperty("收费类型，true为根据房屋收费，false为根据车位收费，null为即不是房屋也不是业主")
	Boolean type;
	@Column
	@ApiModelProperty("收费类型对应的信息，如房屋的id或车位的id")
	Integer type_id;
}
