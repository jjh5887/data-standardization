package kvoting.intern.flowerwebapp.ctdomain.registration;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.constraint.InputType;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintReg;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintRegService;
import kvoting.intern.flowerwebapp.constraint.registration.request.ConstraintRegRequest;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainRepository;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.ctdomain.registration.request.CustomDomainRegRequest;

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
	public void registCreateCustomDomain() {
		Account account = accountRepository.findAll().get(0);
		ConstraintReg constraintReg = (ConstraintReg)constraintRegService.create(generateConstraintRegRequest(),
			account.getEmail());
		CustomDomainReg customDomainReg = (CustomDomainReg)customDomainRegService.create(
			generateCustomDomainRegRequest(Set.of(constraintReg.getItem().getId())), account.getEmail());

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
			.name("?????? ?????? ??? ???????")
			.description("?????????")
			.inputType(InputType.BOOLEAN)
			.value("0")
			.build();
	}

	private CustomDomainRegRequest generateCustomDomainRegRequest(Set<Long> constraints) {
		return CustomDomainRegRequest.builder()
			.base(generateCustomDomainBase())
			.constraints(constraints)
			.build();
	}

	private CustomDomainBase generateCustomDomainBase() {
		return CustomDomainBase.builder()
			.db("??????DB")
			.dataType("KVARCHAR")
			.description("?????? ???????????? ????????? ?????????")
			.build();
	}
}