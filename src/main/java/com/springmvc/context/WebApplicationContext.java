package com.springmvc.context;

import com.springmvc.annotation.AutoWired;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.Service;
import com.springmvc.xml.XmlParse;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName WebApplicationContext
 * @Description: <p>自定义IOC容器</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

public class WebApplicationContext {

    // classpath:springmvc.xml
    String contextConfigLocation;

    // 用于存放bean的包名.类名
    List<String> classNameList = new ArrayList<String>();

    //创建Map集合用于扮演IOC的角色，key存放bean的名称 value存放bean示例
    public Map<String, Object> iocMap = new ConcurrentHashMap<String, Object>();

    public WebApplicationContext() {
    }

    public WebApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public void onRefresh() {
        // step1. 进行解析spring mvn 配置文件的操作 ===>com.yzs.controller,com.yzs.service
        String basePackage = XmlParse.getBasePackage(contextConfigLocation.split(":")[1]);
        String[] packs = basePackage.split(",");

        // step2. 进行包扫描
        for (String pack : packs) {
            executePackage(pack);
        }

        // step3 实例化容器中的bean
        executeInstance();

        // step4 进行自动注入
        executeAutoWired();
    }

    /**
     * 进行自动注入
     */
    private void executeAutoWired() {
        try {
            for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
                // 获取容器中的bean
                Object bean = entry.getValue();
                // 获取bean中的属性
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(AutoWired.class)) {
                        // 获取注解中的value值，该值就是bean的name
                        AutoWired autoWiredAnn = field.getAnnotation(AutoWired.class);
                        String beanName = autoWiredAnn.value();
                        field.setAccessible(true);
                        field.set(bean, iocMap.get(beanName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实例化容器中的bean
     */
    private void executeInstance() {
        try {
            // com.yzs.controller.UserController com.yzs.service.impl.UserServiceImpl
            for (String className : classNameList) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                    iocMap.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service serviceAn = clazz.getAnnotation(Service.class);
                    String beanName = serviceAn.value();
                    iocMap.put(beanName, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行包扫描
     *
     * @param pack
     */
    public void executePackage(String pack) {
        // com.yzs.controller.UserController com/yzs/controller/UserController
        URL url = this.getClass().getClassLoader().getResource("/" + pack.replaceAll("\\.", "/"));
        String path = url.getFile();
        // com/yzs/controller/
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            String pck = pack + "." + file.getName();
            if (file.isDirectory()) {
                // 当前是一个文件目录，com.yzs.service.impl
                executePackage(pck);
            } else {
                // 文件目录下的文件 获取全路径 UserController.class ===> com.yzs.controller.UserController
                String className = pck.replaceAll(".class", "");
                classNameList.add(className);
            }
        }
    }
}
