package kvoting.intern.flowerwebapp.ctdomain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomDomainRepository
	extends org.springframework.data.jpa.repository.JpaRepository<CustomDomain, Long> {
	Page<CustomDomain> findByBase_NameIgnoreCaseContains(String engName, Pageable pageable);
}
