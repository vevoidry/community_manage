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

//房屋
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "房屋表")
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("单元的id")
	Integer unit_id;
	@Column
	@ApiModelProperty("房屋编号")
	Integer code;
	@Column
	@ApiModelProperty("房屋楼层")
	Integer floor;
	@Column
	@ApiModelProperty("X室")
	String type_room;
	@Column
	@ApiModelProperty("X厅")
	String type_hall;
	@Column
	@ApiModelProperty("建筑面积")
	Double area;
	@Column
	@ApiModelProperty("单价")
	Double price;
	@Column
	@ApiModelProperty("备注")
	String remark;
	@Column
	@ApiModelProperty("空闲/已出售/已出租")
	String status;
	@Column
	@ApiModelProperty("所属业主")
	Integer person_id;
	@Column
	@ApiModelProperty("开始时间")
	Date start_time;
	@Column
	@ApiModelProperty("结束时间（出售则是开始时间加上70年）")
	Date end_time;
}
