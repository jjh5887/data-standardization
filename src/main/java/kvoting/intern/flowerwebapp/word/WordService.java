package kvoting.intern.flowerwebapp.word;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordService extends ItemServiceImpl<Word> {
    private final DictService dictService;
    private final DictRegService dictRegService;

    public WordService(WordRepository wordRepository, DictService dictService, DictRegService dictRegService) {
        super(wordRepository);
        this.dictService = dictService;
        this.dictRegService = dictRegService;
    }

    @Transactional(readOnly = true)
    public Word getWordByEng(String engName) {
        return ((WordRepository) itemRepository).findByWordBase_EngName(engName).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Transactional(readOnly = true)
    public Page<Word> getByName(String name, Pageable pageable) {
        return ((WordRepository) itemRepository).findByWordBase_NameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsByEngName(String engName) {
        return ((WordRepository) itemRepository).existsByWordBase_EngName(engName);
    }

    @Override
    public Word save(Word word) {
        Word save = itemRepository.save(word);
        for (Dict dict : save.getDicts()) {
            dict.setUp();
            dictService.save(dict);
        }
        return save;
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

        itemRepository.flush();

        for (DictReg dictReg : word.getDictRegs()) {
            dictReg.getWords().remove(word);
            dictRegService.save(dictReg);
        }
        itemRepository.delete(word);
    }

    @Override
    public void delete(Long id) {
        delete(get(id));
    }
}
