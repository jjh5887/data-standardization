package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WordRegServiceTest {
    @Autowired
    WordRegService wordRegService;
    @Autowired
    WordService wordService;
    @Autowired
    WordRepository wordRepository;
    @Autowired
    WordRegRepository wordRegRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    DictRepository dictRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        dictRepository.deleteAll();
        domainRepository.deleteAll();
        wordRepository.deleteAll();
        accountRepository.deleteAll();
        AccountCreateRequest accountCreateRequest = generateCreateRequest("test@test.com", "1234", "테스트", "테스트 부서");
        accountService.create(accountCreateRequest);
    }

    @Test
    public void registCreateWord() throws Throwable {
        // When
        // create
        Account account = accountService.getAccount("test@test.com");
        WordRegistRequest request = makeRequest();
        WordReg wordReg = (WordReg) wordRegService.create(request, account);

        // Then
        Word word = wordService.getByEng(request.getBase().getEngName());
        assertThat(wordReg.getItem()).isEqualTo(word);
        assertThat(word.getStatus()).isEqualTo(ProcessType.UNHANDLED);

        List<WordReg> wordRegs = wordRegRepository.findAll();
        assertThat(wordRegs.size()).isEqualTo(1);
        for (WordReg reg : wordRegs) {
            assertThat(reg).isEqualTo(wordReg);
        }

        // When
        // delete word
        wordService.delete(word);

        // Then
        assertThat(wordRepository.count()).isEqualTo(0L);
        assertThat(wordRegRepository.count()).isEqualTo(0L);
    }

    @Test
    public void registModifyWord() throws Throwable {
        // When
        // create
        Account account = accountService.getAccount("test@test.com");
        WordRegistRequest request = makeRequest();
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
        // modify
        request.getBase().setEngName("TTT");
        WordReg savedModifyReg = (WordReg) wordRegService.modify(request, wordReg.getItem().getId(), account);

        // Then
        WordReg savedReg = (WordReg) wordRegService.getRegistration(savedModifyReg.getId());
        assertThat(savedReg).isEqualTo(savedModifyReg);
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void registDeleteWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
        // delete
        WordReg savedDeleteReg = (WordReg) wordRegService.delete(wordReg.getItem().getId(), account);

        // Then
        WordReg savedReg = (WordReg) wordRegService.getRegistration(savedDeleteReg.getId());
        assertThat(savedReg).isEqualTo(savedDeleteReg);
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void approveCreateWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        assertThat(wordReg.getItem().getStatus()).isEqualTo(ProcessType.UNHANDLED);
        // approve
        WordReg approvedWordReg = (WordReg) wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);

        // Then
        Word word = (Word) wordService.get(wordReg.getItem().getId());
        assertThat(approvedWordReg.getItem().getStatus()).isEqualTo(ProcessType.APPROVED);
        assertThat(word.getStatus()).isEqualTo(ProcessType.APPROVED);
        assertThat(wordRegRepository.count()).isEqualTo(1L);
    }

    @Test
    public void rejectCreateWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        // reject
        WordReg rejectedWordReg = (WordReg) wordRegService.process(wordReg.getId(), ProcessType.REJECTED, account);

        // Then
        Word word = (Word) wordService.get(wordReg.getItem().getId());
        assertThat(rejectedWordReg.getItem().getStatus()).isEqualTo(ProcessType.REJECTED);
        assertThat(word.getStatus()).isEqualTo(ProcessType.REJECTED);
        assertThat(wordRegRepository.count()).isEqualTo(1L);
    }

    @Test
    public void approveModifyWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        wordService.get(wordReg.getItem().getId());
        wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);

        // modify
        String newName = "나테스트아님";
        request.getBase().setEngName(newName);
        WordReg savedModifyReg = (WordReg) wordRegService.modify(request, wordReg.getItem().getId(), account);

        // approve
        WordReg approvedWordReg = (WordReg) wordRegService.process(savedModifyReg.getId(), ProcessType.APPROVED, account);

        // Then
        Word modWord = (Word) wordService.get(approvedWordReg.getItem().getId());
        assertThat(modWord.getBase().getEngName()).isEqualTo(newName);
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void rejectModifyWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
        Word word = (Word) wordService.get(wordReg.getItem().getId());
        // modify
        String newName = "나테스트아님";
        request.getBase().setName(newName);
        WordReg savedModifyReg = (WordReg) wordRegService.modify(request, wordReg.getItem().getId(), account);

        // reject
        WordReg approvedWordReg = (WordReg) wordRegService.process(savedModifyReg.getId(), ProcessType.REJECTED, account);

        // Then
        Word modWord = (Word) wordService.get(approvedWordReg.getItem().getId());
        assertThat(modWord.getBase().getName()).isEqualTo(word.getBase().getName());
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void approveDeleteWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
        wordService.get(wordReg.getItem().getId());
        // delete
        WordReg savedModifyReg = (WordReg) wordRegService.delete(wordReg.getItem().getId(), account);

        // approve
        Registration process = wordRegService.process(savedModifyReg.getId(), ProcessType.APPROVED, account);

        // Then
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
        assertThat(process.getItem().getStatus()).isEqualTo(ProcessType.DELETABLE);
    }

    @Test
    public void rejectDeleteWord() throws Throwable {
        // When
        // create
        WordRegistRequest request = makeRequest();
        Account account = accountService.getAccount("test@test.com");
        WordReg wordReg = (WordReg) wordRegService.create(request, account);
        wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
        wordService.get(wordReg.getItem().getId());

        // delete
        WordReg savedModifyReg = (WordReg) wordRegService.delete(wordReg.getItem().getId(), account);

        // reject
        wordRegService.process(savedModifyReg.getId(), ProcessType.REJECTED, account);

        // Then
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    private WordRegistRequest makeRequest() {
        return WordRegistRequest.builder()
                .base(WordBase.builder()
                        .engName("TST")
                        .name("테스트")
                        .orgEngName("TEST")
                        .build())
                .build();
    }

    public AccountCreateRequest generateCreateRequest(String email, String password, String name, String department) {
        return AccountCreateRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .department(department)
                .build();
    }
}