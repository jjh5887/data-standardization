package kvoting.intern.flowerwebapp.word.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import kvoting.intern.flowerwebapp.word.Word;

public class WordSerializer extends JsonSerializer<Word> {
	@Override
	public void serialize(Word word, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
		IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("id", word.getId());
		jsonGenerator.writeObjectField("base", word.getBase());
		jsonGenerator.writeEndObject();
	}
}
