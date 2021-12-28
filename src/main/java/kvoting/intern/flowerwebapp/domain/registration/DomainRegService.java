package kvoting.intern.flowerwebapp.domain.registration;

import java.util.stream.Collectors;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
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

@Service
public class DomainRegService extends RegistrationService {

    private final WordService wordService;

    public DomainRegService(DomainRegRepository domainRegRepository, ModelMapper modelMapper,
        DomainService domainService, @Lazy WordService wordService) {
        super(domainRegRepository, modelMapper, domainService);
        this.wordService = wordService;
        this.regClazz = DomainReg.class;
        this.itemClazz = Domain.class;
    }

    @Override
    public void updateItem(Item item, RegRequest regRequest) {
        Domain domain = (Domain) item;
        DomainRegistRequest request = (DomainRegistRequest) regRequest;
        domain.setWords(request.getWords().stream().map(id -> (Word) wordService.get(id)).collect(
            Collectors.toList()));
    }

    @Override
    public void updateReg(Registration registration, RegRequest regRequest) {
        DomainReg domainReg = (DomainReg) registration;
        DomainRegistRequest request = (DomainRegistRequest) regRequest;
        domainReg.setWords(
            request.getWords().stream().map(id -> (Word) wordService.get(id)).collect(
                Collectors.toList()));
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
        if (domain.getWords().stream().anyMatch(word -> ProcessType.APPROVED != word.getStatus())) {
            throw new RuntimeException();
        }
    }

    @Override
    public Registration getRegDetail(Long id) {
        DomainReg domainReg = (DomainReg) getRegistration(id);
        Hibernate.initialize(domainReg.getWords());
        return domainReg;
    }
}
