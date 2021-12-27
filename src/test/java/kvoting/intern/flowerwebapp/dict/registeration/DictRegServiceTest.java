package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.constraint.ConstraintRepository;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainRepository;
import kvoting.intern.flowerwebapp.dict.*;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegRepository;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.RegistrationType;
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
import java.util.Set;
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

    @Autowired
    ConstraintRepository constraintRepository;

    @Autowired
    CustomDomainRepository customDomainRepository;

    @Autowired
    DomainRegRepository domainRegRepository;

    @Autowired
    DomainRegService domainRegService;

    @BeforeEach
    public void setUp() throws Exception {
        dictRepository.deleteAll();
        domainRepository.deleteAll();
        customDomainRepository.deleteAll();
        constraintRepository.deleteAll();
        wordRepository.deleteAll();
        accountRepository.deleteAll();
        applicationRunner.run(null);
        dictRepository.deleteAll();
    }

    @Test
    public void registCreatDict_UpdateWordAndUpdateDomain_DeleteAllWordsOfDict() throws Throwable {
        // When
        // getWords
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");
        Word nm = wordService.getByEng("nm");
        Word mb = wordService.getByEng("mb");


        // getDomains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);

        // create dict_reg
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        dictRegService.create(dictRegistRequest, account);
        DictRegistRequest mb_nm = makeDictRequest(List.of(mb.getId(), nm.getId()), CaseStyle.SNAKE, "회원 이름", Set.of(num.getId()));
        dictRegService.create(mb_nm, account);

        // Then
        assertThat(dictRepository.count()).isEqualTo(2L);
        Page<Dict> dictByWord = dictService.get(List.of(pass.getId()), PageRequest.of(0, 10));
        assertThat(dictByWord.getTotalElements()).isEqualTo(1L);
        for (Dict dict : dictByWord) {
            assertThat(dict.getBase().getEngName()).containsIgnoringCase(pass.getBase().getEngName());
        }


        // When
        // update word
        Word word = (Word) wordService.get(pass.getId());
        word.getBase().setEngName("password");
        Word save = wordService.save(word);

        // Then
        // dict name has also been updated
        Page<Dict> dictByPass = dictService.get(List.of(save.getId()), PageRequest.of(0, 10));
        assertThat(dictByPass.getTotalElements()).isEqualTo(1L);
        for (Dict byPass : dictByPass) {
            assertThat(Pattern.compile(Pattern.quote(save.getBase().getEngName()),
                    Pattern.CASE_INSENSITIVE).matcher(byPass.getBase().getEngName()).find())
                    .isEqualTo(true);
        }


        // When
        // delete some words of dict
        wordService.delete(save);
        assertThat(dictRepository.count()).isEqualTo(2L);

        // Then word has also been deleted
        for (Dict dict : dictRepository.findAll()) {
            assertThat(Pattern.compile(Pattern.quote(save.getBase().getEngName()),
                    Pattern.CASE_INSENSITIVE).matcher(dict.getBase().getEngName()).find())
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
    public void registModifyDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");
        Word nm = wordService.getByEng("nm");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }
        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

        // modify
        dictRegistRequest.setWords(List.of(nm.getId(), pass.getId()));
        dictRegistRequest.setDomains(Set.of(num.getId()));
        DictReg modify = (DictReg) dictRegService.modify(dictRegistRequest, dictReg.getItem().getId(), account);

        // Then
        Dict dict = modify.getItem();
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
    public void registDeleteDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }
        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

        // delete
        dictRegService.delete(dictReg.getItem().getId(), account);

        // Then
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
    }

    @Test
    public void approveCreateDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);


        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }

        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

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
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
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
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");
        Word nm = wordService.getByEng("nm");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }

        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

        // modify
        dictRegistRequest.setWords(Arrays.asList(nm.getId(), pass.getId()));
        dictRegistRequest.setDomains(Set.of(num.getId()));
        DictReg modify = (DictReg) dictRegService.modify(dictRegistRequest, dictReg.getItem().getId(), account);

        // approve
        dictRegService.process(modify.getId(), ProcessType.APPROVED, account);

        // Then
        Dict dict = dictService.getDetail(modify.getItem().getId());
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
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");
        Word nm = wordService.getByEng("nm");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);
        Page<Domain> numDomains = domainService.getByEngNameContains("num", PageRequest.of(0, 10));
        Domain num = numDomains.getContent().get(0);


        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }

        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);
        // modify
        dictRegistRequest.setWords(List.of(nm.getId(), pass.getId()));
        dictRegistRequest.setDomains(Set.of(num.getId()));
        DictReg modify = (DictReg) dictRegService.modify(dictRegistRequest, dictReg.getItem().getId(), account);

        // reject
        dictRegService.process(modify.getId(), ProcessType.REJECTED, account);

        // Then
        Dict dict = modify.getItem();
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
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }
        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

        // delete
        DictReg delete = (DictReg) dictRegService.delete(dictReg.getItem().getId(), account);

        // approve
        DictReg process = (DictReg) dictRegService.process(delete.getId(), ProcessType.APPROVED, account);

        // Then
        assertThat(dictRegRepository.count()).isEqualTo(2L);
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(process.getItem().getStatus()).isEqualTo(ProcessType.DELETABLE);
        dictRegService.executeDelete(process.getId(), account);
        assertThat(dictRegRepository.count()).isEqualTo(0L);
        assertThat(dictRepository.count()).isEqualTo(0L);
    }

    @Test
    public void rejectDeleteDict() throws Throwable {
        // When
        // get words
        Word pass = wordService.getByEng("pass");
        Word us = wordService.getByEng("us");

        // get Domains
        Page<Domain> varDomains = domainService.getByEngNameContains("va", PageRequest.of(0, 10));
        Domain var = varDomains.getContent().get(0);

        // create
        Account account = accountService.getAccount("test@test.com");
        DictRegistRequest dictRegistRequest = makeDictRequest(List.of(us.getId(), pass.getId()), CaseStyle.CAMEL, "헬로~", Set.of(var.getId()));
        DictReg dictReg = (DictReg) dictRegService.create(dictRegistRequest, account);
        List<DomainReg> all = domainRegRepository.findAll();
        for (DomainReg domainReg : all) {
            if (domainReg.getRegistrationType() == RegistrationType.CREATE) {
                domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account);
            }
        }
        dictRegService.process(dictReg.getId(), ProcessType.APPROVED, account);

        // delete
        DictReg delete = (DictReg) dictRegService.delete(dictReg.getItem().getId(), account);

        // reject
        dictRegService.process(delete.getId(), ProcessType.REJECTED, account);

        // Then
        assertThat(dictRepository.count()).isEqualTo(1L);
        assertThat(dictRegRepository.count()).isEqualTo(2L);
    }

    private DictRegistRequest makeDictRequest(List<Long> words, CaseStyle cs, String nm, Set<Long> domains) {
        return DictRegistRequest.builder()
                .words(words)
                .base(makeDictBase(nm, cs))
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