package kvoting.intern.flowerwebapp.word;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findByWordBase_NameContains(String name, Pageable pageable);

    Optional<Word> findByWordBase_EngName(String engName);

    boolean existsByWordBase_EngName(String engName);
}
