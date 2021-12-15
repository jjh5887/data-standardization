package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CC_DOMAIN_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Domain {

    @Id
    @Column(name = "DOMAIN_ID")
    @EqualsAndHashCode.Include
    @GeneratedValue
    private Long id;

    @Embedded
    private DomainBase domainBase;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<DomainReg> domainRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "domains", fetch = FetchType.LAZY)
    private Set<Dict> dicts = new HashSet<>();
}
