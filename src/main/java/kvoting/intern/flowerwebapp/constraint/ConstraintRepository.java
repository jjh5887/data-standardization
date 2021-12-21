package kvoting.intern.flowerwebapp.constraint;

import kvoting.intern.flowerwebapp.ctdomain.InputType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<Constraint, Long> {
    Page<Constraint> findByBase_Name(String name, Pageable pageable);

    Page<Constraint> findByBase_InputType(InputType inputType, Pageable pageable);

    Page<Constraint> findByBase_Value(String value, Pageable pageable);

//    Optional<Constraint> findByAll(String constraintBase_name, String constraintBase_value, InputType constraintBase_inputType);
}
