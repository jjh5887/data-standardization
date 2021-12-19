package kvoting.intern.flowerwebapp.ctdomain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.registration.CustomDomainReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_CUSTOM_DOMAIN_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomDomain implements Item {
    @Id
    @GeneratedValue
    @Column(name = "CUSTOM_DOMAIN_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Embedded
    private CustomDomainBase customDomainBase;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_CUSTOM_DOMAIN_CONSTRAINT_TC",
            joinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_ID"),
            inverseJoinColumns = @JoinColumn(name = "CONSTRAINT_ID"))
    private List<Constraint> constraints;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<CustomDomainReg> customDomainRegs = new HashSet<>();

    @ManyToMany(mappedBy = "customDomains", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "customDomains", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<Dict> dicts = new HashSet<>();

}