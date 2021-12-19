package kvoting.intern.flowerwebapp.word;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word implements Item {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "WORD_ID")
    private Long id;

    @Embedded
    WordBase wordBase;

    @Column(name = "WORD_PROC_TPCD")
    private ProcessType status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<WordReg> wordRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    Set<Dict> dicts = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    Set<CommonCodeReg> commonCodeRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    Set<CommonCode> commonCodes = new HashSet<>();
}
