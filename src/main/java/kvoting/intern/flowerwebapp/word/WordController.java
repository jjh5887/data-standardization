package kvoting.intern.flowerwebapp.word;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/word")
public class WordController {
    private final WordService wordService;
    private final WordRepository wordRepository;

    @GetMapping("/mock")
    public String getMockData(Model model) {
        List<Word> all = wordRepository.findAll();
        model.addAttribute("list", all);
        return "mock";
    }
}
