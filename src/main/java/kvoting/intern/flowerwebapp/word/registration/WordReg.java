package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity(name = "CC_WORD_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "WORD")
public class WordReg extends Registration {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORD_ID")
    private Word word;

    @Embedded
    WordBase wordBase;
}
