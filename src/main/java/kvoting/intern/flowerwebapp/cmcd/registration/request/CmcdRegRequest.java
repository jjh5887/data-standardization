package kvoting.intern.flowerwebapp.cmcd.registration.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "공통코드 요청")
public class CmcdRegRequest extends RegRequest {
    private CommonCodeBase base;
    @ApiModelProperty(value = "용어 id")
    private Long dict;
    @ApiModelProperty(value = "상위 공통코드 id")
    private Long highCommonCode;
    @ApiModelProperty(value = "상위 용어 id")
    private Long highDict;
}
