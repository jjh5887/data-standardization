package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DomainService extends ItemServiceImpl {
    private final DictService dictService;
    private final DictRegService dictRegService;

    public DomainService(DomainRepository domainRepository, DictService dictService, DictRegService dictRegService) {
        super(domainRepository);
        this.dictService = dictService;
        this.dictRegService = dictRegService;
    }

    @Transactional(readOnly = true)
    public Page<Domain> getDomainByEngNameContains(String engName, Pageable pageable) {
        return ((DomainRepository) itemRepository).findByBase_NameContains(engName, pageable);
    }

    @Override
    public void delete(Item domain) throws Throwable {
        domain = get(((Domain) domain).getId());
        for (Dict dict : ((Domain) domain).getDicts()) {
            if (dict.getDomains().size() == 1 && dict.getCustomDomains().size() == 0) {
                dictService.delete(dict);
                continue;
            }
            dict.getDomains().remove(domain);
            dictService.save(dict);
        }

        itemRepository.flush();

        for (DictReg dictReg : ((Domain) domain).getDictRegs()) {
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
