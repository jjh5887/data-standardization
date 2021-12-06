package kvoting.intern.flowerwebapp.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/domain")
public class DomainController {
    private final DomainRepository domainRepository;

    @GetMapping("mock")
    public String getMock(Model model) {
        List<Domain> all = domainRepository.findAll();
        model.addAttribute("domains", all);
        return "mock";
    }
}
