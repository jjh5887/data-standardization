package kvoting.intern.flowerwebapp.dict;

import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class DictService extends ItemServiceImpl {
    private final WordRepository wordRepository;

    public DictService(DictRepository dictRepository, WordRepository wordRepository) {
        super(dictRepository);
        this.wordRepository = wordRepository;
    }

    @Transactional(readOnly = true)
    public Page<Dict> getDictByName(String name, Pageable pageable) {
        return ((DictRepository) itemRepository).findByDictBase_NameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Dict> getDictByEngName(String name, Pageable pageable) {
        return ((DictRepository) itemRepository).findByDictBase_EngNameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Dict> getDictByScreenName(String name, Pageable pageable) {
        return ((DictRepository) itemRepository).findByDictBase_ScreenNameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Dict> getDictByWord(List<Word> words, Pageable pageable) {
        HashSet<Dict> set = new HashSet<>();
        for (Word word : words) {
            word = wordRepository.findById(word.getId()).get();
            set.addAll(word.getDicts());
        }
        List<Dict> dicts = new ArrayList<>(set);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dicts.size());
        return new PageImpl<>(dicts.subList(start, end), pageable, dicts.size());
    }

    @Transactional(readOnly = true)
    public Dict getDetail(Long id) throws Throwable {
        Dict dict = (Dict) get(id);
        Hibernate.initialize(dict.getWords());
        Hibernate.initialize(dict.getDictRegs());
        Hibernate.initialize(dict.getDomains());
        return dict;
    }
}
