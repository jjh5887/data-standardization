package kvoting.intern.flowerwebapp.constraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintReg;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CC_CONSTRAINT_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Constraint implements Item {
    @Id
    @GeneratedValue
    @Column(name = "CONSTRAINT_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Embedded
    private ConstraintBase constraintBase;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<ConstraintReg> constraintRegs = new HashSet<>();

    @ManyToMany(mappedBy = "constraints", fetch = FetchType.LAZY)
    private Set<CustomDomain> customDomains = new HashSet<>();
}
