package kvoting.intern.flowerwebapp.dict.registeration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_DICT_REG_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DictReg {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "DICT_REG_ID")
    private Long id;

    @Embedded
    private DictBase dictBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    private Dict dict;

    @Embedded
    private Registration registration;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "REGT_ID", referencedColumnName = "USER_ID"),
            @JoinColumn(name = "REGT_NM", referencedColumnName = "USER_NM")})
    private Account registrant;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PROCR_ID", referencedColumnName = "USER_ID"),
            @JoinColumn(name = "PROCR_NM", referencedColumnName = "USER_NM")})
    private Account processor;

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

    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;
}
