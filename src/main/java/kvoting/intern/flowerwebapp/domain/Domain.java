package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.domain.registeration.DomainReg;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CC_DOMAIN_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Domain extends DomainBase {
    @Id
    @Column(name = "DOMAIN_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<DomainReg> domainRegs = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "CC_DOMAIN_WORD_TC",
            joinColumns = @JoinColumn(name = "DOMAIN_ID"),
            inverseJoinColumns = {
                    @JoinColumn(name = "WORD_ID", referencedColumnName = "WORD_ID"),
                    @JoinColumn(name = "WORD_ENG_NAME", referencedColumnName = "WORD_ENG_NAME"),
                    @JoinColumn(name = "WORD_NAME", referencedColumnName = "WORD_NAME")}
    )
    private List<Word> words = new ArrayList<>();

}
