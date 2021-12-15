package kvoting.intern.flowerwebapp.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository<T extends Registration> extends JpaRepository<T, Long> {
}
