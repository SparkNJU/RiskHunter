# RiskHunter API


**Description**:RiskHunter API


**HOST**:localhost:8080


**Contacts**:SparkNJU


**Version**:v1

**Time**: 2025.3.24

**URL**:/v2/api-docs

**Online Access**: Start the RiskHunter backend locally and visit http://localhost:8080/doc.html#/home (**Recommended**) or access the online [RiskHunter API Documentation](http://www.riskhunter.xyz:8080/doc.html#/home).

[TOC]






# Smart Assistant related Interfaces

## Get Chat History


**url**:`/api/chat/history/{sessionId}`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Description**: <p>Retrieve chat history for a specified session. </p>



**Params**:


| Parameter | Description | In    | Required | Type           | Schema |
| --------- | ----------- | ----- | -------- | -------------- | ------ |
| sessionId | Session ID  | path  | false    | integer(int64) |        |
| userId    | User ID     | query | false    | integer(int64) |        |


**Status**:


| Code | Description  | Schema                          |
| ---- | ------------ | ------------------------------- |
| 200  | OK           | ResultVO«List«ChatRecordModel»» |
| 401  | Unauthorized |                                 |
| 403  | Forbidden    |                                 |
| 404  | Not Found    |                                 |


**Response Params**:


| Parameter  | Description       | Type              | Schema          |
| ---------- | ----------------- | ----------------- | --------------- |
| code       | Status code       | string            |                 |
| msg        | Message           | string            |                 |
| result     | Chat records      | array             | ChatRecordModel |
| content    | Message content   | string            |                 |
| createTime | Creation time     | string(date-time) |                 |
| direction  | Message direction | boolean           |                 |
| id         | Record ID         | integer(int64)    |                 |
| sessionId  | Session ID        | integer(int64)    |                 |
| userId     | User ID           | integer(int64)    |                 |



**Response Example**:
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
      "content": "What is Nanjing University?",
      "createTime": "2025-03-10T10:15:20"
    },
    {
      "id": 10,
      "sessionId": 3,
      "userId": 2,
      "direction": false,
      "content": "<thought>...</thought>Nanjing University (NJU) is one of China's top comprehensive research universities...",
      "createTime": "2025-03-10T10:15:49"
    }
  ]
}
```


## Non-Streaming Chat


**url**:`/api/chat/noStream`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Chat via non-streaming mode.</p>



**Example**:


```javascript
{
  "message": "hello",
  "sessionId": 2,
  "userId": 1
}
```


**Params**:


| name                  | description      | in   | require | type           | schema         |
| --------------------- | ---------------- | ---- | ------- | -------------- | -------------- |
| chatRequestDTO        | Chat request DTO | body | false   | ChatRequestDTO | ChatRequestDTO |
| &emsp;&emsp;message   |                  |      | false   | string         |                |
| &emsp;&emsp;sessionId |                  |      | false   | integer(int64) |                |
| &emsp;&emsp;userId    |                  |      | false   | integer(int64) |                |


**Status**:


| code | description  | schema           |
| ---- | ------------ | ---------------- |
| 200  | OK           | ResultVO«string» |
| 201  | Created      |                  |
| 401  | Unauthorized |                  |
| 403  | Forbidden    |                  |
| 404  | Not Found    |                  |


**Response Params**:


| name   | description | type   | schema |
| ------ | ----------- | ------ | ------ |
| code   |             | string |        |
| msg    |             | string |        |
| result |             | string |        |


**Response Example**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": "Hello! Is there anything I can help you with? Whether it's academic questions, data analysis, or inquiries in other fields, feel free to let me know~ 😊"
}
```


## RAG Streaming Dialogue Interface


**url**:`/api/chat/ragChat`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Conduct a dialogue in a streaming manner via RAG.</p>



**Params**:


| name      | description     | in    | require | type           | schema |
| --------- | --------------- | ----- | ------- | -------------- | ------ |
| message   | Message content | query | false   | string         |        |
| sessionId | Session ID      | query | false   | integer(int64) |        |
| userId    | User ID         | query | false   | integer(int64) |        |

**Status**:


| code | description  | schema                        |
| ---- | ------------ | ----------------------------- |
| 200  | OK           | Flux«ServerSentEvent«string»» |
| 401  | Unauthorized |                               |
| 403  | Forbidden    |                               |
| 404  | Not Found    |                               |


**Response Params**:


| name     | description | type           | schema         |
| -------- | ----------- | -------------- | -------------- |
| prefetch |             | integer(int32) | integer(int32) |


