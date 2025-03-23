package com.RiskHunter.serviceImpl;

import com.RiskHunter.exception.RiskHunterException;
import com.RiskHunter.repository.UserRepository;
import com.RiskHunter.util.SecurityUtil;
import com.RiskHunter.util.TokenUtil;
import com.RiskHunter.vo.UserVO;
import com.RiskHunter.po.User;
import com.RiskHunter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;


    @Override
    public Boolean register(UserVO userVO) {
        User user = userRepository.findByPhone(userVO.getPhone());
        if (user != null) {
            throw RiskHunterException.phoneAlreadyExists();
        }
        User newUser = userVO.toPO();
        userRepository.save(newUser);
        return true;
    }

    @Override
    public String login(String phone, String password) {
        User user = userRepository.findByPhoneAndPassword(phone, password);
        if (user == null) {
            throw RiskHunterException.phoneOrPasswordError();
        }
        return tokenUtil.getToken(user);
    }

    @Override
    public UserVO getInformation() {
        User user=securityUtil.getCurrentUser();
        return user.toVO();
    }

    @Override
    public Boolean updateInformation(UserVO userVO) {
        User user=securityUtil.getCurrentUser();
        if (userVO.getPassword()!=null){
            user.setPassword(userVO.getPassword());
        }
        if (userVO.getUsername()!=null){
            user.setUsername(userVO.getUsername());
        }
        if (userVO.getDescription()!=null){
            user.setDescription(userVO.getDescription());
        }
        userRepository.save(user);
        return true;
    }

}
