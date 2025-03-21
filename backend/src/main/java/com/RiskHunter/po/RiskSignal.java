package com.RiskHunter.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@TableName("risk_signal")
@ApiModel(value = "风险信号Model")
public class RiskSignal {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "信号ID")
    private Long id;

    @Column(nullable = false, updatable = false)
    @ApiModelProperty(value = "基准货币")
    private Integer BaseCurrency;

    @ApiModelProperty(value = "目标货币")
    private Integer TargetCurrency;

    @ApiModelProperty(value = "时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "EMP值")
    private Double emp;

    @ApiModelProperty(value = "汇率")
    private Double exchangeRate;

    @ApiModelProperty(value = "利率")
    private Double interestRate;

    @ApiModelProperty(value = "外汇储备")
    private Double fxReserves;

    @ApiModelProperty(value = "新闻指数")
    private Double news;

    @ApiModelProperty(value = "分析结果")
    private String analysis;

    @ApiModelProperty(value = "建议")
    private String advice;
}