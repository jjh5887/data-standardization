package kvoting.intern.flowerwebapp.item.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {
    protected final RegistrationRepository registrationRepository;
    protected final ModelMapper modelMapper;
    protected final ItemServiceImpl itemServiceImpl;

    protected Class<? extends Registration> regClazz = Registration.class;
    protected Class<? extends Item> itemClazz = Item.class;


    public Registration save(Registration registration) {
        return (Registration) registrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public Page<Registration> getAllRegs(Pageable pageable) {
        return registrationRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Registration getRegistration(Long id) throws Throwable {
        return (Registration) registrationRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Transactional(readOnly = true)
    public Registration getRegDetail(Long id) throws Throwable {
        Registration registration = getRegistration(id);
        Hibernate.initialize(registration.getItem());
        return registration;
    }

    public Registration create(RegRequest request, Account account) {
        Item item = modelMapper.map(request, (Type) itemClazz);
        item.setStatus(ProcessType.UNHANDLED);
        item.setModifier(account);
        item.setModifierName(account.getName());
        item.setModifiedTime(LocalDateTime.now());
        item = itemServiceImpl.save(item);

        Registration save = save(generateReg(request, item,
                RegistrationType.CREATE, account));
        save.setType(item.getClass().getSimpleName().toUpperCase());
        return save;
    }

    public Registration modify(RegRequest request, Long id, Account account) throws Throwable {
        Item item = itemServiceImpl.get(id);
        validateStatus(item);
        return save(generateReg(request, item,
                RegistrationType.MODIFY, account));
    }


    public Registration delete(Long id, Account account) throws Throwable {
        Item item = itemServiceImpl.get(id);
        validateStatus(item);
        return save(generateDelReg(item, account));
    }

    public void cancel(Long id, Account account) throws Throwable {
        Registration registration = getRegistration(id);
        if (registration.getProcessType() == ProcessType.APPROVED ||
                !registration.getRegistrant().equals(account)) {
            throw new RuntimeException();
        }
        if (registration.getRegistrationType() == RegistrationType.CREATE) {
            itemServiceImpl.delete(registration.getItem());
            return;
        }
        registrationRepository.delete(registration);
    }

    public void executeDelete(Long id, Account account) throws Throwable {
        Registration registration = getRegistration(id);
        Item item = registration.getItem();
        item.setModifier(account);
        if (!registration.getRegistrant().equals(account) ||
                item.getStatus() != ProcessType.DELETABLE) {
            throw new RuntimeException();
        }
        itemServiceImpl.delete(item);
    }

    @Transactional
    public Registration process(Long id, ProcessType type, Account account) throws Throwable {
        Registration registration = getRegistration(id);
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
            modelMapper.map(registration.getBase(), item);
            update(registration, item);
        }
        return save(registration);
    }

    public Registration generateReg(RegRequest request, Item item, RegistrationType type, Account account) {
        Registration registration = modelMapper.map(request, regClazz);
        registration.setItem(item);
        registration.setItemName(item.getName());
        registration.setRegistrationType(type);
        registration.setRegistrant(account);
        registration.setProcessType(ProcessType.UNHANDLED);
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
        registration.setItem(item);
        return registration;
    }

    public void validateStatus(Item item) {
        if (!(item.getStatus() == ProcessType.APPROVED)) {
            throw new RuntimeException();
        }
    }

    public void validateItem(Item item) {
    }

    public void update(Registration registration, Item item) {
    }
}
