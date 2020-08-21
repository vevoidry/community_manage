package com.homework.web.pojo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class HouseVO {
	Integer id;// 主键
	Integer unit_id;// 单元的id
	Integer code;// 房屋编号
	Integer floor;// 房屋楼层
	String type_room;// X室
	String type_hall;// X厅
	Double area;// 建筑面积
	Double price;// 单价
	String remark;// 备注
	String status;// 空闲/已出售/已出租
	Integer person_id;// 所属业主
	Date start_time;// 开始时间
	Date end_time;// 结束时间（出售则是开始时间加上70年）
	
	Integer community_code;
	Integer unit_code;
	String person_name;
}
