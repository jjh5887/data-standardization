package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CC_CMCD_REG_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommonCodeReg {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "CMCD_REG_ID")
    private Long id;

    @Embedded
    private CommonCodeBase commonCodeBase;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    private Dict dict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_CMCD_REG_WORD_TC",
            joinColumns = @JoinColumn(name = "CMCD_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words;

    @PrePersist
    public void setUp() {
        if (words == null) return;
        String name = "";
        for (Word word : words) {
            name += word.getWordBase().getName() + " ";
        }
        this.commonCodeBase.setCodeName(name.substring(0, name.length() - 1));
    }
}
