package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    private Dict dict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;

    @OneToOne
    @JoinColumn(name = "HIGH_CMCD_ID")
    private CommonCode highCommonCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_CMCD_REG_WORD_TC",
            joinColumns = @JoinColumn(name = "CMCD_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words;
}
