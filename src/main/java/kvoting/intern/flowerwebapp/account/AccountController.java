package kvoting.intern.flowerwebapp.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.account.request.AccountDeleteRequest;
import kvoting.intern.flowerwebapp.account.request.AccountLoginRequest;
import kvoting.intern.flowerwebapp.account.request.AccountUpdateRequest;
import kvoting.intern.flowerwebapp.account.response.*;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "사용자 인증 API")
@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "사용자 인증 토큰 발급(로그인)")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AccountLoginRequest accountLoginRequest) {
        accountService.login(accountLoginRequest);
        String token = jwtTokenProvider.createToken(accountLoginRequest.getEmail());
        return ResponseEntity.ok(AccountLoginResponse.builder().token(token).build());
    }

    @ApiOperation(value = "회원 가입")
    @PostMapping
    public ResponseEntity create(@RequestBody AccountCreateRequest accountCreateRequest) {
        Account account = accountService.create(accountCreateRequest);
        return ResponseEntity.ok(modelMapper.map(account, AccountResponse.class));
    }

    @ApiOperation(value = "회원 정보 조회(토큰 기반)")
    @GetMapping
    public ResponseEntity get(HttpServletRequest request) {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        return ResponseEntity.ok(modelMapper.map(account, AccountResponse.class));
    }

    @ApiOperation(value = "회원 상세 정보 조회(토큰 기반)", notes = "사용자가 가지는 모든 요청 정보 포함된 정보를 제공")
    @GetMapping("/detail")
    public ResponseEntity getDetail(HttpServletRequest request) {
        Account detail = accountService.getDetail(jwtTokenProvider.getUserEmail(request));
        return ResponseEntity.ok(modelMapper.map(detail, AccountDetailResponse.class));
    }

    @ApiOperation(value = "이메일 중복 확인")
    @ApiParam(name = "email", value = "이메일")
    @GetMapping("/{email}")
    public ResponseEntity exist(@PathVariable String email) {
        boolean exist = accountService.exist(email);
        return ResponseEntity.ok(AccountDuplicateResponse.builder().isDuplicate(exist).build());
    }

    @ApiOperation(value = "회원 정보 수정(토큰 기반)")
    @PutMapping
    public ResponseEntity update(@RequestBody AccountUpdateRequest accountUpdateRequest, HttpServletRequest request) {
        Account account = accountService.updateAccount(accountUpdateRequest, jwtTokenProvider.getUserEmail(request));
        return ResponseEntity.ok(modelMapper.map(account, AccountResponse.class));
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity delete(@RequestBody AccountDeleteRequest accountDeleteRequest, HttpServletRequest request) {
        accountService.delete(accountDeleteRequest, jwtTokenProvider.getUserEmail(request));
        return ResponseEntity.ok(AccountDeleteResponse.builder().message("ok").build());
    }
}
