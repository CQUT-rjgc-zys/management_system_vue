package com.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 身份, 0-普通用户, 1-管理员
     */
    @TableField("identity")
    private Byte identity;

    /**
     * 性别, 0-男, 1-女
     */
    @TableField("gender")
    private Byte gender;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 工号
     */
    @TableField("job_number")
    private String jobNumber;

    /**
     * 所属部门
     */
    @TableField("department_id")
    private Long department;

    /**
     * 正在执行任务数量
     */
    @TableField("task_amount")
    private Integer taskAmount;
}
