package com.dsltyyz.bundle.common.data;

import com.dsltyyz.bundle.common.data.helper.DataHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author dsltyyz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSerializer extends JsonSerializer<Object> {

    /**
     * 数据处理类
     */
    private Class clazz;
    private String param;

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            DataHelper dataMask = (DataHelper)clazz.newInstance();
            jsonGenerator.writeObject(dataMask.deal(o, param));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
