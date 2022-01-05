package kvoting.intern.flowerwebapp.ctdomain;

import org.hibernate.Hibernate;
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
public class CustomDomainService extends ItemServiceImpl {
	private final DictService dictService;
	private final DictRegService dictRegService;

	public CustomDomainService(CustomDomainRepository customDomainRepository, DictService dictService,
		DictRegService dictRegService) {
		super(customDomainRepository);
		this.dictService = dictService;
		this.dictRegService = dictRegService;
	}

	@Transactional(readOnly = true)
	public CustomDomain getDetail(Long id) {
		CustomDomain customDomain = (CustomDomain)get(id);
		Hibernate.initialize(customDomain.getConstraints());
		return customDomain;
	}

	@Transactional(readOnly = true)
	public Page<CustomDomain> getByName(String name, Pageable pageable) {
		return ((CustomDomainRepository)itemRepository).findByBase_NameIgnoreCaseContains(name, pageable);
	}

	@Override
	public void delete(Item item) {
		for (Dict dict : ((CustomDomain)item).getDicts()) {
			if (dict.getCustomDomains().size() == 1 && dict.getDomains().size() == 0) {
				dictService.delete(dict);
				continue;
			}
			dict.getCustomDomains().remove(item);
			dictService.create(dict);
		}

		itemRepository.flush();

		for (DictReg dictReg : ((CustomDomain)item).getDictRegs()) {
			dictReg.getCustomDomains().remove(item);
			dictRegService.save(dictReg);
		}
		itemRepository.delete(item);
	}

	@Override
	public void delete(Long id) {
		delete(get(id));
	}

	@Override
	public boolean exists(Item item) {
		CustomDomain customDomain = (CustomDomain)item;
		return ((CustomDomainRepository)itemRepository)
			.exists(customDomain.getBase().getName(),
				customDomain.getBase().getDb(),
				customDomain.getBase().getDataType());
	}
}
