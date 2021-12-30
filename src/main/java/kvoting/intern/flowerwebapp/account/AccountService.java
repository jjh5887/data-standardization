package kvoting.intern.flowerwebapp.account;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.account.request.AccountDeleteRequest;
import kvoting.intern.flowerwebapp.account.request.AccountLoginRequest;
import kvoting.intern.flowerwebapp.account.request.AccountUpdateRequest;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
		if (exist(request.getEmail())) {
			throw new RuntimeException();
		}
		Account account = modelMapper.map(request, Account.class);
		account.addRole(AccountRole.USER);
		account.encodePassword(passwordEncoder);
		return save(account);
	}

	@Transactional(readOnly = true)
	public boolean exist(String email) {
		return accountRepository.existsByEmail(email);
	}

	@Transactional(readOnly = true)
	public Account getDetail(Long id) {
		Account account = getAccount(id);
		initialize(account);
		return account;
	}

	@Transactional(readOnly = true)
	public Account getDetail(String email) {
		Account account = getAccount(email);
		initialize(account);
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
		return update(request, account);
	}

	public Account updateAccount(AccountUpdateRequest request, String email) {
		Account account = getAccount(email);
		return update(request, account);
	}

	@Transactional(readOnly = true)
	public void login(AccountLoginRequest request) {
		Account account = getAccount(request.getEmail());
		verifyPassword(account, request.getPassword());
	}

	public void delete(AccountDeleteRequest request, String email) {
		Account account = getAccount(email);
		verifyPassword(account, request.getPassword());
		for (Registration reg : account.getRegs()) {
			reg.setRegistrant(null);
		}
		for (Registration processReg : account.getProcessRegs()) {
			processReg.setProcessor(null);
		}
		for (Word word : account.getWords()) {
			word.setModifier(null);
		}
		for (Domain domain : account.getDomains()) {
			domain.setModifier(null);
		}
		for (Dict dict : account.getDicts()) {
			dict.setModifier(null);
		}
		for (CommonCode commonCode : account.getCommonCodes()) {
			commonCode.setModifier(null);
		}
		for (Constraint constraint : account.getConstraints()) {
			constraint.setModifier(null);
		}
		for (CustomDomain customDomain : account.getCustomDomains()) {
			customDomain.setModifier(null);
		}

		accountRepository.delete(account);
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return getAccount(email);
	}

	private void verifyPassword(Account account, String password) {
		if (!passwordEncoder.matches(password, account.getPassword())) {
			throw new RuntimeException();
		}
	}

	private void initialize(Account account) {
		Hibernate.initialize(account.getRegs());
		Hibernate.initialize(account.getProcessRegs());
	}

	private Account update(AccountUpdateRequest request, Account account) {
		verifyPassword(account, request.getPassword());
		modelMapper.map(request, account);
		if (request.getNewPassword() != null)
			account.setPassword(request.getNewPassword());
		account.encodePassword(passwordEncoder);
		return save(account);
	}
}
