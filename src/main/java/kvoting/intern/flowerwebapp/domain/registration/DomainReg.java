package kvoting.intern.flowerwebapp.domain.registration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.registration.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity(name = "CC_DOMAIN_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "DOMAIN")
public class DomainReg extends Registration {
    @Embedded
    private DomainBase domainBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    private Domain domain;

    @Override
    public void registered() {
        super.registered();
        if (domainBase == null) {
            return;
        }
        String name = domainBase.getDataType().toString() + domainBase.getSize();
        if (domainBase.getScale() > 0) {
            name += "." + domainBase.getScale();
        }
        if (!domainBase.getNullable()) {
            name += "NN";
        } else {
            name += "NY";
        }

        domainBase.setEngName(name);
    }
}