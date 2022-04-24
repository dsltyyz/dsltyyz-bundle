package com.dsltyyz.bundle.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * 正则工具类
 *
 * @author: dsltyyz
 * @date: 2022-4-19
 */
public class PatternUtils {

    /**
     * 获取html中src资源列表
     * @param html
     * @return
     */
    public static List<String> getSrcResource(String html){
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("src=\\\"?(.*?)(\\\"|>|\\\\s+)");
        Matcher matcher = pattern.matcher(html);
        while(matcher.find()){
            String group = matcher.group();
            String url = group.substring(group.indexOf("\"") + 1, group.lastIndexOf("\""));
            list.add(url);
        }
        return list;
    }
}
