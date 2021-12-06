package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import lombok.*;

import javax.persistence.*;

@Entity(name = "CC_WORD_REG_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WordReg {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "WORD_REG_ID")
    private Long id;

    @Embedded
    private WordBase regWord;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "WORD_ID")
    private Word word;

    @Embedded
    private Registration registration;
}
