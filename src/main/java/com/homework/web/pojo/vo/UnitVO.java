package com.homework.web.pojo.vo;

import lombok.Data;

@Data
public class UnitVO {
	Integer id;// 主键
	Integer community_id;// 所属楼栋的id
	Integer code;// 单元编号
	Integer floor;// 层数
	Boolean has_elevator;// 是否有电梯
	String remark;// 备注
	Integer community_code;
}
