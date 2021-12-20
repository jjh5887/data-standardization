package kvoting.intern.flowerwebapp.word.response;

import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordResponse {
    private Long id;
    private WordBase base;
    private ProcessType status;
    private Long modifierId;
    private String modifierName;

    public static WordResponse map(Word word) {
        return WordResponse.builder()
                .id(word.getId())
                .base(word.getBase())
                .status(word.getStatus())
//                .modifierId(word.getModifierId())
//                .modifierName(word.getModifierName())
                .build();
    }
}
