package kvoting.intern.flowerwebapp.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final DomainRepository domainRepository;

    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    public Domain getDomain(Long id) {
        return domainRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Page<Domain> getDomainByName(String name, Pageable pageable) {
        return domainRepository.findByDomainBase_NameContains(name, pageable);
    }

    public Page<Domain> getDomainByEngName(String engName, Pageable pageable) {
        return domainRepository.findByDomainBase_EngNameContains(engName, pageable);
    }

    public void delete(Domain domain) {
        domainRepository.delete(domain);
    }
}
