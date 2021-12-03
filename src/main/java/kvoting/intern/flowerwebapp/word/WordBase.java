package kvoting.intern.flowerwebapp.word;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WordBase {
    @Column(name = "WORD_ENG_NAME")
    private String engName;
    @Column(name = "WORD_NAME")
    private String name;
    @Column(name = "WORD_ORG_ENG_NAME")
    private String orgEngName;
}
