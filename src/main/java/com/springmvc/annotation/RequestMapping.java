package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @annotation Name RequestMapping
 * @Description: <p>RequestMapping注解</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
