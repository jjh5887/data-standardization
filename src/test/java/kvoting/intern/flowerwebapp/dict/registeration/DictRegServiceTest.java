package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.dict.*;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class DictRegServiceTest {

    @Autowired
    DictRegService dictRegService;

    @Autowired
    DictService dictService;

    @Autowired
    WordService wordService;

    @Autowired
    DictRepository dictRepository;

    @Autowired
    DomainService domainService;

    @Autowired
    DomainRepository domainRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    DictRegRepository dictRegRepository;

    @Autowired
    ApplicationRunner applicationRunner;

    @BeforeEach
    public void setUp() throws Exception {
        dictRepository.deleteAll();
        domainRepository.deleteAll();
        wordRepository.deleteAll();
        applicationRunner.run(null);
        dictRepository.deleteAll();
    }

    @Test
    public void registCreatDict_UpdateWordAndUpdateDomain_DeleteAllWordsOfDict() {
        // When
        // getWords
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

        // Then
        assertThat(dictRepository.count()).isEqualTo(2L);
        Page<Dict> dictByWord = dictService.getDictByWord(List.of(pass), PageRequest.of(0, 10));
        assertThat(dictByWord.getTotalElements()).isEqualTo(1L);
        for (Dict dict : dictByWord) {
            assertThat(dict.getDictBase().getEngName()).containsIgnoringCase(pass.getWordBase().getEngName());
        }


        // When
        // update word
        Word word = wordService.getWord(pass.getId());
        word.getWordBase().setEngName("password");
        Word save = wordService.save(word);

        // Then
        // dict name has also been updated
        Page<Dict> dictByPass = dictService.getDictByWord(List.of(save), PageRequest.of(0, 10));
        assertThat(dictByPass.getTotalElements()).isEqualTo(1L);
        for (Dict byPass : dictByPass) {
            assertThat(Pattern.compile(Pattern.quote(save.getWordBase().getEngName()),
                    Pattern.CASE_INSENSITIVE).matcher(byPass.getDictBase().getEngName()).find())
                    .isEqualTo(true);
        }


        // When
        // delete some words of dict
        wordService.deleteWord(save);
        assertThat(dictRepository.count()).isEqualTo(2L);

        // Then word has also been deleted
        for (Dict dict : dictRepository.findAll()) {
            assertThat(Pattern.compile(Pattern.quote(save.getWordBase().getEngName()),
                    Pattern.CASE_INSENSITIVE).matcher(dict.getDictBase().getEngName()).find())
                    .isEqualTo(false);
        }

        // When
        // delete all words of dict
        wordService.deleteWord(us);

        // Then
        // The Dict has also deleted
        assertThat(dictRepository.count()).isEqualTo(1L);

        // When
        domainService.delete(num);

        // Then
        assertThat(dictRepository.count()).isEqualTo(0L);

    }

    @Test
    public void registModifyDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");
        Word nm = wordService.getWordByEng("nm");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getDomainByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // modify
        dictRegistRequest.setWords(List.of(nm, pass));
        dictRegistRequest.setDomains(List.of(num));
        DictReg modify = dictRegService.modify(dictRegistRequest, dictReg.getDict().getId());

        // Then
        Dict dict = modify.getDict();
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
        // before dict info
        assertThat(dict.getWords().contains(us)).isEqualTo(true);
        assertThat(dict.getDomains().contains(var)).isEqualTo(true);
        // after dict info
        assertThat(dict.getWords().contains(nm)).isEqualTo(false);
        assertThat(dict.getDomains().contains(num)).isEqualTo(false);
    }

    @Test
    public void registDeleteDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // delete
        dictRegService.delete(dictReg.getDict().getId());

        // Then
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
    }

    @Test
    public void approveCreateDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // approve
        dictRegService.processDictReg(dictReg.getId(), ProcessType.APPROVED);

        // Then
        assertThat(dictRegRepository.count()).isEqualTo(1L);
        assertThat(dictRepository.findAll().get(0).getStatus()).isEqualTo(ProcessType.APPROVED);
    }

    @Test
    public void rejectCreateDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // delete
        dictRegService.processDictReg(dictReg.getId(), ProcessType.REJECTED);

        // Then
        assertThat(dictRegRepository.count()).isEqualTo(1L);
        assertThat(dictRepository.findAll().get(0).getStatus()).isEqualTo(ProcessType.REJECTED);
    }

    @Test
    public void approveModifyDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");
        Word nm = wordService.getWordByEng("nm");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getDomainByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // modify
        dictRegistRequest.setWords(Arrays.asList(nm, pass));
        dictRegistRequest.setDomains(List.of(num));
        DictReg modify = dictRegService.modify(dictRegistRequest, dictReg.getDict().getId());

        // approve
        dictRegService.processDictReg(modify.getId(), ProcessType.APPROVED);

        // Then
        Dict dict = dictService.getDict(modify.getDict().getId());
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
        // before dict info
        assertThat(dict.getWords().contains(us)).isEqualTo(false);
        assertThat(dict.getDomains().contains(var)).isEqualTo(false);
        // after dict info
        assertThat(dict.getWords().contains(nm)).isEqualTo(true);
        assertThat(dict.getDomains().contains(num)).isEqualTo(true);
    }

    @Test
    public void rejectModifyDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");
        Word nm = wordService.getWordByEng("nm");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getDomainByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);
        // modify
        dictRegistRequest.setWords(List.of(nm, pass));
        dictRegistRequest.setDomains(List.of(num));
        DictReg modify = dictRegService.modify(dictRegistRequest, dictReg.getDict().getId());

        // reject
        dictRegService.processDictReg(modify.getId(), ProcessType.REJECTED);

        // Then
        Dict dict = modify.getDict();
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
        // before dict info
        assertThat(dict.getWords().contains(us)).isEqualTo(true);
        assertThat(dict.getDomains().contains(var)).isEqualTo(true);
        // after dict info
        assertThat(dict.getDomains().contains(num)).isEqualTo(false);
        assertThat(dict.getWords().contains(nm)).isEqualTo(false);
    }

    @Test
    public void approveDeleteDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // delete
        DictReg delete = dictRegService.delete(dictReg.getDict().getId());

        // approve
        dictRegService.processDictReg(delete.getId(), ProcessType.APPROVED);

        // Then
        assertThat(dictRepository.count()).isEqualTo(0L);
        assertThat(dictRegRepository.count()).isEqualTo(0L);
    }

    @Test
    public void rejectDeleteDict() {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = dictRegService.create(dictRegistRequest);

        // delete
        DictReg delete = dictRegService.delete(dictReg.getDict().getId());

        // reject
        dictRegService.processDictReg(delete.getId(), ProcessType.REJECTED);

        // Then
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
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
}