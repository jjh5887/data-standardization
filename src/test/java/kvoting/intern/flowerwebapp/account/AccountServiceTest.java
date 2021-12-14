package kvoting.intern.flowerwebapp.account;

import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.account.request.AccountDeleteRequest;
import kvoting.intern.flowerwebapp.account.request.AccountLoginRequest;
import kvoting.intern.flowerwebapp.account.request.AccountUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void test() {
        AccountCreateRequest accountCreateRequest = generateCreateRequest("test@test.com", "1234", "테스트", "테스트 부서");
        Account savedAccount = accountService.create(accountCreateRequest);
        assertThat(accountRepository.count()).isEqualTo(1L);

        boolean login = accountService.login(generateLoginRequest(accountCreateRequest.getEmail(), accountCreateRequest.getPassword()));
        assertThat(login).isEqualTo(true);

        Account account = accountService.getAccount(accountCreateRequest.getEmail());
        assertThat(savedAccount).isEqualTo(account);

        AccountUpdateRequest accountUpdateRequest = generateUpdateRequest("test5@test.com", "1234", null, "테스트", "테스트 부서");
        accountService.updateAccount(accountUpdateRequest, account.getId());

        assertThat(accountRepository.count()).isEqualTo(1L);
        assertThrows(RuntimeException.class, () -> {
            accountService.getAccount(accountCreateRequest.getEmail());
        });

        accountService.delete(generateDeleteRequest(accountUpdateRequest.getPassword()), accountUpdateRequest.getEmail());
        assertThat(accountRepository.count()).isEqualTo(0L);

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

    public AccountUpdateRequest generateUpdateRequest(String email, String password, String newPassword, String name, String department) {
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