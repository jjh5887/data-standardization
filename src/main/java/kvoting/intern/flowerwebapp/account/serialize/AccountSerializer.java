package kvoting.intern.flowerwebapp.account.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import kvoting.intern.flowerwebapp.account.Account;

public class AccountSerializer extends JsonSerializer<Account> {

	@Override
	public void serialize(Account account, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
		IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("id", account.getId());
		jsonGenerator.writeStringField("name", account.getName());
		jsonGenerator.writeEndObject();
	}
}
