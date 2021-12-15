package kvoting.intern.flowerwebapp.cmcd.registration;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "CC_CMCD_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "CMCD")
public class CommonCodeReg extends Registration {
    @Embedded
    private CommonCodeBase commonCodeBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMCD_ID")
    private CommonCode commonCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ID")
    private Dict dict;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    @JoinTable(name = "CC_CMCD_REG_WORD_TC",
            joinColumns = @JoinColumn(name = "CMCD_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words;

    @Override
    public void registered() {
        if (getDateRegistered() == null) {
            setDateRegistered(LocalDateTime.now());
        }

        if (words == null) return;

        String name = "";
        for (Word word : words) {
            name += word.getWordBase().getName() + " ";
        }
        this.commonCodeBase.setCodeName(name.substring(0, name.length() - 1));
    }
}
