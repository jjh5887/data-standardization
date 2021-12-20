package kvoting.intern.flowerwebapp.item.registration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api("Registration API Controller")
@RestController
@RequestMapping(value = "/reg", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RegistrationController<R extends RegRequest> {
    protected final RegistrationService registrationService;
    protected final AccountService accountService;
    protected final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "id로 등록된 요청 조회", notes = "요청 정보")
    @ApiImplicitParam(name = "name", value = "요청 id")
    @GetMapping("/{id}")
    public ResponseEntity getReg(@PathVariable Long id) throws Throwable {
        return ResponseEntity.ok(registrationService.getRegistration(id));
    }

    @ApiOperation(value = "생성 요청 등록", notes = "등록된 요청")
    @PostMapping
    public ResponseEntity createReg(@RequestBody @ApiParam(value = "생성 내용") R regRequest, @ApiParam(value = "인증 토큰 확인") HttpServletRequest request) {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        Registration registration = registrationService.create(regRequest, account);
        return ResponseEntity.ok(registration);
    }

    @ApiOperation(value = "요청 처리", notes = "처리된 요청")
    @ApiImplicitParam(name = "id", value = "승인 또는 거절하고자 하는 요청 id")
    @PutMapping("/{type}/{id}")
    public ResponseEntity approveReg(@PathVariable ProcessType type, @PathVariable Long id, HttpServletRequest request) throws Throwable {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        Registration registration = registrationService.process(id, type, account);
        return ResponseEntity.ok(registration);
    }

    @ApiOperation(value = "요청 취소")
    @ApiImplicitParam(name = "id", value = "취소하고자 하는 요청 id")
    @DeleteMapping("/{id}")
    public ResponseEntity cancelReg(@PathVariable Long id, HttpServletRequest request) throws Throwable {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        registrationService.cancel(id, account);
        return ResponseEntity.ok("ok");
    }

    @ApiOperation(value = "수정 요청 등록", notes = "등록된 요청")
    @ApiImplicitParam(name = "id", value = "수정하고자 하는 아이템 id")
    @PostMapping("/modify/{id}")
    public ResponseEntity modifyReg(@PathVariable Long id, @RequestBody @ApiParam(value = "수정 내용") R regRequest, HttpServletRequest request) throws Throwable {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        Registration registration = registrationService.modify(regRequest, id, account);
        return ResponseEntity.ok(registration);
    }

    @ApiOperation(value = "삭제 요청 등록", notes = "등록된 요청")
    @ApiImplicitParam(name = "id", value = "삭제하고자 하는 아이템 id")
    @PostMapping("/delete/{id}")
    public ResponseEntity deleteReg(@PathVariable Long id, HttpServletRequest request) throws Throwable {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        Registration registration = registrationService.delete(id, account);
        return ResponseEntity.ok(registration);
    }

    @ApiOperation(value = "삭제 요청 실행")
    @ApiImplicitParam(name = "id", value = "삭제 요청이 승인된 요청 id")
    @DeleteMapping("/execute/{id}")
    public ResponseEntity executeDeleteReg(@PathVariable Long id, HttpServletRequest request) throws Throwable {
        Account account = accountService.getAccount(jwtTokenProvider.getUserEmail(request));
        registrationService.executeDelete(id, account);
        return ResponseEntity.ok("ok");
    }
}
