import pandas as pd
import numpy as np
### start time:2020-10
def multi_pre():
    area_set=['香港','欧洲','日本','英国','瑞士','澳大利亚','美国','中国']
    data=pd.read_excel('day_predict.xlsx')
    huilv_with_china={'中国':1.0,'欧洲':-0.2,'香港':0.1,'日本':-0.1,'澳大利亚':0.4,'瑞士':0.5,'英国':-0.25}
    lilv_with_china={'香港':0.19,'日本':-0.01,'欧洲':0.08,'澳大利亚':-0.16,'瑞士':-0.15,'英国':-0.14}
    lilv_with_usa={'香港':0.4,'日本':-0.15,'欧洲':0.25,'澳大利亚':0.1,'瑞士':-0.1,'英国':-0.01}
    waihui_with_china={'香港':-0.24,'日本':-0.2,'欧洲':0.06,'澳大利亚':-0.95,'瑞士':0.21,'英国':0.3}
    waihui_with_usa={'香港':0.3,'日本':-0.5,'欧洲':0.01,'澳大利亚':-0.1,'瑞士':-0.23,'英国':-0.3}
    huilv_start={'香港':1/7.849014,'澳大利亚':1.458226,'日本':136.709,'瑞士':0.960009,'欧洲':1/0.982536,'英国':1/0.834303,'中国':6.7343}
    lilv_start={'香港':1.6429,'日本':-0.1,'中国':3.7,'澳大利亚':3.85,'欧洲':0,'瑞士':-0.5227,'美国':1.25,'英国':1.1375}
    waihui_start={'香港':247455.0,'澳大利亚':34634.10,'欧洲':54855.00,'日本':1202553.0,'瑞士':93084.32,'英国':24759.33,'美国':26343.99,'中国':31040.71}
    lilv_set=[[] for i in range(8)]
    waihui_set=[[] for i in range(8)]
    huilv_set=[[] for i in range(8)]
    lilv_china=data.loc[:,'中国利率'].to_numpy()
    lilv_usa=data.loc[:,'美国利率'].to_numpy()
    #waihui_usa=data.loc[:,'美国:外汇(测)'].to_numpy()
    #waihui_china=data.loc[:,'中国外汇储备(测)'].to_numpy()
    huilv_china_usa=data.loc[:,'汇率'].to_numpy()
    lilv_china_var=[(lilv_china[0]-lilv_start['中国'])/lilv_start['中国']]
    lilv_usa_var=[(lilv_usa[0]-lilv_start['美国'])/lilv_start['美国']]
    #waihui_usa_var=[(waihui_usa[0]-waihui_start['美国'])/waihui_start['美国']]
    #waihui_china_var=[(waihui_china[0]-waihui_start['中国'])/waihui_start['中国']]
    huilv_china_usa_var=[(huilv_china_usa[0]-huilv_start['中国'])/huilv_start['中国']]
    def count_variation(data,collection):
        for i in range(len(data)-1):
            temp=data[i]
            if -1<data[i]<1:
                temp=1
            collection.append((data[i+1]-temp)/np.abs(temp))
        return collection
    def count_value():
        for i,names in enumerate(area_set):
            lilv_set[i].append(lilv_start[names])
            waihui_set[i].append(waihui_start[names])
            if names=='欧洲' or names=='香港' or names=='英国':
                huilv_set[i].append(1/huilv_start[names])
            elif names!='美国':
                huilv_set[i].append(huilv_start[names])
            else:
                huilv_set[i].append(np.float64(1.0))
            for j in range(len(lilv_china_var)):
                if names=='中国':
                    lilv_set[i].append(lilv_china[j])
    #                waihui_set[i].append(waihui_china[j]) 
                    huilv_set[i].append(huilv_china_usa[j])
                elif names=='美国':
                    lilv_set[i].append(lilv_usa[j])
    #                waihui_set[i].append(waihui_usa[i])
                    huilv_set[i].append(np.float64(1.0))
                else:
                    lilv_set[i].append(lilv_set[i][j]+(lilv_with_china[names]*lilv_china_var[j]+lilv_with_usa[names]*lilv_usa_var[j])/2)
    #                waihui_set[i].append(waihui_set[i][j]+np.abs(waihui_set[i][j])*(waihui_with_china[names]*waihui_china_var[j]+waihui_with_usa[names]*waihui_usa_var[j])/2)
                    if names=='欧洲' or names=='香港' or names=='英国':
                        huilv_set[i].append(1/(1/huilv_set[i][j]+1/np.abs(huilv_set[i][j])*huilv_with_china[names]*huilv_china_usa_var[j]))
                    else:
                        huilv_set[i].append(huilv_set[i][j]+np.abs(huilv_set[i][j])*huilv_with_china[names]*huilv_china_usa_var[j])
    lilv_china_var=count_variation(lilv_china,lilv_china_var)
    lilv_usa_var=count_variation(lilv_usa,lilv_usa_var)
    #waihui_china_var=count_variation(waihui_china,waihui_china_var)
    #waihui_usa_var=count_variation(waihui_usa,waihui_usa_var)
    huilv_china_usa_var=count_variation(huilv_china_usa,huilv_china_usa_var)
    count_value()
    lilv=pd.DataFrame(np.array(lilv_set,dtype=float).T,columns=area_set,
                index=pd.period_range(start='2024-12-30',periods=len(lilv_set[0]),freq='D').strftime("%Y-%m-%d"))
    lilv.index.name='time'
    lilv.to_excel('预测利率.xlsx')
    #waihui=pd.DataFrame(np.array(waihui_set,dtype=float).T,columns=area_set,
    #            index=pd.period_range(start='2022-07',periods=len(lilv_set[0]),freq='M').strftime("%Y-%m"))
    #waihui.index.name='time'
    #waihui.to_excel('预测外汇.xlsx')
    huilv=pd.DataFrame(np.array(huilv_set,dtype=float).T,columns=area_set,
                index=pd.period_range(start='2024-12-30',periods=len(lilv_set[0]),freq='D').strftime("%Y-%m-%d"))
    huilv.index.name='time'
    huilv.to_excel('预测汇率.xlsx')
if __name__=='__main__':
    multi_pre()