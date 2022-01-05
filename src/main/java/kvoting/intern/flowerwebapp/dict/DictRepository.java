package kvoting.intern.flowerwebapp.dict;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DictRepository extends JpaRepository<Dict, Long> {
	Page<Dict> findByBase_NameIgnoreCaseContains(String name, Pageable pageable);

	Page<Dict> findByBase_EngNameIgnoreCaseContains(String engName, Pageable pageable);

	Page<Dict> findByBase_ScreenNameIgnoreCaseContains(String screenName, Pageable pageable);

	default Page<Dict> findByName(String name, Pageable pageable) {
		return findByBase_NameIgnoreCaseContainsOrBase_EngNameIgnoreCaseContainsOrBase_ScreenNameIgnoreCaseContains(
			name, name, name, pageable);
	}

	Page<Dict> findByBase_NameIgnoreCaseContainsOrBase_EngNameIgnoreCaseContainsOrBase_ScreenNameIgnoreCaseContains(
		String base_name, String base_engName, String base_screenName, Pageable pageable);

	@Query(value = "select * from cc_dict_word_tc d where d.WORD_ID = ?1",
		countQuery = "select count(*) from cc_dict_word_tc d where d.WORD_ID = ?1",
		nativeQuery = true)
	Page<Dict> findByWord(Long id, Pageable pageable);

	boolean existsByBase_EngNameIgnoreCase(String base_engName);
}
