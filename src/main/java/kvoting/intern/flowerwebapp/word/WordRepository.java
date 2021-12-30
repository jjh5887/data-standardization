package kvoting.intern.flowerwebapp.word;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface WordRepository extends JpaRepository<Word, Long> {
	Page<Word> findByBase_NameIgnoreCaseContains(String name, Pageable pageable);

	Page<Word> findByBase_EngNameIgnoreCaseContains(String engName, Pageable pageable);

	Page<Word> findByBase_OrgEngNameIgnoreCaseContains(String name, Pageable pageable);

	Optional<Word> findByBase_IgnoreCaseEngName(String engName);

	boolean existsByBase_EngNameIgnoreCase(String engName);
}
