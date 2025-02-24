package com.RiskHunter.serviceImpl;/*
 * @date 02/23 18:03
 */

import com.RiskHunter.DTO.RiskSignalQueryDTO;
import com.RiskHunter.Mapper.RiskSignalMapper;
import com.RiskHunter.po.RiskSignal;
import com.RiskHunter.service.RiskSignalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RiskSignalServiceImpl extends ServiceImpl<RiskSignalMapper, RiskSignal> implements RiskSignalService {

    @Override
    public Page<RiskSignal> getByTimeRange(LocalDateTime start, LocalDateTime end, Integer page, Integer size) {
        QueryWrapper<RiskSignal> wrapper = new QueryWrapper<>();
        if (start != null) {
            wrapper.ge("time", start);
        }
        if (end != null) {
            wrapper.le("time", end);
        }
        return page(new Page<>(page, size), wrapper);
    }
    @Override
    public Page<RiskSignal> advancedSearch(RiskSignalQueryDTO queryDTO) {
        return lambdaQuery()
                .ge(queryDTO.getMinEmp() != null, RiskSignal::getEmp, queryDTO.getMinEmp())
                .le(queryDTO.getMaxEmp() != null, RiskSignal::getEmp, queryDTO.getMaxEmp())
                .ge(queryDTO.getMinExchangeRate() != null, RiskSignal::getExchangeRate, queryDTO.getMinExchangeRate())
                .between(queryDTO.getStartTime() != null && queryDTO.getEndTime() != null,
                        RiskSignal::getTime, queryDTO.getStartTime(), queryDTO.getEndTime())
                .like(StringUtils.isNotBlank(queryDTO.getKeyword()), RiskSignal::getAnalysis, queryDTO.getKeyword())
                .eq(queryDTO.getBaseCurrency() != null, RiskSignal::getBaseCurrency, queryDTO.getBaseCurrency())
                .eq(queryDTO.getTargetCurrency() != null, RiskSignal::getTargetCurrency, queryDTO.getTargetCurrency())
                .orderByDesc(RiskSignal::getTime)
                .page(new Page<>(queryDTO.getPage(), queryDTO.getSize()));
    }


}