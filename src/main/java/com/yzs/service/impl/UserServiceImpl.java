package com.yzs.service.impl;

import com.springmvc.annotation.Service;
import com.yzs.bean.User;
import com.yzs.service.UserService;

/**
 * @ClassName UserServiceImpl
 * @Description: <p>用户类实现</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Override
    public void findUser() {
        System.out.println("==============调用UserServiceImpl.findUser==============");
    }

    @Override
    public User getUserByParameter(String name, String pass) {
        return new User((int) Math.random(), name, pass);
    }

    @Override
    public User getUser() {
        return new User(1, "Old Wang", "admin");
    }
}
