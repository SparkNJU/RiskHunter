import torch
import numpy as np
import pandas as pd
###将数据标准化的方法
def min_max(data):
    if max(data)==min(data):
        return np.mean(data)
    data=(data-min(data))/(max(data)-min(data))
    return data
def normalize(data):
    return (data-np.mean(data))/(max(data)-min(data))
def z_score(data):
    average=np.mean(data)
    sigma=np.sqrt(np.dot(data-average,data-average))
    if sigma==0:
        return 0
    return (data-average)/sigma
###
def process(data,axis):
    return np.apply_along_axis(min_max,axis=axis,arr=data)#调用前面的某种方法处理数据
def load_data(path):
    data=pd.read_excel(path)
    training_data=data.iloc[0:270,1:]
    other=training_data.to_numpy().T
    min_data=[]
    max_data=[]
    for i in range(len(other)):
        min_data.append(min(other[i]))
        max_data.append(max(other[i]))
    training_data=process(training_data.to_numpy(),axis=0)
    testing_data=data.iloc[270:,1:].to_numpy()
    train_X=training_data
    #train_y=training_data[:,-4:]#读取外汇储备数据
    batch_x=[]
    batch_y=[]
    for i in range(0,11):
        batch_x.append(train_X[10*i:10*i+145])#将数据进行切割形成一个batch训练
        batch_y.append(train_X[10*i+145:10*i+170].flatten())
    batch_x=torch.from_numpy(np.array(batch_x)).float()
    batch_y=torch.from_numpy(np.array(batch_y)).float()
    #print(min_data)
    #print(max_data)
    return batch_x,batch_y,testing_data,min_data[-13:],max_data[-13:]
#load_data("data_combination.xls")