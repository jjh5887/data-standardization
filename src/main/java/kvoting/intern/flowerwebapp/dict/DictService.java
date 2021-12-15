package kvoting.intern.flowerwebapp.dict;

import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.registration.ItemService;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
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
public class DictService implements ItemService<Dict, DictRegistRequest> {
    private final DictRepository dictRepository;
    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<Dict> getDictByName(String name, Pageable pageable) {
        return dictRepository.findByDictBase_NameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Dict> getDictByEngName(String name, Pageable pageable) {
        return dictRepository.findByDictBase_EngNameContains(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Dict> getDictByScreenName(String name, Pageable pageable) {
        return dictRepository.findByDictBase_ScreenNameContains(name, pageable);
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
    public Dict getDetail(Long id) {
        Dict dict = get(id);
        Hibernate.initialize(dict.getWords());
        Hibernate.initialize(dict.getDomains());
        return dict;
    }

    public void delete(Long id) {
        dictRepository.delete(get(id));
    }

    @Override
    public void delete(Dict dict) {
        dictRepository.delete(dict);
    }

    @Override
    public void setStatus(Dict item, ProcessType type) {
        item.setStatus(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Dict get(Long id) {
        return dictRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Override
    public Dict map(DictRegistRequest request) {
        Dict map = modelMapper.map(request, Dict.class);
        map.setStatus(ProcessType.UNHANDLED);
        map.setDictRegs(new HashSet<>());
        return map;
    }

    @Override
    public Dict save(Dict dict) {
        return dictRepository.save(dict);
    }

}
