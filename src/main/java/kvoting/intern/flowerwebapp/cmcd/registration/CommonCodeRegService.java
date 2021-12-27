package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeRepository;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommonCodeRegService extends RegistrationService {
    private final DictRepository dictRepository;
    private final CommonCodeRepository commonCodeRepository;

    public CommonCodeRegService(CommonCodeRegRepository commonCodeRegRepository, ModelMapper modelMapper, CommonCodeService commonCodeService, DictRepository dictRepository, CommonCodeRepository commonCodeRepository) {
        super(commonCodeRegRepository, modelMapper, commonCodeService);
        this.dictRepository = dictRepository;
        this.commonCodeRepository = commonCodeRepository;
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
    public void updateItem(Item item, RegRequest request) {
        CmcdRegRequest cmcdRegRequest = (CmcdRegRequest) request;
        CommonCode commonCode = (CommonCode) item;
        if (cmcdRegRequest.getDict() != null) {
            commonCode.setDict(dictRepository.findById(cmcdRegRequest.getDict())
                    .orElseThrow(() -> {
                        throw new RuntimeException();
                    }));
            commonCode.setDictId(((CmcdRegRequest) request).getDict());
            commonCode.setDictName(commonCode.getDict().getName());
        }
        if (cmcdRegRequest.getHighDict() != null) {
            commonCode.setHighDict(dictRepository.findById(cmcdRegRequest.getHighDict())
                    .orElseThrow(() -> {
                        throw new RuntimeException();
                    }));
            commonCode.setHighDictId(((CmcdRegRequest) request).getHighDict());
            commonCode.setHighDictName(commonCode.getHighDict().getName());
        }

        if (cmcdRegRequest.getHighCommonCode() != null) {
            commonCode.setHighCommonCode(commonCodeRepository.findById(cmcdRegRequest.getHighCommonCode()).orElse(null));
            commonCode.setHighCmcdId(commonCode.getHighCommonCode().getId());
            commonCode.setHighCmcdName(commonCode.getHighCommonCode().getName());
        }
    }

    @Override
    public void updateReg(Registration registration, RegRequest request) {
        CmcdRegRequest cmcdRegRequest = (CmcdRegRequest) request;
        CommonCodeReg commonCodeReg = (CommonCodeReg) registration;
        if (cmcdRegRequest.getDict() != null) {
            commonCodeReg.setDict(dictRepository.findById(cmcdRegRequest.getDict())
                    .orElseThrow(() -> {
                        throw new RuntimeException();
                    }));
            commonCodeReg.setDictId(((CmcdRegRequest) request).getDict());
            commonCodeReg.setDictName(commonCodeReg.getDict().getName());
        }
        if (cmcdRegRequest.getHighDict() != null) {
            commonCodeReg.setHighDict(dictRepository.findById(cmcdRegRequest.getHighDict())
                    .orElseThrow(() -> {
                        throw new RuntimeException();
                    }));
            commonCodeReg.setHighDictId(((CmcdRegRequest) request).getHighDict());
            commonCodeReg.setHighDictName(commonCodeReg.getHighDict().getName());
        }

        if (cmcdRegRequest.getHighCommonCode() != null) {
            commonCodeReg.setHighCommonCode(commonCodeRepository.findById(cmcdRegRequest.getHighCommonCode()).orElse(null));
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
    }
}
