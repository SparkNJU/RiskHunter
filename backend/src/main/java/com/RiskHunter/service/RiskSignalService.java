package com.RiskHunter.service;

import com.RiskHunter.DTO.RiskSignalQueryDTO;
import com.RiskHunter.po.RiskSignal;
import com.RiskHunter.serviceImpl.RiskSignalServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

public interface RiskSignalService extends IService<RiskSignal> {
    public Page<RiskSignal> getByTimeRange(LocalDateTime start, LocalDateTime end, Integer page, Integer size);

    public Page<RiskSignal> advancedSearch(RiskSignalQueryDTO queryDTO) ;
}