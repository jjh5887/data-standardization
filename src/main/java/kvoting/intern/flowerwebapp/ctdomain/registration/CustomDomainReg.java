package kvoting.intern.flowerwebapp.ctdomain.registration;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import kvoting.intern.flowerwebapp.ctdomain.InputType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity(name = "CC_CUSTOM_DOMAIN_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "CUSTOM_DOMAIN")
public class CustomDomainReg extends Registration<CustomDomain, CustomDomainBase> {

    @Embedded
    private CustomDomainBase base;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOM_DOMAIN_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private CustomDomain item;

    @Column(name = "CUSTOM_DOMAIN_ID", insertable = false, updatable = false)
    private Long ItemId;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_CUSTOM_DOMAIN_REG_CONSTRAINT_TC",
            joinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "CONSTRAINT_ID"))
    private List<Constraint> constraints;

    @Override
    public void registered() {
        super.registered();
        if (base == null) {
            return;
        }
        String name = base.getDataType();
        constraints.sort(new Comparator<Constraint>() {
            @Override
            public int compare(Constraint o1, Constraint o2) {
                return o1.getConstraintBase().getName().compareTo(o2.getConstraintBase().getName());
            }
        });

        if (constraints != null) {
            for (Constraint constraint : constraints) {
                ConstraintBase base = constraint.getConstraintBase();
                name += base.getName();
                if (base.getInputType() == InputType.BOOLEAN) {
                    if (base.getValue().equals("0")) {
                        name += "N";
                        continue;
                    }
                    if (base.getValue().equals("1")) {
                        name += "Y";
                        continue;
                    }
                    throw new RuntimeException();
                }
                name += base.getValue();
            }
        }
        base.setName(name);
    }

}
