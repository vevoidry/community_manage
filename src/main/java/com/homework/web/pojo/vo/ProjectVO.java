package com.homework.web.pojo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ProjectVO {
	Integer id;// 主键
	String name;// 项目名
//	String type;// 物业费/车位费/临时车费用/押金
	Double money;// 收费费用
	String remark;// 备注
	Date start_date;// 收费开始时间
	Date end_date;// 收费截止时间
	Boolean money_type;// 收费类型，true为统一收费，false为按面积收费

	Double money_sum_all;// 应该收的总费用
	Double money_sum_ok;// 已经收的总费用
	String start_date_string;
	String end_date_string;
}
