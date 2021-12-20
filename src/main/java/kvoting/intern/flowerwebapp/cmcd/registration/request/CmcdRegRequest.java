package kvoting.intern.flowerwebapp.cmcd.registration.request;

import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class CmcdRegRequest extends RegRequest {
    private CommonCodeBase base;
    private List<Word> words;
    private Dict dict;
}
