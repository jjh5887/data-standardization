package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CC_DOMAIN_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainReg extends DomainBase {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "DOMAIN_REG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;

    @ManyToMany
    @JoinTable(
            name = "CC_DOMAIN_REG_WORD_TC",
            joinColumns = @JoinColumn(name = "DOMAIN_REG_ID")
            ,
            inverseJoinColumns = {
                    @JoinColumn(name = "WORD_ENG_NAME", referencedColumnName = "WORD_ENG_NAME"),
                    @JoinColumn(name = "WORD_NAME", referencedColumnName = "WORD_NAME")}
    )
    private List<Word> words = new ArrayList<>();

    @Embedded
    private Registration registration;


}