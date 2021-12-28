package kvoting.intern.flowerwebapp.domain;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kvoting.intern.flowerwebapp.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "도메인 API")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/domain", produces = MediaType.APPLICATION_JSON_VALUE)
public class DomainController {
    private final DomainService domainService;

    @ApiOperation(value = "전체 도메인 조회")
    @GetMapping
    @JsonView(View.Public.class)
    public ResponseEntity getAllDomain(Pageable pageable) {
        return ResponseEntity.ok(domainService.getAllItems(pageable));
    }

    @ApiOperation(value = "id로 도메인 상세 조회")
    @ApiImplicitParam(name = "id", value = "도메인 id", type = "number")
    @GetMapping("/{id}")
    @JsonView(View.Detail.class)
    public ResponseEntity getDomain(@PathVariable Long id) throws Throwable {
        return ResponseEntity.ok(domainService.getDetail(id));
    }

    @ApiOperation(value = "이름으로 도메인 조회")
    @ApiImplicitParam(name = "name", value = "도메인 이름", type = "string")
    @GetMapping("/name/{name}")
    @JsonView(View.Public.class)
    public ResponseEntity getDomainByName(@PathVariable String name, Pageable pageable) {
        return ResponseEntity.ok(domainService.getByEngNameContains(name, pageable));
    }

    @ApiOperation(value = "DB로 도메인 조회")
    @ApiImplicitParam(name = "db", value = "도메인 DB 타입", type = "string")
    @GetMapping("/db/{db}")
    @JsonView(View.Detail.class)
    public ResponseEntity getDomainByDB(@PathVariable DB db, Pageable pageable) {
        return ResponseEntity.ok(domainService.get(db, pageable));
    }

    @ApiOperation(value = "데이터 타입으로 도메인 조회")
    @ApiImplicitParam(name = "type", value = "도메인 데이터 타입", type = "string")
    @GetMapping("/type/{type}")
    @JsonView(View.Public.class)
    public ResponseEntity getDomainByType(@PathVariable DataType type, Pageable pageable) {
        return ResponseEntity.ok(domainService.get(type, pageable));
    }

    @ApiOperation(value = "데이터 길이로 도메인 조회")
    @ApiImplicitParam(name = "length", value = "도메인 데이터 길이", type = "number")
    @GetMapping("/length/{length}")
    @JsonView(View.Public.class)
    public ResponseEntity getDomainBySize(@PathVariable Integer length, Pageable pageable) {
        return ResponseEntity.ok(domainService.getBySize(length, pageable));
    }

    @ApiOperation(value = "데이터 정화도(소수점 자리수)로 도메인 조회")
    @ApiImplicitParam(name = "scale", value = "도메인 데이터 정확도(소수점 자리수)", type = "number")
    @GetMapping("/scale/{scale}")
    @JsonView(View.Public.class)
    public ResponseEntity getDomainByScale(@PathVariable Integer scale, Pageable pageable) {
        return ResponseEntity.ok(domainService.getByScale(scale, pageable));
    }

    @ApiOperation(value = "null 가능 여부로 도메인 조회")
    @ApiImplicitParam(name = "nullable", value = "null 가능 여부", type = "boolean", allowableValues = "true, false")
    @GetMapping("/nullable/{nullable}")
    @JsonView(View.Public.class)
    public ResponseEntity getDomainByNullable(@PathVariable Boolean nullable, Pageable pageable) {
        return ResponseEntity.ok(domainService.getByNullable(nullable, pageable));
    }

    @ApiOperation(value = "DB와 데이터 타입으로 도메인 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "db", value = "도메인 DB 타입", type = "string"),
            @ApiImplicitParam(name = "type", value = "도메인 데이터 타입", type = "string")})
    @GetMapping("/{db}/{type}")
    @JsonView(View.Public.class)
    public ResponseEntity getDomainByDbAndType(@PathVariable DB db, @PathVariable DataType type, Pageable pageable) {
        return ResponseEntity.ok(domainService.get(db, type, pageable));
    }
}
