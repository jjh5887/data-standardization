package kvoting.intern.flowerwebapp.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final DomainRepository domainRepository;

    public Domain saveDomain(Domain domain) {
        return domainRepository.save(domain);
    }

    public Page<Domain> getDomainByName(String name, Pageable pageable) {
        return domainRepository.findByNameContains(name, pageable);
    }
}
