package kvoting.intern.flowerwebapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.serialize.AccountSerializer;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CC_DOMAIN_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Domain implements Item {

    @Id
    @Column(name = "DOMAIN_ID")
    @EqualsAndHashCode.Include
    @GeneratedValue
    private Long id;

    @Embedded
    private DomainBase base;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODFR_ID", referencedColumnName = "USER_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonSerialize(using = AccountSerializer.class)
    private Account modifier;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @Column(name = "MODFR_NM")
    private String modifierName;

    @Column(name = "MODF_TM")
    private LocalDateTime modifiedTime;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<DomainReg> domainRegs = new HashSet<>();

    @ManyToMany(mappedBy = "domains", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "domains", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<Dict> dicts = new HashSet<>();

    @PrePersist
    public void registered() {
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
