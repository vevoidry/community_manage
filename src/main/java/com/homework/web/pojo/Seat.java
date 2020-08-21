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

//车位
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "停车位表")
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("所属停车场的id")
	Integer park_id;
	@Column
	@ApiModelProperty("编号")
	Integer code;
	@Column
	@ApiModelProperty("面积")
	Double area;
	@Column
	@ApiModelProperty("备注")
	String remark;
	@Column
	@ApiModelProperty("null是空闲，1是出售，2是出租")
	Integer type;
	@Column
	@ApiModelProperty("出租终止时间")
	Date end_date;
	@Column
	@ApiModelProperty("所属业主")
	Integer person_id;
	@Column
	@ApiModelProperty("区域")
	String region;
}
