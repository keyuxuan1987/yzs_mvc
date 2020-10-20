package com.springmvc.handler;

import java.lang.reflect.Method;

/**
 * @ClassName MyHandler
 * @Description: <p>地址处理器</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

public class MyHandler {

    private String url;

    //控制器
    private Object controller;

    // 控制器的调用的方法
    private Method method;

    private String[] params;

    public MyHandler() {
    }

    public MyHandler(String url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
