package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommonCodeRegService extends RegistrationService {
    public CommonCodeRegService(CommonCodeRegRepository commonCodeRegRepository, ModelMapper modelMapper, CommonCodeService commonCodeService) {
        super(commonCodeRegRepository, modelMapper, commonCodeService);
        this.regClazz = CommonCodeReg.class;
        this.itemClazz = CommonCode.class;
    }

    @Override
    public void update(Registration registration, Item item) {
        ((CommonCode) item).getWords().clear();
        ((CommonCode) item).getWords().addAll(((CommonCodeReg) registration).getWords());
    }

    @Override
    public void validateItem(Item item) {
        if (((CommonCode) item).getDict().getStatus() != ProcessType.APPROVED) {
            throw new RuntimeException();
        }
    }
}
