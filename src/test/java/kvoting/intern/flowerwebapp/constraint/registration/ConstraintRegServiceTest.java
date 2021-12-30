package kvoting.intern.flowerwebapp.constraint.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.constraint.ConstraintRepository;
import kvoting.intern.flowerwebapp.constraint.ConstraintService;
import kvoting.intern.flowerwebapp.constraint.registration.request.ConstraintRegRequest;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainRepository;
import kvoting.intern.flowerwebapp.constraint.InputType;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConstraintRegServiceTest {
    @Autowired
    ConstraintRegService constraintRegService;
    @Autowired
    ConstraintRegRepository constraintRegRepository;
    @Autowired
    ConstraintService constraintService;
    @Autowired
    ConstraintRepository constraintRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomDomainRepository customDomainRepository;
    @Autowired
    DictRepository dictRepository;

    @BeforeEach
    public void setUp() {
        dictRepository.deleteAll();
        customDomainRepository.deleteAll();
        constraintRepository.deleteAll();
    }

    @Test
    public void registCreateConstraint() throws Throwable {
        Account account = accountRepository.findAll().get(0);
        ConstraintRegRequest request = generateConstraintRegRequest();
        ConstraintReg registration = (ConstraintReg) constraintRegService.create(request, account);

        assertThat(constraintRepository.count()).isEqualTo(1L);
        assertThat(constraintRegRepository.count()).isEqualTo(1L);
        Constraint constraint = (Constraint) constraintService.get(registration.getItem().getId());
        assertThat(constraint.getStatus()).isEqualTo(ProcessType.UNHANDLED);
        registration = (ConstraintReg) constraintRegService.getRegistration(registration.getId());
        assertThat(registration.getBase().getDescription()).isEqualTo(request.getBase().getDescription());
        System.out.println(registration.getBase().getName());
    }

    private ConstraintRegRequest generateConstraintRegRequest() {
        return ConstraintRegRequest.builder()
                .base(generateConstraintBase())
                .build();
    }

    private ConstraintBase generateConstraintBase() {
        return ConstraintBase.builder()
                .name("이거 먹을 수 있음?")
                .description("내꺼임")
                .inputType(InputType.BOOLEAN)
                .value("응애")
                .build();
    }

}