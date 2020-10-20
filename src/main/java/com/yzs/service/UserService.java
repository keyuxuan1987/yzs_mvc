package com.yzs.service;

import com.yzs.bean.User;

/**
 * @ClassName UserService
 * @Description: <p>用户业务类</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

public interface UserService {

    void findUser();

    User getUserByParameter(String name, String pass);

    User getUser();
}
