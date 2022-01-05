package kvoting.intern.flowerwebapp.item.registration;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface RegistrationRepository<T extends Registration> extends JpaRepository<T, Long> {
	boolean existsByIdAndItemId(Long id, Long itemId);
}
