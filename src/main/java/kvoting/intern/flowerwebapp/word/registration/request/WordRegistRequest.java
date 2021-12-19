package kvoting.intern.flowerwebapp.word.registration.request;

import kvoting.intern.flowerwebapp.word.WordBase;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordRegistRequest {
    private WordBase base;
}
