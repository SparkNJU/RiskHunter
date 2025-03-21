// 定义货币代码映射
export const currencyMap: Record<number, string> = {
    1: 'CNY', // 人民币
    2: 'USD', // 美元
    3: 'EUR', // 欧元
    4: 'JPY', // 日元
    5: 'GBP', // 英镑
    6: 'AUD', // 澳元
    7: 'HKD', // 港币
    8: 'CHF'  // 瑞士法郎
};

// 定义货币名称映射
export const currencyNameMap: Record<string, string> = {
    'CNY': '人民币',
    'USD': '美元',
    'EUR': '欧元',
    'JPY': '日元',
    'GBP': '英镑',
    'AUD': '澳元',
    'HKD': '港币',
    'CHF': '瑞士法郎'
};

// Alpha Vantage API密钥
const API_KEY = '4P0CP76A02XFVJ2U';

// 实时汇率接口
interface RealTimeExchangeRateResponse {
    'Realtime Currency Exchange Rate': {
        '1. From_Currency Code': string;
        '2. From_Currency Name': string;
        '3. To_Currency Code': string;
        '4. To_Currency Name': string;
        '5. Exchange Rate': string;
        '6. Last Refreshed': string;
        '7. Time Zone': string;
        '8. Bid Price': string;
        '9. Ask Price': string;
    };
}

// 每日汇率数据接口
interface DailyExchangeRateResponse {
    'Meta Data': {
        '1. Information': string;
        '2. From Symbol': string;
        '3. To Symbol': string;
        '4. Last Refreshed': string;
        '5. Output Size': string;
    };
    'Time Series FX (Daily)': Record<string, {
        '1. open': string;
        '2. high': string;
        '3. low': string;
        '4. close': string;
    }>;
}

/**
 * 获取实时汇率
 * @param fromCurrency 源货币编号
 * @param toCurrency 目标货币编号
 * @returns 实时汇率数据，包含汇率、货币信息等
 */
export async function getRealTimeExchangeRate(
    fromCurrency: number, 
    toCurrency: number
): Promise<RealTimeExchangeRateResponse> {
    try {
        const fromCode = currencyMap[fromCurrency];
        const toCode = currencyMap[toCurrency];
        
        if (!fromCode || !toCode) {
            throw new Error('无效的货币代码');
        }
        
        const url = `https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=${fromCode}&to_currency=${toCode}&apikey=${API_KEY}`;
        
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`API请求失败: ${response.status} ${response.statusText}`);
        }
        
        const data: RealTimeExchangeRateResponse = await response.json();
        return data;
    } catch (error) {
        console.error('获取实时汇率时出错:', error);
        throw error;
    }
}

/**
 * 获取每日汇率历史数据
 * @param fromCurrency 源货币编号
 * @param toCurrency 目标货币编号
 * @param outputSize 数据量大小 ('compact'或'full')
 * @returns 每日汇率历史数据
 */
export async function getDailyExchangeRate(
    fromCurrency: number, 
    toCurrency: number,
    outputSize: 'compact' | 'full' = 'compact'
): Promise<DailyExchangeRateResponse> {
    try {
        const fromCode = currencyMap[fromCurrency];
        const toCode = currencyMap[toCurrency];
        
        if (!fromCode || !toCode) {
            throw new Error('无效的货币代码');
        }
        
        const url = `https://www.alphavantage.co/query?function=FX_DAILY&from_symbol=${fromCode}&to_symbol=${toCode}&outputsize=${outputSize}&apikey=${API_KEY}`;
        
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`API请求失败: ${response.status} ${response.statusText}`);
        }
        
        const data: DailyExchangeRateResponse = await response.json();
        return data;
    } catch (error) {
        console.error('获取每日汇率数据时出错:', error);
        throw error;
    }
}

/**
 * 获取处理过的每日汇率数据，适用于图表显示
 * @param fromCurrency 源货币编号
 * @param toCurrency 目标货币编号
 * @param days 获取的天数
 * @returns 处理后的汇率数据，包含日期和收盘价
 */
export async function getProcessedDailyRates(
    fromCurrency: number,
    toCurrency: number,
    days: number = 30
): Promise<{ date: string; rate: number }[]> {
    try {
        const outputSize = days > 100 ? 'full' : 'compact';
        const data = await getDailyExchangeRate(fromCurrency, toCurrency, outputSize);
        
        const timeSeriesData = data['Time Series FX (Daily)'];
        const processedData = Object.entries(timeSeriesData)
            .map(([date, values]) => ({
                date,
                rate: parseFloat(values['4. close'])
            }))
            .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
            .slice(-days);
            
        return processedData;
    } catch (error) {
        console.error('处理每日汇率数据时出错:', error);
        throw error;
    }
}

/**
 * 格式化汇率显示
 * @param rate 汇率值 
 * @param digits 小数位数
 * @returns 格式化后的汇率字符串
 */
export function formatExchangeRate(rate: number | string, digits: number = 4): string {
    const numericRate = typeof rate === 'string' ? parseFloat(rate) : rate;
    return numericRate.toFixed(digits);
}

/**
 * 获取两种货币的描述性文本
 * @param fromCurrency 源货币编号
 * @param toCurrency 目标货币编号
 * @returns 描述性文本，如"人民币兑美元"
 */
export function getCurrencyPairDescription(fromCurrency: number, toCurrency: number): string {
    const fromCode = currencyMap[fromCurrency];
    const toCode = currencyMap[toCurrency];
    
    if (!fromCode || !toCode) {
        return '未知货币对';
    }
    
    return `${currencyNameMap[fromCode]}兑${currencyNameMap[toCode]}`;
}