package kvoting.intern.flowerwebapp.dict.registeration;

import io.swagger.annotations.Api;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.item.registration.RegistrationController;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "용어 요청 API")
@RestController
@RequestMapping(value = "/dictReg", produces = MediaType.APPLICATION_JSON_VALUE)
public class DictRegController extends RegistrationController<DictRegistRequest> {
    public DictRegController(DictRegService registrationService, AccountService accountService, JwtTokenProvider jwtTokenProvider) {
        super(registrationService, accountService, jwtTokenProvider);
    }
}
