package kvoting.intern.flowerwebapp.ctdomain;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomDomainService extends ItemServiceImpl {
    private final DictService dictService;
    private final DictRegService dictRegService;

    public CustomDomainService(CustomDomainRepository customDomainRepository, DictService dictService, DictRegService dictRegService) {
        super(customDomainRepository);
        this.dictService = dictService;
        this.dictRegService = dictRegService;
    }

    @Transactional(readOnly = true)
    public CustomDomain getDetail(Long id) throws Throwable {
        CustomDomain customDomain = (CustomDomain) get(id);
        Hibernate.initialize(customDomain.getConstraints());
        Hibernate.initialize(customDomain.getDicts());
        Hibernate.initialize(customDomain.getDictRegs());
        return customDomain;
    }

    @Override
    public void delete(Item item) throws Throwable {
        for (Dict dict : ((CustomDomain) item).getDicts()) {
            if (dict.getCustomDomains().size() == 1 && dict.getDomains().size() == 0) {
                dictService.delete(dict);
                continue;
            }
            dict.getCustomDomains().remove(item);
            dictService.save(dict);
        }

        itemRepository.flush();

        for (DictReg dictReg : ((CustomDomain) item).getDictRegs()) {
            dictReg.getCustomDomains().remove(item);
            dictRegService.save(dictReg);
        }
        itemRepository.delete(item);
    }

    @Override
    public void delete(Long id) throws Throwable {
        delete(get(id));
    }
}
