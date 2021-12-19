package kvoting.intern.flowerwebapp.ctdomain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomDomainRepository extends JpaRepository<CustomDomain, Long> {
}
