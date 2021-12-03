package kvoting.intern.flowerwebapp.word;


import kvoting.intern.flowerwebapp.type.ProcessType;
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
    @Column(name = "WORD_ENG_NAME", unique = true)
    private String engName;
    @Column(name = "WORD_NAME")
    private String name;
    @Column(name = "WORD_ORG_ENG_NAME")
    private String orgEngName;
    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<WordReg> wordRegs = new HashSet<>();

    public void addWordReg(WordReg wordReg) {
        wordRegs.add(wordReg);
    }
}