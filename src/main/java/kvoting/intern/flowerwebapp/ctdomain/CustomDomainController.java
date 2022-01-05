package kvoting.intern.flowerwebapp.ctdomain;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import kvoting.intern.flowerwebapp.config.ItemUrl;
import kvoting.intern.flowerwebapp.view.View;
import lombok.RequiredArgsConstructor;

@Api(tags = "커스텀 도메인 API")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = ItemUrl.CUSTOM_DOMAIN, produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomDomainController {

	private final CustomDomainService customDomainService;

	@ApiOperation(value = "전체 커스텀 도메인 조회")
	@GetMapping
	@JsonView(View.Public.class)
	public ResponseEntity getAllCustomDomain(Pageable pageable) {
		return ResponseEntity.ok(customDomainService.getAllItems(pageable));
	}

	@ApiOperation(value = "id로 커스텀 도메인 상세 조회")
	@ApiImplicitParam(name = "id", value = "커스텀 도메인 id", type = "number")
	@GetMapping("/{id}")
	@JsonView(View.Detail.class)
	public ResponseEntity getCustomDomain(@PathVariable Long id) {
		return ResponseEntity.ok(customDomainService.getDetail(id));
	}

	@ApiOperation(value = "이름으로 커스텀 도메인 조회")
	@ApiImplicitParam(name = "name", value = "커스텀 도메인 이름", type = "string")
	@GetMapping("/name/{name}")
	@JsonView(View.Public.class)
	public ResponseEntity getCustomDomainByName(@PathVariable String name, Pageable pageable) {
		return ResponseEntity.ok(customDomainService.getByName(name, pageable));
	}
}
