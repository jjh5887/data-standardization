package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommonCodeRegService extends RegistrationService<CmcdRegRequest, CommonCode> {
    public CommonCodeRegService(CommonCodeRegRepository commonCodeRegRepository, ModelMapper modelMapper, CommonCodeService commonCodeService) {
        super(commonCodeRegRepository, modelMapper, commonCodeService);
        this.regClazz = CommonCodeReg.class;
        this.itemClazz = CommonCode.class;
    }

    @Override
    public void update(Registration registration, CommonCode item) {
        item.getWords().clear();
        item.getWords().addAll(((CommonCodeReg) registration).getWords());
    }

    @Override
    public void validateItem(CommonCode item) {
        if (item.getDict().getStatus() != ProcessType.APPROVED) {
            throw new RuntimeException();
        }
    }
}
