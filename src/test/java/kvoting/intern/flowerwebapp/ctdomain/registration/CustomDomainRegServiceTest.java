package kvoting.intern.flowerwebapp.ctdomain.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintReg;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintRegService;
import kvoting.intern.flowerwebapp.constraint.registration.request.ConstraintRegRequest;
import kvoting.intern.flowerwebapp.ctdomain.*;
import kvoting.intern.flowerwebapp.ctdomain.registration.request.CustomDomainRegRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomDomainRegServiceTest {

    @Autowired
    CustomDomainService customDomainService;
    @Autowired
    CustomDomainRegService customDomainRegService;
    @Autowired
    CustomDomainRepository customDomainRepository;
    @Autowired
    CustomDomainRegRepository customDomainRegRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ConstraintRegService constraintRegService;

    @Test
    public void registCreateCustomDomain() throws Throwable {
        Account account = accountRepository.findAll().get(0);
        ConstraintReg constraintReg = (ConstraintReg) constraintRegService.create(generateConstraintRegRequest(), account);
        CustomDomainReg customDomainReg = (CustomDomainReg) customDomainRegService.create(generateCustomDomainRegRequest(List.of(constraintReg.getItem())), account);

        CustomDomain customDomain = customDomainService.getDetail(customDomainReg.getItem().getId());

        for (Constraint constraint : customDomain.getConstraints()) {
            assertThat(constraint).isEqualTo(constraintReg.getItem());
            System.out.println(constraint.getBase().getName());
        }
        System.out.println(customDomain.getBase().getName());
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
                .value("0")
                .build();
    }

    private CustomDomainRegRequest generateCustomDomainRegRequest(List<Constraint> constraints) {
        return CustomDomainRegRequest.builder()
                .base(generateCustomDomainBase())
                .constraints(constraints)
                .build();
    }

    private CustomDomainBase generateCustomDomainBase() {
        return CustomDomainBase.builder()
                .db("권호DB")
                .dataType("KVARCHAR")
                .description("권호 디비에서 쓰이는 문자열")
                .build();
    }
}