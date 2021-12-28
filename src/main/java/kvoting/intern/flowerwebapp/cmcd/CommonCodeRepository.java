package kvoting.intern.flowerwebapp.cmcd;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    Page<CommonCode> findByBase_CodeContains(String code, Pageable pageable);

    Page<CommonCode> findByBase_CodeNameContains(String codeName, Pageable pageable);

    Page<CommonCode> findByHighCommonCodeIdOrderByBase_Order(Long id, Pageable pageable);
}
