package com.homework.web.pojo.vo;

import lombok.Data;

@Data
public class MenuVO {
	Integer id;// 主键
	Integer parent_id;// 若是父菜单则该值是0，若是子菜单则该值为其父菜单的id
	String page_name;// 页面名称
	String name;// 名称
	String icon;// 图标
	Integer rank;// 排序值
	Boolean is_using;// 是否可用
	String parent_name;
}
