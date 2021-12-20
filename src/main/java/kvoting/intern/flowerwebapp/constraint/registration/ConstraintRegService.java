package kvoting.intern.flowerwebapp.constraint.registration;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ConstraintRegService extends RegistrationService {

    public ConstraintRegService(ConstraintRegRepository constraintRegRepository, ModelMapper modelMapper, ConstraintService itemService) {
        super(constraintRegRepository, modelMapper, itemService);
        this.regClazz = ConstraintReg.class;
        this.itemClazz = Constraint.class;
    }


    @Override
    public void validateItem(Item item) {

    }

    @Override
    public void update(Registration registration, Item item) {

    }
}
