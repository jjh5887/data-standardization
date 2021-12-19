package kvoting.intern.flowerwebapp.item.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class RegistrationService<R, I extends Item> {
    protected final RegistrationRepository registrationRepository;
    protected final ModelMapper modelMapper;
    protected final ItemServiceImpl<I> itemService;

    protected Class<? extends Registration> regClazz = Registration.class;
    protected Class<? extends Item> itemClazz;

    public Registration save(Registration registration) {
        return (Registration) registrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public Registration getRegistration(Long id) throws Throwable {
        return (Registration) registrationRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Registration create(R request, Account account) {
        I item = modelMapper.map(request, (Type) itemClazz);
        item.setStatus(ProcessType.UNHANDLED);
        item = itemService.save(item);

        return save(generateReg(request, item,
                RegistrationType.CREATE, account));
    }

    public Registration modify(R request, Long id, Account account) {
        I item = itemService.get(id);
        validateStatus(item);
        return save(generateReg(request, item,
                RegistrationType.MODIFY, account));
    }

    public Registration delete(Long id, Account account) {
        I item = itemService.get(id);
        validateStatus(item);
        return save(generateDelReg(item, account));
    }

    public void cancel(Long id, Account account) throws Throwable {
        Registration registration = getRegistration(id);
        if (registration.getProcessType() == ProcessType.APPROVED ||
                registration.getRegistrant() != account) {
            throw new RuntimeException();
        }
        if (registration.getRegistrationType() == RegistrationType.CREATE) {
            itemService.delete((I) registration.getItem());
            return;
        }
        registrationRepository.delete(registration);
    }

    public void executeDelete(Long id, Account account) throws Throwable {
        Registration registration = getRegistration(id);
        I item = (I) registration.getItem();
        if (!registration.getRegistrant().equals(account) ||
                item.getStatus() != ProcessType.DELETABLE) {
            throw new RuntimeException();
        }
        itemService.delete(item);
    }

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

    public Registration approve(Registration registration) {
        if (registration.getRegistrationType() == RegistrationType.DELETE) {
            registration.getItem().setStatus(ProcessType.DELETABLE);
            return save(registration);
        }
        validateItem((I) registration.getItem());
        registration.getItem().setStatus(ProcessType.APPROVED);
        if (registration.getRegistrationType() == RegistrationType.MODIFY) {
            I item = (I) registration.getItem();
            modelMapper.map(registration.getBase(), item);
            update(registration, item);
        }
        return save(registration);
    }

    public Registration generateReg(R request, I item, RegistrationType type, Account account) {
        Registration registration = modelMapper.map(request, regClazz);
        registration.setItem(item);
        registration.setRegistrationType(type);
        registration.setRegistrant(account);
        registration.setProcessType(ProcessType.UNHANDLED);
        return registration;
    }

    public Registration generateDelReg(I item, Account account) {
        Registration registration = Registration.builder()
                .registrationType(RegistrationType.DELETE)
                .registrant(account)
                .processType(ProcessType.UNHANDLED)
                .build();
        registration = modelMapper.map(registration, regClazz);
        registration.setItem(item);
        return registration;
    }

    public void validateStatus(I item) {
        if (!(item.getStatus() == ProcessType.APPROVED)) {
            throw new RuntimeException();
        }
    }

    public abstract void validateItem(I item);

    public abstract void update(Registration registration, I item);
}
