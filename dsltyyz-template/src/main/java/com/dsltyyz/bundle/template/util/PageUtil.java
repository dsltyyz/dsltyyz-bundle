package com.dsltyyz.bundle.template.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dsltyyz.bundle.template.page.PageDTO;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Page工具
 *
 * @author: dsltyyz
 * @date: 2021-9-17
 */
public class PageUtil {

    /**
     * 映射转换
     * @param pageDTO
     * @param map
     */
    public static void convertOrderItem(PageDTO pageDTO, Map<String, String> map) {
        //排序列表不为空 且map转换不为空
        if (pageDTO.getOrders() != null && pageDTO.getOrders().size() != 0 && map != null && map.size() > 0) {
            pageDTO.getOrders().forEach(orderItem -> {
                //只替换map中的字段 没有的保留原字段
                if (map.containsKey(orderItem.getColumn())) {
                    orderItem.setColumn(map.get(orderItem.getColumn()));
                }
            });
        }
    }

    /**
     * 实体类注解转换
     * @param pageDTO
     * @param entityClass
     */
    public static void convertOrderItem(PageDTO pageDTO, Class entityClass) {
        //排序列表不为空
        if (pageDTO.getOrders() != null && pageDTO.getOrders().size() != 0) {
            pageDTO.getOrders().forEach(orderItem -> {
                try {
                    Field declaredField = entityClass.getDeclaredField(orderItem.getColumn());
                    boolean annotationPresent = declaredField.isAnnotationPresent(TableId.class);
                    if (declaredField.isAnnotationPresent(TableId.class)) {
                        orderItem.setColumn(declaredField.getAnnotation(TableId.class).value());
                    } else if(declaredField.isAnnotationPresent(TableField.class)){
                        orderItem.setColumn(declaredField.getAnnotation(TableField.class).value());
                    }else{
                        orderItem.setColumn(underscoreName(orderItem.getColumn()));
                    }
                } catch (Exception e) {
                    return;
                }
            });
        }
    }

    /**
     * 下划线转换
     * @param pageDTO
     */
    public static void convertOrderItem(PageDTO pageDTO) {
        //排序列表不为空 且map转换不为空
        if (pageDTO.getOrders() != null && pageDTO.getOrders().size() != 0) {
            pageDTO.getOrders().forEach(orderItem -> {
                orderItem.setColumn(underscoreName(orderItem.getColumn()));
            });
        }
    }

    private static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }

    public static Map<String, String> convertClassToMap(Class entityClass){
        Map<String, String> map = new HashMap<>();
        Field[] declaredFields = entityClass.getDeclaredFields();
        for(Field declaredField:declaredFields){
            if (declaredField.isAnnotationPresent(TableId.class)) {
                map.put(declaredField.getName(), declaredField.getAnnotation(TableId.class).value());
            } else if(declaredField.isAnnotationPresent(TableField.class)){
                map.put(declaredField.getName(), declaredField.getAnnotation(TableField.class).value());
            }else{
                map.put(declaredField.getName(), underscoreName(declaredField.getName()));
            }
        }
        return map;
    }

}
