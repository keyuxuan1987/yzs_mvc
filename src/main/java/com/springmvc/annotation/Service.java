package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @annotation Name Service
 * @Description: <p>Service注解</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value();
}
