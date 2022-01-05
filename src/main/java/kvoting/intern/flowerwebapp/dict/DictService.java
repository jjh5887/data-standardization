package kvoting.intern.flowerwebapp.dict;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;

@Service
public class DictService extends ItemServiceImpl {
	private final WordRepository wordRepository;

	public DictService(DictRepository dictRepository, WordRepository wordRepository) {
		super(dictRepository);
		this.wordRepository = wordRepository;
	}

	@Transactional(readOnly = true)
	public Page<Dict> get(String name, Pageable pageable) {
		return ((DictRepository)itemRepository).findByName(name, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Dict> get(List<Long> ids, Pageable pageable) {
		HashSet<Dict> set = new HashSet<>();
		for (Long id : ids) {
			Word word = wordRepository.findById(id).orElseThrow(() -> {
				throw new RuntimeException();
			});
			set.addAll(word.getDicts());
		}
		List<Dict> dicts = new ArrayList<>(set);
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), dicts.size());
		return new PageImpl<>(dicts.subList(start, end), pageable, dicts.size());
	}

	@Transactional(readOnly = true)
	public Page<Dict> getDictByName(String name, Pageable pageable) {
		return ((DictRepository)itemRepository).findByBase_NameIgnoreCaseContains(name, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Dict> getDictByEngName(String name, Pageable pageable) {
		return ((DictRepository)itemRepository).findByBase_EngNameIgnoreCaseContains(name, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Dict> getDictByScreenName(String name, Pageable pageable) {
		return ((DictRepository)itemRepository).findByBase_ScreenNameIgnoreCaseContains(name, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Dict getDetail(Long id) {
		Dict dict = (Dict)get(id);
		Hibernate.initialize(dict.getDomains());
		Hibernate.initialize(dict.getCustomDomains());
		Hibernate.initialize(dict.getCommonCodes());
		return dict;
	}

	@Override
	public boolean exists(Item item) {
		Dict dict = (Dict)item;
		return ((DictRepository)itemRepository).existsByBase_EngNameIgnoreCase(dict.getBase().getEngName());
	}
}
