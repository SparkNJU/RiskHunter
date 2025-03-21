# RiskHunter API


**简介**:RiskHunter API


**HOST**:localhost:8080


**联系人**:SparkNJU


**Version**:v1

**时间**：2025.3.22


**接口路径**:/v2/api-docs

**在线访问方法**：在本地启动RiskHunter的后端项目，访问http://localhost:8080/doc.html#/home (**推荐**)或者在线访问上线后的[RiskHunter API 文档](http://www.riskhunter.xyz:8080/doc.html#/home)


[TOC]






# 智能助手相关接口


## 获取历史记录


**接口地址**:`/api/chat/history/{sessionId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取指定会话的历史记录</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sessionId|会话ID|path|false|integer(int64)||
|userId|用户ID|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«List«聊天记录Model»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||array|聊天记录Model|
|&emsp;&emsp;content|消息内容|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;direction|消息方向|boolean||
|&emsp;&emsp;id|记录ID|integer(int64)||
|&emsp;&emsp;sessionId|会话ID|integer(int64)||
|&emsp;&emsp;userId|用户ID|integer(int64)||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": [
    {
      "id": 9,
      "sessionId": 3,
      "userId": 2,
      "direction": true,
      "content": "南京大学是什么",
      "createTime": "2025-03-10T10:15:20"
    },
    {
      "id": 10,
      "sessionId": 3,
      "userId": 2,
      "direction": false,
      "content": "<thought>嗯，用户问南京大学是什么，我需要详细回答。首先，南京大学的基本信息，比如成立时间、地位、历史背景。然后说说它是985、211，还有双一流建设高校，学术实力强。学科方面，文理见长，新工科发展不错，还有医学和社科。可能提到一些优势学科，比如天文、地质、计算机。师资力量，院士和杰出人才的数量。校园环境，鼓楼和仙林校区，风景优美。历史名人，像陶行知、吴健雄，还有校友中的知名人物。国际交流方面，合作院校和留学生情况。最后提一下校训和人才培养理念，强调南大的学术氛围和成就。需要确保信息准确，分点清晰，但不用太正式，口语化一些。检查有没有遗漏的重要信息，比如排名或者近期成就，可能需要补充。</thought>南京大学（Nanjing University，简称“南大”）是中国顶尖的综合性研究型大学之一，位列国家“双一流”（A类）、“985工程”和“211工程”重点建设高校。以下是关于南大的详细介绍：\n\n---\n\n### **历史与地位**\n- **建校时间**：源头可追溯至1902年的**三江师范学堂**，后历经两江师范、南京高等师范学校、国立东南大学、国立中央大学等时期，1950年定名南京大学。\n- **历史地位**：曾为民国最高学府“国立中央大学”，1952年院系调整后分出部分学科组建其他高校（如东南大学、河海大学等），文理学科成为今日南大的基础。\n\n---\n\n### **学术实力**\n- **学科优势**：以文、理学科见长，同时发展新工科、医学和社科。\n  - **王牌学科**：天文学（全国第一）、地质学、计算机科学、环境科学、中国语言文学、物理学等。\n  - **国际排名**：QS、THE等榜单中稳居中国高校前10，部分学科进入全球前100。\n- **科研平台**：拥有国家重点实验室（如固体微结构物理、医药生物技术）、国家协同创新中心等。\n\n---\n\n### **师资与人才**\n- **师资力量**：现有中国科学院与中国工程院院士30余人，国家级人才计划入选者超600人。\n- **人才培养**：培养出近300位两院院士，以及杨振宁、李政道等著名校友。\n\n---\n\n### **校园与文化**\n- **校区**：鼓楼校区（历史建筑群）、仙林校区（现代主校区）、浦口校区（部分本科新生）。\n- **校训**：“诚朴雄伟，励学敦行”，学风严谨自由。\n- **文化特色**：开放包容的学术氛围，注重基础研究，被誉为“中国科学社的大本营”。\n\n---\n\n### **国际影响力**\n- **国际合作**：与哈佛、牛津等世界名校建立合作，全球200+所伙伴院校。\n- **留学生**：每年吸引大批国际学生，参与交换项目和联合研究。\n\n---\n\n### **社会贡献**\n- **科技成果**：参与探月工程、天眼FAST、人工智能等国家重大项目。\n- **杰出校友**：包括“共和国勋章”获得者程开甲、诺贝尔奖得主屠呦呦（南大校友教授）等。\n\n---\n\n南京大学以深厚的历史底蕴、顶尖的学术实力和卓越的人才培养，被誉为“中国最令人尊敬的大学之一”。如需更具体信息（如专业、报考等），可进一步提问！",
      "createTime": "2025-03-10T10:15:49"
    }
  ]
}
```


