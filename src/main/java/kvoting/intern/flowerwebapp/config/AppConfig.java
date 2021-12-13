package kvoting.intern.flowerwebapp.config;

import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeRegService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.dict.CaseStyle;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

            @Autowired
            DictService dictService;

            @Autowired
            DictRegService dictRegService;

            @Autowired
            CommonCodeRegService commonCodeRegService;

            private WordRegistRequest makeWordRequest(String engName, String name, String orgEngName) {
                return WordRegistRequest.builder()
                        .engName(engName)
                        .name(name)
                        .orgEngName(orgEngName)
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

            private DictRegistRequest makeDictRequest(List<Word> words, CaseStyle cs, String nm, List<Domain> domains) {
                return DictRegistRequest.builder()
                        .words(words)
                        .dictBase(makeDictBase(nm, cs))
                        .domains(domains)
                        .build();
            }

            private DictBase makeDictBase(String snm, CaseStyle cs) {
                return DictBase.builder()
                        .screenName(snm)
                        .isCommon(false)
                        .caseStyle(cs)
                        .build();
            }

            public CmcdRegRequest generateCmcdRegRequest(List<Word> words, Dict dict) {
                return CmcdRegRequest.builder()
                        .commonCodeBase(generateCommonCodeBase())
                        .words(words)
                        .dict(dict)
                        .build();
            }

            private CommonCodeBase generateCommonCodeBase() {
                return CommonCodeBase.builder()
                        .code("AO1")
                        .description("this is test cmcd descritption")
                        .build();
            }

            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<WordRegistRequest> requests = new ArrayList<>();
                requests.add(makeWordRequest("var", "문자열", "varchar2"));
                requests.add(makeWordRequest("num", "숫자", "number"));
                requests.add(makeWordRequest("cha", "고정 문자열", "char"));
                requests.add(makeWordRequest("dt", "날짜", "date"));
                requests.add(makeWordRequest("td", "타임스탬프", "timestamp"));
                requests.add(makeWordRequest("blb", "블롭", "blob"));
                requests.add(makeWordRequest("clb", "클롭", "clob"));
                requests.add(makeWordRequest("lng", "롱형", "long"));

                requests.add(makeWordRequest("us", "사용자", "user"));
                requests.add(makeWordRequest("pass", "비밀번호", "password"));
                requests.add(makeWordRequest("nm", "이름", "name"));
                requests.add(makeWordRequest("mb", "사용자", "member"));
                requests.add(makeWordRequest("desc", "설명", "description"));

                List<WordReg> wordRegs = new ArrayList<>();
                for (WordRegistRequest request : requests) {
                    wordRegs.add(wordRegService.create(request));
                }

                WordReg wordReg = wordRegs.get(0);
                WordRegistRequest request = requests.get(0);
                wordRegService.processWordReg(wordReg.getId(), ProcessType.APPROVED);
                request.setEngName("va");
                WordReg modify = wordRegService.modify(request, wordReg.getWord().getId());
                wordRegService.processWordReg(modify.getId(), ProcessType.APPROVED);

                List<Word> words = wordRepository.findAll();
                List<DomainReg> domainRegs = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    domainRegs.add(domainRegService.create(makeDomainRequest(List.of(words.get(i)))));
                }

                for (int i = 0; i < domainRegs.size(); i++) {
                    if (i % 2 == 0) {
                        domainRegService.processDomainReg(domainRegs.get(i).getId(), ProcessType.APPROVED);
                    }
                }

                Word word = wordService.getWord(words.get(2).getId());
                word.getWordBase().setEngName("ch");
                wordService.save(word);

                Word pass = wordService.getWordByEng("pass");
                Word us = wordService.getWordByEng("us");
                Word nm = wordService.getWordByEng("nm");
                Word mb = wordService.getWordByEng("mb");

                // getDomains
                Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
                Domain var = varDomains.getContent().get(0);
                Page<Domain> numDomains = domainService.getDomainByEngNameContains("num", PageRequest.of(0, 10));
                Domain num = numDomains.getContent().get(0);

                // create dict_reg
                DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
                dictRegService.create(dictRegistRequest);
                DictRegistRequest mb_nm = makeDictRequest(List.of(mb, nm), CaseStyle.SNAKE, "회원 이름", List.of(num));
                dictRegService.create(mb_nm);

                Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
                Word blb = wordService.getWordByEng("blb");
                CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(blb), dict);
                CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

                // approve
                commonCodeRegService.processCommonCodeReg(commonCodeReg.getId(), ProcessType.APPROVED);
            }
        };
    }
}
