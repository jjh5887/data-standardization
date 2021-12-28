package kvoting.intern.flowerwebapp.constraint.registration;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "CC_CONSTRAINT_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "CONSTRAINT")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "CONSTRAINT_ID"}))
public class ConstraintReg extends Registration<Constraint, ConstraintBase> {

	@Embedded
	ConstraintBase base;
	@Column(name = "CONSTRAINT_ID", insertable = false, updatable = false)
	private Long itemId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSTRAINT_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Constraint item;

	@ManyToMany(mappedBy = "constraints", fetch = FetchType.LAZY)
	private Set<CustomDomain> customDomains = new HashSet<>();
}
