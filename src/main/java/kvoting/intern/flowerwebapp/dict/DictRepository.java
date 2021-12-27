package kvoting.intern.flowerwebapp.dict;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DictRepository extends JpaRepository<Dict, Long> {
    Page<Dict> findByBase_NameContains(String name, Pageable pageable);

    Page<Dict> findByBase_EngNameContains(String engName, Pageable pageable);

    Page<Dict> findByBase_ScreenNameContains(String screenName, Pageable pageable);

    @Query("select distinct c from CC_DICT_TC c where c.base.engName like concat('%', ?1, '%') or c.base.name like concat('%', ?2, '%') or c.base.screenName like concat('%', ?3, '%')")
    Page<Dict> findByName(String engName, String name, String screenName, Pageable pageable);

    @Query(value = "select * from cc_dict_word_tc d where d.WORD_ID = ?1",
            countQuery = "select count(*) from cc_dict_word_tc d where d.WORD_ID = ?1",
            nativeQuery = true)
    Page<Dict> findByWord(Long id, Pageable pageable);
}
