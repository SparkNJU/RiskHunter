package com.RiskHunter.po;
/*
 * @date 02/23 17:59
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;
import javax.persistence.Entity;
@Data
@TableName("risk_signal")
public class RiskSignal {
    @TableId(type = IdType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = false)  // 使用 @Column 注解
    private int BaseCurrency;
    private int TargetCurrency;
    private LocalDateTime time;
    private Double emp;
    private Double exchangeRate;
    private Double interestRate;
    private Double fxReserves;
    private String analysis;
    private String advice;
}