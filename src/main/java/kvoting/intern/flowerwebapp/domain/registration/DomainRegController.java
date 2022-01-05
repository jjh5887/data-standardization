package kvoting.intern.flowerwebapp.domain.registration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.config.RegUrl;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.item.registration.RegistrationController;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;

@Api(tags = "도메인 요청 API")
@RestController
@RequestMapping(value = RegUrl.DOMAIN_REG, produces = MediaType.APPLICATION_JSON_VALUE)
public class DomainRegController extends RegistrationController<DomainRegistRequest> {
	public DomainRegController(DomainRegService registrationService, AccountService accountService,
		JwtTokenProvider jwtTokenProvider) {
		super(registrationService, accountService, jwtTokenProvider);
	}
}
