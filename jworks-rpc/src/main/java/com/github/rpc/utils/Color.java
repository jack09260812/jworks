package com.github.rpc.utils;

import lombok.Data;

/**
 * Created by jinwei on 17-10-12.
 */
@Data
public class Color {
    private int type;

    public Color(){

    }

    public Color(int type) {
        this.type = type;
    }
}
