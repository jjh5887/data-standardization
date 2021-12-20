package kvoting.intern.flowerwebapp.domain.registration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DomainRegService extends RegistrationService {
    public DomainRegService(DomainRegRepository domainRegRepository, ModelMapper modelMapper, DomainService domainService) {
        super(domainRegRepository, modelMapper, domainService);
        this.regClazz = DomainReg.class;
        this.itemClazz = Domain.class;
    }


    @Override
    public void validateItem(Item item) {

    }

    @Override
    public void update(Registration registration, Item item) {

    }
}
