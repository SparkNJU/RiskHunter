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

    private String username;

    private String phone;

    private String password;

    private String description;

    private Integer role;


    public User toPO(){
        User user=new User();
        user.setId(this.id);
        user.setDescription(this.description);
        user.setUsername(this.username);
        user.setPhone(this.phone);
        user.setRole(this.role);
        user.setPassword(this.password);
        return user;
    }
}
