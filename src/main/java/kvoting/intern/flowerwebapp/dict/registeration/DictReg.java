package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_DICT_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "DICT")
public class DictReg extends Registration {
    @Embedded
    private DictBase dictBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    private Dict dict;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_DICT_REG_WORD_TC",
            joinColumns = @JoinColumn(name = "DICT_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CC_DICT_REG_DOMAIN_TC",
            joinColumns = @JoinColumn(name = "DICT_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "DOMAIN_ID"))
    private Set<Domain> domains = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;
}
