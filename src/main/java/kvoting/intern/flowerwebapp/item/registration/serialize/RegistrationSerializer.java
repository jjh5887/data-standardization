package kvoting.intern.flowerwebapp.item.registration.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import kvoting.intern.flowerwebapp.item.registration.Registration;

import java.io.IOException;

public class RegistrationSerializer extends JsonSerializer<Registration> {
    @Override
    public void serialize(Registration registration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", registration.getId());
//        jsonGenerator.writeNumberField("registrant.id", registration.getRegistrant().getId());
//        jsonGenerator.writeStringField("registrant.name", registration.getRegistrant().getName());
        jsonGenerator.writeStringField("registrationType", String.valueOf(registration.getRegistrationType()));
        jsonGenerator.writeStringField("dateRegistered", String.valueOf(registration.getDateRegistered()));
//        jsonGenerator.writeObjectField("processor", registration.getProcessor());
        jsonGenerator.writeStringField("processType", String.valueOf(registration.getProcessType()));
        jsonGenerator.writeStringField("dateProcessed", String.valueOf(registration.getDateProcessed()));
//        jsonGenerator.writeObjectField("item", registration.getItem());
//        jsonGenerator.writeObjectField("base", registration.getBase());
        jsonGenerator.writeEndObject();
    }
}
