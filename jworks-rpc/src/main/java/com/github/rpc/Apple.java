package com.github.rpc;

import com.github.rpc.utils.Color;

/**
 * Created by jinwei.li on 2017/7/4 0004.
 */
public class Apple {


    public void print() {
        System.out.println("this is a apple");
    }

    public String getName() {
        return "apple";
    }

    public int add(Color color) {
        int i = 0;
        i += color.getType();
        return i;
    }
}
