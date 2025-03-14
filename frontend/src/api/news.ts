export const getAllNews = async () => {
    const response = await fetch('/news/news-list.json')
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}

export const getNewsById = async (id: number) => {
    const response = await fetch(`/news/news-content/${id}.json`)
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const data = await response.json()
    return data
}