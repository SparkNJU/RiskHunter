import numpy as np
import matplotlib.pyplot as plt
import torch
import torch.nn as nn
def train_and_eval(model,num_epochs,train_x,train_y,test_x,test_y,lr):
    criterion=nn.MSELoss()
    optimizer=torch.optim.Adam(model.parameters(),lr=lr)
    for epoch in range(num_epochs):
        optimizer.zero_grad()
        outputs=model(train_x)
        loss=criterion(outputs,train_y)
        loss.backward()
        optimizer.step()
        print(f"Epoch [{epoch+1}/{num_epochs}], Loss: {loss.item()}")
        #print(outputs[-1],train_y[-1],test_y)
    model.eval()
    with torch.no_grad():
        test_out=model(test_x)
        print(len(train_x),test_x,train_x[-1:])
        test_x=test_x.numpy()[0]
        x=np.array(range(1,len(test_x)+len(test_out[0])+1))
    #print(x)
    #print(test_x[:,-1:])
    #print(test_out,test_y)
    print(f"loss:{criterion(test_out[0],test_y)}")
    plt.plot(x[0:len(test_x)],test_x[:,-1:])
    plt.plot(x[len(test_x):len(test_x)+len(test_out[0])],test_y.numpy())
    plt.plot(x[len(test_x):len(test_x)+len(test_out[0])],test_out.numpy()[-1])
    plt.show()