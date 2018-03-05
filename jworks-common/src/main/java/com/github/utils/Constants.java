package com.github.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by jinwei.li on 2017/1/2.
 * 常量
 */
public class Constants {
    //数据库字段类型和java类型对照表
    public static final Map<String,String> COLUMN_TYPE = ImmutableMap.of("int","int","varchar","String","char","String","timestamp","java.util.Date");
}
