package kvoting.intern.flowerwebapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.view.View;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_DOMAIN_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonView(View.Public.class)
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"DOMAIN_NAME", "DB_TPCD", "DATA_TPCD"}))
public class Domain implements Item {

    @Id
    @Column(name = "DOMAIN_ID")
    @EqualsAndHashCode.Include
    @GeneratedValue
    private Long id;

    @Embedded
    private DomainBase base;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_DOMAIN_WORD_TC",
            joinColumns = @JoinColumn(name = "DOMAIN_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private List<Word> words = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonView(View.Detail.class)
    private Set<DomainReg> regs = new HashSet<>();

    @ManyToMany(mappedBy = "domains", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    Set<DictReg> dictRegs = new HashSet<>();

    @ManyToMany(mappedBy = "domains", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Set<Dict> dicts = new HashSet<>();

    @Override
    public String getName() {
        return base.getName();
    }

    @PrePersist
    public void setUp() {
        StringBuilder name = new StringBuilder();
        for (Word word : words) {
            name.append(word.getBase().getName()).append(" ");
        }
        if (base.getSize() != null) {
            name.append("L").append(base.getSize());
            if (base.getScale() != null && base.getScale() != 0) {
                name.append(".").append(base.getScale());
            }
            name.append(" ");
        }
        if (base.getNullable() != null) {
            if (base.getNullable()) {
                name.append("NY ");
            } else {
                name.append("NN ");
            }
        }
        base.setName(name.substring(0, name.length() - 1));
    }
}
