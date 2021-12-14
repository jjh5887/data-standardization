package kvoting.intern.flowerwebapp.cmcd;


import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_CMCD_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommonCode {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "CMCD_ID")
    private Long id;

    @Embedded
    private CommonCodeBase commonCodeBase;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @OneToMany(mappedBy = "commonCode", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<CommonCodeReg> commonCodeRegs = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    private Dict dict;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_CMCD_WORD_TC",
            joinColumns = @JoinColumn(name = "CMCD_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words;

    @PrePersist
    public void setUp() {
        String name = "";
        for (Word word : words) {
            name += word.getWordBase().getName() + " ";
        }
        this.commonCodeBase.setCodeName(name.substring(0, name.length() - 1));
    }

}
