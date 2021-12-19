package kvoting.intern.flowerwebapp.account;

import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.account.request.AccountDeleteRequest;
import kvoting.intern.flowerwebapp.account.request.AccountLoginRequest;
import kvoting.intern.flowerwebapp.account.request.AccountUpdateRequest;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account create(AccountCreateRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException();
        }
        Account account = modelMapper.map(request, Account.class);
        account.addRole(AccountRole.USER);
        account.encodePassword(passwordEncoder);
        return save(account);
    }

    @Transactional(readOnly = true)
    public Account getDetail(Long id) {
        Account account = getAccount(id);
        Hibernate.initialize(account.getRegs());
        Hibernate.initialize(account.getProcessRegs());
        return account;
    }

    @Transactional(readOnly = true)
    public Account getAccount(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Account updateAccount(AccountUpdateRequest request, Long id) {
        Account account = getAccount(id);
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException();
        }
        modelMapper.map(request, account);
        if (request.getNewPassword() != null)
            account.setPassword(request.getNewPassword());
        account.encodePassword(passwordEncoder);
        return save(account);
    }

    @Transactional(readOnly = true)
    public boolean login(AccountLoginRequest request) {
        Account account = getAccount(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return false;
        }
        return true;
    }

    public void delete(AccountDeleteRequest request, String email) {
        Account account = getAccount(email);
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException();
        }
        for (Registration reg : account.getRegs()) {
            reg.setRegistrant(null);
        }
        for (Registration processReg : account.getProcessRegs()) {
            processReg.setProcessor(null);
        }

        accountRepository.delete(account);
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getAccount(email);
    }
}