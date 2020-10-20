package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @annotation Name Controller
 * @Description: <p>Controller注解</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
