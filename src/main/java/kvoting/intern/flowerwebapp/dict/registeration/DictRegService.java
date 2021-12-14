package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
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
public class DictRegService {
    private final DictRegRepository dictRegRepository;
    private final DictService dictService;
    private final ModelMapper modelMapper;

    public DictReg save(DictReg dictReg) {
        return dictRegRepository.save(dictReg);
    }

    public boolean exits(Long id) {
        return dictRegRepository.existsById(id);
    }

    public DictReg getDictReg(Long id) {
        return dictRegRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public DictReg create(DictRegistRequest request) {
        Dict mappedDict = modelMapper.map(request, Dict.class);
        mappedDict.setStatus(ProcessType.UNHANDLED);
        mappedDict.setDictRegs(new HashSet<>());
        Dict dict = dictService.save(mappedDict);

        return dictRegRepository.save(generateDictReg(request, dict,
                RegistrationType.CREATE));
    }

    public DictReg modify(DictRegistRequest request, Long id) {
        Dict dict = dictService.getDict(id);
        return dictRegRepository.save(generateDictReg(request, dict,
                RegistrationType.MODIFY));
    }

    public DictReg delete(Long id) {
        Dict dict = dictService.getDict(id);
        return dictRegRepository.save(generateDictReg(dict));
    }

    public DictReg processDictReg(Long id, ProcessType type) {
        DictReg dictReg = getDictReg(id);
        dictReg.getRegistration().setProcessType(type);
        if (type == ProcessType.REJECTED) {
            if (dictReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                dictReg.getDict().setStatus(type);
                dictService.save(dictReg.getDict());
            }
        }
        if (type == ProcessType.APPROVED) {
            if (dictReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                dictReg.getDict().setStatus(type);
                dictService.save(dictReg.getDict());
            }
            if (dictReg.getRegistration().getRegistrationType() == RegistrationType.MODIFY) {
                Dict dict = dictService.getDict(dictReg.getDict().getId());
                modelMapper.map(dictReg.getDictBase(), dict);
                dict.getWords().clear();
                dict.getWords().addAll(dictReg.getWords());
                dict.getDomains().clear();
                dict.getDomains().addAll(dictReg.getDomains());
                dictService.save(dict);
                dictReg.setDict(dict);
            }
            if (dictReg.getRegistration().getRegistrationType() == RegistrationType.DELETE) {
                dictService.delete(dictReg.getDict().getId());
                return null;
            }
        }
        return dictRegRepository.save(dictReg);

    }

    private DictReg generateDictReg(Dict dict) {
        return DictReg.builder()
                .dict(dict)
                .registration(generateReg(RegistrationType.DELETE))
                .build();
    }

    private DictReg generateDictReg(DictRegistRequest request, Dict dict, RegistrationType type) {
        DictReg map = modelMapper.map(request, DictReg.class);
        map.setDict(dict);
        map.setRegistration(generateReg(type));
        return map;
    }

    private Registration generateReg(RegistrationType type) {
        return Registration.builder()
                .registrationType(type)
                .processType(ProcessType.UNHANDLED)
                .build();
    }
}
