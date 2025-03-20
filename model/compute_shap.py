from main import count_Emp
from lstm_data_loader import load_data
import pandas as pd
import numpy as np
import torch
import shap
def shap_count():
    model=torch.load('lstm.pth',weights_only=False)
    batch_x,batch_y,testing_data,min_data,max_data=load_data("data_combination.xls")
    explainer=shap.GradientExplainer(model,batch_x)
    shap_values=explainer.shap_values(batch_x[-1:,:])
    print(shap_values.shape)
    feature_shap = np.abs(shap_values[0][-1]).reshape(26,26,25).mean(axis=2)  # 形状 (1, input_features)
    print(feature_shap.shape)
    feature_shap=feature_shap[:-13,-5:]
    w=np.array([0.1,0.1,0.1,0.1,0.3])
    feature_shap=np.dot(feature_shap,w)
    feature_shap=feature_shap/sum(feature_shap)
    feature_names = ["中国国内信贷", "中国工业增加值当月同比",'中国FDI','美国M2',
                     '中国M2','中国M2乘数','中国财政赤字',
                     '中国CPI当月同比','人民币实际有效汇率指数','中国国外净资产',
                     'TBD利差','WTI原油','美元指数']
    data=pd.DataFrame(index=feature_names, data=feature_shap)
    print(data)
shap_count()