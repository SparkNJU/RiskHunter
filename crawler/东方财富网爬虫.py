import asyncio
import json

from bs4 import BeautifulSoup
from crawl4ai import AsyncWebCrawler, JsonCssExtractionStrategy


async def main():

    schema = {
    "name": "EastMoney Forex News",
    "baseSelector": "div.repeatList",  # 新闻列表的容器
    "type": "article",
    "fields": [
        {
            "name": "title",
            "type": "text",
            "selector": "div.text > p.title > a::text",  # 新闻标题
            "filters": ["strip"]
        },
        {
            "name": "publish_time",
            "type": "text",
            "selector": "div.text > p.time::text",  # 发布时间
            "filters": [
                {"type": "regex", "pattern": r"\d{4}年\d{2}月\d{2}日 \d{2}:\d{2}"},
                {"type": "replace", "old": "年", "new": "-"},
                {"type": "replace", "old": "月", "new": "-"},
                {"type": "replace", "old": "日", "new": ""}
            ]
        },
        {
            "name": "content",
            "type": "text",
            "selector": "div.text > p.info::text",  # 新闻内容摘要
            "filters": [
                {"type": "strip"},
                {"type": "replace", "old": r"\s{2,}", "new": " "}
            ]
        },
        {
            "name": "keywords",
            "type": "list",
            "selector": "meta[name='keywords']::attr(content)",  # 关键词（如果存在）
            "filters": [{"type": "split", "separator": ","}]
        }
    ],
    "excludes": [
        "div.pagerbox",  # 分页区域
        "div.advert",    # 广告区域
        "iframe.lyad"    # 广告 iframe
    ]
}

    async with AsyncWebCrawler(verbose=True) as crawler:
        result = await crawler.arun(url="https://forex.eastmoney.com/a/cwhxw.html")
        html_content = result.html

        # 使用BeautifulSoup解析HTML
        soup = BeautifulSoup(html_content, 'html.parser')

# 提取新闻条目
        news_items = soup.find_all('li', id=lambda x: x and x.startswith('newsTr'))

# 构建JSON数据
        news_list = []
        for item in news_items:
            title = item.find('p', class_='title').text.strip()
            content = item.find('p', class_='info')['title'].strip()
            time = item.find('p', class_='time').text.strip()

            news_list.append({
                'title': title,
                'content': content,
                'time': time
            })

# 输出JSON格式
        json_output = json.dumps(news_list, ensure_ascii=False, indent=4)
        print(json_output)

if __name__ == "__main__":
    asyncio.run(main())