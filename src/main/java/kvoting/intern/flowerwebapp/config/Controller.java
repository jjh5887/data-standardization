package kvoting.intern.flowerwebapp.config;

import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegRepository;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegRepository;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.registration.WordRegRepository;
import kvoting.intern.flowerwebapp.word.registration.WordRegService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
    private final WordRepository wordRepository;
    private final WordRegRepository wordRegRepository;
    private final DomainRepository domainRepository;
    private final DomainRegRepository domainRegRepository;
    private final DictRepository dictRepository;
    private final DictRegRepository dictRegRepository;
    private final WordRegService wordRegService;
    private final DomainRegService domainRegService;

    @GetMapping("/test")
    public ResponseEntity test() {
        Word body = wordRepository.findAll().get(0);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/test1")
    public ResponseEntity test1() throws Throwable {
        Registration registration = wordRegService.getRegistration(3L);
        return ResponseEntity.ok(registration);
    }

    @GetMapping("/test2")
    public ResponseEntity test2() {
        return ResponseEntity.ok(domainRepository.findAll().get(0));
    }

    @GetMapping("/test3")
    public ResponseEntity test3() throws Throwable {
        Registration registration = domainRegService.getRegistration(30L);
        return ResponseEntity.ok(registration);
    }

    @GetMapping("/test4")
    public ResponseEntity test4() {
        return ResponseEntity.ok(dictRepository.findAll().get(0));
    }

    @GetMapping("/test5")
    public ResponseEntity test5() {
        return ResponseEntity.ok(dictRegRepository.findAll().get(0));
    }
}
