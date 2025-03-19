export const getAllNews = async () => {
    const response = await fetch('/news/government/news-list.json')
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}

// type: government, market
export const getNewsListByType = async (type: string) => {
    const response = await fetch(`/news/${type}/news-list.json`)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}

export const getNewsById = async (type: string, id: number) => {
    const response = await fetch(`/news/${type}/news-content/${id}.json`)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}