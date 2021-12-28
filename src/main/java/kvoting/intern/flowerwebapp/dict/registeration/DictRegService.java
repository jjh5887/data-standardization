package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictRegService extends RegistrationService {
    private final WordService wordService;
    private final DomainService domainService;
    private final CustomDomainService customDomainService;
    private final CommonCodeService commonCodeService;

    public DictRegService(DictRegRepository dictRegRepository, ModelMapper modelMapper, DictService dictService,
                          @Lazy WordService wordService,
                          @Lazy DomainService domainService,
                          @Lazy CustomDomainService customDomainService,
                          @Lazy CommonCodeService commonCodeService) {
        super(dictRegRepository, modelMapper, dictService);
        this.wordService = wordService;
        this.domainService = domainService;
        this.customDomainService = customDomainService;
        this.commonCodeService = commonCodeService;
        this.regClazz = DictReg.class;
        this.itemClazz = Dict.class;
    }

    @Override
    public void updateItem(Item item, RegRequest regRequest) {
        DictRegistRequest request = (DictRegistRequest) regRequest;
        Dict dict = (Dict) item;
        dict.setWords(request.getWords().stream().map(id -> (Word) wordService.get(id)).collect(Collectors.toList()));
        dict.setDomains(request.getDomains().stream().map(id -> (Domain) domainService.get(id)).collect(Collectors.toSet()));
        dict.setCustomDomains(request.getCustomDomains().stream().map(id -> (CustomDomain) customDomainService.get(id)).collect(Collectors.toSet()));
        if (request.getCommonCode() != null) {
            dict.setCommonCode((CommonCode) commonCodeService.get(request.getCommonCode()));
        }
    }

    @Override
    public void updateReg(Registration registration, RegRequest regRequest) {
        DictReg dictReg = (DictReg) registration;
        DictRegistRequest request = (DictRegistRequest) regRequest;
        dictReg.setWords(request.getWords().stream().map(id -> (Word) wordService.get(id)).collect(Collectors.toList()));
        dictReg.setDomains(request.getDomains().stream().map(id -> (Domain) domainService.get(id)).collect(Collectors.toSet()));
        dictReg.setCustomDomains(request.getCustomDomains().stream().map(id -> (CustomDomain) customDomainService.get(id)).collect(Collectors.toSet()));
        if (request.getCommonCode() != null) {
            dictReg.setCommonCode((CommonCode) commonCodeService.get(request.getCommonCode()));
        }
    }

    @Override
    public Registration getRegDetail(Long id) {
        DictReg registration = (DictReg) getRegistration(id);
        Hibernate.initialize(registration.getWords());
        Hibernate.initialize(registration.getDomains());
        Hibernate.initialize(registration.getCustomDomains());
        Hibernate.initialize(registration.getCommonCode());
        return registration;
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
    @Transactional(readOnly = true)
    public void validateItem(Item item) {
        List<Item> list = new ArrayList<>(((Dict) item).getWords());
        list.addAll(((Dict) item).getDomains());
        list.addAll(((Dict) item).getCustomDomains());
        if(list.stream().anyMatch(i -> ProcessType.APPROVED != i.getStatus())){
            throw new RuntimeException();
        }
    }
}
