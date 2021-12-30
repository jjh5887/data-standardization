package kvoting.intern.flowerwebapp.cmcd.registration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.item.registration.RegistrationController;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;

@Api(tags = "공통코드 요청 API")
@RestController
@RequestMapping(value = "/cmcdReg", produces = MediaType.APPLICATION_JSON_VALUE)
public class CmcdRegController extends RegistrationController<CmcdRegRequest> {
	public CmcdRegController(CommonCodeRegService registrationService, AccountService accountService,
		JwtTokenProvider jwtTokenProvider) {
		super(registrationService, accountService, jwtTokenProvider);
	}
}
