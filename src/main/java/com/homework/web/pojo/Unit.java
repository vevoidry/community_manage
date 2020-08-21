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

//单元
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "单元表")
public class Unit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("所属楼栋的id")
	Integer community_id;
	@Column
	@ApiModelProperty("单元编号")
	Integer code;
	@Column
	@ApiModelProperty("层数")
	Integer floor;
	@Column
	@ApiModelProperty("是否有电梯")
	Boolean has_elevator;
	@Column
	@ApiModelProperty("备注")
	String remark;
}
