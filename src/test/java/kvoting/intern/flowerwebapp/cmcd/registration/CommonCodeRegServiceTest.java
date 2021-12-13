package kvoting.intern.flowerwebapp.cmcd.registration;

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

    @BeforeEach
    public void setUp() throws Exception {
        commonCodeRepository.deleteAll();
        dictRepository.deleteAll();
        domainRepository.deleteAll();
        wordRepository.deleteAll();
        applicationRunner.run(null);
        commonCodeRepository.deleteAll();
    }

    @Test
    public void registCreateCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);


        // Then
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.getDict(dict.getId());
        CommonCode commonCode = commonCodeService.getCommonCode(commonCodeReg.getCommonCode().getId());

        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
    }

    @Test
    public void registModifyCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // modify
        cmcdRegRequest.getCommonCodeBase().setCode("B01");
        Word clb = wordService.getWordByEng("clb");
        cmcdRegRequest.setWords(List.of(clb));
        CommonCodeReg modify = commonCodeRegService.modify(cmcdRegRequest, commonCodeReg.getCommonCode().getId());

        // Then
        assertThat(commonCodeRegService.getCommonCodeReg(modify.getId()).getCommonCodeBase().getCodeName()).isEqualTo(clb.getWordBase().getName());
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);

        dict = dictService.getDict(dict.getId());
        CommonCode commonCode = commonCodeService.getCommonCode(commonCodeReg.getCommonCode().getId());
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
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // delete
        commonCodeRegService.delete(commonCodeReg.getCommonCode().getId());

        // Then
        assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
        assertThat(commonCodeRepository.count()).isEqualTo(1L);


        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.getDict(dict.getId());
        CommonCode commonCode = commonCodeService.getCommonCode(commonCodeReg.getCommonCode().getId());

        assertThat(dict.getCommonCode()).isEqualTo(commonCode);
        assertThat(dict.getCommonCode().getCommonCodeBase().getCodeName()).isEqualTo(commonCode.getCommonCodeBase().getCodeName());
        assertThat(dict.getCommonCode().getCommonCodeBase().getCode()).isEqualTo(commonCode.getCommonCodeBase().getCode());
        assertThat(dict.getCommonCode().getCommonCodeBase().getDescription()).isEqualTo(commonCode.getCommonCodeBase().getDescription());
        assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
    }

    @Test
    public void approveCreateCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // approve
        commonCodeRegService.processCommonCodeReg(commonCodeReg.getId(), ProcessType.APPROVED);

        // Then
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.getDict(dict.getId());
        CommonCode commonCode = commonCodeService.getCommonCode(commonCodeReg.getCommonCode().getId());

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
    public void rejectCreateCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // reject
        commonCodeRegService.processCommonCodeReg(commonCodeReg.getId(), ProcessType.REJECTED);

        // Then
        assertThat(commonCodeReg.getDict()).isEqualTo(dict);
        dict = dictService.getDict(dict.getId());
        CommonCode commonCode = commonCodeService.getCommonCode(commonCodeReg.getCommonCode().getId());

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
    public void approveModifyCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // modify
        cmcdRegRequest.getCommonCodeBase().setCode("B01");
        Word clb = wordService.getWordByEng("clb");
        cmcdRegRequest.setWords(List.of(clb));
        CommonCodeReg modify = commonCodeRegService.modify(cmcdRegRequest, commonCodeReg.getCommonCode().getId());

        // approve
        commonCodeRegService.processCommonCodeReg(modify.getId(), ProcessType.APPROVED);

        // Then
        CommonCode commonCode = commonCodeService.getCommonCode(modify.getCommonCode().getId());
        assertThat(commonCode.getCommonCodeBase().getCode()).isEqualTo(cmcdRegRequest.getCommonCodeBase().getCode());
        assertThat(commonCode.getCommonCodeBase().getCodeName()).isEqualTo(clb.getWordBase().getName());
    }

    @Test
    public void rejectModifyCmcd() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // modify
        cmcdRegRequest.getCommonCodeBase().setCode("B01");
        Word clb = wordService.getWordByEng("clb");
        cmcdRegRequest.setWords(List.of(clb));
        CommonCodeReg modify = commonCodeRegService.modify(cmcdRegRequest, commonCodeReg.getCommonCode().getId());

        // reject
        commonCodeRegService.processCommonCodeReg(modify.getId(), ProcessType.REJECTED);

        // Then
        CommonCode commonCode = commonCodeService.getCommonCode(modify.getCommonCode().getId());
        assertThat(commonCode.getCommonCodeBase().getCode()).isNotEqualTo(cmcdRegRequest.getCommonCodeBase().getCode());
        assertThat(commonCode.getCommonCodeBase().getCodeName()).isEqualTo(word.getWordBase().getName());
    }

    @Test
    public void approveDeleteCommonCode() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // delete
        CommonCodeReg delete = commonCodeRegService.delete(commonCodeReg.getCommonCode().getId());

        // approve
        commonCodeRegService.processCommonCodeReg(delete.getId(), ProcessType.APPROVED);

        // Then
        assertThat(commonCodeRegRepository.count()).isEqualTo(0L);
        assertThat(commonCodeRepository.count()).isEqualTo(0L);
    }

    @Test
    public void rejectDeleteCommonCode() {
        // When
        // create
        Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
        Word word = wordService.getWordByEng("blb");
        CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
        CommonCodeReg commonCodeReg = commonCodeRegService.create(cmcdRegRequest);

        // delete
        CommonCodeReg delete = commonCodeRegService.delete(commonCodeReg.getCommonCode().getId());

        // reject
        commonCodeRegService.processCommonCodeReg(delete.getId(), ProcessType.REJECTED);

        // Then
        assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
        assertThat(commonCodeRepository.count()).isEqualTo(1L);

        CommonCodeReg rejectedReg = commonCodeRegService.getCommonCodeReg(delete.getId());
        assertThat(rejectedReg.getRegistration().getProcessType()).isEqualTo(ProcessType.REJECTED);
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