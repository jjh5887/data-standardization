package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.account.AccountService;
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

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void setUp() throws Exception {
        dictRepository.deleteAll();
        domainRepository.deleteAll();
        wordRepository.deleteAll();
        accountRepository.deleteAll();
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
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        dictRegService.create(dictRegistRequest, account);
        DictRegistRequest mb_nm = makeDictRequest(List.of(mb, nm), CaseStyle.SNAKE, "회원 이름", List.of(num));
        dictRegService.create(mb_nm, account);

        // Then
        assertThat(dictRepository.count()).isEqualTo(2L);
        Page<Dict> dictByWord = dictService.getDictByWord(List.of(pass), PageRequest.of(0, 10));
        assertThat(dictByWord.getTotalElements()).isEqualTo(1L);
        for (Dict dict : dictByWord) {
            assertThat(dict.getDictBase().getEngName()).containsIgnoringCase(pass.getWordBase().getEngName());
        }


        // When
        // update word
        Word word = wordService.get(pass.getId());
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
        wordService.delete(save);
        assertThat(dictRepository.count()).isEqualTo(2L);

        // Then word has also been deleted
        for (Dict dict : dictRepository.findAll()) {
            assertThat(Pattern.compile(Pattern.quote(save.getWordBase().getEngName()),
                    Pattern.CASE_INSENSITIVE).matcher(dict.getDictBase().getEngName()).find())
                    .isEqualTo(false);
        }

        // When
        // delete all words of dict
        wordService.delete(us);

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
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // modify
        dictRegistRequest.setWords(List.of(nm, pass));
        dictRegistRequest.setDomains(List.of(num));
        DictReg modify = (DictReg) dictRegService.modify(dictRegistRequest, dictReg.getDict().getId(), account);

        // Then
        Dict dict = modify.getDict();
        dict = dictService.getDetail(dict.getId());
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
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // delete
        dictRegService.delete(dictReg.getDict().getId(), account);

        // Then
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
    }

    @Test
    public void approveCreateDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // approve
        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

        // Then
        assertThat(dictRegRepository.count()).isEqualTo(1L);
        assertThat(dictRepository.findAll().get(0).getStatus()).isEqualTo(ProcessType.APPROVED);
    }

    @Test
    public void rejectCreateDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // delete
        dictRegService.process(dictReg.getId(), ProcessType.REJECTED, account);

        // Then
        assertThat(dictRegRepository.count()).isEqualTo(1L);
        assertThat(dictRepository.findAll().get(0).getStatus()).isEqualTo(ProcessType.REJECTED);
    }

    @Test
    public void approveModifyDict() throws Throwable {
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
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // modify
        dictRegistRequest.setWords(Arrays.asList(nm, pass));
        dictRegistRequest.setDomains(List.of(num));
        DictReg modify = (DictReg) dictRegService.modify(dictRegistRequest, dictReg.getDict().getId(), account);

        // approve
        dictRegService.process(modify.getId(), ProcessType.APPROVED, account);

        // Then
        Dict dict = dictService.getDetail(modify.getDict().getId());
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
    public void rejectModifyDict() throws Throwable {
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
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        // modify
        dictRegistRequest.setWords(List.of(nm, pass));
        dictRegistRequest.setDomains(List.of(num));
        DictReg modify = (DictReg) dictRegService.modify(dictRegistRequest, dictReg.getDict().getId(), account);

        // reject
        dictRegService.process(modify.getId(), ProcessType.REJECTED, account);

        // Then
        Dict dict = modify.getDict();
        dict = dictService.getDetail(dict.getId());
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
    public void approveDeleteDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // delete
        DictReg delete = (DictReg) dictRegService.delete(dictReg.getDict().getId(), account);

        // approve
        dictRegService.process(delete.getId(), ProcessType.APPROVED, account);

        // Then
        assertThat(dictRepository.count()).isEqualTo(0L);
        assertThat(dictRegRepository.count()).isEqualTo(0L);
    }

    @Test
    public void rejectDeleteDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getWordByEng("pass");
        Word us = wordService.getWordByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getDomainByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us, pass), CaseStyle.CAMEL, "헬로~", List.of(var));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);

        // delete
        DictReg delete = (DictReg) dictRegService.delete(dictReg.getDict().getId(), account);

        // reject
        dictRegService.process(delete.getId(), ProcessType.REJECTED, account);

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