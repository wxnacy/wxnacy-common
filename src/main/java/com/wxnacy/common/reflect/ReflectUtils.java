package com.wxnacy.common.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ccbobe
 * 对象属性与注解映射及其设置映射value
 */
public class ReflectUtils {

    /**
     * 缓存Class字段信息
     */
    private static Map<Class<?>, Map<String, String>> CACHE_CLASS_INFO = new ConcurrentHashMap(32);

    /**
     * 获取指定类的映射信息
     * @param clazz
     * @param <T>
     * @return 返回映射信息
     */
    public static <T> Map<String,String> getBeanMetaByReflectField(Class<T> clazz){
        Map<String, String> map = CACHE_CLASS_INFO.get(clazz);
        if (map!=null && !map.isEmpty()){
            return CACHE_CLASS_INFO.get(clazz);
        }
        Map<String, String> headerData = new HashMap<>();
        Class<?> cls = clazz;
        //遍历所有父类字节码对象
        while (cls != Object.class) {
            Field[] fields = cls.getDeclaredFields();
            List<Field> list = new ArrayList<>();
            list.addAll(Arrays.asList(fields));
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                ReflectField fieldAnnotation = field.getDeclaredAnnotation(ReflectField.class);
                if (fieldAnnotation != null) {
                    field.setAccessible(true);
                    String name = field.getName();
                    headerData.put(fieldAnnotation.name(),name);
                } else {
                    String name = field.getName();
                    headerData.put(name,name);
                }
            }
            // 遍历所有父类字节码对象
            cls = cls.getSuperclass();
        }
        CACHE_CLASS_INFO.put(clazz,headerData);
        return headerData;
    }

    /**
     * 将map数据映射到指定对象中
     * @param mapData
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String,Object> mapData,Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        T instance = null;
        instance = clazz.getDeclaredConstructor().newInstance();
        Map<String, String> meta = getBeanMetaByReflectField(clazz);
        for(Map.Entry<String, Object> entry : mapData.entrySet()){
            String fieldName = entry.getKey();
            Object dataValue = entry.getValue();
            //存在属性则设置
            if (meta.containsKey(fieldName)){
                setValue(instance,meta.get(fieldName),dataValue);
            }
        }
        return instance;
    }

    /**
     * @param obj
     * @param fieldName
     * @param value
     * @return
     * @throws
     * @Description 设置 属性值
     */
    public static void setValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}