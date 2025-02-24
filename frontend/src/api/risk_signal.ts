import { axios } from '../utils/request'
import { RISK_SIGNAL_MODULE } from './_prefix'

/**
 * 风险信号查询参数
 */
export type RiskSignalQueryDTO = {
    startTime?: string        // ISO 8601格式的日期时间字符串
    endTime?: string         // ISO 8601格式的日期时间字符串
    minEmp?: number         // 最小EMP值
    maxEmp?: number         // 最大EMP值
    minExchangeRate?: number // 最小汇率值
    keyword?: string        // 分析内容关键词
    page?: number          // 页码，默认1
    size?: number          // 每页记录数，默认10
    baseCurrency?: string  // 基准货币
    quoteCurrency?: string // 报价货币
}

/**
 * 风险信号实体
 */
export type RiskSignal = {
    id?: number
    time: string           // ISO 8601格式的日期时间字符串
    emp: number           // 外汇压力指标
    exchangeRate: number  // 汇率
    interestRate: number  // 利率
    fxReserves: number   // 外汇储备
    analysis: string      // 分析文本
    advice: string       // 建议文本
    baseCurrency: string // 基准货币
    quoteCurrency: string // 报价货币
    createTime?: string   // 创建时间
}

/**
 * 获取风险信号列表（按时间范围）
 * @param startTime 开始时间（可选）
 * @param endTime 结束时间（可选）
 * @param page 页码（默认1）
 * @param size 每页大小（默认10）
 */
export const getRiskSignals = (
    startTime?: string,
    endTime?: string,
    page?: number,
    size?: number
) => {
    return axios.get(`${RISK_SIGNAL_MODULE}`, {
        params: {
            startTime,
            endTime,
            page,
            size
        }
    })
}

/**
 * 高级搜索风险信号
 * @param queryDTO 查询条件对象
 */
export const searchRiskSignals = (queryDTO: RiskSignalQueryDTO) => {
    return axios.post(`${RISK_SIGNAL_MODULE}/search`, queryDTO)
}

/**
 * 创建风险信号
 * @param signal 风险信号对象
 */
export const createRiskSignal = (signal: RiskSignal) => {
    return axios.post(`${RISK_SIGNAL_MODULE}`, signal)
}

/**
 * 更新风险信号
 * @param id 风险信号ID
 * @param signal 更新的风险信号对象
 */
export const updateRiskSignal = (id: number, signal: RiskSignal) => {
    return axios.put(`${RISK_SIGNAL_MODULE}/${id}`, signal)
}

/**
 * 删除风险信号
 * @param id 风险信号ID
 */
export const deleteRiskSignal = (id: number) => {
    return axios.delete(`${RISK_SIGNAL_MODULE}/${id}`)
}