package com.fangchehome.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListSort {
	/**
	 * 针对日期年-月从小到大排序
	 * @param list日期集合
	 * @param str日期字段名
	 * @return
	 */
	public static List<Map> taxisByOrdinal(List<Map> list, String str) {
		try {
			Object[] listArray = list.toArray();
			Object temp;
			for (int i = 0; i < listArray.length; i++) {
				Map map = (Map) listArray[i];
				for (int j = i + 1; j < listArray.length; j++) {
					Map map$ = (Map) listArray[j];
					String str1 = map.get(str).toString();
					String str2 = map$.get(str).toString();
					if (str1.compareTo(str2) > 0) {
						temp = listArray[i];
						listArray[i] = listArray[j];
						listArray[j] = temp;
					}
				}
			}
			List newList = Arrays.asList(listArray);
			return newList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
