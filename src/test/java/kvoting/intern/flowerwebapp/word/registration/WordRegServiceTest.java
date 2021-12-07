package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.type.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    public void setUp() {
        domainRepository.deleteAll();
        wordRepository.deleteAll();
    }

    @Test
    public void registCreateWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);

        // Then
        Word word = wordService.getWordByEng(request.getEngName());
        assertThat(wordReg.getWord()).isEqualTo(word);
        assertThat(word.getStatus()).isEqualTo(ProcessType.UNHANDLED);
        assertThat(word.getWordRegs().size()).isEqualTo(1);
        for (WordReg reg : word.getWordRegs()) {
            assertThat(reg).isEqualTo(wordReg);
        }

        // When
        // delete word
        wordRepository.delete(word);

        // Then
        assertThat(wordRegRepository.count()).isEqualTo(0L);
    }

    @Test
    public void registModifyWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        // modify
        request.setEngName("TTT");
        WordReg savedModifyReg = wordRegService.modify(request, wordReg.getWord().getId());

        // Then
        WordReg savedReg = wordRegService.getWordReg(savedModifyReg.getId());
        assertThat(savedReg).isEqualTo(savedModifyReg);
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void registDeleteWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        // delete
        WordReg savedDeleteReg = wordRegService.delete(wordReg.getWord().getId());

        // Then
        WordReg savedReg = wordRegService.getWordReg(savedDeleteReg.getId());
        assertThat(savedReg).isEqualTo(savedDeleteReg);
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void approveCreateWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        // approve
        WordReg approvedWordReg = wordRegService.processWordReg(wordReg.getId(), ProcessType.APPROVED);

        // Then
        Word word = wordService.getWord(wordReg.getWord().getId());
        assertThat(approvedWordReg.getWord().getStatus()).isEqualTo(ProcessType.APPROVED);
        assertThat(word.getStatus()).isEqualTo(ProcessType.APPROVED);
        assertThat(wordRegRepository.count()).isEqualTo(1L);
    }

    @Test
    public void rejectCreateWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        // reject
        WordReg rejectedWordReg = wordRegService.processWordReg(wordReg.getId(), ProcessType.REJECTED);

        // Then
        Word word = wordService.getWord(wordReg.getWord().getId());
        assertThat(rejectedWordReg.getWord().getStatus()).isEqualTo(ProcessType.REJECTED);
        assertThat(word.getStatus()).isEqualTo(ProcessType.REJECTED);
        assertThat(wordRegRepository.count()).isEqualTo(1L);
    }

    @Test
    public void approveModifyWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        Word word = wordService.getWord(wordReg.getWord().getId());
        // modify
        String newName = "나테스트아님";
        request.setName(newName);
        WordReg savedModifyReg = wordRegService.modify(request, wordReg.getWord().getId());

        // approve
        WordReg approvedWordReg = wordRegService.processWordReg(savedModifyReg.getId(), ProcessType.APPROVED);

        // Then
        Word modWord = wordService.getWord(approvedWordReg.getWord().getId());
        assertThat(modWord.getWordBase().getName()).isEqualTo(newName);
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void rejectModifyWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        Word word = wordService.getWord(wordReg.getWord().getId());
        // modify
        String newName = "나테스트아님";
        request.setName(newName);
        WordReg savedModifyReg = wordRegService.modify(request, wordReg.getWord().getId());

        // reject
        WordReg approvedWordReg = wordRegService.processWordReg(savedModifyReg.getId(), ProcessType.REJECTED);

        // Then
        Word modWord = wordService.getWord(approvedWordReg.getWord().getId());
        assertThat(modWord.getWordBase().getName()).isEqualTo(word.getWordBase().getName());
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    @Test
    public void approveDeleteWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        Word word = wordService.getWord(wordReg.getWord().getId());
        // delete
        WordReg savedModifyReg = wordRegService.delete(wordReg.getWord().getId());

        // approve
        WordReg approvedWordReg = wordRegService.processWordReg(savedModifyReg.getId(), ProcessType.APPROVED);

        // Then
        assertThat(wordRegRepository.count()).isEqualTo(0);
        assertThat(wordRepository.count()).isEqualTo(0);
    }

    @Test
    public void rejectDeleteWord() {
        // When
        // create
        WordRegistRequest request = makeRequest();
        WordReg wordReg = wordRegService.create(request);
        Word word = wordService.getWord(wordReg.getWord().getId());

        // delete
        WordReg savedModifyReg = wordRegService.delete(wordReg.getWord().getId());

        // reject
        WordReg approvedWordReg = wordRegService.processWordReg(savedModifyReg.getId(), ProcessType.REJECTED);

        // Then
        assertThat(wordRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(1L);
    }

    private WordRegistRequest makeRequest() {
        return WordRegistRequest.builder()
                .engName("TST")
                .name("테스트")
                .orgEngName("TEST")
                .build();
    }
}