package kvoting.intern.flowerwebapp.word;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private WordBase base;

    @Column(name = "WORD_PROC_TPCD")
    private ProcessType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODFR_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Account modifier;

    @Column(name = "MODFR_ID", insertable = false, updatable = false)
    private Long modifierId;

    @Column(name = "MODFR_NM")
    private String modifierName;

    @Column(name = "MODF_TM")
    private LocalDateTime modifiedTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Set<WordReg> wordRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    Set<Dict> dicts = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    Set<CommonCodeReg> commonCodeRegs = new HashSet<>();

    @ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    Set<CommonCode> commonCodes = new HashSet<>();

    @Override
    public String getName() {
        return base.getEngName();
    }
}
