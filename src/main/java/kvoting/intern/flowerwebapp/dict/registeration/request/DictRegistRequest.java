package kvoting.intern.flowerwebapp.dict.registeration.request;

import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
public class DictRegistRequest extends RegRequest {
    private DictBase base;
    private List<Word> words;
    private List<Domain> domains;
    private Set<CustomDomain> customDomains;
}