**Response Example**:
```javascript
data: Hello,
data: I am
data: RiskHunter AI...
```


## RAG Knowledge Base Search Interface


**url**:`/api/chat/ragSearch`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p> Search the knowledge base via RAG.</p>



**Example**:


```javascript
{
  "message": "Search for the main methods of foreign exchange risk management",
  "sessionId": 1,
  "userId": 1
}
```


**Params**:


| name                  | description      | in   | require | type           | schema         |
| --------------------- | ---------------- | ---- | ------- | -------------- | -------------- |
| chatRequestDTO        | Chat request DTO | body | false   | ChatRequestDTO | ChatRequestDTO |
| &emsp;&emsp;message   |                  |      | false   | string         |                |
| &emsp;&emsp;sessionId |                  |      | false   | integer(int64) |                |
| &emsp;&emsp;userId    |                  |      | false   | integer(int64) |                |


**Status**:


| code | description  | schema           |
| ---- | ------------ | ---------------- |
| 200  | OK           | ResultVO«string» |
| 201  | Created      |                  |
| 401  | Unauthorized |                  |
| 403  | Forbidden    |                  |
| 404  | Not Found    |                  |


**Response Params**:


| name   | description | type   | schema |
| ------ | ----------- | ------ | ------ |
| code   |             | string |        |
| msg    |             | string |        |
| result |             | string |        |


**Response Example**:
```javascript
{
    "code": "000",
    "msg": "",
    "result": "The main methods of foreign exchange risk management include the following: Currency hedging, currency swaps, and currency hedging: Balance or eliminate foreign exchange risks by establishing reverse transactions, exchanging cash flows at fixed exchange rates, or locking in currency relationships [1]. For example, currency hedging offsets exchange rate fluctuations through two-way transactions, currency swaps fix the exchange rate of future cash flows in the form of a contract, and currency hedging eliminates the correlation risk between different currencies [1]."
}
```


## Create Session Interface


**url**:`/api/chat/session`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Create a new chat session.</p>



**Params**:


| name   | description                                            | in    | require | type           | schema |
| ------ | ------------------------------------------------------ | ----- | ------- | -------------- | ------ |
| userId | Unique identifier of the user (must be greater than 0) | query | false   | integer(int64) |        |

**Status**:


| code | description  | schema         |
| ---- | ------------ | -------------- |
| 200  | OK           | ResultVO«long» |
| 201  | Created      |                |
| 401  | Unauthorized |                |
| 403  | Forbidden    |                |
| 404  | Not Found    |                |


**Response Params**:


| name   | description | type           | schema         |
| ------ | ----------- | -------------- | -------------- |
| code   |             | string         |                |
| msg    |             | string         |                |
| result |             | integer(int64) | integer(int64) |


**Response Example**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": 6
}
```
## Delete Session Interface


**url**:`/api/chat/session/{sessionId}`


**method**:`DELETE`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Delete session by id</p>



**Params**:


| name | description | in    | require | type | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sessionId|session ID|path|false|integer(int64)||
|userId|user ID|query|false|integer(int64)||


**Status**:


| code | description | schema |
| -------- | -------- | ----- | 
|200|OK|ResultVO«boolean»|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**Response Params**:


| name | description | type | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|msg||string||
|result||boolean||


**Response Example**:
```javascript
{
	"code": "",
	"msg": "",
	"result": true
}
```


## Update Session Title


**url**:`/api/chat/session/{sessionId}/title`


**method**:`PUT`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Update the title of the specified session.</p>



**Params**:

| name      | description | in    | require | type           | schema |
| --------- | ----------- | ----- | ------- | -------------- | ------ |
| sessionId | Session ID  | path  | false   | integer(int64) |        |
| title     | New title   | query | false   | string         |        |
| userId    | User ID     | query | false   | integer(int64) |        |


**Status**:


| code | description  | schema            |
| ---- | ------------ | ----------------- |
| 200  | OK           | ResultVO«boolean» |
| 201  | Created      |                   |
| 401  | Unauthorized |                   |
| 403  | Forbidden    |                   |
| 404  | Not Found    |                   |


**Response Params**:


| name   | description | type    | schema |
| ------ | ----------- | ------- | ------ |
| code   |             | string  |        |
| msg    |             | string  |        |
| result |             | boolean |        |


**Response Example**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": true
}
```


## Get All Session IDs of a User


**url**:`/api/chat/sessions`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Get all session IDs of the specified user.</p>



