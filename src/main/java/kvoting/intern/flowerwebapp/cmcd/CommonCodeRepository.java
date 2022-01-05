package kvoting.intern.flowerwebapp.cmcd;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
	Page<CommonCode> findByBase_CodeIgnoreCaseContains(String code, Pageable pageable);

	Page<CommonCode> findByBase_CodeNameIgnoreCaseContains(String codeName, Pageable pageable);

	// Page<CommonCode> findByHighCommonCodeIdOrderByBase_Order(Long id, Pageable pageable);

	default boolean exists(String code, String name) {
		return existsByBase_CodeIgnoreCaseAndBase_CodeNameIgnoreCase(code, name);
	}

	// boolean existsByBase_CodeIgnoreCaseAndDictId(String base_code, Long id);

	boolean existsByBase_CodeIgnoreCaseAndBase_CodeNameIgnoreCase(String base_code, String base_codeName);

	// boolean existsByBase_OrderAndDictId(Long base_order, Long dictId);
}
