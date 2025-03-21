package com.RiskHunter.service;

import com.RiskHunter.vo.AlertVO;
import com.RiskHunter.vo.ExposureMatrixVO;
import com.RiskHunter.vo.RiskDashboardVO;
import com.RiskHunter.vo.RiskMapVO;

import java.util.List;

public interface CalculateService {
    RiskDashboardVO getRiskDashboard();
    AlertVO getAlert();
    RiskMapVO getRiskMap();
    List<ExposureMatrixVO> getExposure();
}