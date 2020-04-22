package io.chainmind.myriadapi;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @JsonComponent 注释允许我们将带注释的类公开为Jackson序列化器和/或反序列化器，而无需手动将其添加到ObjectMapper。
 */
@JsonComponent
public class PageImplSerializer extends JsonSerializer<Page<?>> {
    @Override
    public void serialize(Page<?> page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();//开始写
        jsonGenerator.writeNumberField ("totalElements",page.getTotalElements());
        jsonGenerator.writeNumberField ("totalPages",page.getTotalPages());
        jsonGenerator.writeNumberField ("numberOfElements",page.getNumberOfElements());
        jsonGenerator.writeNumberField ("size",page.getSize());
        jsonGenerator.writeNumberField ("number",page.getNumber());
        jsonGenerator.writeObjectField ("content",page.getContent());
        jsonGenerator.writeBooleanField ("first",page.isFirst());
        jsonGenerator.writeBooleanField ("last",page.isLast());
        jsonGenerator.writeBooleanField ("empty",page.isEmpty());
        jsonGenerator.writeEndObject();//用完记得关闭
    }

}