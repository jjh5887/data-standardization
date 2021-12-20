package kvoting.intern.flowerwebapp.word;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Api("Word API Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
public class WordController {
    private final WordService wordService;

    @ApiOperation(value = "id로 단어 조회", notes = "단어 정보")
    @ApiImplicitParam(name = "id", value = "단어 id")
    @GetMapping("/{id}")
    public ResponseEntity getWord(@PathVariable Long id) throws Throwable {
        Word body = (Word) wordService.get(id);
        return ResponseEntity.ok(body);
    }

    @ApiOperation(value = "영어(약어)로 단어 조회", notes = "단어 목록")
    @ApiImplicitParam(name = "name", value = "단어 영어명(약어)")
    @GetMapping("/eng/{name}")
    public ResponseEntity getWordByEng(@PathVariable String name, Pageable pageable) {
        Page<Word> byEng = wordService.getByEng(name, pageable);
        return ResponseEntity.ok(byEng);
    }

    @ApiOperation(value = "한글로 단어 조회", notes = "단어 목록")
    @ApiImplicitParam(name = "name", value = "단어 한글명")
    @GetMapping("/name/{name}")
    public ResponseEntity getWordByName(@PathVariable String name, Pageable pageable) {
        Page<Word> byName = wordService.getByName(name, pageable);
        return ResponseEntity.ok(byName);
    }

    @ApiOperation(value = "영어로 이름 조회", notes = "단어 목록")
    @ApiImplicitParam(name = "name", value = "단어 영어명")
    @GetMapping("/org/{name}")
    public ResponseEntity getWordByOrgEngName(@PathVariable String name, Pageable pageable) {
        Page<Word> byOrgEng = wordService.getByOrgEng(name, pageable);
        return ResponseEntity.ok(byOrgEng);
    }
}
