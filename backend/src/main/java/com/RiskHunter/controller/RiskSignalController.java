package com.RiskHunter.controller;/*
 * @date 02/23 18:05
 */

import com.RiskHunter.DTO.RiskSignalQueryDTO;
import com.RiskHunter.po.RiskSignal;
import com.RiskHunter.service.RiskSignalService;
import com.RiskHunter.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;

@RestController
@Api(value = "风险信号管理", tags = { "风险信号相关接口" })
@RequestMapping("/api/risk-signals")
@RequiredArgsConstructor
public class RiskSignalController {
    @Autowired
    private RiskSignalService riskSignalService;

    /**
     * 创建 RiskSignal
     *
     * @param signal RiskSignal 对象，通过 RequestBody 传递
     * @return ResultVO 包含创建的 RiskSignal 对象的 ResultVO
     */
    @ApiOperation(value = "创建风险信号", notes = "创建一个新的风险信号")
    @PostMapping
    public ResultVO<RiskSignal> create(
            @ApiParam(value = "风险信号对象", required = true) @RequestBody RiskSignal signal) {
        riskSignalService.save(signal);
        return ResultVO.buildSuccess(signal);
    }

    /**
     * 根据时间范围分页查询 RiskSignal 列表
     *
     * @param startTime  可选，开始时间，LocalDateTime 类型
     * @param endTime    可选，结束时间，LocalDateTime 类型
     * @param page       可选，页码，默认为 1
     * @param size       可选，每页大小，默认为 10
     * @return ResultVO 包含分页 RiskSignal 列表的 ResultVO
     */
    @ApiOperation(value = "分页查询风险信号", notes = "根据时间范围分页查询风险信号列表")
    @GetMapping
    public ResultVO<Page<RiskSignal>> list(
            @ApiParam(value = "开始时间", required = false) @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam(value = "结束时间", required = false) @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "10") @RequestParam(defaultValue = "10") Integer size) {
        // 设置默认开始时间为1900年1月1日
        if(startTime == null) {
            startTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        }

        // 设置默认结束时间为当前时间
        if(endTime == null) {
            endTime = LocalDateTime.now();
        }
        return ResultVO.buildSuccess(riskSignalService.getByTimeRange(startTime, endTime, page, size));
    }

    /**
     * 根据 ID 更新 RiskSignal
     *
     * @param id     RiskSignal 的 ID，通过 PathVariable 传递
     * @param signal RiskSignal 对象，包含要更新的信息，通过 RequestBody 传递
     * @return ResultVO 包含更新后的 RiskSignal 对象的 ResultVO
     */
    @ApiOperation(value = "更新风险信号", notes = "根据 ID 更新风险信号")
    @PutMapping("/{id}")
    public ResultVO<RiskSignal> update(
            @ApiParam(value = "风险信号 ID", required = true) @PathVariable Long id,
            @ApiParam(value = "风险信号对象", required = true) @RequestBody RiskSignal signal) {
        signal.setId(id);
        riskSignalService.updateById(signal);
        return ResultVO.buildSuccess(signal);
    }

    /**
     * 根据 ID 删除 RiskSignal
     *
     * @param id RiskSignal 的 ID，通过 PathVariable 传递
     * @return ResultVO 包含操作结果的 ResultVO
     */
    @ApiOperation(value = "删除风险信号", notes = "根据 ID 删除风险信号")
    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(
            @ApiParam(value = "风险信号 ID", required = true) @PathVariable Long id) {
        riskSignalService.removeById(id);
        return ResultVO.buildSuccess(null);
    }

    /**
     * 高级查询 RiskSignal 列表
     *
     * @param queryDTO RiskSignalQueryDTO 对象，包含查询条件，通过 RequestBody 传递
     * @return ResultVO 包含分页 RiskSignal 列表的 ResultVO
     * * 前端需要传递的 JSON 格式如下：
     *      *
     *      * {
     *      *   "startTime": "2024-02-23T10:00:00",  // 开始时间，ISO 8601格式的日期时间字符串
     *      *   "endTime": "2024-02-23T18:00:00",    // 结束时间，ISO 8601格式的日期时间字符串
     *      *   "minEmp": 0.5,                       // 最小EMP值，可选
     *      *   "maxEmp": 1.5,                       // 最大EMP值，可选
     *      *   "minExchangeRate": 6.5,              // 最小汇率值，可选
     *      *   "keyword": "风险",                    // 分析内容关键词，可选
     *      *   "page": 1,                           // 页码，默认值为1
     *      *   "size": 10                           // 每页记录数，默认值为10
     *      * }
     *      * 所有字段都是可选的。如果不需要某个条件，可以省略相应的字段或传 null。例如，一个最简单的查询可以是：
     *      *
     *      * {
     *      *   "page": 1,
     *      *   "size": 10
     *      * }
     *      * 或者只按时间范围查询：
     *      *
     *      * {
     *      *   "startTime": "2024-02-23T00:00:00",
     *      *   "endTime": "2024-02-23T23:59:59",
     *      *   "page": 1,
     *      *   "size": 10
     *      * }
     */
    @ApiOperation(value = "高级查询风险信号", notes = "根据多种条件进行高级查询")
    @PostMapping("/search")
    public ResultVO<Page<RiskSignal>> advancedSearch(
            @ApiParam(value = "查询条件对象", required = true) @RequestBody RiskSignalQueryDTO queryDTO) {
        return ResultVO.buildSuccess(riskSignalService.advancedSearch(queryDTO));
    }
}