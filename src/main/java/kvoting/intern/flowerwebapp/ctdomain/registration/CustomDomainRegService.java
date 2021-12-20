package kvoting.intern.flowerwebapp.ctdomain.registration;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomDomainRegService extends RegistrationService {
    public CustomDomainRegService(CustomDomainRegRepository customDomainRegRepository, ModelMapper modelMapper, CustomDomainService customDomainService) {
        super(customDomainRegRepository, modelMapper, customDomainService);
        this.regClazz = CustomDomainReg.class;
        this.itemClazz = CustomDomain.class;
    }

    @Override
    public void update(Registration registration, Item item) {
        ((CustomDomain) item).getConstraints().clear();
        ((CustomDomain) item).getConstraints().addAll(((CustomDomainReg) registration).getConstraints());
    }

    @Override
    public void validateItem(Item item) {
        for (Constraint constraint : ((CustomDomain) item).getConstraints()) {
            if (constraint.getStatus() != ProcessType.APPROVED) {
                throw new RuntimeException();
            }
        }
    }
}