## 非流式聊天接口


**接口地址**:`/api/chat/noStream`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>通过非流式方式进行聊天</p>



**请求示例**:


```javascript
{
  "message": "hello",
  "sessionId": 2,
  "userId": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|chatRequestDTO|聊天请求DTO|body|false|ChatRequestDTO|ChatRequestDTO|
|&emsp;&emsp;message|||false|string||
|&emsp;&emsp;sessionId|||false|integer(int64)||
|&emsp;&emsp;userId|||false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«string»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||string||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": "Hello! 有什么我可以帮您的吗？无论是学术问题、数据分析，还是其他领域的疑问，都可以告诉我哦~ 😊"
}
```


## RAG流式对话接口


**接口地址**:`/api/chat/ragChat`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>通过RAG流式方式进行对话</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|message|消息内容|query|false|string||
|sessionId|会话ID|query|false|integer(int64)||
|userId|用户ID|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Flux«ServerSentEvent«string»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|prefetch||integer(int32)|integer(int32)|


**响应示例**:
```javascript
data:您好，

data:我是

data:Risk

data:Hunter AI

…………由于内容过长，在文档中省略

data:当前

data:系统

data:时间为

data:2025年

data:3月

data:17日，

data:所有

data:分析均

data:基于该

data:时间

data:节点的

data:市场

data:环境。

```


## RAG知识库搜索接口


**接口地址**:`/api/chat/ragSearch`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>通过RAG方式进行知识库搜索</p>



**请求示例**:


```javascript
{
  "message": "搜索一下外汇风险管理的主要方法",
  "sessionId": 1,
  "userId": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|chatRequestDTO|聊天请求DTO|body|false|ChatRequestDTO|ChatRequestDTO|
|&emsp;&emsp;message|||false|string||
|&emsp;&emsp;sessionId|||false|integer(int64)||
|&emsp;&emsp;userId|||false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«string»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||string||


**响应示例**:
```javascript
{
	"code": "000",
	"msg": "",
	"result": "外汇风险管理方法主要包括以下几种：

货币对冲、货币掉期和货币套期：通过建立反向交易、固定汇率交换现金流或锁定货币关系来平衡或消除外汇风险[1]。例如，货币对冲通过双向交易抵消汇率波动，货币掉期以合同形式固定未来现金流汇率，货币套期则消除不同货币间的相关性风险[1]。

远期合同法与BSI法：远期合同通过匹配外币流入与流出来对冲风险，而BSI法（借款-即期合同-投资法）通过借入外币、兑换本币并投资，同时消除时间与价值风险[2]。

贷款限额与外汇储备管理：银行通过设定地区贷款限额和调整利率进行风险补偿，国家则通过质量管理、数量控制及投资优化管理外汇储备风险[2]。

风险承受能力评估与交易部位定制：投资者需根据财务状况设定单笔交易风险比例（如2%账户额度），并灵活调整交易单位（标准手、迷你手等）以匹配止损目标[4]。

技术性风控措施：包括设置止盈止损、跟踪止损订单以限制亏损，避免周末跳空风险，以及关注重大新闻事件（如央行决策）对市场的冲击[2][4]。

监管与合规手段：国家层面通过强化外汇监管、打击违法违规活动维护市场秩序，企业需遵循合规框架以系统性降低风险[3]。

综合运用以上方法，结合基本面分析与波浪理论等工具，可有效降低外汇波动带来的负面影响[2][4]。"
}
```


## 创建会话接口


**接口地址**:`/api/chat/session`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建一个新的会话</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户唯一标识符（必须大于0）|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«long»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": 6
}
```


## 更新会话标题


**接口地址**:`/api/chat/session/{sessionId}/title`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新指定会话的标题</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sessionId|会话ID|path|false|integer(int64)||
|title|新标题|query|false|string||
|userId|用户ID|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«boolean»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||boolean||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": true
}
```


## 获取用户的所有会话ID


**接口地址**:`/api/chat/sessions`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取指定用户的所有会话ID</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户ID|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«List«聊天会话Model»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||array|聊天会话Model|
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;id|会话ID|integer(int64)||
|&emsp;&emsp;title|会话标题|string||
|&emsp;&emsp;updateTime|更新时间|string(date-time)||
|&emsp;&emsp;userId|用户ID|integer(int64)||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": [
    {
      "id": 5,
      "userId": 2,
      "title": "风险信号:...",
      "createTime": "2025-03-11T20:21:45",
      "updateTime": "2025-03-11T20:23:11"
    },
    {
      "id": 4,
      "userId": 2,
      "title": "",
      "createTime": "2025-03-11T20:03:36",
      "updateTime": "2025-03-11T20:03:36"
    },
    {
      "id": 3,
      "userId": 2,
      "title": "南京大学是...",
      "createTime": "2025-03-10T10:15:10",
      "updateTime": "2025-03-10T10:15:20"
    },
    {
      "id": 2,
      "userId": 2,
      "title": "",
      "createTime": "2025-03-09T23:28:34",
      "updateTime": "2025-03-09T23:28:34"
    },
    {
      "id": 1,
      "userId": 2,
      "title": "花旗杯是...",
      "createTime": "2025-03-09T23:09:17",
      "updateTime": "2025-03-09T23:09:17"
    }
  ]
}
```


