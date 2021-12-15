package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.registration.ItemService;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class DomainService implements ItemService<Domain, DomainRegistRequest> {
    private final DomainRepository domainRepository;
    private final DictService dictService;
    private final DictRegService dictRegService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<Domain> getDomainByEngNameContains(String engName, Pageable pageable) {
        return domainRepository.findByDomainBase_EngNameContains(engName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Domain get(Long id) {
        return domainRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Override
    public Domain map(DomainRegistRequest request) {
        Domain map = modelMapper.map(request, Domain.class);
        map.setStatus(ProcessType.UNHANDLED);
        map.setDomainRegs(new HashSet<>());
        return map;
    }

    @Override
    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }


    @Override
    public void setStatus(Domain item, ProcessType type) {
        item.setStatus(type);
    }

    @Override
    public void delete(Domain domain) {
        domain = get(domain.getId());
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
