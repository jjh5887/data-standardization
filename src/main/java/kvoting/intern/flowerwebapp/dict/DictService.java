package kvoting.intern.flowerwebapp.dict;

import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DictService {
    private final DictRepository dictRepository;
    private final WordRepository wordRepository;

    public Dict save(Dict dict) {
        return dictRepository.save(dict);
    }

    public Dict getDict(Long id) {
        return dictRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Page<Dict> getDictByName(String name, Pageable pageable) {
        return dictRepository.findByDictBase_NameContains(name, pageable);
    }

    public Page<Dict> getDictByEngName(String name, Pageable pageable) {
        return dictRepository.findByDictBase_EngNameContains(name, pageable);
    }

    public Page<Dict> getDictByScreenName(String name, Pageable pageable) {
        return dictRepository.findByDictBase_ScreenNameContains(name, pageable);
    }

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

    public void delete(Long id) {
        dictRepository.delete(getDict(id));
    }

    public void delete(Dict dict) {
        dictRepository.delete(dict);
    }

}
