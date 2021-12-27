package kvoting.intern.flowerwebapp.cmcd;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeBase {
    @Column(name = "CMCD_CD_VALUE")
    @ApiModelProperty(value = "코드 값")
    private String code;
    @ApiModelProperty(value = "코드 이름")
    @Column(name = "CMCD_CD_NAME")
    private String codeName;
    @ApiModelProperty(value = "코드 설명")
    @Column(name = "CMCD_CD_DESC_CONT")
    private String description;
    @ApiModelProperty(value = "코드 순서")
    @Column(name = "CMCD_CD_ORDER")
    private Long order;
}
