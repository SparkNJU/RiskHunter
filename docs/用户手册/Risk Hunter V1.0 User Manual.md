# Risk Hunter V1.0 User Manual

## 1. Introduction

### 1.1 Purpose
This manual aims to help users comprehensively understand and effectively use the Risk Hunter foreign exchange risk management system. It is suitable for corporate finance personnel, financial institution practitioners, and risk management professionals.

### 1.2 Project Background
Risk Hunter is a cutting-edge foreign exchange risk management platform that integrates traditional financial data with advanced AI technology to provide comprehensive foreign exchange risk monitoring, early warning, and management services. It was developed by the XXXX team at Nanjing University.

### 1.3 Definitions
- **EMP**: Exchange Market Pressure indicator.
- **Risk Signal**: Foreign exchange risk warning indicators generated based on algorithmic analysis.
- **RAG**: Retrieval-Augmented Generation, technology used by the intelligent assistant.

### 1.4 References
- Risk Hunter System Requirements Specification
- Element Plus UI Component Library Documentation (https://element-plus.org/)
- Vue 3 Official Documentation (https://v3.vuejs.org/)

## 2. Software Overview

### 2.1 Objectives
Risk Hunter aims to create a statistics and AI-driven risk management tool that precisely captures exchange rate risk signals by integrating analysis of past trends using multidimensional data models, providing enterprises with more forward-looking and scientific risk management support.

### 2.2 Functions
- **Risk Signal Monitoring**: Generates warning indicators based on LSTM time series modeling and DCC-GARCH dynamic correlation analysis.
- **Intelligent Assistant**: Provides professional risk management advice based on large language models such as DeepSeek-R1 and Qwen-32B.
- **Foreign Exchange Data Analysis**: Provides forex data visualization, supporting various indicator queries and chart displays.
- **News Information**: Integrates authoritative media and policy information in the forex field to provide real-time updates on market dynamics and policy news.
- **User Management**: Supports user registration, login, and personal information management.

### 2.3 Performance
a. Data Accuracy
   - Foreign exchange data supports multi-decimal precision display.
   - Risk prediction accuracy is above 85%.

b. Time Characteristics
   - Page load response time <3 seconds.
   - Data query processing time <5 seconds.
   - Intelligent assistant response time generally within 5-30 seconds depending on network conditions.

c. Flexibility
   - Responsive design, adapts to desktop and mobile devices.
   - Multiple query parameter combinations to meet different analysis needs.

## 3. Operating Environment

### 3.1 Hardware
- **Computer Requirements**: Any device capable of running a modern web browser.
- **Memory Recommendation**: Minimum 2GB RAM.
- **Storage Space**: Browser cache approximately 200MB.
- **Input Devices**: Keyboard, mouse, or touchscreen.
- **Network Requirements**: Stable internet connection, recommended 5Mbps or higher.

### 3.2 Supporting Software
- **Browsers**: Chrome 88+, Firefox 85+, Safari 14+, Edge 88+.
- **Operating Systems**: Windows 10+, macOS 10.14+, iOS 13+, Android 9+.
- **Dependent Services**: Backend API server (ensure access to the online service address: http://riskhunter.xyz).

## 4. Instructions for Use

### 4.1 Installation and Initialization
1. Open your browser and visit the Risk Hunter website URL: http://riskhunter.xyz.
2. First-time users need to register an account (requiring phone number, username, and password).
3. Log in to the system after completing registration.
4. The system will automatically load the homepage content during initialization; no additional setup is required.

### 4.2 Input

#### 4.2.1 Data Background

- **User Authentication Data**: From user input, stored in the system database
- **Risk Signal Query Parameters**: Filtering conditions set by users
- **Foreign Exchange Data Query**: Selection based on preset indicator lists
- **Intelligent Assistant Questions**: Natural language questions input by users

#### 4.2.2 Data Format

- **Time Input**: ISO 8601 format date-time strings (YYYY-MM-DD HH:MM:SS) (via the date-time selector)
- **Currency Selection**: Base currency and quote currency selected via dropdown menus
- **Value Ranges**: Minimum/maximum EMP values, minimum exchange rate values, etc.
- **Keyword Search**: Free text input

#### 4.2.3 Input Examples

##### Investment Advice Query:

- Type (Select): Balanced

##### Risk Signal Query:

- Base Currency (Select): EUR-Euro
- Quote Currency (Select): JPY-Japanese Yen
- Start Time (Select): 2023-01-01 00:00:00
- End Time (Select): 2024-01-01 00:00:00
- Maximum Drawdown Ratio (Enter number, unit %): 10
- Trading Period (Enter number, unit days): 10

##### Foreign Exchange Data Query:

- Data Name (Select): RMB: Real Effective Exchange Rate Index (Medium)
- Start Time (Select): 2022-01-01 00:00:00
- End Time (Select): 2024-03-01 00:00:00

##### Intelligent Assistant Dialogue:

- Question (Can insert risk signal): Please analyze risk signal: Base currency: USD, Quote currency: GBP, Time: 2023-07-01 00:00:00, EMP: 104, Exchange rate: 1.05, Interest rate: 3.2, Foreign exchange reserves: 530.
- Model (Select): DeepSeek-R1 (Default)
- Output Mode (Select): Streaming output
- Knowledge Base (Select): Enabled

### 4.3 Output

#### 4.3.1 Data Background

- **Risk Indicator Display**: Warning lists and investment advice based on current risk indicators
- **Risk Signal List**: Generated based on query conditions, displayed on the web interface
- **Foreign Exchange Data Charts**: Visualization charts generated based on query parameters
- **Intelligent Assistant Replies**: Real-time answers generated by AI models
- **News Content**: News reports from authoritative media

#### 4.3.2 Data Format

- **Risk Signals**: Table format, including fields such as base currency, quote currency, time, EMP, exchange rate, etc.
- **Foreign Exchange Data**: Line charts, distinguishing between actual data and forecast data
- **Intelligent Assistant Replies**: Markdown format text
- **News Content**: HTML format articles

#### 4.3.3 Output Examples

##### Investment Advice:

Balanced Investment Advice

- Suitable for investors with moderate risk tolerance:
  - Recommendation 1: Invest in currency pairs with moderate volatility
  - Recommendation 2: Adopt medium-term trading strategies
  - Recommendation 3: Diversify investment portfolio to spread risk

##### Risk Signal List:

| Base Currency | Quote Currency |        Time         |   EMP    | Exchange Rate | Interest Rate | FX Reserves |
| :-----------: | :------------: | :-----------------: | :------: | :-----------: | :-----------: | :---------: |
|      EUR      |      JPY       | 2023-10-15 00:00:00 | 100.5000 |    1.1200     |    2.5000     |   495.00    |

- Analysis Text: Economy returning to normal, exchange rate stabilizing.
- Recommendation Text: Maintain prudent fiscal and monetary policies.

##### Foreign Exchange Data Line Chart:

![Foreign Exchange Data Line Chart](https://p.inari.site/guest/25-03/18/67d8fbe246312.png)

##### Intelligent Assistant Reply:

- Based on the provided parameters and risk model analysis, USD/GBP in July 2023 shows the following risk signals:
  1. **Exchange Rate Overshooting Risk**: The current exchange rate of 1.05 is approaching the shadow exchange rate critical value[1]. According to the international reserves dynamic model, when foreign exchange reserves (530 billion) do not match domestic credit growth, there is a risk of short-term exchange rate overshooting. It is recommended to monitor whether the reserve consumption rate exceeds the safety threshold of 2.3% per month[1].
  2. **Interest Rate Inversion Pressure**: The USD interest rate of 3.2% is significantly higher than the GBP rate for the same period. According to the forward exchange rate pricing formula, the theoretical 1-year forward exchange rate should be the spot rate × (1+3.2%)/(1+GBP interest rate). If the actual forward quote deviates from this value by more than 1.5%, there is a risk of arbitrage-driven exchange rate volatility[4].
  3. **EMP Warning Signal**: The Economic Policy Uncertainty Index (EMP) reaches 104, exceeding the threshold of 100, suggesting activation of a dynamic hedging mechanism. According to the decision tree model, a delta-neutral hedging portfolio should be established, with automatic position adjustment triggered when volatility breaks through 2%[2].
  4. **Option Protection Cost**: At the current volatility level, the theoretical premium for purchasing a 3-month at-the-money put option should be 2.1%-2.8% of the spot exchange rate. If the market quote exceeds this range, a collar strategy is recommended to reduce hedging costs[4].
- Risk Response Recommendations: Immediately establish 40%-60% forward exposure coverage, with the remaining portion using 1.5% out-of-the-money put option combinations. When the EMP index remains above 110 for 3 consecutive days or experiences a single-day volatility exceeding 1.8%, the emergency hedging plan should be activated[2].

#### 4.4 Errors and Recovery

- **Login Failure**: Displays "Phone number or password incorrect", please check your input and try again.
- **Login Verification Code Error**: Re-enter the correct verification code, or click the verification code area to refresh and enter correctly.
- **Not Logged In/Please Log In First**: Complete login on the login page before attempting to access.
- **Data Loading Failure**: Displays "Data loading failed", click the retry button or refresh the page.
- **Network Error**: Ensure network connection is normal, click the relevant function button to retry.

#### 4.5 Help Queries
- Each page provides intuitive interface operation guidance
- Contact information is displayed at the bottom of the main page; technical support can be contacted via email

## 5. Operation Instructions

### 5.1 Operation Table
| Operation Scenario              | Purpose                                                      |
| ------------------------------- | ------------------------------------------------------------ |
| Homepage Browsing               | Understanding system overview and main functions             |
| User Registration/Login         | Gaining system access permissions                            |
| Risk Signal Query               | Obtaining risk signal alerts and investment advice under specific conditions |
| Foreign Exchange Data Viewing   | Analyzing historical and forecast data trends                |
| Intelligent Assistant Dialogue  | Getting answers and advice for professional questions        |
| News Browsing                   | Understanding latest market dynamics and policy information  |
| Personal Information Management | Updating personal profile and password                       |

### 5.2 Operation Steps

#### 5.2.1 Operation Control

1. **Account Registration**:
   - Fill in fields such as username, identity, phone number, password, and confirm password
   - Click to send verification code to receive SMS verification code
   - Enter the SMS verification code received on your phone and click login

2. **System Login**:
   - Visit the system URL, enter phone number and password
   - Enter verification code and click login
   - After successful login, automatically redirected to personal information page
   
3. **Risk Signal Query**:
   - Click "Risk Signal" in the navigation bar or on the homepage to enter the query page
   - Set filtering conditions (currency, time range, trading requirements)
   - Click the "Query" button to get results
   
4. **Using Intelligent Assistant**:
   - Click "Intelligent Assistant" in the navigation bar or on the homepage to enter the dialogue page
   - Create a new session or select an existing session
   - Enter a question and send, wait for AI response
   - Can insert risk signals, choose streaming/standard output, select different large language models, enable/disable knowledge base functionality
   
5. **Foreign Exchange Data Viewing**:
   - Click "Foreign Exchange Data" in the navigation bar or on the homepage to enter the query page
   - Select data name from the dropdown menu
   - Set optional advanced filtering conditions
   - Click "Query" to get visualization charts
   
6. **News Browsing**:
   - Click "News Information" in the navigation bar to enter the news list
   - Click on news titles of interest to view details
   
7. **Personal Information Management**:
   - Click "Personal Information" in the user menu in the top right corner
   - View and modify personal information
   - Change password; after successful password modification, re-login is required

#### 5.2.2 Operation Information

a. **Operation Purpose**: Providing full-process support for foreign exchange risk management

b. **Operation Requirements**: Login required to access core functions

c. **Startup Method**: Direct access to system URL via browser

d. **Expected Running Time**: 
   - Page loading: 1-3 seconds
   - Data query: 2-5 seconds
   - AI assistant response: 5-30 seconds (depending on question complexity)

e. **Operation Format Description**: 
   - Form inputs must follow the format requirements in prompts
   - Date selector for visual selection of date and time
   - Dropdown menus for selecting predefined options

f. **Other Matters**:
   - System supports responsive design, can be used on mobile devices

#### 5.2.3 Input/Output Files

- The system is a web application, data is stored on backend servers
- User data is encrypted during transmission and storage via HTTPS

#### 5.2.4 Startup or Recovery Process

- The system automatically saves user session status
- After unexpected browser closure, reopening and logging in can restore previous work
- Intelligent assistant session content is stored long-term on the server, historical conversations can be viewed at any time

## 6. Non-standard Processes

- **Server Connection Failure**: 
  1. Check network connection
  2. Wait 1-2 minutes and refresh the page
  3. If failure persists, contact system administrator

- **Data Inconsistency**:
  1. Refresh page to reload data
  2. Log out and log back in

- **Browser Compatibility Issues**:
  1. Try clearing browser cache
  2. Update to recommended browser version

- **Login Verification Code Not Displaying**:
  1. Click verification code image area to refresh
  2. If problem persists, try using a different browser

## 7. Operation Commands Overview

| Command/Operation     | Function                                     | Location                                               |
| --------------------- | -------------------------------------------- | ------------------------------------------------------ |
| Login                 | Enter system                                 | Login page                                             |
| Register              | Create new account                           | Registration page                                      |
| Logout                | Safely exit system                           | Navigation bar                                         |
| Home                  | Go to homepage                               | Navigation bar                                         |
| Risk Signal           | Enter risk signal page                       | Homepage/Navigation bar                                |
| Intelligent Assistant | Enter intelligent assistant page             | Homepage/Navigation bar                                |
| Foreign Exchange Data | Enter foreign exchange data page             | Homepage/Navigation bar                                |
| News Display          | Enter news display page                      | Homepage/Navigation bar                                |
| Risk Signal Query     | Filter risk signals by conditions            | Risk signal page filter bar                            |
| Risk Signal Reset     | Reset risk signal filtering conditions       | Risk signal page filter bar                            |
| Analysis Details      | Get analysis for a specific risk signal      | Risk signal page list                                  |
| Create New Session    | Start new intelligent assistant conversation | Intelligent assistant page                             |
| Select Session        | View historical conversations                | Intelligent assistant sidebar                          |
| Sidebar Switch        | Open/close intelligent assistant sidebar     | Intelligent assistant sidebar/top left of message area |
| Send Message          | Ask intelligent assistant a question         | Intelligent assistant input box                        |
| Abort Generation      | Stop intelligent assistant answer generation | Intelligent assistant input bar                        |
| Insert Risk Signal    | Quote risk signal in conversation            | Intelligent assistant input bar                        |
| Output Mode Selection | Select streaming/standard output             | Intelligent assistant input bar                        |
| Model Selection       | Select large language model                  | Intelligent assistant input bar                        |
| Knowledge Base        | Enable/disable knowledge base function       | Intelligent assistant input bar                        |
| Data Query            | Query foreign exchange data                  | Foreign exchange data page                             |
| News Title            | View news content                            | News display page                                      |
| View Details          | View news content                            | News display page                                      |

## 8. Program Files and Data Files Overview

1. All program files are automatically maintained by the system; users do not need to operate them manually.
2. User data is only saved in secure directories on the server, accessible only to the user.

## 9. User Operation Examples

### Example 1: Querying Risk Signals for a Specific Currency Pair

1. Log in to the system
2. Click "Risk Signal" in the navigation bar
3. In the filtering conditions, select:
   - Base Currency: USD-US Dollar
   - Quote Currency: JPY-Japanese Yen
   - Start Time: 2024-01-01
   - End Time: 2024-03-01
4. In trading requirements, enter numbers:
   - Maximum Drawdown Ratio: 10
   - Trading Period: 10
5. Click the "Query" button
6. View the risk signal list
7. Click to expand a row to view detailed analysis and recommendations

### Example 2: Using the Intelligent Assistant to Analyze Risk

1. Click "Intelligent Assistant" in the navigation bar
2. Click the "New Conversation" button
3. Enter the question: "Please analyze recent USD/CNY trends and potential risks"
4. Turn on the "Knowledge Base" switch
5. Click "Send" or press Enter
6. View the analysis reply generated by the intelligent assistant
7. Click the "+" button, select a risk signal from the dropdown menu to insert into the conversation
8. Continue asking: "Based on this signal, what hedging strategy do you recommend?"
9. View the intelligent assistant's strategy recommendations

### Example 3: Viewing Foreign Exchange Data Charts

1. Click "Foreign Exchange Data" in the navigation bar
2. Select "RMB: Real Effective Exchange Rate Index (Medium)" from the data name dropdown menu
3. Expand "Advanced Options" for time range filtering (optional)
4. Click the "Query" button
5. View the generated line chart, observe trends in actual data and forecast data

### Example 4: Updating Personal Information and Password

1. Click "Personal Information" in the navigation bar in the top right corner
2. In the personal information card, click the "Modify Personal Information" button
3. Enter new username and address, click "Update"
4. Click "Change Password" to switch to the password modification interface
5. Enter new password and confirm, click "Update"
6. Log back into the system after confirmation prompt