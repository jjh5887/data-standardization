package kvoting.intern.flowerwebapp.word;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findByNameContains(String name, Pageable pageable);

    boolean existsByEngName(String engName);

    Optional<Word> findByEngName(String engName);
}
