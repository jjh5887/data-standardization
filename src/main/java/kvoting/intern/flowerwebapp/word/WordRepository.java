package kvoting.intern.flowerwebapp.word;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Primary
public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findByBase_NameContains(String name, Pageable pageable);

    Page<Word> findByBase_EngNameContains(String engName, Pageable pageable);

    Page<Word> findByBase_OrgEngNameContains(String name, Pageable pageable);

    Optional<Word> findByBase_EngName(String engName);

    boolean existsByBase_EngName(String engName);
}
