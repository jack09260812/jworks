package com.github.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by jinwei.li on 2017/8/24 0024.
 */
@Data
public class Role implements Serializable {
    private static final long serialVersionUID = 5921195276170827339L;
    // id
    private Integer id;
    // 名称
    private String name;
    // 角色资源
    private List<Resource> resources;
    //录入时间
    private Date createTime;
    //最近修改时间
    private Date modifyTime;
}
