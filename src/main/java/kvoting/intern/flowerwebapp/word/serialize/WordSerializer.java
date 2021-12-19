package kvoting.intern.flowerwebapp.word.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import kvoting.intern.flowerwebapp.word.Word;

import java.io.IOException;

public class WordSerializer extends JsonSerializer<Word> {
    @Override
    public void serialize(Word word, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", word.getId());
        jsonGenerator.writeStringField("engName", word.getWordBase().getEngName());
        jsonGenerator.writeStringField("name", word.getWordBase().getName());
        jsonGenerator.writeStringField("orgEngName", word.getWordBase().getOrgEngName());
        jsonGenerator.writeEndObject();
    }
}
