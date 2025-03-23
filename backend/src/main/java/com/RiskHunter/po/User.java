package com.RiskHunter.po;

import com.RiskHunter.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ApiModel(value = "用户Model")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @Basic
    @Column(name = "username")
    @ApiModelProperty(value = "用户名")
    private String username;

    @Basic
    @Column(name = "phone")
    @ApiModelProperty(value = "电话号码")
    private String phone;

    @Basic
    @Column(name = "password")
    @ApiModelProperty(value = "密码")
    private String password;

    @Basic
    @Column(name = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    @Basic
    @Column(name = "role")
    @ApiModelProperty(value = "用户角色")
    private Integer role;

    public UserVO toVO(){
        UserVO userVO=new UserVO();
        userVO.setId(this.id);
        userVO.setDescription(this.description);
        userVO.setUsername(this.username);
        userVO.setRole(this.role);
        userVO.setPhone(this.phone);
        userVO.setPassword(this.password);
        return userVO;
    }
}