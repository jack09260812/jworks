package com.github.rpc;

import com.github.rpc.utils.Color;

import java.util.Scanner;

/**
 * Created by jinwei.li on 2017/7/4 0004.
 */
public class Test {
    public static void main(String[] args) throws Throwable {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RpcServer server = RpcServer.getInstance();
                server.register("apple", new Apple());
            }
        }).start();
        Thread.sleep(3000);
        Apple apple = RpcClient.getInstance().proxy("apple",Apple.class);
        apple.print();
        System.out.println(apple.getName());
        System.out.println(apple.add(new Color(1)));
    }
}
