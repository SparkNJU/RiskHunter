package com.RiskHunter.exception;

/**
 * @Author: DingXiaoyu
 * @Date: 0:26 2023/11/26
 * 你可以在这里自定义Exception
*/
public class RiskHunterException extends RuntimeException{

    public RiskHunterException(String message){
        super(message);
    }
    public static RiskHunterException phoneAlreadyExists(){
        return new RiskHunterException("手机号已经存在!");
    }

    public static RiskHunterException notLogin(){
        return new RiskHunterException("未登录!");
    }

    public static RiskHunterException phoneOrPasswordError(){
        return new RiskHunterException("手机号或密码错误!");
    }

}
