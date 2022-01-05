package kvoting.intern.flowerwebapp.ctdomain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomDomainRepository
	extends org.springframework.data.jpa.repository.JpaRepository<CustomDomain, Long> {
	Page<CustomDomain> findByBase_NameIgnoreCaseContains(String engName, Pageable pageable);

	default boolean exists(String base_name, String base_db,
		String base_dataType) {
		return existsByBase_NameIgnoreCaseAndBase_DbIgnoreCaseAndBase_DataTypeIgnoreCase(base_name, base_db,
			base_dataType);
	}

	boolean existsByBase_NameIgnoreCaseAndBase_DbIgnoreCaseAndBase_DataTypeIgnoreCase(String base_name, String base_db,
		String base_dataType);
}
