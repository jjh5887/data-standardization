package kvoting.intern.flowerwebapp.word;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.ctdomain.registration.CustomDomainRegService;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;

@Service
public class WordService extends ItemServiceImpl {
	private final DomainService domainService;
	private final DomainRegService domainRegService;
	private final DictService dictService;
	private final DictRegService dictRegService;
	private final CustomDomainService customDomainService;
	private final CustomDomainRegService customDomainRegService;

	public WordService(WordRepository wordRepository, DomainService domainService,
		DomainRegService domainRegService, DictService dictService,
		DictRegService dictRegService, CustomDomainService customDomainService,
		CustomDomainRegService customDomainRegService) {
		super(wordRepository);
		this.domainService = domainService;
		this.domainRegService = domainRegService;
		this.dictService = dictService;
		this.dictRegService = dictRegService;
		this.customDomainService = customDomainService;
		this.customDomainRegService = customDomainRegService;
	}

	@Transactional(readOnly = true)
	public Word getByEng(String engName) {
		return ((WordRepository)itemRepository).findByBase_IgnoreCaseEngName(engName).orElseThrow(() -> {
			throw new RuntimeException();
		});
	}

	@Transactional(readOnly = true)
	public Page<Word> getByEng(String engName, Pageable pageable) {
		return ((WordRepository)itemRepository).findByBase_EngNameIgnoreCaseContains(engName, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Word> getByOrgEng(String orgEngName, Pageable pageable) {
		return ((WordRepository)itemRepository).findByBase_OrgEngNameIgnoreCaseContains(orgEngName, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Word> getByName(String name, Pageable pageable) {
		return ((WordRepository)itemRepository).findByBase_NameIgnoreCaseContains(name, pageable);
	}

	@Transactional(readOnly = true)
	public boolean existsByEngName(String engName) {
		return ((WordRepository)itemRepository).existsByBase_EngNameIgnoreCase(engName);
	}

	@Override
	public Item getDetail(Long id) {
		return get(id);
	}

	@Override
	public Word save(Item word) {
		Word save = (Word)itemRepository.save(word);
		for (Dict dict : save.getDicts()) {
			dict.setUp();
			dictService.save(dict);
		}
		for (Domain domain : save.getDomains()) {
			domain.setUp();
			domainService.save(domain);
		}
		for (CustomDomain customDomain : save.getCustomDomains()) {
			customDomain.setUp();
			customDomainService.save(customDomain);
		}

		return save;
	}

	@Override
	public void delete(Item item) {
		Word word = (Word)get(item.getId());
		for (Domain domain : word.getDomains()) {
			if (domain.getWords().size() == 1) {
				domainService.delete(domain);
				continue;
			}
			domain.getWords().remove(domain);
			domain.setUp();
			domainService.save(domain);
		}

		itemRepository.flush();

		for (DomainReg domainReg : word.getDomainRegs()) {
			domainReg.getWords().remove(domainReg);
			domainRegService.save(domainReg);
		}

		for (CustomDomain customDomain : word.getCustomDomains()) {

		}

		for (Dict dict : word.getDicts()) {
			if (dict.getWords().size() == 1) {
				dictService.delete(dict);
				continue;
			}
			dict.getWords().remove(item);
			dict.setUp();
			dictService.save(dict);
		}

		itemRepository.flush();

		for (DictReg dictReg : word.getDictRegs()) {
			dictReg.getWords().remove(item);
			dictRegService.save(dictReg);
		}
		itemRepository.delete(item);
	}

	@Override
	public void delete(Long id) throws Throwable {
		delete(get(id));
	}
}
