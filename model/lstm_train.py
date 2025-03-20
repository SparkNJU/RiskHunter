import numpy as np
import matplotlib.pyplot as plt
import torch
import pandas as pd
import torch.nn as nn
def train_and_eval(model,num_epochs,train_x,train_y,testing_data,min_data,max_data,lr,length):
    criterion=nn.MSELoss()
    optimizer=torch.optim.Adam(model.parameters(),lr=lr)
    for epoch in range(num_epochs):
        optimizer.zero_grad()
        outputs=model(train_x)
        loss=criterion(outputs,train_y)
        loss.backward()
        optimizer.step()
        print(f"Epoch [{epoch+1}/{num_epochs}], Loss: {loss.item()}")
        if loss.item()<=1e-8:
            break
        #print(outputs[-1],train_y[-1],test_y)
    model.eval()
    with torch.no_grad():
        training=list(np.array((train_x[-1])))
        test_out=model(torch.from_numpy(np.array(list([training]))))
        total_predict=list(test_out.numpy()[0].reshape(length,int(len(test_out.numpy()[0])/length)))
        for i in range(1,3):
            training=training[length:]+list(test_out.numpy()[0].reshape(length,int(len(test_out.numpy()[0])/length)))
            test_out=model(torch.from_numpy(np.array([training])))
            total_predict+=list(test_out.numpy()[0].reshape(length,int(len(test_out.numpy()[0])/length)))
        total_predict=np.array(total_predict)[:,-13:]
    for i in range(-13,0):
        total_predict[:,i]=total_predict[:,i]*(max_data[i]-min_data[i])+min_data[i]
    result=pd.DataFrame(total_predict,
                 columns=['香港','欧洲','日本','英国','瑞士','澳大利亚','美国','中国',
                          '美国:外汇(测)','美国:国债长期平均实际利率(测)',
                          '中央银行政策利率:中国','中国外汇储备(测)',
                          '即期汇率:美元兑人民币(测)'],index=pd.date_range(start='2022-07',periods=75,freq='ME'))
    result.to_excel('predict.xlsx')
    torch.save(model,'lstm.pth')
    '''testing_data=testing_data[:,-5:]
    x=np.array(range(1,len(total_predict)+1))
    x1=np.array(range(1,len(testing_data[:,0])+1))
    plt.subplot(2,3,1)
    plt.plot(x,total_predict[:,0])
    plt.plot(x1,testing_data[:,0])
    plt.plot()
    plt.subplot(2,3,2)
    plt.plot(x,total_predict[:,1])
    plt.plot(x1,testing_data[:,1])
    plt.subplot(2,3,3)
    plt.plot(x,total_predict[:,2])
    plt.plot(x1,testing_data[:,2])
    plt.subplot(2,3,4)
    plt.plot(x,total_predict[:,3])
    plt.plot(x1,testing_data[:,3])
    plt.subplot(2,3,5)
    plt.plot(x,total_predict[:,4])
    plt.plot(x1,testing_data[:,4])
    plt.show()'''
    