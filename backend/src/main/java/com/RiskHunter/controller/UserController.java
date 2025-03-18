package com.RiskHunter.controller;

import com.RiskHunter.service.UserService;
import com.RiskHunter.vo.ResultVO;
import com.RiskHunter.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户管理", tags = { "用户相关接口" })
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "用户注册", notes = "注册一个新用户")
    @PostMapping("/register")
    public ResultVO<Boolean> register(
            @ApiParam(value = "用户信息对象", required = true) @RequestBody UserVO userVO) {
        return ResultVO.buildSuccess(userService.register(userVO));
    }

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public ResultVO<String> login(
            @ApiParam(value = "用户手机号", required = true) @RequestParam("phone") String phone,
            @ApiParam(value = "用户密码", required = true) @RequestParam("password") String password) {
        return ResultVO.buildSuccess(userService.login(phone, password));
    }

    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户的信息")
    @GetMapping
    public ResultVO<UserVO> getInformation() {
        return ResultVO.buildSuccess(userService.getInformation());
    }

    @ApiOperation(value = "更新用户信息", notes = "更新当前登录用户的信息")
    @PostMapping
    public ResultVO<Boolean> updateInformation(
            @ApiParam(value = "用户信息对象", required = true) @RequestBody UserVO userVO) {
        return ResultVO.buildSuccess(userService.updateInformation(userVO));
    }
}