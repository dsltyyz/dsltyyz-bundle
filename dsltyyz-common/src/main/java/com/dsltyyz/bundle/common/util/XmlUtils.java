package com.dsltyyz.bundle.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;

import java.util.List;

/**
 * Description:
 * XML工具类
 *
 * @author: dsltyyz
 * @date 2022-3-30
 */
public class XmlUtils {

    public static JSONObject xmlToJSONObject(String xmlString) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlString);
        JSONObject json = new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;

    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element, JSONObject json) {
        //如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (!isEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && !isEmpty(element.getText())) {//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for (Element e : chdEl) {//有子元素
            if (!e.elements().isEmpty()) {//子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {//如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }
            } else {//子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (!isEmpty(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }


    public static boolean isEmpty(String str) {
        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws DocumentException {
        System.out.println(xmlToJSONObject("<xml><ToUserName><![CDATA[gh_d728bee8a89d]]></ToUserName>\n" +
                "<FromUserName><![CDATA[opKpL5kL9r-yuaaY0p5RrIf03zGA]]></FromUserName>\n" +
                "<CreateTime>1650250197</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[测试三下]]></Content>\n" +
                "<MsgId>23625901531164649</MsgId>\n" +
                "</xml>").toJSONString());
    }
}
