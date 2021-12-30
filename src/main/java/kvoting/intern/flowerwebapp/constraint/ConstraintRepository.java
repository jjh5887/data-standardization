package kvoting.intern.flowerwebapp.constraint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<Constraint, Long> {
	Page<Constraint> findByBase_NameContains(String name, Pageable pageable);

	Page<Constraint> findByBase_InputType(InputType inputType, Pageable pageable);

	Page<Constraint> findByBase_Value(String value, Pageable pageable);

	Page<Constraint> findByBase_NameAndBase_InputType(String base_name, InputType base_inputType, Pageable pageable);
}
