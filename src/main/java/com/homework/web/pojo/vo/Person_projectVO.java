package com.homework.web.pojo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class Person_projectVO {
	Integer id;// 主键
	Integer person_id;// 业主id
	Integer project_id;// 项目id
	Date create_time;// 发起收费的时间
	Boolean is_handle;// 是否已收费
	String method;// 收费方式，微信/支付宝
	Double money;// 收费费用
	Date end_time;// 缴费时间
	Boolean type;// 收费类型
	Integer type_id;// 收费类型对应的id
	String person_name;
	String project_name;
	String data;
}
