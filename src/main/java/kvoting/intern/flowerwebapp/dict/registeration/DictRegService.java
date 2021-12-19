package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.word.Word;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DictRegService extends RegistrationService<DictRegistRequest, Dict> {

    public DictRegService(DictRegRepository dictRegRepository, ModelMapper modelMapper, DictService dictService) {
        super(dictRegRepository, modelMapper, dictService);
        this.regClazz = DictReg.class;
        this.itemClazz = Dict.class;
    }

    @Override
    public void update(Registration registration, Dict item) {
        item.getWords().clear();
        item.getWords().addAll(((DictReg) registration).getWords());
        item.getDomains().clear();
        item.getDomains().addAll(((DictReg) registration).getDomains());
        item.getCustomDomains().clear();
        item.getCustomDomains().addAll(((DictReg) registration).getCustomDomains());
    }

    @Override
    public void validateItem(Dict item) {
        for (Word word : item.getWords()) {
            if (word.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
        for (Domain domain : item.getDomains()) {
            if (domain.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
        for (CustomDomain customDomain : item.getCustomDomains()) {
            if (customDomain.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
    }
}