**Params**:


| name   | description | in    | require | type           | schema |
| ------ | ----------- | ----- | ------- | -------------- | ------ |
| userId | User ID     | query | false   | integer(int64) |        |


**Status**:


| code | description  | schema                           |
| ---- | ------------ | -------------------------------- |
| 200  | OK           | ResultVO«List«ChatSessionModel»» |
| 401  | Unauthorized |                                  |
| 403  | Forbidden    |                                  |
| 404  | Not Found    |                                  |


**Response Params**:


| name                   | description   | type              | schema           |
| ---------------------- | ------------- | ----------------- | ---------------- |
| code                   |               | string            |                  |
| msg                    |               | string            |                  |
| result                 |               | array             | ChatSessionModel |
| &emsp;&emsp;createTime | Creation time | string(date-time) |                  |
| &emsp;&emsp;id         | Session ID    | integer(int64)    |                  |
| &emsp;&emsp;title      | Session title | string            |                  |
| &emsp;&emsp;updateTime | Update time   | string(date-time) |                  |
| &emsp;&emsp;userId     | User ID       | integer(int64)    |                  |


**Response Example**:
```javascript
{
    "code": "",
    "msg": "",
    "result": [
        {
            "createTime": "2025-03-09T23:09:17",
            "id": 1,
            "title": "What is the Citi Cup?",
            "updateTime": "2025-03-09T23:09:17",
            "userId": 2
        },
        {
            "createTime": "2025-03-09T23:28:34",
            "id": 2,
            "title": "",
            "updateTime": "2025-03-09T23:28:34",
            "userId": 2
        },
        {
            "createTime": "2025-03-10T10:15:10",
            "id": 3,
            "title": "What is Nanjing University?",
            "updateTime": "2025-03-10T10:15:20",
            "userId": 2
        },
        {
            "createTime": "2025-03-11T20:03:36",
            "id": 4,
            "title": "",
            "updateTime": "2025-03-11T20:03:36",
            "userId": 2
        },
        {
            "createTime": "2025-03-11T20:21:45",
            "id": 5,
            "title": "Risk signals:...",
            "updateTime": "2025-03-11T20:23:11",
            "userId": 2
        }
    ]
}
```


## Streaming Chat Interface


**url**:`/api/chat/stream`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Conduct a chat in a streaming manner.</p>



**Params**:


| name      | description     | in    | require | type           | schema |
| --------- | --------------- | ----- | ------- | -------------- | ------ |
| message   | Message content | query | false   | string         |        |
| modelName | Model name      | query | false   | string         |        |
| sessionId | Session ID      | query | false   | integer(int64) |        |
| userId    | User ID         | query | false   | integer(int64) |        |

**Status**:


| code | description  | schema                        |
| ---- | ------------ | ----------------------------- |
| 200  | OK           | Flux«ServerSentEvent«string»» |
| 401  | Unauthorized |                               |
| 403  | Forbidden    |                               |
| 404  | Not Found    |                               |


**Response Params**:


| name     | description | type           | schema         |
| -------- | ----------- | -------------- | -------------- |
| prefetch |             | integer(int32) | integer(int32) |


**Response Example**:
```javascript
data:<thought>Sure</thought>
data:<thought>,</thought>
data:<thought>how</thought>
// Omit the middle content due to space limitations
data:<thought>adjust</thought>
data:<thought>the</thought>
data:<thought>answer</thought>
data:<thought>direction</thought>
data:<thought>.</thought>
data:Hello!
data:Is there anything
data:I can
data:help you
data:with ? Whether it's
data:academic
data:questions,
data:data analysis,
data:life
data:advice,or
data:just a chat,
data:I'm here
```


# User related Interfaces


## Get User Information


**url**:`/api/users`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Get the information of the currently logged-in user.</p>



**Params**:


None


**Status**:


| code | description  | schema           |
| ---- | ------------ | ---------------- |
| 200  | OK           | ResultVO«UserVO» |
| 401  | Unauthorized |                  |
| 403  | Forbidden    |                  |
| 404  | Not Found    |                  |


**Response Params**:


| name                 | description | type           | schema |
| -------------------- | ----------- | -------------- | ------ |
| code                 |             | string         |        |
| msg                  |             | string         |        |
| result               |             | UserVO         | UserVO |
| &emsp;&emsp;address  |             | string         |        |
| &emsp;&emsp;id       |             | integer(int32) |        |
| &emsp;&emsp;password |             | string         |        |
| &emsp;&emsp;phone    |             | string         |        |
| &emsp;&emsp;role     |             | integer(int32) |        |
| &emsp;&emsp;username |             | string         |        |


