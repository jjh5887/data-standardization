package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.word.Word;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictRegService extends RegistrationService {

    public DictRegService(DictRegRepository dictRegRepository, ModelMapper modelMapper, DictService dictService) {
        super(dictRegRepository, modelMapper, dictService);
        this.regClazz = DictReg.class;
        this.itemClazz = Dict.class;
    }

    @Override
    public void update(Registration registration, Item item) {
        ((Dict) item).getWords().clear();
        ((Dict) item).getWords().addAll(((DictReg) registration).getWords());
        ((Dict) item).getDomains().clear();
        ((Dict) item).getDomains().addAll(((DictReg) registration).getDomains());
        ((Dict) item).getCustomDomains().clear();
        ((Dict) item).getCustomDomains().addAll(((DictReg) registration).getCustomDomains());
    }

    @Override
    @Transactional
    public void validateItem(Item item) {
        for (Word word : ((Dict) item).getWords()) {
            if (word.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
        for (Domain domain : ((Dict) item).getDomains()) {
            System.out.println(domain.getStatus());
            if (domain.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
        for (CustomDomain customDomain : ((Dict) item).getCustomDomains()) {
            if (customDomain.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
    }
}
