package com.RiskHunter.po;/*
 * @date 03/02 15:10
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("chat_record")
public class ChatRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sessionId;
    private Long userId;
    private Boolean direction; // true=用户消息，false=AI消息
    private String content;
    private LocalDateTime createTime;
}