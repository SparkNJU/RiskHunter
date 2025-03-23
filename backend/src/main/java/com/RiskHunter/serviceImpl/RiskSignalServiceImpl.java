package com.RiskHunter.serviceImpl;

import com.RiskHunter.DTO.RiskSignalQueryDTO;
import com.RiskHunter.Mapper.RiskSignalMapper;
import com.RiskHunter.po.RiskSignal;
import com.RiskHunter.service.RiskSignalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        wrapper.orderByAsc("time"); // Ensure ascending order by time
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public Page<RiskSignal> advancedSearch(RiskSignalQueryDTO queryDTO) {
        Page<RiskSignal> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        QueryWrapper<RiskSignal> wrapper = new QueryWrapper<>();

        if (queryDTO.getMinEmp() != null) {
            wrapper.ge("emp", queryDTO.getMinEmp());
        }
        if (queryDTO.getMaxEmp() != null) {
            wrapper.le("emp", queryDTO.getMaxEmp());
        }
        if (queryDTO.getMinExchangeRate() != null) {
            wrapper.ge("exchange_rate", queryDTO.getMinExchangeRate());
        }
        if (queryDTO.getStartTime() != null && queryDTO.getEndTime() != null) {
            wrapper.between("time", queryDTO.getStartTime(), queryDTO.getEndTime());
        }
        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            wrapper.like("analysis", queryDTO.getKeyword());
        }
        if (queryDTO.getBaseCurrency() != null) {
            wrapper.eq("base_currency", queryDTO.getBaseCurrency());
        }
        if (queryDTO.getTargetCurrency() != null) {
            wrapper.eq("target_currency", queryDTO.getTargetCurrency());
        }

        wrapper.orderByAsc("time"); // Ensure ascending order by time

        // 获取总记录数
        long total = count(wrapper);
        page.setTotal(total);

        // 获取分页数据
        page.setRecords(baseMapper.selectPage(page, wrapper).getRecords());

        return page;
    }
}