package kvoting.intern.flowerwebapp.constraint;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "제약사항 API", value = "커스텀 도메인에서 사용되는 제약사항")
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/constraint", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConstraintController {

}
