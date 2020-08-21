package com.homework.web.pojo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class SeatVO {
	Integer id;// 主键
	Integer park_id;// 所属停车场的id
	Integer code;// 编号
	Double area;// 面积
	String remark;// 备注
	Integer type;// null是空闲，去是出售，2是出租
	Date end_date;//出租终止时间
	Integer person_id;// 所属业主
	String region;// 区域
	Integer park_code;
	String person_name;
	String person_phone;
}
