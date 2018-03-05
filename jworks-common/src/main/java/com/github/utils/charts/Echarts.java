package com.github.utils.charts;

import com.github.utils.collections.CollectionsUtil;
import com.github.utils.date.DateUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jinwei.li on 2017/9/19 0019.
 */
public class Echarts {

    /**
     * 填充日期区间，不存在的填充为0
     *
     * @param data
     * @param begin
     * @param end
     * @param precision
     * @return
     */
    public static Map<String, ?> fillDateRange(Map<String, ?> data, Date begin, Date end, int precision) {
        return fillDateRange(data, begin, end, precision, 0);
    }

    /**
     * 填充日期区间
     *
     * @param data
     * @param begin
     * @param end
     * @param precision 1.天 2.小时
     * @return
     */
    public static Map<String, ?> fillDateRange(Map<String, ?> data, Date begin, Date end, int precision, Object fill) {
        if (data == null) {
            throw new RuntimeException("集合不能为空");
        }
        DateTime d1 = new DateTime(begin);
        DateTime d2 = new DateTime(end);
        Map<String, Object> result = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        switch (precision) {
            case 1:
                //天
                int days = Days.daysBetween(d1, d2).getDays();
                for (int i = 0; i <= days; i++) {
                    result.put(d1.plusDays(i).toString("yyyy-MM-dd"), fill);
                }
                result.putAll(data);
                break;
            case 2:
                //小时
                int hours = Hours.hoursBetween(d1, d2).getHours();
                for (int i = 0; i <= hours; i++) {
                    result.put(d1.plusHours(i).toString("yyyy-MM-dd HH"), fill);
                }
                result.putAll(data);
                break;
            default:
                result = null;

        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, Integer> data = ImmutableMap.of("2017-09-01", 10, "2017-09-10", 5);
        Map<String, Object> result = (Map<String, Object>) Echarts.fillDateRange(data, DateUtil.date(2017, 9, 1, 0, 0, 0), new Date(), 1);
        System.out.println(Joiner.on(",").withKeyValueSeparator("=").join(result));
    }
}
