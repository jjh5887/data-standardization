package kvoting.intern.flowerwebapp.domain.registration;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CC_DOMAIN_REG_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainReg {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "DOMAIN_REG_ID")
    private Long id;

    @Embedded
    private DomainBase domainBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    private Domain domain;

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
    @JoinTable(name = "CC_DOMAIN_REG_WORD_TC",
            joinColumns = @JoinColumn(name = "DOMAIN_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words = new ArrayList<>();

    @PrePersist
    public void setUp() {
        if (words == null)
            return;
        String name = "";
        String engName = "";
        for (Word word : words) {
            name += word.getWordBase().getName() + "_";
            engName += word.getWordBase().getEngName() + "_";
        }
        this.domainBase.setName(name.substring(0, name.length() - 1));
        this.domainBase.setEngName(engName.substring(0, engName.length() - 1));
    }
}