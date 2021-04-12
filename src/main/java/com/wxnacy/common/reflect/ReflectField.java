package com.wxnacy.common.reflect;
import java.lang.annotation.*;
/**
 * 通过注解名称将数据映射到对象中
 * @author ccbobe
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReflectField{
    String name();
}
