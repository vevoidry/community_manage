package com.homework.web.pojo.vo;

import java.util.Date;

import lombok.Data;
@Data
public class AdviceVO {
	Integer id;// 主键
	Integer person_id;// 业主id
	String title;// 标题
	String content;// 内容
	Date start_time;// 开始时间
	Date end_time;// 结束时间
	Boolean is_handle;// 是否已处理

	String person_name;
//	String start_time_string;
//	String end_time_string;
}
