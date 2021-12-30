package kvoting.intern.flowerwebapp.item.registration.serialize;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import kvoting.intern.flowerwebapp.item.registration.Registration;

public class RegistrationSetSerializer extends JsonSerializer<Set<Registration>> {
	@Override
	public void serialize(Set<Registration> registrations, JsonGenerator jsonGenerator,
		SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartArray();
		for (Registration registration : registrations) {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeNumberField("id", registration.getId());
			jsonGenerator.writeStartObject(registration.getRegistrant());
			jsonGenerator.writeNumberField("id", registration.getRegistrant().getId());
			jsonGenerator.writeStringField("name", registration.getRegistrant().getName());
			jsonGenerator.writeEndObject();
			jsonGenerator.writeStringField("registrationType", String.valueOf(registration.getRegistrationType()));
			jsonGenerator.writeStringField("dateRegistered", String.valueOf(registration.getDateRegistered()));
			if (registration.getProcessor() != null) {
				jsonGenerator.writeStartObject(registration.getProcessor());
				jsonGenerator.writeNumberField("id", registration.getProcessor().getId());
				jsonGenerator.writeStringField("name", registration.getProcessor().getName());
				jsonGenerator.writeEndObject();
			}
			jsonGenerator.writeStringField("processType", String.valueOf(registration.getProcessType()));
			jsonGenerator.writeStringField("dateProcessed", String.valueOf(registration.getDateProcessed()));
			jsonGenerator.writeObjectField("item", registration.getItem());
			jsonGenerator.writeObjectField("base", registration.getBase());
			jsonGenerator.writeEndObject();
		}

		jsonGenerator.writeEndArray();
	}
}
