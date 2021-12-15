package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeRepository;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommonCodeRegServiceTest {

    @Autowired
    CommonCodeRegService commonCodeRegService;

    @Autowired
    CommonCodeService commonCodeService;

    @Autowired
    DictService dictService;

    @Autowired
    DictRegService dictRegService;

    @Autowired
    WordService wordService;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    CommonCodeRegRepository commonCodeRegRepository;

    @Autowired
    ApplicationRunner applicationRunner;

    @Autowired
    DictRepository dictRepository;

    @Autowired
    DomainRepository domainRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void setUp() throws Exception {
        commonCodeRepository.deleteAll();
        dictRepository.deleteAll();
        domainRepository.deleteAll();
        wordRepository.deleteAll();
        accountRepository.deleteAll();
        applicationRunner.run(null);
        commonCodeRepository.deleteAll();
    }

    @Test
    public void registCreateCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");

        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);


        // Then
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.get(dict.getId());
        CommonCode commonCode = commonCodeService.get(commonCodeReg.getCommonCode().getId());

        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
    }

    @Test
    public void registModifyCmcd() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // modify
        cmcdRegRequest.getCommonCodeBase().setCode("B01");
        Word clb = wordService.getWordByEng("clb");
        cmcdRegRequest.setWords(List.of(clb));
        CommonCodeReg modify = (CommonCodeReg) commonCodeRegService.modify(cmcdRegRequest, commonCodeReg.getCommonCode().getId(), account);

        // Then
        CommonCodeReg modifyReg = (CommonCodeReg) commonCodeRegService.getRegistration(modify.getId());

        assertThat(modifyReg.getCommonCodeBase().getCodeName()).isEqualTo(clb.getWordBase().getName());
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);

        dict = dictService.get(dict.getId());
        CommonCode commonCode = commonCodeService.get(commonCodeReg.getCommonCode().getId());
        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
    }

    @Test
    public void registDeleteCommonCode() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // delete
        commonCodeRegService.delete(commonCodeReg.getCommonCode().getId(), account);

        // Then
        assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
        assertThat(commonCodeRepository.count()).isEqualTo(1L);


        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.get(dict.getId());
        CommonCode commonCode = commonCodeService.get(commonCodeReg.getCommonCode().getId());

        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
    }

    @Test
    public void approveCreateCmcd() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // approve
        commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

        // Then
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.get(dict.getId());
        CommonCode commonCode = commonCodeService.get(commonCodeReg.getCommonCode().getId());

        assertThat(commonCodeRepository.count()).isEqualTo(1L);
        assertThat(commonCodeRegRepository.count()).isEqualTo(1L);

        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
        assertThat(commonCode.getStatus()).isEqualTo(ProcessType.APPROVED);
    }

    @Test
    public void rejectCreateCmcd() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // reject
        commonCodeRegService.process(commonCodeReg.getId(), ProcessType.REJECTED, account);

        // Then
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.get(dict.getId());
        CommonCode commonCode = commonCodeService.get(commonCodeReg.getCommonCode().getId());

        assertThat(commonCodeRepository.count()).isEqualTo(1L);
        assertThat(commonCodeRegRepository.count()).isEqualTo(1L);

        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
        assertThat(commonCode.getStatus()).isEqualTo(ProcessType.REJECTED);
    }

    @Test
    public void approveModifyCmcd() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        Account account = accountService.getAccount("test@test.com");
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // modify
        cmcdRegRequest.getCommonCodeBase().setCode("B01");
        Word clb = wordService.getWordByEng("clb");
        cmcdRegRequest.setWords(List.of(clb));
        CommonCodeReg modify = (CommonCodeReg) commonCodeRegService.modify(cmcdRegRequest, commonCodeReg.getCommonCode().getId(), account);

        // approve
        commonCodeRegService.process(modify.getId(), ProcessType.APPROVED, account);

        // Then
        CommonCode commonCode = commonCodeService.get(modify.getCommonCode().getId());
        assertThat(commonCode.getCommonCodeBase().getCode()).isEqualTo(cmcdRegRequest.getCommonCodeBase().getCode());
        assertThat(commonCode.getCommonCodeBase().getCodeName()).isEqualTo(clb.getWordBase().getName());
    }

    @Test
    public void rejectModifyCmcd() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // modify
        cmcdRegRequest.getCommonCodeBase().setCode("B01");
        Word clb = wordService.getWordByEng("clb");
        cmcdRegRequest.setWords(List.of(clb));
        CommonCodeReg modify = (CommonCodeReg) commonCodeRegService.modify(cmcdRegRequest, commonCodeReg.getCommonCode().getId(), account);

        // reject
        commonCodeRegService.process(modify.getId(), ProcessType.REJECTED, account);

        // Then
        CommonCode commonCode = commonCodeService.get(modify.getCommonCode().getId());
        assertThat(commonCode.getCommonCodeBase().getCode()).isNotEqualTo(cmcdRegRequest.getCommonCodeBase().getCode());
        assertThat(commonCode.getCommonCodeBase().getCodeName()).isEqualTo(word.getWordBase().getName());
    }

    @Test
    public void approveDeleteCommonCode() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // delete
        CommonCodeReg delete = (CommonCodeReg) commonCodeRegService.delete(commonCodeReg.getCommonCode().getId(), account);

        // approve
        commonCodeRegService.process(delete.getId(), ProcessType.APPROVED, account);

        // Then
        assertThat(commonCodeRegRepository.count()).isEqualTo(0L);
        assertThat(commonCodeRepository.count()).isEqualTo(0L);
    }

    @Test
    public void rejectDeleteCommonCode() throws Throwable {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        Account account = accountService.getAccount("test@test.com");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = (CommonCodeReg) commonCodeRegService.create(cmcdRegRequest, account);

        // delete
        CommonCodeReg delete = (CommonCodeReg) commonCodeRegService.delete(commonCodeReg.getCommonCode().getId(), account);

        // reject
        commonCodeRegService.process(delete.getId(), ProcessType.REJECTED, account);

        // Then
        assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
        assertThat(commonCodeRepository.count()).isEqualTo(1L);

        CommonCodeReg rejectedReg = (CommonCodeReg) commonCodeRegService.getRegistration(delete.getId());
        assertThat(rejectedReg.getProcessType()).isEqualTo(ProcessType.REJECTED);
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
}