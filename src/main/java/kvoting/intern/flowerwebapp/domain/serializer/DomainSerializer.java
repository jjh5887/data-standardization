package kvoting.intern.flowerwebapp.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import kvoting.intern.flowerwebapp.domain.Domain;

public class DomainSerializer extends JsonSerializer<Domain> {
	@Override
	public void serialize(Domain domain, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
		IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("id", domain.getId());
		jsonGenerator.writeObjectField("domainBase", domain.getBase());
		jsonGenerator.writeEndObject();
	}
}
