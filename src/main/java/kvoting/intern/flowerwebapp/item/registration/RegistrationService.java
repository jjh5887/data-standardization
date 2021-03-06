package kvoting.intern.flowerwebapp.item.registration;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.exception.WebException;
import kvoting.intern.flowerwebapp.exception.code.AccountErrorCode;
import kvoting.intern.flowerwebapp.exception.code.RegistrationErrorCode;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class RegistrationService {
	protected final RegistrationRepository registrationRepository;
	protected final ModelMapper modelMapper;
	protected final ItemServiceImpl itemServiceImpl;
	@Lazy
	protected final AccountService accountService;

	protected Class<? extends Registration> regClazz = Registration.class;
	protected Class<? extends Item> itemClazz = Item.class;

	public Registration save(Registration registration) {
		return (Registration)registrationRepository.save(registration);
	}

	@Transactional(readOnly = true)
	public Page getAllRegs(Pageable pageable) {
		return registrationRepository.findAll(pageable);
	}

	@SneakyThrows
	@Transactional(readOnly = true)
	public Registration getRegistration(Long id) {
		return (Registration)registrationRepository.findById(id).orElseThrow(() -> {
			throw new WebException(RegistrationErrorCode.RegistrationNotFound);
		});
	}

	@Transactional(readOnly = true)
	public Registration getRegDetail(Long id) {
		Registration registration = getRegistration(id);
		Hibernate.initialize(registration.getItem());
		return registration;
	}

	@SneakyThrows
	public Registration create(RegRequest request, String email) {
		Account account = accountService.getAccount(email);
		Item item = modelMapper.map(request, (Type)itemClazz);
		item.setStatus(ProcessType.UNHANDLED);
		item.setModifier(account);
		item.setModifierName(account.getName());
		item.setModifiedTime(LocalDateTime.now());
		setUpItem(item, request);
		item = itemServiceImpl.create(item);

		Registration save = save(generateReg(request, item,
			RegistrationType.CREATE, account));
		save.setType(item.getClass().getSimpleName().toUpperCase());
		return save;
	}

	public Registration modify(RegRequest request, Long id, String email) {
		Account account = accountService.getAccount(email);
		Item item = itemServiceImpl.get(id);
		validateStatus(item);

		Registration save = generateReg(request, item,
			RegistrationType.MODIFY, account);
		save.setType(item.getClass().getSimpleName().toUpperCase());
		return save(save);
	}

	public Registration delete(Long id, String email) {
		Account account = accountService.getAccount(email);
		Item item = itemServiceImpl.get(id);
		validateStatus(item);
		Registration save = save(generateDelReg(item, account));
		save.setType(item.getClass().getSimpleName().toUpperCase());
		return save;
	}

	public void cancel(Long id, String email) {
		Account account = accountService.getAccount(email);
		Registration registration = getRegistration(id);
		if (registration.getProcessType() == ProcessType.APPROVED) {
			throw new WebException(RegistrationErrorCode.RegistrationAlreadyProcessed);
		}
		if (!registration.getRegistrant().equals(account)) {
			throw new WebException(AccountErrorCode.Unauthorized);
		}
		if (registration.getRegistrationType() == RegistrationType.CREATE) {
			itemServiceImpl.delete(registration.getItem());
			return;
		}
		registrationRepository.delete(registration);
	}

	public void executeDelete(Long id, String email) {
		Account account = accountService.getAccount(email);
		Registration registration = getRegistration(id);
		Item item = registration.getItem();
		item.setModifier(account);
		if (!registration.getRegistrant().equals(account)) {
			throw new WebException(AccountErrorCode.Unauthorized);
		}
		if (item.getStatus() != ProcessType.DELETABLE) {
			throw new WebException(RegistrationErrorCode.NotDeletableItem);
		}
		itemServiceImpl.delete(item);
	}

	@Transactional
	public Registration process(Long id, ProcessType type, String email) {
		Account account = accountService.getAccount(email);
		Registration registration = getRegistration(id);
		if (registration.getProcessType() != ProcessType.UNHANDLED) {
			throw new WebException(RegistrationErrorCode.RegistrationAlreadyProcessed);
		}
		registration.setProcessType(type);
		registration.setProcessor(account);

		if (type == ProcessType.APPROVED) {
			return approve(registration);
		}
		return reject(registration);
	}

	public Registration reject(Registration registration) {
		if (registration.getRegistrationType() == RegistrationType.CREATE) {
			registration.getItem().setStatus(ProcessType.REJECTED);
		}
		return save(registration);
	}

	@Transactional
	public Registration approve(Registration registration) {
		registration.getItem().setModifier(registration.getRegistrant());
		registration.getItem().setModifierName(registration.getRegistrant().getName());
		registration.getItem().setModifiedTime(LocalDateTime.now());
		if (registration.getRegistrationType() == RegistrationType.DELETE) {
			registration.getItem().setStatus(ProcessType.DELETABLE);
			return save(registration);
		}
		validateItem(registration.getItem());
		registration.getItem().setStatus(ProcessType.APPROVED);
		if (registration.getRegistrationType() == RegistrationType.MODIFY) {
			Item item = registration.getItem();
			update(registration, item);
			itemServiceImpl.update(item);
		}
		return save(registration);
	}

	public Registration generateReg(RegRequest request, Item item, RegistrationType type, Account account) {
		Registration registration = modelMapper.map(request, regClazz);
		registration.setItem(item);
		;
		registration.setItemId(item.getId());
		registration.setItemName(item.getName());
		registration.setRegistrationType(type);
		registration.setRegistrant(account);
		registration.setProcessType(ProcessType.UNHANDLED);
		setUpReg(registration, request);
		if (exists(registration)) {
			throw new WebException(RegistrationErrorCode.DuplicatedRegistration);
		}
		return registration;
	}

	public Registration generateDelReg(Item item, Account account) {
		Registration registration = Registration.builder()
			.registrationType(RegistrationType.DELETE)
			.registrant(account)
			.itemName(item.getName())
			.processType(ProcessType.UNHANDLED)
			.build();
		registration = modelMapper.map(registration, regClazz);
		registration.setItemId(item.getId());
		registration.setItemName(item.getName());
		registration.setItem(item);
		return registration;
	}

	public void validateStatus(Item item) {
		if (!(item.getStatus() == ProcessType.APPROVED)) {
			throw new WebException(RegistrationErrorCode.ItemNotApproved);
		}
	}

	public abstract void setUpItem(Item item, RegRequest regRequest);

	public abstract void validateItem(Item item);

	public abstract void update(Registration registration, Item item);

	public abstract void setUpReg(Registration registration, RegRequest request);

	public boolean exists(Registration registration) {
		return registrationRepository
			.existsByIdAndItemId(registration.getId(), registration.getItemId());
	}
}
