package com.wxnacy.common.reflect;

import jdk.jfr.DataAmount;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectUtilsTest {
    @ReflectField(name = "_name")
    private String name;

    public String getName() {
        return name;
    }

    @Test
    public void setValue() throws NoSuchFieldException, IllegalAccessException {
        var item = new ReflectUtilsTest();
        String name = "wxnacy";
        ReflectUtils.setValue(item, "name", name);
        assert name == item.getName();
    }

    @Test
    public void mapToBean() {
        String name = "wxnacy";
        Map<String, Object> map = new HashMap<>();
        map.put("_name", name);
        try {
            var item = ReflectUtils.mapToBean(map, ReflectUtilsTest.class);
            assert item.getName() == name;

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}