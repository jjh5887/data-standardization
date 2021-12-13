package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonCodeRegService {
    private final CommonCodeRegRepository commonCodeRegRepository;
    private final CommonCodeService commonCodeService;
    private final ModelMapper modelMapper;

    public CommonCodeReg save(CommonCodeReg commonCodeReg) {
        return commonCodeRegRepository.save(commonCodeReg);
    }

    public CommonCodeReg getCommonCodeReg(Long id) {
        return commonCodeRegRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public CommonCodeReg create(CmcdRegRequest request) {
        CommonCode mappedCommonCode = modelMapper.map(request, CommonCode.class);
        mappedCommonCode.setStatus(ProcessType.UNHANDLED);
        mappedCommonCode.setCommonCodeRegs(new HashSet<>());
        CommonCode commonCode = commonCodeService.save(mappedCommonCode);

        return save(generateCmcdReg(request, commonCode, RegistrationType.CREATE));
    }

    public CommonCodeReg modify(CmcdRegRequest request, Long id) {
        CommonCode commonCode = commonCodeService.getCommonCode(id);
        return save(generateCmcdReg(request, commonCode, RegistrationType.MODIFY));
    }

    public CommonCodeReg delete(Long id) {
        CommonCode commonCode = commonCodeService.getCommonCode(id);
        return save(generateCmcdReg(commonCode));
    }

    public CommonCodeReg processCommonCodeReg(Long id, ProcessType type) {
        CommonCodeReg commonCodeReg = getCommonCodeReg(id);
        commonCodeReg.getRegistration().setProcessType(type);

        if (type == ProcessType.REJECTED) {
            if (commonCodeReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                commonCodeReg.getCommonCode().setStatus(type);
                commonCodeService.save(commonCodeReg.getCommonCode());
            }
        }
        if (type == ProcessType.APPROVED) {
            if (commonCodeReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                commonCodeReg.getCommonCode().setStatus(type);
                commonCodeService.save(commonCodeReg.getCommonCode());
            }
            if (commonCodeReg.getRegistration().getRegistrationType() == RegistrationType.MODIFY) {
//                CommonCode commonCode = commonCodeService.getCommonCode(commonCodeReg.getCommonCode().getId());
                CommonCode commonCode = commonCodeReg.getCommonCode();
                modelMapper.map(commonCodeReg.getCommonCodeBase(), commonCode);
                commonCode.getWords().clear();
                commonCode.getWords().addAll(commonCodeReg.getWords());
                commonCodeService.save(commonCode);
                commonCodeReg.setCommonCode(commonCode);
            }
            if (commonCodeReg.getRegistration().getRegistrationType() == RegistrationType.DELETE) {
                commonCodeService.delete(commonCodeReg.getCommonCode().getId());
                return null;
            }

        }
        return save(commonCodeReg);
    }

    private CommonCodeReg generateCmcdReg(CommonCode commonCode) {
        return CommonCodeReg.builder()
                .commonCode(commonCode)
                .registration(generateReg(RegistrationType.DELETE))
                .build();
    }

    private CommonCodeReg generateCmcdReg(CmcdRegRequest request, CommonCode commonCode, RegistrationType type) {
        CommonCodeReg map = modelMapper.map(request, CommonCodeReg.class);
        map.setCommonCode(commonCode);
        map.setRegistration(generateReg(type));
        return map;
    }

    private Registration generateReg(RegistrationType type) {
        return Registration.builder()
                .registrant("admin")
                .registrationType(type)
                .processType(ProcessType.UNHANDLED)
                .build();
    }

}
