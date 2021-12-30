package kvoting.intern.flowerwebapp.word.registration;

import io.swagger.annotations.Api;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.item.registration.RegistrationController;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "단어 요청 API")
@RestController
@RequestMapping(value = "/word-reg", produces = MediaType.APPLICATION_JSON_VALUE)
public class WordRegController extends RegistrationController<WordRegistRequest> {
    public WordRegController(WordRegService registrationService, AccountService accountService, JwtTokenProvider jwtTokenProvider) {
        super(registrationService, accountService, jwtTokenProvider);
    }
}