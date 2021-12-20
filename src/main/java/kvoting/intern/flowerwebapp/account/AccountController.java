package kvoting.intern.flowerwebapp.account;

import kvoting.intern.flowerwebapp.account.request.AccountLoginRequest;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AccountLoginRequest request) {
        if (accountService.login(request)) {
            return ResponseEntity.ok(jwtTokenProvider.createToken(request.getEmail()));
        }
        return ResponseEntity.badRequest().build();
    }

}
