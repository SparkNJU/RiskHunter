package com.RiskHunter.po;

import com.RiskHunter.enums.RoleEnum;
import com.RiskHunter.vo.UserVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "password")
    private String password;


    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "role")
    private Integer role;

    public UserVO toVO(){
        UserVO userVO=new UserVO();
        userVO.setId(this.id);
        userVO.setAddress(this.address);
        userVO.setName(this.name);
        userVO.setRole(this.role);
        userVO.setPhone(this.phone);
        userVO.setPassword(this.password);
        return userVO;
    }
}
