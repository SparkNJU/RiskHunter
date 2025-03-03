import torch.nn as nn
import torch
class SelfAttention(nn.Module):
    def __init__(self, hidden_size):
        super().__init__()
        self.attention = nn.Sequential(
            nn.Linear(hidden_size, 16),
            nn.Tanh(),
            nn.Linear(16, 1)
        )
    def forward(self, lstm_output):
        attn_scores = self.attention(lstm_output)
        attn_weights = torch.softmax(attn_scores, dim=1)
        return attn_weights.squeeze(-1)
class Lstm(nn.Module):
    def __init__(self, input_size,hidden_size,output_size,num_layer):
        super().__init__()
        self.lstm=nn.LSTM(input_size,hidden_size,num_layer,batch_first=True)
        self.attention=SelfAttention(hidden_size)
        self.out=nn.Sequential(
            nn.Linear(hidden_size,32),
            nn.ReLU(),
            nn.Linear(32,output_size))
    def forward(self,x):
        lstm_out,_=self.lstm(x)
        attn_out=self.attention(lstm_out)
        context=context = torch.sum(attn_out.unsqueeze(2) * lstm_out, dim=1)
        output=self.out(context)
        return output