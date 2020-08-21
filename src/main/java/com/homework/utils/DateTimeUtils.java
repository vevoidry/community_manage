package com.homework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	// 用于对前端的时间和后端的时间进行转换
	public static String format(Date date) {
		String format = simpleDateFormat.format(date);
		format = format.replaceAll(" ", "T");
		System.out.println(format);
		return format;
	}

}
