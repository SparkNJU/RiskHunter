package com.RiskHunter.vo;

import com.RiskHunter.po.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserVO {

    private Integer id;

    private String name;

    private String phone;

    private String password;

    private String address;

    private Integer role;


    public User toPO(){
        User user=new User();
        user.setId(this.id);
        user.setAddress(this.address);
        user.setUsername(this.name);
        user.setPhone(this.phone);
        user.setRole(this.role);
        user.setPassword(this.password);
        return user;
    }
}
