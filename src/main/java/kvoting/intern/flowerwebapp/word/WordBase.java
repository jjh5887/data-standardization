package kvoting.intern.flowerwebapp.word;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "영어명(약어)")
    private String engName;
    @Column(name = "WORD_NAME")
    @ApiModelProperty(value = "한글명")
    private String name;
    @Column(name = "WORD_ORG_ENG_NAME")
    @ApiModelProperty(value = "원문영어명")
    private String orgEngName;
}
