package kvoting.intern.flowerwebapp.cmcd;

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
public class CommonCodeBase {
    @Column(name = "CMCD_CD_VALUE")
    private String code;
    @Column(name = "CMCD_CD_NAME")
    private String codeName;
    @Column(name = "CMCD_SCR_ORDR")
    private int order;
    @Column(name = "CMCD_CD_DESC_CONT")
    private String description;
}
