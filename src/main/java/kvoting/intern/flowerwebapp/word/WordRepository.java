package kvoting.intern.flowerwebapp.word;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
	Page<Word> findByBase_NameIgnoreCaseContains(String name, Pageable pageable);

	Page<Word> findByBase_EngNameIgnoreCaseContains(String engName, Pageable pageable);

	Page<Word> findByBase_OrgEngNameIgnoreCaseContains(String name, Pageable pageable);

	Optional<Word> findByBase_IgnoreCaseEngName(String engName);

	boolean existsByBase_EngNameIgnoreCaseAndBase_NameIgnoreCaseAndBase_OrgEngNameIgnoreCase(String base_engName,
		String base_name, String base_orgEngName);

	default boolean exists(String base_engName, String base_name, String base_orgEngName) {
		return existsByBase_EngNameIgnoreCaseAndBase_NameIgnoreCaseAndBase_OrgEngNameIgnoreCase(base_engName, base_name,
			base_orgEngName);
	}
}
