package kvoting.intern.flowerwebapp.item.registration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;

@Primary
@Service
public class RegServiceImpl extends RegistrationService {
	public RegServiceImpl(RegistrationRepository registrationRepository, ModelMapper modelMapper,
		ItemServiceImpl itemServiceImpl,
		AccountService accountService) {
		super(registrationRepository, modelMapper, itemServiceImpl, accountService);
	}

	@Override
	public void setUpItem(Item item, RegRequest regRequest) {

	}

	@Override
	public void validateItem(Item item) {

	}

	@Override
	public void update(Registration registration, Item item) {

	}

	@Override
	public void setUpReg(Registration registration, RegRequest request) {

	}
}