## 流式聊天接口


**接口地址**:`/api/chat/stream`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>通过流式方式进行聊天</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|message|消息内容|query|false|string||
|modelName|模型名称|query|false|string||
|sessionId|会话ID|query|false|integer(int64)||
|userId|用户ID|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Flux«ServerSentEvent«string»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|prefetch||integer(int32)|integer(int32)|


**响应示例**:
```javascript
data:<thought>好的</thought>

data:<thought>，</thought>

data:<thought>用户</thought>
…………由于篇幅过长，省略中间内容
data:<thought>调整</thought>

data:<thought>回答</thought>

data:<thought>方向</thought>

data:<thought>。</thought>

data:你好！

data:有什么

data:我可以

data:为你效劳

data:的吗

data:？无论是

data:学术

data:问题、

data:数据分析、

data:生活

data:建议，

data:还是

data:闲聊，

data:我都

data:在这儿

data:帮你

data:解答～🌟

```


# 用户相关接口


## 获取用户信息


**接口地址**:`/api/users`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前登录用户的信息</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«UserVO»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||UserVO|UserVO|
|&emsp;&emsp;address||string||
|&emsp;&emsp;id||integer(int32)||
|&emsp;&emsp;password||string||
|&emsp;&emsp;phone||string||
|&emsp;&emsp;role||integer(int32)||
|&emsp;&emsp;username||string||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": {
    "id": 2,
    "username": "seecoder",
    "phone": "13333333333",
    "password": "123456789",
    "address": null,
    "role": 2
  }
}
```


## 更新用户信息


**接口地址**:`/api/users`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新当前登录用户的信息</p>



**请求示例**:


```javascript
{
  "address": "仙林大道163号",
  "id": 2,
  "password": "123456789",
  "phone": "13333333333",
  "role": 2,
  "username": "SeeCoder"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userVO|用户信息对象|body|true|UserVO|UserVO|
|&emsp;&emsp;address|||false|string||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;password|||false|string||
|&emsp;&emsp;phone|||false|string||
|&emsp;&emsp;role|||false|integer(int32)||
|&emsp;&emsp;username|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«boolean»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||boolean||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": true
}
```


## 用户登录


**接口地址**:`/api/users/login`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>用户登录接口</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|password|用户密码|query|true|string||
|phone|用户手机号|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«string»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||string||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyIiwiZXhwIjoxNzQyMzAxOTQ2fQ.zQrO1009QUXESy5w2wDqP6F3eEwXt5OkaQ1Z-B9jilc"
}
```


## 用户注册


**接口地址**:`/api/users/register`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>注册一个新用户</p>



**请求示例**:


```javascript
{
  "address": "南京大学",
  "id": 6,
  "password": "123456",
  "phone": "1331234567891",
  "role": 1,
  "username": "Test"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userVO|用户信息对象|body|true|UserVO|UserVO|
|&emsp;&emsp;address|||false|string||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;password|||false|string||
|&emsp;&emsp;phone|||false|string||
|&emsp;&emsp;role|||false|integer(int32)||
|&emsp;&emsp;username|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«boolean»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||boolean||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": true
}
```


# 风险信号相关接口


## 分页查询风险信号


