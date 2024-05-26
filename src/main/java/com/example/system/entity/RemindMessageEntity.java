package com.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("remind_messages")
public class RemindMessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 打卡信息ID
     */
    @TableField("dk_info")
    private Long dkInfo;

    @TableField("receive")
    private boolean receive;

}
