package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommonCodeRegService extends RegistrationService<CmcdRegRequest, CommonCode, CommonCodeBase> {
    public CommonCodeRegService(CommonCodeRegRepository commonCodeRegRepository, ModelMapper modelMapper, CommonCodeService commonCodeService) {
        super(commonCodeRegRepository, modelMapper, commonCodeService);
        regClazz = CommonCodeReg.class;
    }

    @Override
    public CommonCode getItem(Registration registration) {
        return ((CommonCodeReg) registration).getCommonCode();
    }

    @Override
    public CommonCodeBase getBase(Registration registration) {
        return ((CommonCodeReg) registration).getCommonCodeBase();
    }

    @Override
    public void setItem(Registration registration, CommonCode item) {
        ((CommonCodeReg) registration).setCommonCode(item);
    }

    @Override
    public void update(Registration registration, CommonCode item) {
        item.getWords().clear();
        item.getWords().addAll(((CommonCodeReg) registration).getWords());
    }
}
