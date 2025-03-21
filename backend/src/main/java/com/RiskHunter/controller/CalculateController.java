package com.RiskHunter.controller;

import com.RiskHunter.service.CalculateService;
import com.RiskHunter.vo.AlertVO;
import com.RiskHunter.vo.ExposureMatrixVO;
import com.RiskHunter.vo.RiskDashboardVO;
import com.RiskHunter.vo.RiskMapVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/calculate")
@RequiredArgsConstructor
@Api(tags = "风险计算接口")
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @ApiOperation(value = "获取风险仪表盘数据", notes = "获取风险仪表盘数据")
    @GetMapping("/dashboard")
    public RiskDashboardVO getRiskDashboard() {
        return calculateService.getRiskDashboard();
    }

    @ApiOperation(value = "获取预警信息", notes = "获取预警信息")
    @GetMapping("/alert")
    public AlertVO getAlert() {
        return calculateService.getAlert();
    }

    @ApiOperation(value = "获取风险地图数据", notes = "获取风险地图数据")
    @GetMapping("/map")
    public RiskMapVO getRiskMap() {
        return calculateService.getRiskMap();
    }

    @ApiOperation(value = "获取敞口分析数据", notes = "获取敞口分析数据")
    @GetMapping("/exposure")
    public List<ExposureMatrixVO> getExposure() {
        return calculateService.getExposure();
    }
}