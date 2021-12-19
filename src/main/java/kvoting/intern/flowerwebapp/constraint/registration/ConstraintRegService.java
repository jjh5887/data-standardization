package kvoting.intern.flowerwebapp.constraint.registration;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.registration.request.ConstraintRegRequest;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ConstraintRegService extends RegistrationService<ConstraintRegRequest, Constraint> {

    public ConstraintRegService(ConstraintRegRepository constraintRegRepository, ModelMapper modelMapper, ItemServiceImpl<Constraint> itemService) {
        super(constraintRegRepository, modelMapper, itemService);
        this.regClazz = ConstraintReg.class;
        this.itemClazz = Constraint.class;
    }


    @Override
    public void validateItem(Constraint item) {

    }

    @Override
    public void update(Registration registration, Constraint item) {

    }
}
