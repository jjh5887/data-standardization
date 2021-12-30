package kvoting.intern.flowerwebapp.constraint.registration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.constraint.registration.request.ConstraintRegRequest;
import kvoting.intern.flowerwebapp.item.registration.RegistrationController;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;

@Api(tags = "제약사항 요청 API", description = "커스텀 도메인에서 사용되는 제약사항의 요청")
@Controller
@RequestMapping(value = "/constraint-reg", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConstraintRegController extends RegistrationController<ConstraintRegRequest> {
	public ConstraintRegController(ConstraintRegService registrationService,
		AccountService accountService,
		JwtTokenProvider jwtTokenProvider) {
		super(registrationService, accountService, jwtTokenProvider);
	}
}
