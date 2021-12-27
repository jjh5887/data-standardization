package kvoting.intern.flowerwebapp.word;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "단어 API")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
public class WordController {
    private final WordService wordService;

    @ApiOperation(value = "id로 단어 조회")
    @ApiImplicitParam(name = "id", value = "단어 id", type = "number")
    @GetMapping("/{id}")
    @JsonView(View.Public.class)
    public ResponseEntity getWord(@PathVariable Long id) throws Throwable {
        return ResponseEntity.ok(wordService.get(id));
    }

    @ApiOperation(value = "id로 단어 상세 조회")
    @ApiImplicitParam(name = "id", value = "단어 id", type = "number")
    @GetMapping("/detail/{id}")
    @JsonView(View.Detail.class)
    public ResponseEntity getDetailWord(@PathVariable Long id) throws Throwable {
        return ResponseEntity.ok(wordService.getDetail(id));
    }

    @ApiOperation(value = "영어(약어)로 단어 조회")
    @ApiImplicitParam(name = "name", value = "단어 영어명(약어)")
    @GetMapping("/eng/{name}")
    @JsonView(View.Public.class)
    public ResponseEntity getWordByEng(@PathVariable String name, Pageable pageable) {
        Page<Word> byEng = wordService.getByEng(name, pageable);
        return ResponseEntity.ok(byEng);
    }

    @ApiOperation(value = "전체 단어 조회")
    @GetMapping
    @JsonView(View.Public.class)
    public ResponseEntity getAllWord(Pageable pageable) {
        Page<Item> byEng = wordService.getAllItems(pageable);
        return ResponseEntity.ok(byEng);
    }

    @ApiOperation(value = "한글로 단어 조회")
    @ApiImplicitParam(name = "name", value = "단어 한글명")
    @GetMapping("/name/{name}")
    @JsonView(View.Public.class)
    public ResponseEntity getWordByName(@PathVariable String name, Pageable pageable) {
        Page<Word> byName = wordService.getByName(name, pageable);
        return ResponseEntity.ok(byName);
    }

    @ApiOperation(value = "영어로 이름 조회")
    @ApiImplicitParam(name = "name", value = "단어 영어명")
    @GetMapping("/org/{name}")
    @JsonView(View.Public.class)
    public ResponseEntity getWordByOrgEngName(@PathVariable String name, Pageable pageable) {
        Page<Word> byOrgEng = wordService.getByOrgEng(name, pageable);
        return ResponseEntity.ok(byOrgEng);
    }
}
