package kvoting.intern.flowerwebapp.domain.registration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
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

import java.util.ArrayList;

@Service
public class DomainRegService extends RegistrationService {
    private final WordRepository wordRepository;

    public DomainRegService(DomainRegRepository domainRegRepository, ModelMapper modelMapper, DomainService domainService, WordRepository wordRepository) {
        super(domainRegRepository, modelMapper, domainService);
        this.wordRepository = wordRepository;
        this.regClazz = DomainReg.class;
        this.itemClazz = Domain.class;
    }

    @Override
    public void updateItem(Item item, RegRequest request) {
        Domain domain = (Domain) item;
        domain.setWords(new ArrayList<>());
        DomainRegistRequest domainRegistRequest = (DomainRegistRequest) request;
        for (Long word : domainRegistRequest.getWords()) {
            domain.getWords().add(wordRepository.findById(word).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
    }

    @Override
    public void updateReg(Registration registration, RegRequest request) {
        DomainReg domainReg = (DomainReg) registration;
        domainReg.setWords(new ArrayList<>());
        DomainRegistRequest domainRegistRequest = (DomainRegistRequest) request;
        for (Long word : domainRegistRequest.getWords()) {
            domainReg.getWords().add(wordRepository.findById(word).orElseThrow(() -> {
                throw new RuntimeException();
            }));
        }
    }

    @Override
    public void update(Registration registration, Item item) {
        DomainReg domainReg = (DomainReg) registration;
        Domain domain = (Domain) item;
        domain.getWords().clear();
        domain.getWords().addAll(domainReg.getWords());
    }

    @Override
    public void validateItem(Item item) {
        Domain domain = (Domain) item;
        for (Word word : domain.getWords()) {
            if (word.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public Registration getRegDetail(Long id) throws Throwable {
        DomainReg domainReg = (DomainReg) getRegistration(id);
        Hibernate.initialize(domainReg.getWords());
        return domainReg;
    }
}
