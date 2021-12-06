package kvoting.intern.flowerwebapp.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Long> {
    Page<Domain> findByNameContains(String name, Pageable pageable);

    Page<Domain> findByEngNameContains(String engName, Pageable pageable);
}
