const forexTypes = [
    { type: 'CHN', name: '中国外汇数据' },
    { type: 'USA', name: '美国外汇数据' },
    { type: 'FXR', name: '外汇储备' },
    { type: 'FX_RATE', name: '汇率' },
    { type: 'ROI', name: '利率' },
]

const forexList: { [key: string]: string[] } = {
    CHN: [
        '人民币：实际有效汇率指数',
        'FDI',
        '金融机构有价证券及投资',
        'M2',
        '财政赤字',
        'CPI：当月同比',
        '外债余额：短期债务（季度）',
        '工业增加值：当月同比',
        'M2乘数',
        '国内信贷',
        '工业增加值：当期（季度）',
        '国外净资产',
    ],
    USA: [
        '美元指数（日度）',
        'M2',
        '即期汇率：美元兑人民币（日度）',
        '美国国债长期平均实际利率（日度）',
        '期货结算价：WTI原油（日度）',
        'TED美国国债 - 欧洲美元利差（日度）',
    ],
    FXR: [
        '外汇储备中国香港',
        '外汇储备澳大利亚',
        '外汇储备日本',
        '外汇储备英国',
        '外汇储备中国',
        '外汇储备美国',
        '外汇储备瑞士',
        '外汇储备欧洲中央银行',
    ],
    FX_RATE: [
        '港币兑美元',
        '美元兑瑞士法郎',
        '欧元兑美元',
        '美元兑澳元',
        '美元兑人民币',
        '英镑兑美元',
        '美元兑日元',
    ],
    ROI: [
        '利率欧元区',
        '利率瑞士',
        '利率澳大利亚',
        '利率美国',
        '利率中国香港',
        '利率中国',
        '利率日本',
    ]
};


export const getAllForexTypes = () => {
    return forexTypes
}

export const getAllForexList = () => {
    return forexList
}

export const getForexByName = async (type: string, name: string) => {
    const response = await fetch(`/forex/${type}/${name}.json`)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}