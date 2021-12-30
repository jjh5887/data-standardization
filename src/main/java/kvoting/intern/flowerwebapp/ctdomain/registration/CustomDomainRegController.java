package kvoting.intern.flowerwebapp.ctdomain.registration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.ctdomain.registration.request.CustomDomainRegRequest;
import kvoting.intern.flowerwebapp.item.registration.RegistrationController;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;

@Api(tags = "커스텀 도메인 요청 API")
@Controller
@RequestMapping(value = "/custom-domain-reg", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomDomainRegController extends RegistrationController<CustomDomainRegRequest> {
	public CustomDomainRegController(CustomDomainRegService registrationService,
		AccountService accountService,
		JwtTokenProvider jwtTokenProvider) {
		super(registrationService, accountService, jwtTokenProvider);
	}
}
