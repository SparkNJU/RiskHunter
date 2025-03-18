package com.RiskHunter.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_record")
@ApiModel(value = "聊天记录Model")
public class ChatRecord {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "会话ID")
    private Long sessionId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "消息方向", notes = "true=用户消息，false=AI消息")
    private Boolean direction;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}