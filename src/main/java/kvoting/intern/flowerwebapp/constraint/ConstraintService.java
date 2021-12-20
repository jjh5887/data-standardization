package kvoting.intern.flowerwebapp.constraint;

import kvoting.intern.flowerwebapp.ctdomain.InputType;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstraintService extends ItemServiceImpl {
    private final ConstraintRepository constraintRepository;

    public ConstraintService(ConstraintRepository constraintRepository, ConstraintRepository constraintRepository1) {
        super(constraintRepository);
        this.constraintRepository = constraintRepository1;
    }

    @Transactional(readOnly = true)
    public Page<Constraint> getByName(String name, Pageable pageable) {
        return constraintRepository.findByConstraintBase_Name(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Constraint> getByValue(String value, Pageable pageable) {
        return constraintRepository.findByConstraintBase_Value(value, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Constraint> getByInput(InputType inputType, Pageable pageable) {
        return constraintRepository.findByConstraintBase_InputType(inputType, pageable);
    }

}
