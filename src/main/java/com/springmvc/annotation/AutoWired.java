package com.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @annotation Name AutoWired
 * @Description: <p>AutoWired注解</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AutoWired {
    String value();
}
