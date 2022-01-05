package kvoting.intern.flowerwebapp.constraint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<Constraint, Long> {
	Page<Constraint> findByBase_NameIgnoreCaseContains(String name, Pageable pageable);

	Page<Constraint> findByBase_InputType(InputType inputType, Pageable pageable);

	Page<Constraint> findByBase_ValueContains(String value, Pageable pageable);

	Page<Constraint> findByBase_NameIgnoreCaseContainsAndBase_InputType(String base_name, InputType base_inputType,
		Pageable pageable);

	default boolean exists(String base_name, InputType base_inputType,
		String base_value) {
		return existsByBase_NameIgnoreCaseAndBase_InputTypeAndBase_ValueIgnoreCase(base_name, base_inputType,
			base_value);
	}

	boolean existsByBase_NameIgnoreCaseAndBase_InputTypeAndBase_ValueIgnoreCase(String base_name,
		InputType base_inputType,
		String base_value);
}
