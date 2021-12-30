package kvoting.intern.flowerwebapp.dict;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import kvoting.intern.flowerwebapp.view.View;
import lombok.RequiredArgsConstructor;

@Api(tags = "용어 API")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dict", produces = MediaType.APPLICATION_JSON_VALUE)
public class DictController {
	private final DictService dictService;

	@ApiOperation(value = "전체 용어 조회")
	@GetMapping
	@JsonView(View.Public.class)
	public ResponseEntity getAllDicts(Pageable pageable) {
		return ResponseEntity.ok(dictService.getAllItems(pageable));
	}

	@ApiOperation(value = "id로 용어 상세 조회")
	@ApiImplicitParam(name = "id", value = "용어 id", type = "number")
	@GetMapping("/{id}")
	@JsonView(View.Detail.class)
	public ResponseEntity getDict(@PathVariable Long id) {
		return ResponseEntity.ok(dictService.getDetail(id));
	}

	@ApiOperation(value = "이름으로 용어 조회")
	@ApiImplicitParam(name = "name", value = "용어 이름(영어, 한글, 화면)", type = "string")
	@GetMapping("/name/{name}")
	@JsonView(View.Public.class)
	public ResponseEntity getDomainByName(@PathVariable String name, Pageable pageable) {
		return ResponseEntity.ok(dictService.get(name, pageable));
	}

	@ApiOperation(value = "단어로 용어 조회")
	@ApiImplicitParam(name = "ids", value = "단어 id 리스트", type = "string")
	@GetMapping("/words")
	@JsonView(View.Public.class)
	public ResponseEntity getDictByWord(@RequestParam List<Long> ids, Pageable pageable) {
		return ResponseEntity.ok(dictService.get(ids, pageable));
	}
}
