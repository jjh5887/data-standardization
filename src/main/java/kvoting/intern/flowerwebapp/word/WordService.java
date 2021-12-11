package kvoting.intern.flowerwebapp.word;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
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
    private final DictService dictService;
    private final DomainRegService domainRegService;
    private final DictRegService dictRegService;

    public Word save(Word word) {
        Word save = wordRepository.save(word);
        for (Domain domain : save.getDomains()) {
            domain.setUp();
            domainService.save(domain);
        }
        for (Dict dict : save.getDicts()) {
            dict.setUp();
            dictService.save(dict);
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
        word = getWord(word.getId());
        for (Domain domain : word.getDomains()) {
            if (domain.getWords().size() == 1) {
                domainService.delete(domain);
                continue;
            }
            domain.getWords().remove(word);
            domain.setUp();
            domainService.save(domain);
        }
        for (Dict dict : word.getDicts()) {
            if (dict.getWords().size() == 1) {
                dictService.delete(dict);
                continue;
            }
            dict.getWords().remove(word);
            dict.setUp();
            dictService.save(dict);
        }

        wordRepository.flush();

        for (DomainReg domainReg : word.getDomainRegs()) {
            domainReg.getWords().remove(word);
            domainRegService.save(domainReg);
        }
        for (DictReg dictReg : word.getDictRegs()) {
            dictReg.getWords().remove(word);
            dictRegService.save(dictReg);
        }
        wordRepository.delete(word);
    }

    public boolean existsByEngName(String engName) {
        return wordRepository.existsByWordBase_EngName(engName);
    }
}
