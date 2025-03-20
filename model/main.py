
from count_emp import count_emp
from multi_predict import multi_pre
import torch
from Lstm_model import Lstm
import pandas as pd
import numpy as np
from train_main import length,choose_feature
from train_main import save_model
from lstm_data_loader import load_data
def predict(model,data,min_data,max_data,pre_length,processed=True):
    if processed==False:
        data=(data-min_data)/(max_data-min_data)
    model.eval()
    with torch.no_grad():
        iter_data=list(np.array(data))
        test_out=model(torch.from_numpy(np.array(list([iter_data]))))
        total_predict=list(test_out.numpy()[0].reshape(length,int(len(test_out.numpy()[0])/length)))
        for i in range(1,int(pre_length/25)):
            iter_data=iter_data[length:]+list(test_out.numpy()[0].reshape(length,int(len(test_out.numpy()[0])/length)))
            test_out=model(torch.from_numpy(np.array([iter_data])))
            total_predict+=list(test_out.numpy()[0].reshape(length,int(len(test_out.numpy()[0])/length)))
        total_predict=np.array(total_predict)[:,-13:]
    for i in range(-13,0):
        total_predict[:,i]=total_predict[:,i]*(max_data[i]-min_data[i])+min_data[i]
    result=pd.DataFrame(total_predict,
                 columns=['香港','欧洲','日本','英国','瑞士','澳大利亚','美国','中国',
                          '美国:外汇(测)','美国:国债长期平均实际利率(测)',
                          '中央银行政策利率:中国','中国外汇储备(测)',
                          '即期汇率:美元兑人民币(测)'],index=pd.period_range(start='2022-08',periods=pre_length,freq='M').strftime('%Y-%m'))
    result.index.name='time'
    result.to_excel('predict.xlsx')
from day_lstm import save_model
def count_Emp(out=True):
    #batch_x,batch_y,testing_data,min_data,max_data=load_data("data_combination.xls")
    model=torch.load('day_lstm.pth',weights_only=False)
    #predict(model,batch_x[-1],min_data,max_data,175)
    save_model(train=False,model=model)
    multi_pre()
    return count_emp(out)#,batch_x[-1]
if __name__=='__main__':
    count_Emp(out=True)