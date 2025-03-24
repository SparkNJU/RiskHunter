import csv
import requests
from bs4 import BeautifulSoup
import json
import os

output_dir = 'news-0324content-raw'
os.makedirs(output_dir, exist_ok=True)

csv_file = '0324财经资讯.csv'
start_id = 269

with open(csv_file, 'r', encoding='utf-8') as file:
    reader = list(csv.reader(file))[43::-1]  # 读取所有行并反转
    #next(reader)  # 跳过标题行
    print(reader)
    for idx, row in enumerate(reader, start=0):
        print(row)
        news_id = start_id  + idx
        _, date, original_title, _, _, url = row  # 解包所有列

        # 请求页面
        response = requests.get(url)
        response.encoding = 'utf-8'  # 显式指定编码更可靠
        soup = BeautifulSoup(response.text, 'html.parser')

        # 提取内容
        content_div = soup.find('div', id='ContentBody')  # 通过 id 精准定位
        if content_div:
            paragraphs = [p.get_text(strip=True) for p in content_div.find_all('p')]
            content = '\n'.join(paragraphs)
        else:
            content = "Content not found"

        # 提取标题
        title_tag = soup.find('h1')
        if not title_tag:  # 备选方案
            title_tag = soup.find('title') or soup.find('strong')
        title = title_tag.get_text(strip=True) if title_tag else original_title  # 保留原始标题作备选

        # 构建 JSON
        news_data = {
            'id': news_id,
            'date': date,
            'title': title,
            'content': content,
            'url': url  # 建议保留原文链接
        }

        # 保存文件
        json_file = os.path.join(output_dir, f'news_{news_id}.json')
        with open(json_file, 'w', encoding='utf-8') as json_out:
            json.dump(news_data, json_out, ensure_ascii=False, indent=4)

        print(f'Saved {news_id}')