package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.registration.Registration;
import lombok.*;

import javax.persistence.*;

@Entity(name = "CC_DOMAIN_REG_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainReg {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "DOMAIN_REG_ID")
    private Long id;

    @Embedded
    private DomainBase regDomain;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOMAIN_ID")
    private Domain domain;

    @Embedded
    private Registration registration;
}