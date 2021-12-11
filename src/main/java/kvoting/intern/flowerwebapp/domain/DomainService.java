package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DomainService {
    private final DomainRepository domainRepository;
    private final DictService dictService;
    private final DictRegService dictRegService;

    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    public Domain getDomain(Long id) {
        return domainRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Page<Domain> getDomainByNameContains(String name, Pageable pageable) {
        return domainRepository.findByDomainBase_NameContains(name, pageable);
    }

    public Page<Domain> getDomainByEngNameContains(String engName, Pageable pageable) {
        return domainRepository.findByDomainBase_EngNameContains(engName, pageable);
    }

    public void delete(Domain domain) {
        domain = getDomain(domain.getId());
        for (Dict dict : domain.getDicts()) {
            if (dict.getDomains().size() == 1) {
                dictService.delete(dict);
                continue;
            }
            dict.getDomains().remove(domain);
            dictService.save(dict);
        }

        domainRepository.flush();

        for (DictReg dictReg : domain.getDictRegs()) {
            dictReg.getDomains().remove(domain);
            dictRegService.save(dictReg);
        }
        domainRepository.delete(domain);
    }
}
