package kvoting.intern.flowerwebapp.word.registration;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import kvoting.intern.flowerwebapp.item.registration.Registration;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class WordReg extends Registration<Word, WordBase> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORD_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonIgnore
    private Word item;

    @Column(name = "WORD_ID", insertable = false, updatable = false)
    private Long ItemId;

    @Embedded
    WordBase base;
}
