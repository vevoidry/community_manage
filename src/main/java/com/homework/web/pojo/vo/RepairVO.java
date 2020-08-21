package com.homework.web.pojo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RepairVO {
	Integer id;// 主键
	Integer person_id;// 业主id
	String title;// 标题
	String content;// 内容
	Date start_time;// 创建时间
	Date end_time;// 处理时间
	Boolean is_handle;// 是否已处理
	Integer user_id;// 管理员指定的用户（维修工）的id
	Double price;// 报修收费（维修工设置价格）
	String process;// 维修的过程
	String result;// 维修的结果

	String person_name;
	String person_phone;
	String user_name;
}
