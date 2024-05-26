package com.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("field_tasks")
public class FieldTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    @TableField("name")
    private String name;

    @TableField("start_time")
    private Timestamp startTime;

    @TableField("end_time")
    private Timestamp endTime;

    @TableField("task_brief")
    private String taskBrief;

    @TableField("description")
    private String description;

    @TableField("task_spot")
    private String taskSpot;

    @TableField("status")
    private int status;
}
