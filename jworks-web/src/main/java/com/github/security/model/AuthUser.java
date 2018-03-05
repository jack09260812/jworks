package com.github.security.model;

import lombok.Data;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by jinwei.li on 2017/8/24 0024.
 */
@Data
public class AuthUser implements Serializable {

    private static final long serialVersionUID = 1073168581936971176L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    //用于csrf防御，rest请求需要在header中返回
    private String uuid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 电话号码
     */
    private String telephone;
    /**
     * 电子邮件
     */
    private String email;
    //录入时间
    private Date createTime;
    //最近修改时间
    private Date modifyTime;
    /**
     * 账户状态，被锁定之类的，默认为0，表示正常
     */
    private int status;
    /**
     * 类型,默认为0，普通用户，1为超级管理员
     */
    private int type;
    /**
     * 角色列表
     */
    private List<Role> roles;

}
