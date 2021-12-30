package kvoting.intern.flowerwebapp.account;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.account.request.AccountDeleteRequest;
import kvoting.intern.flowerwebapp.account.request.AccountLoginRequest;
import kvoting.intern.flowerwebapp.account.request.AccountUpdateRequest;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationRepository;

@SpringBootTest
class AccountServiceTest {

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	RegistrationRepository registrationRepository;

	@Test
	public void test() {
		accountService.delete(AccountDeleteRequest.builder().password("1234").build(), "test@test.com");
		AccountCreateRequest accountCreateRequest = generateCreateRequest("test@test.com", "1234", "테스트", "테스트 부서");
		Account savedAccount = accountService.create(accountCreateRequest);
		assertThat(accountRepository.count()).isEqualTo(1L);

		assertDoesNotThrow(() -> accountService.login(
			generateLoginRequest(accountCreateRequest.getEmail(), accountCreateRequest.getPassword())));

		Account account = accountService.getAccount(accountCreateRequest.getEmail());
		assertThat(savedAccount).isEqualTo(account);

		AccountUpdateRequest accountUpdateRequest = generateUpdateRequest("test5@test.com", "1234", null, "테스트",
			"테스트 부서");
		accountService.updateAccount(accountUpdateRequest, account.getId());

		assertThat(accountRepository.count()).isEqualTo(1L);
		assertThrows(RuntimeException.class, () -> accountService.getAccount(accountCreateRequest.getEmail()));

		accountService.delete(generateDeleteRequest(accountUpdateRequest.getPassword()),
			accountUpdateRequest.getEmail());
		assertThat(accountRepository.count()).isEqualTo(0L);
		assertThat(registrationRepository.count()).isNotEqualTo(0L);

		for (Object reg : registrationRepository.findAll()) {
			assertThat(((Registration)reg).getRegistrant()).isEqualTo(null);
			assertThat(((Registration)reg).getProcessor()).isEqualTo(null);
		}
	}

	@Test
	public void detailTest() {
		Account account = accountService.getAccount("test@test.com");
		Account detail = accountService.getDetail(account.getId());
		System.out.println(detail.getRegs().size());
		System.out.println(detail.getProcessRegs().size());

		for (Object reg : registrationRepository.findAll()) {
			assertThat(((Registration)reg).getRegistrant()).isEqualTo(account);
			assertThat(((Registration)reg).getProcessor() == null || ((Registration)reg).getProcessor()
				.equals(account)).isEqualTo(true);
		}
	}

	public AccountCreateRequest generateCreateRequest(String email, String password, String name, String department) {
		return AccountCreateRequest.builder()
			.email(email)
			.password(password)
			.name(name)
			.department(department)
			.build();
	}

	public AccountLoginRequest generateLoginRequest(String email, String password) {
		return AccountLoginRequest.builder()
			.email(email)
			.password(password)
			.build();
	}

	public AccountUpdateRequest generateUpdateRequest(String email, String password, String newPassword, String name,
		String department) {
		return AccountUpdateRequest.builder()
			.email(email)
			.password(password)
			.newPassword(newPassword)
			.name(name)
			.department(department)
			.build();
	}

	public AccountDeleteRequest generateDeleteRequest(String password) {
		return AccountDeleteRequest.builder()
			.password(password)
			.build();
	}
}