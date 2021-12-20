package kvoting.intern.flowerwebapp.constraint.registration;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CC_CONSTRAINT_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "CONSTRAINT")
public class ConstraintReg extends Registration<Constraint, ConstraintBase> {

    @Embedded
    ConstraintBase base;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONSTRAINT_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Constraint item;

    @Column(name = "CONSTRAINT_ID", insertable = false, updatable = false)
    private Long ItemId;

    @ManyToMany(mappedBy = "constraints", fetch = FetchType.LAZY)
    private Set<CustomDomain> customDomains = new HashSet<>();
}
