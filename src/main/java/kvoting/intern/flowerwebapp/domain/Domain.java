package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.domain.registeration.DomainReg;
import kvoting.intern.flowerwebapp.type.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "domain", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<DomainReg> domainRegs = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    @JoinTable(name = "CC_DOMAIN_WORD_CC",
            joinColumns = @JoinColumn(name = "DOMAIN_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words = new ArrayList<>();

    @PrePersist
    public void setUp() {
        String name = "";
        String engName = "";
        for (Word word : words) {
            name += word.getWordBase().getName() + "_";
            engName += word.getWordBase().getEngName() + "_";
        }
        this.domainBase.setName(name.substring(0, name.length() - 1));
        this.domainBase.setEngName(engName.substring(0, engName.length() - 1));
    }
}
