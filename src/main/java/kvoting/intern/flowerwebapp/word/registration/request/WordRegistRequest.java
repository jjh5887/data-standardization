package kvoting.intern.flowerwebapp.word.registration.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordRegistRequest {
    private String engName;
    private String name;
    private String orgEngName;
}
