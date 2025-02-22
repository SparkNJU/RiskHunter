import axios from 'axios'
import { SERVER_ADDR } from '../config'

//创建一个axios的实例service
const service = axios.create(
    {
        //baseURL: SERVER_ADDR, 
        //TODO : 设置后端地址（本地或服务器），会将请求转发到后端端口
        baseURL: "http://localhost:8080",
        timeout: 30000
    }
)

//判断是否登录
function hasToken() {
    return !(sessionStorage.getItem('token') == '')
}

//当前实例的拦截器，对所有要发送给后端的请求进行处理，在其中加入token
service.interceptors.request.use(
    config => {
        if(hasToken()) {
            config.headers['token'] = sessionStorage.getItem('token')
        }
        return config
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
)

//当前实例的拦截器，对所有从后端收到的请求进行处理，检验http的状态码
service.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return response;
        } else {
            return Promise.reject();
        }
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
)

//设置为全局变量
export {
    service as axios
}
