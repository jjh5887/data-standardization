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
}


