package kvoting.intern.flowerwebapp.constraint;

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
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kvoting.intern.flowerwebapp.view.View;
import lombok.RequiredArgsConstructor;

@Api(tags = "제약사항 API", description = "커스텀 도메인에서 사용되는 제약사항")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/constraint", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConstraintController {
	private final ConstraintService constraintService;

	@ApiOperation(value = "전체 제약사항 조회")
	@GetMapping
	@JsonView(View.Public.class)
	public ResponseEntity getAllConstraints(Pageable pageable) {
		return ResponseEntity.ok(constraintService.getAllItems(pageable));
	}

	@ApiOperation(value = "id로 제약사항 상세 조회")
	@GetMapping("/{id}")
	@ApiImplicitParam(name = "id", value = "제약사항 id", type = "number")
	@JsonView(View.Detail.class)
	public ResponseEntity getById(@PathVariable Long id) {
		return ResponseEntity.ok(constraintService.getDetail(id));
	}

	@ApiOperation(value = "이름으로 제약사항 조회")
	@GetMapping("/name/{name}")
	@ApiImplicitParam(name = "name", value = "제약사항 이름", type = "string")
	@JsonView(View.Public.class)
	public ResponseEntity getByName(@PathVariable String name, Pageable pageable) {
		return ResponseEntity.ok(constraintService.getByName(name, pageable));
	}

	@ApiOperation(value = "입렵 타입으로 제약사항 조회")
	@GetMapping("/type/{input}")
	@ApiImplicitParam(name = "input", value = "제약사항 입력 타입", dataTypeClass = InputType.class)
	@JsonView(View.Public.class)
	public ResponseEntity getByInput(@PathVariable InputType input, Pageable pageable) {
		return ResponseEntity.ok(constraintService.getByInput(input, pageable));
	}

	@ApiOperation(value = "값으로 제약사항 조회")
	@GetMapping("/value/{value}")
	@ApiImplicitParam(name = "value", value = "제약사항 값", type = "string")
	@JsonView(View.Public.class)
	public ResponseEntity getByValue(@PathVariable String value, Pageable pageable) {
		return ResponseEntity.ok(constraintService.getByValue(value, pageable));
	}

	@ApiOperation(value = "이름, 입력 타입으로 제약사항 조회")
	@GetMapping("/{name}/{input}/")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "제약사항 이름", type = "string"),
		@ApiImplicitParam(name = "input", value = "제약사항 입력 타입", dataTypeClass = InputType.class)
	})
	@JsonView(View.Public.class)
	public ResponseEntity getByName_Input_Value(@PathVariable String name,
		@PathVariable InputType input, Pageable pageable) {
		return ResponseEntity.ok(constraintService.getByNameAndType(name, input, pageable));
	}
}
