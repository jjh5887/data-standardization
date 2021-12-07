package kvoting.intern.flowerwebapp.domain.registeration.request;

import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DomainRegistRequest {
    private DomainBase domainBase;
    private List<Word> words;
}
