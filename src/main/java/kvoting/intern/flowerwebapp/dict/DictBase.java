package kvoting.intern.flowerwebapp.dict;

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
public class DictBase {
    @Column(name = "DICT_NAME")
    private String name;
    @Column(name = "DICT_ENG_NAME")
    private String engName;
    @Column(name = "DICT_SCR_NAME")
    private String screenName;
    @Column(name = "DICT_CMCD_YN")
    private Boolean isCommon;
    @Column(name = "DICT_CASE")
    private CaseStyle caseStyle;
    @Column(name = "DICT_DESC_CONT")
    private String description;
}
