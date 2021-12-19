package kvoting.intern.flowerwebapp.item.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository<T extends Registration> extends JpaRepository<T, Long> {
}
