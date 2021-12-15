package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DictRegService extends RegistrationService<DictRegistRequest, Dict, DictBase> {

    public DictRegService(DictRegRepository dictRegRepository, ModelMapper modelMapper, DictService dictService) {
        super(dictRegRepository, modelMapper, dictService);
        regClazz = DictReg.class;
    }

    @Override
    public Dict getItem(Registration registration) {
        return ((DictReg) registration).getDict();
    }

    @Override
    public DictBase getBase(Registration registration) {
        return ((DictReg) registration).getDictBase();
    }

    @Override
    public void setItem(Registration registration, Dict item) {
        ((DictReg) registration).setDict(item);
    }

    @Override
    public void update(Registration registration, Dict item) {
        item.getWords().clear();
        item.getWords().addAll(((DictReg) registration).getWords());
        item.getDomains().clear();
        item.getDomains().addAll(((DictReg) registration).getDomains());
    }
}
