package com.yzs.controller;

import com.springmvc.annotation.AutoWired;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.RequestMapping;
import com.springmvc.annotation.ResponseBody;
import com.yzs.bean.User;
import com.yzs.service.UserService;

/**
 * @ClassName UserController
 * @Description: <p>用户controller</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

@Controller
public class UserController {

    @AutoWired(value = "userService")
    private UserService userService;

    @RequestMapping("/findUser")
    public String findUser() {
        userService.findUser();
        return "forward:/success.jsp";
    }

    @RequestMapping("/getUserByParameter")
    @ResponseBody
    public User getUserByParameter(String name, String pass) {
        return userService.getUserByParameter(name, pass);
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public User getUser() {
        return userService.getUser();
    }

}
