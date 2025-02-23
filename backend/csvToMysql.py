import pandas as pd
import mysql.connector
from sqlalchemy import create_engine
from datetime import datetime

# 1. 读取CSV文件
df = pd.read_csv(r'C:\Users\HP\Desktop\大二下学期\花旗杯\RiskHunter\backend\tempData.csv')
print(df.head())
# 2. 配置MySQL连接信息
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': 'cz050128',
    'database': 'riskhunter'
}

# 使用SQLAlchemy方法导入数据
def import_to_mysql():
    try:
        # 创建数据库连接
        engine = create_engine(
            'mysql+mysqlconnector://root:cz050128@localhost/riskhunter'
        )
        
        # 将时间字符串转换为datetime格式
        df['time'] = pd.to_datetime(df['time'])
        print(df.head())
        # 将DataFrame写入MySQL
        rows_affected = df.to_sql('risk_signal', 
                 engine, 
                 if_exists='replace',  
                 index=False,
                 dtype={
                     'time': 'DATETIME',
                     'emp': 'DOUBLE',
                     'exchangeRate': 'DOUBLE',
                     'interestRate': 'DOUBLE',
                     'fxReserves': 'DOUBLE',
                     'analysis': 'TEXT',
                     'advice': 'TEXT'
                 })
        print(f"成功写入{rows_affected}行数据到MySQL数据库")
        
    except Exception as e:
        print(f"导入过程中出现错误: {e}")

if __name__ == "__main__":
    import_to_mysql()
    print("导入完成!")
    # 验证数据导入
    query = "SELECT * FROM risk_signal LIMIT 5"
    df_verify = pd.read_sql(query, engine)
    print(df_verify)