import pandas as pd
from Lstm_model import Lstm
import torch
import torch.nn as nn
import numpy as np
def min_max(data):
    if max(data)==min(data):
        return np.mean(data)
    data=(data-min(data))/(max(data)-min(data))
    return data
def process(data,axis):
    return np.apply_along_axis(min_max,axis=axis,arr=data)#调用前面的某种方法处理数据
def load_data(path):
    data=pd.read_excel(path)
    training_data=data.iloc[65:9130,1:]
    other=training_data.to_numpy().T
    min_data=[]
    max_data=[]
    for i in range(len(other)):
        min_data.append(min(other[i]))
        max_data.append(max(other[i]))
    training_data=process(training_data.to_numpy(),axis=0)
    train_X=training_data
    batch_x=[]
    batch_y=[]
    for i in range(0,78):
        batch_x.append(train_X[100*i:100*i+1000])#将数据进行切割形成一个batch训练
        batch_y.append(train_X[100*i+1000:100*i+1365].flatten())
    batch_x=torch.from_numpy(np.array(batch_x)).float()
    batch_y=torch.from_numpy(np.array(batch_y)).float()
    return batch_x,batch_y,min_data,max_data
def save_model(train=True,model=None,iteration=10000,learning_rate=0.001):
    batch_x,batch_y,min_data,max_data=load_data('day_data.xlsx')
    if train==True:
        model=Lstm(11,128,365*11,1)
        criterion=nn.MSELoss()
        optimizer=torch.optim.Adam(model.parameters(),lr=learning_rate)
        for epoch in range(iteration):
            optimizer.zero_grad()
            outputs=model(batch_x)
            loss=criterion(outputs,batch_y)
            loss.backward()
            optimizer.step()
            print(f"Epoch [{epoch+1}/{iteration}], Loss: {loss.item()}")
            if loss.item()<=1e-8:
                break
        torch.save(model,'day_lstm.pth')
            #print(outputs[-1],train_y[-1],test_y)
    model.eval()
    with torch.no_grad():
        test_out=model(batch_x[-1:])
        test_out=test_out.numpy()[0].reshape(365,int(len(test_out.numpy()[0])/365))
        for i in range(len(min_data)):
            test_out[i]=test_out[i]*(max_data[i]-min_data[i])+min_data[i]
        print(test_out.shape)
        out=pd.DataFrame(index=pd.period_range(start='2024-12-31',periods=len(test_out)).strftime("%Y-%m-%d"),
                         columns=['美国利率','中国利率','汇率',
                            '香港','欧洲','日本','英国','瑞士','澳大利亚','美国','中国'],data=test_out)
        out.index.name='time'
        out.to_excel('day_predict.xlsx')
if __name__=='__main__':
    save_model(True)