**Response Example**:
```javascript
{
    "code": "",
    "msg": "",
    "result": {
        "address": "163 Xianlin Avenue",
        "id": 2,
        "password": "123456789",
        "phone": "13333333333",
        "role": 2,
        "username": "SeeCoder"
    }
}
```


## Update User Information


**url**:`/api/users`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Update the information of the currently logged-in user.</p>



**Example**:


```javascript
{
    "address": "163 Xianlin Avenue",
    "id": 2,
    "password": "123456789",
    "phone": "13333333333",
    "role": 2,
    "username": "SeeCoder"
}
```


**Params**:


| name                 | description             | in   | require | type           | schema |
| -------------------- | ----------------------- | ---- | ------- | -------------- | ------ |
| userVO               | User information object | body | false   | UserVO         | UserVO |
| &emsp;&emsp;address  | User's address          |      | false   | string         |        |
| &emsp;&emsp;id       | User ID                 |      | false   | integer(int32) |        |
| &emsp;&emsp;password | User password           |      | false   | string         |        |
| &emsp;&emsp;phone    | User's phone number     |      | false   | string         |        |
| &emsp;&emsp;role     | User role               |      | false   | integer(int32) |        |
| &emsp;&emsp;username | Username                |      | false   | string         |        |


**Status**:


| code | description  | schema            |
| ---- | ------------ | ----------------- |
| 200  | OK           | ResultVO«boolean» |
| 201  | Created      |                   |
| 401  | Unauthorized |                   |
| 403  | Forbidden    |                   |
| 404  | Not Found    |                   |


**Response Params**:


| name   | description | type    | schema |
| ------ | ----------- | ------- | ------ |
| code   |             | string  |        |
| msg    |             | string  |        |
| result |             | boolean |        |


**Response Example**:
```javascript
{
    "code": "000",
    "msg": null,
    "result": true
}
```


## User Login


**url**:`/api/users/login`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>User login interface.</p>



**Params**:


| Parameter | Description   | In    | Required | Type   | Schema |
| --------- | ------------- | ----- | -------- | ------ | ------ |
| password  | User password | query | true     | string |        |
| phone     | User phone    | query | true     | string |        |

**Status**:


| code | description  | schema           |
| ---- | ------------ | ---------------- |
| 200  | OK           | ResultVO«string» |
| 201  | Created      |                  |
| 401  | Unauthorized |                  |
| 403  | Forbidden    |                  |
| 404  | Not Found    |                  |


**Response Params**:


| name   | description | type   | schema |
| ------ | ----------- | ------ | ------ |
| code   |             | string |        |
| msg    |             | string |        |
| result |             | string |        |


**Response Example**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyIiwiZXhwIjoxNzQyMzAxOTQ2fQ.zQrO1009QUXESy5w2wDqP6F3eEwXt5OkaQ1Z-B9jilc"
}
```


## User Registration


**url**:`/api/users/register`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Register a new user</p>



**Example**:


```javascript
{
  "address": "Nanjing University",
  "id": 6,
  "password": "123456",
  "phone": "1331234567891",
  "role": 1,
  "username": "Test"
}
```


**Params**:


| Parameter Name       | Parameter Description   | Request Type | Is Required | Data Type      | Schema |
| -------------------- | ----------------------- | ------------ | ----------- | -------------- | ------ |
| userVO               | User information object | body         | true        | UserVO         | UserVO |
| &emsp;&emsp;address  | User address            |              | false       | string         |        |
| &emsp;&emsp;id       | User ID                 |              | false       | integer(int32) |        |
| &emsp;&emsp;password | User password           |              | false       | string         |        |
| &emsp;&emsp;phone    | User phone number       |              | false       | string         |        |
| &emsp;&emsp;role     | User role               |              | false       | integer(int32) |        |
| &emsp;&emsp;username | Username                |              | false       | string         |        |


**Status**:


| code | description  | schema            |
| ---- | ------------ | ----------------- |
| 200  | OK           | ResultVO«boolean» |
| 201  | Created      |                   |
| 401  | Unauthorized |                   |
| 403  | Forbidden    |                   |
| 404  | Not Found    |                   |


**Response Params**:


| name   | description | type    | schema |
| ------ | ----------- | ------- | ------ |
| code   |             | string  |        |
| msg    |             | string  |        |
| result |             | boolean |        |


**Response Example**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": true
}
```


