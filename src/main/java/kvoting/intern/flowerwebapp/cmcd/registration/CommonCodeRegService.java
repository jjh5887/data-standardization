package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CommonCodeRegService extends RegistrationService {

    private final DictService dictService;
    private final CommonCodeService commonCodeService;

    public CommonCodeRegService(CommonCodeRegRepository commonCodeRegRepository,
        ModelMapper modelMapper, @Lazy CommonCodeService commonCodeService,
        @Lazy DictService dictService,
        CommonCodeService commonCodeService1) {
        super(commonCodeRegRepository, modelMapper, commonCodeService);
        this.dictService = dictService;
        this.commonCodeService = commonCodeService1;
        this.regClazz = CommonCodeReg.class;
        this.itemClazz = CommonCode.class;
    }

    @Override
    public void validateItem(Item item) {
        if (((CommonCode) item).getDict().getStatus() != ProcessType.APPROVED) {
            throw new RuntimeException();
        }
    }

    @Override
    public void updateItem(Item item, RegRequest regRequest) {
        CmcdRegRequest request = (CmcdRegRequest) regRequest;
        CommonCode commonCode = (CommonCode) item;
        if (request.getDict() != null) {
            commonCode.setDict((Dict) dictService.get(request.getDict()));
            commonCode.setDictId(commonCode.getDict().getId());
            commonCode.setDictName(commonCode.getDict().getName());
        }
        if (request.getHighDict() != null) {
            commonCode.setHighDict((Dict) dictService.get(request.getHighDict()));
            commonCode.setHighDictId(commonCode.getHighDict().getId());
            commonCode.setHighDictName(commonCode.getHighDict().getName());
        }
        if (request.getHighCommonCode() != null) {
            commonCode.setHighCommonCode(
                (CommonCode) commonCodeService.get(request.getHighCommonCode()));
            commonCode.setHighCmcdId(commonCode.getHighCommonCode().getId());
            commonCode.setHighCmcdName(commonCode.getHighCommonCode().getName());
        }
    }

    @Override
    public void updateReg(Registration registration, RegRequest regRequest) {
        CmcdRegRequest request = (CmcdRegRequest) regRequest;
        CommonCodeReg commonCodeReg = (CommonCodeReg) registration;
        if (request.getDict() != null) {
            commonCodeReg.setDict((Dict) dictService.get(request.getDict()));
            commonCodeReg.setDictId(commonCodeReg.getDict().getId());
            commonCodeReg.setDictName(commonCodeReg.getDict().getName());
        }
        if (request.getHighDict() != null) {
            commonCodeReg.setHighDict((Dict) dictService.get(request.getHighDict()));
            commonCodeReg.setHighDictId(commonCodeReg.getHighDict().getId());
            commonCodeReg.setHighDictName(commonCodeReg.getHighDict().getName());
        }
        if (request.getHighCommonCode() != null) {
            commonCodeReg.setHighCommonCode(
                (CommonCode) commonCodeService.get(request.getHighCommonCode()));
            commonCodeReg.setHighCmcdId(commonCodeReg.getHighCommonCode().getId());
            commonCodeReg.setHighCmcdName(commonCodeReg.getHighCommonCode().getName());
        }
    }

    @Override
    public void update(Registration registration, Item item) {
        CommonCodeReg commonCodeReg = (CommonCodeReg) registration;
        CommonCode commonCode = (CommonCode) item;
        commonCode.setDict(commonCodeReg.getDict());
        commonCode.setHighDict(((CommonCodeReg) registration).getHighDict());
        commonCode.setHighCommonCode(commonCodeReg.getHighCommonCode());
        commonCode.setBase(commonCodeReg.getBase());
    }
}
