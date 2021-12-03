package kvoting.intern.flowerwebapp.config;

import kvoting.intern.flowerwebapp.type.ProcessType;
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

            private WordRegistRequest makeRequest(int idx) {
                return WordRegistRequest.builder()
                        .engName("TST" + idx)
                        .name("테스트" + idx)
                        .orgEngName("TEST" + idx)
                        .build();
            }

            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<WordRegistRequest> requests = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    requests.add(makeRequest(i));
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
                System.out.println(modify.getRegistration().getRegistrationType());

                System.out.println();
                System.out.println();
                System.out.println("hi");
                wordRegService.processWordReg(modify.getId(), ProcessType.APPROVED);
            }
        };
    }
}
