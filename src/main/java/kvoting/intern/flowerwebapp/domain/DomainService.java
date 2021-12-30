package kvoting.intern.flowerwebapp.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;

@Service
public class DomainService extends ItemServiceImpl {

	private final DictService dictService;
	private final DictRegService dictRegService;

	public DomainService(DomainRepository domainRepository, DictService dictService,
		DictRegService dictRegService) {
		super(domainRepository);
		this.dictService = dictService;
		this.dictRegService = dictRegService;
	}

	@Transactional(readOnly = true)
	public Page<Domain> getByEngNameContains(String engName, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_NameIgnoreCaseContains(engName, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Domain> get(DataType dataType, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_DataType(dataType, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Domain> get(DB db, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_Db(db, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Domain> get(DB db, DataType type, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_DbAndBase_DataType(db, type,
			pageable);
	}

	@Transactional(readOnly = true)
	public Page<Domain> getBySize(Integer size, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_Size(size, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Domain> getByScale(Integer scale, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_Scale(scale, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Domain> getByNullable(Boolean nullable, Pageable pageable) {
		return ((DomainRepository)itemRepository).findByBase_Nullable(nullable, pageable);
	}

	@Override
	public Item getDetail(Long id) {
		return get(id);
	}

	@Override
	public void delete(Item domain) {
		domain = get(domain.getId());
		for (Dict dict : ((Domain)domain).getDicts()) {
			if (dict.getDomains().size() == 1 && dict.getCustomDomains().size() == 0) {
				dictService.delete(dict);
				continue;
			}
			dict.getDomains().remove(domain);
			dictService.save(dict);
		}

		itemRepository.flush();

		for (DictReg dictReg : ((Domain)domain).getDictRegs()) {
			dictReg.getDomains().remove(domain);
			dictRegService.save(dictReg);
		}
		itemRepository.delete(domain);
	}

	@Override
	public void delete(Long id) throws Throwable {
		delete(get(id));
	}
}
