import torch.nn as nn
import torch
from lstm_data_loader import load_data
from Lstm_model import Lstm
from lstm_train import train_and_eval
model=Lstm(11,32,20,1)
batch_x,batch_y,test_x,test_y=load_data("data_combination.xls")
train_and_eval(model,5000,batch_x,batch_y,test_x,test_y,0.0001)