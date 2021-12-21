package kvoting.intern.flowerwebapp.domain.registration;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DomainReg extends Registration<Domain, DomainBase> {
    @Embedded
    private DomainBase base;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Domain item;

    @Column(name = "DOMAIN_ID", insertable = false, updatable = false)
    private Long ItemId;
}