package kvoting.intern.flowerwebapp.dict;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.registration.ProcessType;
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

    @Embedded
    DictBase dictBase;

    @OneToMany(mappedBy = "dict", cascade = CascadeType.REMOVE)
    Set<DictReg> dictRegs = new HashSet<>();

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    @JoinTable(name = "CC_DICT_WORD_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "CC_DICT_DOMAIN_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "DOMAIN_ID"))
    private Set<Domain> domains = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;

    @PrePersist
    public void setUp() {
        switch (getDictBase().getCaseStyle()) {
        case SNAKE:
            makeSnakeName();
            break;
        case CAMEL:
            makeCamelName();
            break;
        default:
            throw new RuntimeException();
        }
    }

    public void makeSnakeName() {
        String name = "";
        String engName = "";
        for (Word word : words) {
            name += word.getWordBase().getName() + " ";
            engName += word.getWordBase().getEngName() + "_";
        }
        this.dictBase.setName(name.substring(0, name.length() - 1));
        this.dictBase.setEngName(engName.substring(0, engName.length() - 1).toUpperCase());
    }

    public void makeCamelName() {
        String name = "";
        String engName = "";
        for (int i = 0; i < words.size(); i++) {
            String nameBuf = words.get(i).getWordBase().getName() + " ";
            String engNameBuf = words.get(i).getWordBase().getEngName().toLowerCase();
            if (i > 0) {
                char c2 = Character.toUpperCase(engNameBuf.charAt(0));
                engNameBuf = c2 + engNameBuf.substring(1);
            }
            name += nameBuf;
            engName += engNameBuf;
        }
        this.dictBase.setName(name);
        this.dictBase.setEngName(engName);
    }
}