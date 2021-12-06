package kvoting.intern.flowerwebapp.domain.registeration.request;

import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DomainRegistRequest {
    private String description;
    private DB db;
    private DataType dataType;
    private int size;
    private int scale;
    private boolean nullable;
    private List<Word> words;
}
