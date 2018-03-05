package com.github.shortlink;

/**
 * 根据设置长度和url生成短链接字符串
 * Created by lijinwei on 2017/4/13.
 */
public interface StringGenerator {

    String generator(String url);

    void setLength(int length);
}
