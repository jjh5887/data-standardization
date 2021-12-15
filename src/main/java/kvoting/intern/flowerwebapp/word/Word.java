package kvoting.intern.flowerwebapp.word;


import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CC_WORD_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Word {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "WORD_ID")
    private Long id;

    @Embedded
    WordBase wordBase;

    @Column(name = "WORD_PROC_TPCD")
    private ProcessType status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "word", cascade = CascadeType.REMOVE)
    private Set<WordReg> wordRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    Set<Dict> dicts = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    Set<CommonCodeReg> commonCodeRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    Set<CommonCode> commonCodes = new HashSet<>();
}
