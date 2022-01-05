package kvoting.intern.flowerwebapp.cmcd;

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

@Api(tags = "공통코드 API")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = ItemUrl.COMMON_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonCodeController {
	private final CommonCodeService commonCodeService;

	@ApiOperation(value = "전제 공통코드 조회")
	@GetMapping
	@JsonView(View.Public.class)
	public ResponseEntity getAllCmcds(Pageable pageable) {
		return ResponseEntity.ok(commonCodeService.getAllItems(pageable));
	}

	@ApiOperation(value = "id로 공통코드 상세 조회")
	@ApiImplicitParam(name = "id", value = "공통코드 id", type = "number")
	@GetMapping("/{id}")
	@JsonView(View.Detail.class)
	public ResponseEntity getCmcd(@PathVariable Long id) {
		return ResponseEntity.ok(commonCodeService.getDetail(id));
	}

	@ApiOperation(value = "code로 공통코드 조회")
	@ApiImplicitParam(name = "code", value = "코드 값", type = "string")
	@GetMapping("/code/{code}")
	@JsonView(View.Public.class)
	public ResponseEntity getByCode(@PathVariable String code, Pageable pageable) {
		return ResponseEntity.ok(commonCodeService.getByCode(code, pageable));
	}

	@ApiOperation(value = "code 이름으로 공통코드 조회")
	@ApiImplicitParam(name = "name", value = "코드 이름", type = "string")
	@GetMapping("/name/{name}")
	@JsonView(View.Public.class)
	public ResponseEntity getByCodeName(@PathVariable String name, Pageable pageable) {
		return ResponseEntity.ok(commonCodeService.getByCodeName(name, pageable));
	}

	// @ApiOperation(value = "상위 공통코드로 공통코드 조회")
	// @ApiImplicitParam(name = "id", value = "상위 공통코드 id", type = "number")
	// @GetMapping("/high/{id}")
	// @JsonView(View.Public.class)
	// public ResponseEntity getByHighCmcd(@PathVariable Long id, Pageable pageable) {
	// 	return ResponseEntity.ok(commonCodeService.getByHighCmcd(id, pageable));
	// }
}
