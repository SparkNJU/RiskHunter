import torch.nn as nn
import torch
from lstm_data_loader import load_data
from Lstm_model import Lstm
from lstm_train import train_and_eval
length=25
choose_feature=list(range(0,26))
def save_model(iteration=10000,learning_rate=0.001):
    model=Lstm(26,128,25*len(choose_feature),1)
    batch_x,batch_y,testing_data,min_data,max_data=load_data("data_combination.xls")
    train_and_eval(model,iteration,batch_x,batch_y,testing_data,min_data,max_data,learning_rate,length)
    return batch_x,min_data,max_data
if __name__=='__main__':
    save_model()