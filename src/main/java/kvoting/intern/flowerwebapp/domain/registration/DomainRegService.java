package kvoting.intern.flowerwebapp.domain.registration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DomainRegService extends RegistrationService<DomainRegistRequest, Domain, DomainBase> {
    public DomainRegService(DomainRegRepository domainRegRepository, ModelMapper modelMapper, DomainService domainService) {
        super(domainRegRepository, modelMapper, domainService);
        this.regClazz = DomainReg.class;
    }

    @Override
    public Domain getItem(Registration registration) {
        return ((DomainReg) registration).getDomain();
    }

    @Override
    public DomainBase getBase(Registration registration) {
        return ((DomainReg) registration).getDomainBase();
    }

    @Override
    public void setItem(Registration registration, Domain item) {
        ((DomainReg) registration).setDomain(item);
    }

    @Override
    public void update(Registration registration, Domain item) {

    }
}