# Risk Signal related Interfaces


## Paged Query Risk Signals


**url**:`/api/risk-signals`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Query risk signal list in pages based on time range</p>



**Params**:


| Parameter Name | Parameter Description | Request Type | Is Required | Data Type         | Schema |
| -------------- | --------------------- | ------------ | ----------- | ----------------- | ------ |
| endTime        | End time              | query        | false       | string(date-time) |        |
| page           | Page number           | query        | false       | integer(int32)    |        |
| size           | Page size             | query        | false       | integer(int32)    |        |
| startTime      | Start time            | query        | false       | string(date-time) |        |


**Status**:


| Status Code | Description  | Schema                          |
| ----------- | ------------ | ------------------------------- |
| 200         | OK           | ResultVO«Page«RiskSignalModel»» |
| 401         | Unauthorized |                                 |
| 403         | Forbidden    |                                 |
| 404         | Not Found    |                                 |


**Response Params**:

| Parameter Name                         | Parameter Description     | Type           | Schema                |
| -------------------------------------- | ------------------------- | -------------- | --------------------- |
| code                                   | Status code               | string         |                       |
| msg                                    | Message                   | string         |                       |
| result                                 | Risk signal list          | Page           | Page«RiskSignalModel» |
| &emsp;&emsp;countId                    | Count ID                  | string         |                       |
| &emsp;&emsp;current                    | Current page              | integer(int64) |                       |
| &emsp;&emsp;maxLimit                   | Max limit                 | integer(int64) |                       |
| &emsp;&emsp;optimizeCountSql           | Optimize count SQL        | boolean        |                       |
| &emsp;&emsp;orders                     | Sorting orders            | array          | OrderItem             |
| &emsp;&emsp;&emsp;&emsp;asc            | Ascending                 | boolean        |                       |
| &emsp;&emsp;&emsp;&emsp;column         | Column name               | string         |                       |
| &emsp;&emsp;pages                      | Total pages               | integer(int64) |                       |
| &emsp;&emsp;records                    | Risk signal records       | array          | RiskSignalModel       |
| &emsp;&emsp;&emsp;&emsp;advice         | Advice                    | string         |                       |
| &emsp;&emsp;&emsp;&emsp;analysis       | Analysis result           | string         |                       |
| &emsp;&emsp;&emsp;&emsp;baseCurrency   | Base currency             | integer        |                       |
| &emsp;&emsp;&emsp;&emsp;emp            | EMP value                 | number         |                       |
| &emsp;&emsp;&emsp;&emsp;exchangeRate   | Exchange rate             | number         |                       |
| &emsp;&emsp;&emsp;&emsp;fxReserves     | Foreign exchange reserves | number         |                       |
| &emsp;&emsp;&emsp;&emsp;id             | Signal ID                 | integer        |                       |
| &emsp;&emsp;&emsp;&emsp;interestRate   | Interest rate             | number         |                       |
| &emsp;&emsp;&emsp;&emsp;targetCurrency | Target currency           | integer        |                       |
| &emsp;&emsp;&emsp;&emsp;time           | Time                      | string         |                       |
| &emsp;&emsp;searchCount                | Search count              | boolean        |                       |
| &emsp;&emsp;size                       | Page size                 | integer(int64) |                       |
| &emsp;&emsp;total                      | Total records             | integer(int64) |                       |


**Response Example**:
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
      // Omitted due to length
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


## Create Risk Signal


**url**:`/api/risk-signals`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Create a new risk signal</p>



**Example**:


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


**Params**:


| Parameter Name             | Parameter Description     | Request Type | Is Required | Data Type         | Schema          |
| -------------------------- | ------------------------- | ------------ | ----------- | ----------------- | --------------- |
| signal                     | Risk signal object        | body         | true        | RiskSignalModel   | RiskSignalModel |
| &emsp;&emsp;advice         | Advice                    |              | false       | string            |                 |
| &emsp;&emsp;analysis       | Analysis result           |              | false       | string            |                 |
| &emsp;&emsp;baseCurrency   | Base currency             |              | false       | integer(int32)    |                 |
| &emsp;&emsp;emp            | EMP value                 |              | false       | number(double)    |                 |
| &emsp;&emsp;exchangeRate   | Exchange rate             |              | false       | number(double)    |                 |
| &emsp;&emsp;fxReserves     | Foreign exchange reserves |              | false       | number(double)    |                 |
| &emsp;&emsp;id             | Signal ID                 |              | false       | integer(int64)    |                 |
| &emsp;&emsp;interestRate   | Interest rate             |              | false       | number(double)    |                 |
| &emsp;&emsp;targetCurrency | Target currency           |              | false       | integer(int32)    |                 |
| &emsp;&emsp;time           | Time                      |              | false       | string(date-time) |                 |


