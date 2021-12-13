package kvoting.intern.flowerwebapp.cmcd.registration.request;

import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CmcdRegRequest {
    private CommonCodeBase commonCodeBase;
    private List<Word> words;
    private Dict dict;
}
