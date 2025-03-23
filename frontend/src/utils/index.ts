//将身份转化为中文显示
export function parseRole(role: number | null) {
    if (role === 1) {
        return "企业"
    } else if (role === 2) {
        return "金融机构"
    }
}

// 将时间转化为日常方式
export function parseTime(time: string) {
    // 如果时间已经是 'YYYY-MM-DD HH:MM:SS' 格式，直接返回
    if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(time)) {
        return time;
    }
    
    // 处理ISO格式 'YYYY-MM-DDTHH:MM:SS.sssZ'
    let times = time.split(/T|\./)
    return times[0] + " " + (times[1] || "");
}

export const CurrencyList = [
    { number: 1, code: 'CNY', name: '人民币' },
    { number: 2, code: 'USD', name: '美元' },
    { number: 3, code: 'EUR', name: '欧元' },
    { number: 4, code: 'JPY', name: '日元' },
    { number: 5, code: 'GBP', name: '英镑' },
    { number: 6, code: 'AUD', name: '澳元' },
    { number: 7, code: 'HKD', name: '港币' },
    { number: 8, code: 'CHF', name: '瑞士法郎' }
];

export function parseCurrencyName(index: number): string {
    const currency = CurrencyList.find(c => c.number === index)
    return currency ? `${currency.code}` : 'null'
}


