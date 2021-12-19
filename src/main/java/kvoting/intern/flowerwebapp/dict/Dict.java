package kvoting.intern.flowerwebapp.dict;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dict implements Item {

    @Embedded
    DictBase dictBase;
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    Set<DictReg> dictRegs = new HashSet<>();
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "DICT_ID")
    private Long id;
    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_DICT_WORD_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private List<Word> words = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CC_DICT_DOMAIN_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "DOMAIN_ID"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<Domain> domains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CC_DICT_CUSTOM_DOMAIN_TC",
            joinColumns = @JoinColumn(name = "DICT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_ID"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    private Set<CustomDomain> customDomains = new HashSet<>();

    @OneToOne(mappedBy = "dict", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
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