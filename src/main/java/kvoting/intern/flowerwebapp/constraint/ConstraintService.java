package kvoting.intern.flowerwebapp.constraint;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.ctdomain.InputType;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;

@Service
public class ConstraintService extends ItemServiceImpl {
	private final ConstraintRepository constraintRepository;

	public ConstraintService(ConstraintRepository constraintRepository, ConstraintRepository constraintRepository1) {
		super(constraintRepository);
		this.constraintRepository = constraintRepository1;
	}

	@Transactional(readOnly = true)
	public Page<Constraint> getByName(String name, Pageable pageable) {
		return constraintRepository.findByBase_Name(name, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Constraint> getByValue(String value, Pageable pageable) {
		return constraintRepository.findByBase_Value(value, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Constraint> getByInput(InputType inputType, Pageable pageable) {
		return constraintRepository.findByBase_InputType(inputType, pageable);
	}

	@Override
	public Item getDetail(Long id) {
		Constraint constraint = (Constraint)get(id);
		Hibernate.initialize(constraint.getCustomDomains());
		return constraint;
	}
}
