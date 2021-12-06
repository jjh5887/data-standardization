package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registeration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
import kvoting.intern.flowerwebapp.type.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import kvoting.intern.flowerwebapp.word.registration.WordRegService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DomainRegServiceTest {
    @Autowired
    DomainRegService domainRegService;
    @Autowired
    DomainService domainService;
    @Autowired
    DomainRegRepository domainRegRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    WordRegService wordRegService;
    @Autowired
    WordRepository wordRepository;
    @Autowired
    WordService wordService;

    @BeforeEach
    public void setUp() {
        domainRepository.deleteAll();
        wordRepository.deleteAll();
    }

    @Test
    public void registCreateDomain() {
        // When
        // generateWords
        generateWords();
        List<Word> words = wordRepository.findAll();
        // create
        DomainRegistRequest domainRegistRequest = makeDomRequest(words);
        DomainReg domainReg = domainRegService.create(domainRegistRequest);

        // Then
        Domain domain = domainService.getDomain(domainReg.getDomain().getId());
        List<Word> domainWords = domain.getWords();
        List<Word> regWords = domainReg.getRegDomain().getWords();
        assertThat(domainWords.size()).isEqualTo(regWords.size());
        assertThat(domain.getStatus()).isEqualTo(ProcessType.UNHANDLED);

        Word word = wordService.getWord(words.get(0).getId());
        word.setEngName("GGG");
        wordService.save(word);

        Page<Domain> domainByName = domainService.getDomainByEngName(word.getEngName(), PageRequest.of(0, 10));
        assertThat(domainByName.getTotalElements()).isEqualTo(1L);
    }

    @Test
    public void registModifyDomain() {
        // When
        // create
        generateWords();
        List<Word> words = wordRepository.findAll();
        DomainRegistRequest request = makeRequest(words);
        DomainReg domainReg = domainRegService.create(request);
        // modify
        request.setDescription("this is new domain");
        request.getWords().remove(3);
        DomainReg savedModifyReg = domainRegService.modify(request, domainReg.getDomain().getId());

        // Then
        DomainReg savedReg = domainRegService.getDomainReg(savedModifyReg.getId());
        assertThat(savedReg).isEqualTo(savedModifyReg);
        assertThat(savedModifyReg.getRegDomain().getWords().size()).isEqualTo(9L);
        assertThat(domainRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(10L);
    }

    @Test
    public void registDeleteDomain() {
        // When
        // create
        generateWords();
        List<Word> words = wordRepository.findAll();
        DomainRegistRequest request = makeRequest(words);
        DomainReg domainReg = domainRegService.create(request);
        // delete
        DomainReg savedDeleteReg = domainRegService.delete(domainReg.getDomain().getId());

        // Then
        DomainReg savedReg = domainRegService.getDomainReg(savedDeleteReg.getId());
        assertThat(savedReg).isEqualTo(savedDeleteReg);
        assertThat(domainRegRepository.count()).isEqualTo(2L);
        assertThat(wordRepository.count()).isEqualTo(10L);
    }

    @Test
    public void approveCreateDomain() {
        // When
        // generateWords
        generateWords();
        List<Word> words = wordRepository.findAll();
        // create
        DomainRegistRequest domainRegistRequest = makeDomRequest(words);
        DomainReg domainReg = domainRegService.create(domainRegistRequest);
        // approve
        DomainReg approvedDomainReg = domainRegService.processDomainReg(domainReg.getId(), ProcessType.APPROVED);

        // Then
        assertThat(domainRegRepository.count()).isEqualTo(1L);
        // 두 등록은 처리상태가 다르지만 @EqualsAndHashCode 로 인해 id 값이 같으면 같음
        assertThat(domainReg).isEqualTo(approvedDomainReg);
        // 하지만 실제로 처리상태는 다름
        assertThat(domainReg.getRegistration().getProcessType()).isNotEqualTo(approvedDomainReg.getRegistration().getProcessType());
        Domain domain = domainService.getDomain(domainReg.getDomain().getId());
        assertThat(domain.getStatus()).isEqualTo(ProcessType.APPROVED);
    }

    @Test
    public void rejectCreateDomain() {
        // When
        // generateWords
        generateWords();
        List<Word> words = wordRepository.findAll();
        // create
        DomainRegistRequest domainRegistRequest = makeDomRequest(words);
        DomainReg domainReg = domainRegService.create(domainRegistRequest);
        // approve
        DomainReg rejectedDomainReg = domainRegService.processDomainReg(domainReg.getId(), ProcessType.REJECTED);

        // Then
        assertThat(domainRegRepository.count()).isEqualTo(1L);
        // 두 등록은 처리상태가 다르지만 @EqualsAndHashCode 로 인해 id 값이 같으면 같음
        assertThat(domainReg).isEqualTo(rejectedDomainReg);
        // 하지만 실제로 처리상태는 다름
        assertThat(domainReg.getRegistration().getProcessType()).isNotEqualTo(rejectedDomainReg.getRegistration().getProcessType());
        Domain domain = domainService.getDomain(domainReg.getDomain().getId());
        assertThat(domain.getStatus()).isEqualTo(ProcessType.REJECTED);
    }

    @Test
    public void approveModifyDomain() {
        // When
        // create
        generateWords();
        List<Word> words = wordRepository.findAll();
        DomainRegistRequest request = makeRequest(words);
        DomainReg domainReg = domainRegService.create(request);
        // modify
        request.setDescription("this is new domain");
        request.getWords().remove(3);
        DomainReg savedModifyReg = domainRegService.modify(request, domainReg.getDomain().getId());
        // approve
        DomainReg approvedReg = domainRegService.processDomainReg(savedModifyReg.getId(), ProcessType.APPROVED);

        // Then
        Domain domain = domainService.getDomain(approvedReg.getDomain().getId());
        assertThat(domainRepository.count()).isEqualTo(1L);
        assertThat(domain.getDescription()).isEqualTo(request.getDescription());
        List<Word> domainWords = domain.getWords();
        List<Word> approvedWords = approvedReg.getRegDomain().getWords();

        for (int i = 0; i < domainWords.size(); i++) {
            assertThat(domainWords.get(i)).isEqualTo(approvedWords.get(i));
        }
    }

    @Test
    public void rejectModifyDomain() {
        // When
        // create
        generateWords();
        List<Word> words = wordRepository.findAll();
        DomainRegistRequest request = makeRequest(words);
        DomainReg domainReg = domainRegService.create(request);
        // modify
        request.setDescription("this is new domain");
        request.getWords().remove(3);
        assertThat(request.getWords().size()).isEqualTo(9);
        DomainReg savedModifyReg = domainRegService.modify(request, domainReg.getDomain().getId());
        // approve
        DomainReg rejectedReg = domainRegService.processDomainReg(savedModifyReg.getId(), ProcessType.REJECTED);

        // Then
        Domain domain = domainService.getDomain(rejectedReg.getDomain().getId());
        assertThat(domainRepository.count()).isEqualTo(1L);

        List<Word> domainWords = domain.getWords();
        List<Word> domainRegWords = domainReg.getRegDomain().getWords();
        assertThat(domainWords.size()).isEqualTo(10);
        for (int i = 0; i < domainWords.size(); i++) {
            assertThat(domainWords.get(i)).isEqualTo(domainRegWords.get(i));
        }
    }

    @Test
    public void approveDeleteDomain() {
        // When
        // create
        generateWords();
        List<Word> words = wordRepository.findAll();
        DomainRegistRequest request = makeRequest(words);
        DomainReg domainReg = domainRegService.create(request);
        // delete
        DomainReg savedModifyReg = domainRegService.delete(domainReg.getDomain().getId());
        // approve
        domainRegService.processDomainReg(savedModifyReg.getId(), ProcessType.APPROVED);

        // Then
        assertThat(domainRegRepository.count()).isEqualTo(0L);
        assertThat(domainRepository.count()).isEqualTo(0L);
    }

    @Test
    public void rejectDeleteDomain() {
        // When
        // create
        generateWords();
        List<Word> words = wordRepository.findAll();
        DomainRegistRequest request = makeRequest(words);
        DomainReg domainReg = domainRegService.create(request);
        // delete
        DomainReg savedModifyReg = domainRegService.delete(domainReg.getDomain().getId());
        // reject
        domainRegService.processDomainReg(savedModifyReg.getId(), ProcessType.REJECTED);

        // Then
        assertThat(domainRegRepository.count()).isEqualTo(2L);
        assertThat(domainRepository.count()).isEqualTo(1L);
    }

    private DomainRegistRequest makeDomRequest(List<Word> words) {
        return DomainRegistRequest.builder()
                .dataType(DataType.VARCHAR2)
                .db(DB.ORACLE)
                .description("test domain")
                .nullable(true)
                .scale(0)
                .size(20)
                .words(words)
                .build();
    }

    private DomainRegistRequest makeRequest(List<Word> words) {
        return DomainRegistRequest.builder()
                .words(words)
                .size(20)
                .scale(0)
                .nullable(true)
                .description("this is test domain")
                .db(DB.ORACLE)
                .dataType(DataType.VARCHAR2)
                .build();
    }

    private WordRegistRequest makeRequest(int idx) {
        return WordRegistRequest.builder()
                .engName("TST" + idx)
                .name("테스트" + idx)
                .orgEngName("TEST" + idx)
                .build();
    }

    public void generateWords() {
        List<WordRegistRequest> requests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            requests.add(makeRequest(i));
        }

        List<WordReg> wordRegs = new ArrayList<>();
        for (WordRegistRequest request : requests) {
            wordRegs.add(wordRegService.create(request));
        }
    }
}