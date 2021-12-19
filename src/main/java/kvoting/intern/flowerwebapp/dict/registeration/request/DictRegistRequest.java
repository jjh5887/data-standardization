package kvoting.intern.flowerwebapp.dict.registeration.request;

import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class DictRegistRequest {
    private DictBase base;
    private List<Word> words;
    private List<Domain> domains;
    private Set<CustomDomain> customDomains;
}
