package kvoting.intern.flowerwebapp.constraint.registration;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;

@Service
public class ConstraintRegService extends RegistrationService {

	public ConstraintRegService(ConstraintRegRepository constraintRegRepository,
		ModelMapper modelMapper, ConstraintService itemService, AccountService accountService) {
		super(constraintRegRepository, modelMapper, itemService, accountService);
		this.regClazz = ConstraintReg.class;
		this.itemClazz = Constraint.class;
	}

	@Override
	public void setUpItem(Item item, RegRequest regRequest) {

	}

	@Override
	public void validateItem(Item item) {
	}

	@Override
	public void update(Registration registration, Item item) {
		Constraint constraint = (Constraint)item;
		ConstraintReg constraintReg = (ConstraintReg)registration;
		constraint.setBase(constraintReg.getBase());
	}

	@Override
	public void setUpReg(Registration registration, RegRequest request) {
	}
}
