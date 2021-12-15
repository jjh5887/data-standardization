package kvoting.intern.flowerwebapp.registration;

import kvoting.intern.flowerwebapp.account.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class RegistrationService<R, I, B> {
    protected final RegistrationRepository registrationRepository;
    protected final ModelMapper modelMapper;
    protected final ItemService<I, R> itemService;

    protected Class<? extends Registration> regClazz = Registration.class;

    public Registration save(Registration registration) {
        return (Registration) registrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public Registration getRegistration(Long id) throws Throwable {
        Registration registration = (Registration) registrationRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
        return registration;
    }

    public Registration create(R request, Account account) {
        I item = itemService.map(request);
        item = itemService.save(item);

        return save(generateReg(request, item,
                RegistrationType.CREATE, account));
    }

    public Registration modify(R request, Long id, Account account) {
        I item = itemService.get(id);
        return save(generateReg(request, item,
                RegistrationType.MODIFY, account));
    }

    public Registration delete(Long id, Account account) {
        I item = itemService.get(id);
        return save(generateDelReg(item, account));
    }

    public Registration process(Long id, ProcessType type, Account account) throws Throwable {
        Registration registration = getRegistration(id);
        registration.setProcessType(type);
        registration.setProcessor(account);
        if (type == ProcessType.REJECTED) {
            if (registration.getRegistrationType() == RegistrationType.CREATE) {
                itemService.setStatus(getItem(registration), type);
            }
        }
        if (type == ProcessType.APPROVED) {
            if (registration.getRegistrationType() == RegistrationType.CREATE) {
                itemService.setStatus(getItem(registration), type);
            }
            if (registration.getRegistrationType() == RegistrationType.MODIFY) {
                I item = getItem(registration);
                modelMapper.map(getBase(registration), item);
                update(registration, item);
                item = itemService.save(item);
                setItem(registration, item);
            }
            if (registration.getRegistrationType() == RegistrationType.DELETE) {
                itemService.delete(getItem(registration));
                return null;
            }
        }
        return save(registration);
    }

    public Registration generateReg(R request, I item, RegistrationType type, Account account) {
        Registration registration = modelMapper.map(request, regClazz);
        setItem(registration, item);
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
        setItem(registration, item);
        return registration;
    }

    public abstract I getItem(Registration registration);

    public abstract B getBase(Registration registration);

    public abstract void setItem(Registration registration, I item);

    public abstract void update(Registration registration, I item);

}
