package kvoting.intern.flowerwebapp.dict;

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
public class DictBase {
    @Column(name = "DICT_NAME")
    @ApiModelProperty(hidden = true)
    private String name;

    @Column(name = "DICT_ENG_NAME")
    @ApiModelProperty(hidden = true)
    private String engName;

    @ApiModelProperty(value = "표기 방식")
    @Column(name = "DICT_CASE")
    private CaseStyle caseStyle;

    @Column(name = "DICT_SCR_NAME")
    @ApiModelProperty(value = "화면에 표시되는 이름")
    private String screenName;

    @ApiModelProperty(value = "설명")
    @Column(name = "DICT_DESC_CONT")
    private String description;

    @ApiModelProperty(value = "공통코드 여부")
    @Column(name = "DICT_CMCD_YN")
    private Boolean isCommon;
}
