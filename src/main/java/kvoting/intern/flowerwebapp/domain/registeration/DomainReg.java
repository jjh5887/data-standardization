package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.registration.Registration;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity(name = "CC_DOMAIN_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DomainRegPK.class)
public class DomainReg extends DomainBase {

    @Id
    private long seqNo;

    @Id
    private long id;

    @Embedded
    private Registration registration;
}