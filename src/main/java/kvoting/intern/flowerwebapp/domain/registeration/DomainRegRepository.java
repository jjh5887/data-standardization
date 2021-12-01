package kvoting.intern.flowerwebapp.domain.registeration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRegRepository extends JpaRepository<DomainReg, Long> {
    Page<DomainReg> findByNameContains(String name, Pageable pageable);
}
