export const newsImages = [
  new URL('../assets/images/news/1.png', import.meta.url).href,
  new URL('../assets/images/news/2.png', import.meta.url).href,
  new URL('../assets/images/news/3.png', import.meta.url).href,
  new URL('../assets/images/news/4.png', import.meta.url).href,
]

// type: government, market
export const getNewsListByType = async (type: string) => {
    const response = await fetch(`/news/${type}/news-list.json`)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}

export const getNewsById = async (type: string, id: number) => {
    // 由于数据暂未适配故类型限制为market
    // const response = await fetch(`/news/${type}/news-content/${id}.json`)
    const response = await fetch(`/news/market/news-content/${id}.json`)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}