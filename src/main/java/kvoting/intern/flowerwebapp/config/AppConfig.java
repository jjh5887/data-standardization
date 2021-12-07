package kvoting.intern.flowerwebapp.config;

import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registeration.DomainReg;
import kvoting.intern.flowerwebapp.domain.registeration.DomainRegService;
import kvoting.intern.flowerwebapp.domain.registeration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
import kvoting.intern.flowerwebapp.type.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import kvoting.intern.flowerwebapp.word.registration.WordRegService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            WordRegService wordRegService;

            @Autowired
            DomainRegService domainRegService;

            @Autowired
            WordService wordService;

            @Autowired
            WordRepository wordRepository;

            @Autowired
            DomainRepository domainRepository;

            @Autowired
            DomainService domainService;

            private WordRegistRequest makeWordRequest(int idx) {
                return WordRegistRequest.builder()
                        .engName("TST" + idx)
                        .name("테스트" + idx)
                        .orgEngName("TEST" + idx)
                        .build();
            }

            private DomainRegistRequest makeDomainRequest(List<Word> words) {
                return DomainRegistRequest.builder()
                        .words(words)
                        .domainBase(makeDomainBase())
                        .build();
            }

            private DomainBase makeDomainBase() {
                return DomainBase.builder()
                        .size(20)
                        .scale(0)
                        .nullable(true)
                        .description("this is test domain")
                        .db(DB.ORACLE)
                        .dataType(DataType.VARCHAR2)
                        .build();
            }

            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<WordRegistRequest> requests = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    requests.add(makeWordRequest(i));
                }

                List<WordReg> wordRegs = new ArrayList<>();
                for (WordRegistRequest request : requests) {
                    wordRegs.add(wordRegService.create(request));
                }

                WordReg wordReg = wordRegs.get(0);
                WordRegistRequest request = requests.get(0);
                wordRegService.processWordReg(wordReg.getId(), ProcessType.APPROVED);
                request.setName("TTT");
                WordReg modify = wordRegService.modify(request, wordReg.getWord().getId());
                wordRegService.processWordReg(modify.getId(), ProcessType.APPROVED);

                List<Word> words = wordRepository.findAll();
                List<DomainReg> domainRegs = new ArrayList<>();
                domainRegs.add(domainRegService.create(makeDomainRequest(words)));
                domainRegs.add(domainRegService.create(makeDomainRequest(words.subList(0, 5))));
                domainRegs.add(domainRegService.create(makeDomainRequest(words.subList(1, 3))));
                domainRegs.add(domainRegService.create(makeDomainRequest(words.subList(4, 9))));
                domainRegs.add(domainRegService.create(makeDomainRequest(words.subList(5, 8))));
                domainRegs.add(domainRegService.create(makeDomainRequest(words.subList(7, 9))));

                for (int i = 0; i < domainRegs.size(); i++) {
                    if (i % 2 == 0) {
                        domainRegService.processDomainReg(domainRegs.get(i).getId(), ProcessType.APPROVED);
                    }
                }

                Word word = wordService.getWord(words.get(2).getId());
                word.getWordBase().setEngName("GGGG");
                wordService.save(word);
            }
        };
    }
}
