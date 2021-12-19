package kvoting.intern.flowerwebapp.domain.registration;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.domain.serializer.DomainSerializer;
import kvoting.intern.flowerwebapp.item.registration.Registration;
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
public class DomainReg extends Registration<Domain, DomainBase> {
    @Embedded
    private DomainBase base;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonSerialize(using = DomainSerializer.class)
    private Domain item;

    @Override
    public void registered() {
        super.registered();
        if (base == null) {
            return;
        }
        String name = base.getDataType().toString();
        if (base.getSize() != null) {
            name += base.getSize();
        }
        if (base.getScale() != null) {
            name += "." + base.getScale();
        }
        if (base.getNullable() != null) {
            if (!base.getNullable()) {
                name += "NN";
            } else {
                name += "NY";
            }
        }
        base.setName(name);
    }
}