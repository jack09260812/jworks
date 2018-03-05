package com.github.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jinwei.li on 2017/8/24 0024.
 */
@Data
public class Resource implements Serializable {
    private static final long serialVersionUID = 2566068175241290155L;
    //资源ID
    private Integer id;
    //父ID
    private Integer pid;
    //资源名称
    private String name;
    //资源标识
    private String uri;
    //录入时间
    private Date createTime;
    //最近修改时间
    private Date modifyTime;
}