**Status**:

| Status Code | Description  | Schema                    |
| ----------- | ------------ | ------------------------- |
| 200         | OK           | ResultVO«RiskSignalModel» |
| 201         | Created      |                           |
| 401         | Unauthorized |                           |
| 403         | Forbidden    |                           |
| 404         | Not Found    |                           |


**Response Params**:

| Parameter Name             | Parameter Description     | Type              | Schema          |
| -------------------------- | ------------------------- | ----------------- | --------------- |
| code                       | Status code               | string            |                 |
| msg                        | Message                   | string            |                 |
| result                     | Risk signal details       | RiskSignalModel   | RiskSignalModel |
| &emsp;&emsp;advice         | Advice                    | string            |                 |
| &emsp;&emsp;analysis       | Analysis result           | string            |                 |
| &emsp;&emsp;baseCurrency   | Base currency             | integer(int32)    |                 |
| &emsp;&emsp;emp            | EMP value                 | number(double)    |                 |
| &emsp;&emsp;exchangeRate   | Exchange rate             | number(double)    |                 |
| &emsp;&emsp;fxReserves     | Foreign exchange reserves |                   | number(double)  |  |
| &emsp;&emsp;id             | Signal ID                 | integer(int64)    |                 |
| &emsp;&emsp;interestRate   | Interest rate             | number(double)    |                 |
| &emsp;&emsp;targetCurrency | Target currency           | integer(int32)    |                 |
| &emsp;&emsp;time           | Time                      | string(date-time) |                 |


**Response Example**:
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


## Advanced Query Risk Signals


**url**:`/api/risk-signals/search`


**method**:`POST`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Advanced query based on multiple conditions.</p>



**Example**:


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


**Params**:

| Parameter Name              | Parameter Description  | Request Type | Is Required | Data Type          | Schema             |
| --------------------------- | ---------------------- | ------------ | ----------- | ------------------ | ------------------ |
| queryDTO                    | Query condition object | body         | true        | RiskSignalQueryDTO | RiskSignalQueryDTO |
| &emsp;&emsp;baseCurrency    | Base currency          |              | false       | integer(int32)     |                    |
| &emsp;&emsp;endTime         | End time               |              | false       | string(date-time)  |                    |
| &emsp;&emsp;keyword         | Keyword                |              | false       | string             |                    |
| &emsp;&emsp;maxEmp          | Max EMP value          |              | false       | number(double)     |                    |
| &emsp;&emsp;minEmp          | Min EMP value          |              | false       | number(double)     |                    |
| &emsp;&emsp;minExchangeRate | Min exchange rate      |              | false       | number(double)     |                    |
| &emsp;&emsp;page            | Page number            |              | false       | integer(int32)     |                    |
| &emsp;&emsp;size            | Page size              |              | false       | integer(int32)     |                    |
| &emsp;&emsp;startTime       | Start time             |              | false       | string(date-time)  |                    |
| &emsp;&emsp;targetCurrency  | Target currency        |              | false       | integer(int32)     |                    |


**Status**:

| Status Code | Description  | Schema                          |
| ----------- | ------------ | ------------------------------- |
| 200         | OK           | ResultVO«Page«RiskSignalModel»» |
| 201         | Created      |                                 |
| 401         | Unauthorized |                                 |
| 403         | Forbidden    |                                 |
| 404         | Not Found    |                                 |


**Response Params**:


