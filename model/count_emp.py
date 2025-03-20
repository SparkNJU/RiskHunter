import pandas as pd
import numpy as np
from lstm_data_loader import z_score,min_max
def count_emp(out=True):
    area_name=['香港','欧洲','日本','英国','瑞士','澳大利亚','美国','中国']
    weight=[0.3,0.2,0.2,0.3]
    l=pd.read_excel('预测利率.xlsx').set_index('time').loc['2025-01-01':'2025-12-30']
    w=pd.read_excel('预测外汇_日.xlsx').set_index('time').loc['2025-01-01':'2025-12-30']
    h=pd.read_excel('预测汇率.xlsx').set_index('time').loc['2025-01-01':'2025-12-30']
    news=pd.read_excel('day_predict.xlsx').set_index('time').loc['2025-01-01':'2025-12-30','香港':'中国']
    diff_l=l.diff()[1:]
    diff_w=w.diff()[1:]
    diff_h=h.diff()[1:]
    diff_news=news.diff()[1:]
    diff_l=np.apply_along_axis(z_score,axis=0,arr=diff_l.to_numpy())
    diff_w=np.apply_along_axis(z_score,axis=0,arr=diff_w.to_numpy())
    diff_h=np.apply_along_axis(z_score,axis=0,arr=diff_h.to_numpy())
    diff_news=np.apply_along_axis(z_score,axis=0,arr=diff_news.to_numpy())
    emp=diff_h*weight[0]+diff_l*weight[1]+diff_w*weight[2]+diff_news*weight[3]
    emp=np.apply_along_axis(np.abs,axis=0,arr=emp)
    bemp=emp>0.1
    if out==True:
        emp=pd.DataFrame(emp,columns=area_name,index=pd.period_range(start='2025-01-01',periods=len(emp),freq='D').strftime("%Y-%m-%d"))
        emp.to_excel('emp_value.xlsx')
        bemp=pd.DataFrame(bemp,columns=area_name,index=pd.period_range(start='2025-01-01',periods=len(emp),freq='D').strftime("%Y-%m-%d"))
        bemp.to_excel('bemp_value.xlsx')
        emp=emp.to_numpy()
        bemp=bemp.to_numpy()
    return emp
if __name__=='__main__':
    count_emp()