**接口地址**:`/api/risk-signals`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据时间范围分页查询风险信号列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|endTime|结束时间|query|false|string(date-time)||
|page|页码|query|false|integer(int32)||
|size|每页大小|query|false|integer(int32)||
|startTime|开始时间|query|false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«Page«风险信号Model»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||Page«风险信号Model»|Page«风险信号Model»|
|&emsp;&emsp;countId||string||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;optimizeCountSql||boolean||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|风险信号Model|
|&emsp;&emsp;&emsp;&emsp;advice|建议|string||
|&emsp;&emsp;&emsp;&emsp;analysis|分析结果|string||
|&emsp;&emsp;&emsp;&emsp;baseCurrency||integer||
|&emsp;&emsp;&emsp;&emsp;emp|EMP值|number||
|&emsp;&emsp;&emsp;&emsp;exchangeRate|汇率|number||
|&emsp;&emsp;&emsp;&emsp;fxReserves|外汇储备|number||
|&emsp;&emsp;&emsp;&emsp;id|信号ID|integer||
|&emsp;&emsp;&emsp;&emsp;interestRate|利率|number||
|&emsp;&emsp;&emsp;&emsp;targetCurrency||integer||
|&emsp;&emsp;&emsp;&emsp;time|时间|string||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": {
    "records": [
      {
        "id": 1,
        "time": "2023-01-01T00:00:00",
        "emp": 100.5,
        "exchangeRate": 1.12,
        "interestRate": 2.5,
        "fxReserves": 500,
        "analysis": "Initial analysis: Stable economy.",
        "advice": "Monitor inflation closely.",
        "targetCurrency": 2,
        "baseCurrency": 1
      },
	  ……………由于内容过长，在此省略
      {
        "id": 20,
        "time": "2023-10-15T00:00:00",
        "emp": 100.5,
        "exchangeRate": 1.12,
        "interestRate": 2.5,
        "fxReserves": 495,
        "analysis": "Economy returning to normal, exchange rate stabilizing.",
        "advice": "Maintain prudent fiscal and monetary policies.",
        "targetCurrency": 4,
        "baseCurrency": 3
      }
    ],
    "total": 0,
    "size": 10,
    "current": 1,
    "orders": [],
    "optimizeCountSql": true,
    "searchCount": true,
    "maxLimit": null,
    "countId": null,
    "pages": 0
  }
}
```


## 创建风险信号


**接口地址**:`/api/risk-signals`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建一个新的风险信号</p>



**请求示例**:


```javascript
{
  "advice": "hard to say",
  "analysis": "what can i say",
  "baseCurrency": 1,
  "emp": 123,
  "exchangeRate": 456,
  "fxReserves": 101,
  "id": 21,
  "interestRate": 10,
  "targetCurrency": 3,
  "time": "2023-10-15T00:00:00"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|signal|风险信号对象|body|true|风险信号Model|风险信号Model|
|&emsp;&emsp;advice|建议||false|string||
|&emsp;&emsp;analysis|分析结果||false|string||
|&emsp;&emsp;baseCurrency|||false|integer(int32)||
|&emsp;&emsp;emp|EMP值||false|number(double)||
|&emsp;&emsp;exchangeRate|汇率||false|number(double)||
|&emsp;&emsp;fxReserves|外汇储备||false|number(double)||
|&emsp;&emsp;id|信号ID||false|integer(int64)||
|&emsp;&emsp;interestRate|利率||false|number(double)||
|&emsp;&emsp;targetCurrency|||false|integer(int32)||
|&emsp;&emsp;time|时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«风险信号Model»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||风险信号Model|风险信号Model|
|&emsp;&emsp;advice|建议|string||
|&emsp;&emsp;analysis|分析结果|string||
|&emsp;&emsp;baseCurrency||integer(int32)||
|&emsp;&emsp;emp|EMP值|number(double)||
|&emsp;&emsp;exchangeRate|汇率|number(double)||
|&emsp;&emsp;fxReserves|外汇储备|number(double)||
|&emsp;&emsp;id|信号ID|integer(int64)||
|&emsp;&emsp;interestRate|利率|number(double)||
|&emsp;&emsp;targetCurrency||integer(int32)||
|&emsp;&emsp;time|时间|string(date-time)||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": {
    "id": 21,
    "time": "2023-10-15T00:00:00",
    "emp": 123,
    "exchangeRate": 456,
    "interestRate": 10,
    "fxReserves": 101,
    "analysis": "what can i say",
    "advice": "hard to say",
    "targetCurrency": 3,
    "baseCurrency": 1
  }
}
```


## 高级查询风险信号


**接口地址**:`/api/risk-signals/search`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>根据多种条件进行高级查询</p>



**请求示例**:


```javascript
{
  "baseCurrency": 0,
  "endTime": "",
  "keyword": "",
  "maxEmp": 0,
  "minEmp": 0,
  "minExchangeRate": 0,
  "page": 0,
  "size": 0,
  "startTime": "",
  "targetCurrency": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|queryDTO|查询条件对象|body|true|RiskSignalQueryDTO|RiskSignalQueryDTO|
|&emsp;&emsp;baseCurrency|||false|integer(int32)||
|&emsp;&emsp;endTime|||false|string(date-time)||
|&emsp;&emsp;keyword|||false|string||
|&emsp;&emsp;maxEmp|||false|number(double)||
|&emsp;&emsp;minEmp|||false|number(double)||
|&emsp;&emsp;minExchangeRate|||false|number(double)||
|&emsp;&emsp;page|||false|integer(int32)||
|&emsp;&emsp;size|||false|integer(int32)||
|&emsp;&emsp;startTime|||false|string(date-time)||
|&emsp;&emsp;targetCurrency|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«Page«风险信号Model»»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||Page«风险信号Model»|Page«风险信号Model»|
|&emsp;&emsp;countId||string||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;optimizeCountSql||boolean||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|风险信号Model|
|&emsp;&emsp;&emsp;&emsp;advice|建议|string||
|&emsp;&emsp;&emsp;&emsp;analysis|分析结果|string||
|&emsp;&emsp;&emsp;&emsp;baseCurrency||integer||
|&emsp;&emsp;&emsp;&emsp;emp|EMP值|number||
|&emsp;&emsp;&emsp;&emsp;exchangeRate|汇率|number||
|&emsp;&emsp;&emsp;&emsp;fxReserves|外汇储备|number||
|&emsp;&emsp;&emsp;&emsp;id|信号ID|integer||
|&emsp;&emsp;&emsp;&emsp;interestRate|利率|number||
|&emsp;&emsp;&emsp;&emsp;targetCurrency||integer||
|&emsp;&emsp;&emsp;&emsp;time|时间|string||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||


**响应示例**:
```javascript
{
	"code": "",
	"msg": "",
	"result": {
		"countId": "",
		"current": 0,
		"maxLimit": 0,
		"optimizeCountSql": true,
		"orders": [
			{
				"asc": true,
				"column": ""
			}
		],
		"pages": 0,
		"records": [
			{
				"advice": "",
				"analysis": "",
				"baseCurrency": 0,
				"emp": 0,
				"exchangeRate": 0,
				"fxReserves": 0,
				"id": 0,
				"interestRate": 0,
				"targetCurrency": 0,
				"time": ""
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	}
}
```


## 更新风险信号


**接口地址**:`/api/risk-signals/{id}`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>根据 ID 更新风险信号</p>



**请求示例**:


```javascript
{
  "advice": "hard to say",
  "analysis": "what can i say",
  "baseCurrency": 1,
  "emp": 123,
  "exchangeRate": 456,
  "fxReserves": 101,
  "id": 15,
  "interestRate": 10,
  "targetCurrency": 3,
  "time": "2023-10-15T00:00:00"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|风险信号 ID|path|true|integer(int64)||
|signal|风险信号对象|body|true|风险信号Model|风险信号Model|
|&emsp;&emsp;advice|建议||false|string||
|&emsp;&emsp;analysis|分析结果||false|string||
|&emsp;&emsp;baseCurrency|||false|integer(int32)||
|&emsp;&emsp;emp|EMP值||false|number(double)||
|&emsp;&emsp;exchangeRate|汇率||false|number(double)||
|&emsp;&emsp;fxReserves|外汇储备||false|number(double)||
|&emsp;&emsp;id|信号ID||false|integer(int64)||
|&emsp;&emsp;interestRate|利率||false|number(double)||
|&emsp;&emsp;targetCurrency|||false|integer(int32)||
|&emsp;&emsp;time|时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«风险信号Model»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||风险信号Model|风险信号Model|
|&emsp;&emsp;advice|建议|string||
|&emsp;&emsp;analysis|分析结果|string||
|&emsp;&emsp;baseCurrency||integer(int32)||
|&emsp;&emsp;emp|EMP值|number(double)||
|&emsp;&emsp;exchangeRate|汇率|number(double)||
|&emsp;&emsp;fxReserves|外汇储备|number(double)||
|&emsp;&emsp;id|信号ID|integer(int64)||
|&emsp;&emsp;interestRate|利率|number(double)||
|&emsp;&emsp;targetCurrency||integer(int32)||
|&emsp;&emsp;time|时间|string(date-time)||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": {
    "id": 15,
    "time": "2023-10-15T00:00:00",
    "emp": 123,
    "exchangeRate": 456,
    "interestRate": 10,
    "fxReserves": 101,
    "analysis": "what can i say",
    "advice": "hard to say",
    "targetCurrency": 3,
    "baseCurrency": 1
  }
}
```


## 删除风险信号


**接口地址**:`/api/risk-signals/{id}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据 ID 删除风险信号</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|风险信号 ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«Void»|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||


**响应示例**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": null
}
```