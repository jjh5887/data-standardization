package kvoting.intern.flowerwebapp.ctdomain.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintService;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.ctdomain.registration.request.CustomDomainRegRequest;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordService;

@Service
public class CustomDomainRegService extends RegistrationService {
	private final ConstraintService constraintService;
	private final WordService wordService;

	public CustomDomainRegService(CustomDomainRegRepository customDomainRegRepository, ModelMapper modelMapper,
		CustomDomainService customDomainService,
		@Lazy ConstraintService constraintService, @Lazy WordService wordService, AccountService accountService) {
		super(customDomainRegRepository, modelMapper, customDomainService, accountService);
		this.constraintService = constraintService;
		this.wordService = wordService;
		this.regClazz = CustomDomainReg.class;
		this.itemClazz = CustomDomain.class;
	}

	@Override
	public void update(Registration registration, Item item) {
		CustomDomain customDomain = (CustomDomain)item;
		CustomDomainReg customDomainReg = (CustomDomainReg)registration;
		customDomain.getConstraints().clear();
		customDomain.getConstraints().addAll(customDomainReg.getConstraints());
		customDomain.setBase(customDomainReg.getBase());
	}

	@Override
	public void setUpReg(Registration registration, RegRequest regRequest) {
		CustomDomainRegRequest request = (CustomDomainRegRequest)regRequest;
		CustomDomainReg customDomainReg = (CustomDomainReg)registration;
		customDomainReg.setWords(request.getWords()
			.stream()
			.map(id -> (Word)wordService.get(id))
			.collect(Collectors.toList()));
		customDomainReg.setConstraints(request.getConstraints()
			.stream()
			.map(id -> (Constraint)constraintService.get(id))
			.collect(Collectors.toSet()));
	}

	@Override
	public void setUpItem(Item item, RegRequest regRequest) {
		CustomDomainRegRequest request = (CustomDomainRegRequest)regRequest;
		CustomDomain customDomain = (CustomDomain)item;
		customDomain.setWords(request.getWords()
			.stream()
			.map(id -> (Word)wordService.get(id))
			.collect(Collectors.toList()));
		customDomain.setConstraints(request.getConstraints()
			.stream()
			.map(id -> (Constraint)constraintService.get(id))
			.collect(Collectors.toSet()));
	}

	@Override
	public void validateItem(Item item) {
		CustomDomain customDomain = (CustomDomain)item;
		List<Item> list = new ArrayList<>(customDomain.getWords());
		list.addAll(customDomain.getConstraints());
		if (list.stream().anyMatch(i -> ProcessType.APPROVED != i.getStatus())) {
			throw new RuntimeException();
		}
	}
}
