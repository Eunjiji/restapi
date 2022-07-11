package me.whiteship.restapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent  // objectMapper가 이 클래스를 사용하려면 등록을 시켜줘야하는데 그 등록을 spring boot가 제공하는 @JsonComponent를 사용해서 해준다.
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartArray();
        errors.getFieldErrors().forEach(e ->{
            try {
                gen.writeStartObject();
                gen.writeStringField("field", e.getField());
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null) {
                    gen.writeStringField("rejectValue", rejectedValue.toString());
                }

                gen.writeEndObject();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(e ->{
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            }catch (IOException e1){
                e1.printStackTrace();
            }
        });
        gen.writeEndArray();
    }
}
