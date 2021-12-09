package kvoting.intern.flowerwebapp.dict;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.type.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_DICT_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dict {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "DICT_ID")
    private Long id;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @Embedded
    DictBase dictBase;

    @OneToMany(mappedBy = "dict", cascade = CascadeType.REMOVE)
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_DICT_WORD_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CC_DICT_DOMAIN_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "DOMAIN_ID"))
    private Set<Domain> domains = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;
}