package com.dsltyyz.bundle.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 反射工具类
 *
 * @author: dsltyyz
 * @date: 2019/04/10
 */
public class ReflexUtils {

    /**
     * 实例化实体
     *
     * @param claszz 类类型
     * @param <T>    实例
     * @return
     */
    public static <T> T instance(Class<T> claszz) throws IllegalAccessException, InstantiationException {
        return claszz.newInstance();
    }

    /**
     * 为实体组装属性值
     *
     * @param object   操作对象
     * @param property 属性
     * @param value    值
     */
    public static void setPropertyValueForObject(Object object, String property, String value) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        Class clazz = object.getClass();
        Field field = clazz.getDeclaredField(property);
        Class<?> typeClazz = field.getType();
        Method method = clazz.getMethod(setFieldMethod(property), typeClazz);
        //可能存在值无法插入
        method.invoke(object, getClassTypeValue(typeClazz, value));
    }

    /**
     * 通过class类型获取获取对应类型的值
     *
     * @param typeClass class类型
     * @param value     值
     * @return Object
     */
    private static Object getClassTypeValue(Class<?> typeClass, String value) throws ParseException {
        if (typeClass.isInstance(value)) {
            return typeClass.cast(value);
        } else if (typeClass == Integer.class) {
            return Integer.valueOf(value);
        } else if (typeClass == Long.class) {
            return Long.valueOf(value);
        } else if (typeClass == Double.class) {
            return Double.valueOf(value);
        } else if (typeClass == Boolean.class) {
            return Boolean.valueOf(value);
        } else if (typeClass == Date.class) {
            return DateUtils.parse(value);
        } else if (typeClass == LocalDateTime.class) {
            return LocalDateTimeUtils.parse(value);
        } else if (typeClass == BigInteger.class) {
            return new BigInteger(value);
        } else if (typeClass == BigDecimal.class) {
            return new BigDecimal(value);
        }
        return null;
    }


    /**
     * 将对象转成map
     *
     * @param object
     * @return
     */
    public static Map<String, String> objectToMap(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Method method = clazz.getMethod(getFieldMethod(field.getName()));
            Class methodTypeClass = method.getReturnType();
            Object value = method.invoke(object);
            if (Date.class.getSimpleName().equals(methodTypeClass.getSimpleName())) {
                if (value != null) {
                    value = DateUtils.format((Date) value);
                }
            } else if (LocalDateTime.class.getSimpleName().equals(methodTypeClass.getSimpleName())) {
                if (value != null) {
                    value = LocalDateTimeUtils.format((LocalDateTime) value);
                }
            }
            map.put(field.getName(), null == value ? "" : String.valueOf(value));
        }
        return map;
    }

    /**
     * 组装get方法字符串
     *
     * @param field
     * @return
     */
    public static String getFieldMethod(String field) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    /**
     * 组装set方法字符串
     *
     * @param field
     * @return
     */
    public static String setFieldMethod(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }
}
