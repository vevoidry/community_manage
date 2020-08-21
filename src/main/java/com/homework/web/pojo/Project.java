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

//收费项目
@Data
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "收费项目表")
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键")
	Integer id;
	@Column
	@ApiModelProperty("项目名")
	String name;
//	@Column
//	String type;// 物业费/车位费/临时车费用/押金
	@Column
	@ApiModelProperty("收费费用")
	Double money;
	@Column
	@ApiModelProperty("备注")
	String remark;
	@Column
	@ApiModelProperty("收费开始时间")
	Date start_date;
	@Column
	@ApiModelProperty("收费截止时间")
	Date end_date;
	@Column
	@ApiModelProperty("收费类型，true为统一收费，false为按面积收费")
	Boolean money_type;
}
