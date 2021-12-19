package kvoting.intern.flowerwebapp.constraint;

import kvoting.intern.flowerwebapp.ctdomain.InputType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<Constraint, Long> {
    Page<Constraint> findByConstraintBase_Name(String name, Pageable pageable);

    Page<Constraint> findByConstraintBase_InputType(InputType inputType, Pageable pageable);

    Page<Constraint> findByConstraintBase_Value(String value, Pageable pageable);

//    Optional<Constraint> findByAll(String constraintBase_name, String constraintBase_value, InputType constraintBase_inputType);
}
