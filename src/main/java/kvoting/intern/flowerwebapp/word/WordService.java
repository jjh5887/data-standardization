package kvoting.intern.flowerwebapp.word;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
import kvoting.intern.flowerwebapp.registration.ItemService;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class WordService implements ItemService<Word, WordRegistRequest> {
    private final WordRepository wordRepository;
    private final DomainService domainService;
    private final DictService dictService;
    private final DomainRegService domainRegService;
    private final DictRegService dictRegService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Word getWordByEng(String engName) {
        return wordRepository.findByWordBase_EngName(engName).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Transactional(readOnly = true)
    public Page<Word> getByName(String name, Pageable pageable) {
        return wordRepository.findByWordBase_NameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsByEngName(String engName) {
        return wordRepository.existsByWordBase_EngName(engName);
    }

    @Override
    public Word save(Word word) {
        Word save = wordRepository.save(word);
        for (Dict dict : save.getDicts()) {
            dict.setUp();
            dictService.save(dict);
        }
        return save;
    }

    @Override
    @Transactional(readOnly = true)
    public Word get(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Override
    public Word map(WordRegistRequest request) {
        Word map = modelMapper.map(request, Word.class);
        map.setStatus(ProcessType.UNHANDLED);
        map.setWordRegs(new HashSet<>());
        return map;
    }

    @Override
    public void delete(Word word) {
        word = get(word.getId());
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

        for (DictReg dictReg : word.getDictRegs()) {
            dictReg.getWords().remove(word);
            dictRegService.save(dictReg);
        }
        wordRepository.delete(word);
    }

    @Override
    public void setStatus(Word item, ProcessType type) {
        item.setStatus(type);
    }


}
