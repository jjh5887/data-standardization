package kvoting.intern.flowerwebapp.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kvoting.intern.flowerwebapp.item.registration.ProcessType;

public interface DomainRepository extends JpaRepository<Domain, Long> {
	Page<Domain> findByBase_NameIgnoreCaseContains(String engName, Pageable pageable);

	Page<Domain> findByBase_DataType(DataType dataType, Pageable pageable);

	Page<Domain> findByBase_Db(DB db, Pageable pageable);

	Page<Domain> findByBase_DbAndBase_DataType(DB db, DataType dataType, Pageable pageable);

	Page<Domain> findByBase_Size(Integer size, Pageable pageable);

	Page<Domain> findByBase_Scale(Integer scale, Pageable pageable);

	Page<Domain> findByBase_Nullable(Boolean nullable, Pageable pageable);

	Page<Domain> findByStatus(ProcessType status, Pageable pageable);
}
