package kvoting.intern.flowerwebapp.cmcd;


import com.fasterxml.jackson.annotation.*;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.view.View;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_CMCD_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonView(View.Public.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"CMCD_CD_VALUE", "DICT_ID"}),
        @UniqueConstraint(columnNames = {"CMCD_CD_ORDER", "DICT_ID"})
})
public class CommonCode implements Item {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "CMCD_ID")
    private Long id;

    @Embedded
    private CommonCodeBase base;

    @Column(name = "STDZ_PROC_TPCD")
    private ProcessType status;

    @Column(name = "MODFR_ID", insertable = false, updatable = false)
    private Long modifierId;

    @Column(name = "MODFR_NM")
    private String modifierName;

    @Column(name = "MODF_TM")
    private LocalDateTime modifiedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODFR_ID", referencedColumnName = "USER_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Account modifier;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Set<CommonCodeReg> regs = new HashSet<>();

    @Column(name = "DICT_ID", insertable = false, updatable = false)
    private Long dictId;

    @Column(name = "DICT_NM")
    private String dictName;

    @Column(name = "HIGH_DICT_ID", insertable = false, updatable = false)
    private Long highDictId;

    @Column(name = "HIGH_DICT_NM")
    private String highDictName;

    @Column(name = "HIGH_CMCD_ID", insertable = false, updatable = false)
    private Long highCmcdId;

    @Column(name = "HIGH_CMCD_NM")
    private String highCmcdName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Dict dict;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HIGH_DICT_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Dict highDict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HIGH_CMCD_ID")
    @JsonIgnore
    private CommonCode highCommonCode;

    @OneToMany(mappedBy = "highCommonCode", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonView(View.Detail.class)
    @JsonIgnoreProperties({"lowCommonCodes"})
    private List<CommonCode> lowCommonCodes = new ArrayList<>();

    @Override
    public String getName() {
        return base.getCodeName();
    }
}
