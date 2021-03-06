package kvoting.intern.flowerwebapp.item.registration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.config.RegUrl;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import kvoting.intern.flowerwebapp.view.View;
import lombok.RequiredArgsConstructor;

@Api(tags = "통합 요청 API")
@RestController
@RequestMapping(value = RegUrl.REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RegistrationController<R extends RegRequest> {
	protected final RegistrationService registrationService;
	protected final AccountService accountService;
	protected final JwtTokenProvider jwtTokenProvider;

	@ApiOperation(value = "전체 요청 조회")
	@GetMapping
	@JsonView(View.Public.class)
	public ResponseEntity getAllReg(@PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(registrationService.getAllRegs(pageable));
	}

	@ApiOperation(value = "id로 등록된 요청 조회")
	@ApiImplicitParam(name = "id", value = "요청 id")
	@GetMapping("/{id}")
	@JsonView(View.Detail.class)
	public ResponseEntity getReg(@PathVariable Long id) {
		return ResponseEntity.ok(registrationService.getRegDetail(id));
	}

	@ApiOperation(value = "생성 요청 등록")
	@PostMapping
	@JsonView(View.Detail.class)
	public ResponseEntity createReg(@RequestBody R request, HttpServletRequest servletRequest) {
		Registration registration = registrationService
			.create(request, jwtTokenProvider.getUserEmail(servletRequest));
		return ResponseEntity.ok(registration);
	}

	@ApiOperation(value = "수정 요청 등록")
	@ApiImplicitParam(name = "id", value = "수정하고자 하는 아이템 id")
	@PostMapping("/modify/{id}")
	@JsonView(View.Detail.class)
	public ResponseEntity modifyReg(@PathVariable Long id, @RequestBody R request,
		HttpServletRequest servletRequest) {
		Registration registration = registrationService.modify(request, id,
			jwtTokenProvider.getUserEmail(servletRequest));
		return ResponseEntity.ok(registration);
	}

	@ApiOperation(value = "삭제 요청 등록")
	@ApiImplicitParam(name = "id", value = "삭제하고자 하는 아이템 id")
	@PostMapping("/delete/{id}")
	@JsonView(View.Public.class)
	public ResponseEntity deleteReg(@PathVariable Long id, HttpServletRequest request) {
		Registration registration = registrationService
			.delete(id, jwtTokenProvider.getUserEmail(request));
		return ResponseEntity.ok(registration);
	}

	@ApiOperation(value = "요청 처리")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "승인 또는 거절하고자 하는 요청 id"),
		@ApiImplicitParam(name = "type", value = "처리 상태", allowableValues = "APPROVED, REJECTED")})
	@PutMapping("/{type}/{id}")
	@JsonView(View.Public.class)
	public ResponseEntity approveReg(@PathVariable Long id, @PathVariable ProcessType type,
		HttpServletRequest servletRequest) {
		Registration registration = registrationService.process(id, type,
			jwtTokenProvider.getUserEmail(servletRequest));
		return ResponseEntity.ok(registration);
	}

	@ApiOperation(value = "요청 취소")
	@ApiImplicitParam(name = "id", value = "취소하고자 하는 요청 id")
	@DeleteMapping("/{id}")
	public ResponseEntity cancelReg(@PathVariable Long id, HttpServletRequest servletRequest) {
		registrationService.cancel(id, jwtTokenProvider.getUserEmail(servletRequest));
		return ResponseEntity.ok("ok");
	}

	@ApiOperation(value = "삭제 요청 실행")
	@ApiImplicitParam(name = "id", value = "삭제 요청이 승인된 요청 id")
	@DeleteMapping("/execute/{id}")
	public ResponseEntity executeDeleteReg(@PathVariable Long id, HttpServletRequest request) {
		registrationService.executeDelete(id, jwtTokenProvider.getUserEmail(request));
		return ResponseEntity.ok("ok");
	}
}
