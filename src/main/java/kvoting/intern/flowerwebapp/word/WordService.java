package kvoting.intern.flowerwebapp.word;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.word.registration.WordRegRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WordService {
    private final WordRepository wordRepository;
    private final DomainService domainService;
    private final WordRegRepository wordRegRepository;

    public Word save(Word word) {
        Word save = wordRepository.save(word);
        for (Domain domain : save.getDomains()) {
            domain.setUp();
            domainService.save(domain);
        }
        return save;
    }

    public Page<Word> getByName(String name, Pageable pageable) {
        return wordRepository.findByWordBase_NameContains(name, pageable);
    }

    public Word getWordByEng(String engName) {
        return wordRepository.findByWordBase_EngName(engName).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Word getWord(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public void deleteWord(Word word) {
        wordRepository.delete(word);
    }

    public boolean existsByEngName(String engName) {
        return wordRepository.existsByWordBase_EngName(engName);
    }
}
