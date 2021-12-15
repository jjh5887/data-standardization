package kvoting.intern.flowerwebapp.config;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRole;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
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
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationRepository;
import kvoting.intern.flowerwebapp.registration.RegistrationService;
import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
            RegistrationRepository registrationRepository;

            @Autowired
            @Qualifier("wordRegService")
            RegistrationService wordRegService;

            @Autowired
            AccountService accountService;


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

            public AccountCreateRequest generateCreateRequest(String email, String password, String name, String department) {
                return AccountCreateRequest.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .department(department)
                        .build();
            }

            private WordRegistRequest makeWordRequest(String engName, String name, String orgEngName) {
                return WordRegistRequest.builder()
                        .wordBase(WordBase.builder()
                                .engName(engName)
                                .name(name)
                                .orgEngName(orgEngName)
                                .build())
                        .build();
            }

            private DomainRegistRequest makeDomainRequest(int idx) {
                return DomainRegistRequest.builder()
                        .domainBase(makeDomainBase(idx))
                        .build();
            }

            private DomainBase makeDomainBase(int idx) {
                return DomainBase.builder()
                        .size(20)
                        .scale(0)
                        .nullable(true)
                        .description("this is test domain")
                        .db(DB.ORACLE)
                        .dataType(DataType.values()[idx])
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

            @SneakyThrows
            @Override
            public void run(ApplicationArguments args) {
                AccountCreateRequest accountCreateRequest = generateCreateRequest("test@test.com", "1234", "테스트", "테스트 부서");
                Account savedAccount = accountService.create(accountCreateRequest);
                savedAccount.addRole(AccountRole.ADMIN);
                Account account = accountService.save(savedAccount);


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

                List<Registration> wordRegs = new ArrayList<>();
                for (WordRegistRequest request : requests) {
                    wordRegs.add(wordRegService.create(request, account));
                }

                Registration wordReg = wordRegs.get(0);
                WordRegistRequest request = requests.get(0);

                wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
                request.getWordBase().setEngName("va");

                WordReg modify = (WordReg) wordRegService.modify(request, ((WordReg) wordReg).getWord().getId(), account);
                wordRegService.process(modify.getId(), ProcessType.APPROVED, account);

                List<Word> words = wordRepository.findAll();
                List<DomainReg> domainRegs = new ArrayList<>();
                for (int i = 0; i < DataType.values().length; i++) {
                    domainRegs.add((DomainReg) domainRegService.create(makeDomainRequest(i), account));
                }

                for (int i = 0; i < domainRegs.size(); i++) {
                    if (i % 2 == 0) {
                        domainRegService.process(domainRegs.get(i).getId(), ProcessType.APPROVED, account);
                    }
                }

                Word word = wordService.get(words.get(2).getId());
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
                dictRegService.create(dictRegistRequest, account);
                DictRegistRequest mb_nm = makeDictRequest(List.of(mb, nm), CaseStyle.SNAKE, "회원 이름", List.of(num));
                dictRegService.create(mb_nm, account);

                Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
                Word blb = wordService.getWordByEng("blb");
                CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(blb), dict);
                CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

                // approve
                commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);
            }
        };
    }
}
