package com.springmvc.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.RequestMapping;
import com.springmvc.annotation.ResponseBody;
import com.springmvc.context.WebApplicationContext;
import com.springmvc.handler.MyHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DispatcherServlet
 * @Description: <p>核心控制器</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

public class DispatcherServlet extends HttpServlet {

    /**
     * 指定Spring MVC容器
     */
    private WebApplicationContext applicationContext;

    /**
     * 用于存放用户请求映射关系
     */
    List<MyHandler> handlerList = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        // step1 初始化参数 classpath:springmvc.xml
        String contextConfigLocation = this.getServletConfig().getInitParameter("contextConfigLocation");

        // step2 创建Spring MVC容器
        applicationContext = new WebApplicationContext(contextConfigLocation);

        // step3 进行初始化操作
        applicationContext.onRefresh();

        // step4 初始化请求映射关系
        initHandlerMapping();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatcher(req, resp);
    }

    public void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MyHandler myHandler = getHandler(req);
        try {
            PrintWriter writer = resp.getWriter();
            if (myHandler == null) {
                writer.print("<h1>404 NOT FOUND!</h1>");
            } else {
                // 参数处理
                String[] params = myHandler.getParams();
                Object[] values = null;
                if (params != null) {
                    values = handlerParameters(req, params);
                }
                // 执行调用
                Object result = myHandler.getMethod().invoke(myHandler.getController(), values);
                if (result instanceof String) {
                    String viewName = (String) result;
                    String viewType = viewName.split(":")[0];
                    String viewPage = viewName.split(":")[1];
                    if (viewType.equals("forward")) {
                        req.getRequestDispatcher(viewPage).forward(req, resp);
                    } else {
                        // redirect:/user.jsp
                        resp.sendRedirect(viewPage);
                    }
                } else {
                    Method method = myHandler.getMethod();
                    if (method.isAnnotationPresent(ResponseBody.class)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(result);
                        resp.setContentType("text/html;charset=UTF-8");
                        writer.print(json);
                        writer.flush();
                        writer.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object[] handlerParameters(HttpServletRequest req, String[] params) {
        Object[] values = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            String value = req.getParameter(param);
            values[i] = value;
        }
        return values;
    }

    /**
     * 获取请求对应的Handler
     *
     * @param req
     * @return
     */
    public MyHandler getHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        for (MyHandler myHandler : handlerList) {
            if (myHandler.getUrl().equals(requestURI)) {
                return myHandler;
            }
        }
        return null;
    }

    /**
     * 初始化请求映射关系
     */
    private void initHandlerMapping() {
        for (Map.Entry<String, Object> entry : applicationContext.iocMap.entrySet()) {
            // 获取bean class的类型
            Class<?> clazz = entry.getValue().getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                // 获取bean中的所有方法
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        //获取注解中的值
                        String url = requestMapping.value();
                        // 建立映射地址
                        MyHandler myHandler = new MyHandler(url, entry.getValue(), method);
                        // 处理参数
                        handlerParameters(method, myHandler);
                        handlerList.add(myHandler);
                    }
                }
            }
        }

    }

    private void handlerParameters(Method method, MyHandler myHandler) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            String[] params = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                params[i] = parameter.getName();
            }
            myHandler.setParams(params);
        }
    }
}