| Parameter Name | Parameter Description | Type    | Schema               |
|----------------|-----------------------|---------|----------------------|
| code           | Status code           | string  |                      |
| msg            | Message               | string  |                      |
| result         | Risk signal list      | Page    | Page«RiskSignalModel»|
| &emsp;&emsp;countId      | Count ID             | string  |                      |
| &emsp;&emsp;current      | Current page         | integer(int64) |        |
| &emsp;&emsp;maxLimit     | Max limit            | integer(int64) |        |
| &emsp;&emsp;optimizeCountSql | Optimize count SQL | boolean |        |
| &emsp;&emsp;orders       | Sorting orders       | array   | OrderItem            |
| &emsp;&emsp;&emsp;&emsp;asc      | Ascending            | boolean |        |
| &emsp;&emsp;&emsp;&emsp;column   | Column name          | string  |        |
| &emsp;&emsp;pages        | Total pages          | integer(int64) |        |
| &emsp;&emsp;records      | Risk signal records  | array   | RiskSignalModel      |
| &emsp;&emsp;&emsp;&emsp;advice   | Advice               | string  |        |
| &emsp;&emsp;&emsp;&emsp;analysis | Analysis result      | string  |        |
| &emsp;&emsp;&emsp;&emsp;baseCurrency | Base currency       | integer |        |
| &emsp;&emsp;&emsp;&emsp;emp      | EMP value            | number  |        |
| &emsp;&emsp;&emsp;&emsp;exchangeRate | Exchange rate      | number  |        |
| &emsp;&emsp;&emsp;&emsp;fxReserves | Foreign exchange reserves | number |  |
| &emsp;&emsp;&emsp;&emsp;id        | Signal ID            | integer |        |
| &emsp;&emsp;&emsp;&emsp;interestRate | Interest rate      | number  |        |
| &emsp;&emsp;&emsp;&emsp;targetCurrency | Target currency   | integer |        |
| &emsp;&emsp;&emsp;&emsp;time      | Time                 | string  |        |
| &emsp;&emsp;searchCount  | Search count         | boolean |        |
| &emsp;&emsp;size         | Page size            | integer(int64) |        |
| &emsp;&emsp;total        | Total records        | integer(int64) |        |

**Response Example**:
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


## Update Risk Signal


**url**:`/api/risk-signals/{id}`


**method**:`PUT`


**produces**:`application/json`


**consumes**:`*/*`


**Note**:<p>Update risk signal by ID</p>



**Example**:


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


**Params**:


| Parameter Name | Parameter Description | Request Type | Is Required | Data Type            | Schema         |
|----------------|-----------------------|--------------|-------------|----------------------|----------------|
| id             | Risk signal ID        | path         | true        | integer(int64)       |                |
| signal         | Risk signal object    | body         | true        | RiskSignalModel      | RiskSignalModel|
| &emsp;&emsp;advice     | Advice               |              | false       | string               |                |
| &emsp;&emsp;analysis   | Analysis result      |              | false       | string               |                |
| &emsp;&emsp;baseCurrency | Base currency       |              | false       | integer(int32)       |                |
| &emsp;&emsp;emp        | EMP value            |              | false       | number(double)       |                |
| &emsp;&emsp;exchangeRate | Exchange rate      |              | false       | number(double)       |                |
| &emsp;&emsp;fxReserves | Foreign exchange reserves |          | false       | number(double)       |                |
| &emsp;&emsp;id        | Signal ID            |              | false       | integer(int64)       |                |
| &emsp;&emsp;interestRate | Interest rate      |              | false       | number(double)       |                |
| &emsp;&emsp;targetCurrency | Target currency   |              | false       | integer(int32)       |                |
| &emsp;&emsp;time      | Time                 |              | false       | string(date-time)    |                |

**Status**:


| Status Code | Description      | Schema               |
|-------------|------------------|----------------------|
| 200         | OK               | ResultVO«RiskSignalModel» |
| 201         | Created          |                      |
| 401         | Unauthorized     |                      |
| 403         | Forbidden        |                      |
| 404         | Not Found        |                      |

**Response Params**:

| Parameter Name | Parameter Description | Type          | Schema         |
|----------------|-----------------------|---------------|----------------|
| code           | Status code           | string        |                |
| msg            | Message               | string        |                |
| result         | Updated risk signal   | RiskSignalModel | RiskSignalModel|
| &emsp;&emsp;advice     | Advice               | string        |                |
| &emsp;&emsp;analysis   | Analysis result      | string        |                |
| &emsp;&emsp;baseCurrency | Base currency       | integer(int32) |                |
| &emsp;&emsp;emp        | EMP value            | number(double) |                |
| &emsp;&emsp;exchangeRate | Exchange rate      | number(double) |                |
| &emsp;&emsp;fxReserves | Foreign exchange reserves |          | number(double) |                |
| &emsp;&emsp;id        | Signal ID            | integer(int64) |                |
| &emsp;&emsp;interestRate | Interest rate      | number(double) |                |
| &emsp;&emsp;targetCurrency | Target currency   | integer(int32) |                |
| &emsp;&emsp;time      | Time                 | string(date-time) |        |


**Response Example**:
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


## Delete Risk Signal


**url**:`/api/risk-signals/{id}`


