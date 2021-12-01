package kvoting.intern.flowerwebapp.domain.registeration;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class DomainRegPK implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "DOMAIN_ID")
    private long id;

    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "DOMAIN_SEQNO")
    private long seqNo;
}
