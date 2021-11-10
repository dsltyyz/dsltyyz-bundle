package com.dsltyyz.bundle.common.data;

import com.dsltyyz.bundle.common.data.format.DataFormat;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author Administrator
 */
public class DataFormatAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated annotated){
        DataFormat formatter = annotated.getAnnotation(DataFormat.class);
        if(formatter != null){
            return new DataSerializer(formatter.value(), formatter.param());
        }
        return super.findSerializer(annotated);
    }


}