**method**:`DELETE`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Delete risk signal by ID.</p>



**Params**:


| Parameter Name | Parameter Description | Request Type | Is Required | Data Type    | Schema |
|----------------|-----------------------|--------------|-------------|--------------|--------|
| id             | Risk signal ID        | path         | true        | integer(int64) |        |

**Status**:


| code | description  | schema         |
| ---- | ------------ | -------------- |
| 200  | OK           | ResultVO«Void» |
| 204  | No Content   |                |
| 401  | Unauthorized |                |
| 403  | Forbidden    |                |


**Response Params**:


| name | description | type   | schema |
| ---- | ----------- | ------ | ------ |
| code |             | string |        |
| msg  |             | string |        |


**Response Example**:
```javascript
{
  "code": "000",
  "msg": null,
  "result": null
}
```

# Risk Cauculation related Interfaces


## Get Alert Information


**url**:`/api/calculate/alert`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Get Alert Information</p>



**Params**:

null


**Status**:


| code | description | schema |
| -------- | -------- | ----- | 
|200|OK|AlertVO|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**Response Params**:


| name | description | type | schema |
| -------- | -------- | ----- |----- | 
|content||string||
|level||string||
|title||string||
|updateTime||string||


**Response Example**:
```javascript
[
	{
		"content": "",
		"level": "",
		"title": "",
		"updateTime": ""
	}
]
```


## Get Risk Dashboard Information


**url**:`/api/calculate/dashboard`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Get Risk Dashboard data</p>



**Params**:


null


**Status**:


| code | description | schema |
| -------- | -------- | ----- | 
|200|OK|RiskDashboardVO|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**Response Params**:


| name | description | type | schema |
| -------- | -------- | ----- |----- | 
|factorBreakdown||array|RiskFactor|
|&emsp;&emsp;name||string||
|&emsp;&emsp;value||number(double)||
|name||string||
|riskStatus||string||
|score||number(double)|number(double)|
|trend||ScoreTrend|ScoreTrend|
|&emsp;&emsp;direction||string||
|&emsp;&emsp;value||number(double)||
|updateTime||string||


**Response Example**:
```javascript
{
	"factorBreakdown": [
		{
			"name": "",
			"value": 0
		}
	],
	"name": "",
	"riskStatus": "",
	"score": 0,
	"trend": {
		"direction": "",
		"value": 0
	},
	"updateTime": ""
}
```


## Get Exposure Analysis Information


**url**:`/api/calculate/exposure`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Get Exposure Analysis data</p>



**Params**:


null


**Status**:


| code | description | schema |
| -------- | -------- | ----- | 
|200|OK|ExposureMatrixVO|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**Response Params**:


| name | description | type | schema |
| -------- | -------- | ----- |----- | 
|terms||array|Term|
|&emsp;&emsp;amount||number(double)||
|&emsp;&emsp;currency||integer(int32)||
|&emsp;&emsp;range||string||
|&emsp;&emsp;riskLevel||integer(int32)||


**Response Example**:
```javascript
[
	{
		"terms": [
			{
				"amount": 0,
				"currency": 0,
				"range": "",
				"riskLevel": 0
			}
		]
	}
]
```


## Get Risk Map Information


**url**:`/api/calculate/map`


**method**:`GET`


**produces**:`application/x-www-form-urlencoded`


**consumes**:`*/*`


**Note**:<p>Get Risk Map data</p>



**Params**:


null


**Status**:


| code | description | schema |
| -------- | -------- | ----- | 
|200|OK|RiskMapVO|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**Response Params**:


| name | description | type | schema |
| -------- | -------- | ----- |----- | 
|regions||RegionRisk|RegionRisk|
|&emsp;&emsp;currencyPair||integer(int32)||
|&emsp;&emsp;currentRate||number(double)||
|&emsp;&emsp;hotNews||array|NewsItem|
|&emsp;&emsp;&emsp;&emsp;date||string||
|&emsp;&emsp;&emsp;&emsp;title||string||
|&emsp;&emsp;&emsp;&emsp;url||string||
|&emsp;&emsp;rateChange||number(double)||
|&emsp;&emsp;riskLevel||integer(int32)||
|&emsp;&emsp;suggestions||array|string|


**Response Example**:
```javascript
{
	"regions": {
		"additionalProperties1": {
			"currencyPair": 0,
			"currentRate": 0,
			"hotNews": [],
			"rateChange": 0,
			"riskLevel": 0,
			"suggestions": []
		}
	}
}
```