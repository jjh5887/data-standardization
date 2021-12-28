package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.cmcd.CommonCodeRepository;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainRepository;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class DictRegService extends RegistrationService {
    private final WordRepository wordRepository;
    private final DomainRepository domainRepository;
    private final CustomDomainRepository customDomainRepository;
    private final CommonCodeRepository commonCodeRepository;

    public DictRegService(DictRegRepository dictRegRepository, ModelMapper modelMapper, DictService dictService, WordRepository wordRepository, DomainRepository domainRepository, CustomDomainRepository customDomainRepository, CommonCodeRepository commonCodeRepository) {
        super(dictRegRepository, modelMapper, dictService);
        this.wordRepository = wordRepository;
        this.domainRepository = domainRepository;
        this.customDomainRepository = customDomainRepository;
        this.commonCodeRepository = commonCodeRepository;
        this.regClazz = DictReg.class;
        this.itemClazz = Dict.class;
    }

    @Override
    public void updateItem(Item item, RegRequest request) {
        DictRegistRequest registRequest = (DictRegistRequest) request;
        Dict dict = (Dict) item;
        dict.setWords(new ArrayList<>());
        dict.setDomains(new HashSet<>());
        dict.setCustomDomains(new HashSet<>());
        for (Long id : registRequest.getWords()) {
            dict.getWords().add(wordRepository.findById(id).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
        for (Long id : registRequest.getDomains()) {
            dict.getDomains().add(domainRepository.findById(id).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
        for (Long id : registRequest.getCustomDomains()) {
            dict.getCustomDomains().add(customDomainRepository.findById(id).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
        if (registRequest.getCommonCode() != null) {
            dict.setCommonCode(commonCodeRepository.findById(registRequest.getCommonCode()).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
    }

    @Override
    public void updateReg(Registration registration, RegRequest request) {
        DictReg dictReg = (DictReg) registration;
        DictRegistRequest registRequest = (DictRegistRequest) request;
        dictReg.setWords(new ArrayList<>());
        dictReg.setDomains(new HashSet<>());
        dictReg.setCustomDomains(new HashSet<>());
        for (Long id : registRequest.getWords()) {
            dictReg.getWords().add(wordRepository.findById(id).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
        for (Long id : registRequest.getDomains()) {
            dictReg.getDomains().add(domainRepository.findById(id).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
        for (Long id : registRequest.getCustomDomains()) {
            dictReg.getCustomDomains().add(customDomainRepository.findById(id).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
        if (registRequest.getCommonCode() != null) {
            dictReg.setCommonCode(commonCodeRepository.findById(registRequest.getCommonCode()).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
    }

    @Override
    public Registration getRegDetail(Long id) throws Throwable {
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
        for (Word word : ((Dict) item).getWords()) {
            if (word.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
        for (Domain domain : ((Dict) item).getDomains()) {
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
