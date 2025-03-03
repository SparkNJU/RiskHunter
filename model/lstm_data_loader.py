import torch
import numpy as np
import pandas as pd
###将数据标准化的方法
def min_max(data):
    data=(data-min(data))/(max(data)-min(data))
    return data
def normalize(data):
    return (data-np.mean(data))/(max(data)-min(data))
def z_score(data):
    average=np.mean(data)
    sigma=np.sqrt(np.dot(data-average,data-average))
    return (data-average)/sigma
###
def process(data,axis):
    return np.apply_along_axis(min_max,axis=axis,arr=data)#调用前面的某种方法处理数据
def load_data(path):
    data=pd.read_excel(path)
    pro_data=process(data.iloc[0:260,1:12].to_numpy(),axis=0)
    train_X=pro_data[0:240]
    train_y=pro_data[0:240,-1]#读取外汇储备数据
    test_X=[pro_data[150:240]]#分割一部分用于测试
    test_y=pro_data[240:260,-1]
    print(train_X)
    print(train_y)
    batch_x=[]
    batch_y=[]
    for i in range(0,15):
        batch_x.append(train_X[10*i:10*i+80])#将数据进行切割形成一个batch训练
        batch_y.append(train_y[10*i+80:10*i+100])
    batch_x=torch.from_numpy(np.array(batch_x)).float()
    batch_y=torch.from_numpy(np.array(batch_y)).float()
    test_X=torch.from_numpy(np.array(test_X)).float()
    test_y=torch.from_numpy(np.array(test_y)).float()
    print(batch_x,batch_y)
    return batch_x,batch_y,test_X,test_y
#load_data(r"C:\Users\L\OneDrive - 南京大学\Desktop\py_coding\LSTM_Model\data_combination.xls")